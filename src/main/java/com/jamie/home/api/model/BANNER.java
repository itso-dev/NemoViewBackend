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
public class BANNER {
    private Integer banner;
    private String state;
    private String title;
    private String image;
    private String type;
    private String location;
    private Integer seq;
    private String link;
    private String start;
    private String end;
    private Integer hits;
    private Integer views;
    private Date regdate;
    private Date upddate;

    private ArrayList<MultipartFile> saveFiles;
    private String deleteFiles;
}
