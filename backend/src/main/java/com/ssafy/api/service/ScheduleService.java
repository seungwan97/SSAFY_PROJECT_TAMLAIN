package com.ssafy.api.service;

import com.ssafy.api.request.SurveyRegistReq;
import com.ssafy.api.response.PlaceDetailRes;
import com.ssafy.api.response.SearchPlaceRes;
import com.ssafy.api.request.ScheduleRegistReq;
import com.ssafy.api.response.JejuPlaceRes;
import com.ssafy.api.response.ScheduleThumbnailRes;

import java.util.List;

public interface ScheduleService {
    public List<SearchPlaceRes> getserarchPlace(String keyword);
    public PlaceDetailRes getPlaceDetail(int jejuPlaceId);
    List<ScheduleThumbnailRes> getScheduleThumbnail();
    void registSchedule(ScheduleRegistReq scheduleRegistReq);
    List<JejuPlaceRes> getRecommendJejuPlace(SurveyRegistReq surveyRegistReq);
}
