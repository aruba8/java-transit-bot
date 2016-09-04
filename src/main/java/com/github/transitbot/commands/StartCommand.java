package com.github.transitbot.commands;

import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * Start command class.
 */
public class StartCommand extends BotCommand {

    /**
     * tag for logs.
     */
    private static final String LOGTAG = "STARTCOMMAND";

    /**
     * Construct a command.
     */
    public StartCommand() {
        super("start", "With this command you can start the Bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        StringBuilder messageBuilder = new StringBuilder();
        String lastName = user.getLastName() == null ? "" : user.getLastName();
        String userName = user.getFirstName() + " " + lastName;
        messageBuilder.append("Hi ").append(userName).append("\n");
        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(messageBuilder.toString());

        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
