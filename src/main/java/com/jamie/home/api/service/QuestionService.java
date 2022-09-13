package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamie.home.api.model.*;
import com.jamie.home.util.KeywordUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuestionService extends BasicService{
    public List<QUESTION> list(SEARCH search) throws Exception {
        if(search.getKeywords() != null){
            search.setKeywordList(KeywordUtils.getKeywordListFromSearch(search.getKeywords()));
        }
        List<QUESTION> list = questionDao.getListQuestion(search);
        if(search.getAnswerMember() != null){
            for(int i=0; i<list.size(); i++){
                search.setQuestion(list.get(i).getQuestion());
                list.get(i).setAnswer(questionDao.getListQuestionAnswer(search));
            }
        }
        return list;
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
        MEMBER param = new MEMBER();
        param.setMember(question.getMember());
        MEMBER memberInfo = memberDao.getMember(param);
        // 회원정보에서 공통키워드 추출
        List<Keywords> common = KeywordUtils.getCommonKeyword(memberInfo);

        // 직접 선택한 필수키워드 추출
        List<Keywords> mandatory = KeywordUtils.getMandatoryKeyword(question.getKeywordList());

        question.setKeywords(KeywordUtils.getKeywordsValue(common, mandatory, null));

        int result = questionDao.insertQuestion(question);

        // QUESTION_KEYWORD 저장
        List<KEYWORD> keywords = KeywordUtils.getMandatoryKeywordForSave(question.getKeywordList());

        SEARCH search = new SEARCH();
        search.setQuestion(question.getQuestion());
        if(keywords != null && keywords.size() != 0){
            search.setReview_keywords(keywords);
            questionDao.insertQuestionKeywrod(search);
        }

        // 포인트 내역 수정
        POINT point = new POINT();
        point.setValues(question.getMember(), "3", question.getPoint(), "질문등록", "1");
        pointDao.insertPoint(point);
        param.setPoint(question.getPoint()*(-1));
        memberDao.updateMemberPoint(param);
        return result;
    }

    public Integer modi(QUESTION question) throws Exception {
        MEMBER param = new MEMBER();
        param.setMember(question.getMember());
        MEMBER memberInfo = memberDao.getMember(param);
        // 회원정보에서 공통키워드 추출
        List<Keywords> common = KeywordUtils.getCommonKeyword(memberInfo);

        // 직접 선택한 필수키워드 추출
        List<Keywords> mandatory = KeywordUtils.getMandatoryKeyword(question.getKeywordList());

        question.setKeywords(KeywordUtils.getKeywordsValue(common, mandatory, null));

        int result = questionDao.updateQuestion(question);

        // QUESTION_KEYWORD 저장
        List<KEYWORD> keywords = KeywordUtils.getMandatoryKeywordForSave(question.getKeywordList());

        SEARCH search = new SEARCH();
        search.setQuestion(question.getQuestion());
        questionDao.deleteAllQuestionKeywrod(search);
        if(keywords != null && keywords.size() != 0){
            search.setReview_keywords(keywords);
            questionDao.insertQuestionKeywrod(search);
        }
        return result;
    }

    public List<QUESTION_ANSWER> listAnswer(SEARCH search) {
        return questionDao.getListQuestionAnswer(search);
    }

    public Integer listAnswerCnt(SEARCH search) {
        return questionDao.getListQuestionAnswerCnt(search);
    }

    public Integer saveAnswer(QUESTION_ANSWER answer) {
        int result = questionDao.insertQuestionAnswer(answer);

        // 답변채택 알림 TYPE 8
        if(result != 0) {
            QUESTION param = new QUESTION();
            param.setQuestion(answer.getQuestion());
            QUESTION question = questionDao.getQuestion(param);

            INFO info = new INFO();
            info.setValues(question.getMember(), "8", question.getQuestion(), "내 질문에 댓글이 달렸어요! 지금 댓글을 확인해 보세요!", "");
            infoDao.insertInfo(info);
        }

        return result;
    }

    public Integer choose(QUESTION_ANSWER answer) {
        // 답변채택
        int result = questionDao.updateQuestionAnswerChoose(answer);

        if(result != 0) {
            QUESTION_ANSWER infoAnswer = questionDao.getAnswer(answer);
            // 답변채택 알림 TYPE 7
            INFO info = new INFO();
            info.setValues(infoAnswer.getMember(), "7", infoAnswer.getQuestion(), "내 댓글이 채택되었어요! 지급된 포인트를 확인해 보세요!", "");
            infoDao.insertInfo(info);
        }

        return result;
    }

    public Integer modiQuestionState(QUESTION question) {
        return questionDao.updateQuestionState(question);
    }

    public Integer removeAnswer(QUESTION_ANSWER answer) {
        return questionDao.deleteAnswer(answer);
    }
}
