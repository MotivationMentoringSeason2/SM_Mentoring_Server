package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.AvailableTime;
import net.skhu.mentoring.enumeration.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailableTimeRepository extends JpaRepository<AvailableTime, Long> {
    List<AvailableTime> findByAccount(Account account);
    List<AvailableTime> findByAccountAndDay(Account account, Day day);
    boolean existsByAccount(Account account);
    void deleteByAccount(Account account);
}
