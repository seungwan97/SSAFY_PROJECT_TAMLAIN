package com.ssafy.api.service;

import com.ssafy.api.response.ScheduleHistoryRes;

import java.util.List;

public interface HistoryService {
    List<ScheduleHistoryRes> getScheduleHistory(Long userId);
}
