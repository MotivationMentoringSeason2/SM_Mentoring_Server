package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByAccount(Account account);
}
