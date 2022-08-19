package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@ToString
public class POINT {
    private Integer point;
    private Integer member;
    private String type;
    private Integer value;
    private String content;
    private String state;
    private Date regdate;
    private Date upddate;

    //관리자
    private String nickname;
    private String account;
    private String reject;

    public void setValues(Integer member, String type, Integer value, String content, String state) {
        this.member = member;
        this.type = type;
        this.value = value;
        this.content = content;
        this.state = state;
    }
}
