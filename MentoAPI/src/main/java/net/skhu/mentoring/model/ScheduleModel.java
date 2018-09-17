package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleModel {
    private Long teamId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String method;
}
