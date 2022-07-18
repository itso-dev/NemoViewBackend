package com.jamie.home.api.controller;

import com.jamie.home.api.model.*;
import com.jamie.home.api.service.FaqService;
import com.jamie.home.api.service.MemberService;
import com.jamie.home.jwt.JwtFilter;
import com.jamie.home.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
            Integer cnt = faqService.listCnt(search);
            VoList<FAQ> result = new VoList<>(cnt, list);
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
}