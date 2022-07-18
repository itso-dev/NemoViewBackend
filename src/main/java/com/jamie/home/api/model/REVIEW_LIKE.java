package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class REVIEW_LIKE {
    private Integer like;
    private Integer review;
    private Integer member;
    private Boolean del;
}
