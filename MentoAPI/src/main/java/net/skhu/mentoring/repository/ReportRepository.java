package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Report;
import net.skhu.mentoring.domain.Schedule;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.enumeration.ResultStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findBySchedule(Schedule schedule);
    List<Report> findByScheduleTeam(Team team);
    List<Report> findByScheduleStatusAndScheduleTeam(ResultStatus status, Team team);
    boolean existsByIdIn(List<Long> id);
    boolean existsByScheduleTeam(Team team);
    void deleteByIdIn(List<Long> id);
    void deleteByScheduleTeam(Team team);
}
