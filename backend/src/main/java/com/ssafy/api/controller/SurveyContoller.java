package com.ssafy.api.controller;

import com.ssafy.api.request.SurveyRegistReq;
import com.ssafy.api.response.ReviewScheduleItemRes;
import com.ssafy.api.response.SurveyRes;
import com.ssafy.api.service.SurveyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "설문 조사 API", tags = {"Survey"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyContoller {
    private final SurveyService surveyService;
    @ApiOperation(value = "설문 조사 등록", notes = "사용자의 여행 스타일 설문하기")
    @PostMapping("/regist")
    public SurveyRes registSurvey(@RequestBody SurveyRegistReq surveyRegistReq) {
        return surveyService.registSurvey(surveyRegistReq);
    }
}
