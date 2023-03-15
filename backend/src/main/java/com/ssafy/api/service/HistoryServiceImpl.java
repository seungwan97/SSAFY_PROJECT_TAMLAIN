package com.ssafy.api.service;

import com.ssafy.api.request.ScheduleModifyReq;
import com.ssafy.api.response.ScheduleHistoryRes;
import com.ssafy.db.entity.Schedule;
import com.ssafy.db.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("historyService")
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final ScheduleRepository scheduleRepository;
    @Override
    public List<ScheduleHistoryRes> getScheduleHistory(Long userId) {
        Optional<List<Schedule>> oScheduleList = scheduleRepository.findAllByUserIdAndIsDeleteFalse(userId);
        List<ScheduleHistoryRes> scheduleHistoryResList = new ArrayList<>();
        int size = oScheduleList.get().size();

        for(int i = 0; i < size; i++) {
            ScheduleHistoryRes scheduleHistoryRes = new ScheduleHistoryRes(
                    oScheduleList.get().get(i).getId(),
                    oScheduleList.get().get(i).getName(),
                    oScheduleList.get().get(i).getScheduleProfile().getImageUrl(),
                    oScheduleList.get().get(i).getDay(),
                    oScheduleList.get().get(i).getPeriod(),
                    oScheduleList.get().get(i).isReview(),
                    oScheduleList.get().get(i).getSurvey().getStartDate(),
                    oScheduleList.get().get(i).getSurvey().getEndDate());

            scheduleHistoryResList.add(scheduleHistoryRes);
        }

        return scheduleHistoryResList;
    }

    @Override
    public void deleteScheduleHistory(Long scheduleId) {
        Optional<Schedule> oSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule = oSchedule.orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));

        schedule.setDelete(true);
        scheduleRepository.save(schedule);
    }

    @Override
    public void modifyScheduleName(ScheduleModifyReq scheduleModifyReq) {
        Optional<Schedule> oSchedule = scheduleRepository.findById(scheduleModifyReq.getId());
        Schedule schedule = oSchedule.orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));

        schedule.setName(schedule.getName());
        scheduleRepository.save(schedule);
    }
}
