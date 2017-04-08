package com.github.transitbot.utils;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.util.Map;

/**
 * Class to send requests to google maps api.
 */
public class GoogleApiRequestSender {

    /**
     * logger.
     */
    private static final String LOG = GoogleApiRequestSender.class.getName();

    /**
     * sender.
     *
     * @param apiUrl apiUrl
     * @param apiKey apiKey
     * @param params map of params
     * @return response
     * @throws UnirestException exception
     */
    public HttpResponse<JsonNode> sendRequest(String apiUrl, String apiKey, Map params) throws UnirestException {
        return Unirest.get(apiUrl).queryString("key", apiKey).queryString(params).asJson();
    }


}
