package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.ContactService;
import com.jamie.home.api.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/review/*")
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            search.calStart();
            List<REVIEW> list = reviewService.list(search);

            // 필수키워드 정보, 댓글 정보, 도움수 정보, 회원키 있을 시 도움 했는 지 정보
            for(int i=0; i<list.size(); i++){
                REVIEW review = new REVIEW();
                review.setReview(list.get(i).getReview());
                list.get(i).setKeywordList(reviewService.keywordList(review));
                list.get(i).setReplyCnt(reviewService.replyListCnt(review));
                list.get(i).setLikeCnt(reviewService.likeListCnt(review));
                list.get(i).setLikeYn(reviewService.likeYn(review));
            }
            Integer cnt = reviewService.listCnt(search);
            VoList<REVIEW> result = new VoList<>(cnt, list);
            result.setPage(search.getPage(), search.getPage_block());

            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_REVIEW_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
        try {
            REVIEW review = new REVIEW();
            review.setReview(key);
            reviewService.upHits(review);
            REVIEW result = reviewService.get(review);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_REVIEW_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_FAIL", null);
        }
    }

    @RequestMapping(value="/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@Validated @ModelAttribute REVIEW review) {
        try {
            int result = reviewService.save(review);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REVIEW_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_REVIEW_SUCCESS", review);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REVIEW_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modi(@PathVariable("key") int key, @Validated @ModelAttribute REVIEW review) {
        try {
            review.setReview(key);
            int result = reviewService.modi(review);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REVIEW_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_REVIEW_SUCCESS", review);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REVIEW_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}/like", method= RequestMethod.POST)
    public ResponseOverlays like(@PathVariable("key") int key, @Validated @RequestBody REVIEW_LIKE like) {
        try {
            like.setReview(key);
            int result = reviewService.like(like);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REVIEW_LIKE_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_REVIEW_LIKE_SUCCESS", like);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REVIEW_LIKE_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}/reply/list", method= RequestMethod.POST)
    public ResponseOverlays replyList(@PathVariable("key") int key, @Validated @RequestBody REVIEW review) {
        try {
            review.setReview(key);
            List<REVIEW_REPLY> list = reviewService.replyList(review);
            Integer cnt = reviewService.replyListCnt(review);
            VoList<REVIEW_REPLY> result = new VoList<>(cnt, list);

            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_REVIEW_REPLY_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_REPLY_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_REPLY_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}/reply/save", method= RequestMethod.POST)
    public ResponseOverlays saveReply(@PathVariable("key") int key, @Validated @RequestBody REVIEW_REPLY reply) {
        try {
            reply.setReview(key);
            int result = reviewService.saveReply(reply);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REVIEW_REPLY_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_REVIEW_REPLY_SUCCESS", reply);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REVIEW_REPLY_FAIL", null);
        }
    }
}