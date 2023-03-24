package com.ssafy.api.request;

import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class SurveyRegistReq {
    private int userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private char gender;
    private int ageRange;
    private String travelMember;
    private boolean isCar;
    private String travelTheme;
    private List<String>[] surveyFavorCategoryList;
}
