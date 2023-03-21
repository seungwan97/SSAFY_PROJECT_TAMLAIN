package com.ssafy.api.response;

import lombok.Data;

@Data
public class ReviewScheduleItemRes {
    private String thumbnailImageUrl;
    private String jejuPlaceName;
    private int scheduleItemId;
    private int jejuPlaceId;

    public ReviewScheduleItemRes(String thumbnailImageUrl, String jejuPlaceName, int scheduleItemId, int jejuPlaceId) {
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.jejuPlaceName = jejuPlaceName;
        this.scheduleItemId = scheduleItemId;
        this.jejuPlaceId = jejuPlaceId;
    }
}
