package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.MailService;
import com.jamie.home.api.service.MainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/main/*")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private MainService mainService;

    @Autowired
    private MailService mailService;

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
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_POINT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_POINT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_POINT_FAIL", null);
        }
    }

    @RequestMapping(value="/admin/value", method= RequestMethod.PUT)
    public ResponseOverlays modiKeywords(@Validated @RequestBody SEARCH search) {
        try {
            int result = mainService.modiAdminValue(search);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_MEMBER_SUCCESS", result);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_MEMBER_FAIL", null);
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
                if(search.getEmail() != null){
                    mailService.sendMail(result);
                }
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_MEMBER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/banner/list", method= RequestMethod.POST)
    public ResponseOverlays listBanner(@Validated @RequestBody SEARCH search) {
        try {
            List<BANNER> list = mainService.listBanner(search);
            if(list != null){
                VoList<BANNER> result = new VoList<>(list.size(), list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_BANNER_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BANNER_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_BANNER_FAIL", null);
        }
    }

    @RequestMapping(value="/banner/{key}/hits", method= RequestMethod.PUT)
    public ResponseOverlays modiBannerHits(@PathVariable("key") int key, @Validated @RequestBody SEARCH search) {
        try {
            search.setBanner(key);
            int result = mainService.modiBannerHits(search);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BANNER_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_BANNER_SUCCESS", result);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BANNER_FAIL", null);
        }
    }

    @RequestMapping(value="/banner/{key}/views", method= RequestMethod.PUT)
    public ResponseOverlays modiBannerViews(@PathVariable("key") int key, @Validated @RequestBody SEARCH search) {
        try {
            search.setBanner(key);
            int result = mainService.modiBannerViews(search);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BANNER_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "SAVE_BANNER_SUCCESS", result);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SAVE_BANNER_FAIL", null);
        }
    }

    @RequestMapping(value="/search/list", method= RequestMethod.POST)
    public ResponseOverlays listSearch(@Validated @RequestBody SEARCH search) {
        try {
            List<String> list = mainService.listSearch(search);
            if(list != null){
                VoList<String> result = new VoList<>(list.size(), list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_KEYWORD_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_KEYWORD_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_KEYWORD_FAIL", null);
        }
    }

    @RequestMapping(value="/pageViewUp", method= RequestMethod.POST)
    public ResponseOverlays pageViewUp(@Validated @RequestBody SEARCH search) {
        try {
            Integer result = mainService.increasePageView(search);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "UPDATE_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "UPDATE_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "UPDATE_FAIL", null);
        }
    }

    @RequestMapping(value="/member/{key}/point/month", method= RequestMethod.GET)
    public ResponseOverlays pageViewUp(@PathVariable("key") int key) {
        try {
            MEMBER member = new MEMBER();
            member.setMember(key);
            Integer result = mainService.getPointMonth(member);
            if(result != null){
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_POINT_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_POINT_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_POINT_FAIL", null);
        }
    }
}