package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@ApiModel(description = "Flask에서 사용자의 설문 조사를 반영한 추천 장소를 받기 위한 Request")
@Getter
public class FlaskRecommendReq {
    private SurveyRegistReq surveyRegistReq;
    private List<FlaskJejuPlaceItem> flaskJejuPlaceItemList;

    @Builder
    public FlaskRecommendReq(SurveyRegistReq surveyRegistReq, List<FlaskJejuPlaceItem> flaskJejuPlaceItemList) {
        this.surveyRegistReq = surveyRegistReq;
        this.flaskJejuPlaceItemList = flaskJejuPlaceItemList;
    }
}
