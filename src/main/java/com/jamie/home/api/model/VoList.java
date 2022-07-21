package com.jamie.home.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class VoList<T> {

    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("CNT")
    public int CNT;
    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("PAGE")
    public int PAGE;
    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("PAGE_BLOCK")
    public int PAGE_BLOCK;
    @SuppressWarnings({"unused", "WeakerAccess"})
    @JsonProperty("LIST")
    public List<T> LIST;

    public VoList(int CNT, List<T> LIST) {
        this.CNT = CNT;
        this.LIST = LIST;
    }

    public void setPage(int PAGE, int PAGE_BLOCK){
        this.PAGE = PAGE;
        this.PAGE_BLOCK = PAGE_BLOCK;
    }
}