package com.ssafy.api.request;

import lombok.Getter;

@Getter
public class ReviewItemReq {
    private int scheduleItemId;
    private int jejuPlaceId;
    private int score;
}
