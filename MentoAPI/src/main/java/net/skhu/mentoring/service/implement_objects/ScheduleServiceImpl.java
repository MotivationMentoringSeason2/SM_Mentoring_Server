package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Schedule;
import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.enumeration.ResultStatus;
import net.skhu.mentoring.model.ConfirmModel;
import net.skhu.mentoring.model.ScheduleModel;
import net.skhu.mentoring.repository.ScheduleRepository;
import net.skhu.mentoring.repository.SemesterRepository;
import net.skhu.mentoring.repository.TeamRepository;
import net.skhu.mentoring.service.interfaces.ScheduleService;
import net.skhu.mentoring.vo.ScheduleBriefVO;
import net.skhu.mentoring.vo.TotalActivityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public TotalActivityVO fetchMentoringActivityByTeamId(final Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            List<Schedule> schedules = scheduleRepository.findByTeamAndStatus(team.get(), ResultStatus.PERMIT);
            return TotalActivityVO.builtToVO(schedules);
        } else return null;
    }

    @Override
    public List<ScheduleBriefVO> fetchBriefListByTeamId(final Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            List<Schedule> schedules = scheduleRepository.findByTeam(team.get());
            return schedules.stream()
                    .map(schedule -> ScheduleBriefVO.builtToVO(schedule))
                    .collect(Collectors.toList());
        } else return null;
    }

    @Override
    public ScheduleModel fetchScheduleModelById(final Long scheduleId) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if(schedule.isPresent())
            return ScheduleModel.builtToVO(schedule.get());
        else return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> createScheduleByModelAndTeamId(final ScheduleModel scheduleModel, final Long teamId) {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if(semester.isPresent()){
            Optional<Team> team = teamRepository.findByIdAndSemester(teamId, semester.get());
            if(team.isPresent()){
                Schedule schedule = new Schedule();
                schedule.setTeam(team.get());
                schedule.setMethod(scheduleModel.getMethod());
                schedule.setStartDate(LocalDateTime.of(scheduleModel.getClassDate(), scheduleModel.getStartTime()));
                schedule.setEndDate(LocalDateTime.of(scheduleModel.getClassDate(), scheduleModel.getEndTime()));
                schedule.setStatus(ResultStatus.LOADING);
                schedule.setAdminMessage("");
                scheduleRepository.save(schedule);
                return ResponseEntity.ok("새로운 수업 시간 일정이 추가 되었습니다.");
            } else return new ResponseEntity<>("선택하신 팀은 현재 운영하지 않는 멘토링으로 수업 시간 등록이 불가능합니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else return new ResponseEntity<>("학기 조회 중에 문제가 발생했습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateScheduleByModelAndTeamId(final ScheduleModel scheduleModel, final Long teamId, final Long scheduleId) {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if(semester.isPresent()){
            Optional<Team> team = teamRepository.findByIdAndSemester(teamId, semester.get());
            if(team.isPresent()){
                Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
                if(schedule.isPresent()) {
                    Schedule tmpSchedule = schedule.get();
                    if(tmpSchedule.getStatus().equals(ResultStatus.PERMIT)){
                        return new ResponseEntity<>("선택하신 수업 일정의 보고서가 확인 완료 되어 수업 시간을 변경할 수 없습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
                    }
                    tmpSchedule.setMethod(scheduleModel.getMethod());
                    tmpSchedule.setStartDate(LocalDateTime.of(scheduleModel.getClassDate(), scheduleModel.getStartTime()));
                    tmpSchedule.setEndDate(LocalDateTime.of(scheduleModel.getClassDate(), scheduleModel.getEndTime()));
                    tmpSchedule.setStatus(tmpSchedule.getStatus());
                    tmpSchedule.setAdminMessage(tmpSchedule.getAdminMessage());
                    scheduleRepository.save(tmpSchedule);
                    return ResponseEntity.ok("수업 시간 일정 변경 작업이 완료 되었습니다.");
                } else return new ResponseEntity<>("선택하신 수업 일정이 존재하지 않아 수정 작업을 진행하지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            } else return new ResponseEntity<>("선택하신 팀은 현재 운영하지 않는 멘토링으로 수업 시간 수정이 불가능합니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else return new ResponseEntity<>("학기 조회 중에 문제가 발생했습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> updateAdminMessageAndStatus(final Long scheduleId, final ConfirmModel confirmModel) {
        Optional<Schedule> schedule = scheduleRepository.findById(scheduleId);
        if(schedule.isPresent()) {
            Schedule tmpSchedule = schedule.get();
            tmpSchedule.setStatus(confirmModel.getStatus());
            tmpSchedule.setAdminMessage(String.format("%s - %s", confirmModel.getAdmin(), confirmModel.getMessage()));
            scheduleRepository.save(tmpSchedule);
            return ResponseEntity.ok("수업 시간 및 보고서에 대한 커멘트를 모두 달았습니다.");
        } else return new ResponseEntity<>("선택하신 수업 일정이 존재하지 않아 수정 작업을 진행하지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteScheduleByIdList(final List<Long> scheduleIds) {
        if(scheduleRepository.existsByIdIn(scheduleIds)){
            scheduleRepository.deleteByIdIn(scheduleIds);
            return new ResponseEntity<>("선택하신 수업 시간 목록이 삭제 되었습니다.", HttpStatus.OK);
        } else return new ResponseEntity<>("선택하신 수업 시간이 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteScheduleByTeam(final Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent())
            if(scheduleRepository.existsByTeam(team.get())){
                scheduleRepository.deleteByTeam(team.get());
                return new ResponseEntity<>("선택하신 멘토로 모든 수업 시간이 삭제 되었습니다. 복구는 불가능합니다.", HttpStatus.OK);
            } else return new ResponseEntity<>("선택하신 멘토가 가진 수업 시간이 없습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        else return new ResponseEntity<>("선택하신 멘토가 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
}
