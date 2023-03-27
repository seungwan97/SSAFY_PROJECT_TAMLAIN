package com.ssafy.api.response;

import lombok.Builder;

import java.util.List;

public class ScheduleDetailRes {
    private MypageCommonInfo mypageCommonInfo;
    private List<List<ScheduleDetailItem>> scheduleDetailItemList;

    @Builder
    public ScheduleDetailRes(MypageCommonInfo mypageCommonInfo, List<List<ScheduleDetailItem>> scheduleDetailItemList) {
        this.mypageCommonInfo = mypageCommonInfo;
        this.scheduleDetailItemList = scheduleDetailItemList;
    }
}
