package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Professor;
import net.skhu.mentoring.enumeration.Gender;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.util.Encryption;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorSignModel {
    private String identity;
    private String password;
    private Gender gender;
    private long departmentId;
    private String name;
    private String phone;
    private String email;
    private String officePhone;
    private String officePlace;
    private Boolean hasChairman;
    private List<Long> subDepartments;

    public static Professor builtToCreateDTO(ProfessorSignModel professorSignModel){
        return new Professor(0L, UserType.PROFESSOR, professorSignModel.getGender(), new Department(professorSignModel.getDepartmentId(), null), professorSignModel.getName(), professorSignModel.getIdentity(), Encryption.encrypt(professorSignModel.getPassword(), Encryption.MD5), professorSignModel.getPhone(), professorSignModel.getEmail(), professorSignModel.getOfficePhone(), professorSignModel.getOfficePlace(), professorSignModel.hasChairman);
    }

    public static ProfessorSignModel builtToUpdateModel(Professor professor){
        return new ProfessorSignModel(professor.getIdentity(), "", professor.getGender(), professor.getDepartment() != null ? professor.getDepartment().getId() : -1, professor.getName(), professor.getPhone(), professor.getEmail(), professor.getOfficePhone(), professor.getOfficePlace(), professor.getHasChairman(), professor.getSubDepartments().stream().map(department -> department.getId()).collect(Collectors.toList()));
    }
}
