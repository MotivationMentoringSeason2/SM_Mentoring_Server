package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Professor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorSignModel {
    private String identity;
    private String password;
    private String gender;
    private long departmentId;
    private String name;
    private String phone;
    private String email;
    private String officePhone;
    private String officePlace;
    private Boolean hasChairman;
    private List<Long> multiDepartments;

    public static ProfessorSignModel builtToUpdateModel(Professor professor){
        return new ProfessorSignModel(professor.getIdentity(), "", professor.getGender(), professor.getDepartment() != null ? professor.getDepartment().getId() : -1, professor.getName(), professor.getPhone(), professor.getEmail(), professor.getOfficePhone(), professor.getOfficePlace(), professor.getHasChairman(), professor.getMultiDepartments().stream().map(department -> department.getId()).collect(Collectors.toList()));
    }
}
