package com.jamie.home.api.service;

import com.jamie.home.api.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public DASH adminDashInfo(SEARCH search) {
        DASH result = memberDao.getAdminDashInfo(search);
        // 카테고리 정보
        result.setCategoryList(categoryDao.getListCategory(search));

        // 인기 카테고리
        result.setCategoryRankList(categoryDao.listCategoryRank());

        // 인기 키워드 - 리뷰
        List<KEYWORD> reviewKeywordList = new ArrayList<>();
        List<KEYWORD> reviewKeywordRank = categoryDao.listReviewKeywordRank();
        for(int i=0; i<reviewKeywordRank.size(); i++){
            KEYWORD keyword = categoryDao.getKeyword(reviewKeywordRank.get(i));
            keyword.setCnt(reviewKeywordRank.get(i).getCnt());
            reviewKeywordList.add(keyword);
        }

        result.setReviewKeywordRankList(reviewKeywordList);

        // 인기 키워드 - 질문
        List<KEYWORD> qeustionKeywordList = new ArrayList<>();
        List<KEYWORD> questionKeywordRank = categoryDao.listQuestionKeywordRank();
        for(int i=0; i<questionKeywordRank.size(); i++){
            KEYWORD keyword = categoryDao.getKeyword(questionKeywordRank.get(i));
            keyword.setCnt(questionKeywordRank.get(i).getCnt());
            qeustionKeywordList.add(keyword);
        }

        result.setQuestionKeywordRankList(qeustionKeywordList);
        return result;
    }

    public List<BANNER> listBanner(SEARCH search) {
        List<BANNER> result = bannerDao.getListBanner(search);
        if("1".equals(search.getType())){
            for(int i=0; i<result.size(); i++){
                SEARCH param = new SEARCH();
                param.setBanner(result.get(i).getBanner());
                bannerDao.updateBannerViews(param);
            }
        }
        return result;
    }

    public Integer modiBannerHits(SEARCH search) {
        if(search.getMember() != null){
            bannerDao.upsertBannerMember(search);
        }
        return bannerDao.updateBannerHits(search);
    }

    public Integer modiBannerViews(SEARCH search) {
        return bannerDao.updateBannerViews(search);
    }

    public List<String> listSearch(SEARCH search) {
        return searchDao.getListSearch(search);
    }
}
