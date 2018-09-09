package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Professor;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorViewVO {
    private Long id;
    private String identity;
    private String name;
    private String phone;
    private String email;
    private String gender;
    private Boolean hasChairman;
    private String officePhone;
    private String officePlace;
    private String department;
    private String multiDepartments;

    public static ProfessorViewVO builtToVO(Professor professor){
        String multiDepts = professor.getMultiDepartments().size() > 0 ?
                String.join(", ", professor.getMultiDepartments().stream()
                        .map(dep -> dep.getName()).collect(Collectors.toList())) : "없음";
        return new ProfessorViewVO(professor.getId(), professor.getIdentity(), professor.getName(), professor.getPhone(), professor.getEmail(), professor.getGender(), professor.getHasChairman(), professor.getOfficePhone(), professor.getOfficePlace(), professor.getDepartment().getName(), multiDepts);
    }
}
