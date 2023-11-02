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
    List<REVIEW_REPLY> getListReviewReply(SEARCH search);
    Integer getListReviewReplyCnt(SEARCH search);
    Integer insertReviewKeywrod(SEARCH search);
    //관리자
    Integer updateReviewState(REVIEW review);
    void deleteAllReviewKeywrod(SEARCH search);
    Integer deleteReply(REVIEW_REPLY reply);
    REVIEW_REPLY getReviewReply(REVIEW_REPLY param_review_reply);

    PROFILE_CNT listTotalCnt(SEARCH search);

    List<KEYWORD> getListMemberKeywordInCategory(REVIEW review);

    Integer getCountReviewModify(REVIEW review);

    Integer getReviewReplyLike(REVIEW_REPLY reply);

    Integer deleteReviewReplyLike(REVIEW_REPLY reply);

    Integer insertReviewReplyLike(REVIEW_REPLY reply);
    Integer deleteReview(REVIEW review);
}
