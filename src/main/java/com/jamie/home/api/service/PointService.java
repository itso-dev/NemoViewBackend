package com.jamie.home.api.service;

import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.POINT;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PointService extends BasicService{
    public List<POINT> list(SEARCH search) {
        return pointDao.getListPoint(search);
    }

    public Integer listCnt(SEARCH search) {
        return pointDao.getListPointCnt(search);
    }

    public Integer save(POINT point) throws Exception {
        return pointDao.insertPoint(point);
    }
}
