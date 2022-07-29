package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CATEGORY {
    private Integer category;
    private String name;
    private Integer seq;
    private String state;
    private String icon;

    private List<CATEGORY_CLASSIFICATION> classifications;
}