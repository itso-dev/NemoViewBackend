package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class MEMBER {
    private Integer member;
    private String email;
    private String password;
    private String phone;
    private String name;
    private String nickname;
    private String gender;
    private String birthday;
    private String job;
    private String money;
    private String location;
    private Integer point;
    private String code;
    private String swords;
    private String account;
    private String profile;
    private String state;
    private Date logdate;
    private String keywords;
    @Enumerated(EnumType.STRING)
    private ROLE role;
    private Date regdate;
    private Date upddate;

    //파일업로드
    private ArrayList<MultipartFile> saveFiles;
    private String deleteFiles;

    private String keywordList;
    private String inputCode;
}
