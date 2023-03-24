package com.ssafy.api.service;

import com.ssafy.api.request.SurveyRegistReq;
import com.ssafy.db.entity.*;
import com.ssafy.db.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("SurveyService")
@RequiredArgsConstructor
public class SurveyServiceImpl implements SurveyService {
    private final UserRepository userRepository;
    private final TravelMemberRepository travelMemberRepository;
    private final TravelThemeRepository travelThemeRepository;
    private final SurveyRepository surveyRepository;
    private final CategoryRepository categoryRepository;
    private final SurveyFavorCategoryRepository surveyFavorCategoryRepository;
    @Override
    public int registSurvey(SurveyRegistReq surveyRegistReq) {
        List<String>[] list = surveyRegistReq.getSurveyFavorCategoryList();
        int count = 0, firstPage = 0;
        boolean flag = false;

        for(int i = 0; i < list.length; i++) {
            if(list[i].size() == 0) {
                count++;
                if(!flag) firstPage = i+1;
                flag = true;
            }
        }

        if(flag) return firstPage;

        Optional<User> oUser = userRepository.findById(surveyRegistReq.getUserId());
        User user = oUser.orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

        Optional<TravelMember> oTravelMember = travelMemberRepository.findByType(surveyRegistReq.getTravelMember());
        TravelMember travelMember = oTravelMember.orElseThrow(() -> new IllegalArgumentException("travelMember doesn't exist"));

        Optional<TravelTheme> oTravelTheme = travelThemeRepository.findByName(surveyRegistReq.getTravelTheme());
        TravelTheme travelTheme = oTravelTheme.orElseThrow(() -> new IllegalArgumentException("travelTheme doesn't exist"));

        int month = surveyRegistReq.getStartDate().getMonthValue();
        String season = "";

        if(12 <= month && month <= 2) season = "겨울";
        else if(3 <= month && month <= 5) season = "봄";
        else if(6 <= month && month <= 8) season = "여름";
        else season = "가을";

        Survey survey = new Survey();
        survey.setUser(user);
        survey.setStartDate(surveyRegistReq.getStartDate());
        survey.setEndDate(surveyRegistReq.getEndDate());
        survey.setGender(surveyRegistReq.getGender());
        survey.setAgeRange(surveyRegistReq.getAgeRange());
        survey.setTravelMemberCode(travelMember.getId());
        survey.setCar(surveyRegistReq.isCar());
        survey.setTravelThemeCode(travelTheme.getId());
        survey.setSeason(season);

        int surveyId = surveyRepository.save(survey).getId();

        for(int i = 0; i < list.length; i++) {
            for(int j = 0; j < list[i].size(); j++) {
                Optional<Category> oCategory = categoryRepository.findByCategoryDetailName(list[i].get(j));
                Category category = oCategory.orElseThrow(() -> new IllegalArgumentException("category doesn't exist"));

                Optional<Survey> oSurvey = surveyRepository.findById(surveyId);
                Survey newSurvey = oSurvey.orElseThrow(() -> new IllegalArgumentException("survey doesn't exist"));

                SurveyFavorCategory surveyFavorCategory = new SurveyFavorCategory();
                surveyFavorCategory.setSurvey(newSurvey);
                surveyFavorCategory.setCategory(category);

                surveyFavorCategoryRepository.save(surveyFavorCategory);
            }
        }

        return -1;
    }
}
