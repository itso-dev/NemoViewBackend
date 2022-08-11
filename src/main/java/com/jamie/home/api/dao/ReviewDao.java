package com.jamie.home.api.dao;

import com.jamie.home.api.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReviewDao {
    List<REVIEW> getListReview(SEARCH search);
    Integer getListReviewCnt(SEARCH search);
    REVIEW getReview(REVIEW review);
    Integer updateReviewHits(REVIEW review);
    Integer insertReview(REVIEW review);
    Integer updateReview(REVIEW review);
    Integer upsertReviewlike(REVIEW_LIKE like);
    Integer insertReviewReply(REVIEW_REPLY reply);
    List<REVIEW_REPLY> getListReviewReply(REVIEW review);
    Integer getListReviewReplyCnt(REVIEW review);
    Integer insertReviewKeywrod(SEARCH search);
    //관리자
    Integer updateReviewState(REVIEW review);
    void deleteAllReviewKeywrod(SEARCH search);
}
