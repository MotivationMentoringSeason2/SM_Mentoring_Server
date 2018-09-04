package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.enumeration.Day;

import java.time.LocalTime;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeModel implements Comparable<AvailableTimeModel> {
    private Day day;
    private LocalTime startTime;
    private LocalTime endTime;

    public boolean isNotVaildRange(AvailableTimeModel another) {
        LocalTime anotherStartTime = another.getStartTime();
        LocalTime anotherEndTime = another.getEndTime();
        if (anotherStartTime.compareTo(anotherEndTime) >= 0) return false;
        if (anotherStartTime.compareTo(this.startTime) < 0 && anotherEndTime.compareTo(this.startTime) < 0) return true;
        else if (anotherStartTime.compareTo(this.endTime) > 0 && anotherEndTime.compareTo(this.endTime) > 0)
            return true;
        else return false;
    }

    @Override
    public int compareTo(AvailableTimeModel availableTimeModel) {
        if (this.day == availableTimeModel.getDay()) {
            return this.startTime.compareTo(availableTimeModel.getStartTime());
        } else
            return this.day.ordinal() - availableTimeModel.getDay().ordinal();
    }
}
