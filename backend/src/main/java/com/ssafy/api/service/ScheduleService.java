package com.ssafy.api.service;

import com.ssafy.api.request.ScheduleReloadReq;
import com.ssafy.api.request.SurveyRegistReq;
import com.ssafy.api.response.*;
import com.ssafy.api.request.ScheduleRegistReq;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ScheduleService {
    SuccessRes<List<SearchPlaceRes>> getserarchPlace(String keyword);
    SuccessRes<PlaceDetailRes> getPlaceDetail(int jejuPlaceId);
    SuccessRes<List<ScheduleThumbnailRes>> getScheduleThumbnail();
    CommonRes registSchedule(ScheduleRegistReq scheduleRegistReq);
    SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>> getRecommendJejuPlace(int surveyId);

    SuccessRes<List<SearchPlaceRes>> getserarchPlace();
    SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>> getReloadRecommendJejuPlace(ScheduleReloadReq scheduleReloadReq);

    void saveJejuPlace(ScheduleReloadReq scheduleReloadReq);
    Map<String, List<Integer>> getJejuPlace(String id);
}
