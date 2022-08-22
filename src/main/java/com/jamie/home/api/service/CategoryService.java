package com.jamie.home.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.model.CATEGORY;
import com.jamie.home.api.model.CATEGORY_CLASSIFICATION;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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

    public Integer save(CATEGORY category) throws Exception{
        category.setIcon(
                FileUtils.saveFiles(
                        category.getSaveFiles(),
                        uploadDir
                )
        );

        category.setSaveFiles(null);

        int reuslt = categoryDao.insertCategory(category);
        ObjectMapper mapper = new ObjectMapper();
        List<CATEGORY_CLASSIFICATION> cList = Arrays.asList(mapper.readValue(category.getClassificationsStr(), CATEGORY_CLASSIFICATION[].class));

        for(int i=0; i<cList.size(); i++){
            CATEGORY_CLASSIFICATION cTarget = cList.get(i);
            cTarget.setCategory(category.getCategory());
            categoryDao.insertCategoryClassification(cTarget);
            categoryDao.insertCategoryKeywords(cTarget);
        }

        return reuslt;
    }

    public int modi(CATEGORY category) throws Exception {
        category.setIcon(
                FileUtils.modiFiles(
                        category.getFiles(),
                        category.getDeleteFiles(),
                        category.getSaveFiles(),
                        uploadDir
                )
        );

        category.setSaveFiles(null);

        int reuslt = categoryDao.updateCategory(category);

        ObjectMapper mapper = new ObjectMapper();
        List<CATEGORY_CLASSIFICATION> cList = Arrays.asList(mapper.readValue(category.getClassificationsStr(), CATEGORY_CLASSIFICATION[].class));

        categoryDao.deleteAllClassifications(category);
        categoryDao.deleteAllKeywords(category);
        for(int i=0; i<cList.size(); i++){
            CATEGORY_CLASSIFICATION cTarget = cList.get(i);
            cTarget.setCategory(category.getCategory());
            categoryDao.insertCategoryClassification(cTarget);
            categoryDao.insertCategoryKeywords(cTarget);
        }

        return reuslt;
    }
}
