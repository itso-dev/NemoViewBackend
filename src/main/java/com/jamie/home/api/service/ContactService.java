package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.dao.ContactDao;
import com.jamie.home.api.dao.FaqDao;
import com.jamie.home.api.model.*;
import com.jamie.home.util.CodeUtils;
import com.jamie.home.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
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

    public int save(CONTACT contact) throws Exception {
        contact.setFiles(
                FileUtils.saveFiles(
                        contact.getSaveFiles(),
                        uploadDir
                )
        );
        contact.setSaveFiles(null);
        return contactDao.insertContact(contact);
    }

    public int modi(CONTACT contact) throws Exception {
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
