package com.ssafy.api.service;

import com.ssafy.api.request.ScheduleRegistReq;
import com.ssafy.api.response.ScheduleThumbnailRes;

import java.util.List;

public interface ScheduleService {
    List<ScheduleThumbnailRes> getScheduleThumbnail();
    void registSchedule(ScheduleRegistReq scheduleRegistReq);
}
