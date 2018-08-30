package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AccountBaseRepository<T extends Account> extends JpaRepository<T, Long> {
}
