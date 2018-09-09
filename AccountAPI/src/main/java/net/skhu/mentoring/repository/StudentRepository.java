package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends AccountBaseRepository<Student>, JpaRepository<Student, Long> {
    Optional<Student> findByIdentity(String identity);
    boolean existsByDepartmentIdAndHasChairmanIsTrue(Long departmentId);
}
