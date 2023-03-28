package com.ssafy.api.controller;

import com.ssafy.api.request.ScheduleRegistReq;
import com.ssafy.api.response.JejuPlaceRes;
import com.ssafy.api.response.ScheduleThumbnailRes;
import com.ssafy.api.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "일정 API", tags = {"Schedule"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @ApiOperation(value = "일정 썸네일 사진 조회", notes = "일정 등록할 썸네일 사진 조회하기")
    @GetMapping("/thumbnail")
    public ResponseEntity<?> getScheduleThumbnail() {
        List<ScheduleThumbnailRes> scheduleThumbnailResList = scheduleService.getScheduleThumbnail();
        return ResponseEntity.status(200).body(scheduleThumbnailResList);
    }

    @ApiOperation(value = "일정 등록", notes = "사용자가 만든 일정 등록하기")
    @PostMapping()
    public ResponseEntity<?> registSchedule(@RequestBody ScheduleRegistReq scheduleRegistReq) {
        scheduleService.registSchedule(scheduleRegistReq);
        return ResponseEntity.status(200).body("일정 등록");
    }

    @ApiOperation(value = "추천 불러오기", notes = "설문 조사를 통한 추천 장소 불러오기")
    @GetMapping("/recommend/survey")
    public ResponseEntity<?> getRecommendJejuPlace(@RequestBody ScheduleRegistReq scheduleRegistReq) {
        List<JejuPlaceRes> jejuPlaceResList = scheduleService.getRecommendJejuPlace(scheduleRegistReq);
        return ResponseEntity.status(200).body(jejuPlaceResList);
    }
    
}
