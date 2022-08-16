package com.jamie.home.api.service;

import com.jamie.home.api.model.CATEGORY;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.SEARCH;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MainService extends BasicService{
    public List<CATEGORY> listCategory(SEARCH search) {
        return categoryDao.getListCategory(search);
    }

    public Integer listCategoryCnt(SEARCH search) {
        return categoryDao.getListCategoryCnt(search);
    }

    public Integer getAdminValue(SEARCH search) {
        Integer pointTot = pointDao.getAdminValue(search);
        Integer minusPoint = pointDao.getListPointMinusTot();
        return pointTot - minusPoint;
    }

    public List<CATEGORY> listCategoryKeyword(SEARCH search) {
        return categoryDao.getCategoryWithKeywordList(search);
    }

    public MEMBER find(SEARCH search) {
        return memberDao.find(search);
    }
}
