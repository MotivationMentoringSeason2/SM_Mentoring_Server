package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.StickyPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickyPhotoRepository extends JpaRepository<StickyPhoto, Long> {
}
