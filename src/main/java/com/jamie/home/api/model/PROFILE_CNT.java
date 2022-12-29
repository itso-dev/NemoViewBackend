package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PROFILE_CNT {
    private Integer reviewCnt;
    private Integer reviewTotalCnt;
    private Integer reviewTotalHits;
    private Integer reviewTotalLikes;
    private Integer questionCnt;
}
