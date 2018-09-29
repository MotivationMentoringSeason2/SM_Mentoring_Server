package net.skhu.mentoring.service.integrate_objects;

import net.skhu.mentoring.domain.Calendar;
import net.skhu.mentoring.model.CalendarModel;
import net.skhu.mentoring.repository.CalendarRepository;
import net.skhu.mentoring.service.interfaces.CalendarService;
import net.skhu.mentoring.vo.CalendarVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService {
    @Autowired
    private CalendarRepository calendarRepository;

    @Override
    public List<CalendarVO> fetchCalendarSchedules() {
        return calendarRepository.findAll().stream()
                .map(calendar -> CalendarVO.builtToVO(calendar))
                .collect(Collectors.toList());
    }

    @Override
    public CalendarVO fetchCurrentSchedule() {
        Optional<Calendar> currentCalendar = calendarRepository.findByCurrentCalendar();
        return currentCalendar.isPresent() ? CalendarVO.builtToVO(currentCalendar.get()) : null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeUpdatingCalendarSchedule(final String userId, final CalendarModel calendarModel) {
        Optional<Calendar> calendar = calendarRepository.findById(calendarModel.getId());
        if(calendar.isPresent()){
            Calendar updateCalendar = calendar.get();
            updateCalendar.setWriter(userId);
            updateCalendar.setStartDate(calendarModel.getStartDate());
            updateCalendar.setEndDate(calendarModel.getEndDate());
            calendarRepository.save(updateCalendar);
            return ResponseEntity.ok(String.format("%s 일정 수정 작업이 완료 되었습니다.", updateCalendar.getContext()));
        } else
            return new ResponseEntity<>("선택하신 일정이 존재하지 않아 수정되지 않았습니다.", HttpStatus.NOT_MODIFIED);
    }
}
