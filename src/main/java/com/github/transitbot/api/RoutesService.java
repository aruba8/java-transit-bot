package com.github.transitbot.api;

import com.github.transitbot.utils.RequestSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

/**
 * class to work with routes api.
 */
public class RoutesService {

    /**
     * path.
     */
    private String routesPath = "routes/";

    /**
     * get json by stopNumber.
     *
     * @param stopNumber stopNumber
     * @return json
     */
    public HttpResponse<JsonNode> getRoutesByStopNumber(String stopNumber) {
        return RequestSender.sendRequest(routesPath + stopNumber);
    }
}
