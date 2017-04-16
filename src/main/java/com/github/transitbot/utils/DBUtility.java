package com.github.transitbot.utils;

import com.github.transitbot.dao.models.ChatState;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * class to work with db.
 */
public final class DBUtility {

    /**
     * instance.
     */
    private static DBUtility instance = null;

    /**
     * connectionSource.
     */
    private ConnectionSource connectionSource;


    /**
     * DBUtility construct.
     */
    private DBUtility() {
        String databaseUrl = ConfigReader.getPropValues().getProperty("h2.db.url");
        String user = ConfigReader.getPropValues().getProperty("h2.db.user");
        String password = ConfigReader.getPropValues().getProperty("h2.db.password");
        try {
            connectionSource = new JdbcConnectionSource(databaseUrl, user, password);
            TableUtils.createTableIfNotExists(connectionSource, ChatState.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * getter for instance.
     *
     * @return DBUtility instance.
     */
    public static DBUtility getInstance() {
        if (instance == null) {
            synchronized (DBUtility.class) {
                instance = new DBUtility();
            }
        }
        return instance;
    }

    /**
     * get dao.
     *
     * @param t   t.
     * @param <T> t.
     * @return dao
     */
    public <T> Dao getDao(Class<T> t) {
        if (instance == null) {
            instance = getInstance();
        }
        Dao<T, String> dao = null;
        try {
            dao = DaoManager.createDao(connectionSource, t);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dao;
    }
}
