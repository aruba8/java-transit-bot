package com.github.transitbot.api.services;

import com.github.transitbot.api.models.Coordinates;
import com.github.transitbot.utils.RequestSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    /**
     * Get locations which have names.
     *
     * @param locationResponse locationResponse
     * @return set of locations
     */
    public Set<JSONObject> getAddressesWithNameFromLocation(HttpResponse<JsonNode> locationResponse) {
        JSONArray rawObject = locationResponse.getBody().getArray();
        JSONObject locObject = rawObject.getJSONObject(0);
        JSONArray locationsArray = locObject.getJSONArray("locations");
        Set<JSONObject> locationsWithName = new HashSet<>();
        for (Object object : locationsArray) {
            JSONObject locationObject = (JSONObject) object;
            if (locationObject.has("name")) {
                locationsWithName.add(locationObject);
            }
        }
        return locationsWithName;
    }

}
