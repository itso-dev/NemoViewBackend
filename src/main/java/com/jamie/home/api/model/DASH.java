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
public class DASH {
    private Integer memberTot;
    private Integer memberNew;
    private Integer memberLogin;
    private Integer pointTot;
    private Integer pointApply;
    private Integer pointComplete;
    private List<CATEGORY> categoryList;
    private List<CATEGORY> categoryRankList;
    private List<KEYWORD> reviewKeywordRankList;
    private List<KEYWORD> questionKeywordRankList;
}
