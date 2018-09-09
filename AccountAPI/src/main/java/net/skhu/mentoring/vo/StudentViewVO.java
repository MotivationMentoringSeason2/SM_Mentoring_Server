package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Student;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentViewVO {
    private Long id;
    private String identity;
    private String name;
    private String phone;
    private String email;
    private String gender;
    private Integer grade;
    private Boolean hasChairman;
    private String status;
    private String department;
    private String multiDepartments;

    public static StudentViewVO builtToVO(Student student){
        String multiDepts = student.getMultiDepartments().size() > 0 ?
                String.join(", ", student.getMultiDepartments().stream()
                                    .map(dep -> dep.getName()).collect(Collectors.toList())) : "없음";
        String stdType = "";
        switch(student.getStatus()){
            case NORMAL :
                stdType = student.getHasChairman() ? "학생회장" : "일반학생";
                break;
            case MENTI :
                stdType = student.getHasChairman() ? "학생회장 + 멘티" : "멘티학생";
                break;
            case MENTO :
                stdType = student.getHasChairman() ? "학생회장 + 멘토" : "멘토학생";
                break;
        }
        return new StudentViewVO(student.getId(), student.getIdentity(), student.getName(), student.getPhone(), student.getEmail(), student.getGender(), student.getGrade(), student.getHasChairman(), stdType, student.getDepartment().getName(), multiDepts);
    }
}
