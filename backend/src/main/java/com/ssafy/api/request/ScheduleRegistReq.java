package com.ssafy.api.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleRegistReq {
    private int userId;
    private int surveyId;
    private int scheduleThumbnailId;
    private String name;
    private List<ScheduleRegistItem> scheduleRegistItemList;
}
