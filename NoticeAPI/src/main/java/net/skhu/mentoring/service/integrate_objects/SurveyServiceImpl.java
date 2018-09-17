package net.skhu.mentoring.service.integrate_objects;

import net.skhu.mentoring.domain.Survey;
import net.skhu.mentoring.enumeration.SurveyType;
import net.skhu.mentoring.model.SurveyModel;
import net.skhu.mentoring.repository.SurveyRepository;
import net.skhu.mentoring.service.interfaces.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SurveyServiceImpl implements SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;

    @Override
    public List<Survey> fetchSurveyListByType(final SurveyType type) {
        List<SurveyType> surveyTypes = Arrays.asList(SurveyType.ALL, type);
        return surveyRepository.findByTypeIn(surveyTypes);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeSurveyCreating(final String writer, final SurveyModel surveyModel) {
        Survey createSurvey = new Survey();
        createSurvey.setId(0L);
        createSurvey.setWriter(writer);
        createSurvey.setAddress(surveyModel.getAddress());
        createSurvey.setType(surveyModel.getSurveyType());
        createSurvey.setWrittenDate(LocalDateTime.now());
        surveyRepository.save(createSurvey);
        return ResponseEntity.ok("설문조사 사이트가 추가 되었습니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeSurveyUpdating(final Long surveyId, final SurveyModel surveyModel) {
        Optional<Survey> survey = surveyRepository.findById(surveyId);
        if(survey.isPresent()){
            Survey updateSurvey = survey.get();
            updateSurvey.setType(surveyModel.getSurveyType());
            updateSurvey.setAddress(surveyModel.getAddress());
            updateSurvey.setWrittenDate(LocalDateTime.now());
            surveyRepository.save(updateSurvey);
            return ResponseEntity.ok("설문조사 사이트 수정 작업이 완료 되었습니다.");
        } else return new ResponseEntity<>("설문조사 영역이 존재하지 않아 수정 작업이 이뤄지지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeSurveyRemoving(final Long surveyId) {
        if(surveyRepository.existsById(surveyId)){
            surveyRepository.deleteById(surveyId);
            return ResponseEntity.ok("설문조사 사이트 삭제 작업이 완료 되었습니다.");
        } else return new ResponseEntity<>("설문조사 영역이 존재하지 않아 삭제 작업이 이뤄지지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeSurveyRemovingMultiple(final List<Long> ids) {
        surveyRepository.deleteByIdIn(ids);
        return ResponseEntity.ok("선택하신 설문조사 사이트 삭제 작업이 완료 되었습니다.");
    }
}
