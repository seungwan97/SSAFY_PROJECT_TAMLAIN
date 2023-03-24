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
    public List<ScheduleHistoryRes> getScheduleHistory(int userId) {
        Optional<List<Schedule>> oScheduleList = scheduleRepository.findAllByUserIdAndIsDeleteFalse(userId);
        List<ScheduleHistoryRes> scheduleHistoryResList = new ArrayList<>();
        int size = oScheduleList.get().size();

        for(int i = 0; i < size; i++) {
            ScheduleHistoryRes scheduleHistoryRes = new ScheduleHistoryRes(
                    oScheduleList.get().get(i).getId(),
                    oScheduleList.get().get(i).getScheduleThumbnail().getThumbnailImageUrl(),
                    oScheduleList.get().get(i).getUser().getUserProfile().getNickName(),
                    oScheduleList.get().get(i).getName(),
                    oScheduleList.get().get(i).getSurvey().getStartDate(),
                    oScheduleList.get().get(i).getSurvey().getEndDate(),
                    oScheduleList.get().get(i).getPeriod(),
                    oScheduleList.get().get(i).isReview());

            scheduleHistoryResList.add(scheduleHistoryRes);
        }

        return scheduleHistoryResList;
    }

    @Override
    public void deleteScheduleHistory(int scheduleId) {
        Optional<Schedule> oSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule = oSchedule.orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));

        schedule.setDelete(true);
        scheduleRepository.save(schedule);
    }

    @Override
    public void modifyScheduleName(ScheduleModifyReq scheduleModifyReq) {
        Optional<Schedule> oSchedule = scheduleRepository.findById(scheduleModifyReq.getScheduleId());
        Schedule schedule = oSchedule.orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));

        schedule.setName(scheduleModifyReq.getName());
        scheduleRepository.save(schedule);
    }
}
