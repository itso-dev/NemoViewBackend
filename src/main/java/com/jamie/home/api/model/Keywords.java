package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Keywords {
    private String type;
    private Integer key;
    private String value;

    public Keywords(String type, Integer key, String value){
        this.type = type;
        this.key = key;
        this.value = value;
    }
}
