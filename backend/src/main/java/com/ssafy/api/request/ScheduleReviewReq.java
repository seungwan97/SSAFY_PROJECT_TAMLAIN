package com.ssafy.api.request;

import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
public class ScheduleReviewReq {
    private int userId;
    private List<ReviewItemReq> reviewItemReqList;
}
