package com.ssafy.api.response;

import lombok.Data;

@Data
public class ScheduleDetailItem {
    private int scheduleItemId;
    private int day;
    private int jejuPlaceId;
    private String jejuPlaceName;
    private Double latitude;
    private Double longitude;
    private String roadAddress;
    private String placeUrl;
    private String imageUrl;
    private int reviewCount;
    private int reviewScore;
    private String tag;

    public ScheduleDetailItem(int scheduleItemId, int day, int jejuPlaceId, String jejuPlaceName, Double latitude, Double longitude, String roadAddress, String placeUrl, String imageUrl, int reviewCount, int reviewScore, String tag) {
        this.scheduleItemId = scheduleItemId;
        this.day = day;
        this.jejuPlaceId = jejuPlaceId;
        this.jejuPlaceName = jejuPlaceName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roadAddress = roadAddress;
        this.placeUrl = placeUrl;
        this.imageUrl = imageUrl;
        this.reviewCount = reviewCount;
        this.reviewScore = reviewScore;
        this.tag = tag;
    }
}
