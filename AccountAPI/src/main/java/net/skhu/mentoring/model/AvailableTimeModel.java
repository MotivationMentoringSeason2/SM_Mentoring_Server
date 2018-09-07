package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.AvailableTime;
import net.skhu.mentoring.enumeration.Day;

import java.time.Duration;
import java.time.LocalTime;
import java.time.Period;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AvailableTimeModel implements Comparable<AvailableTimeModel> {
    private Day day;
    private LocalTime startTime;
    private LocalTime endTime;

    public static AvailableTimeModel builtToModel(AvailableTime availableTime){
        return new AvailableTimeModel(availableTime.getDay(), availableTime.getStartTime(), availableTime.getEndTime());
    }

    public boolean isValidRange(AvailableTimeModel another) {
        LocalTime anotherStartTime = another.getStartTime();
        LocalTime anotherEndTime = another.getEndTime();
        if (this.startTime.isAfter(this.endTime) || anotherStartTime.isAfter(anotherEndTime)) return false;
        if (anotherStartTime.isBefore(this.startTime) && anotherEndTime.isBefore(this.startTime)) return true;
        else if (anotherStartTime.isAfter(this.endTime) && anotherEndTime.isAfter(this.endTime)) return true;
        else return false;
    }

    public int getDayOrdinal(){
        return this.day.ordinal();
    }

    @Override
    public int compareTo(AvailableTimeModel availableTimeModel) {
        if (this.day == availableTimeModel.getDay()) {
            return this.startTime.compareTo(availableTimeModel.getStartTime());
        } else
            return this.day.ordinal() - availableTimeModel.getDay().ordinal();
    }
}
