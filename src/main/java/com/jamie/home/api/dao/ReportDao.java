package com.jamie.home.api.dao;

import com.jamie.home.api.model.REPORT;
import com.jamie.home.api.model.SEARCH;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ReportDao {
    List<REPORT> getListReport(SEARCH search);

    Integer getListReportCnt(SEARCH search);

    Integer insertReport(REPORT report);

    REPORT getReport(REPORT report);
}
