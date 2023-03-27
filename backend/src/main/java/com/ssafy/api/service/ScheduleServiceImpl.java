package com.ssafy.api.service;

import com.ssafy.api.response.PlaceDetailRes;
import com.ssafy.api.response.SearchPlaceRes;
import com.ssafy.db.entity.JejuPlace;
import com.ssafy.db.entity.ScheduleItem;
import com.ssafy.db.repository.JejuPlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("scheduleService")
@Slf4j
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final JejuPlaceRepository jejuPlaceRepository;


    @Override
    public List<SearchPlaceRes> getserarchPlace(String keyword) {
        Optional<List<JejuPlace>> oJejuPlacesList = jejuPlaceRepository.findByNameContaining(keyword);
        List<SearchPlaceRes> serachPlaceResList = new ArrayList<>();
        int size = oJejuPlacesList.get().size();

        for(int i = 0; i < size; i++) {
            SearchPlaceRes serachPlaceRes = new SearchPlaceRes(
                    oJejuPlacesList.get().get(i).getImgUrl(),
                    oJejuPlacesList.get().get(i).getName(),
                    oJejuPlacesList.get().get(i).getRoadAddress());

            serachPlaceResList.add(serachPlaceRes);
        }

        return serachPlaceResList;
    }

    @Override
    public PlaceDetailRes getPlaceDetail(int jejuPlaceId) {
        Optional<JejuPlace> oJejuPlaces = jejuPlaceRepository.findById(jejuPlaceId);
        JejuPlace jejuPlace = oJejuPlaces.orElseThrow(() -> new IllegalArgumentException("jejuPlace doesn't exist"))

        return PlaceDetailRes.builder()
                .placeUrl(jejuPlace.getPlaceUrl())
                .reviewScore((double) (jejuPlace.getReviewScoreSum() / jejuPlace.getReviewCount()))
                .latitude(jejuPlace.getLatitude())
                .longitude(jejuPlace.getLongitude())
                .jejuPlaceName(jejuPlace.getName())
                .roadAddress(jejuPlace.getRoadAddress())
                .build();
    }

}
