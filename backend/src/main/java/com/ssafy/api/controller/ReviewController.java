package com.ssafy.api.controller;

import com.ssafy.api.response.ReviewScheduleItemsRes;
import com.ssafy.api.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "리뷰(별점) API", tags = {"Review"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation(value = "별점 일정 목록 조회", notes = "별점을 등록할 일정 목록 조회하기")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<?> getReviewScheduleHistory(@PathVariable("scheduleId") Long scheduleId) {
        List<ReviewScheduleItemsRes> reviewScheduleItemsResList = reviewService.getReviewScheduleHistory(scheduleId);
        return ResponseEntity.status(200).body(reviewScheduleItemsResList);
    }

}
