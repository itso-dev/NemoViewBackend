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
            categoryDao.insertCategoryNewKeywords(cTarget);
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
        List<CATEGORY_CLASSIFICATION> cListNew = Arrays.asList(mapper.readValue(category.getNewClassificationsStr(), CATEGORY_CLASSIFICATION[].class));
        List<CATEGORY_CLASSIFICATION> cListDel = Arrays.asList(mapper.readValue(category.getDelClassificationsStr(), CATEGORY_CLASSIFICATION[].class));

        category.setClassifications(cList);
        category.setNewClassifications(cListNew);
        category.setDelClassifications(cListDel);

        // 1. 세부 카테고리 삭제
        if(category.getDelClassifications() != null && category.getDelClassifications().size() != 0){
            categoryDao.deleteClassifications(category);
        }
        // 2. 세부 카테고리 수정
        for(int i=0; i<category.getClassifications().size(); i++){
            CATEGORY_CLASSIFICATION cTarget = category.getClassifications().get(i);
            // 2-1. 키워드 삭제
            if(cTarget.getDelKeywords() != null && cTarget.getDelKeywords().size() != 0){
                categoryDao.deleteKeywords(cTarget);
            }
            // 2-1. 키워드 수정
            for(int j=0; j<cTarget.getKeywords().size(); j++){
                categoryDao.updateClassification(cTarget);
                categoryDao.updateKeyword(cTarget.getKeywords().get(j));
            }
            // 2-1. 키워드 신규
            if(cTarget.getNewKeywords() != null && cTarget.getNewKeywords().size() != 0){
                cTarget.setCategory(category.getCategory());
                categoryDao.insertCategoryNewKeywords(cTarget);
            }
        }
        // 3. 세부 카테고리 신규
        for(int i=0; i<category.getNewClassifications().size(); i++){
            CATEGORY_CLASSIFICATION cTarget = category.getNewClassifications().get(i);
            cTarget.setCategory(category.getCategory());
            categoryDao.insertCategoryClassification(cTarget);
            if(cTarget.getNewKeywords() != null && cTarget.getNewKeywords().size() != 0){
                categoryDao.insertCategoryNewKeywords(cTarget);
            }
        }
        return reuslt;
    }
}
