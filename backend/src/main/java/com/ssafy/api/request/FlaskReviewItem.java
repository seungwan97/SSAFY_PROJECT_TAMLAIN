package com.ssafy.api.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FlaskReviewItem {
    private int userId;
    private String season;
    private int travelThemeCode;
    private int jejuPlaceId;
    private int score;

    @Builder
    public FlaskReviewItem(int userId, String season, int travelThemeCode, int jejuPlaceId, int score) {
        this.userId = userId;
        this.season = season;
        this.travelThemeCode = travelThemeCode;
        this.jejuPlaceId = jejuPlaceId;
        this.score = score;
    }
}
