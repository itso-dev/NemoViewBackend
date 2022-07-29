package com.jamie.home.api.dao;

import com.jamie.home.api.model.CATEGORY;
import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryDao {
    List<CATEGORY> getListCategory(SEARCH search);

    Integer getListCategoryCnt(SEARCH search);

    CATEGORY getCategoryWithKeyword(CATEGORY category);
}
