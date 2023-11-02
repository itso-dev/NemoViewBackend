package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jamie.home.api.model.*;
import com.jamie.home.util.FileUtils;
import com.jamie.home.util.KeywordUtils;
import javassist.compiler.ast.Keyword;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
                List<QUESTION_ANSWER> list1 = questionDao.getListQuestionAnswer(search);
                list.get(i).setAnswer(list1);
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
        // 파일 저장
        question.setFiles(
                FileUtils.saveFiles(
                        question.getFiles_new(),
                        uploadDir
                )
        );
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
        if (question.getPoint().intValue() != 0){
            param.setPoint(question.getPoint()*(-1));
            memberDao.updateMemberPoint(param);

            POINT point = new POINT();
            point.setValues(question.getMember(), "3", question.getPoint(), memberInfo.getPoint() + (question.getPoint()*(-1)),"질문등록", "1");
            pointDao.insertPoint(point);
        }

        // 질문등록 시 전체 알림 TYPE 11
        // search.setKeywordList(KeywordUtils.getKeywordListFromSearch(question.getKeywords()));
        if(search.getKeywordList() != null && search.getKeywordList().size() > 0){
            List<MEMBER> memberList = memberDao.getListMemberSameKeyword(search);
            List<INFO> infoList = new ArrayList<>();
            for(int i=0; i<memberList.size(); i++){
                INFO info = new INFO();
                info.setValues(memberList.get(i).getMember(),
                        "11",
                        question.getQuestion(),
                        "다른 사용자가 도움을 필요로 하고 있어요!\n지금 내용을 확인해 보세요!",
                        "",
                        "[{\"name\":\"input-check.png\",\"uuid\":\"input-check\",\"path\":\"/image/login/input-check.png\"}]");
                infoList.add(info);
                sendPushMessage(memberList.get(i).getMember(), "질문하기", "다른 사용자가 도움을 필요로 하고 있어요!\n지금 내용을 확인해 보세요!");
            }
            search.setInfo_list(infoList);
            infoDao.insertInfoAll(search);
        }

        // 오늘 첫등록 일때 포인트 지급
        SEARCH paramSearch = new SEARCH();
        paramSearch.setMember(question.getMember());
        paramSearch.setTodayCnt(true);
        Integer todayCnt = questionDao.getListQuestionCnt(paramSearch);
        if(todayCnt != null && todayCnt.intValue() == 1){
            MEMBER member = new MEMBER();
            member.setMember(search.getMember());
            MEMBER memberInfo2 = memberDao.getMember(member);

            POINT point = new POINT();
            point.setValues(memberInfo2.getMember(), "1", 10, memberInfo2.getPoint() + 10, "커뮤니티 글 작성", "1");
            pointDao.insertPoint(point);
        }
        return result;
    }

    public Integer modi(QUESTION question) throws Exception {
        question.setFiles(
                FileUtils.modiFiles(
                        question.getFiles(),
                        question.getFiles_del(),
                        question.getFiles_new(),
                        uploadDir
                )
        );
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
        if(question.getKeywordList() != null && !question.getKeywordList().equals("[]")){
            List<KEYWORD> keywords = KeywordUtils.getMandatoryKeywordForSave(question.getKeywordList());

            SEARCH search = new SEARCH();
            search.setQuestion(question.getQuestion());
            questionDao.deleteAllQuestionKeywrod(search);
            if(keywords != null && keywords.size() != 0){
                search.setReview_keywords(keywords);
                questionDao.insertQuestionKeywrod(search);
            }
        }
        return result;
    }

    public List<QUESTION_ANSWER> listAnswer(SEARCH search) {
        List<QUESTION_ANSWER> list = questionDao.getListQuestionAnswer(search);
        for(int i=0; i< list.size(); i++){
            QUESTION_ANSWER answer = list.get(i);
            search.setAnswer_key(answer.getAnswer());
            list.get(i).setRe_answerList(questionDao.getListQuestionAnswer(search));
        }
        return list;
    }

    public Integer listAnswerCnt(SEARCH search) {
        return questionDao.getListQuestionAnswerCnt(search);
    }

    public Integer saveAnswer(QUESTION_ANSWER answer) throws Exception {
        // 파일 저장
        answer.setFiles(
                FileUtils.saveFiles(
                        answer.getFiles_new(),
                        uploadDir
                )
        );
        int result = questionDao.insertQuestionAnswer(answer);

        if(result != 0) {
            // 오늘 첫등록 일때 포인트 지급
            SEARCH paramSearch = new SEARCH();
            paramSearch.setMember(answer.getMember());
            paramSearch.setTodayCnt(true);
            Integer todayCnt = questionDao.getListQuestionAnswerCnt(paramSearch);

            if(todayCnt != null && todayCnt.intValue() == 1){
                MEMBER member = new MEMBER();
                member.setMember(answer.getMember());
                MEMBER memberInfo = memberDao.getMember(member);

                POINT point = new POINT();
                point.setValues(memberInfo.getMember(), "1", 3, memberInfo.getPoint() + 3, "커뮤니티 댓글 작성", "1");
                pointDao.insertPoint(point);
            }

            if(answer.getAnswer_key() == null){ // 답변
                QUESTION param = new QUESTION();
                param.setQuestion(answer.getQuestion());
                QUESTION question = questionDao.getQuestion(param);

                // 질문에 댓글 달릴 때 알림 TYPE 8
                INFO info = new INFO();
                info.setValues(question.getMember(),
                        "8",
                        question.getQuestion(),
                        "내 질문에 댓글이 달렸어요!\n지금 댓글을 확인해 보세요!",
                        "",
                        "[{\"name\":\"comment-off.png\",\"uuid\":\"comment-off\",\"path\":\"/image/common/comment-off.png\"}]");
                infoDao.insertInfo(info);
                sendPushMessage(question.getMember(), "질문댓글", "내 질문에 댓글이 달렸어요!\n지금 댓글을 확인해 보세요!");
            } else {
                QUESTION_ANSWER param = new QUESTION_ANSWER();
                param.setAnswer(answer.getAnswer_key());
                QUESTION_ANSWER question_answer = questionDao.getAnswer(param);

                // 질문 대댓글달림 알림 TYPE 13
                INFO info = new INFO();
                info.setValues(question_answer.getMember(),
                        "13",
                        answer.getQuestion(),
                        "내 댓글에 대댓글이 달렸어요!\n지금 대댓글을 확인해 보세요!",
                        "",
                        "[{\"name\":\"comment-off.png\",\"uuid\":\"comment-off\",\"path\":\"/image/common/comment-off.png\"}]");
                infoDao.insertInfo(info);
                sendPushMessage(question_answer.getMember(), "질문댓글", "내 댓글에 대댓글이 달렸어요!\n지금 대댓글을 확인해 보세요!");
            }
        }

        return result;
    }

    public Integer choose(QUESTION_ANSWER answer) {
        // 답변채택
        int result = questionDao.updateQuestionAnswerChoose(answer);

        if(result != 0) {
            QUESTION_ANSWER infoAnswer = questionDao.getAnswer(answer);

            // 포인트 적립
            MEMBER member = new MEMBER();
            member.setMember(infoAnswer.getMember());
            MEMBER memberInfo = memberDao.getMember(member);

            QUESTION param = new QUESTION();
            param.setQuestion(infoAnswer.getQuestion());
            QUESTION infoQuestion = questionDao.getQuestion(param);
            POINT point = new POINT();
            point.setValues(infoAnswer.getMember(), "1", infoQuestion.getPoint(), memberInfo.getPoint()+infoQuestion.getPoint(), "답변채택", "1");
            pointDao.insertPoint(point);

            // 회원 포인트 업데이트
            memberInfo.setPoint(infoQuestion.getPoint());
            memberDao.updateMemberPoint(memberInfo);

            // 질문 댓글 채택 시 알림 TYPE 7
            INFO info = new INFO();
            info.setValues(infoAnswer.getMember(),
                    "7",
                    infoAnswer.getQuestion(),
                    "내 댓글이 채택되었어요!\n지급된 포인트를 확인해 보세요!",
                    "",
                    "[{\"name\":\"comment-on.png\",\"uuid\":\"comment-on\",\"path\":\"/image/common/comment-on.png\"}]");
            infoDao.insertInfo(info);
            sendPushMessage(infoAnswer.getMember(), "질문하기", "내 댓글이 채택되었어요!\n지급된 포인트를 확인해 보세요!");
        }

        return result;
    }

    public Integer modiQuestionState(QUESTION question) {
        return questionDao.updateQuestionState(question);
    }

    public Integer removeAnswer(QUESTION_ANSWER answer) {
        return questionDao.deleteAnswer(answer);
    }

    public Integer like(QUESTION question) {
        Integer result = questionDao.getQuestionLike(question);
        if(result != null && result != 0){
            return questionDao.deleteQuestionLike(question);
        } else {
            return questionDao.insertQuestionLike(question);
        }
    }

    public Integer getLike(QUESTION question) {
        return questionDao.getQuestionLike(question);
    }

    public int likeAnswer(QUESTION_ANSWER answer) {
        Integer result = questionDao.getQuestionAnswerLike(answer);
        if(result != null && result != 0){
            return questionDao.deleteQuestionAnswerLike(answer);
        } else {
            return questionDao.insertQuestionAnswerLike(answer);
        }
    }

    public int saveClickNew(QUESTION question) {
        Integer result = questionDao.getQuestionMemberNew(question);
        if(result != null && result != 0){
            return 1;
        } else {
            return questionDao.insertQuestionMemberNew(question);
        }
    }
}
