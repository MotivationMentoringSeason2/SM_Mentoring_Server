package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.ClassPhoto;
import net.skhu.mentoring.domain.Report;
import net.skhu.mentoring.domain.Schedule;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.model.ReportModel;
import net.skhu.mentoring.repository.ClassPhotoRepository;
import net.skhu.mentoring.repository.ReportRepository;
import net.skhu.mentoring.repository.ScheduleRepository;
import net.skhu.mentoring.repository.SemesterRepository;
import net.skhu.mentoring.repository.TeamRepository;
import net.skhu.mentoring.service.interfaces.ReportService;
import net.skhu.mentoring.vo.ReportBriefVO;
import net.skhu.mentoring.vo.ReportViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private ClassPhotoRepository classPhotoRepository;

    private String getFileSuffix(final String fileName) {
        int infix = fileName.lastIndexOf('.');
        return fileName.substring(infix, fileName.length());
    }

    private void uploadingReportPhotoFile(final MultipartFile photoFile, final Report report) throws IOException {
        Optional<ClassPhoto> classPhoto = classPhotoRepository.findByReport(report);
        if(classPhoto.isPresent()){
            ClassPhoto tmpClassPhoto = classPhoto.get();
            tmpClassPhoto.setFileName(photoFile.getOriginalFilename());
            tmpClassPhoto.setFileSize(photoFile.getSize());
            tmpClassPhoto.setFileData(photoFile.getBytes());
            tmpClassPhoto.setFileSuffix(this.getFileSuffix(photoFile.getOriginalFilename()).toUpperCase());
            tmpClassPhoto.setUploadDate(LocalDateTime.now());
            classPhotoRepository.save(tmpClassPhoto);
        } else {
            ClassPhoto newClassPhoto = new ClassPhoto(0L, report, photoFile.getOriginalFilename(), photoFile.getSize(), photoFile.getBytes(), this.getFileSuffix(photoFile.getOriginalFilename()).toUpperCase(), LocalDateTime.now());
            classPhotoRepository.save(newClassPhoto);
        }
    }

    @Override
    public List<ReportBriefVO> fetchReportListByTeamId(final Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            List<Report> reports = reportRepository.findByScheduleTeam(team.get());
            return reports.stream()
                    .map(report -> ReportBriefVO.builtToVO(report))
                    .collect(Collectors.toList());
        } else return null;
    }

    @Override
    public ReportViewVO fetchReportViewById(final Long reportId) {
        Optional<Report> report = reportRepository.findById(reportId);
        if(report.isPresent()){
            Optional<ClassPhoto> classPhoto = classPhotoRepository.findByReport(report.get());
            return ReportViewVO.builtToVO(report.get(), classPhoto.get());
        } else return null;
    }

    @Override
    public ReportModel fetchReportModelById(final Long reportId) {
        Optional<Report> report = reportRepository.findById(reportId);
        if(report.isPresent()) return ReportModel.builtToModel(report.get());
        else return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> createReportWithScheduleId(final Long scheduleId, final ReportModel reportModel, final MultipartFile photoFile) throws IOException {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if(schedule.isPresent()){
            Report report = new Report();
            report.setSchedule(schedule.get());
            report.setPresentDate(LocalDateTime.now());
            report.setClassSubject(reportModel.getClassSubject());
            report.setClassPlace(reportModel.getClassPlace());
            report.setClassBriefing(reportModel.getClassBriefing());
            report.setAbsentPerson(reportModel.getAbsentPerson());
            Report newReport = reportRepository.save(report);
            if(photoFile != null){
                this.uploadingReportPhotoFile(photoFile, newReport);
            }
            return new ResponseEntity<>("작성하신 보고서가 정상적으로 추가 되었습니다.", HttpStatus.OK);
        } else return new ResponseEntity<>("선택하신 스케쥴이 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateReportWithScheduleId(final Long scheduleId, final ReportModel reportModel, final MultipartFile photoFile) throws IOException {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if(schedule.isPresent()){
            Optional<Report> report = reportRepository.findBySchedule(schedule.get());
            if(report.isPresent()) {
                Report updateReport = report.get();
                updateReport.setSchedule(schedule.get());
                updateReport.setPresentDate(LocalDateTime.now());
                updateReport.setClassSubject(reportModel.getClassSubject());
                updateReport.setClassPlace(reportModel.getClassPlace());
                updateReport.setClassBriefing(reportModel.getClassBriefing());
                updateReport.setAbsentPerson(reportModel.getAbsentPerson());
                reportRepository.save(updateReport);
                if (photoFile != null) {
                    this.uploadingReportPhotoFile(photoFile, updateReport);
                }
                return new ResponseEntity<>("작성하신 보고서가 정상적으로 수정  되었습니다.", HttpStatus.OK);
            } else return new ResponseEntity<>("선택하신 스케쥴이 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else return new ResponseEntity<>("선택하신 스케쥴이 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteReportByIdList(final List<Long> reportIds) {
        if(reportRepository.existsByIdIn(reportIds)){
            reportRepository.deleteByIdIn(reportIds);
            return new ResponseEntity<>("선택하신 보고서의 목록이 삭제 되었습니다. 스케쥴은 그대로 남아 있으니 이를 토대로 다시 작성하시면 됩니다.", HttpStatus.OK);
        } else return new ResponseEntity<>("선택하신 보고서가 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteReportByTeam(final Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()) {
            if(reportRepository.existsByScheduleTeam(team.get())) {
                reportRepository.deleteByScheduleTeam(team.get());
                return new ResponseEntity<>("선택하신 멘토로 모든 보고서가 삭제 되었습니다. 복구는 불가능합니다.", HttpStatus.OK);
            } else return new ResponseEntity<>("선택하신 멘토는 보고서를 올리지 않아 삭제를 진행하지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else return new ResponseEntity<>("선택하신 멘토가 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
}
