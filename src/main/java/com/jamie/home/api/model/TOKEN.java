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
    private String remember;
    private Double expiryHour;

    public TOKEN (MEMBER member, String token, Double expiryHour){
        this.member = member;
        this.token = token;
        this.expiryHour = expiryHour;
    }
}
