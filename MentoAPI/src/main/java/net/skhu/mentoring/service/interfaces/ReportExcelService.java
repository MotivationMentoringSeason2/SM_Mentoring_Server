package net.skhu.mentoring.service.interfaces;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;

public interface ReportExcelService {
    XSSFWorkbook fetchReportMultiWorkbook(final Long teamId, final String realName) throws IOException;
}
