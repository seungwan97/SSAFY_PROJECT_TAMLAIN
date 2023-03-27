package com.ssafy.api.service;

import com.ssafy.api.response.ScheduleThumbnailRes;

import java.util.List;

public interface ScheduleService {
    List<ScheduleThumbnailRes> getScheduleThumbnail();
}
