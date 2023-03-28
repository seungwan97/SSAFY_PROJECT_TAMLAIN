package com.ssafy.api.controller;

import com.ssafy.api.request.SurveyRegistReq;
import com.ssafy.api.response.ReviewScheduleItemRes;
import com.ssafy.api.service.SurveyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "설문 조사 API", tags = {"Survey"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyContoller {
    private final SurveyService surveyService;
    @ApiOperation(value = "설문 조사 등록", notes = "사용자의 여행 스타일 설문하기")
    @PostMapping()
    public ResponseEntity<?> registSurvey(@RequestBody SurveyRegistReq surveyRegistReq) {
        int[] result = surveyService.registSurvey(surveyRegistReq);
        if(result[0] == -1) return ResponseEntity.status(202).body(result[1]);
        else return ResponseEntity.status(201).body(result[1]);
    }
}
