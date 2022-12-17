package com.jamie.home.api.dao;

import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.POINT;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PointDao {
    List<POINT> getListPoint(SEARCH search);

    Integer getListPointCnt(SEARCH search);

    Integer insertPoint(POINT point);

    Integer updatePointState(POINT point);

    Integer getAdminValue(SEARCH search);

    Integer getListPointMinusTot();

    POINT getPoint(POINT point);

    Integer updateAdminValue(SEARCH search);

    POINT getListPointCnts(SEARCH search);
}
