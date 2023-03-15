package com.ssafy.api.request;

import lombok.Getter;

@Getter
public class ScheduleModifyReq {
    private Long scheduleId;
    private String name;
}
