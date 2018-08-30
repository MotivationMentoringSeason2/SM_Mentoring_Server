package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.DepartmentRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRelationRepository extends JpaRepository<DepartmentRelation, Long> {
    List<DepartmentRelation> findByAccount(Account account);
    List<DepartmentRelation> findByAccountAndHasMainIsTrue(Account account);
    List<DepartmentRelation> findByAccountAndHasMainIsFalse(Account account);
}