package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }

            List<REVIEW> list = reviewService.list(search);

            if(list != null){
                Integer cnt = reviewService.listCnt(search);
                VoList<REVIEW> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_REVIEW_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_FAIL", null);
        }
    }

    @RequestMapping(value="/cnt", method= RequestMethod.POST)
    public ResponseOverlays listCnt(@Validated @RequestBody SEARCH search) {
        try {
            PROFILE_CNT result = reviewService.listTotalCnt(search);

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

    @RequestMapping(value="/{key}", method= RequestMethod.POST)
    public ResponseOverlays getForPost(@PathVariable("key") int key, @Validated @RequestBody SEARCH search) {
        try {
            REVIEW review = new REVIEW();
            review.setReview(key);
            review.setMember(search.getMember());
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

    @RequestMapping(value="/{key}/hits", method= RequestMethod.PUT)
    public ResponseOverlays modiHits(@PathVariable("key") int key, @Validated @RequestBody REVIEW review) {
        try {
            review.setReview(key);
            int result = reviewService.upHits(review);
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
    public ResponseOverlays listReply(@PathVariable("key") int key, @Validated @RequestBody REVIEW review) {
        try {
            review.setReview(key);
            List<REVIEW_REPLY> list = reviewService.listReply(review);

            if(list != null){
                Integer cnt = reviewService.listReplyCnt(review);
                VoList<REVIEW_REPLY> result = new VoList<>(cnt, list);
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

    @RequestMapping(value="/reply/{key}", method= RequestMethod.PUT)
    public ResponseOverlays removeReply(@PathVariable("key") int key, @Validated @RequestBody REVIEW_REPLY reply) {
        try {
            reply.setReply(key);
            int result = reviewService.removeReply(reply);
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

    @RequestMapping(value="/category/{key}/keyword", method= RequestMethod.POST)
    public ResponseOverlays getCategory(@PathVariable("key") int key, @Validated @RequestBody CATEGORY category) {
        try {
            category.setCategory(key);
            CATEGORY result = reviewService.getCategory(category);
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
    
    @RequestMapping(value="/reply/{key}/like", method= RequestMethod.POST)
    public ResponseOverlays likeReply(@PathVariable("key") int key, @Validated @RequestBody REVIEW_REPLY reply) {
        try {
            reply.setReply(key);
            int result = reviewService.likeReply(reply);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REPLY_LIKE_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_REPLY_LIKE_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_REPLY_LIKE_FAIL", null);
        }
    }
}