package net.skhu.mentoring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="\"report\"")
public class Report implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    @Column(nullable = false)
    private String classPlace;

    @Column(nullable = false)
    private String classSubject;

    @Column(nullable = false)
    private String classBriefing;

    @Column(nullable = false)
    private String absentPerson;

    @Column(nullable = false)
    private LocalDateTime presentDate;
}
