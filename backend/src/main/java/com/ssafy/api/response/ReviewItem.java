package com.ssafy.api.response;

import lombok.Data;

@Data
public class ReviewItem {
    private String jejuPlaceImgUrl;
    private String jejuPlaceName;
    private int score;
    private boolean isVisit;
}
