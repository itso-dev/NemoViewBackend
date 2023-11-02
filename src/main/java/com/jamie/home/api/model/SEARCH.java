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
    private String name;
    private String birthday;
    private String email;
    private String nickname;
    private Boolean stopMember;
    private Boolean alertMember;

    // 리뷰
    private Integer review;
    private Integer reply_key;
    private Integer category;
    private Boolean videoYn;
    private String keywords;
    private List<Keywords> keywordList;
    private List<KEYWORD> review_keywords;
    private Integer reviewMember;
    private Integer likeMember;

    // 질문
    private Integer question;
    private Integer answer_key;
    private String type;
    private Integer questionMember;
    private Integer answerMember;
    private Boolean noAnswer;
    private Boolean helpAnswer;

    // 알림
    private Integer key;
    private Boolean del;

    // 관리자
    private String state;
    private String antiState;
    private Boolean admin;
    private Boolean needAnswer;
    private String valueType;
    private Integer value;
    private String searchStartDate;
    private String searchEndDate;
    private String chkPointList;
    // 배너
    private Integer banner;
    private String location;

    // auth
    private String returnurl;
    private String enc_data;
    private String auth_iv;
    private String auth_key;

    // push
    private String title;
    private String body;

    // bm
    private Integer ad;

    // point
    private Boolean todayCnt;

    private List<INFO> info_list;
    public void calStart(){
        this.start = (this.page-1) * this.page_block;
    }
}
