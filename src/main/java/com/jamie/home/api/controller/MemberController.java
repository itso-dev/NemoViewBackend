package com.jamie.home.api.controller;

import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.ResponseOverlays;
import com.jamie.home.api.model.TOKEN;
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

@RestController
@RequestMapping("/member/*")
public class MemberController {

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Value("${file.upload.dir}")
    private String uploadDir;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private MemberService memberService;

    @RequestMapping(value="/{key}", method= RequestMethod.GET)
    public ResponseOverlays get(@PathVariable("key") int key) {
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

    @RequestMapping(value="/save", method= RequestMethod.POST)
    public ResponseOverlays save(@Validated @RequestBody MEMBER member) {
        try {
            int result = memberService.save(member);
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

    @RequestMapping(value="/{key}", method= RequestMethod.PUT)
    public ResponseOverlays modi(@PathVariable("key") int key, @Validated @RequestBody MEMBER member) {
        try {
            member.setMember(key);
            int result = memberService.modi(member);
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

    @RequestMapping(value="/login", method= RequestMethod.POST)
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
                return new ResponseOverlays(HttpServletResponse.SC_OK, "LOGIN_MEMBER_SUCCESS", new TOKEN(result, jwt));
            } else {
                return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "LOGIN_MEMBER_FAIL", null);
            }
        } catch (Exception e){
            logger.error(e.getLocalizedMessage());
            return new ResponseOverlays(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "LOGIN_MEMBER_FAIL", null);
        }
    }

    @RequestMapping(value="/email/check", method= RequestMethod.POST)
    public ResponseOverlays checkEmail(@Validated @RequestBody MEMBER member) {
        try {
            MEMBER result = memberService.checkEmail(member);
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

    @RequestMapping(value="/{key}/keywords", method= RequestMethod.PUT)
    public ResponseOverlays modiKeywords(@PathVariable("key") int key, @Validated @RequestBody MEMBER member) {
        try {
            member.setMember(key);
            int result = memberService.modiKeywords(member);
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

    @RequestMapping(value="/{key}/search/keywords", method= RequestMethod.PUT)
    public ResponseOverlays modiSearchKeywords(@PathVariable("key") int key, @Validated @RequestBody MEMBER member) {
        try {
            member.setMember(key);
            int result = memberService.modiSearchKeywords(member);
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

    @RequestMapping(value="/{key}/account", method= RequestMethod.PUT)
    public ResponseOverlays modiAccount(@PathVariable("key") int key, @Validated @RequestBody MEMBER member) {
        try {
            member.setMember(key);
            int result = memberService.modiAccount(member);
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
}