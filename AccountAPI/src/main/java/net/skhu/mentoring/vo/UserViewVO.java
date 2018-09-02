package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.enumeration.UserType;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserViewVO {
    private long id;
    private String identity;
    private String name;
    private String type;
    private String departmentName;

    public static UserViewVO builtToVO(Account account){
        String tmpType = "";
        switch(account.getType()){
            case UserType.STUDENT :
                tmpType = "학생";
                break;
            case UserType.PROFESSOR :
                tmpType = "교수";
                break;
            case UserType.EMPLOYEE :
                tmpType = "직원";
                break;
        }
        Department mainDepartment = account.getDepartment();
        return new UserViewVO(account.getId(), account.getIdentity(), account.getName(), tmpType, mainDepartment != null ? mainDepartment.getName() : "-");
    }
}
