package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class COMMON_KEYWORD {
    private Integer common_keyword;
    private Integer category;
    private Integer cnt;
    private String name;
    private Boolean selected;
}
