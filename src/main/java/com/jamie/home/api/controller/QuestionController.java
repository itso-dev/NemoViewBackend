package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/question/*")
public class QuestionController {

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    private QuestionService questionService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            if(search.getPage() != null && search.getPage_block() != null && search.getPage() > 0 && search.getPage_block() > 0){
                search.calStart();
            }

            List<QUESTION> list = questionService.list(search);

            if(list != null){
                Integer cnt = questionService.listCnt(search);
                VoList<QUESTION> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_QUESTION_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_FAIL", null);
        }
    }

    @RequestMapping(value="/cnt", method= RequestMethod.POST)
    public ResponseOverlays listCnt(@Validated @RequestBody SEARCH search) {
        try {
            PROFILE_CNT result = new PROFILE_CNT();
            result.setQuestionCnt(questionService.listCnt(search));
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

    @RequestMapping(value="/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
        try {
            QUESTION question = new QUESTION();
            question.setQuestion(key);
            questionService.upHits(question);
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

    @RequestMapping(value="/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays save(@Validated @ModelAttribute QUESTION question) {
        try {
            int result = questionService.save(question);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_QUESTION_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.PUT, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays modi(@PathVariable("key") int key, @Validated @ModelAttribute QUESTION question) {
        try {
            question.setQuestion(key);
            int result = questionService.modi(question);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_QUESTION_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_FAIL", false);
        }
    }

    @RequestMapping(value="/{key}/like", method= RequestMethod.POST)
    public ResponseOverlays like(@PathVariable("key") int key, @Validated @RequestBody QUESTION question) {
        try {
            question.setQuestion(key);
            int result = questionService.like(question);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_LIKE_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_QUESTION_LIKE_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_LIKE_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}/islike", method= RequestMethod.POST)
    public ResponseOverlays islike(@PathVariable("key") int key, @Validated @RequestBody QUESTION question) {
        try {
            question.setQuestion(key);
            int result = questionService.getLike(question);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_LIKE_NULL", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_QUESTION_LIKE_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_QUESTION_LIKE_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}/answer/list", method= RequestMethod.POST)
    public ResponseOverlays listAnswer(@PathVariable("key") int key, @Validated @RequestBody SEARCH search) {
        try {
            search.setQuestion(key);
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

    @RequestMapping(value="/{key}/answer/save", method= RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseOverlays saveAnswer(@PathVariable("key") int key, @Validated @ModelAttribute QUESTION_ANSWER answer) {
        try {
            answer.setQuestion(key);
            int result = questionService.saveAnswer(answer);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_ANSWER_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_QUESTION_ANSWER_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_ANSWER_FAIL", false);
        }
    }

    @RequestMapping(value="/answer/{key}/choose", method= RequestMethod.PUT)
    public ResponseOverlays choose(@PathVariable("key") int key, @Validated @RequestBody QUESTION_ANSWER answer) {
        try {
            answer.setAnswer(key);
            int result = questionService.choose(answer);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_LIKE_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_QUESTION_LIKE_SUCCESS", answer);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_LIKE_FAIL", null);
        }
    }

    @RequestMapping(value="/answer/{key}", method= RequestMethod.PUT)
    public ResponseOverlays removeAnswer(@PathVariable("key") int key, @Validated @RequestBody QUESTION_ANSWER answer) {
        try {
            answer.setAnswer(key);
            int result = questionService.removeAnswer(answer);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_REMOVE_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_QUESTION_REMOVE_SUCCESS", answer);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_QUESTION_REMOVE_FAIL", null);
        }
    }

    @RequestMapping(value="/answer/{key}/like", method= RequestMethod.POST)
    public ResponseOverlays likeAnswer(@PathVariable("key") int key, @Validated @RequestBody QUESTION_ANSWER answer) {
        try {
            answer.setAnswer(key);
            int result = questionService.likeAnswer(answer);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_ANSWER_LIKE_NOT_SAVE", false);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_ANSWER_LIKE_SUCCESS", true);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_ANSWER_LIKE_FAIL", null);
        }
    }
}