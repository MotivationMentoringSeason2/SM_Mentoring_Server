package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Employee;

import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeViewVO {
    private Long id;
    private String identity;
    private String name;
    private String phone;
    private String email;
    private String gender;
    private String officePhone;
    private String officePlace;
    private String departments;

    public static EmployeeViewVO builtToVO(Employee employee) {
        String departments = employee.getDepartments().size() > 0 ?
                String.join(", ", employee.getDepartments().stream()
                        .map(dep -> dep.getName()).collect(Collectors.toList())) : "없음";
        return new EmployeeViewVO(employee.getId(), employee.getIdentity(), employee.getName(), employee.getPhone(), employee.getEmail(), employee.getGender(), employee.getOfficePhone(), employee.getOfficePlace(), departments);
    }
}
