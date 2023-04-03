package com.ssafy.api.request;

import com.ssafy.api.response.ReviewItem;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@ApiModel(description = "Flask에서 사용자의 설문 조사를 반영한 첫 추천 장소를 받기 위한 Request")
@Getter
public class FlaskFirstRecommendReq {
    private FlaskSurveyReq flaskSurveyReq;
    private List<FlaskJejuPlaceItem> flaskJejuPlaceItemList;
    private List<FlaskReviewItem> flaskReviewItemList;

    @Builder
    public FlaskFirstRecommendReq(FlaskSurveyReq flaskSurveyReq, List<FlaskJejuPlaceItem> flaskJejuPlaceItemList, List<FlaskReviewItem> flaskReviewItemList) {
        this.flaskSurveyReq = flaskSurveyReq;
        this.flaskJejuPlaceItemList = flaskJejuPlaceItemList;
        this.flaskReviewItemList = flaskReviewItemList;
    }
}
