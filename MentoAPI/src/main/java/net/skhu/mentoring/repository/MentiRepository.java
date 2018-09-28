package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Menti;
import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MentiRepository extends JpaRepository<Menti, Long> {
    List<Menti> findByTeam(Team team);
    List<Menti> findByUserIdOrderByIdDesc(String userId);
    Optional<Menti> findByTeamSemesterAndUserId(Semester semester, String userId);
    boolean existsByUserIdAndTeam(String userId, Team team);
    boolean existsByUserIdAndTeamSemester(String userId, Semester semester);
    long countByTeam(Team team);
    void deleteByUserId(String userId);
    void deleteByUserIdAndTeam(String userId, Team team);
}
