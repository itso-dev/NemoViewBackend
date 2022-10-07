package com.jamie.home.api.dao;

import com.jamie.home.api.model.*;
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

    Integer insertCategoryNewKeywords(CATEGORY_CLASSIFICATION cTarget);

    Integer updateCategory(CATEGORY category);

    Integer deleteClassifications(CATEGORY category);

    Integer deleteKeywords(CATEGORY_CLASSIFICATION cTarget);

    List<CATEGORY> listCategoryRank();

    List<KEYWORD> listReviewKeywordRank();

    List<KEYWORD> listQuestionKeywordRank();

    KEYWORD getKeyword(KEYWORD keyword);

    String getKeywordName(KEYWORD keyword);

    Integer updateKeyword(CATEGORY_KEYWORD keyword);

    Integer updateClassification(CATEGORY_CLASSIFICATION cTarget);
}
