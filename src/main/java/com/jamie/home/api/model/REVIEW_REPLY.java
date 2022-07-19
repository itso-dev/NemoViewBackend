package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class REVIEW_REPLY {
    private Integer reply;
    private Integer review;
    private Integer member;
    private String content;

    private MEMBER memberVO;
}
