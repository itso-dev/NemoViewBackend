package com.jamie.home.api.dao;

import com.jamie.home.api.model.REPORT;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReportDao {
    Integer insertReport(REPORT report);
}
