package com.jamie.home;

import com.jamie.home.api.dao.MemberDao;
import com.jamie.home.api.model.Keywords;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.api.service.MemberService;
import com.jamie.home.util.KeywordUtils;
import com.jamie.home.util.NiceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class HomeApplicationTests {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberDao memberDao;

    @Test
    void contextLoads() throws Exception {
/*        MEMBER param = new MEMBER();
        param.setMember(4051);
        //param.setEmail("j383170107@naver.com");

        MEMBER member = memberService.get(param);

        member.setPassword("Qwer1234!@");

        System.out.println(member);

        memberService.modi(member);

        System.out.println(member);*/
/*        System.out.println("start :::::::::: "+new Date());
        SEARCH search = new SEARCH();
        List<MEMBER> list = memberService.list(search);
        System.out.println(list.size());
        for(int i=0; i<list.size(); i++){
            memberDao.INSERT_COMMON_KEYWORD(list.get(i));
        }
        System.out.println("end :::::::::: "+new Date());*/

    }

}
