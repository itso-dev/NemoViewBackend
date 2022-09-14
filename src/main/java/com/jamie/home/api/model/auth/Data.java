package com.jamie.home.api.model.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Data {
    private DataHeader dataHeader;
    private DataBody dataBody;
}
