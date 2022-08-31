package com.jamie.home.api.dao;

import com.jamie.home.api.model.QUESTION;
import com.jamie.home.api.model.QUESTION_ANSWER;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface QuestionDao {
    List<QUESTION> getListQuestion(SEARCH search);

    Integer getListQuestionCnt(SEARCH search);

    QUESTION getQuestion(QUESTION question);

    Integer updateQuestionHits(QUESTION question);

    Integer insertQuestion(QUESTION question);

    Integer updateQuestion(QUESTION question);

    List<QUESTION_ANSWER> getListQuestionAnswer(SEARCH search);

    Integer getListQuestionAnswerCnt(SEARCH search);

    Integer insertQuestionAnswer(QUESTION_ANSWER answer);

    Integer updateQuestionAnswerChoose(QUESTION_ANSWER answer);

    Integer updateQuestionState(QUESTION question);

    Integer insertQuestionKeywrod(SEARCH search);
    void deleteAllQuestionKeywrod(SEARCH search);

    QUESTION_ANSWER getAnswer(QUESTION_ANSWER answer);

    Integer deleteAnswer(QUESTION_ANSWER answer);
}
