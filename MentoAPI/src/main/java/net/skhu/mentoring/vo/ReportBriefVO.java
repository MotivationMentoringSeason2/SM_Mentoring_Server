package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Report;
import net.skhu.mentoring.domain.Schedule;
import net.skhu.mentoring.enumeration.ResultStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportBriefVO {
    private Long id;
    private Long scheduleId;
    private String classSubject;
    private String classType;
    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private ResultStatus status;
    private String message;

    public static ReportBriefVO builtToVO(Report report){
        Schedule schedule = report.getSchedule();
        if(schedule != null){
            LocalDate classDate = schedule.getStartDate().toLocalDate();
            LocalTime startTime = schedule.getStartDate().toLocalTime();
            LocalTime endTime = schedule.getEndDate().toLocalTime();
            return new ReportBriefVO(report.getId(), schedule.getId(), report.getClassSubject(), schedule.getMethod(), classDate, startTime, endTime, schedule.getStatus(), schedule.getAdminMessage());
        }
        else return null;
    }
}
