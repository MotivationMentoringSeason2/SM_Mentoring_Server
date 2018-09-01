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
@EqualsAndHashCode(callSuper = true, exclude = {"subDepartments"})
@ToString(exclude = {"subDepartments"})
@Entity
@Table(name = "professor")
@DiscriminatorValue(UserType.PROFESSOR)
public class Professor extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    public Professor(){
        super();
    }

    public Professor(Long id, String type, String gender, Department department, String name, String identity, String password, String phone, String email, String officePhone, String officePlace, Boolean hasChairman){
        super(id, type, gender, department, name, identity, password, phone, email);
        this.officePhone = officePhone;
        this.officePlace = officePlace;
        this.hasChairman = hasChairman;
        this.multiDepartments = new ArrayList<Department>();
    }

    public Professor(Long id, String type, String gender, Department department, String name, String identity, String password, String phone, String email, String officePhone, String officePlace, Boolean hasChairman, List<Department> multiDepartments){
        this(id, type, gender, department, name, identity, password, phone, email, officePhone, officePlace, hasChairman);
        this.multiDepartments = multiDepartments;
    }

    @Column(nullable = false)
    private String officePhone;

    @Column(nullable = false)
    private String officePlace;

    @Column(nullable = false)
    private Boolean hasChairman;

    @ManyToMany
    @JoinTable(name="multimajor", joinColumns=@JoinColumn(name="accountId"), inverseJoinColumns=@JoinColumn(name="departmentId"))
    private List<Department> multiDepartments;
}
