package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
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
@RequestMapping("/main/*")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MainService mainService;

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

    @RequestMapping(value="/admin/value", method= RequestMethod.POST)
    public ResponseOverlays getAdminValue(@Validated @RequestBody SEARCH search) {
        try {
            Integer result = mainService.getAdminValue(search);
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

    @RequestMapping(value="/category/keyword/list", method= RequestMethod.POST)
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

    @RequestMapping(value="/find", method= RequestMethod.POST)
    public ResponseOverlays findPassword(@Validated @RequestBody SEARCH search) {
        try {
            MEMBER result = mainService.find(search);
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
}