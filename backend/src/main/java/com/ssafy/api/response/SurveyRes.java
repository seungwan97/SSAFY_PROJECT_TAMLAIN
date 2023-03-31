package com.ssafy.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SurveyRes {
    private String result;
    private int surveyId;
    private int firstPageNum;

    @Builder
    public SurveyRes(String result, int surveyId, int firstPageNum) {
        this.result = result;
        this.surveyId = surveyId;
        this.firstPageNum = firstPageNum;
    }
}
