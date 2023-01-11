package com.jamie.home.api.service;

import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.INFO;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ContactService extends BasicService{
    public List<CONTACT> list(SEARCH search) {
        return contactDao.getListContact(search);
    }

    public Integer listCnt(SEARCH search) {
        return contactDao.getListContactCnt(search);
    }

    public Integer save(CONTACT contact) throws Exception {
        contact.setFiles(
                FileUtils.saveFiles(
                        contact.getSaveFiles(),
                        uploadDir
                )
        );
        contact.setSaveFiles(null);
        return contactDao.insertContact(contact);
    }

    public Integer modi(CONTACT contact) throws Exception {
        contact.setFiles(
                FileUtils.modiFiles(
                        contact.getFiles(),
                        contact.getDeleteFiles(),
                        contact.getSaveFiles(),
                        uploadDir
                )
        );

        contact.setSaveFiles(null);
        return contactDao.updateContact(contact);
    }

    public Integer modiContactAnswer(CONTACT contact){
        CONTACT contactInfo = contactDao.getContact(contact);

        // 고객센터 답변 등록 알림 TYPE 5
        if(contactInfo.getAnswer() == null){ // 중복알림 방지
            INFO info = new INFO();
            info.setValues(contactInfo.getMember(),
                    "5",
                    contactInfo.getContact(),
                    "고객 센터로 문의한 질문에 답변이 등록되었어요.\n지금 확인해 보세요!",
                    "",
                    "[{\"name\":\"qna-icon.png\",\"uuid\":\"qna-icon\",\"path\":\"/image/mypage/qna-icon.png\"}]");
            infoDao.insertInfo(info);
            sendPushMessage(contactInfo.getMember(), "문의하기", "고객 센터로 문의한 질문에 답변이 등록되었어요.\n지금 확인해 보세요!");
        }

        return contactDao.updateContactAnswer(contact);
    }

    public CONTACT get(CONTACT contact) {
        return contactDao.getContact(contact);
    }
}
