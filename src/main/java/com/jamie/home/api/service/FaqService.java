package com.jamie.home.api.service;

import com.jamie.home.api.dao.FaqDao;
import com.jamie.home.api.dao.MemberDao;
import com.jamie.home.api.model.FAQ;
import com.jamie.home.api.model.SEARCH;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FaqService extends BasicService{
    public List<FAQ> list(SEARCH search) {
        return faqDao.getListFaq(search);
    }

    public Integer listCnt(SEARCH search) {
        return faqDao.getListFaqCnt(search);
    }
}
