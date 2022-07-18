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

    int updateReviewHits(REVIEW review);

    int insertReview(REVIEW review);

    int updateReview(REVIEW review);

    int upsertReviewlike(REVIEW_LIKE like);

    List<REVIEW_REPLY> getListReviewReply(REVIEW review);

    int insertReviewReply(REVIEW_REPLY reply);

    List<KEYWORD> getListReviewKeword(REVIEW review);

    Integer getListReviewReplyCnt(REVIEW review);

    Integer getListReviewLikeCnt(REVIEW review);

    Integer getListReviewLikeYn(REVIEW review);
}
