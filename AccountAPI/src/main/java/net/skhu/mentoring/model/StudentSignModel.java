package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Student;
import net.skhu.mentoring.enumeration.Gender;
import net.skhu.mentoring.enumeration.StudentStatus;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.util.Encryption;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSignModel {
    private String identity;
    private String password;
    private Gender gender;
    private long departmentId;
    private String name;
    private String phone;
    private String email;
    private int grade;
    private Boolean hasChairman;
    private List<Long> subDepartments;

    public static Student builtToCreateDTO(StudentSignModel studentSignModel){
        return new Student(0L, UserType.PROFESSOR, studentSignModel.getGender(), new Department(studentSignModel.getDepartmentId(), null), studentSignModel.getName(), studentSignModel.getIdentity(), Encryption.encrypt(studentSignModel.getPassword(), Encryption.MD5), studentSignModel.getPhone(), studentSignModel.getEmail(), studentSignModel.getGrade(), StudentStatus.NORMAL, studentSignModel.getHasChairman());
    }

    public static StudentSignModel builtToUpdateModel(Student student){
        return new StudentSignModel(student.getIdentity(), "", student.getGender(), student.getDepartment() != null ? student.getDepartment().getId() : -1, student.getName(), student.getPhone(), student.getEmail(), student.getGrade(), student.getHasChairman(), student.getSubDepartments().stream().map(department -> department.getId()).collect(Collectors.toList()));
    }
}
