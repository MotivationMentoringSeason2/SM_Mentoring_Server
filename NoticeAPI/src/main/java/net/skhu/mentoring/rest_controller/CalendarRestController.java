package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.model.CalendarModel;
import net.skhu.mentoring.service.interfaces.CalendarService;
import net.skhu.mentoring.vo.CalendarVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/NoticeAPI/calendar")
public class CalendarRestController {
    @Autowired
    private CalendarService calendarService;

    @GetMapping("view")
    public ResponseEntity<List<CalendarVO>> fetchCalendarScheduleView(){
        return ResponseEntity.ok(calendarService.fetchCalendarSchedules());
    }

    @PutMapping("update/{userId}")
    public ResponseEntity<String> executeUpdateCalendarSchedule(@PathVariable String userId, @RequestBody CalendarModel calendarModel){
        return calendarService.executeUpdatingCalendarSchedule(userId, calendarModel);
    }
}
