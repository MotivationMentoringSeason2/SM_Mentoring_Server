package net.skhu.mentoring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.skhu.mentoring.enumeration.Gender;
import net.skhu.mentoring.enumeration.UserType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"departments"})
@ToString(exclude = {"departments"})
@Entity
@Table(name = "employee")
@DiscriminatorValue(UserType.EMPLOYEE)
public class Employee extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    public Employee() {
        super();
    }

    public Employee(Long id, String type, String gender, Department department, String name, String identity, String password, String phone, String email, String officePhone, String officePlace) {
        super(id, type, gender, department, name, identity, password, phone, email);
        this.officePhone = officePhone;
        this.officePlace = officePlace;
        this.departments = new ArrayList<Department>();
    }

    public Employee(Long id, String type, String gender, Department department, String name, String identity, String password, String phone, String email, String officePhone, String officePlace, List<Department> departments) {
        this(id, type, gender, department, name, identity, password, phone, email, officePhone, officePlace);
        this.departments = departments;
    }

    @Column(nullable = false)
    private String officePhone;

    @Column(nullable = false)
    private String officePlace;

    @ManyToMany
    @JoinTable(name = "multimajor", joinColumns = @JoinColumn(name = "accountId"), inverseJoinColumns = @JoinColumn(name = "departmentId"))
    private List<Department> departments;
}
