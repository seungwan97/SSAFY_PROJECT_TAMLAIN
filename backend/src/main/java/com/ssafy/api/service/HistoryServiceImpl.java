package com.ssafy.api.service;

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
        Optional<List<Schedule>> scheduleList = scheduleRepository.findAllByUserId(userId);
        List<ScheduleHistoryRes> scheduleHistoryResList = new ArrayList<>();
        int size = scheduleList.get().size();

        for(int i = 0; i < size; i++) {
            ScheduleHistoryRes scheduleHistoryRes = new ScheduleHistoryRes(
                    scheduleList.get().get(i).getId(),
                    scheduleList.get().get(i).getName(),
                    scheduleList.get().get(i).getScheduleProfile().getImageUrl(),
                    scheduleList.get().get(i).getDay(),
                    scheduleList.get().get(i).getPeriod(),
                    scheduleList.get().get(i).isReview(),
                    scheduleList.get().get(i).getSurvey().getStartDate(),
                    scheduleList.get().get(i).getSurvey().getEndDate());

            scheduleHistoryResList.add(scheduleHistoryRes);
        }

        return scheduleHistoryResList;
    }
}
