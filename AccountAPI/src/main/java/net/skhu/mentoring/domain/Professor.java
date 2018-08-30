package net.skhu.mentoring.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.skhu.mentoring.enumeration.Gender;
import net.skhu.mentoring.enumeration.UserType;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, exclude = {"departmentRelations"})
@ToString(exclude = {"departmentRelations"})
@Entity
@DiscriminatorValue(UserType.PROFESSOR)
public class Professor extends Account implements Serializable {
    private static final long serialVersionUID = 1L;

    public Professor(){
        super();
    }

    public Professor(Long id, String type, Gender gender, String name, String identity, String password, String phone, String email, String officePhone, String officePlace, Boolean hasChairman){
        super(id, type, gender, name, identity, password, phone, email);
        this.officePhone = officePhone;
        this.officePlace = officePlace;
        this.hasChairman = hasChairman;
        this.departmentRelations = new ArrayList<DepartmentRelation>();
    }

    @Column(nullable = false)
    private String officePhone;

    @Column(nullable = false)
    private String officePlace;

    @Column(nullable = false)
    private Boolean hasChairman;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<DepartmentRelation> departmentRelations;
}
