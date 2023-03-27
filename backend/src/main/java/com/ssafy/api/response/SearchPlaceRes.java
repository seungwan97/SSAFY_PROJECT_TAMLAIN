package com.ssafy.api.response;

import lombok.Data;

@Data
public class SearchPlaceRes {
    private String imgUrl;
    private String name;
    private String roadAddress;

    public SearchPlaceRes(String imgUrl, String name, String roadAddress) {
        this.imgUrl = imgUrl;
        this.name = name;
        this.roadAddress = roadAddress;
    }
}