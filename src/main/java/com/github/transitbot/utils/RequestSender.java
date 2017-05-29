package com.github.transitbot.utils;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.Map;

/**
 * request sender.
 */
public class RequestSender {
    /**
     * Log tag.
     */
    private static final String LOG = RequestSender.class.getName();

    /**
     * static method to send request.
     *
     * @param path   represents path of url
     * @param params url parameters Map<String, Object>
     * @return HttpResponse object. Json object can be retrieved from it
     */
    public static HttpResponse<JsonNode> sendRequest(String path, Map params) {
        String apiUrl = ConfigReader.getPropValues().getProperty("transit.url");
        String apiKey = ConfigReader.getPropValues().getProperty("transit.key");
        String jsonExtension = ".json";
        HttpResponse<JsonNode> response = null;

        try {
            String fullPath = apiUrl + path;
            BotLogger.info(LOG, "Full path: " + fullPath);
            response = Unirest.get(fullPath + jsonExtension)
                    .queryString(params)
                    .queryString("api-key", apiKey).asJson();
            BotLogger.info(LOG, "Sent request: " + fullPath);
            BotLogger.info(LOG, "Params: " + params);
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;

    }

}
