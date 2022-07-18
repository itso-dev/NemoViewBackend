package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.model.*;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class ReviewService extends BasicService{
    public List<REVIEW> list(SEARCH search) {
        return reviewDao.getListReview(search);
    }

    public Integer listCnt(SEARCH search) {
        return reviewDao.getListReviewCnt(search);
    }

    public REVIEW get(REVIEW review){
        return reviewDao.getReview(review);
    }

    public int upHits(REVIEW review){
        return reviewDao.updateReviewHits(review);
    }

    public int save(REVIEW review) throws Exception {
        review.setPhoto(
                FileUtils.saveFiles(
                        review.getSavePhotos(),
                        uploadDir
                )
        );
        review.setVideo(
                FileUtils.saveFiles(
                        review.getSaveVideos(),
                        uploadDir
                )
        );

        review.setSavePhotos(null);
        review.setSaveVideos(null);
        return reviewDao.insertReview(review);
    }

    public int modi(REVIEW review) throws Exception {
        review.setPhoto(
                FileUtils.modiFiles(
                        review.getPhoto(),
                        review.getDeletePhotos(),
                        review.getSavePhotos(),
                        uploadDir
                )
        );
        review.setVideo(
                FileUtils.modiFiles(
                        review.getVideo(),
                        review.getDeleteVideos(),
                        review.getSaveVideos(),
                        uploadDir
                )
        );

        review.setSavePhotos(null);
        review.setSaveVideos(null);
        return reviewDao.updateReview(review);
    }

    public int like(REVIEW_LIKE like) {
        return reviewDao.upsertReviewlike(like);
    }

    public List<REVIEW_REPLY> replyList(REVIEW review) {
        return reviewDao.getListReviewReply(review);
    }

    public int saveReply(REVIEW_REPLY reply) {
        return reviewDao.insertReviewReply(reply);
    }

    public List<KEYWORD> keywordList(REVIEW review) {
        return reviewDao.getListReviewKeword(review);
    }

    public Integer replyListCnt(REVIEW review) {
        return reviewDao.getListReviewReplyCnt(review);
    }

    public Integer likeListCnt(REVIEW review) {
        return reviewDao.getListReviewLikeCnt(review);
    }

    public Integer likeYn(REVIEW review) {
        return reviewDao.getListReviewLikeYn(review);
    }
}
