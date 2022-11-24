package com.jamie.home.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.model.CATEGORY;
import com.jamie.home.api.model.CATEGORY_CLASSIFICATION;
import com.jamie.home.api.model.CATEGORY_KEYWORD;
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
        return categoryDao.getCategoryWithKeywordList(search);
    }

    public List<CATEGORY_CLASSIFICATION> listClassification(SEARCH search) {
        return categoryDao.getClassificationWithKeywordList(search);
    }

    /*public Integer listCnt(SEARCH search) {
        return categoryDao.getListCategoryCnt(search);
    }*/

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
            categoryDao.insertCategoryClassificationGroup(cTarget);
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
            categoryDao.deleteClassificationGroups(category);
        }
        // 2. 세부 카테고리 수정
        for(int i=0; i<category.getClassifications().size(); i++){
            categoryDao.updateClassificationGroup(category.getClassifications().get(i));
        }
        // 3. 세부 카테고리 신규
        for(int i=0; i<category.getNewClassifications().size(); i++){
            CATEGORY_CLASSIFICATION cTarget = category.getNewClassifications().get(i);
            cTarget.setCategory(category.getCategory());
            categoryDao.insertCategoryClassificationGroup(cTarget);
        }
        return reuslt;
    }

    public CATEGORY getCategory(CATEGORY category) {
        return categoryDao.getCategoryWithKeyword(category);
    }

    public CATEGORY_CLASSIFICATION getClassification(CATEGORY_CLASSIFICATION classification) {
        return categoryDao.getClassificationWithKeyword(classification);
    }

    public int saveClassification(CATEGORY_CLASSIFICATION classification) throws Exception {
        int reuslt = categoryDao.insertCategoryClassification(classification);

        ObjectMapper mapper = new ObjectMapper();
        List<CATEGORY_KEYWORD> kList = Arrays.asList(mapper.readValue(classification.getKeywordsStr(), CATEGORY_KEYWORD[].class));

        classification.setNewKeywords(kList);
        if(classification.getNewKeywords() != null && classification.getNewKeywords().size() != 0){
            categoryDao.insertCategoryNewKeywords(classification);
        }

        return reuslt;
    }

    public int modiClassification(CATEGORY_CLASSIFICATION classification) throws Exception {
        int reuslt = categoryDao.updateClassification(classification);

        ObjectMapper mapper = new ObjectMapper();
        List<CATEGORY_KEYWORD> kList = Arrays.asList(mapper.readValue(classification.getKeywordsStr(), CATEGORY_KEYWORD[].class));
        List<CATEGORY_KEYWORD> kListNew = Arrays.asList(mapper.readValue(classification.getNewKeywordsStr(), CATEGORY_KEYWORD[].class));
        List<CATEGORY_KEYWORD> kListDel = Arrays.asList(mapper.readValue(classification.getDelKeywordsStr(), CATEGORY_KEYWORD[].class));

        classification.setKeywords(kList);
        classification.setNewKeywords(kListNew);
        classification.setDelKeywords(kListDel);

        // 2-1. 키워드 삭제
        if(classification.getDelKeywords() != null && classification.getDelKeywords().size() != 0){
            categoryDao.deleteKeywords(classification);
        }
        // 2-1. 키워드 수정
        for(int j=0; j<classification.getKeywords().size(); j++){
            categoryDao.updateClassification(classification);
            categoryDao.updateKeyword(classification.getKeywords().get(j));
        }
        // 2-1. 키워드 신규
        if(classification.getNewKeywords() != null && classification.getNewKeywords().size() != 0){
            categoryDao.insertCategoryNewKeywords(classification);
        }

        // 4. 회원, 리뷰, 질문 키워드 재설정
        categoryDao.updateMemberKeywords();
        categoryDao.updateReviewKeywords();
        categoryDao.updateQuestionKeywords();

        return reuslt;
    }

    public Integer removeCategory(CATEGORY category) {
        return categoryDao.deleteCategory(category);
    }

    public Integer removeClassification(CATEGORY_CLASSIFICATION classification) {
        return categoryDao.deleteClassification(classification);
    }
}
