package com.ssafy.api.service;

import com.ssafy.api.request.ScheduleRegistReq;
import com.ssafy.api.response.JejuPlaceRes;
import com.ssafy.api.response.ScheduleThumbnailRes;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ScheduleService {
    List<ScheduleThumbnailRes> getScheduleThumbnail();
    void registSchedule(ScheduleRegistReq scheduleRegistReq);
    List<JejuPlaceRes> getRecommendJejuPlace(ScheduleRegistReq scheduleRegistReq);
}
