package com.ssafy.api.controller;

import com.ssafy.api.request.ReviewRegistReq;
import com.ssafy.api.response.ReviewRes;
import com.ssafy.api.response.ReviewScheduleItemRes;
import com.ssafy.api.service.ReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "리뷰(별점) API", tags = {"Review"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @ApiOperation(value = "별점 일정 목록 조회", notes = "별점을 등록할 일정 목록 조회하기")
    @GetMapping("/scheduleItem/{scheduleId}")
    public Object getReviewScheduleHistory(@PathVariable("scheduleId") int scheduleId) {
        ReviewScheduleItemRes reviewScheduleItemRes = reviewService.getReviewScheduleHistory(scheduleId);
        if(reviewScheduleItemRes == null) return "여행 마지막 날짜 이후부터 리뷰 등록 가능";
        return reviewScheduleItemRes;

//        ReviewScheduleItemRes reviewScheduleItemRes = reviewService.getReviewScheduleHistory(scheduleId);
//        if(reviewScheduleItemRes == null) return ResponseEntity.status(202).body("여행 마지막 날짜 이후부터 리뷰 등록 가능");
//        return ResponseEntity.status(200).body(reviewScheduleItemRes);
    }

    @ApiOperation(value = "별점 등록", notes = "각 장소마다 별점 등록하기")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/regist")
    public void registReview(@RequestBody ReviewRegistReq reviewRegistReq) {
        reviewService.registReview(reviewRegistReq);
    }

    @ApiOperation(value = "별점 조회", notes = "내가 등록한 별점 조회하기")
    @GetMapping("/{scheduleId}")
    public ReviewRes getReview(@PathVariable("scheduleId") int scheduleId) {
        return reviewService.getReview(scheduleId);
    }

}
