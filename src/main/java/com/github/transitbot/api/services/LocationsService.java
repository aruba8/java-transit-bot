package com.github.transitbot.api.services;

import com.github.transitbot.api.models.Coordinates;
import com.github.transitbot.utils.RequestSender;
import com.mashape.unirest.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * LocationsService class.
 */
public class LocationsService {

    /**
     * path.
     */
    private String routesPath = "locations";

    /**
     * get locations json from api.
     *
     * @param coordinates coordinates
     * @return response
     */
    public HttpResponse getLocationsByCoordinates(Coordinates coordinates) {
        Map params = new HashMap();
        params.put("lat", coordinates.getLatitude());
        params.put("lon", coordinates.getLongitude());
        return RequestSender.sendRequest(routesPath, params);
    }

}
