package com.jamie.home.api.service;

import com.jamie.home.api.model.BANNER;
import com.jamie.home.api.model.CATEGORY;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BannerService extends BasicService{
    public List<BANNER> list(SEARCH search) {
        return bannerDao.getListBanner(search);
    }

    public Integer listCnt(SEARCH search) {
        return bannerDao.getListBannerCnt(search);
    }

    public Integer save(BANNER banner) throws Exception {
        banner.setImage(
                FileUtils.saveFiles(
                        banner.getSaveFiles(),
                        uploadDir
                )
        );
        banner.setSaveFiles(null);
        return bannerDao.insertBanner(banner);
    }

    public Integer modi(BANNER banner) throws Exception {
        banner.setImage(
                FileUtils.modiFiles(
                        banner.getImage(),
                        banner.getDeleteFiles(),
                        banner.getSaveFiles(),
                        uploadDir
                )
        );

        banner.setSaveFiles(null);
        return bannerDao.updateBanner(banner);
    }

    public BANNER get(BANNER banner) {
        return bannerDao.getBanner(banner);
    }
}
