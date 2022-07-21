package com.jamie.home.api.service;

import com.jamie.home.api.model.QUESTION;
import com.jamie.home.api.model.QUESTION_ANSWER;
import com.jamie.home.api.model.REVIEW_LIKE;
import com.jamie.home.api.model.SEARCH;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuestionService extends BasicService{
    public List<QUESTION> list(SEARCH search) {
        return questionDao.getListQuestion(search);
    }

    public Integer listCnt(SEARCH search) {
        return questionDao.getListQuestionCnt(search);
    }

    public QUESTION get(QUESTION question){
        return questionDao.getQuestion(question);
    }

    public Integer upHits(QUESTION question){
        return questionDao.updateQuestionHits(question);
    }

    public Integer save(QUESTION question) throws Exception {
        return questionDao.insertQuestion(question);
    }

    public Integer modi(QUESTION question) {
        return questionDao.updateQuestion(question);
    }

    public List<QUESTION_ANSWER> listAnswer(SEARCH search) {
        return questionDao.getListQuestionAnswer(search);
    }

    public Integer listAnswerCnt(SEARCH search) {
        return questionDao.getListQuestionAnswerCnt(search);
    }

    public Integer saveAnswer(QUESTION_ANSWER answer) {
        return questionDao.insertQuestionAnswer(answer);
    }

    public Integer choose(QUESTION_ANSWER answer) {
        return questionDao.updateQuestionAnswerChoose(answer);
    }

    public Integer modiQuestionState(QUESTION question) {
        return questionDao.updateQuestionState(question);
    }
}
