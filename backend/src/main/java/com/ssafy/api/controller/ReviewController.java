package com.ssafy.api.controller;

import com.ssafy.api.request.ScheduleReviewReq;
import com.ssafy.api.response.ReviewScheduleItemRes;
import com.ssafy.api.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "리뷰(별점) API", tags = {"Review"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation(value = "별점 일정 목록 조회", notes = "별점을 등록할 일정 목록 조회하기")
    @GetMapping("/scheduleItem/{scheduleId}")
    public ResponseEntity<?> getReviewScheduleHistory(@PathVariable("scheduleId") int scheduleId) {
        List<ReviewScheduleItemRes> reviewScheduleItemsResList = reviewService.getReviewScheduleHistory(scheduleId);
        return ResponseEntity.status(200).body(reviewScheduleItemsResList);
    }

    @ApiOperation(value = "별점 등록", notes = "각 장소마다 별점 등록하기")
    @PostMapping()
    public ResponseEntity<?> registReview(@RequestBody ScheduleReviewReq scheduleReviewReq) {
        reviewService.registReview(scheduleReviewReq);
        return ResponseEntity.status(200).body("별점 등록 성공");
    }

}
