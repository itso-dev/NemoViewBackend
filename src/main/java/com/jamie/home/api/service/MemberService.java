package com.jamie.home.api.service;

import com.jamie.home.api.dao.MemberDao;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.ROLE;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.util.CodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService extends BasicService{
    public MEMBER get(MEMBER member){
        return memberDao.getMember(member);
    }

    public int save(MEMBER member) {
        while(true){ // 회원 고유 코드값 생성
            member.setCode(CodeUtils.getCodeValue());
            int codeCnt = memberDao.checkCode(member);
            if(codeCnt == 0){
                break;
            }
        }
        // 비밀번호 암호화
        member.setPassword(encoder.encode(member.getPassword()));
        member.setRole(ROLE.ROLE_USER);

        return memberDao.insertMember(member);
    }

    public int modi(MEMBER member) {
        if(member.getPassword() != null){
            member.setPassword(encoder.encode(member.getPassword()));
        }
        return memberDao.updateMember(member);
    }

    public MEMBER checkEmail(MEMBER member) {
        return memberDao.checkEmail(member);
    }

    public int updateLogDate(MEMBER member) {
        return memberDao.updateLogDate(member);
    }
}
