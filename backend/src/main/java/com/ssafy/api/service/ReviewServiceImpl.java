package com.ssafy.api.service;

import com.ssafy.api.request.ReviewRegistItem;
import com.ssafy.api.request.ReviewRegistReq;
import com.ssafy.api.response.*;
import com.ssafy.db.entity.*;
import com.ssafy.db.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("reviewService")
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleItemRepository scheduleItemRepository;
    private final UserRepository userRepository;
    private final JejuDataRepository jejuDataRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public ReviewScheduleItemRes getReviewScheduleHistory(int scheduleId) {
        Optional<List<ScheduleItem>> oScheduleItemList = scheduleItemRepository.findAllByScheduleId(scheduleId);
        List<ReviewScheduleItem> reviewScheduleItemList = new ArrayList<>();
        int size = oScheduleItemList.get().size();

        for(int i = 0; i < size; i++) {
            int scheduleItemId = oScheduleItemList.get().get(i).getId();
            JejuPlace jejuPlace = oScheduleItemList.get().get(i).getJejuPlace();

            ReviewScheduleItem reviewScheduleItem = new ReviewScheduleItem(
                    jejuPlace.getImgUrl(),
                    jejuPlace.getName(),
                    scheduleItemId,
                    jejuPlace.getId());

            reviewScheduleItemList.add(reviewScheduleItem);
        }

        ReviewScheduleItemRes reviewScheduleItemRes = new ReviewScheduleItemRes(getMyPageCommonInfo(scheduleId), reviewScheduleItemList);
        return reviewScheduleItemRes;
    }

    public MypageCommonInfo getMyPageCommonInfo(int scheduleId) {
        Optional<Schedule> oSchedule = scheduleRepository.findById(scheduleId);
        Schedule schedule = oSchedule.orElseThrow(() -> new IllegalArgumentException("schedule doesn't exist"));

        MypageCommonInfo mypageCommonInfo = new MypageCommonInfo(
                schedule.getScheduleThumbnail().getThumbnailImageUrl(),
                schedule.getName(),
                schedule.getSurvey().getStartDate(),
                schedule.getSurvey().getEndDate(),
                schedule.getPeriod());

        return  mypageCommonInfo;
    }

    @Override
    public void registReview(ReviewRegistReq reviewRegistReq) {
        Optional<User> oUser = userRepository.findById(reviewRegistReq.getUserId());
        User user = oUser.orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));
        int size = reviewRegistReq.getReviewRegistItemList().size();

        for(int i = 0; i < size; i++) {
            ReviewRegistItem reviewRegistItem = reviewRegistReq.getReviewRegistItemList().get(i);

            Optional<ScheduleItem> oScheduleItem = scheduleItemRepository.findById(reviewRegistItem.getScheduleItemId());
            ScheduleItem scheduleItem = oScheduleItem.orElseThrow(() -> new IllegalArgumentException("scheduleItem doesn't exist"));

            Optional<JejuPlace> oJejuPlace = jejuDataRepository.findById(reviewRegistItem.getJejuPlaceId());
            JejuPlace jejuPlace = oJejuPlace.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"));

            Review review = new Review();
            review.setUser(user);
            review.setScheduleItem(scheduleItem);
            review.setJejuPlace(jejuPlace);
            review.setScore(reviewRegistItem.getScore());

            reviewRepository.save(review);
        }
    }

    @Override
    public ReviewRes getReview(int scheduleId) {
        Optional<List<ScheduleItem>> oScheduleItemList = scheduleItemRepository.findAllByScheduleId(scheduleId);
        List<ReviewItem> reviewItemList = new ArrayList<>();
        int size = oScheduleItemList.get().size();

        for(int i = 0; i < size; i++) {
            ScheduleItem scheduleItem = oScheduleItemList.get().get(i);

            ReviewItem reviewItem = new ReviewItem();
            reviewItem.setJejuPlaceImgUrl(scheduleItem.getJejuPlace().getImgUrl());
            reviewItem.setJejuPlaceName(scheduleItem.getJejuPlace().getName());

            Optional<Review> review = reviewRepository.findById(scheduleItem.getId());
            if(!review.isEmpty()) {
                reviewItem.setScore(review.get().getScore());
                reviewItem.setVisit(true);
            } else {
                reviewItem.setScore(0);
                reviewItem.setVisit(false);
            }

            reviewItemList.add(reviewItem);
        }

        ReviewRes reviewRes = new ReviewRes(getMyPageCommonInfo(scheduleId), reviewItemList);
        return reviewRes;
    }

}
