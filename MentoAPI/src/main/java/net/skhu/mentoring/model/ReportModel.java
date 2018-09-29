package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Report;
import net.skhu.mentoring.domain.Schedule;
import net.skhu.mentoring.domain.Team;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportModel {
    private Long teamId;
    private String classPlace;
    private String classSubject;
    private String classBriefing;
    private String absentPerson;

    public static ReportModel builtToModel(Report report){
        Schedule schedule = report.getSchedule();
        Team team = schedule != null ? schedule.getTeam() : null;
        return new ReportModel(team != null ? team.getId() : 0, report.getClassPlace(), report.getClassSubject(), report.getClassBriefing(), report.getAbsentPerson());
    }
}
