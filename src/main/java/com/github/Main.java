package com.github;


import com.github.transitbot.updatehandlers.CommandsHandler;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.logging.Level;

/**
 * Entry point.
 */
public class Main {

    /**
     * Log tag.
     */
    private static final String LOG = Main.class.getName();

    /**
     * Main method to start bot.
     *
     * @param args args
     */
    public static void main(String[] args) {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            BotLogger.setLevel(Level.ALL);
            telegramBotsApi.registerBot(new CommandsHandler());
            BotLogger.info(LOG, "CommandsHandler registered");

        } catch (Exception e) {
            BotLogger.error(LOG, e);
        }


    }
}
