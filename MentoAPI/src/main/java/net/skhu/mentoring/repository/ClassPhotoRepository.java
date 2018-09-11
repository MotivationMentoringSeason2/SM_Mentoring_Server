package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.ClassPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClassPhotoRepository extends JpaRepository<ClassPhoto , Long>{
}
