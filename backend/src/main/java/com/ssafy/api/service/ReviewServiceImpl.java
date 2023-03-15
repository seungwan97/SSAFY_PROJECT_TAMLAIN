package com.ssafy.api.service;

import com.ssafy.api.response.ReviewScheduleItemsRes;
import com.ssafy.db.entity.JejuData;
import com.ssafy.db.entity.ScheduleItems;
import com.ssafy.db.repository.JejuDataRepository;
import com.ssafy.db.repository.ReviewRepository;
import com.ssafy.db.repository.ScheduleItemsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("reviewService")
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ScheduleItemsRepository scheduleItemsRepository;
    @Override
    public List<ReviewScheduleItemsRes> getReviewScheduleHistory(Long scheduleId) {
        Optional<List<ScheduleItems>> oScheduleItemsList = scheduleItemsRepository.findAllByScheduleId(scheduleId);
        List<ReviewScheduleItemsRes> reviewScheduleItemsResList = new ArrayList<>();
        int size = oScheduleItemsList.get().size();

        for(int i = 0; i < size; i++) {
            Long scheduleItemsId = oScheduleItemsList.get().get(i).getId();
            JejuData jejuData = oScheduleItemsList.get().get(i).getJejuData();

            ReviewScheduleItemsRes reviewScheduleItemsRes = new ReviewScheduleItemsRes(
                    scheduleItemsId,
                    jejuData.getId(),
                    jejuData.getName(),
                    jejuData.getImgUrl());

            reviewScheduleItemsResList.add(reviewScheduleItemsRes);
        }
        return reviewScheduleItemsResList;
    }

}
