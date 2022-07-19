package com.jamie.home.api.dao;

import com.jamie.home.api.model.MEMBER;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberDao {
    MEMBER getMember(MEMBER member);

    Integer insertMember(MEMBER member);

    Integer updateMember(MEMBER member);

    MEMBER checkEmail(MEMBER member);

    Integer checkCode(MEMBER member);

    Integer updateLogDate(MEMBER member);
}
