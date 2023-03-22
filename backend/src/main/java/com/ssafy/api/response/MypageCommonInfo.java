package com.ssafy.api.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MypageCommonInfo {
    private String thumbnailImageUrl;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int period;

    public MypageCommonInfo(String thumbnailImageUrl, String name, LocalDate startDate, LocalDate endDate, int period) {
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
    }
}
