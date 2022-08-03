package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class SEARCH {
    // 리스트
    private Integer page;
    private Integer page_block;
    private Integer start;
    private String orderType;
    private String searchType;
    private String searchKeyword;

    // 회원
    private Integer member;

    // 리뷰
    private Integer review;
    private Integer category;
    private Boolean videoYn;
    private String keywords;
    private List<String> keywordList;
    private List<KEYWORD> review_keywords;

    // 질문
    private Integer question;
    private String type;

    // 알림
    private Boolean del;

    // 관리자
    private String state;
    private Boolean admin;

    public void calStart(){
        this.start = (this.page-1) * this.page_block;
    }
}
