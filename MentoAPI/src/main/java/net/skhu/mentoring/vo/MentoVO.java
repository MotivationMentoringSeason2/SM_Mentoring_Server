package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Subject;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.domain.TeamAdvertiseFile;
import net.skhu.mentoring.enumeration.ResultStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentoVO {
    private Long id;
    private String name;
    private String mento;
    private Integer person;
    private List<Subject> subjects;
    private String advertise;
    private String qualify;

    private Long advFileId;
    private String advFileName;
    private Long advFileSize;

    private ResultStatus status;

    public static MentoVO builtToVO(Team team, TeamAdvertiseFile advFile){
        return new MentoVO(team.getId(), team.getName(), team.getMento(), team.getPerson(), team.getSubjects(), team.getAdvertise(), team.getQualify(), advFile != null ? advFile.getId() : null, advFile != null ? advFile.getFileName() : null, advFile != null ? advFile.getFileSize() : null, team.getStatus());
    }
}
