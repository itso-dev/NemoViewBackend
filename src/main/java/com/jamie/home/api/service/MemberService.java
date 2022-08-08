package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamie.home.api.model.*;
import com.jamie.home.util.CodeUtils;
import com.jamie.home.util.FileUtils;
import com.jamie.home.util.KeywordUtils;
import org.springframework.security.core.parameters.P;
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

        // 공통키워드 추출
        List<Keywords> common = KeywordUtils.getCommonKeyword(member);

        member.setKeywords(KeywordUtils.getKeywordsValue(common, null, null));

        MEMBER codeMember = memberDao.getMemberByCode(member);

        Integer result = memberDao.insertMember(member);

        // 회원코드 입력시 TODO
        if(codeMember != null) {
            SEARCH search = new SEARCH();
            search.setType("2");
            Integer pointValue = pointDao.getAdminValue(search);
            POINT point = new POINT();
            point.setValues(member.getMember(), "1", pointValue, "회원가입", "1");
            pointDao.insertPoint(point);
            member.setPoint(pointValue);
            memberDao.updateMemberPoint(member);
            // 코드주인도 적립
            point.setMember(codeMember.getMember());
            point.setContent("코드입력");
            pointDao.insertPoint(point);
            codeMember.setPoint(pointValue);
            memberDao.updateMemberPoint(codeMember);
        }

        return result;
    }

    public Integer modi(MEMBER member) throws Exception {
        if(member.getPassword() != null){
            member.setPassword(encoder.encode(member.getPassword()));
        }

        if(member.getSaveFiles() !=  null){
            if(member.getProfile() == null){
                member.setProfile(
                        FileUtils.saveFiles(
                                member.getSaveFiles(),
                                uploadDir
                        )
                );
            } else {
                member.setProfile(
                        FileUtils.modiFiles(
                                member.getProfile(),
                                member.getDeleteFiles(),
                                member.getSaveFiles(),
                                uploadDir
                        )
                );
            }
            member.setSaveFiles(null);
        }
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
        // 회원정보에서 공통키워드 추출
        List<Keywords> common = KeywordUtils.getCommonKeyword(memberInfo);

        // 직접 선택한 필수키워드 추출
        List<Keywords> mandatory = KeywordUtils.getMandatoryKeyword(member.getKeywordList());

        member.setKeywords(KeywordUtils.getKeywordsValue(common, mandatory, null));

        return memberDao.updateMemberKeywords(member);
    }

    public Integer hideMember(MEMBER member) {
        return memberDao.updateMemberHide(member);
    }

    public Integer modiSearchKeywords(MEMBER member) {
        return memberDao.updateMemberSearchKeywords(member);
    }

    public int modiAccount(MEMBER member) {
        return memberDao.updateMemberAccount(member);
    }

    public int inputCode(MEMBER member) {
        int result = 0;
        MEMBER codeMember = memberDao.getMemberByCode(member);
        if(codeMember != null) {
            POINT point = new POINT();
            point.setValues(member.getMember(), "1", 1000, "코드입력", "1");
            pointDao.insertPoint(point);
            member.setPoint(1000);
            memberDao.updateMemberPoint(member);
            // 코드주인도 적립
            point.setMember(codeMember.getMember());
            pointDao.insertPoint(point);
            codeMember.setPoint(1000);
            memberDao.updateMemberPoint(codeMember);
            result = 1;
        }
        return result;
    }
}
