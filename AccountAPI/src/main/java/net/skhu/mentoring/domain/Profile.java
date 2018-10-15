package net.skhu.mentoring.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.enumeration.ImageSuffix;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="\"profile\"")
public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private Integer fileSize;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private byte[] fileData;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageSuffix fileSuffix;

    @Column(nullable = false)
    private LocalDateTime uploadDate;
}
