package com.ssafy.api.service;

import com.ssafy.api.request.ScheduleModifyReq;
import com.ssafy.api.response.MypageCommonInfo;
import com.ssafy.api.response.ScheduleDetailItem;
import com.ssafy.api.response.ScheduleDetailRes;
import com.ssafy.api.response.ScheduleHistoryRes;
import com.ssafy.db.entity.JejuPlace;
import com.ssafy.db.entity.Schedule;
import com.ssafy.db.entity.ScheduleItem;
import com.ssafy.db.entity.Survey;
import com.ssafy.db.repository.ScheduleItemRepository;
import com.ssafy.db.repository.ScheduleRepository;
import com.ssafy.db.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("historyService")
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleItemRepository scheduleItemRepository;
    private final SurveyRepository surveyRepository;
    @Override
    public List<ScheduleHistoryRes> getScheduleHistory(int userId) {
        Optional<List<Schedule>> oScheduleList = scheduleRepository.findAllByUserIdAndIsDeleteFalse(userId);
        List<ScheduleHistoryRes> scheduleHistoryResList = new ArrayList<>();
        int size = oScheduleList.get().size();

        for(int i = 0; i < size; i++) {
            ScheduleHistoryRes scheduleHistoryRes = new ScheduleHistoryRes(
                    oScheduleList.get().get(i).getId(),
                    oScheduleList.get().get(i).getScheduleThumbnail().getThumbnailImageUrl(),
                    oScheduleList.get().get(i).getUser().getNickName(),
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

        int surveyId = schedule.getSurvey().getId();
        Optional<Survey> oSurvey = surveyRepository.findById(surveyId);
        Survey survey = oSurvey.orElseThrow(() -> new IllegalArgumentException("survey doesn't exist"));

        schedule.setDelete(true);
        scheduleRepository.save(schedule);

        survey.setDelete(true);
        surveyRepository.save(survey);
    }

    @Override
    public void modifyScheduleName(ScheduleModifyReq scheduleModifyReq) {
        Optional<Schedule> oSchedule = scheduleRepository.findById(scheduleModifyReq.getScheduleId());
        Schedule schedule = oSchedule.orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));

        schedule.setName(scheduleModifyReq.getName());
        scheduleRepository.save(schedule);
    }

    public MypageCommonInfo getMyPageCommonInfo(int scheduleId) {
        Optional<Schedule> oSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule = oSchedule.orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));

        MypageCommonInfo mypageCommonInfo = new MypageCommonInfo(
                schedule.getScheduleThumbnail().getThumbnailImageUrl(),
                schedule.getName(),
                schedule.getSurvey().getStartDate(),
                schedule.getSurvey().getEndDate(),
                schedule.getPeriod());

        return  mypageCommonInfo;
    }

    @Override
    public ScheduleDetailRes getScheduleDetail(int scheduleId) {
        Optional<List<ScheduleItem>> oScheduleItemList = scheduleItemRepository.findAllByScheduleId(scheduleId);
        List<List<ScheduleDetailItem>> scheduleDetailItemAllList = new ArrayList<>();
        List<ScheduleDetailItem> scheduleDetailItemList = new ArrayList<>();
        int size = oScheduleItemList.get().size();
        int beforeDay = 1;

        for(int i = 0; i < size; i++) {
            ScheduleItem scheduleItem = oScheduleItemList.get().get(i);
            JejuPlace jejuPlace = scheduleItem.getJejuPlace();
            int currentDay = scheduleItem.getDay();
            Double reviewScore = ((double)jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount());

            if(beforeDay != currentDay) {
                scheduleDetailItemAllList.add(scheduleDetailItemList);
                scheduleDetailItemList = new ArrayList<>();
            }

            ScheduleDetailItem scheduleDetailItem = new ScheduleDetailItem(
                    scheduleItem.getId(),
                    currentDay,
                    jejuPlace.getId(),
                    jejuPlace.getName(),
                    jejuPlace.getLatitude(),
                    jejuPlace.getLongitude(),
                    jejuPlace.getRoadAddress(),
                    jejuPlace.getPlaceUrl(),
                    jejuPlace.getImgUrl(),
                    jejuPlace.getReviewCount(),
                    Math.round(reviewScore*10)/10.0,
                    jejuPlace.getTag());

            scheduleDetailItemList.add(scheduleDetailItem);
            beforeDay = currentDay;
        }

        scheduleDetailItemAllList.add(scheduleDetailItemList);
        ScheduleDetailRes scheduleDetailRes = new ScheduleDetailRes(getMyPageCommonInfo(scheduleId), scheduleDetailItemAllList);
        return scheduleDetailRes;
    }
}
