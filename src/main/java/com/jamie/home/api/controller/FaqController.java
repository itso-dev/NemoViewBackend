package com.jamie.home.api.controller;

import com.jamie.home.api.model.FAQ;
import com.jamie.home.api.model.ResponseOverlays;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.api.model.VoList;
import com.jamie.home.api.service.FaqService;
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
@RequestMapping("/faq/*")
public class FaqController {

    private static final Logger logger = LoggerFactory.getLogger(FaqController.class);

    @Autowired
    private FaqService faqService;

    @RequestMapping(value="/list", method= RequestMethod.POST)
    public ResponseOverlays list(@Validated @RequestBody SEARCH search) {
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
}