package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Schedule;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TotalActivityVO {
    private int count;
    private long diffSeconds;

    public static TotalActivityVO builtToVO(List<Schedule> schedules){
        return new TotalActivityVO(schedules.size(),
            schedules.stream()
                .map(schedule -> {
                    LocalTime startTime = schedule.getStartDate().toLocalTime();
                    LocalTime endTime = schedule.getEndDate().toLocalTime();
                    Duration duration = Duration.between(startTime, endTime);
                    return duration.getSeconds();
                })
                .mapToLong(Long::longValue).sum()
        );
    }
}
