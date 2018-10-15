package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    @Query(value = "SELECT * FROM semester WHERE NOW() BETWEEN startDate AND endDate", nativeQuery = true)
    Optional<Semester> findByCurrentSemester();
    Optional<Semester> findByName(String name);
}
