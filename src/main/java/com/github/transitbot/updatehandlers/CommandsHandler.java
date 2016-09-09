package com.github.transitbot.updatehandlers;

import com.github.transitbot.api.models.BusSchedule;
import com.github.transitbot.api.models.Route;
import com.github.transitbot.api.services.BusScheduleService;
import com.github.transitbot.api.services.RoutesService;
import com.github.transitbot.commands.HelpCommand;
import com.github.transitbot.commands.StartCommand;
import com.github.transitbot.utils.ConfigReader;
import com.github.transitbot.utils.Emoji;
import com.github.transitbot.utils.TemplateUtility;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        register(new StartCommand());

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

    @Override
    public void processNonCommandUpdate(Update update) {
        try {
            Message message = update.getMessage();
            CallbackQuery callbackQuery = update.getCallbackQuery();
            if (callbackQuery == null && message != null && message.hasText()) {
                if (validateStopNumber(message.getText())) {
                    showAnswerOnStopNumber(message);
                } else {
                    handleWrongMessage(message);
                }
            } else if (callbackQuery != null) {
                String callBackData = callbackQuery.getData();
                String[] callBackDataArray = callBackData.split(":");
                String callbackButtonCode = callBackDataArray[0];
                String stopNumber = callBackDataArray[1];
                switch (callbackButtonCode) {
                    case "1":
                        showInfo(callbackQuery, stopNumber);
                        break;
                    case "2":
                        showSchedule(callbackQuery, stopNumber);
                        break;
                    default:
                }
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
        editMessageText.setText(templateUtility.renderTemplate(
                "schedule.ftlh", "data", data));
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
        InlineKeyboardMarkup keyboard = createKeyboard(message.getText());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Stop #" + message.getText());
        sendMessage.setReplyMarkup(keyboard);
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage(sendMessage);
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
        infoButton.setText("Info").setCallbackData("1:" + stopNumber);
        scheduleButton.setText("Schedules").setCallbackData("2:" + stopNumber);
        internalKeyboard.add(infoButton);
        internalKeyboard.add(scheduleButton);
        keyBoard.add(internalKeyboard);
        markup.setKeyboard(keyBoard);
        return markup;
    }
}
