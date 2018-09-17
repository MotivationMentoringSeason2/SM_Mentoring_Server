package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.TeamAdvertiseFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamAdvertiseRepository extends JpaRepository<TeamAdvertiseFile, Long> {
}
