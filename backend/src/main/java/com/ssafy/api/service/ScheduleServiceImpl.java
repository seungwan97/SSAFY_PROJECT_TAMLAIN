package com.ssafy.api.service;

import com.ssafy.api.request.*;
import com.ssafy.api.response.*;
import com.ssafy.db.entity.*;
import com.ssafy.db.repository.*;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Period;
import java.util.*;

@Service("scheduleService")
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final UserRepository userRepository;
    private final ScheduleThumbnailRepository scheduleThumbnailRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleItemRepository scheduleItemRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyFavorCategoryRepository surveyFavorCategoryRepository;
    private final JejuPlaceRepository jejuPlaceRepository;

    @Override
    public List<SearchPlaceRes> getserarchPlace(String keyword) {
        List<JejuPlace> jejuPlaceList = jejuPlaceRepository.findByNameContaining(keyword);
        List<SearchPlaceRes> serachPlaceResList = new ArrayList<>();

        for(JejuPlace jejuPlace : jejuPlaceList) {
            LinkedHashMap<String, Double> map = new LinkedHashMap<>();
            map.put("La", jejuPlace.getLatitude());
            map.put("Ma", jejuPlace.getLongitude());

            SearchPlaceRes serachPlaceRes = new SearchPlaceRes(
                    serachPlaceResList.size()+1,
                    jejuPlace.getImgUrl(),
                    jejuPlace.getName(),
                    jejuPlace.getRoadAddress(),
                    map);
            serachPlaceResList.add(serachPlaceRes);
        }

        return serachPlaceResList;
    }

    @Override
    public SuccessRes<PlaceDetailRes> getPlaceDetail(int jejuPlaceId) {
        Optional<JejuPlace> oJejuPlaces = jejuPlaceRepository.findById(jejuPlaceId);
        JejuPlace jejuPlace = oJejuPlaces.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"));

        PlaceDetailRes placeDetailRes = PlaceDetailRes.builder()
                .placeUrl(jejuPlace.getPlaceUrl())
                .reviewScore((double) (jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount()))
                .latitude(jejuPlace.getLatitude())
                .longitude(jejuPlace.getLongitude())
                .jejuPlaceName(jejuPlace.getName())
                .roadAddress(jejuPlace.getRoadAddress())
                .build();

        return new SuccessRes<PlaceDetailRes>(true, "상세 정보를 조회합니다.", placeDetailRes);
    }



    @Override
    public SuccessRes<List<ScheduleThumbnailRes>> getScheduleThumbnail() {
        List<ScheduleThumbnail> scheduleThumbnailList = scheduleThumbnailRepository.findAll();
        List<ScheduleThumbnailRes> scheduleThumbnailResList = new ArrayList<>();

        for(ScheduleThumbnail scheduleThumbnail : scheduleThumbnailList) {
            ScheduleThumbnailRes scheduleThumbnailRes = ScheduleThumbnailRes.builder()
                    .scheduleThumbnailId(scheduleThumbnail.getId())
                    .thumbnailImageUrl(scheduleThumbnail.getThumbnailImageUrl())
                    .build();
            scheduleThumbnailResList.add(scheduleThumbnailRes);
        }

        return new SuccessRes<List<ScheduleThumbnailRes>>(true, "일정 썸네일 이미지를 조회합니다.", scheduleThumbnailResList);
    }

    @Override
    public CommonRes registSchedule(ScheduleRegistReq scheduleRegistReq) {
        Optional<User> oUser = userRepository.findById(scheduleRegistReq.getUserId());
        User user = oUser.orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));

        Optional<Survey> oSurvey = surveyRepository.findById(scheduleRegistReq.getSurveyId());
        Survey survey = oSurvey.orElseThrow(() -> new IllegalArgumentException("survey doesn't exist"));

        int period = Period.between(survey.getStartDate(), survey.getEndDate()).getDays() + 1;

        Optional<ScheduleThumbnail> oScheduleThumbnail = scheduleThumbnailRepository.findById(scheduleRegistReq.getScheduleThumbnailId());
        ScheduleThumbnail scheduleThumbnail = oScheduleThumbnail.orElseThrow(() -> new IllegalArgumentException("scheduleThumbnail doesn't exist"));

        Schedule schedule = Schedule.builder()
                .user(user)
                .survey(survey)
                .scheduleThumbnail(scheduleThumbnail)
                .name(scheduleRegistReq.getName())
                .period(period)
                .isDelete(false)
                .isReview(false)
                .build();

        scheduleRepository.save(schedule);

        List<ScheduleRegistItem> scheduleRegistItemList = scheduleRegistReq.getScheduleRegistItemList();
        for(ScheduleRegistItem scheduleRegistItem : scheduleRegistItemList) {
            Optional<JejuPlace> oJejuPlace = jejuPlaceRepository.findById(scheduleRegistItem.getJejuPlaceId());
            JejuPlace jejuPlace = oJejuPlace.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"));

            ScheduleItem scheduleItem = ScheduleItem.builder()
                    .schedule(schedule)
                    .jejuPlace(jejuPlace)
                    .day(scheduleRegistItem.getDay())
                    .build();

            scheduleItemRepository.save(scheduleItem);
        }

        return new CommonRes(true, "일정 등록을 완료했습니다.");
    }

    @Override
    public SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>> getRecommendJejuPlace(int surveyId) {
        Optional<Survey> oSurvey = surveyRepository.findById(surveyId);
        Survey survey = oSurvey.orElseThrow(() -> new IllegalArgumentException("survey doesn't exist"));

        List<SurveyFavorCategory> surveyFavorCategoryList = surveyFavorCategoryRepository.findAllBySurveyId(surveyId);
        List<Integer> categoryList = new ArrayList<>();
        for(SurveyFavorCategory surveyFavorCategory : surveyFavorCategoryList) {
            categoryList.add(surveyFavorCategory.getCategory().getId());
        }

        FlaskSurveyReq flaskSurveyReq = FlaskSurveyReq.builder()
                .userId(survey.getUser().getId())
                .startDate(survey.getStartDate())
                .endDate(survey.getEndDate())
                .travelThemeCode(survey.getTravelThemeCode())
                .season(survey.getSeason())
                .surveyFavorCategoryList(categoryList)
                .build();

//        List<JejuPlace> jejuPlaceList = jejuPlaceRepository.findAllByCategoryId(categoryList);
        List<JejuPlace> jejuPlaceList = new ArrayList<>();
        for(Integer id : categoryList) {
            List<JejuPlace> list = jejuPlaceRepository.findAllByCategoryId(id);
            for(JejuPlace jejuPlace : list) {
                jejuPlaceList.add(jejuPlace);
            }
        }

        List<FlaskJejuPlaceItem> flaskJejuPlaceItemList = new ArrayList<>();

        for(JejuPlace jejuPlace : jejuPlaceList) {
//            System.out.println("id: " + jejuPlace.getId());

            FlaskJejuPlaceItem flaskJejuPlaceItem = FlaskJejuPlaceItem.builder()
                    .jejuPlaceId(jejuPlace.getId())
                    .name(jejuPlace.getName())
                    .categoryId(jejuPlace.getCategory().getId())
                    .categoryName(jejuPlace.getCategory().getCategoryName())
                    .categoryDetailName(jejuPlace.getCategory().getCategoryDetailName())
                    .latitude(jejuPlace.getLatitude())
                    .longitude(jejuPlace.getLongitude())
                    .reviewScore((double) (jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount()))
                    .build();

            flaskJejuPlaceItemList.add(flaskJejuPlaceItem);
        }

        FlaskRecommendReq recommendReq = FlaskRecommendReq.builder()
                .flaskSurveyReq(flaskSurveyReq)
                .flaskJejuPlaceItemList(flaskJejuPlaceItemList)
                .build();

        HttpResponse<LinkedHashMap<String, List<Integer>>> httpResponse =  Unirest.post("http://127.0.0.1:5000/recommend")
                .header("Content-Type", "application/json")
                .body(recommendReq)
                .asObject(new GenericType<LinkedHashMap<String, List<Integer>>>() {});

        LinkedHashMap<String, List<Integer>> recommendMap = httpResponse.getBody();
        LinkedHashMap<String, List<JejuPlaceRes>> resultMap = new LinkedHashMap<>();
        List<JejuPlaceRes> jejuPlaceResList = new ArrayList<>();
        for(String str : recommendMap.keySet()) {
            List<Integer> list = recommendMap.get(str);
            for(Integer i : list) {
                Optional<JejuPlace> oJejuPlace = jejuPlaceRepository.findById(i);
                JejuPlace jejuPlace = oJejuPlace.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"));

                JejuPlaceRes jejuPlaceRes = JejuPlaceRes.builder()
                        .name(jejuPlace.getName())
                        .categoryId(jejuPlace.getCategory().getId())
                        .mapInfo(MapInfo.builder()
                                .title(jejuPlace.getName())
                                .latlng(LatLng.builder().la(jejuPlace.getLatitude()).ma(jejuPlace.getLongitude()).build())
                                .build())
                        .roadAddress(jejuPlace.getRoadAddress())
                        .placeUrl(jejuPlace.getPlaceUrl())
                        .imgUrl(jejuPlace.getImgUrl())
                        .reviewScore(Math.round(((double) (jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount()))*10)/10.0)
                        .tag("#" + jejuPlace.getTag().replace("_", " #"))
                        .build();

                jejuPlaceResList.add(jejuPlaceRes);
            }

            String categoryDescription = "";
            switch(str) {
                case "맛집" :
                    categoryDescription = "탐나's RESTAURANT PICK";
                    break;
                case "카페/간식" :
                    categoryDescription = "아직";
                    break;
                case "액티비티/체험" :
                    categoryDescription = "다";
                    break;
                case "스포츠/레저" :
                    categoryDescription = "안정함";
                    break;
                case "전시" :
                    categoryDescription = "ㅠ";
                    break;
                case "휴양" :
                    categoryDescription = "ㅠ";
                    break;
                default:
                    break;
            }

            resultMap.put(categoryDescription, jejuPlaceResList);
        }

        return new SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>>(true, "설문 조사에 대한 추천 장소를 받습니다.", resultMap);
    }

}
