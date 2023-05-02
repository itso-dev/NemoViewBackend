package com.jamie.home.api.dao;

import com.jamie.home.api.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MemberDao {
    MEMBER getMember(MEMBER member);

    Integer insertMember(MEMBER member);

    Integer updateMember(MEMBER member);

    MEMBER checkDuplicate(MEMBER member);

    Integer checkCode(MEMBER member);

    Integer updateLogDate(MEMBER member);

    Integer updateMemberKeywords(MEMBER member);

    List<MEMBER> getListMember(SEARCH search);

    Integer getListMemberCnt(SEARCH search);

    Integer updateStateMember(MEMBER member);

    Integer updateMemberSearchKeywords(MEMBER member);

    MEMBER getMemberByCode(MEMBER member);

    Integer updateMemberPoint(MEMBER member);

    int updateMemberAccount(MEMBER member);

    List<KEYWORD> getMemberKeyword(MEMBER member);
    Integer insertMemberKeyword(MEMBER member);
    void deleteMemberKeyword(KEYWORD keyword);

    MEMBER find(SEARCH search);

    DASH getAdminDashInfo(SEARCH search);

    Integer insertRemember(REMEMBER remember);

    REMEMBER getRemember(MEMBER member);

    int deleteMember(MEMBER member);

    List<KEYWORD> getListMemberKeyword(MEMBER reviewMember);

    List<Keywords> getMandatoryKeyword(MEMBER reviewMember);

    List<MEMBER> getListMemberSameKeyword(SEARCH search);

    void updateDeviceToken(MEMBER member);

    Integer updatePageView(SEARCH search);

    Integer getMemberPointMonth(MEMBER member);

    int insertMemberAlarm(MEMBER member);
    int deleteMemberAlarm(MEMBER member);

    void INSERT_COMMON_KEYWORD_FROM_MEMBER_DATA(MEMBER member);

    void INSERT_COMMON_KEYWORD(MEMBER member);

    List<KEYWORD> getMemberCommonKeyword(MEMBER member);
    Integer insertMemberCommonKeyword(MEMBER member);
    void deleteMemberCommonKeyword(KEYWORD keyword);

    MEMBER getMemberCheckIn(MEMBER member);

    Integer insertMemberCheckIn(MEMBER member);
}
