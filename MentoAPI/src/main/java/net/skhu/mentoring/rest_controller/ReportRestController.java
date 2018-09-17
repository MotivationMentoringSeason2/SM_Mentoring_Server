package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.model.ReportModel;
import net.skhu.mentoring.service.interfaces.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("MentoAPI")
public class ReportRestController {
    @Autowired
    private ReportService reportService;

    @GetMapping("reports/{teamId}")
    public ResponseEntity<String> fetchReportListByTeamId(@PathVariable Long teamId){
        return ResponseEntity.ok("팀 별 보고서 목록을 불러옵니다.");
    }

    @GetMapping("report/{reportId}")
    public ResponseEntity<String> fetchReportById(@PathVariable Long reportId){
        return ResponseEntity.ok("보고서 한 단위를 불러옵니다.");
    }

    @PostMapping("report")
    public ResponseEntity<String> executeReportCreating(@RequestBody ReportModel reportModel){
        return ResponseEntity.ok("보고서를 추가 하였습니다.");
    }

    @PutMapping("report")
    public ResponseEntity<String> executeReportUpdating(@RequestBody ReportModel reportModel){
        return ResponseEntity.ok("보고서를 수정 하였습니다.");
    }

    @DeleteMapping("report/{reportId}")
    public ResponseEntity<String> executeReportRemoving(@PathVariable Long reportId){
        return ResponseEntity.ok("보고서를 지도 교수나 관리자에 의해 삭제 받았습니다.");
    }

    @DeleteMapping("subjects/{teamId}")
    public ResponseEntity<String> executeSubjectRemovingByTeamId(@PathVariable Long teamId){
        return ResponseEntity.ok("관리자가 이제 볼 필요 없는 팀의 아이디로 보고서를 지웁니다.");
    }
}
