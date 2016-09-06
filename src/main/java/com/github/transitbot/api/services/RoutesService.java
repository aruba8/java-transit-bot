package com.github.transitbot.api.services;

import com.github.transitbot.api.models.Route;
import com.github.transitbot.utils.RequestSender;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private HttpResponse<JsonNode> getInfoByRouteNumber(String routeNumber) {
        return RequestSender.sendRequest(routesPath + "/" + routeNumber, null);
    }

    /**
     * get raw routes by stop number.
     *
     * @param stopNumber stopNumber
     * @return json
     */
    private HttpResponse<JsonNode> getRoutesByStopNumberRequest(String stopNumber) {
        Map<String, String> params = new HashMap<>();
        params.put("stop", stopNumber);
        return RequestSender.sendRequest(routesPath, params);
    }

    /**
     * get list of routes by stop number.
     *
     * @param stopNumber string ex. 10562
     * @return list of Routes
     */
    public List<Route> getRoutesByStopNumber(String stopNumber) {
        JSONArray array = getRoutesByStopNumberRequest(stopNumber).getBody().getObject().getJSONArray("routes");
        if (array.length() == 0) {
            return null;
        }
        List<Route> routes = new ArrayList<>();
        for (Object jsonRroute : array) {
            JSONObject jsonObjectRoute = (JSONObject) jsonRroute;
            Route route = new Route();
            route.setRouteNumber("" + jsonObjectRoute.getInt("number"));
            route.setRouteName(jsonObjectRoute.getString("name"));
            route.setRouteCoverage(jsonObjectRoute.getString("coverage"));
            routes.add(route);
        }
        return routes;
    }


}
