package com.jamie.home.api.service;

import com.jamie.home.api.model.CATEGORY;
import com.jamie.home.api.model.INFO;
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
        Integer result = null;
        if("avail_point_tot".equals(search.getValueType())){
            Integer pointTot = pointDao.getAdminValue(search);
            Integer minusPoint = pointDao.getListPointMinusTot();
            result = pointTot - minusPoint;
        } else if("value".equals(search.getValueType())) {
             result = pointDao.getAdminValue(search);
        }
        return result;
    }

    public List<CATEGORY> listCategoryKeyword(SEARCH search) {
        return categoryDao.getCategoryWithKeywordList(search);
    }

    public MEMBER find(SEARCH search) {
        return memberDao.find(search);
    }

    public Integer modiAdminValue(SEARCH search) {
        search.setValueType("value");
        Integer point = pointDao.getAdminValue(search);

        if(point < search.getValue()){
            // 환급 가능 금액 수정 시 늘어났으면 전체 알림
            search.setAlertMember(true);
            List<MEMBER> memberList = memberDao.getListMember(search);
            // 답변채택 알림 TYPE 6
            INFO info = new INFO();
            info.setMemberList(memberList);
            infoDao.insertInfo(info);
        }

        return pointDao.updateAdminValue(search);
    }
}
