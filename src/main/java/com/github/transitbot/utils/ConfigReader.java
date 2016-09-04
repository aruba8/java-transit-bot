package com.github.transitbot.utils;

import java.io.FileReader;
import java.util.Properties;

/**
 * Config reader.
 */
public class ConfigReader {

    /**
     * Returns loaded properties.
     *
     * @return properties
     */
    public static Properties getPropValues() {

        Properties prop = new Properties();
        try {
            String propFileName = "config.properties";
            prop.load(new FileReader(propFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prop;
    }
}
