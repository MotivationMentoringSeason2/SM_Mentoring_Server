package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.model.ConfirmModel;
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

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("MentoAPI")
public class ScheduleRestController {
    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("schedules/{teamId}")
    public ResponseEntity<?> fetchSchedulesByTeamId(@PathVariable Long teamId){
        return ResponseEntity.ok(scheduleService.fetchBriefListByTeamId(teamId));
    }

    @GetMapping("schedule/model/{scheduleId}")
    public ResponseEntity<?> fetchScheduleModelById(@PathVariable Long scheduleId){
        return ResponseEntity.ok(scheduleService.fetchScheduleModelById(scheduleId));
    }

    @PostMapping("schedule/{teamId}")
    public ResponseEntity<String> executeScheduleCreating(@PathVariable Long teamId, @RequestBody ScheduleModel scheduleModel){
        return scheduleService.createScheduleByModelAndTeamId(scheduleModel, teamId);
    }

    @PutMapping("schedule/{teamId}/{scheduleId}")
    public ResponseEntity<String> executeScheduleUpdating(@PathVariable Long teamId, @PathVariable Long scheduleId, @RequestBody ScheduleModel scheduleModel){
        return scheduleService.updateScheduleByModelAndTeamId(scheduleModel, teamId, scheduleId);
    }

    @PutMapping("schedule/confirm/{scheduleId}")
    public ResponseEntity<String> executeScheduleRecognizing(@PathVariable Long scheduleId, @RequestBody ConfirmModel confirmModel){
        return scheduleService.updateAdminMessageAndStatus(scheduleId, confirmModel);
    }

    @DeleteMapping("schedules")
    public ResponseEntity<String> executeScheduleRemoving(@RequestBody List<Long> scheduleIds){
        return scheduleService.deleteScheduleByIdList(scheduleIds);
    }

    @DeleteMapping("schedules/{teamId}")
    public ResponseEntity<String> executeSubjectRemovingByTeamId(@PathVariable Long teamId){
        return scheduleService.deleteScheduleByTeam(teamId);
    }
}
