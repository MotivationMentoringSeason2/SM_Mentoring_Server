package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Menti;
import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentiRepository extends JpaRepository<Menti, Long> {
    List<Menti> findByTeam(Team team);
    boolean existsByUserIdAndTeam(String userId, Team team);
    boolean existsByUserIdAndTeamSemester(String userId, Semester semester);
    long countByTeam(Team team);
    void deleteByUserId(String userId);
    void deleteByUserIdAndTeam(String userId, Team team);
}
