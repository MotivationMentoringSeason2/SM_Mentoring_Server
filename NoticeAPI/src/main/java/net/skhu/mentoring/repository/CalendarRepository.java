package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
    @Query(value = "SELECT * FROM Calendar WHERE NOW() BETWEEN startDate AND endDate", nativeQuery = true)
    Optional<Calendar> findByCurrentCalendar();
}
