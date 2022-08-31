package com.jamie.home.api.service;

import com.jamie.home.api.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Autowired
    ReportDao reportDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    PointDao pointDao;
    @Autowired
    InfoDao infoDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    SearchDao searchDao;
    @Autowired
    BannerDao bannerDao;
    @Autowired
    JavaMailSender javaMailSender;
}
