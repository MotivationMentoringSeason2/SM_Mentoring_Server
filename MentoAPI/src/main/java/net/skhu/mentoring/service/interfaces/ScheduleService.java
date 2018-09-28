package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.ConfirmModel;
import net.skhu.mentoring.model.ScheduleModel;
import net.skhu.mentoring.vo.ScheduleBriefVO;
import net.skhu.mentoring.vo.TotalActivityVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ScheduleService {
    TotalActivityVO fetchMentoringActivityByTeamId(final Long teamId);
    List<ScheduleBriefVO> fetchBriefListByTeamId(final Long teamId);
    ScheduleModel fetchScheduleModelById(final Long scheduleId);

    ResponseEntity<String> createScheduleByModelAndTeamId(final ScheduleModel scheduleModel, final Long teamId);
    ResponseEntity<String> updateScheduleByModelAndTeamId(final ScheduleModel scheduleModel, final Long teamId, final Long scheduleId);
    ResponseEntity<String> updateAdminMessageAndStatus(final Long scheduleId, final ConfirmModel confirmModel);
    ResponseEntity<String> deleteScheduleByIdList(final List<Long> scheduleIds);
    ResponseEntity<String> deleteScheduleByTeam(final Long teamId);
}
