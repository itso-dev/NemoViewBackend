package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
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
    private Integer hits;
    private String state;
    private String keywords;
    private Date regdate;
    private Date upddate;

    // 파일업로드
    private ArrayList<MultipartFile> savePhotos;
    private ArrayList<MultipartFile> saveVideos;
    private String deletePhotos;
    private String deleteVideos;

    // 키워드
    String keywordList;
    String keywordInputList;

    // 추가로 필요한 값들
    private Integer likeYn;
    private Integer likeCnt;
    private Integer replyCnt;
    private Integer reportCnt;
    private String categoryName;
    private MEMBER memberVO;
    private Integer point;
    private String reject;
}
