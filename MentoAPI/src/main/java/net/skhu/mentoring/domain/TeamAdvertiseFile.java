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
@Table(name="\"teamadvertisefile\"")
public class TeamAdvertiseFile implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "teamId")
    private Team team;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Long fileSize;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] fileData;

    @Column(nullable = false)
    private String fileSuffix;

    @Column(nullable = false)
    private LocalDateTime uploadDate;
}
