package com.ssafy.api.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleHistoryRes {
    private int scheduleId;
    private String thumbnailImageUrl;
    private String nickName;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int period;
    private boolean isReview;

    public ScheduleHistoryRes(int scheduleId, String thumbnailImageUrl, String nickName, String name, LocalDate startDate, LocalDate endDate, int period, boolean isReview) {
        this.scheduleId = scheduleId;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.nickName = nickName;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
        this.isReview = isReview;
    }
}
