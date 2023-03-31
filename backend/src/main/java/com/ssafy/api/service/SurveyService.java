package com.ssafy.api.service;

import com.ssafy.api.request.SurveyRegistReq;
import com.ssafy.api.response.SuccessRes;
import com.ssafy.api.response.SurveyRes;

public interface SurveyService {
    SuccessRes<Integer> registSurvey(SurveyRegistReq surveyRegistReq);
}
