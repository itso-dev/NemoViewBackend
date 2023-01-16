package com.jamie.home.api.dao;

import com.jamie.home.api.model.INFO;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface InfoDao {
    List<INFO> getListInfo(SEARCH search);

    Integer getListInfoCnt(SEARCH search);

    Integer insertInfo(INFO info);

    Integer updateInfo(INFO info);
}
