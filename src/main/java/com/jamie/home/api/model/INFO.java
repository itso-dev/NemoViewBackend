package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class INFO {
    private Integer info;
    private Integer member;
    private Integer key;
    private String title;
    private String content;
    private String thumb;
    private String type;
    private Boolean del;
    private Date regdate;
    private Date upddate;

    private List<MEMBER> memberList;

    public void setValues(Integer member, String type, Integer key, String title, String content, String thumb) {
        this.member = member;
        this.type = type;
        this.key = key;
        this.title = title;
        this.content = content;
        this.thumb = thumb;
    }
}
