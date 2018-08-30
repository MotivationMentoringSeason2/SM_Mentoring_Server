package net.skhu.mentoring.component;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.Employee;
import net.skhu.mentoring.domain.Professor;
import net.skhu.mentoring.domain.Student;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.repository.EmployeeRepository;
import net.skhu.mentoring.repository.ProfessorRepository;
import net.skhu.mentoring.repository.StudentRepository;
import net.skhu.mentoring.vo.PrincipalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PrincipalGenerator {
    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StudentRepository studentRepository;

    public String fetchRoleWithAccount(final Account account){
        boolean isAdmin = false;

        switch(account.getType()){
            case UserType.PROFESSOR :
            case UserType.EMPLOYEE :
                isAdmin = true;
                break;
            case UserType.STUDENT :
                Optional<Student> student = studentRepository.findByIdentity(account.getIdentity());
                if(!student.isPresent()) break;
                else {
                    Student tmpStudent = student.get();
                    isAdmin = tmpStudent.getHasChairman() ? true : false;
                }
        }

        return isAdmin ? "ROLE_ADMIN" : "ROLE_USER";
    }

    public PrincipalVO fetchPrincipalVOWithAccount(final Account account){
        if(account == null) return null;
        switch(account.getType()){
            case UserType.EMPLOYEE :
                Optional<Employee> employee = employeeRepository.findByIdentity(account.getIdentity());
                return PrincipalVO.builtToVOWithEmployee(employee.get(), LocalDateTime.now());
            case UserType.PROFESSOR :
                Optional<Professor> professor = professorRepository.findByIdentity(account.getIdentity());
                return PrincipalVO.builtToVOWithProfessor(professor.get(), LocalDateTime.now());
            case UserType.STUDENT :
                Optional<Student> student = studentRepository.findByIdentity(account.getIdentity());
                return PrincipalVO.builtToVOWithStudent(student.get(), LocalDateTime.now());
        }
        return null;
    }
}
