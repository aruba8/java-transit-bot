package com.github.transitbot.dao;

import com.github.transitbot.dao.models.ChatState;
import com.github.transitbot.dao.models.TripData;
import com.github.transitbot.utils.DBUtility;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * class to handle with DB.
 */
public class DBService {

    /**
     * getting chatState by charId.
     *
     * @param chatId chatId
     * @return chatState
     */
    public static ChatState getChatStateByChatId(Long chatId) {
        ChatState chatState = null;
        try {
            Dao<ChatState, String> dao = DBUtility.getInstance().getDao(ChatState.class);
            chatState = dao.queryForId(chatId.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return chatState;
    }

    /**
     * save chatState.
     *
     * @param state ChatState
     */
    public static void saveChatState(ChatState state) {
        try {
            Dao<ChatState, String> dao = DBUtility.getInstance().getDao(ChatState.class);
            dao.create(state);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * update chatState.
     *
     * @param state state
     */
    public static void updateChatState(ChatState state) {
        try {
            Dao<ChatState, String> dao = DBUtility.getInstance().getDao(ChatState.class);
            dao.update(state);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveTripData(TripData tripData) {
        try {
            Dao<TripData, String> dao = DBUtility.getInstance().getDao(TripData.class);
            dao.create(tripData);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void updateTripData(TripData tripData) {
        try {
            Dao<TripData, String> dao = DBUtility.getInstance().getDao(TripData.class);
            dao.update(tripData);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static TripData getTripData(Long chatId) {
        TripData tripData = null;
        try {
            Dao<TripData, String> dao = DBUtility.getInstance().getDao(TripData.class);
            tripData = dao.queryForId(chatId.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tripData;
    }

    public static void clearTripData(Long chatId) {
        try {
            Dao<TripData, String> dao = DBUtility.getInstance().getDao(TripData.class);
            dao.deleteById(chatId.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
