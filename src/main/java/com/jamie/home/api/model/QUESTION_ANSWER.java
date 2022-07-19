package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class QUESTION_ANSWER {
    private Integer answer;
    private Integer question;
    private Integer member;
    private String content;
    private Boolean choose;
}
