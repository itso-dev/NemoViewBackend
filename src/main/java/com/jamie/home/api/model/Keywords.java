package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Keywords {
    private String type;
    private String value;

    public Keywords(String type, String value){
        this.type = type;
        this.value = value;
    }
}
