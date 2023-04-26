package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.*;
import com.jamie.home.jwt.JwtFilter;
import com.jamie.home.jwt.TokenProvider;
import com.jamie.home.util.FirebaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
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
    private BasicService basicService;
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
    @Autowired
    private ReportService reportService;

    @RequestMapping(value="/member/login", method= RequestMethod.POST)
    public ResponseOverlays login(@Value("${jwt.token-validity-in-seconds}") Double expirySec, @Validated @RequestBody MEMBER member) {
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
                    return new ResponseOverlays(HttpServletResponse.SC_OK, "LOGIN_MEMBER_SUCCESS", new TOKEN(result, jwt, (expirySec / 3600.0)));
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
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }
            List<MEMBER> list = memberService.list(search);
            if(list != null){
                Integer cnt = memberService.listCnt(search);
                VoList<MEMBER> result = new VoList<>(cnt, list);
                //result.setPage(search.getPage(), search.getPage_block());
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
    
    @RequestMapping(value="member/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays removeMember(@PathVariable("key") int key) {
        try {
            MEMBER member = new MEMBER();
            member.setMember(key);
            int result = memberService.removeMember(member);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_MEMBER_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "DELETE_MEMBER_SUCCESS", member);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_MEMBER_FAIL", null);
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
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }
            search.setAdmin(true);
            List<REVIEW> list = reviewService.list(search);

            if(list != null){
                Integer cnt = reviewService.listCnt(search);
                VoList<REVIEW> result = new VoList<>(cnt, list);
                //result.setPage(search.getPage(), search.getPage_block());
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
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }
            List<QUESTION> list = questionService.list(search);

            if(list != null){
                Integer cnt = questionService.listCnt(search);
                VoList<QUESTION> result = new VoList<>(cnt, list);
                //result.setPage(search.getPage(), search.getPage_block());
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
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }
            List<POINT> list = pointService.list(search);

            if(list != null){
                Integer cnt = pointService.listCnt(search);
                VoList<POINT> result = new VoList<>(cnt, list);
                //result.setPage(search.getPage(), search.getPage_block());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_POINT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_POINT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_POINT_FAIL", null);
        }
    }

    @RequestMapping(value="/point/list/cnt", method= RequestMethod.POST)
    public ResponseOverlays listPointCnts(@Validated @RequestBody SEARCH search) {
        try {
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }
            POINT point = pointService.listCnts(search);

            if(point != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_POINT_SUCCESS", point);
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

    @RequestMapping(value="/point/state/all", method= RequestMethod.PUT)
    public ResponseOverlays modiPointStateAll(@Validated @RequestBody SEARCH search) {
        try {
            int result = pointService.modiPointStateAll(search);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_POINT_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_POINT_SUCCESS", search);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_POINT_FAIL", null);
        }
    }

    @RequestMapping(value="/category/list", method= RequestMethod.POST)
    public ResponseOverlays categoryList(@Validated @RequestBody SEARCH search) {
        try {
            List<CATEGORY> list = categoryService.list(search);
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
            CATEGORY result = categoryService.getCategory(category);
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

    @RequestMapping(value="/category/classification/list", method= RequestMethod.POST)
    public ResponseOverlays classificationList(@Validated @RequestBody SEARCH search) {
        try {
            List<CATEGORY_CLASSIFICATION> list = categoryService.listClassification(search);
            if(list != null){
                VoList<CATEGORY_CLASSIFICATION> result = new VoList<>(list.size(), list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CLASSIFICATION_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CLASSIFICATION_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CLASSIFICATION_FAIL", null);
        }
    }

    @RequestMapping(value="/category/classification/{key}", method= RequestMethod.GET)
    public ResponseOverlays getClassification(@PathVariable("key") int key) {
        try {
            CATEGORY_CLASSIFICATION classification = new CATEGORY_CLASSIFICATION();
            classification.setClassification(key);
            CATEGORY_CLASSIFICATION result = categoryService.getClassification(classification);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_CLASSIFICATION_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CLASSIFICATION_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_CLASSIFICATION_FAIL", null);
        }
    }

    @RequestMapping(value="/category/classification/save", method= RequestMethod.POST)
    public ResponseOverlays saveClassification(@Validated @RequestBody CATEGORY_CLASSIFICATION classification) {
        try {
            int result = categoryService.saveClassification(classification);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CLASSIFICATION_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CLASSIFICATION_SUCCESS", classification);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CLASSIFICATION_FAIL", null);
        }
    }

    @RequestMapping(value="/category/classification/{key}", method= RequestMethod.PUT)
    public ResponseOverlays modiClassification(@PathVariable("key") int key, @Validated @RequestBody CATEGORY_CLASSIFICATION classification) {
        try {
            classification.setClassification(key);
            int result = categoryService.modiClassification(classification);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CLASSIFICATION_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_CLASSIFICATION_SUCCESS", classification);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_CLASSIFICATION_FAIL", null);
        }
    }

    @RequestMapping(value="/category/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays removeCategory(@PathVariable("key") int key) {
        try {
            CATEGORY category = new CATEGORY();
            category.setCategory(key);
            int result = categoryService.removeCategory(category);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_CATEGORY_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "DELETE_CATEGORY_SUCCESS", category);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_CATEGORY_FAIL", null);
        }
    }

    @RequestMapping(value="/category/classification/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays modiClassification(@PathVariable("key") int key) {
        try {
            CATEGORY_CLASSIFICATION classification = new CATEGORY_CLASSIFICATION();
            classification.setClassification(key);
            int result = categoryService.removeClassification(classification);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_CLASSIFICATION_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "DELETE_CLASSIFICATION_SUCCESS", classification);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_CLASSIFICATION_FAIL", null);
        }
    }

    @RequestMapping(value="/contact/list", method= RequestMethod.POST)
    public ResponseOverlays listContact(@Validated @RequestBody SEARCH search) {
        try {
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }
            List<CONTACT> list = contactService.list(search);

            if(list != null){
                Integer cnt = contactService.listCnt(search);
                VoList<CONTACT> result = new VoList<>(cnt, list);
                //result.setPage(search.getPage(), search.getPage_block());
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
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }
            List<FAQ> list = faqService.list(search);
            if(list != null){
                Integer cnt = faqService.listCnt(search);
                VoList<FAQ> result = new VoList<>(cnt, list);
                //result.setPage(search.getPage(), search.getPage_block());
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

    @RequestMapping(value="/dash", method= RequestMethod.POST)
    public ResponseOverlays dashInfo(@Validated @RequestBody SEARCH search) {
        try {
            DASH result = mainService.adminDashInfo(search);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_DASH_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_DASH_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_DASH_FAIL", null);
        }
    }

    @RequestMapping(value="/banner/list", method= RequestMethod.POST)
    public ResponseOverlays listBanner(@Validated @RequestBody SEARCH search) {
        try {
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }
            List<BANNER> list = bannerService.list(search);
            if(list != null){
                Integer cnt = bannerService.listCnt(search);
                VoList<BANNER> result = new VoList<>(cnt, list);
                //result.setPage(search.getPage(), search.getPage_block());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_BANNER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BANNER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BANNER_FAIL", null);
        }
    }

    @RequestMapping(value="/banner/member/list", method= RequestMethod.POST)
    public ResponseOverlays listBannerMember(@Validated @RequestBody SEARCH search) {
        try {
            List<MEMBER> list = bannerService.listMember(search);
            if(list != null){
                VoList<MEMBER> result = new VoList<>(list.size(), list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_BANNER_MEMBER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BANNER_MEMBER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BANNER_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/banner/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays saveBanner(@Validated @ModelAttribute BANNER banner) {
        try {
            int result = bannerService.save(banner);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BANNER_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_BANNER_SUCCESS", banner);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BANNER_FAIL", null);
        }
    }

    @RequestMapping(value="/banner/{key}", method= RequestMethod.GET)
    public ResponseOverlays getBanner(@PathVariable("key") int key) {
        try {
            BANNER banner = new BANNER();
            banner.setBanner(key);
            BANNER result = bannerService.get(banner);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_BANNER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BANNER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BANNER_FAIL", null);
        }
    }

    @RequestMapping(value="/banner/{key}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modiBanner(@PathVariable("key") int key, @Validated @ModelAttribute BANNER banner) {
        try {
            banner.setBanner(key);
            int result = bannerService.modi(banner);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BANNER_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_BANNER_SUCCESS", banner);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BANNER_FAIL", null);
        }
    }

    @RequestMapping(value="/banner/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays removeBanner(@PathVariable("key") int key) {
        try {
            BANNER banner = new BANNER();
            banner.setBanner(key);
            Integer result = bannerService.removeBanner(banner);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "DELETE_BANNER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETEBANNER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_BANNER_FAIL", null);
        }
    }

    @RequestMapping(value="/report/list", method= RequestMethod.POST)
    public ResponseOverlays listReport(@Validated @RequestBody SEARCH search) {
        try {
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }
            List<REPORT> list = reportService.list(search);

            if(list != null){
                Integer cnt = reportService.listCnt(search);
                VoList<REPORT> result = new VoList<>(cnt, list);
                //result.setPage(search.getPage(), search.getPage_block());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_REPORT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REPORT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REPORT_FAIL", null);
        }
    }

    @RequestMapping(value="/report/{key}", method= RequestMethod.GET)
    public ResponseOverlays getReport(@PathVariable("key") int key) {
        try {
            REPORT report = new REPORT();
            report.setReport(key);
            REPORT result = reportService.get(report);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_REPORT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REPORT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_REPORT_FAIL", null);
        }
    }
    @RequestMapping(value="/send/push", method= RequestMethod.POST)
    public ResponseOverlays sendPush(@Validated @RequestBody SEARCH search) {
        try {
            basicService.sendPushMessage(null,search.getTitle(), search.getBody());
            return new ResponseOverlays(HttpServletResponse.SC_OK, "SEND_PUSH_SUCCESS", true);
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SEND_PUSH_FAIL", null);
        }
    }

    @RequestMapping(value="/category/common/list", method= RequestMethod.POST)
    public ResponseOverlays commonKeywordList(@Validated @RequestBody SEARCH search) {
        try {
            List<COMMON_KEYWORD_LIST> list = categoryService.listCommonKeyword(search);
            if(list != null){
                VoList<COMMON_KEYWORD_LIST> result = new VoList<>(list.size(), list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_COMMON_KEYWORD_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_COMMON_KEYWORD_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_COMMON_KEYWORD_FAIL", null);
        }
    }

    @RequestMapping(value="/category/common/{key}", method= RequestMethod.GET)
    public ResponseOverlays getCommonKeyword(@PathVariable("key") int key) {
        try {
            COMMON_KEYWORD common_keyword = new COMMON_KEYWORD();
            common_keyword.setCommon_keyword(key);
            COMMON_KEYWORD_LIST result = categoryService.getCommonKeyword(common_keyword);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_COMMON_KEYWORD_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_COMMON_KEYWORD_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_COMMON_KEYWORD_FAIL", null);
        }
    }

    @RequestMapping(value="/category/common/save", method= RequestMethod.POST)
    public ResponseOverlays saveCommonKeyword(@Validated @RequestBody COMMON_KEYWORD_LIST common_keyword_list) {
        try {
            int result = categoryService.saveCommonKeyword(common_keyword_list);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_COMMON_KEYWORD_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_COMMON_KEYWORD_SUCCESS", common_keyword_list);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_COMMON_KEYWORD_FAIL", null);
        }
    }

    @RequestMapping(value="/category/common/{key}", method= RequestMethod.PUT)
    public ResponseOverlays modiCommonKeyword(@PathVariable("key") int key, @Validated @RequestBody COMMON_KEYWORD_LIST common_keyword_list) {
        try {
            common_keyword_list.getCommon_keyword().setCommon_keyword(key);
            int result = categoryService.modiCommonKeyword(common_keyword_list);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_COMMON_KEYWORD_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_COMMON_KEYWORD_SUCCESS", common_keyword_list);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_COMMON_KEYWORD_FAIL", null);
        }
    }

    @RequestMapping(value="/category/common/{key}", method= RequestMethod.DELETE)
    public ResponseOverlays removeCommonKeyword(@PathVariable("key") int key) {
        try {
            COMMON_KEYWORD common_keyword = new COMMON_KEYWORD();
            common_keyword.setCommon_keyword(key);
            int result = categoryService.removeCommonKeyword(common_keyword);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_COMMON_KEYWORD_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "DELETE_COMMON_KEYWORD_SUCCESS", common_keyword);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_COMMON_KEYWORD_FAIL", null);
        }
    }
}