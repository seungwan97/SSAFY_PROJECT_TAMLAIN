package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@ApiModel(description = "Flask에서 사용자의 첫 추천 장소를 받기 위한 Request")
@Getter
public class FlaskFirstRecommendReq {
    @ApiModelProperty(value = "설문 조사 정보")
    private FlaskSurveyItem flaskSurveyItem;
    @ApiModelProperty(value = "제주 장소 목록")
    private List<FlaskJejuPlaceItem> flaskJejuPlaceItemList;
    @ApiModelProperty(value = "리뷰 목록")
    private List<FlaskReviewItem> flaskReviewItemList;

    @Builder
    public FlaskFirstRecommendReq(FlaskSurveyItem flaskSurveyItem, List<FlaskJejuPlaceItem> flaskJejuPlaceItemList, List<FlaskReviewItem> flaskReviewItemList) {
        this.flaskSurveyItem = flaskSurveyItem;
        this.flaskJejuPlaceItemList = flaskJejuPlaceItemList;
        this.flaskReviewItemList = flaskReviewItemList;
    }
}
