package com.github.transitbot.api.models;

/**
 * Bean to represent route info.
 */
public class Route {

    /**
     * route name.
     */
    private String routeName;

    /**
     * route coverage.
     */
    private String routeCoverage;

    /**
     * Route number.
     */
    private String routeNumber;

    /**
     * constructor to create bean.
     *
     * @param routeName     routeName
     * @param routeCoverage routeCoverage
     */
    public Route(String routeName, String routeCoverage) {
        this.routeName = routeName;
        this.routeCoverage = routeCoverage;
    }

    /**
     * constructor by default.
     */
    public Route() {

    }

    /**
     * getter.
     *
     * @return routeName
     */
    public String getRouteName() {
        return routeName;
    }

    /**
     * setter.
     *
     * @param routeName routeName
     */
    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    /**
     * getter.
     *
     * @return routeCoverage
     */
    public String getRouteCoverage() {
        return routeCoverage;
    }

    /**
     * setter.
     *
     * @param routeCoverage routeCoverage
     */
    public void setRouteCoverage(String routeCoverage) {
        this.routeCoverage = routeCoverage;
    }

    /**
     * getter.
     *
     * @return routeNumber
     */
    public String getRouteNumber() {
        return routeNumber;
    }

    /**
     * setter.
     *
     * @param routeNumber routeNumber
     */
    public void setRouteNumber(String routeNumber) {
        this.routeNumber = routeNumber;
    }
}
