package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.ClassPhoto;
import net.skhu.mentoring.domain.Report;
import net.skhu.mentoring.domain.Schedule;
import net.skhu.mentoring.enumeration.ResultStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportViewVO {
    private Long scheduleId;
    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String method;
    private String classPlace;
    private String classSubject;
    private String classBriefing;
    private String absentPerson;
    private ResultStatus status;
    private String adminMessage;
    private Long classPhotoId;
    private String fileName;

    public static ReportViewVO builtToVO(Report report, ClassPhoto classPhoto){
        Schedule schedule = report.getSchedule();
        if(schedule != null){
            LocalDate classDate = schedule.getStartDate().toLocalDate();
            LocalTime startTime = schedule.getStartDate().toLocalTime();
            LocalTime endTime = schedule.getEndDate().toLocalTime();
            return new ReportViewVO(schedule.getId(), classDate, startTime, endTime, schedule.getMethod(), report.getClassPlace(), report.getClassSubject(), report.getClassBriefing(), report.getAbsentPerson(), schedule.getStatus(), schedule.getAdminMessage(), classPhoto.getId(), classPhoto.getFileName());
        } else return null;
    }
}
