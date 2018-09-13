package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.CalendarModel;
import net.skhu.mentoring.vo.CalendarVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CalendarService {
    List<CalendarVO> fetchCalendarSchedules();
    ResponseEntity<String> executeUpdatingCalendarSchedule(final String userId, final CalendarModel calendarModel);
}
