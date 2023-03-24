package com.ssafy.api.service;

import com.ssafy.api.request.ScheduleModifyReq;
import com.ssafy.api.response.ScheduleDetailRes;
import com.ssafy.api.response.ScheduleHistoryRes;
import com.ssafy.db.entity.Schedule;

import java.util.List;

public interface HistoryService {
    List<ScheduleHistoryRes> getScheduleHistory(int userId);
    void deleteScheduleHistory(int scheduleId);
    void modifyScheduleName(ScheduleModifyReq scheduleModifyReq);

    ScheduleDetailRes getScheduleDetail(int scheduleId);
}
