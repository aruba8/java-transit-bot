package com.github.transitbot.api.services;

import com.github.transitbot.utils.RequestSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * TripPlannerService.
 */
public class TripPlannerService {

    /**
     * plannerPath.
     */
    private final String plannerPath = "trip-planner";

    /**
     * getPlansByAddressDateAndTime.
     *
     * @param originKey      originKey
     * @param destinationKey destinationKey
     * @param dateTime       dateTime
     * @param mode           mode
     * @return HttpResponse
     */
    public HttpResponse<JsonNode> getPlansByAddressDateAndTime(String originKey, String destinationKey,
                                                               LocalDateTime dateTime, String mode) {
        Map<String, String> params = new HashMap<>();
        params.put("origin", "addresses/" + originKey);
        params.put("destination", "addresses/" + destinationKey);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatterFormatter = DateTimeFormatter.ofPattern("HH:MM");
        params.put("date", dateTime.format(dateFormatter));
        params.put("time", dateTime.format(timeFormatterFormatter));
        params.put("mode", mode);
        return RequestSender.sendRequest(plannerPath, params);
    }
}
