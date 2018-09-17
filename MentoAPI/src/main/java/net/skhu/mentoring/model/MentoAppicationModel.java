package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.enumeration.ResultStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MentoAppicationModel {
    private Long semesterId;
    private String mento;
    private String teamName;
    private Integer person;
    private String advertise;
    private String qualify;
    private ResultStatus status;
    private List<Long> subjects;
}
