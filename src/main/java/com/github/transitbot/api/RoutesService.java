package com.github.transitbot.api;

import com.github.transitbot.utils.RequestSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * class to work with routes api.
 */
public class RoutesService {

    /**
     * path.
     */
    private String routesPath = "routes";

    /**
     * get json by routeNumber.
     *
     * @param routeNumber routeNumber
     * @return json
     */
    public HttpResponse<JsonNode> getInfoByRouteNumber(String routeNumber) {
        return RequestSender.sendRequest(routesPath + "/" + routeNumber, null);
    }

    /**
     * get routes by stop number.
     * @param stopNumber stopNumber
     * @return json
     */
    public HttpResponse<JsonNode> getRoutesByStopNumber(String stopNumber) {
        Map<String, String> params = new HashMap<>();
        params.put("stop", stopNumber);
        return RequestSender.sendRequest(routesPath, params);
    }

}
