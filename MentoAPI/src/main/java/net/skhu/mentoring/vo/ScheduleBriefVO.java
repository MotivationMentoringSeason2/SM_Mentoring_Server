package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Schedule;
import net.skhu.mentoring.enumeration.ResultStatus;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleBriefVO {
    private Long id;
    private String classType;
    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private ResultStatus status;

    public static ScheduleBriefVO builtToVO(Schedule schedule){
        LocalDate classDate = schedule.getStartDate().toLocalDate();
        LocalTime startTime = schedule.getStartDate().toLocalTime();
        LocalTime endTime = schedule.getEndDate().toLocalTime();
        return new ScheduleBriefVO(schedule.getId(), schedule.getMethod(), classDate, startTime, endTime, schedule.getStatus());
    }
}
