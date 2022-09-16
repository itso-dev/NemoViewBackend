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

    MEMBER checkEmail(MEMBER member);

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

    Integer insertMemberKeywrod(SEARCH search);
    void deleteAllMemberKeywrod(SEARCH search);

    MEMBER find(SEARCH search);

    DASH getAdminDashInfo(SEARCH search);

    Integer insertRemember(REMEMBER remember);

    REMEMBER getRemember(MEMBER member);

    int deleteMember(MEMBER member);

    List<KEYWORD> getListMemberKeyword(MEMBER reviewMember);

    List<Keywords> getMandatoryKeyword(MEMBER reviewMember);

    List<MEMBER> getListMemberSameKeyword(SEARCH search);
}
