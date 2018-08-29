package net.skhu.mentoring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.skhu.mentoring.enumeration.Gender;
import net.skhu.mentoring.enumeration.StudentType;
import net.skhu.mentoring.enumeration.UserType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(UserType.STUDENT)
public class Student extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    public Student(){
        super();
    }

    public Student(Long id, Gender gender, String name, String identity, String password, String phone, String email, Integer grade, StudentType type, Boolean hasChairman){
        super(id, gender, name, identity, password, phone, email);
        this.grade = grade;
        this.type = type;
        this.hasChairman = hasChairman;
    }

    @Column(nullable = false)
    private Integer grade;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private StudentType type;

    @Column(nullable = false)
    private Boolean hasChairman;
}
