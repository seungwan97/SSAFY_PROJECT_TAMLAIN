package com.ssafy.api.service;

import com.ssafy.api.request.ScheduleReviewReq;
import com.ssafy.api.response.ReviewScheduleItemRes;

import java.util.List;

public interface ReviewService {
    List<ReviewScheduleItemRes> getReviewScheduleHistory(int scheduleId);
    void registReview(ScheduleReviewReq scheduleReviewReq);
}
