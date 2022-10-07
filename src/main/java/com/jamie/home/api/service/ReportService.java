package com.jamie.home.api.service;

import com.jamie.home.api.model.CONTACT;
import com.jamie.home.api.model.REPORT;
import com.jamie.home.api.model.SEARCH;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ReportService extends BasicService{
    public List<REPORT> list(SEARCH search) {
        return reportDao.getListReport(search);
    }

    public Integer listCnt(SEARCH search) {
        return reportDao.getListReportCnt(search);
    }

    public Integer save(REPORT report) throws Exception {
        report.setFiles(
                FileUtils.saveFiles(
                        report.getSaveFiles(),
                        uploadDir
                )
        );
        report.setSaveFiles(null);
        return reportDao.insertReport(report);
    }

    public REPORT get(REPORT report) {
        return reportDao.getReport(report);
    }
}
