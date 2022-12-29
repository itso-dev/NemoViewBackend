package com.jamie.home.api.service;

import com.jamie.home.api.model.*;
import com.jamie.home.util.FileUtils;
import com.jamie.home.util.KeywordUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ReviewService extends BasicService{
    public List<REVIEW> list(SEARCH search) throws Exception {
        if(search.getKeywords() != null){
            search.setKeywordList(KeywordUtils.getKeywordListFromSearch(search.getKeywords()));
        }
        if(search.getSearchKeyword() != null && !"".equals(search.getSearchKeyword())){
            if(search.getAdmin() == null || !search.getAdmin()){
                searchDao.insertSearchKeyword(search);
            }
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
        MEMBER reviewMember = memberDao.getMember(param);
        List<Keywords> common = KeywordUtils.getCommonKeyword(reviewMember);

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

            // 리뷰 작성자 키워드 저장
            List<KEYWORD> addList = new ArrayList<>();
            List<KEYWORD> keywordList = memberDao.getListMemberKeyword(reviewMember);
            for(int i=0; i<keywords.size(); i++){
                boolean add = true;
                for(int j=0; j<keywordList.size(); j++){
                    if(keywordList.get(j).getKeyword().equals(keywords.get(i).getKeyword())){
                        add = false;
                        break;
                    }
                }
                if(add){
                    addList.add(keywords.get(i));
                }
            }

            for(int i=0; i<addList.size(); i++){
                keywordList.add(addList.get(i));
            }

            // MEMBER_KEYWORD 수정
            SEARCH search2 = new SEARCH();
            search2.setMember(reviewMember.getMember());
            memberDao.deleteAllMemberKeywrod(search2);
            search2.setReview_keywords(keywordList);
            memberDao.insertMemberKeywrod(search2);

            // 회원정보에서 공통키워드 추출
            List<Keywords> memberCommon = KeywordUtils.getCommonKeyword(reviewMember);
            List<Keywords> memberMandatory = memberDao.getMandatoryKeyword(reviewMember);
            reviewMember.setKeywords(KeywordUtils.getKeywordsValue(memberCommon, memberMandatory, null));
            memberDao.updateMemberKeywords(reviewMember);
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

        MEMBER param = new MEMBER();
        param.setMember(review.getMember());
        MEMBER reviewMember = memberDao.getMember(param);
        List<Keywords> common = KeywordUtils.getCommonKeyword(reviewMember);

        List<Keywords> mandatory = KeywordUtils.getMandatoryKeyword(review.getKeywordList());

        List<Keywords> input = KeywordUtils.getInputKeyword(review.getKeywordInputList());
        // 키워드 저장
        review.setKeywords(KeywordUtils.getKeywordsValue(common, mandatory, input));
        List<KEYWORD> keywords = KeywordUtils.getMandatoryKeywordForSave(review.getKeywordList());

        review.setSavePhotos(null);
        review.setSaveVideos(null);
        //review.setState("1"); // 검수단계로 변경
        int result = reviewDao.updateReview(review);

        // 리뷰 키워드 저장
        SEARCH search = new SEARCH();
        search.setReview(review.getReview());
        reviewDao.deleteAllReviewKeywrod(search);
        if(keywords != null && keywords.size() != 0){
            search.setReview_keywords(keywords);
            reviewDao.insertReviewKeywrod(search);

            // 리뷰 작성자 키워드 저장
            List<KEYWORD> addList = new ArrayList<>();
            List<KEYWORD> keywordList = memberDao.getListMemberKeyword(reviewMember);
            for(int i=0; i<keywords.size(); i++){
                boolean add = true;
                for(int j=0; j<keywordList.size(); j++){
                    if(keywordList.get(j).getKeyword().equals(keywords.get(i).getKeyword())){
                        add = false;
                        break;
                    }
                }
                if(add){
                    addList.add(keywords.get(i));
                }
            }

            for(int i=0; i<addList.size(); i++){
                keywordList.add(addList.get(i));
            }

            // MEMBER_KEYWORD 수정
            SEARCH search2 = new SEARCH();
            search2.setMember(reviewMember.getMember());
            memberDao.deleteAllMemberKeywrod(search2);
            search2.setReview_keywords(keywordList);
            memberDao.insertMemberKeywrod(search2);

            // 회원정보에서 공통키워드 추출
            List<Keywords> memberCommon = KeywordUtils.getCommonKeyword(reviewMember);
            List<Keywords> memberMandatory = memberDao.getMandatoryKeyword(reviewMember);
            reviewMember.setKeywords(KeywordUtils.getKeywordsValue(memberCommon, memberMandatory, null));
            memberDao.updateMemberKeywords(reviewMember);
        }
        return result;
    }

    public Integer like(REVIEW_LIKE like) {
        return reviewDao.upsertReviewlike(like);
    }

    public Integer saveReply(REVIEW_REPLY reply) {
        int result = reviewDao.insertReviewReply(reply);

        if(result != 0){
            if(reply.getReply_key() == null){ // 댓글
                REVIEW param = new REVIEW();
                param.setReview(reply.getReview());
                REVIEW review = reviewDao.getReview(param);

                // 댓글달림 알림 TYPE 9
                INFO info = new INFO();
                info.setValues(review.getMember(),
                        "9",
                        review.getReview(),
                        "내 리뷰에 댓글이 달렸어요! 지금 댓글을 확인해 보세요!",
                        "",
                        review.getPhoto());
                infoDao.insertInfo(info);
                sendPushMessage(review.getMember(), "리뷰댓글", "내 리뷰에 댓글이 달렸어요! 지금 댓글을 확인해 보세요!");
            } else { // 대댓글
                REVIEW param = new REVIEW();
                param.setReview(reply.getReview());
                REVIEW review = reviewDao.getReview(param);
                // 댓글 단 회원의 키값 필요
                REVIEW_REPLY param_review_reply = new REVIEW_REPLY();
                param_review_reply.setReply(reply.getReply_key());
                REVIEW_REPLY review_reply = reviewDao.getReviewReply(param_review_reply);

                // 리뷰 대댓글달림 알림 TYPE 12
                INFO info = new INFO();
                info.setValues(review_reply.getMember(),
                        "12",
                        review.getReview(),
                        "내 댓글에 대댓글이 달렸어요! 지금 대댓글을 확인해 보세요!",
                        "",
                        review.getPhoto());
                infoDao.insertInfo(info);
                sendPushMessage(review.getMember(), "리뷰댓글", "내 댓글에 대댓글이 달렸어요! 지금 대댓글을 확인해 보세요!");
            }
        }

        return result;
    }

    public List<REVIEW_REPLY> listReply(REVIEW review) {
        List<REVIEW_REPLY> list = reviewDao.getListReviewReply(review);
        for(int i=0; i< list.size(); i++){
            REVIEW_REPLY reply = list.get(i);
            review.setReply_key(reply.getReply());
            list.get(i).setRe_replyList(reviewDao.getListReviewReply(review));
        }
        return list;
    }

    public Integer listReplyCnt(REVIEW review) {
        return reviewDao.getListReviewReplyCnt(review);
    }

    public Integer modiReviewState(REVIEW review){
        REVIEW reviewInfo = reviewDao.getReview(review);
        if("2".equals(review.getState()) && review.getPoint() != null){ // 리뷰 승인
            // 포인트 적립
            POINT point = new POINT();
            point.setValues(reviewInfo.getMember(), "1", review.getPoint(), "리뷰승인", "1");
            pointDao.insertPoint(point);

            // 회원 포인트 업데이트
            MEMBER member = new MEMBER();
            member.setMember(reviewInfo.getMember());
            MEMBER memberInfo = memberDao.getMember(member);
            memberInfo.setPoint(review.getPoint());
            memberDao.updateMemberPoint(memberInfo);

            // 리뷰승인 알림 TYPE 1
            INFO info = new INFO();
            info.setValues(reviewInfo.getMember(),
                    "1",
                    reviewInfo.getReview(),
                    "리뷰가 승인되어 "+review.getPoint()+"포인트가 지급 되었어요! 지금 등록된 리뷰를 확인해 보세요.",
                    "",
                    reviewInfo.getPhoto());
            infoDao.insertInfo(info);
            sendPushMessage(reviewInfo.getMember(), "리뷰", "리뷰가 승인되어 "+review.getPoint()+"포인트가 지급 되었어요! 지금 등록된 리뷰를 확인해 보세요.");

        } else if("4".equals(review.getState())) { // 리뷰 반려
            // 리뷰반려 알림 TYPE 2
            INFO info = new INFO();
            info.setValues(reviewInfo.getMember(),
                    "2",
                    reviewInfo.getReview(),
                    "리뷰 등록이 거절되었어요. 사유를 확인 후 다시 등록해 주세요.",
                    review.getReject(),
                    reviewInfo.getPhoto());
            infoDao.insertInfo(info);
            sendPushMessage(reviewInfo.getMember(), "리뷰", "리뷰 등록이 거절되었어요. 사유를 확인 후 다시 등록해 주세요.");
        }
        return reviewDao.updateReviewState(review);
    }

    public CATEGORY getCategory(CATEGORY category) {
        return categoryDao.getCategoryWithKeyword(category);
    }

    public Integer removeReply(REVIEW_REPLY reply) {
        return reviewDao.deleteReply(reply);
    }

    public PROFILE_CNT listTotalCnt(SEARCH search) {
        PROFILE_CNT result = reviewDao.listTotalCnt(search);
        result.setReviewCnt(reviewDao.getListReviewCnt(search));
        return result;
    }
}
