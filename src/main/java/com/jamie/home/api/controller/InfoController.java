package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.InfoService;
import com.jamie.home.api.service.PointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/info/*")
public class InfoController {

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Autowired
    private InfoService infoService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
        try {
            List<INFO> list = infoService.list(search);

            if(list != null){
                Integer cnt = infoService.listCnt(search);
                VoList<INFO> result = new VoList<>(cnt, list);
                return new ResponseOverlays(HttpServletResponse.SC_OK, "GET_INFO_SUCCESS", result);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_INFO_NULL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "GET_INFO_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}", method= RequestMethod.PUT)
    public ResponseOverlays del(@PathVariable("key") int key, @Validated @RequestBody INFO info) {
        try {
            info.setInfo(key);
            info.setDel(true);
            int result = infoService.modify(info);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_INFO_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "DELETE_INFO_SUCCESS", info);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_INFO_FAIL", null);
        }
    }

    @RequestMapping(value="/del", method= RequestMethod.PUT)
    public ResponseOverlays delAll(@Validated @RequestBody INFO info) {
        try {
            info.setDel(true);
            int result = infoService.modify(info);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_INFO_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "DELETE_INFO_SUCCESS", info);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_INFO_FAIL", null);
        }
    }

    @RequestMapping(value="/{key}/show", method= RequestMethod.PUT)
    public ResponseOverlays checkShow(@PathVariable("key") int key, @Validated @RequestBody INFO info) {
        try {
            info.setInfo(key);
            info.setShow(true);
            int result = infoService.modify(info);
            if(result == 0){
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "CHECK_SHOW_INFO_NOT_SAVE", null);
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_OK, "CHECK_SHOW_INFO_SUCCESS", info);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "CHECK_SHOW_INFO_FAIL", null);
        }
    }
}