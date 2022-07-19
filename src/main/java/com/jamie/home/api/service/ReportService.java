package com.jamie.home.api.service;

import com.jamie.home.api.model.REPORT;
import com.jamie.home.util.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReportService extends BasicService{
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
}
