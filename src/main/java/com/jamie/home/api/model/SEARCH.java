package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SEARCH {
    // 리스트
    private Integer page;
    private Integer page_block;
    private Integer start;

    // 회원
    private Integer member;

    // 리뷰
    private Integer review;

    // 질문
    private Integer question;

    // 알림
    private Boolean del;

    // 관리자
    private Boolean state;
    private Boolean admin;

    public void calStart(){
        this.start = (this.page-1) * this.page_block;
    }
}
