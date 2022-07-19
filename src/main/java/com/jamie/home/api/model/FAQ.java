package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FAQ {
    private Integer faq;
    private String type;
    private String state;
    private String title;
    private String content;
}
