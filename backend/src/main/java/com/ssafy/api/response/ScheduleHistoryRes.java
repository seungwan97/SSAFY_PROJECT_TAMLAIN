package com.ssafy.api.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleHistoryRes {
    private Long id;
    private String name;
    private String imageUrl;
    private String day;
    private String period;
    private boolean isReview;
    private LocalDate startDate;
    private LocalDate endDate;

    public ScheduleHistoryRes(Long id, String name, String imageUrl, String day, String period, boolean isReview, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.day = day;
        this.period = period;
        this.isReview = isReview;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
