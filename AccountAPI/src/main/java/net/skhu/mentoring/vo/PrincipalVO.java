package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Employee;
import net.skhu.mentoring.domain.Professor;
import net.skhu.mentoring.domain.Student;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalVO {
    private String identity;
    private String name;
    private String userType;
    private String studentStatus;
    private LocalDateTime loginTime;

    public static PrincipalVO builtToVOWithProfessor(Professor professor, LocalDateTime loginTime){
        return new PrincipalVO(professor.getIdentity(), professor.getName(), professor.getType(), "NONE", loginTime);
    }

    public static PrincipalVO builtToVOWithStudent(Student student, LocalDateTime loginTime){
        return new PrincipalVO(student.getIdentity(), student.getName(), student.getType(), student.getStatus().name(), loginTime);
    }

    public static PrincipalVO builtToVOWithEmployee(Employee employee, LocalDateTime loginTime){
        return new PrincipalVO(employee.getIdentity(), employee.getName(), employee.getType(), "NONE", loginTime);
    }
}
