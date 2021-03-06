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
    List<Team> findByMentoOrderByIdDesc(String mento);
    List<Team> findBySemesterAndStatus(Semester semester, ResultStatus status);
    Optional<Team> findByIdAndSemester(Long id, Semester semester);
    Optional<Team> findBySemesterAndStatusAndMento(Semester semester, ResultStatus status, String mento);
    Optional<Team> findByMentoAndSemester(String mento, Semester semester);
    Optional<Team> findByMento(String mento);
    boolean existsByMentoAndSemester(String mento, Semester semester);
    void deleteByMentoAndSemester(String mento, Semester semester);
    void deleteByMento(String mento);
}
