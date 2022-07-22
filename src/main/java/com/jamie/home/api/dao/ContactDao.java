package com.jamie.home.api.dao;

import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ContactDao {
    List<CONTACT> getListContact(SEARCH search);

    Integer getListContactCnt(SEARCH search);

    Integer insertContact(CONTACT contact);

    Integer updateContact(CONTACT contact);

    Integer updateContactAnswer(CONTACT contact);
}
