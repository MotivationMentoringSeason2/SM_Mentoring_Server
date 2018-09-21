package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Menti;
import net.skhu.mentoring.domain.Team;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonVO {
    private String mento;
    private List<String> mentis;

    public static PersonVO builtToVO(Team team, List<Menti> mentis){
        return new PersonVO(team.getMento(), mentis.stream()
                .map(menti -> menti.getUserId())
                .collect(Collectors.toList()));
    }
}
