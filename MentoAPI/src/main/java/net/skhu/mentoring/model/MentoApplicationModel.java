package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Team;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentoApplicationModel {
    private String mento;
    private String teamName;
    private Integer person;
    private String advertise;
    private String qualify;
    private List<Long> subjects;

    public static MentoApplicationModel builtToVO(Team team){
        return new MentoApplicationModel(team.getMento(), team.getName(), team.getPerson(), team.getAdvertise(), team.getQualify(),
                team.getSubjects().stream()
                    .map(subject -> subject.getId())
                    .collect(Collectors.toList())
        );
    }
}
