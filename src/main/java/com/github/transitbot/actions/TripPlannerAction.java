package com.github.transitbot.actions;

import com.github.transitbot.updatehandlers.CommandsHandler;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;

/**
 * Class to dedicate actions regarding trip planner.
 */
public class TripPlannerAction {

    /**
     * enterDestinationMessage.
     *
     * @param message         message
     * @param commandsHandler this
     * @throws TelegramApiException TelegramApiException
     */
    public void enterDestinationMessage(Message message, CommandsHandler commandsHandler) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Enter destination address:");
        sendMessage.setChatId(message.getChatId().toString());
        commandsHandler.sendMessage(sendMessage);
    }

}
