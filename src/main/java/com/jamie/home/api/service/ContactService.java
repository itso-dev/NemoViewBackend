package com.jamie.home.api.service;

import com.jamie.home.api.model.CONTACT;
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
}
