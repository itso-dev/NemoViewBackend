package com.jamie.home.api.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataBody {
    private String rsp_cd;
    private String res_msg;
    private String result_cd;
    private String site_code;
    private String token_version_id;
    private String token_val;
    private Integer period;
}
