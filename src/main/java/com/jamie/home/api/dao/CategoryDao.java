package com.jamie.home.api.dao;

import com.jamie.home.api.model.CATEGORY;
import com.jamie.home.api.model.CATEGORY_CLASSIFICATION;
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

    List<CATEGORY> getCategoryWithKeywordList(SEARCH search);

    Integer insertCategory(CATEGORY category);

    Integer insertCategoryClassification(CATEGORY_CLASSIFICATION cTarget);

    Integer insertCategoryKeywords(CATEGORY_CLASSIFICATION cTarget);

    Integer updateCategory(CATEGORY category);

    Integer deleteAllClassifications(CATEGORY category);

    Integer deleteAllKeywords(CATEGORY category);
}
