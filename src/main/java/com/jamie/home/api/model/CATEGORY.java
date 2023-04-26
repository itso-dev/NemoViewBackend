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
public class CATEGORY {
    private Integer category;
    private Integer member;
    private String name;
    private Integer seq;
    private String state;
    private String icon;
    private String files;
    private Integer cnt;

    private ArrayList<MultipartFile> saveFiles;
    private String deleteFiles;

    private List<CATEGORY_CLASSIFICATION> classifications;
    private List<CATEGORY_CLASSIFICATION> delClassifications = new ArrayList<>();
    private List<CATEGORY_CLASSIFICATION> newClassifications = new ArrayList<>();
    private String classificationsStr;
    private String newClassificationsStr;
    private String delClassificationsStr;
}
