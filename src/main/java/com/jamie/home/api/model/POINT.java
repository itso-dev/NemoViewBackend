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
    private Integer accrue;
    private String content;
    private String state;
}
