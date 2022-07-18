package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class CONTACT {
    private Integer contact;
    private Integer member;
    private String title;
    private String content;
    private String answer;
    private String files;

    private ArrayList<MultipartFile> saveFiles;
    private String deleteFiles;
}
