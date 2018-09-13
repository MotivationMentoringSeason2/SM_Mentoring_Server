package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Calendar;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarVO {
    private String context;
    private LocalDate startDate;
    private LocalDate endDate;

    public static CalendarVO builtToVO(Calendar calendar){
        return new CalendarVO(calendar.getContext(), calendar.getStartDate(), calendar.getEndDate());
    }
}
