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

        if(result != 0){
            if(codeMember != null) {
                SEARCH search = new SEARCH();
                search.setType("2");
                search.setValueType("value");
                Integer pointValue = pointDao.getAdminValue(search);
                POINT point = new POINT();
                point.setValues(member.getMember(), "1", pointValue, "회원가입", "1");
                pointDao.insertPoint(point);
                member.setPoint(pointValue);
                memberDao.updateMemberPoint(member);
                // 코드주인도 적립
                point.setMember(codeMember.getMember());
                point.setContent("친구 추천 코드");
                pointDao.insertPoint(point);
                codeMember.setPoint(pointValue);
                memberDao.updateMemberPoint(codeMember);

                // 친구 초대 코드 입력 시 알림 TYPE 10
                INFO info = new INFO();
                info.setValues(codeMember.getMember(),
                        "10",
                        codeMember.getMember(),
                        "내 초대코드로 친구가 가입하여 포인트가 지급되었어요! 지급된 포인트를 확인해 보세요!",
                        "",
                        "[{\"name\":\"point-icon.png\",\"uuid\":\"point-icon\",\"path\":\"/image/mypage/point-icon.png\"}]");
                infoDao.insertInfo(info);
                sendPushMessage(codeMember.getMember(), "포인트", "내 초대코드로 친구가 가입하여 포인트가 지급되었어요! 지급된 포인트를 확인해 보세요!");
            }
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

        int result = memberDao.updateMemberKeywords(member);

        // MEMBER_KEYWORD 저장
        List<KEYWORD> keywords = KeywordUtils.getMandatoryKeywordForSave(member.getKeywordList());

        SEARCH search = new SEARCH();
        search.setMember(member.getMember());
        memberDao.deleteAllMemberKeywrod(search);
        if(keywords != null && keywords.size() != 0){
            search.setReview_keywords(keywords);
            memberDao.insertMemberKeywrod(search);
        }
        return result;
    }

    public Integer modiStateMember(MEMBER member) {
        return memberDao.updateStateMember(member);
    }

    public Integer modiSearchKeywords(MEMBER member) {
        return memberDao.updateMemberSearchKeywords(member);
    }

    public Integer modiAccount(MEMBER member) {
        return memberDao.updateMemberAccount(member);
    }

    public Integer inputCode(MEMBER member) {
        int result = 0;
        MEMBER codeMember = memberDao.getMemberByCode(member);
        if(codeMember != null) {
            POINT point = new POINT();
            point.setValues(member.getMember(), "1", 1000, "친구 추천 코드", "1");
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

    public Integer saveRemember(REMEMBER remember) {
        return memberDao.insertRemember(remember);
    }

    public REMEMBER getRemember(MEMBER member) {
        return memberDao.getRemember(member);
    }

    public int removeMember(MEMBER member) {
        return memberDao.deleteMember(member);
    }

    public List<KEYWORD> getListKeywordMandatory(MEMBER member) {
        return memberDao.getListMemberKeyword(member);
    }

    public void updateDeviceToken(MEMBER member) {
        // 디바이스 토큰값 수정
        if(member.getAndroid_device_token() != null || member.getIos_device_token() != null){
            memberDao.updateDeviceToken(member);
        }
    }
}
