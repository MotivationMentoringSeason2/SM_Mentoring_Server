package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportModel {
    private Long scheduleId;
    private String classPlace;
    private String classSubject;
    private String classBriefing;
    private String absentPerson;
}
