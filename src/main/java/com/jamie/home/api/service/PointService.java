package com.jamie.home.api.service;

import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.MEMBER;
import com.jamie.home.api.model.POINT;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PointService extends BasicService{
    public List<POINT> list(SEARCH search) {
        return pointDao.getListPoint(search);
    }

    public Integer listCnt(SEARCH search) {
        return pointDao.getListPointCnt(search);
    }

    public Integer save(POINT point) throws Exception {
        MEMBER member = new MEMBER();
        member.setMember(point.getMember());
        member.setPoint(point.getValue()*(-1));
        memberDao.updateMemberPoint(member);
        return pointDao.insertPoint(point);
    }
    public Integer modiPointState(POINT point) {
        return pointDao.updatePointState(point);
    }
}
