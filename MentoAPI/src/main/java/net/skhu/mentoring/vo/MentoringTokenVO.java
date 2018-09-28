package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Team;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentoringTokenVO {
    private Long id;
    private String name;
    private String status;

    public static MentoringTokenVO builtToVO(Team team, String status){
        return new MentoringTokenVO(team.getId(), team.getName(), status);
    }
}
