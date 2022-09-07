package com.jamie.home.api.dao;

import com.jamie.home.api.model.BANNER;
import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BannerDao {
    List<BANNER> getListBanner(SEARCH search);

    Integer getListBannerCnt(SEARCH search);

    Integer insertBanner(BANNER contact);

    Integer updateBanner(BANNER contact);

    BANNER getBanner(BANNER contact);

    Integer updateBannerHits(SEARCH search);

    void upsertBannerMember(SEARCH search);

    List<MEMBER> getListBannerMember(SEARCH search);

    Integer updateBannerViews(SEARCH search);
}
