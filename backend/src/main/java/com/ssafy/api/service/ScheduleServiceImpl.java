package com.ssafy.api.service;

import com.ssafy.api.response.ScheduleThumbnailRes;
import com.ssafy.db.entity.ScheduleThumbnail;
import com.ssafy.db.repository.ScheduleThumbnailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("scheduleService")
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleThumbnailRepository scheduleThumbnailRepository;

    @Override
    public List<ScheduleThumbnailRes> getScheduleThumbnail() {
        List<ScheduleThumbnail> scheduleThumbnailList = scheduleThumbnailRepository.findAll();
        List<ScheduleThumbnailRes> scheduleThumbnailResList = new ArrayList<>();

        for(int i = 0; i < scheduleThumbnailList.size(); i++) {
            ScheduleThumbnail scheduleThumbnail = scheduleThumbnailList.get(i);
            ScheduleThumbnailRes scheduleThumbnailRes = new ScheduleThumbnailRes(scheduleThumbnail.getId(), scheduleThumbnail.getThumbnailImageUrl());
            scheduleThumbnailResList.add(scheduleThumbnailRes);
        }
        return scheduleThumbnailResList;
    }
}
