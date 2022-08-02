package com.jamie.home.api.service;

import com.jamie.home.api.model.*;
import com.jamie.home.util.FileUtils;
import com.jamie.home.util.KeywordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReviewService extends BasicService{
    public List<REVIEW> list(SEARCH search) throws Exception {
        if(search.getKeywords() != null){
            search.setKeywordList(KeywordUtils.getKeywordListFromSearch(search.getKeywords()));
        }
        return reviewDao.getListReview(search);
    }

    public Integer listCnt(SEARCH search) {
        return reviewDao.getListReviewCnt(search);
    }

    public REVIEW get(REVIEW review){
        return reviewDao.getReview(review);
    }

    public Integer upHits(REVIEW review){
        return reviewDao.updateReviewHits(review);
    }

    public Integer save(REVIEW review) throws Exception {
        // 파일 저장
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

        MEMBER param = new MEMBER();
        param.setMember(review.getMember());
        List<Keywords> common = KeywordUtils.getCommonKeyword(memberDao.getMember(param));

        List<Keywords> mandatory = KeywordUtils.getMandatoryKeyword(review.getKeywordList());

        List<Keywords> input = KeywordUtils.getInputKeyword(review.getKeywordInputList());
        // 키워드 저장
        review.setKeywords(KeywordUtils.getKeywordsValue(common, mandatory, input));
        List<KEYWORD> keywords = KeywordUtils.getMandatoryKeywordForSave(review.getKeywordList());

        review.setSavePhotos(null);
        review.setSaveVideos(null);
        int result = reviewDao.insertReview(review);

        // 리뷰 키워드 저장
        if(keywords != null && keywords.size() != 0){
            SEARCH search = new SEARCH();
            search.setReview(review.getReview());
            search.setReview_keywords(keywords);
            reviewDao.insertReviewKeywrod(search);
        }

        return result;
    }

    public Integer modi(REVIEW review) throws Exception {
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

    public Integer like(REVIEW_LIKE like) {
        return reviewDao.upsertReviewlike(like);
    }

    public Integer saveReply(REVIEW_REPLY reply) {
        return reviewDao.insertReviewReply(reply);
    }

    public List<REVIEW_REPLY> listReply(REVIEW review) {
        return reviewDao.getListReviewReply(review);
    }

    public Integer listReplyCnt(REVIEW review) {
        return reviewDao.getListReviewReplyCnt(review);
    }

    public Integer modiReviewState(REVIEW review){
        return reviewDao.updateReviewState(review);
    }

    public CATEGORY getCategory(CATEGORY category) {
        return categoryDao.getCategoryWithKeyword(category);
    }
}
