package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends AccountBaseRepository<Professor>, JpaRepository<Professor, Long> {
    Optional<Professor> findByIdentity(String identity);
    boolean existsByDepartmentIdAndHasChairmanIsTrue(Long departmentId);
}
