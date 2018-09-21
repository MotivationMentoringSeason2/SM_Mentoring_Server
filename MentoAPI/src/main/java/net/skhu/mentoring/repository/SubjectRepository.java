package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findByName(String name);
    boolean existsByIdIn(List<Long> ids);
    void deleteByIdIn(List<Long> ids);
}
