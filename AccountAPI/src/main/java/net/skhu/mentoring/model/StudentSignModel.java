package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Student;
import net.skhu.mentoring.enumeration.Gender;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentSignModel {
    private String identity;
    private String password;
    private String gender;
    private long departmentId;
    private String name;
    private String phone;
    private String email;
    private int grade;
    private List<Long> multiDepartments;

    public static StudentSignModel builtToUpdateModel(Student student){
        return new StudentSignModel(student.getIdentity(), "", student.getGender(), student.getDepartment() != null ? student.getDepartment().getId() : -1, student.getName(), student.getPhone(), student.getEmail(), student.getGrade(), student.getMultiDepartments().stream().map(department -> department.getId()).collect(Collectors.toList()));
    }
}
