package com.ssafy.api.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SurveyRes {
    private boolean success;
    private int result;

    @Builder
    public SurveyRes(boolean success, int result) {
        this.success = success;
        this.result = result;
    }
}
