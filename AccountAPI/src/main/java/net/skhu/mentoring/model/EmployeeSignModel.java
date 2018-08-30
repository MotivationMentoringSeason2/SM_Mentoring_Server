package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Employee;
import net.skhu.mentoring.enumeration.Gender;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.util.Encryption;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSignModel {
    private String identity;
    private String password;
    private Gender gender;
    private String name;
    private String phone;
    private String email;
    private String officePhone;
    private String officePlace;
    private List<Long> departments;

    public static Employee builtToCreateDTO(EmployeeSignModel employeeSignModel){
        return new Employee(0L, UserType.EMPLOYEE, employeeSignModel.getGender(), null, employeeSignModel.getName(), employeeSignModel.getIdentity(), Encryption.encrypt(employeeSignModel.getPassword(), Encryption.MD5), employeeSignModel.getPhone(), employeeSignModel.getEmail(), employeeSignModel.getOfficePhone(), employeeSignModel.getOfficePlace());
    }

    public static EmployeeSignModel builtToUpdateModel(Employee employee){
        return new EmployeeSignModel(employee.getIdentity(), "", employee.getGender(), employee.getName(), employee.getPhone(), employee.getEmail(), employee.getOfficePhone(), employee.getOfficePlace(), employee.getSubDepartments().stream().map(department -> department.getId()).collect(Collectors.toList()));
    }
}
