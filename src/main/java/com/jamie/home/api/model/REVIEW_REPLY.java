package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class REVIEW_REPLY {
    private Integer reply;
    private Integer reply_key;
    private Integer review;
    private Integer member;
    private String content;
    private Integer likeCnt;
    private Integer likeYn;
    private Integer reReplyYn;
    private Date regdate;
    private Date upddate;

    private MEMBER memberVO;
    private List<REVIEW_REPLY> re_replyList;
}
