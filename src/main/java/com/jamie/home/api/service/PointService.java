package com.jamie.home.api.service;

import com.jamie.home.api.model.*;
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
        if("1".equals(point.getState())){ //정산 완료
            POINT pointInfo = pointDao.getPoint(point);
            // 답변채택 알림 TYPE 3
            INFO info = new INFO();
            info.setValues(pointInfo.getMember(), "3", pointInfo.getMember(), "계좌로 환급이 완료되었어요! 변경 내역을 확인해 보세요!", "");
            infoDao.insertInfo(info);

        } else if("3".equals(point.getState())) { // 정산 거절
            // 정산 거절 시 포인트 반납
            POINT pointInfo = pointDao.getPoint(point);
            MEMBER member = new MEMBER();
            member.setMember(pointInfo.getMember());
            MEMBER memberInfo = memberDao.getMember(member);
            memberInfo.setPoint(pointInfo.getPoint());
            memberDao.updateMemberPoint(memberInfo);

            // 답변채택 알림 TYPE 4
            INFO info = new INFO();
            info.setValues(pointInfo.getMember(), "4", pointInfo.getMember(), "환급 신청에 실패했어요! 지금 사유를 확인해 보세요!", point.getReject());
            infoDao.insertInfo(info);
        }

        return pointDao.updatePointState(point);
    }
}
