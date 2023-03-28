package com.ssafy.api.response;

import com.ssafy.api.request.ReviewRegistItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class ReviewRes {
    private MypageCommonInfo mypageCommonInfo;
    private List<ReviewItem> reviewItemList;

    @Builder
    public ReviewRes(MypageCommonInfo mypageCommonInfo, List<ReviewItem> reviewItemList) {
        this.mypageCommonInfo = mypageCommonInfo;
        this.reviewItemList = reviewItemList;
    }
}
