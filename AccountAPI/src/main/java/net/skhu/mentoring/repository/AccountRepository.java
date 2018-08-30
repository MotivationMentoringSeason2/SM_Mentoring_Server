package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Account;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends AccountBaseRepository<Account> {
    Optional<Account> findByIdentity(String identity);
}
