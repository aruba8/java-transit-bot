package com.github.transitbot.updatehandlers;

import com.github.transitbot.commands.HelpCommand;
import com.github.transitbot.commands.StartCommand;
import com.github.transitbot.utils.ConfigReader;
import com.github.transitbot.utils.Emoji;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.logging.BotLogger;


/**
 * Handler for commands.
 */
public class CommandsHandler extends TelegramLongPollingCommandBot {

    /**
     * tag for logs.
     */
    private static final String LOGTAG = "COMMANDSHANDLER";


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

    }

    @Override
    public String getBotUsername() {
        return null;
    }

    @Override
    public String getBotToken() {
        return ConfigReader.getPropValues().getProperty("bot.token");
    }
}
