package net.skhu.mentoring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.skhu.mentoring.enumeration.Gender;
import net.skhu.mentoring.enumeration.StudentStatus;
import net.skhu.mentoring.enumeration.UserType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@DiscriminatorValue(UserType.STUDENT)
public class Student extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    public Student(){
        super();
    }

    public Student(Long id, String type, Gender gender, Department department, String name, String identity, String password, String phone, String email, Integer grade, StudentStatus status, Boolean hasChairman){
        super(id, type, gender, department, name, identity, password, phone, email);
        this.grade = grade;
        this.status = status;
        this.hasChairman = hasChairman;
        this.subDepartments = new ArrayList<Department>();
    }

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StudentStatus status;

    @Column(nullable = false)
    private Boolean hasChairman;

    @ManyToMany
    @JoinTable(name="multimajor", joinColumns=@JoinColumn(name="accountId"), inverseJoinColumns=@JoinColumn(name="departmentId"))
    private List<Department> subDepartments;
}
