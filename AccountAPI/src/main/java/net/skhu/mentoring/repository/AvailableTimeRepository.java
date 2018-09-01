package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.AvailableTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableTimeRepository extends JpaRepository<AvailableTime, Long> {
    List<AvailableTime> findByAccount(Account account);
}
