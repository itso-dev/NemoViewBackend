package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.CategoryService;
import com.jamie.home.api.service.FaqService;
import com.jamie.home.api.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/bm/*")
public class BmController {

    private static final Logger logger = LoggerFactory.getLogger(BmController.class);

    @Autowired
    private MainService mainService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value="/category/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            List<CATEGORY> list = mainService.listCategory(search);
            if(list != null){
                Integer cnt = mainService.listCategoryCnt(search);
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

    @RequestMapping(value="/classification/list", method= RequestMethod.POST)
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

    @RequestMapping(value="/common/keyword/list", method= RequestMethod.POST)
    public ResponseOverlays commonKeywordList(@Validated @RequestBody SEARCH search) {
        try {
            List<COMMON_KEYWORD> list = mainService.listCommonKeyword(search);
            if(list != null){
                VoList<COMMON_KEYWORD> result = new VoList<>(list.size(), list);
                //result.setPage(search.getPage(), search.getPage_block());
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_COMMON_KEYWORD_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_COMMON_KEYWORD_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_COMMON_KEYWORD_FAIL", null);
        }
    }

    @RequestMapping(value="/ad/open", method= RequestMethod.POST)
    public ResponseOverlays modifyAdOpen(@Validated @RequestBody SEARCH search) {
        try {
            if(search.getMember() == null || search.getAd() == null){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "PARAMETER_ERROR", null);
            } else {
                int result = mainService.modifyMemberPointAndMemberAdOpen(search);
                if(result != 0){
                    return new ResponseOverlays(HttpServletResponse.SC_OK, "CARD_OPEN_SUCCESS", result);
                } else {
                    return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "CARD_OPEN_NULL", null);
                }
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "CARD_OPEN_FAIL", null);
        }
    }
}