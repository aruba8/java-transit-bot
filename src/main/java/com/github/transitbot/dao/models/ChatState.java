package com.github.transitbot.dao.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * ChatState model.
 */
@DatabaseTable(tableName = "chat_state")
public class ChatState {

    /**
     * chatId.
     */
    @DatabaseField(id = true)
    private Long chatId;
    /**
     * chatState.
     */
    @DatabaseField
    private int chatState;

    /**
     * default.
     */
    public ChatState() {
    }

    /**
     * ChatState constructor.
     *
     * @param chatId    chatId
     * @param chatState chatState
     */
    public ChatState(Long chatId, int chatState) {
        this.chatId = chatId;
        this.chatState = chatState;
    }

    /**
     * getter.
     *
     * @return chatId
     */
    public Long getChatId() {
        return chatId;
    }

    /**
     * setter.
     *
     * @param chatId chatId
     */
    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    /**
     * getter.
     *
     * @return chatState
     */
    public int getChatState() {
        return chatState;
    }

    /**
     * setter.
     *
     * @param chatState chatState
     */
    public void setChatState(int chatState) {
        this.chatState = chatState;
    }

    /**
     * to string.
     *
     * @return string
     */
    public String toString() {
        return "Chat id: " + getChatId() + ", state: " + getChatState();
    }
}
