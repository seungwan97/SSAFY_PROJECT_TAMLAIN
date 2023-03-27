package com.ssafy.api.response;

import lombok.Data;

@Data
public class ScheduleThumbnailRes {
    private int scheduleThumbnailId;
    private String thumbnailImageUrl;

    public ScheduleThumbnailRes(int scheduleThumbnailId, String thumbnailImageUrl) {
        this.scheduleThumbnailId = scheduleThumbnailId;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }
}
