package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.model.ScheduleModel;
import net.skhu.mentoring.service.interfaces.ScheduleService;
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
public class ScheduleRestController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("schedules/{teamId}")
    public ResponseEntity<String> fetchSchedulesByTeamId(@PathVariable Long teamId){
        return ResponseEntity.ok("팀 번호(멘토 번호)를 이용하여 스케쥴 목록을 가져옵니다.");
    }

    @GetMapping("schedule/{scheduleId}")
    public ResponseEntity<String> fetchScheduleById(@PathVariable Long scheduleId){
        return ResponseEntity.ok("스케쥴 한 단위를 가져옵니다.");
    }

    @GetMapping("schedule/current")
    public ResponseEntity<String> fetchCurrentSemester(){
        return ResponseEntity.ok("현재 해당되는 학기를 가져옵니다.");
    }

    @PostMapping("schedule")
    public ResponseEntity<String> executeScheduleCreating(@RequestBody ScheduleModel scheduleModel){
        return ResponseEntity.ok("스케쥴 한 단위를 제작합니다.");
    }

    @PutMapping("schedule")
    public ResponseEntity<String> executeScheduleUpdating(@RequestBody ScheduleModel scheduleModel){
        return ResponseEntity.ok("스케쥴 한 단위를 수정합니다.");
    }

    @PutMapping("schedule/recognized/{scheduleId}")
    public ResponseEntity<String> executeScheduleRecognizing(@PathVariable Long scheduleId){
        return ResponseEntity.ok("스케쥴을 지도 교수 혹은 관리자에게 검토 받았습니다. 인정 혹은 미흡 둘 다 해당됨.");
    }

    @DeleteMapping("schedule/{scheduleId}")
    public ResponseEntity<String> executeScheduleRemoving(@PathVariable Long scheduleId){
        return ResponseEntity.ok("스케쥴 한 단위를 삭제합니다.");
    }

    @DeleteMapping("schedules/{teamId}")
    public ResponseEntity<String> executeSubjectRemovingByTeamId(@PathVariable Long teamId){
        return ResponseEntity.ok("관리자가 이제 볼 필요 없는 팀의 아이디로 스케쥴을 삭제합니다.");
    }
}
