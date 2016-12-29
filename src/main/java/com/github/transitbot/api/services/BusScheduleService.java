package com.github.transitbot.api.services;

import com.github.transitbot.api.models.BusSchedule;
import com.github.transitbot.utils.RequestSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Service to work with schedule.
 */
public class BusScheduleService {

    /**
     * path api.
     */
    private final String schedulePath = "stops/";

    /**
     * time zone.
     */
    private final String winnipegTimeZone = "America/Winnipeg";

    /**
     * get data from transit api.
     *
     * @param stopNumber stopNumber
     * @return json object
     */
    private HttpResponse<JsonNode> getRoutesByStopNumberRequest(String stopNumber) {
        Map<String, String> params = new HashMap<>();
        params.put("start", LocalDateTime.now(ZoneId.of("America/Winnipeg")).toString());
        return RequestSender.sendRequest(schedulePath + stopNumber + "/schedule", params);
    }

    /**
     * get list of schedules.
     *
     * @param stopNumber stopNumber
     * @return list of scheduled
     */
    public List<BusSchedule> getBusSchedulesByStopNumber(String stopNumber) {
        JSONArray rawSchedule = getRoutesByStopNumberRequest(stopNumber).getBody().getObject()
                .getJSONObject("stop-schedule").getJSONArray("route-schedules");
        List<BusSchedule> busSchedules = new ArrayList<>();
        for (Object object : rawSchedule) {
            JSONObject stop = (JSONObject) object;
            JSONArray stopBuses = stop.getJSONArray("scheduled-stops");
            for (Object stopBusesObject : stopBuses) {
                JSONObject bus = (JSONObject) stopBusesObject;
                BusSchedule schedule = new BusSchedule();
                schedule.setBusName(stop.getJSONObject("route").getString("name"));
                schedule.setBusNumber(String.valueOf(stop.getJSONObject("route").getInt("number")));
                String departureDateTime = getDepartureDateTimeStringFromBus(bus);
                String formattedDepartureDateTime = formatDateTime(departureDateTime);
                schedule.setFormattedDepartureTime(formattedDepartureDateTime);
                schedule.setTimeBeforeDepart(String.valueOf(calculateTimeBeforeDepartureFromString(departureDateTime)));
                busSchedules.add(schedule);
            }
        }
        Collections.sort(busSchedules);
        return busSchedules;
    }

    /**
     * retrieve departure datetime from json object.
     *
     * @param bus json
     * @return string
     */
    private String getDepartureDateTimeStringFromBus(JSONObject bus) {
        return bus.getJSONObject("times").getJSONObject("departure").getString("estimated");
    }

    /**
     * format to shorter.
     *
     * @param unformattedString unformattedString
     * @return short date
     */
    private String formatDateTime(String unformattedString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
        return LocalDateTime.parse(unformattedString).format(formatter);
    }

    /**
     * calculate time before departure.
     *
     * @param dateTimeString dateTimeString
     * @return minutes
     */
    private long calculateTimeBeforeDepartureFromString(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime departureDateTime = LocalDateTime.parse(dateTimeString, formatter);
        LocalDateTime now = LocalDateTime.now(ZoneId.of(winnipegTimeZone));
        return now.until(departureDateTime, ChronoUnit.MINUTES);
    }

}
