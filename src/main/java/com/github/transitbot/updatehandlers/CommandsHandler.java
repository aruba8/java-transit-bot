package com.github.transitbot.updatehandlers;

import com.github.transitbot.actions.TripPlannerAction;
import com.github.transitbot.api.models.BusSchedule;
import com.github.transitbot.api.models.Coordinates;
import com.github.transitbot.api.models.Route;
import com.github.transitbot.api.services.*;
import com.github.transitbot.api.services.exceptions.StopNotFoundException;
import com.github.transitbot.commands.HelpCommand;
import com.github.transitbot.commands.StartCommand;
import com.github.transitbot.dao.DBService;
import com.github.transitbot.dao.models.ChatState;
import com.github.transitbot.dao.models.ChatStateEnum;
import com.github.transitbot.dao.models.TripData;
import com.github.transitbot.utils.ConfigReader;
import com.github.transitbot.utils.Emoji;
import com.github.transitbot.utils.TemplateUtility;
import org.json.JSONObject;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Handler for commands.
 */
public class CommandsHandler extends TelegramLongPollingCommandBot {

    /**
     * tag for logs.
     */
    private static final String LOGTAG = "COMMANDSHANDLER";

    /**
     * amount of schedules in list.
     */
    private final int amountOfSchedulesInList = 5;


    /**
     * Constructor.
     */
    public CommandsHandler() {
        StartCommand startCommand = new StartCommand();
        register(startCommand);

        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);

