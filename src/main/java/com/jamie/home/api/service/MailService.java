package com.jamie.home.api.service;

import com.jamie.home.api.dao.MemberDao;
import com.jamie.home.api.model.MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.*;
import java.util.UUID;

@Service
@Transactional
public class MailService extends BasicService{

    public void sendMail(MEMBER member) throws Exception {
        String uuid = UUID.randomUUID().toString();
        String uuidPw = uuid.substring(0,uuid.indexOf('-')).toUpperCase();

        member.setPassword(encoder.encode(uuidPw));

        memberDao.updateMember(member);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper msgHelper = new MimeMessageHelper(message, true, "UTF-8");
        // 수신 대상
        msgHelper.setTo(member.getEmail());
        msgHelper.setFrom("sender@gmail.com");

        msgHelper.setSubject("[네모리뷰] 비밀번호 임시발급");
        String htmlContent = "임시 비밀번호["+uuidPw+"]로 로그인해주시기 바랍니다.<br>";
        msgHelper.setText(htmlContent, true);
        javaMailSender.send(message);
    }
}
