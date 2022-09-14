package com.jamie.home.api.model.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Key {
    private String key;
    private String iv;
    private String hmac_key;
    private String req_dtim;
    private String req_no;
    private String enc_data;
    private String integrity_value;
    private DataBody dataBody;

    public Key() {
    }

    public Key(String key, String iv, String hmac_key, String req_dtim, String req_no, String enc_data, String integrity_value, DataBody dataBody) {
        this.key = key;
        this.iv = iv;
        this.hmac_key = hmac_key;
        this.req_dtim = req_dtim;
        this.req_no = req_no;
        this.enc_data = enc_data;
        this.integrity_value = integrity_value;
        this.dataBody = dataBody;
    }
}
