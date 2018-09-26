package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.domain.Subject;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.enumeration.ResultStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CareerBriefVO {
    private Long id;
    private Semester semester;
    private String name;
    private String mento;
    private Integer appPerson;
    private Integer limPerson;
    private List<Subject> subjects;
    private ResultStatus status;

    public static CareerBriefVO builtToVO(Team team, int appPerson){
        return new CareerBriefVO(team.getId(), team.getSemester(), team.getName(), team.getMento(), appPerson, team.getPerson(), team.getSubjects(), team.getStatus());
    }
}
