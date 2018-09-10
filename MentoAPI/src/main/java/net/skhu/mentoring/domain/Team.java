package net.skhu.mentoring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.enumeration.ResultStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Team implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "semesterId")
    private Semester semester;

    @Column(nullable = false)
    private String mento;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer person;

    @Column(nullable = false)
    private String advertise;

    @Column(nullable = false)
    private String qualify;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResultStatus status;

    @ManyToMany
    @JoinTable(name = "subjectandteam", joinColumns = @JoinColumn(name = "teamId"), inverseJoinColumns = @JoinColumn(name = "subjectId"))
    private Subject subject;
}
