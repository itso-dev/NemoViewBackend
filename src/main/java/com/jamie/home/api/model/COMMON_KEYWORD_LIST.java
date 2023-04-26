package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class COMMON_KEYWORD_LIST {
    private COMMON_KEYWORD common_keyword;
    private List<COMMON_KEYWORD> keywordList;
    private List<COMMON_KEYWORD> keywordList_del;
    private List<COMMON_KEYWORD> keywordList_new;
}
