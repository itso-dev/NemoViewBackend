package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class QUESTION {
    private Integer question;
    private Integer member;
    private String type;
    private String title;
    private String content;
    private Integer hits;
    private Integer point;
    private String state;
    private String keywords;
}
