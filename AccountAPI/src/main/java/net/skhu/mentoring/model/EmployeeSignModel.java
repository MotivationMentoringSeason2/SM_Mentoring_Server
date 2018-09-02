package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Employee;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSignModel {
    private String identity;
    private String password;
    private String gender;
    private String name;
    private String phone;
    private String email;
    private String officePhone;
    private String officePlace;
    private List<Long> departments;

    public static EmployeeSignModel builtToUpdateModel(Employee employee){
        return new EmployeeSignModel(employee.getIdentity(), "", employee.getGender(), employee.getName(), employee.getPhone(), employee.getEmail(), employee.getOfficePhone(), employee.getOfficePlace(), employee.getDepartments().stream().map(department -> department.getId()).collect(Collectors.toList()));
    }
}
