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
public class REPORT {
    private Integer report;
    private Integer member;
    private Integer key;
    private String type;
    private String title;
    private String content;
    private String files;
    private Date regdate;
    private Date upddate;

    private ArrayList<MultipartFile> saveFiles;

    private MEMBER memberVO;
}
