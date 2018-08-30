package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends AccountBaseRepository<Employee>, JpaRepository<Employee, Long> {

}
