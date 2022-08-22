package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.*;
import com.jamie.home.jwt.JwtFilter;
import com.jamie.home.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/*")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

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

    @Autowired
    private MainService mainService;
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
    @RequestMapping(value="/member/login", method= RequestMethod.POST)
    public ResponseOverlays login(@Validated @RequestBody MEMBER member) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getEmail(), member.getPassword());

            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = tokenProvider.createToken(authentication);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

            MEMBER result = memberService.checkEmail(member);
            if(jwt != null){
                // 최근 로그인 업데이트
                memberService.updateLogDate(result);
                if(ROLE.ROLE_ADMIN.equals(result.getRole())){
                    return new ResponseOverlays(HttpServletResponse.SC_OK, "LOGIN_MEMBER_SUCCESS", new TOKEN(result, jwt));
                }else {
                    return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "LOGIN_MEMBER_FAIL", null);
                }
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "LOGIN_MEMBER_FAIL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "LOGIN_MEMBER_FAIL", null);
        }
    }
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

    @RequestMapping(value="member/{key}/state", method= RequestMethod.PUT)
    public ResponseOverlays hideMember(@PathVariable("key") int key, @Validated @RequestBody MEMBER member) {
        try {
            member.setMember(key);
            int result = memberService.modiStateMember(member);
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

    @RequestMapping(value="/review/{key}/reject/list", method= RequestMethod.POST)
    public ResponseOverlays listReviewReject(@PathVariable("key") int key, @Validated @RequestBody SEARCH search) {
        try {
            search.setKey(key);
            List<INFO> list = infoService.list(search);

            if(list != null){
                VoList<INFO> result = new VoList<>(list.size(), list);
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
            search.calStart();
            List<POINT> list = pointService.list(search);

            if(list != null){
                Integer cnt = pointService.listCnt(search);
                VoList<POINT> result = new VoList<>(cnt, list);
                result.setPage(search.getPage(), search.getPage_block());
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
    public ResponseOverlays keywordList(@Validated @RequestBody SEARCH search) {
        try {
            List<CATEGORY> list = mainService.listCategoryKeyword(search);
            if(list != null){
                VoList<CATEGORY> result = new VoList<>(list.size(), list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CATEGORY_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CATEGORY_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CATEGORY_FAIL", null);
        }
    }

    @RequestMapping(value="/category/{key}", method= RequestMethod.GET)
    public ResponseOverlays getCategory(@PathVariable("key") int key) {
        try {
            CATEGORY category = new CATEGORY();
            category.setCategory(key);
            CATEGORY result = reviewService.getCategory(category);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CATEGORY_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CATEGORY_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CATEGORY_FAIL", null);
        }
    }

    @RequestMapping(value="/category/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@Validated @ModelAttribute CATEGORY category) {
        try {
            int result = categoryService.save(category);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CATEGORY_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CATEGORY_SUCCESS", category);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CATEGORY_FAIL", null);
        }
    }

    @RequestMapping(value="/category/{key}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@PathVariable("key") int key, @Validated @ModelAttribute CATEGORY category) {
        try {
            category.setCategory(key);
            int result = categoryService.modi(category);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CATEGORY_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CATEGORY_SUCCESS", category);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CATEGORY_FAIL", null);
        }
    }

    @RequestMapping(value="/contact/list", method= RequestMethod.POST)
    public ResponseOverlays listContact(@Validated @RequestBody SEARCH search) {
        try {
            search.calStart();
            List<CONTACT> list = contactService.list(search);

            if(list != null){
                Integer cnt = contactService.listCnt(search);
                VoList<CONTACT> result = new VoList<>(cnt, list);
                result.setPage(search.getPage(), search.getPage_block());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CONTACT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CONTACT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CONTACT_FAIL", null);
        }
    }

    @RequestMapping(value="/contact/{key}", method= RequestMethod.GET)
    public ResponseOverlays getContact(@PathVariable("key") int key) {
        try {
            CONTACT contact = new CONTACT();
            contact.setContact(key);
            CONTACT result = contactService.get(contact);
            if(result != null){
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
            search.calStart();
            List<FAQ> list = faqService.list(search);
            if(list != null){
                Integer cnt = faqService.listCnt(search);
                VoList<FAQ> result = new VoList<>(cnt, list);
                result.setPage(search.getPage(), search.getPage_block());
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