package com.ssafy.api.service;

import com.ssafy.api.response.PlaceDetailRes;
import com.ssafy.api.response.SearchPlaceRes;

import java.util.List;

public interface ScheduleService {
    public List<SearchPlaceRes> getserarchPlace(String keyword);

    public PlaceDetailRes getPlaceDetail(int jejuPlaceId);
}
