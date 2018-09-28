package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Schedule;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.enumeration.ResultStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByTeamAndStatus(Team team, ResultStatus status);
    boolean existsByIdIn(List<Long> id);
    boolean existsByTeam(Team team);
    void deleteByIdIn(List<Long> id);
    void deleteByTeam(Team team);
}
