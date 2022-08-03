package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

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

    public void setValues(Integer member, String type, Integer value, String content, String state) {
        this.member = member;
        this.type = type;
        this.value = value;
        this.content = content;
        this.state = state;
    }
}
