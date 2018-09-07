package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.AvailableTime;
import net.skhu.mentoring.enumeration.Day;

import java.time.LocalTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeVO {
    private Day day;
    private LocalTime startTime;
    private LocalTime endTime;

    public static AvailableTimeVO builtToVO(AvailableTime availableTime){
        return new AvailableTimeVO(availableTime.getDay(), availableTime.getStartTime(), availableTime.getEndTime());
    }

    public int getDayOrdinal(){
        return this.day.ordinal();
    }
}
