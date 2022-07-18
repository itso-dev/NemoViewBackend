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
public class REVIEW {
    private Integer review;
    private Integer member;
    private Integer category;
    private String brand;
    private String name;
    private String mnumber;
    private String photo;
    private String video;
    private Float grade;
    private String motive;
    private String plus;
    private String minus;
    private String proposer;
    private String store;
    private String price;
    private String link;
    private String usetime;
    private String tip;
    private String keywords;
    private Integer hits;
    private String state;

    private ArrayList<MultipartFile> savePhotos;
    private ArrayList<MultipartFile> saveVideos;
    private String deletePhotos;
    private String deleteVideos;

    // 추가로 필요한 값들
    private Integer likeYn;
    private Integer likeCnt;
    private Integer replyCnt;
    private String categoryName;
    private MEMBER memberVO;
    private List<KEYWORD> keywordList;
}
