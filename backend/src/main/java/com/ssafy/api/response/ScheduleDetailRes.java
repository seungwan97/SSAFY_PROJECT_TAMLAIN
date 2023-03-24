package com.ssafy.api.response;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleDetailRes {
    private MypageCommonInfo mypageCommonInfo;
    private List<ScheduleDetailItem> scheduleDetailItemList;

    public ScheduleDetailRes(MypageCommonInfo mypageCommonInfo, List<ScheduleDetailItem> scheduleDetailItemList) {
        this.mypageCommonInfo = mypageCommonInfo;
        this.scheduleDetailItemList = scheduleDetailItemList;
    }
}
