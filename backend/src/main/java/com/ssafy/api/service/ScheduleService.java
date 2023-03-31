package com.ssafy.api.service;

import com.ssafy.api.request.SurveyRegistReq;
import com.ssafy.api.response.*;
import com.ssafy.api.request.ScheduleRegistReq;

import java.util.LinkedHashMap;
import java.util.List;

public interface ScheduleService {
    SuccessRes<List<SearchPlaceRes>> getserarchPlace(String keyword);
    SuccessRes<PlaceDetailRes> getPlaceDetail(int jejuPlaceId);
    SuccessRes<List<ScheduleThumbnailRes>> getScheduleThumbnail();
    CommonRes registSchedule(ScheduleRegistReq scheduleRegistReq);
    SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>> getRecommendJejuPlace(int surveyId);
}
