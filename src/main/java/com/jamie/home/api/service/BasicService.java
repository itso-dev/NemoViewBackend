package com.jamie.home.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.dao.ContactDao;
import com.jamie.home.api.dao.FaqDao;
import com.jamie.home.api.dao.MemberDao;
import com.jamie.home.api.dao.ReviewDao;
import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.FILE;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class BasicService {
    @Autowired
    PasswordEncoder encoder;
    @Value("${file.upload.dir}")
    String uploadDir;
    @Autowired
    MemberDao memberDao;
    @Autowired
    ContactDao contactDao;
    @Autowired
    FaqDao faqDao;
    @Autowired
    ReviewDao reviewDao;
}
