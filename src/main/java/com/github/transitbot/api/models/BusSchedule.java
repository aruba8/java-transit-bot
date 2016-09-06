package com.github.transitbot.api.models;

/**
 * BusSchedule bin.
 */
public class BusSchedule implements Comparable<BusSchedule> {

    /**
     * busNumber.
     */
    private String busNumber;

    /**
     * busName.
     */
    private String busName;

    /**
     * timeBeforeDepart.
     */
    private String timeBeforeDepart;

    /**
     * formattedDepartureTime.
     */
    private String formattedDepartureTime;

    /**
     * getter.
     *
     * @return busNumber
     */
    public String getBusNumber() {
        return busNumber;
    }

    /**
     * setter.
     *
     * @param busNumber busNumber
     */
    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    /**
     * getter.
     *
     * @return busName
     */
    public String getBusName() {
        return busName;
    }

    /**
     * setter.
     *
     * @param busName busName
     */
    public void setBusName(String busName) {
        this.busName = busName;
    }

    /**
     * getter.
     *
     * @return timeBeforeDepart
     */
    public String getTimeBeforeDepart() {
        return timeBeforeDepart;
    }

    /**
     * setter.
     *
     * @param timeBeforeDepart timeBeforeDepart
     */
    public void setTimeBeforeDepart(String timeBeforeDepart) {
        this.timeBeforeDepart = timeBeforeDepart;
    }

    /**
     * getter.
     *
     * @return formattedDepartureTime
     */
    public String getFormattedDepartureTime() {
        return formattedDepartureTime;
    }

    /**
     * setter.
     *
     * @param formattedDepartureTime formattedDepartureTime
     */
    public void setFormattedDepartureTime(String formattedDepartureTime) {
        this.formattedDepartureTime = formattedDepartureTime;
    }

    @Override
    public int compareTo(BusSchedule busSchedule) {
        return Integer.parseInt(this.getTimeBeforeDepart()) - Integer.parseInt(busSchedule.getTimeBeforeDepart());
    }
}
