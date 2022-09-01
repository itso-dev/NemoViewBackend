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
public class REMEMBER {
    private Integer remember;
    private Integer member;
    private String token;
    private String uuid;
    private Date regdate;
    private Date upddate;
}
