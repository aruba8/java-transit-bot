package com.github.transitbot.commands;

import com.github.transitbot.dao.DBService;
import com.github.transitbot.dao.models.ChatState;
import com.github.transitbot.utils.TemplateUtility;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        String lastName = user.getLastName() == null ? "" : user.getLastName();
        String userName = user.getFirstName() + " " + lastName;
        Map root = new HashMap<>();
        root.put("userName", userName);
        TemplateUtility templateUtility = new TemplateUtility();
        SendMessage answer = new SendMessage();
        answer.setChatId(chat.getId().toString());
        answer.setText(templateUtility.renderTemplate("start.ftlh", "data", root));

        Long chatId = chat.getId();
        ChatState state = DBService.getChatStateByChatId(chatId);
        if (state == null) {
            state = new ChatState(chatId, 0);
            DBService.saveChatState(state);
        } else {
            state = new ChatState(chatId, 0);
            DBService.updateChatState(state);
        }

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
        answer.setReplyMarkup(keyboardMarkup);


        try {
            absSender.sendMessage(answer);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}
