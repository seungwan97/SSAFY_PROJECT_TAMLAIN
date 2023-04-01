package com.ssafy.api.response;

import lombok.Builder;
import lombok.Data;

@Data
public class PlaceDetailRes {
    private String placeUrl;
    private Double reviewScore;
    private Double La;
    private Double Ma;
    private String jejuPlaceName;
    private String roadAddress;

    @Builder
    public PlaceDetailRes(String placeUrl, Double reviewScore, Double la, Double ma, String jejuPlaceName, String roadAddress) {
        this.placeUrl = placeUrl;
        this.reviewScore = reviewScore;
        this.La = la;
        this.Ma = ma;
        this.jejuPlaceName = jejuPlaceName;
        this.roadAddress = roadAddress;
    }
}
