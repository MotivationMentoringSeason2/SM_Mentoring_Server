package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.Survey;
import net.skhu.mentoring.enumeration.SurveyType;
import net.skhu.mentoring.model.SurveyModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SurveyService {
    List<Survey> fetchSurveyListByType(final SurveyType type);
    ResponseEntity<String> executeSurveyCreating(final String writer, final SurveyModel surveyModel);
    ResponseEntity<String> executeSurveyUpdating(final Long surveyId, final SurveyModel surveyModel) ;
    ResponseEntity<String> executeSurveyRemoving(final Long surveyId);
    ResponseEntity<String> executeSurveyRemovingMultiple(final List<Long> ids);
}
