package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Menti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MentiRepository extends JpaRepository<Menti, Long> {
}
