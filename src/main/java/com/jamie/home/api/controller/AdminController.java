package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/*")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private PointService pointService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BannerService bannerService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private FaqService faqService;
    @Autowired
    private InfoService infoService;
/*
    @RequestMapping(value="/dashboard", method= RequestMethod.POST)
    public ResponseOverlays dashboard(@Validated @RequestBody SEARCH search) {
        try {
            search.calStart();
            List<CATEGORY> list = memberService.listCategory(search);
            if(list != null){
                Integer cnt = memberService.listCategoryCnt(search);
                VoList<CATEGORY> result = new VoList<>(cnt, list);
                result.setPage(search.getPage(), search.getPage_block());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_DASHBOARD_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_DASHBOARD_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_DASHBOARD_FAIL", null);
        }
    }*/

    @RequestMapping(value="/member/list", method= RequestMethod.POST)
    public ResponseOverlays listMember(@Validated @RequestBody SEARCH search) {
        try {
            search.calStart();
            List<MEMBER> list = memberService.list(search);
            if(list != null){
                Integer cnt = memberService.listCnt(search);
                VoList<MEMBER> result = new VoList<>(cnt, list);
                result.setPage(search.getPage(), search.getPage_block());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_MEMBER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="member/{key}", method= RequestMethod.GET)
    public ResponseOverlays getMember(@PathVariable("key") int key) {
        try {
            MEMBER member = new MEMBER();
            member.setMember(key);
            MEMBER result = memberService.get(member);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_MEMBER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="member/{key}/hide", method= RequestMethod.PUT)
    public ResponseOverlays hideMember(@PathVariable("key") int key, @Validated @RequestBody MEMBER member) {
        try {
            member.setMember(key);
            int result = memberService.hideMember(member);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_MEMBER_SUCCESS", member);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/review/list", method= RequestMethod.POST)
    public ResponseOverlays listReview(@Validated @RequestBody SEARCH search) {
        try {
            search.calStart();
            List<REVIEW> list = reviewService.list(search);

            if(list != null){
                Integer cnt = reviewService.listCnt(search);
                VoList<REVIEW> result = new VoList<>(cnt, list);
                result.setPage(search.getPage(), search.getPage_block());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_REVIEW_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REVIEW_FAIL", null);
        }
    }

    @RequestMapping(value="/review/{key}", method= RequestMethod.GET)
    public ResponseOverlays getReview(@PathVariable("key") int key) {
        try {
            REVIEW review = new REVIEW();
            review.setReview(key);
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

    @RequestMapping(value="/review/{key}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modiReview(@PathVariable("key") int key, @Validated @ModelAttribute REVIEW review) {
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

    @RequestMapping(value="/review/{key}/state", method= RequestMethod.PUT)
    public ResponseOverlays modiReviewState(@PathVariable("key") int key, @Validated @RequestBody REVIEW review) {
        try {
            review.setReview(key);
            int result = reviewService.modiReviewState(review);
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

    @RequestMapping(value="/question/list", method= RequestMethod.POST)
    public ResponseOverlays listQuestion(@Validated @RequestBody SEARCH search) {
        try {
            search.calStart();
            List<QUESTION> list = questionService.list(search);

            if(list != null){
                Integer cnt = questionService.listCnt(search);
                VoList<QUESTION> result = new VoList<>(cnt, list);
                result.setPage(search.getPage(), search.getPage_block());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_QUESTION_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_FAIL", null);
        }
    }

    @RequestMapping(value="/question/{key}", method= RequestMethod.GET)
    public ResponseOverlays getQuestion(@PathVariable("key") int key) {
        try {
            QUESTION question = new QUESTION();
            question.setQuestion(key);
            QUESTION result = questionService.get(question);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_QUESTION_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_FAIL", null);
        }
    }

    @RequestMapping(value="/question/answer/list", method= RequestMethod.POST)
    public ResponseOverlays listAnswer(@Validated @RequestBody SEARCH search) {
        try {
            List<QUESTION_ANSWER> list = questionService.listAnswer(search);

            if(list != null){
                Integer cnt = questionService.listAnswerCnt(search);
                VoList<QUESTION_ANSWER> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_QUESTION_ANSWER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_ANSWER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_ANSWER_FAIL", null);
        }
    }

    @RequestMapping(value="/question/{key}/state", method= RequestMethod.PUT)
    public ResponseOverlays modiQuestionState(@PathVariable("key") int key, @Validated @RequestBody QUESTION question) {
        try {
            question.setQuestion(key);
            int result = questionService.modiQuestionState(question);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_QUESTION_SUCCESS", question);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_FAIL", null);
        }
    }

    @RequestMapping(value="/point/list", method= RequestMethod.POST)
    public ResponseOverlays listPoint(@Validated @RequestBody SEARCH search) {
        try {
            List<POINT> list = pointService.list(search);

            if(list != null){
                Integer cnt = pointService.listCnt(search);
                VoList<POINT> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_POINT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_POINT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_POINT_FAIL", null);
        }
    }

    @RequestMapping(value="/point/{key}/state", method= RequestMethod.PUT)
    public ResponseOverlays modiPointState(@PathVariable("key") int key, @Validated @RequestBody POINT point) {
        try {
            point.setPoint(key);
            int result = pointService.modiPointState(point);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_POINT_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_POINT_SUCCESS", point);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_POINT_FAIL", null);
        }
    }

    @RequestMapping(value="/category/list", method= RequestMethod.POST)
    public ResponseOverlays listCategory(@Validated @RequestBody SEARCH search) {
        try {
            List<CATEGORY> list = categoryService.list(search);

            if(list != null){
                Integer cnt = categoryService.listCnt(search);
                VoList<CATEGORY> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CATEGORY_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CATEGORY_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CATEGORY_FAIL", null);
        }
    }

    @RequestMapping(value="/contact/list", method= RequestMethod.POST)
    public ResponseOverlays listContact(@Validated @RequestBody SEARCH search) {
        try {
            List<CONTACT> list = contactService.list(search);

            if(list != null){
                Integer cnt = contactService.listCnt(search);
                VoList<CONTACT> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CONTACT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CONTACT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CONTACT_FAIL", null);
        }
    }

    @RequestMapping(value="/contact/{key}", method= RequestMethod.PUT)
    public ResponseOverlays modiContactAnswer(@PathVariable("key") int key, @Validated @RequestBody CONTACT contact) {
        try {
            contact.setContact(key);
            int result = contactService.modiContactAnswer(contact);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CONTACT_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CONTACT_SUCCESS", contact);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CONTACT_FAIL", null);
        }
    }

    @RequestMapping(value="/faq/list", method= RequestMethod.POST)
    public ResponseOverlays listFaq(@Validated @RequestBody SEARCH search) {
        try {
            List<FAQ> list = faqService.list(search);
            if(list != null){
                Integer cnt = faqService.listCnt(search);
                VoList<FAQ> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_FAQ_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_FAQ_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_FAQ_FAIL", null);
        }
    }

    @RequestMapping(value="/faq/save", method= RequestMethod.POST)
    public ResponseOverlays save(@Validated @RequestBody FAQ faq) {
        try {
            int result = faqService.save(faq);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_FAQ_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_FAQ_SUCCESS", faq);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_FAQ_FAIL", null);
        }
    }

    @RequestMapping(value="/faq/{key}", method= RequestMethod.GET)
    public ResponseOverlays getFaq(@PathVariable("key") int key) {
        try {
            FAQ faq = new FAQ();
            faq.setFaq(key);
            FAQ result = faqService.get(faq);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_FAQ_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_FAQ_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_FAQ_FAIL", null);
        }
    }

    @RequestMapping(value="/faq/{key}", method= RequestMethod.PUT)
    public ResponseOverlays modiFaq(@PathVariable("key") int key, @Validated @RequestBody FAQ faq) {
        try {
            faq.setFaq(key);
            int result = faqService.modi(faq);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_FAQ_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_FAQ_SUCCESS", faq);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_FAQ_FAIL", null);
        }
    }
}