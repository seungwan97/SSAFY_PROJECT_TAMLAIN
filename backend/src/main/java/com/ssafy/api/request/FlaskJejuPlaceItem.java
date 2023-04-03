package com.ssafy.api.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FlaskJejuPlaceItem {
    private int jejuPlaceId;
    private int categoryId;
    private String categoryName;
    private int reviewScoreSum;
    private int reviewCount;

    @Builder
    public FlaskJejuPlaceItem(int jejuPlaceId, int categoryId, String categoryName, int reviewScoreSum, int reviewCount) {
        this.jejuPlaceId = jejuPlaceId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.reviewScoreSum = reviewScoreSum;
        this.reviewCount = reviewCount;
    }
}
