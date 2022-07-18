package com.jamie.home.api.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TOKEN {
    private MEMBER member;
    private String token;

    public TOKEN (MEMBER member, String token){
        this.member = member;
        this.token = token;
    }
}
