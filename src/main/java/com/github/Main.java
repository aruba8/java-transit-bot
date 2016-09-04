package com.github;


import com.github.transitbot.updatehandlers.CommandsHandler;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * Entry point.
 */
public class Main {

    /**
     * Main method to start bot.
     *
     * @param args args
     */
    public static void main(String[] args) {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new CommandsHandler());

        } catch (TelegramApiException e) {
            BotLogger.error("Main mr", e);
        }


    }
}
