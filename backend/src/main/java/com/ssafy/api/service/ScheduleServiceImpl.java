package com.ssafy.api.service;

import com.ssafy.api.request.*;
import com.ssafy.api.response.*;
import com.ssafy.db.entity.*;
import com.ssafy.db.repository.*;
import kong.unirest.GenericType;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final ReviewRepository reviewRepository;
    private final RedisTemplate<String, RedisPlace> redisTemplate;

    @Override
    public SuccessRes<List<SearchPlaceRes>> getsearchPlace(String keyword) {
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
                    jejuPlace.getId(),
                    map);
            serachPlaceResList.add(serachPlaceRes);
        }

        return new SuccessRes<List<SearchPlaceRes>>(true, "해당 검색어에 포함된 장소들을 조회합니다.", serachPlaceResList);
    }

    @Override
    public SuccessRes<List<JejuPlaceRes>> getAllPlace() {
        List<JejuPlace> jejuPlaceList = jejuPlaceRepository.findAll();
        List<JejuPlaceRes> jejuPlaceResList = new ArrayList<>();

        for(JejuPlace jejuPlace : jejuPlaceList) {
            JejuPlaceRes jejuPlaceRes = JejuPlaceRes.builder()
                    .name(jejuPlace.getName())
                    .categoryId(jejuPlace.getCategory().getId())
                    .mapInfo(MapInfo.builder()
                            .jejuPlaceId(jejuPlace.getId())
                            .title(jejuPlace.getName())
                            .latlng(LatLng.builder().la(jejuPlace.getLatitude()).ma(jejuPlace.getLongitude()).build())
                            .build())
                    .roadAddress(jejuPlace.getRoadAddress())
                    .placeUrl(jejuPlace.getPlaceUrl())
                    .imgUrl(jejuPlace.getImgUrl())
                    .reviewScore((jejuPlace.getReviewCount() != 0) ? Math.round(((double) jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount())*10)/10.0 : 0)
                    .tag((jejuPlace.getTag() == null || jejuPlace.getTag().isBlank()) ? "" : "#" + jejuPlace.getTag().replace("_", " #"))
                    .build();

            jejuPlaceResList.add(jejuPlaceRes);
        }

        return new SuccessRes<List<JejuPlaceRes>>(true, "검색을 위한 전체 장소를 조회합니다.", jejuPlaceResList);
    }

    @Override
    public SuccessRes<PlaceDetailRes> getPlaceDetail(int jejuPlaceId) {
        Optional<JejuPlace> oJejuPlaces = jejuPlaceRepository.findById(jejuPlaceId);
        JejuPlace jejuPlace = oJejuPlaces.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"));

        PlaceDetailRes placeDetailRes = PlaceDetailRes.builder()
                .placeUrl(jejuPlace.getPlaceUrl())
                .reviewScore((jejuPlace.getReviewCount() != 0) ? Math.round(((double) jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount())*10)/10.0 : 0)
                .mapInfo(MapInfo.builder()
                        .jejuPlaceId(jejuPlace.getId())
                        .title(jejuPlace.getName())
                        .latlng(LatLng.builder().la(jejuPlace.getLatitude()).ma(jejuPlace.getLongitude()).build())
                        .build())
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

            scheduleRegistItem.getJejuPlaceId();
            ScheduleItem scheduleItem = ScheduleItem.builder()
                    .schedule(schedule)
                    .jejuPlace(jejuPlace)
                    .day(scheduleRegistItem.getDay())
                    .build();

            scheduleItemRepository.save(scheduleItem);
        }

        //redis있는 내용 전체 삭제
        redisTemplate.delete(redisTemplate.keys("*"));

        return new CommonRes(true, "일정 등록을 완료했습니다.");
    }

    @Override
    public SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>> getRecommendJejuPlace(int surveyId) {
        Optional<Survey> oSurvey = surveyRepository.findById(surveyId);
        Survey survey = oSurvey.orElseThrow(() -> new IllegalArgumentException("survey doesn't exist"));

        // 설문 조사 카테고리 조회
        List<SurveyFavorCategory> surveyFavorCategoryList = surveyFavorCategoryRepository.findAllBySurveyId(surveyId);
        List<Integer> categoryList = new ArrayList<>();
        for(SurveyFavorCategory surveyFavorCategory : surveyFavorCategoryList) {
            categoryList.add(surveyFavorCategory.getCategory().getId());
        }

        // 설문 조사 Flask Req
        FlaskSurveyReq flaskSurveyReq = FlaskSurveyReq.builder()
                .userId(survey.getUser().getId())
                .travelThemeCode(survey.getTravelThemeCode())
                .season(survey.getSeason())
                .surveyFavorCategoryList(categoryList)
                .build();

        // 제주 장소 Flask Req
        List<JejuPlace> jejuPlaceList = new ArrayList<>();
        for(Integer id : categoryList) {
            List<JejuPlace> list = jejuPlaceRepository.findAllByCategoryId(id);
            for(JejuPlace jejuPlace : list) {
                jejuPlaceList.add(jejuPlace);
            }
        }

//        List<JejuPlace> jejuPlaceList = jejuPlaceRepository.findAll();
        List<FlaskJejuPlaceItem> flaskJejuPlaceItemList = new ArrayList<>();

        for(JejuPlace jejuPlace : jejuPlaceList) {
            FlaskJejuPlaceItem flaskJejuPlaceItem = FlaskJejuPlaceItem.builder()
                    .jejuPlaceId(jejuPlace.getId())
                    .categoryId(jejuPlace.getCategory().getId())
                    .categoryName(jejuPlace.getCategory().getCategoryName())
                    .reviewScoreSum(jejuPlace.getReviewScoreSum())
                    .reviewCount(jejuPlace.getReviewCount())
                    .build();

            flaskJejuPlaceItemList.add(flaskJejuPlaceItem);
        }

        // 리뷰 Flask Req
        List<Review> reviewList = reviewRepository.findAll();
        List<FlaskReviewItem> flaskReviewItemList = new ArrayList<>();
        for (Review review : reviewList){
            FlaskReviewItem flaskReviewItem = FlaskReviewItem.builder()
                    .userId(review.getUser().getId())
                    .season(review.getScheduleItem().getSchedule().getSurvey().getSeason())
                    .travelThemeCode(review.getScheduleItem().getSchedule().getSurvey().getTravelThemeCode())
                    .jejuPlaceId(review.getJejuPlace().getId())
                    .score(review.getScore())
                    .build();

            flaskReviewItemList.add(flaskReviewItem);
        }

        // Flask로 Req
        FlaskFirstRecommendReq flaskFirstRecommendReq = FlaskFirstRecommendReq.builder()
                .flaskSurveyReq(flaskSurveyReq)
                .flaskJejuPlaceItemList(flaskJejuPlaceItemList)
                .flaskReviewItemList(flaskReviewItemList)
                .build();

        HttpResponse<LinkedHashMap<String, List<Integer>>> httpResponse =  Unirest.post("http://127.0.0.1:5000/recommend")
                .header("Content-Type", "application/json")
                .body(flaskFirstRecommendReq)
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
                                .jejuPlaceId(jejuPlace.getId())
                                .title(jejuPlace.getName())
                                .latlng(LatLng.builder().la(jejuPlace.getLatitude()).ma(jejuPlace.getLongitude()).build())
                                .build())
                        .roadAddress(jejuPlace.getRoadAddress())
                        .placeUrl(jejuPlace.getPlaceUrl())
                        .imgUrl(jejuPlace.getImgUrl())
                        .reviewScore((jejuPlace.getReviewCount() != 0) ? Math.round(((double) jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount())*10)/10.0 : 0)
                        .tag((jejuPlace.getTag() == null || jejuPlace.getTag().isBlank()) ? "" : "#" + jejuPlace.getTag().replace("_", " #"))
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

        return new SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>>(true, "설문 조사에 대한 첫 추천 장소를 받습니다.", resultMap);
    }

    @Override
    public SuccessRes<LinkedHashMap<String, List<JejuPlaceRes>>> getReloadRecommendJejuPlace(ScheduleReloadReq scheduleReloadReq)  {
        Optional<Survey> oSurvey = surveyRepository.findById(scheduleReloadReq.getSurveyId());
        Survey survey = oSurvey.orElseThrow(() -> new IllegalArgumentException("survey doesn't exist"));

        List<JejuPlace> jejuPlaceList = jejuPlaceRepository.findAll();
        List<JejuPlace> jejuPlaceDeleteList = new ArrayList<>();
        System.out.println("----------------" + scheduleReloadReq.getSurveyId());
        System.out.println(scheduleReloadReq.getPlaceDeleteId().keySet());
        for(List<Integer> In : scheduleReloadReq.getPlaceDeleteId().values()){
            for(int i : In) {
                Optional<JejuPlace> oJejuPlace = jejuPlaceRepository.findById(i);
                JejuPlace jejuPlace = oJejuPlace.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"));
                jejuPlaceDeleteList.add(jejuPlace);
            }
        }

        // redis 값이 있다면 추가해서 보내주는 것
        if(redisTemplate.keys("*").size() != 0){
            for (String key : redisTemplate.keys("*")){
                Optional<JejuPlace> oJejuPlace = jejuPlaceRepository.findById(Integer.parseInt(key));
                JejuPlace jejuPlace = oJejuPlace.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"));
                jejuPlaceDeleteList.add(jejuPlace);

            }
        }
        //redis 값 추가
        saveJejuPlace(scheduleReloadReq);

        // 삭제된 값들 저장 완료
        jejuPlaceList.removeAll(jejuPlaceDeleteList);
        List<FlaskJejuPlaceItem> flaskJejuPlaceItemList = new ArrayList<>();

        // 선택된 값들 저장
        HashMap<Integer, List<FlaskJejuPlaceItem>> scheduleItemMap = new HashMap<>();
        for(String key : scheduleReloadReq.getPlaceSelectId().keySet()){
            List<JejuPlace> jejuPlaceSelectList = new ArrayList<>();
            List<FlaskJejuPlaceItem> flaskJejuPlaceSelectItemList = new ArrayList<>();
            for(int i : scheduleReloadReq.getPlaceSelectId().get(key)) {
                Optional<JejuPlace> oJejuPlace = jejuPlaceRepository.findById(i);
                JejuPlace jejuPlace = oJejuPlace.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"));
                jejuPlaceSelectList.add(jejuPlace);
                // 보내는 형식에 맞게 변환
                getFlaskJejuItem(jejuPlaceSelectList, flaskJejuPlaceSelectItemList);
            }
            scheduleItemMap.put(Integer.parseInt(key), flaskJejuPlaceSelectItemList);
        }


        List<SurveyFavorCategory> surveyFavorCategoryList = surveyFavorCategoryRepository.findAllBySurveyId(scheduleReloadReq.getSurveyId());
        List<Integer> categoryList = new ArrayList<>();
        for(SurveyFavorCategory surveyFavorCategory : surveyFavorCategoryList) {
            categoryList.add(surveyFavorCategory.getCategory().getId());
        }

        FlaskSurveyReq flaskSurveyReq = FlaskSurveyReq.builder()
                .userId(survey.getUser().getId())
//                .startDate(survey.getStartDate())
//                .endDate(survey.getEndDate())
                .travelThemeCode(survey.getTravelThemeCode())
                .season(survey.getSeason())
                .surveyFavorCategoryList(categoryList)
                .build();

        getFlaskJejuItem(jejuPlaceList, flaskJejuPlaceItemList);

        // 리뷰 확인
        List<Review> reviewList = reviewRepository.findAll();
        List<ReviewItem> reviewItems = new ArrayList<>();
        for (Review review : reviewList){
            ReviewItem reviewItem = ReviewItem.builder()
                    .jejuPlaceImgUrl(review.getJejuPlace().getPlaceUrl())
                    .jejuPlaceName(review.getJejuPlace().getName())
                    .score(review.getScore())
                    .build();
            reviewItems.add(reviewItem);
        }
//        List<SurveyFavorCategory> surveyFavorCategoryList = s.findAllBySurveyId(scheduleReloadReq.getSurveyId());
//        List<Integer> categoryList = new ArrayList<>();
//        for(SurveyFavorCategory surveyFavorCategory : surveyFavorCategoryList) {
//            categoryList.add(surveyFavorCategory.getCategory().getId());
//        }

        FlaskRecommendReq recommendReq = FlaskRecommendReq.builder()
                .flaskSurveyReq(flaskSurveyReq)
                .flaskJejuPlaceItemList(flaskJejuPlaceItemList)
                .scheduleItemMap(scheduleItemMap)
                .reviewItem(reviewItems)
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
                                .jejuPlaceId(jejuPlace.getId())
                                .title(jejuPlace.getName())
                                .latlng(LatLng.builder().la(jejuPlace.getLatitude()).ma(jejuPlace.getLongitude()).build())
                                .build())
                        .roadAddress(jejuPlace.getRoadAddress())
                        .placeUrl(jejuPlace.getPlaceUrl())
                        .imgUrl(jejuPlace.getImgUrl())
                        .reviewScore((jejuPlace.getReviewCount() != 0) ? Math.round(((double) jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount())*10)/10.0 : 0)
                        .tag((jejuPlace.getTag() == null || jejuPlace.getTag().isBlank()) ? "" : "#" + jejuPlace.getTag().replace("_", " #"))
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

    @Override
    public void saveJejuPlace(ScheduleReloadReq scheduleReloadReq) {
        for(Map.Entry<String, List<Integer>> entry : scheduleReloadReq.getPlaceDeleteId().entrySet()){
            RedisPlace redisPlace = new RedisPlace(entry.getKey(), Collections.singletonMap(entry.getKey(), entry.getValue()));
            redisTemplate.opsForHash().putAll(entry.getKey(), redisPlace.getJejuPlaceId());
        }
    }

    @Override
    public Map<String, List<Integer>> getJejuPlace(String id) {
        Map<Object, Object> result = redisTemplate.opsForHash().entries(id);
        Map<String, List<Integer>> data = new HashMap<>();
        for (Map.Entry<Object, Object> entry : result.entrySet()) {
            String key = entry.getKey().toString();
            data.put(key, (List<Integer>) entry.getValue());
        }
        return data;
    }

    private void getFlaskJejuItem(List<JejuPlace> jejuPlaceList, List<FlaskJejuPlaceItem> flaskJejuPlaceItemList) {
        for(JejuPlace jejuPlace : jejuPlaceList) {
            FlaskJejuPlaceItem flaskJejuPlaceItem = FlaskJejuPlaceItem.builder()
                    .jejuPlaceId(jejuPlace.getId())
//                    .name(jejuPlace.getName())
                    .categoryId(jejuPlace.getCategory().getId())
                    .categoryName(jejuPlace.getCategory().getCategoryName())
//                    .categoryDetailName(jejuPlace.getCategory().getCategoryDetailName())
//                    .latitude(jejuPlace.getLatitude())
//                    .longitude(jejuPlace.getLongitude())
//                    .reviewScore((jejuPlace.getReviewCount() != 0) ? Math.round(((double) jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount())*10)/10.0 : 0)
                    .build();

            flaskJejuPlaceItemList.add(flaskJejuPlaceItem);
        }
    }


}
