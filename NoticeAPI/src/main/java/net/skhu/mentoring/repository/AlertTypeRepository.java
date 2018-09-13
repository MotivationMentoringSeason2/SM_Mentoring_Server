package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.AlertType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertTypeRepository extends JpaRepository<AlertType, Long> {
}
