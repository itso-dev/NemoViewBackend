package com.jamie.home.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jamie.home.api.model.*;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
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
            // 정산 완료 알림 TYPE 3
            INFO info = new INFO();
            info.setValues(pointInfo.getMember(),
                    "3",
                    pointInfo.getMember(),
                    "계좌로 환급이 완료되었어요!\n변경 내역을 확인해 보세요!",
                    "",
                    "[{\"name\":\"point_transfer.png\",\"uuid\":\"point_transfer\",\"path\":\"/image/mypage/point_transfer.png\"}]");
            infoDao.insertInfo(info);
            sendPushMessage(pointInfo.getMember(), "포인트", "계좌로 환급이 완료되었어요!\n변경 내역을 확인해 보세요!");

        } else if("3".equals(point.getState())) { // 정산 거절
            // 정산 거절 시 포인트 반납
            POINT pointInfo = pointDao.getPoint(point);
            MEMBER member = new MEMBER();
            member.setMember(pointInfo.getMember());
            MEMBER memberInfo = memberDao.getMember(member);
            memberInfo.setPoint(pointInfo.getValue());
            memberDao.updateMemberPoint(memberInfo);

            // 정산 거절 알림 TYPE 4
            INFO info = new INFO();
            info.setValues(pointInfo.getMember(),
                    "4",
                    pointInfo.getMember(),
                    "환급 신청에 실패했어요!\n지금 사유를 확인해 보세요!",
                    point.getReject(),
                    "[{\"name\":\"point-icon.png\",\"uuid\":\"point-icon\",\"path\":\"/image/mypage/point-icon.png\"}]");
            infoDao.insertInfo(info);
            sendPushMessage(pointInfo.getMember(), "포인트", "환급 신청에 실패했어요!\n지금 사유를 확인해 보세요!");
        }

        return pointDao.updatePointState(point);
    }

    public int modiPointStateAll(SEARCH search) throws JsonProcessingException {
        int result = 0;
        ObjectMapper mapper = new ObjectMapper();
        List<Integer> list = Arrays.asList(mapper.readValue(search.getChkPointList(), Integer[].class));
        for(int i=0; i<list.size(); i++){
            POINT point = new POINT();
            point.setPoint(list.get(i));
            point.setState("1");

            POINT pointInfo = pointDao.getPoint(point);
            // 모든 정산 완료 알림 TYPE 3
            INFO info = new INFO();
            info.setValues(pointInfo.getMember(),
                    "3",
                    pointInfo.getMember(),
                    "계좌로 환급이 완료되었어요!\n변경 내역을 확인해 보세요!",
                    "",
                    "[{\"name\":\"point_transfer.png\",\"uuid\":\"point_transfer\",\"path\":\"/image/mypage/point_transfer.png\"}]");
            infoDao.insertInfo(info);
            sendPushMessage(pointInfo.getMember(), "포인트", "계좌로 환급이 완료되었어요!\n변경 내역을 확인해 보세요!");

            result += pointDao.updatePointState(point);
        }
        return result;
    }

    public POINT listCnts(SEARCH search) {
        return pointDao.getListPointCnts(search);
    }
}
