package com.ssafy.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ssafy.api.request.ScheduleRegistReq;
import com.ssafy.api.request.ScheduleReloadReq;
import com.ssafy.api.response.PlaceDetailRes;
import com.ssafy.api.response.JejuPlaceRes;
import com.ssafy.api.response.ScheduleThumbnailRes;
import com.ssafy.api.response.SearchPlaceRes;
import com.ssafy.api.response.*;
import com.ssafy.api.service.ScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;

@Api(value = "일점 API", tags = {"Schedule"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @ApiOperation(value = "장소 검색", notes = "장소 입력시 검색")
    @GetMapping("/search/{keyword}")
    public SuccessRes<List<SearchPlaceRes>> serarchPlace(@PathVariable("keyword") String keyword) {
        return scheduleService.getserarchPlace(keyword);
    }
    @ApiOperation(value = "장소 상세 정보", notes = "장소 아이디 입력시 해당 장소의 상세정보")
    @GetMapping("/{jejuPlaceId}")
    public SuccessRes<PlaceDetailRes> getPlaceDetail(@PathVariable("jejuPlaceId") int jejuPlaceId) {
        return scheduleService.getPlaceDetail(jejuPlaceId);
    }

    @ApiOperation(value = "일정 썸네일 사진 조회", notes = "일정 등록할 썸네일 사진 조회하기")
    @GetMapping("/thumbnail")
    public SuccessRes<List<ScheduleThumbnailRes>> getScheduleThumbnail() {
        return scheduleService.getScheduleThumbnail();
    }

    @ApiOperation(value = "일정 등록", notes = "사용자가 만든 일정 등록하기")
    @PostMapping("/regist")
    public CommonRes registSchedule(@RequestBody ScheduleRegistReq scheduleRegistReq) {
        return scheduleService.registSchedule(scheduleRegistReq);
    }

    @ApiOperation(value = "추천 불러오기", notes = "설문 조사를 통한 추천 장소 불러오기")
    @GetMapping("/recommend/survey/{surveyId}")
    public SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>> getRecommendJejuPlace(@PathVariable("surveyId") int surveyId) {
        return scheduleService.getRecommendJejuPlace(surveyId);
    }

    @ApiOperation(value = "재추천", notes = "재추천 추천 장소 불러오기")
    @PostMapping("/recommend/survey")
    public SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>> getReloadRecommendJejuPlace(@RequestBody ScheduleReloadReq scheduleReloadReq) {
        return scheduleService.getReloadRecommendJejuPlace(scheduleReloadReq);
    }

}
