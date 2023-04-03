package com.ssafy.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@ApiModel(description = "Flask에서 사용자의 설문 조사 정보를 포함한 Request")
@Getter
public class FlaskSurveyReq {
    @ApiModelProperty(value = "사용자 id")
    private int userId;
    @ApiModelProperty(value = "여행 테마 id")
    private int travelThemeCode;
    @ApiModelProperty(value = "계절")
    private String season;
    @ApiModelProperty(value = "사용자가 선택한 세부 카테고리 목록")
    private List<Integer> surveyFavorCategoryList;

    @Builder
    public FlaskSurveyReq(int userId, int travelThemeCode, String season, List<Integer> surveyFavorCategoryList) {
        this.userId = userId;
        this.travelThemeCode = travelThemeCode;
        this.season = season;
        this.surveyFavorCategoryList = surveyFavorCategoryList;
    }
}
