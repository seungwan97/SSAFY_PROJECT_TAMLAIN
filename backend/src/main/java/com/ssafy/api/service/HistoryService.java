package com.ssafy.api.service;

import com.ssafy.api.request.ScheduleModifyReq;
import com.ssafy.api.response.ScheduleHistoryRes;

import java.util.List;

public interface HistoryService {
    List<ScheduleHistoryRes> getScheduleHistory(Long userId);
    void deleteScheduleHistory(Long scheduleId);
    void modifyScheduleName(ScheduleModifyReq scheduleModifyReq);

}
