package com.github.transitbot.updatehandlers;

import com.github.transitbot.api.models.BusSchedule;
import com.github.transitbot.api.services.BusScheduleService;
import com.github.transitbot.commands.HelpCommand;
import com.github.transitbot.commands.StartCommand;
import com.github.transitbot.utils.ConfigReader;
import com.github.transitbot.utils.Emoji;
import com.github.transitbot.utils.TemplateUtility;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.List;
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
    private int amountOfSchedulesInList = 5;


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
            if (message != null && message.hasText()) {
                if (validateStopNumber(message.getText())) {
                    BusScheduleService service = new BusScheduleService();
                    List<BusSchedule> busSchedules = service.getBusSchedulesByStopNumber(message.getText());
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(message.getChatId().toString());
                    TemplateUtility templateUtility = new TemplateUtility();

                    if (busSchedules.size() < amountOfSchedulesInList) {
                        amountOfSchedulesInList = busSchedules.size();
                    }
                    sendMessage.setText(templateUtility.renderTemplate(
                            "schedule.ftlh", "busses", busSchedules.subList(0, amountOfSchedulesInList)));
                    sendMessage(sendMessage);
                } else {
                    handleWrongMessage(message);
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
     * handels wrong message.
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
}
