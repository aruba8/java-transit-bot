package com.github.transitbot.dao.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "trip_data")
public class TripData {
    /**
     * chatId.
     */
    @DatabaseField(id = true)
    private Long chatId;

    @DatabaseField
    private String destinationKey;

    @DatabaseField
    private String destinationType;

    @DatabaseField
    private String originKey;

    @DatabaseField
    private String originType;

    public TripData() {
    }

    public TripData(Long chatId, String destinationKey, String destinationType, String originKey, String originType) {
        this.chatId = chatId;
        this.destinationKey = destinationKey;
        this.destinationType = destinationType;
        this.originKey = originKey;
        this.originType = originType;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getDestinationKey() {
        return destinationKey;
    }

    public void setDestinationKey(String destinationKey) {
        this.destinationKey = destinationKey;
    }

    public String getOriginKey() {
        return originKey;
    }

    public void setOriginKey(String originKey) {
        this.originKey = originKey;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public String getOriginType() {
        return originType;
    }

    public void setOriginType(String originType) {
        this.originType = originType;
    }
}
