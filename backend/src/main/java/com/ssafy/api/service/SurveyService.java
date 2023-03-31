package com.ssafy.api.service;

import com.ssafy.api.request.SurveyRegistReq;
import com.ssafy.api.response.SurveyRes;

public interface SurveyService {
    SurveyRes registSurvey(SurveyRegistReq surveyRegistReq);
}
