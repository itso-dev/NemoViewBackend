package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CATEGORY_KEYWORD {
    private Integer keyword;
    private Integer classification;
    private String name;
    private Boolean selected;
}
