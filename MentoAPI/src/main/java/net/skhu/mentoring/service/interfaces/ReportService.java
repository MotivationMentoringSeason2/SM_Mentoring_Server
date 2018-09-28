package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.ReportModel;
import net.skhu.mentoring.vo.ReportBriefVO;
import net.skhu.mentoring.vo.ReportViewVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReportService {
    List<ReportBriefVO> fetchReportListByTeamId(final Long teamId);
    ReportViewVO fetchReportViewById(final Long reportId);
    ReportModel fetchReportModelById(final Long reportId);

    ResponseEntity<String> createReportWithScheduleId(final Long scheduleId, final ReportModel reportModel, final MultipartFile photoFile) throws IOException;
    ResponseEntity<String> updateReportWithScheduleId(final Long scheduleId, final ReportModel reportModel, final MultipartFile photoFile) throws IOException;
    ResponseEntity<String> deleteReportByIdList(final List<Long> reportIds);
    ResponseEntity<String> deleteReportByTeam(final Long teamId);
}
