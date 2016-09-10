package com.github.transitbot.api.services;

import com.github.transitbot.utils.RequestSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

public class StopService {

    /**
     * path.
     */
    private String stopsPath = "stops/";


    /**
     * get json by routeNumber.
     *
     * @param stopNumber stopNumber
     * @return json
     */
    private HttpResponse<JsonNode> getDataByStopNumber(String stopNumber) {
        return RequestSender.sendRequest(stopsPath + stopNumber, null);
    }

    /**
     * get stop name.
     *
     * @param stopNumber stopNumber
     * @return stop name
     */
    public String getStopNameByStopNuber(String stopNumber) {
        return getDataByStopNumber(stopNumber).getBody().getObject().getJSONObject("stop").getString("name");
    }


}
