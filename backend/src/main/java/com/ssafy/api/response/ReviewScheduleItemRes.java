package com.ssafy.api.response;

import java.util.List;

public class ReviewScheduleItemRes {
    private MypageCommonInfo mypageCommonInfo;
    private List<ReviewScheduleItem> reviewScheduleItemList;

    public ReviewScheduleItemRes(MypageCommonInfo mypageCommonInfo, List<ReviewScheduleItem> reviewScheduleItemList) {
        this.mypageCommonInfo = mypageCommonInfo;
        this.reviewScheduleItemList = reviewScheduleItemList;
    }
}
