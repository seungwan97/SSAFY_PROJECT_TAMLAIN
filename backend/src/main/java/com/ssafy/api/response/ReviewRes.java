package com.ssafy.api.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewRes {
    private String thumbnailImageUrl;
    private String jejuPlaceName;
    private int score;
    private boolean isVisit;
}
