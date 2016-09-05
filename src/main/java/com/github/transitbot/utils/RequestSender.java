package com.github.transitbot.utils;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

/**
 * request sender.
 */
public class RequestSender {

    /**
     * static method to send request.
     *
     * @param path represents path of url
     * @param params url parameters Map<String, Object>
     * @return HttpResponse object. Json object can be retrieved from it
     */
    public static HttpResponse<JsonNode> sendRequest(String path, Map params) {
        String apiUrl = ConfigReader.getPropValues().getProperty("transit.url");
        String apiKey = ConfigReader.getPropValues().getProperty("transit.key");
        String jsonExtension = ".json";
        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(apiUrl + path + jsonExtension).queryString(params)
                    .queryString("api-key", apiKey).asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return response;

    }

}
