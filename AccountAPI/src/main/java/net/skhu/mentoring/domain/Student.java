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
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"departmentRelations"})
@ToString(exclude = {"departmentRelations"})
@Entity
@DiscriminatorValue(UserType.STUDENT)
public class Student extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    public Student(){
        super();
    }

    public Student(Long id, String type, Gender gender, String name, String identity, String password, String phone, String email, Integer grade, StudentStatus status, Boolean hasChairman){
        super(id, type, gender, name, identity, password, phone, email);
        this.grade = grade;
        this.status = status;
        this.hasChairman = hasChairman;
        this.departmentRelations = new ArrayList<DepartmentRelation>();
    }

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StudentStatus status;

    @Column(nullable = false)
    private Boolean hasChairman;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<DepartmentRelation> departmentRelations;
}
