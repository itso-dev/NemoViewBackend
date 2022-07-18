package com.jamie.home.api.dao;

import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberDao {
    MEMBER getMember(MEMBER member);

    int insertMember(MEMBER member);

    int updateMember(MEMBER member);

    MEMBER checkEmail(MEMBER member);

    int checkCode(MEMBER member);

    int updateLogDate(MEMBER member);
}
