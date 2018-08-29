package net.skhu.mentoring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.skhu.mentoring.enumeration.Gender;
import net.skhu.mentoring.enumeration.UserType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@DiscriminatorValue(UserType.PROFESSOR)
public class Professor extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    public Professor(){
        super();
    }

    public Professor(Long id, Gender gender, String name, String identity, String password, String phone, String email, String officePhone, String officePlace, Boolean hasChairman){
        super(id, gender, name, identity, password, phone, email);
        this.officePhone = officePhone;
        this.officePlace = officePlace;
        this.hasChairman = hasChairman;
    }

    @Column(nullable = false)
    private String officePhone;

    @Column(nullable = false)
    private String officePlace;

    @Column(nullable = false)
    private Boolean hasChairman;
}
