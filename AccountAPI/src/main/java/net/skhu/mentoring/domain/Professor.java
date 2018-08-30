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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"subDepartments"})
@ToString(exclude = {"subDepartments"})
@Entity
@DiscriminatorValue(UserType.PROFESSOR)
public class Professor extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    public Professor(){
        super();
    }

    public Professor(Long id, String type, Gender gender, Department department, String name, String identity, String password, String phone, String email, String officePhone, String officePlace, Boolean hasChairman){
        super(id, type, gender, department, name, identity, password, phone, email);
        this.officePhone = officePhone;
        this.officePlace = officePlace;
        this.hasChairman = hasChairman;
        this.subDepartments = new ArrayList<Department>();
    }

    @Column(nullable = false)
    private String officePhone;

    @Column(nullable = false)
    private String officePlace;

    @Column(nullable = false)
    private Boolean hasChairman;

    @ManyToMany
    @JoinTable(name="multimajor", joinColumns=@JoinColumn(name="accountId"), inverseJoinColumns=@JoinColumn(name="departmentId"))
    private List<Department> subDepartments;
}
