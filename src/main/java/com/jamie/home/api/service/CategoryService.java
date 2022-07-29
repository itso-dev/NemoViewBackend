package com.jamie.home.api.service;

import com.jamie.home.api.model.CATEGORY;
import com.jamie.home.api.model.FAQ;
import com.jamie.home.api.model.SEARCH;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService extends BasicService{
    public List<CATEGORY> list(SEARCH search) {
        return categoryDao.getListCategory(search);
    }

    public Integer listCnt(SEARCH search) {
        return categoryDao.getListCategoryCnt(search);
    }

}
