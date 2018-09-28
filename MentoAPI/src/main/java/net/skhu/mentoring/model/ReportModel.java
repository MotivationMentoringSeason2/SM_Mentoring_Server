package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Report;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportModel {
    private String classPlace;
    private String classSubject;
    private String classBriefing;
    private String absentPerson;

    public static ReportModel builtToModel(Report report){
        return new ReportModel(report.getClassPlace(), report.getClassSubject(), report.getClassBriefing(), report.getAbsentPerson());
    }
}
