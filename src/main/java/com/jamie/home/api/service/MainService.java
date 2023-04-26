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

        /*if(point < search.getValue()){
            // 환급 가능 금액 수정 시 늘어났으면 전체 알림
            search.setAlertMember(true);
            List<MEMBER> memberList = memberDao.getListMember(search);
            // 알림 TYPE 6
            INFO info = new INFO();
            info.setMemberList(memberList);
            infoDao.insertInfo(info);
            sendPushMessage(null, "포인트", "이번 달 환급 가능 금액이 늘어났어요.\n지금 확인하고 환급 신청해 보세요!");
        }*/

        return pointDao.updateAdminValue(search);
    }

    public DASH adminDashInfo(SEARCH search) {
        DASH result = memberDao.getAdminDashInfo(search);
        // 카테고리 정보
        result.setCategoryList(categoryDao.getListCategory(search));

        // 인기 카테고리
        result.setCategoryRankList(categoryDao.listCategoryRank());

        // 인기 키워드 - 리뷰
        result.setReviewKeywordRankList(categoryDao.listReviewKeywordRank());

        // 인기 키워드 - 질문
        result.setQuestionKeywordRankList(categoryDao.listQuestionKeywordRank());

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

    public Integer increasePageView(SEARCH search) {
        return memberDao.updatePageView(search);
    }

    public Integer getPointMonth(MEMBER member) {
        return memberDao.getMemberPointMonth(member);
    }

    public List<COMMON_KEYWORD> listCommonKeyword(SEARCH search) {
        return categoryDao.getListCommonKeyword(search);
    }
}
