package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamie.home.api.model.Keywords;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.ROLE;
import com.jamie.home.util.CodeUtils;
import com.jamie.home.util.KeywordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService extends BasicService{
    public MEMBER get(MEMBER member){
        return memberDao.getMember(member);
    }

    public Integer save(MEMBER member) throws JsonProcessingException {
        while(true){ // 회원 고유 코드값 생성
            member.setCode(CodeUtils.getCodeValue());
            int codeCnt = memberDao.checkCode(member);
            if(codeCnt == 0){
                break;
            }
        }
        // 비밀번호 암호화
        member.setPassword(encoder.encode(member.getPassword()));
        member.setRole(ROLE.ROLE_USER);

        //공통키워드 추출
        List<Keywords> common = KeywordUtils.getCommonKeyword(member);

        member.setKeywords(KeywordUtils.getKeywordsValue(common, null, null));

        return memberDao.insertMember(member);
    }

    public Integer modi(MEMBER member) throws JsonProcessingException {
        if(member.getPassword() != null){
            member.setPassword(encoder.encode(member.getPassword()));
        }

        //공통키워드 추출
        List<Keywords> common = KeywordUtils.getCommonKeyword(member);

        member.setKeywords(KeywordUtils.getKeywordsValue(common, null, null));

        return memberDao.updateMember(member);
    }

    public MEMBER checkEmail(MEMBER member) {
        return memberDao.checkEmail(member);
    }

    public Integer updateLogDate(MEMBER member) {
        return memberDao.updateLogDate(member);
    }
}
