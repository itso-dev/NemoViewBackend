package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@ToString
public class CONTACT {
    private Integer contact;
    private Integer member;
    private String type;
    private String title;
    private String content;
    private String answer;
    private String files;
    private Date regdate;
    private Date upddate;

    private ArrayList<MultipartFile> saveFiles;
    private String deleteFiles;
}
