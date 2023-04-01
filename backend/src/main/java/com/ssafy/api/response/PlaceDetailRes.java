package com.ssafy.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class PlaceDetailRes {
    private String placeUrl;
    private Double reviewScore;
    @JsonProperty("La")
    private Double la;
    @JsonProperty("Ma")
    private Double ma;
    private String jejuPlaceName;
    private String roadAddress;

    @Builder
    public PlaceDetailRes(String placeUrl, Double reviewScore, Double la, Double ma, String jejuPlaceName, String roadAddress) {
        this.placeUrl = placeUrl;
        this.reviewScore = reviewScore;
        this.la = la;
        this.ma = ma;
        this.jejuPlaceName = jejuPlaceName;
        this.roadAddress = roadAddress;
    }
}
