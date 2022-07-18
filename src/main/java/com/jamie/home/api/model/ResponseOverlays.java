package com.jamie.home.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseOverlays<T> {

    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("CODE")
    public int CODE;
    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("IDEA")
    public String IDEA;
    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("BODY")
    public T BODY;

    public ResponseOverlays(int CODE, String IDEA, T BODY) {
        this.CODE = CODE;
        this.IDEA = IDEA;
        this.BODY = BODY;
    }
}