        registerDefaultAction((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId().toString());
            commandUnknownMessage.setText("The command '" + message.getText()
                    + "' is not known by this bot. Here comes some help " + Emoji.AMBULANCE);
            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                BotLogger.error(LOGTAG, e);
            }
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[]{});
        });
    }

    public Long getChatIdFromUpdate(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Message message = update.getMessage();
        Long chatId = null;
        if (callbackQuery != null) {
            chatId = callbackQuery.getMessage().getChatId();
        }
        if (message != null) {
            chatId = message.getChatId();
        }
        return chatId;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        BotLogger.info(LOGTAG, "##### update id: " + update.getUpdateId());
        TripPlannerAction tripPlannerAction = new TripPlannerAction();
        ChatState chatState;
        try {
            Long chatId = getChatIdFromUpdate(update);
            chatState = DBService.getChatStateByChatId(chatId);
            Message message = update.getMessage();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            switch (chatState.getChatState()) {
                case INITIAL_STATE:
                    switch (message.getText()) {
                        case "Get schedule by stop number":
                            chatState.setChatState(ChatStateEnum.BUS_SCHEDULE_STATE);
                            DBService.updateChatState(chatState);
                            enterStopNumberMessage(message);
                            return;
                        case "Use trip planner":
                            chatState.setChatState(ChatStateEnum.TRIP_PLANNER_STATE_0);
                            DBService.updateChatState(chatState);
                            tripPlannerAction.enterDestinationMessage(message, this);
                            return;
                        default:
                    }
                    showInitialKeyboard(chatId);
                    break;
                case BUS_SCHEDULE_STATE:
                    if (validateStopNumber(message.getText())) {
                        showAnswerOnStopNumber(message);
                        chatState.setChatState(ChatStateEnum.SHOW_BUS_SCHEDULE_STATE);
                        DBService.updateChatState(chatState);
                    } else {
                        handleWrongMessage(message);
                    }
                    break;
                case SHOW_BUS_SCHEDULE_STATE:
                    String callBackData = callbackQuery.getData();
                    String[] callBackDataArray = callBackData.split("#");
                    String callbackButtonCode = callBackDataArray[0];
                    String value = callBackDataArray[1];
                    if (callbackButtonCode.equals("1")) {
                        showInfo(callbackQuery, value);
                        break;
                    } else if (callbackButtonCode.equals("2")) {
                        showSchedule(callbackQuery, value);
                        break;
                    } else {
                        chatState.setChatState(ChatStateEnum.INITIAL_STATE);
                        DBService.updateChatState(chatState);
                        showInitialKeyboard(chatId);
                    }
                    break;
                case TRIP_PLANNER_STATE_0:
                    String destAddress = message.getText();
                    GoogleGeoApiService googleGeoApiService = new GoogleGeoApiService();
                    LocationsService locationsService = new LocationsService();
                    Coordinates coordinates = googleGeoApiService.getCoordinatesByAddress(destAddress);
                    Set<JSONObject> addressSet = locationsService.getAllLocations(locationsService.getLocationsByCoordinates(coordinates));
                    if (addressSet.size() == 0) {
                        break;
                    }
                    InlineKeyboardMarkup markup = createKeyboard(addressSet);
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setReplyMarkup(markup);
                    sendMessage.setText("Select the nearest place or intersection:");
                    sendMessage.setChatId(chatId.toString());
                    sendMessage(sendMessage);
                    chatState.setChatState(ChatStateEnum.TRIP_PLANNER_STATE_1);
                    DBService.updateChatState(chatState);
                    BotLogger.info(LOGTAG, "Chat state: " + chatState);
                    break;
                case TRIP_PLANNER_STATE_1:
                    String data = callbackQuery.getData();
                    String[] dataArray = data.split("#");
                    String destinationKey = dataArray[1];
                    String locationType = dataArray[2];
                    TripData tripData = new TripData(chatId, destinationKey, locationType, null, null);
                    DBService.saveTripData(tripData);
                    chatState.setChatState(ChatStateEnum.TRIP_PLANNER_STATE_2);
                    DBService.updateChatState(chatState);
                    break;
                case TRIP_PLANNER_STATE_2:
                    TripData tripData1 = DBService.getTripData(chatId);
                    System.out.println();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return ConfigReader.getPropValues().getProperty("bot.token");
    }

    /**
     * validate if string has only stop number.
     *
     * @param string text
     * @return true or false
     */
    private boolean validateStopNumber(String string) {
        String pattern = "^[0-9]{5}$";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(string);
        return matcher.matches();
    }

    /**
     * handles wrong message.
     *
     * @param message I don't understand you
     */
    private void handleWrongMessage(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText("I don't understand you " + Emoji.UNAMUSED_FACE);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * shows schedule of stop.
     *
     * @param callbackQuery callbackQuery
     * @param stopNumber    stopNumber
     * @throws TelegramApiException TelegramApiException
     */
    private void showSchedule(CallbackQuery callbackQuery, String stopNumber) throws TelegramApiException {
        InlineKeyboardMarkup keyboard = createKeyboard(stopNumber);
        Message message = callbackQuery.getMessage();
        String chatId = message.getChatId().toString();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(message.getMessageId());
        BusScheduleService service = new BusScheduleService();
        List<BusSchedule> busSchedules = service.getBusSchedulesByStopNumber(stopNumber);
        TemplateUtility templateUtility = new TemplateUtility();
        int normalAmountOfschedules = amountOfSchedulesInList;
        if (busSchedules.size() < amountOfSchedulesInList) {
            normalAmountOfschedules = busSchedules.size();
        }
        Map data = new HashMap<>();
        data.put("busses", busSchedules.subList(0, normalAmountOfschedules));
        data.put("stopNumber", stopNumber);
        data.put("busStopEmoji", Emoji.BUS_STOP);
        data.put("busEmoji", "" + Emoji.BUS_ONCOMING);
        String text = templateUtility.renderTemplate("schedule.ftlh", "data", data);
        editMessageText.setText(text);
        editMessageText.setReplyMarkup(keyboard);
        editMessageText.enableMarkdown(true);
        editMessageText(editMessageText);
    }

    /**
     * show info about stop.
     *
     * @param callbackQuery callbackQuery
     * @param stopNumber    stopNumber
     * @throws TelegramApiException TelegramApiException
     */
    private void showInfo(CallbackQuery callbackQuery, String stopNumber) throws TelegramApiException {
        InlineKeyboardMarkup keyboard = createKeyboard(stopNumber);
        Message message = callbackQuery.getMessage();
        String chatId = message.getChatId().toString();
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(message.getMessageId());
        RoutesService service = new RoutesService();
        List<Route> routeList = service.getRoutesByStopNumber(stopNumber);
        Map data = new HashMap<>();
        data.put("routes", routeList);
        data.put("stopNumber", stopNumber);
        data.put("busEmoji", "" + Emoji.BUS);
        data.put("busStopEmoji", Emoji.BUS_STOP);
        TemplateUtility templateUtility = new TemplateUtility();
        editMessageText.setText(templateUtility.renderTemplate(
                "route.ftlh", "data", data));
        editMessageText.setReplyMarkup(keyboard);
        editMessageText.enableMarkdown(true);
        editMessageText(editMessageText);
    }

    /**
     * initial answer on stop number.
     *
     * @param message message
     * @throws TelegramApiException TelegramApiException
     */
    private void showAnswerOnStopNumber(Message message) throws TelegramApiException {
        String stopNumber = message.getText();
        StopService service = new StopService();
        String stopName = null;
        SendMessage sendMessage = new SendMessage();
        try {
            stopName = service.getStopNameByStopNumber(stopNumber);
            InlineKeyboardMarkup keyboard = createKeyboard(stopNumber);
            sendMessage.setReplyMarkup(keyboard);
            Map root = new HashMap<>();
            root.put("stopNumber", stopNumber);
            root.put("stopName", stopName);
            TemplateUtility utility = new TemplateUtility();
            sendMessage.setText(utility.renderTemplate("info.ftlh", "data", root));
            sendMessage.enableMarkdown(true);
        } catch (StopNotFoundException e) {
            sendMessage.setText(e.getMessage());
        }
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage(sendMessage);
    }

    /**
     * show enter stop number message.
     *
     * @param message message
     * @throws TelegramApiException TelegramApiException
     */
    public void enterStopNumberMessage(Message message) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Enter stop number: ");
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage(sendMessage);
    }

    /**
     * creates and show initial keyboard.
     *
     * @param chatId callbackQuery
     */
    public void showInitialKeyboard(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setOneTimeKeyboad(true);
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("Get schedule by stop number");
        // Add the first row to the keyboard
        keyboard.add(row);
        // Create another keyboard row
        row = new KeyboardRow();
        // Set each button for the second line
        row.add("Use trip planner");
        // Add the second row to the keyboard
        keyboard.add(row);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        // Add it to the message
        message.setReplyMarkup(keyboardMarkup);
        message.setText("What are we going to do?");
        try {
            sendMessage(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    /**
     * Create inline keyboard.
     *
     * @param stopNumber stopNumber
     * @return InlineKeyboardMarkup InlineKeyboardMarkup
     */
    private InlineKeyboardMarkup createKeyboard(String stopNumber) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyBoard = new ArrayList<>();
        List<InlineKeyboardButton> internalKeyboard = new ArrayList<>();
        InlineKeyboardButton infoButton = new InlineKeyboardButton();
        InlineKeyboardButton scheduleButton = new InlineKeyboardButton();
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        infoButton.setText("Info").setCallbackData("1#" + stopNumber);
        scheduleButton.setText("Schedules").setCallbackData("2#" + stopNumber);
        backButton.setText("Back").setCallbackData("3#" + stopNumber);
        internalKeyboard.add(infoButton);
        internalKeyboard.add(scheduleButton);
        internalKeyboard.add(backButton);
        keyBoard.add(internalKeyboard);
        markup.setKeyboard(keyBoard);
        return markup;
    }

    /**
     * Create inline keyboard.
     *
     * @param addresses stopNumber
     * @return InlineKeyboardMarkup InlineKeyboardMarkup
     */
    private InlineKeyboardMarkup createKeyboard(Set<JSONObject> addresses) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyBoard = new ArrayList<>();
        for (JSONObject address : addresses) {
            List<InlineKeyboardButton> internalKeyboard = new ArrayList<>();
            InlineKeyboardButton addressButton = new InlineKeyboardButton();
            String buttonName = "XXX";
            String locationType = address.getString("type");
            switch (locationType) {
                case "monument":
                    buttonName = address.getString("name");
                    break;
                case "address":
                    buttonName = address.getInt("street-number") + " " + address.getJSONObject("street").getString("name");
                    break;
                case "intersection":
                    buttonName = "'" + address.getJSONObject("cross-street").getString("name") + "' x '" + address.getJSONObject("street").getString("name") + "'";
                    break;
            }
            addressButton.setText(buttonName);

            Object object = address.get("key");
            if (object instanceof Integer) {
                addressButton.setCallbackData("key#" + address.getInt("key") + "#" + locationType);
            } else if (object instanceof String) {
                addressButton.setCallbackData("key#" + address.getString("key") + "#" + locationType);
            }
            internalKeyboard.add(addressButton);
            keyBoard.add(internalKeyboard);
        }
        markup.setKeyboard(keyBoard);
        return markup;
    }

}
