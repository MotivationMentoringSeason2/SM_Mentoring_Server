package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessorRepository extends AccountBaseRepository<Professor>, JpaRepository<Professor, Long> {

}
