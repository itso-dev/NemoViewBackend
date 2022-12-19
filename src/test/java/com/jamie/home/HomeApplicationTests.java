package com.jamie.home;

import com.jamie.home.api.dao.MemberDao;
import com.jamie.home.api.model.Keywords;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.service.MemberService;
import com.jamie.home.util.KeywordUtils;
import com.jamie.home.util.NiceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SpringBootTest
class HomeApplicationTests {

    @Autowired
    MemberService memberService;

    @Test
    void contextLoads() throws Exception {
        MEMBER param = new MEMBER();
        //param.setMember(815);
        param.setMember(7);

        MEMBER member = memberService.get(param);

        member.setPassword("1234");

        memberService.modi(member);

        System.out.println(member);

    }

}
