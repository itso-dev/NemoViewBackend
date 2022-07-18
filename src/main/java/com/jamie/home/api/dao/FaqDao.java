package com.jamie.home.api.dao;

import com.jamie.home.api.model.FAQ;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface FaqDao {
    List<FAQ> getListFaq(SEARCH search);

    Integer getListFaqCnt(SEARCH search);
}
