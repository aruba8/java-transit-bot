package com.github.transitbot.api.services;

import com.github.transitbot.api.models.Coordinates;
import com.github.transitbot.api.services.exceptions.GoogleApiZeroResultsException;
import com.github.transitbot.utils.ConfigReader;
import com.github.transitbot.utils.GoogleApiRequestSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * GoogleGeoApiService.
 */
public class GoogleGeoApiService {

    /**
     * Sender.
     */
    private GoogleApiRequestSender googleApiRequestSender;

    /**
     * Google api url.
     */
    private String googleApiUrl;

    /**
     * Google Api Key.
     */
    private String googleApiKey;

    /**
     * Constructor.
     */
    public GoogleGeoApiService() {
        googleApiRequestSender = new GoogleApiRequestSender();
        googleApiUrl = ConfigReader.getPropValues().getProperty("google.api.url");
        googleApiKey = ConfigReader.getPropValues().getProperty("google.api.key");
    }


    /**
     * get coordinate of location from address string.
     *
     * @param address string.
     * @return Coordinates.
     */
    public Coordinates getCoordinatesByAddress(String address) {
        Map params = new HashMap();
        params.put("address", address + "+Winnipeg,+MB,+Canada");
        Coordinates coordinates = null;
        try {
            HttpResponse response = googleApiRequestSender.sendRequest(googleApiUrl, googleApiKey, params);
            coordinates = getCoordinatesFromResponse(response);
        } catch (UnirestException | GoogleApiZeroResultsException e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    /**
     * method to parse response.
     *
     * @param response response.
     * @return Coordinates.
     * @throws GoogleApiZeroResultsException throws if result zero.
     */
    public Coordinates getCoordinatesFromResponse(HttpResponse<JsonNode> response)
            throws GoogleApiZeroResultsException {
        String status = response.getBody().getObject().getString("status");
        if (!status.equals("OK")) {
            throw new GoogleApiZeroResultsException("Google Geo Api returned ZERO_RESULTS");
        }
        JSONObject object = response.getBody().getObject().getJSONArray("results").getJSONObject(0);
        JSONObject geometry = object.getJSONObject("geometry");
        JSONObject location = geometry.getJSONObject("location");
        double longitude = location.getDouble("lng");
        double latitude = location.getDouble("lat");
        return new Coordinates(Double.toString(longitude), Double.toString(latitude));
    }
}
