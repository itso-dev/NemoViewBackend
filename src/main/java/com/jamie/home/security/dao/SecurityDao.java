package com.jamie.home.security.dao;

import com.jamie.home.api.model.MEMBER;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SecurityDao {
    MEMBER getMemberForLogin(String id);
}
