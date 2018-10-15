package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="\"semester\"")
public class SemesterModel {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
