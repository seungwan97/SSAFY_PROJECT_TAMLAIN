package com.ssafy.api.service;

import com.ssafy.api.request.ReviewItemReq;
import com.ssafy.api.request.ScheduleReviewReq;
import com.ssafy.api.response.ReviewScheduleItemRes;
import com.ssafy.db.entity.*;
import com.ssafy.db.repository.JejuDataRepository;
import com.ssafy.db.repository.ReviewRepository;
import com.ssafy.db.repository.ScheduleItemRepository;
import com.ssafy.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("reviewService")
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ScheduleItemRepository scheduleItemRepository;
    private final UserRepository userRepository;
    private final JejuDataRepository jejuDataRepository;
    private final ReviewRepository reviewRepository;
    @Override
    public List<ReviewScheduleItemRes> getReviewScheduleHistory(int scheduleId) {
        Optional<List<ScheduleItem>> oScheduleItemList = scheduleItemRepository.findAllByScheduleId(scheduleId);
        List<ReviewScheduleItemRes> reviewScheduleItemResList = new ArrayList<>();
        int size = oScheduleItemList.get().size();

        for(int i = 0; i < size; i++) {
            int scheduleItemId = oScheduleItemList.get().get(i).getId();
            JejuPlace jejuPlace = oScheduleItemList.get().get(i).getJejuPlace();

            ReviewScheduleItemRes reviewScheduleItemsRes = new ReviewScheduleItemRes(
                    oScheduleItemList.get().get(i).getSchedule().getScheduleThumbnail().getThumbnailImageUrl(),
                    jejuPlace.getName(),
                    scheduleItemId,
                    jejuPlace.getId());

            reviewScheduleItemResList.add(reviewScheduleItemsRes);
        }
        return reviewScheduleItemResList;
    }

    @Override
    public void registReview(ScheduleReviewReq scheduleReviewReq) {
        Optional<User> oUser = userRepository.findById(scheduleReviewReq.getUserId());
        User user = oUser.orElseThrow(() -> new IllegalArgumentException("user doesn't exist"));
        int size = scheduleReviewReq.getReviewItemReqList().size();

        for(int i = 0; i < size; i++) {
            ReviewItemReq reviewItemReq = scheduleReviewReq.getReviewItemReqList().get(i);

            Optional<ScheduleItem> oScheduleItem = scheduleItemRepository.findById(reviewItemReq.getScheduleItemId());
            ScheduleItem scheduleItem = oScheduleItem.orElseThrow(() -> new IllegalArgumentException("scheduleItem doesn't exist"));

            Optional<JejuPlace> oJejuPlace = jejuDataRepository.findById(reviewItemReq.getJejuPlaceId());
            JejuPlace jejuPlace = oJejuPlace.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"));

            Review review = new Review();
            review.setUser(user);
            review.setScheduleItem(scheduleItem);
            review.setJejuPlace(jejuPlace);
            review.setScore(reviewItemReq.getScore());

            reviewRepository.save(review);
        }
    }

}
