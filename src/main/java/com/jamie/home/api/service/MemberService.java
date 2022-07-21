package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamie.home.api.model.*;
import com.jamie.home.util.CodeUtils;
import com.jamie.home.util.KeywordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService extends BasicService{
    public List<MEMBER> list(SEARCH search) {
        return memberDao.getListMember(search);
    }

    public Integer listCnt(SEARCH search) {
        return memberDao.getListMemberCnt(search);
    }

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

        // 수정 시 키워드 안에 있는 필수 키워드 값을 가져온다
        List<Keywords> mandatory = KeywordUtils.getKeywordInMemberByType(KeywordUtils.mandatoryType, member.getKeywords());

        member.setKeywords(KeywordUtils.getKeywordsValue(common, mandatory, null));

        return memberDao.updateMember(member);
    }

    public MEMBER checkEmail(MEMBER member) {
        return memberDao.checkEmail(member);
    }

    public Integer updateLogDate(MEMBER member) {
        return memberDao.updateLogDate(member);
    }

    public Integer modiKeywords(MEMBER member) throws JsonProcessingException {
        MEMBER memberInfo = memberDao.getMember(member);
        //공통키워드 추출
        List<Keywords> common = KeywordUtils.getCommonKeyword(memberInfo);

        //필수키워드 추출
        List<Keywords> mandatory = KeywordUtils.getMandatoryKeyword(member.getKeywordList());

        member.setKeywords(KeywordUtils.getKeywordsValue(common, mandatory, null));

        return memberDao.updateMemberKeywords(member);
    }

    public Integer hideMember(MEMBER member) {
        return memberDao.updateMemberHide(member);
    }

}
