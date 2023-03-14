package com.ssafy.api.controller;

import com.ssafy.api.response.ScheduleHistoryRes;
import com.ssafy.api.service.HistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "일정 내역 API", tags = {"History"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/history")
public class HisoryController {
    private final HistoryService historyService;
    @ApiOperation(value = "일정 내역 조회", notes = "마이페이지에서 나의 일정 내역 조회하기")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getScheduleHistory(@PathVariable("userId") Long userId) {
        List<ScheduleHistoryRes> scheduleHistoryResList = historyService.getScheduleHistory(userId);
        return ResponseEntity.status(200).body(scheduleHistoryResList);
    }
}
