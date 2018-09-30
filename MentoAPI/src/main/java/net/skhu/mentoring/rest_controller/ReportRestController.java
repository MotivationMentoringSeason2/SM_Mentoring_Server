package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.domain.ClassPhoto;
import net.skhu.mentoring.model.ReportModel;
import net.skhu.mentoring.service.interfaces.ReportExcelService;
import net.skhu.mentoring.service.interfaces.ReportService;
import net.skhu.mentoring.service.interfaces.ScheduleService;
import net.skhu.mentoring.service.interfaces.TeamService;
import net.skhu.mentoring.vo.MentoVO;
import net.skhu.mentoring.vo.ReportListVO;
import net.skhu.mentoring.vo.ReportViewVO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("MentoAPI")
public class ReportRestController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ReportExcelService reportExcelService;

    private HttpHeaders generateImageHeader(ClassPhoto classPhoto) {
        HttpHeaders header = new HttpHeaders();
        switch (classPhoto.getFileSuffix()) {
            case "JPG" :
            case "JPEG" :
                header.setContentType(MediaType.IMAGE_JPEG);
                break;
            case "PNG" :
                header.setContentType(MediaType.IMAGE_PNG);
                break;
            case "GIF" :
                header.setContentType(MediaType.IMAGE_GIF);
                break;
        }
        return header;
    }

    @GetMapping("reports/{teamId}")
    public ResponseEntity<ReportListVO> fetchReportListByTeamId(@PathVariable Long teamId){
        return ResponseEntity.ok(ReportListVO.builtToVO(reportService.fetchReportListByTeamId(teamId), scheduleService.fetchMentoringActivityByTeamId(teamId)));
    }

    @GetMapping("report/{reportId}")
    public ResponseEntity<ReportViewVO> fetchReportById(@PathVariable Long reportId){
        return ResponseEntity.ok(reportService.fetchReportViewById(reportId));
    }

    @GetMapping("report/model/{reportId}")
    public ResponseEntity<ReportModel> fetchReportModelById(@PathVariable Long reportId){
        return ResponseEntity.ok(reportService.fetchReportModelById(reportId));
    }

    @GetMapping("report/photo/{photoId}")
    public ResponseEntity<?> fetchEachProfile(@PathVariable Long photoId) {
        ClassPhoto classPhoto = reportService.fetchClassPhotoById(photoId);
        if (classPhoto != null) {
            HttpHeaders headers = this.generateImageHeader(classPhoto);
            return new ResponseEntity<>(classPhoto.getFileData(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "report/{scheduleId}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> executeReportCreating(@PathVariable Long scheduleId, @RequestPart("reportModel") ReportModel reportModel, @RequestPart("photoFile") MultipartFile photoFile) throws IOException {
        return reportService.createReportWithScheduleId(scheduleId, reportModel, photoFile);
    }

    @PutMapping("report/context/{scheduleId}")
    public ResponseEntity<String> executeReportUpdatingContextOnly(@PathVariable Long scheduleId, @RequestBody ReportModel reportModel) throws IOException {
        return reportService.updateReportWithScheduleId(scheduleId, reportModel, null);
    }

    @PutMapping(value = "report/{scheduleId}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> executeReportUpdating(@PathVariable Long scheduleId, @RequestPart("reportModel") ReportModel reportModel, @RequestPart("photoFile") MultipartFile photoFile) throws IOException {
        return reportService.updateReportWithScheduleId(scheduleId, reportModel, photoFile);
    }

    @DeleteMapping("reports")
    public ResponseEntity<String> executeReportRemoving(@RequestBody List<Long> reportIds){
        return reportService.deleteReportByIdList(reportIds);
    }

    @DeleteMapping("report/{teamId}")
    public ResponseEntity<String> executeRemovingByTeamId(@PathVariable Long teamId){
        return reportService.deleteReportByTeam(teamId);
    }

    @GetMapping("report/excel/{teamId}/{realname}")
    public void executeReportListByMento(@PathVariable Long teamId, @PathVariable String realname, HttpServletResponse response) throws Exception{
        MentoVO mentoVO = teamService.fetchMentoInfoByTeamId(teamId);
        if(mentoVO != null) {
            XSSFWorkbook workbook = reportExcelService.fetchReportMultiWorkbook(teamId, realname);
            String fileName = URLEncoder.encode(String.format("%s_멘토링_보고서_목록.xlsx", mentoVO.getName()), "UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ";");
            try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
                workbook.write(output);
            }
        }
    }
}
