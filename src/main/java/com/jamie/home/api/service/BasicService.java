package com.jamie.home.api.service;

import com.jamie.home.api.controller.AdminController;
import com.jamie.home.api.dao.*;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.ResponseOverlays;
import com.jamie.home.util.FirebaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

@Service
@Transactional
public class BasicService {
    private static final Logger logger = LoggerFactory.getLogger(BasicService.class);
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

    public void sendPushMessage(Integer key, String title, String body){
       /* if(key == null){ // 전체 푸시
            try {
                FirebaseUtils.sendMessageTo(null,title,body);
            } catch (Exception e){
                logger.error(e.getLocalizedMessage());
            }
        } else { // 타겟팅
            MEMBER param = new MEMBER();
            param.setMember(key);
            MEMBER member = memberDao.getMember(param);

            if(member == null){
                logger.error("We can't find the member info");
            }

            try {
                if(member.getAndroid() != null && !"".equals(member.getAndroid())){
                    FirebaseUtils.sendMessageTo(member.getAndroid(), title, body);
                }
                if(member.getIos() != null && !"".equals(member.getIos())){
                    FirebaseUtils.sendMessageTo(member.getIos(), title, body);
                }
            } catch (Exception e){
                logger.error(e.getLocalizedMessage());
            }
        }*/
    }
}
