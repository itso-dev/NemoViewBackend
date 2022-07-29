package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CATEGORY_CLASSIFICATION {
    private Integer classification;
    private String name;
    private Integer seq;
    private String state;

    private List<CATEGORY_KEYWORD> keywords;
}
