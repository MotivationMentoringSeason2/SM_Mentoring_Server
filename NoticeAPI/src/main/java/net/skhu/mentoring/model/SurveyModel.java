package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.enumeration.SurveyType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyModel {
    private String address;
    private SurveyType surveyType;
}
