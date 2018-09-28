package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.ClassPhoto;
import net.skhu.mentoring.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassPhotoRepository extends JpaRepository<ClassPhoto, Long> {
    Optional<ClassPhoto> findByReport(Report report);
}
