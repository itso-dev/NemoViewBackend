package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class INFO {
    private Integer info;
    private Integer member;
    private Integer key;
    private String title;
    private Integer content;
    private String thumb;
    private String type;
    private Boolean del;
}
