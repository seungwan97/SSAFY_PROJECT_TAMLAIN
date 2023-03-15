package com.ssafy.api.response;

import lombok.Data;

@Data
public class ReviewScheduleItemsRes {
    private Long scheduleItemsId;
    private Long jejuDataId;
    private String name;
    private String imgUrl;

    public ReviewScheduleItemsRes(Long scheduleItemsId, Long jejuDataId, String name, String imgUrl) {
        this.scheduleItemsId = scheduleItemsId;
        this.jejuDataId = jejuDataId;
        this.name = name;
        this.imgUrl = imgUrl;
    }
}
