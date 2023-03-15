package com.ssafy.api.service;

import com.ssafy.api.response.ReviewScheduleItemsRes;

import java.util.List;

public interface ReviewService {
    List<ReviewScheduleItemsRes> getReviewScheduleHistory(Long scheduleId);
}
