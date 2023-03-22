package com.ssafy.api.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewRegistReq {
    private int userId;
    private List<ReviewRegistItem> reviewRegistItemList;
}
