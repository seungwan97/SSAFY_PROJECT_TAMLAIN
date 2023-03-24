package com.ssafy.api.response;

import com.ssafy.api.request.ReviewRegistItem;
import lombok.Data;

import java.util.List;

@Data
public class ReviewRes {
    private MypageCommonInfo mypageCommonInfo;
    private List<ReviewItem> reviewItemList;

    public ReviewRes(MypageCommonInfo mypageCommonInfo, List<ReviewItem> reviewItemList) {
        this.mypageCommonInfo = mypageCommonInfo;
        this.reviewItemList = reviewItemList;
    }
}
