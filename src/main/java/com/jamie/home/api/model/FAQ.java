package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Date;

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
