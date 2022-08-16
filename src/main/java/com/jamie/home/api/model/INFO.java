package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

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

    public void setValues(Integer member, String type, Integer key, String title, String content) {
        this.member = member;
        this.type = type;
        this.key = key;
        this.title = title;
        this.content = content;
    }
}
