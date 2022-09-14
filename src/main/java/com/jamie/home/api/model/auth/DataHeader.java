package com.jamie.home.api.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataHeader {
    private String cnty_cd;
    private String gw_rslt_cd;
    private String gw_rslt_msg;
}
