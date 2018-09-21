package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Subject;
import net.skhu.mentoring.domain.Team;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentiAppVO {
    private Long id;
    private String name;
    private String mento;
    private Integer appPerson;
    private Integer limPerson;
    private List<Subject> subjects;
    private Boolean hasApplicated;

    public static MentiAppVO builtToVO(Team team, int appPerson, boolean hasApplicated){
        return new MentiAppVO(team.getId(), team.getName(), team.getMento(), appPerson, team.getPerson(), team.getSubjects(), hasApplicated);
    }
}
