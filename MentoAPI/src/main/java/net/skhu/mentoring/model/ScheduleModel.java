package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleModel {
    private Long teamId;
    private LocalDate classDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String method;

    public static ScheduleModel builtToVO(Schedule schedule){
        return new ScheduleModel(schedule.getTeam().getId(), schedule.getStartDate().toLocalDate(), schedule.getStartDate().toLocalTime(), schedule.getEndDate().toLocalTime(), schedule.getMethod());
    }
}
