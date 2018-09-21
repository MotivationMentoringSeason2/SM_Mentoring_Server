package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.enumeration.ResultStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    List<Team> findBySemester(Semester semester);
    List<Team> findBySemesterAndStatus(Semester semester, ResultStatus status);
    Optional<Team> findByMentoAndSemester(String mento, Semester semester);
    boolean existsByMentoAndSemester(String mento, Semester semester);
    void deleteByMentoAndSemester(String mento, Semester semester);
    void deleteByMento(String mento);
}
