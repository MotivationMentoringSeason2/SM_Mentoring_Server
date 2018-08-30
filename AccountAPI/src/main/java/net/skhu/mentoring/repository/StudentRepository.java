package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends AccountBaseRepository<Student>, JpaRepository<Student, Long> {
    List<Student> findByGrade(Integer grade);
}
