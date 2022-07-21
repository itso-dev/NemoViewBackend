package com.jamie.home.api.service;

import com.jamie.home.api.model.CATEGORY;
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
}
