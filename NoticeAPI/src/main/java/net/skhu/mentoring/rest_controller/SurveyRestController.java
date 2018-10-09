package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.domain.Survey;
import net.skhu.mentoring.enumeration.SurveyType;
import net.skhu.mentoring.model.SurveyModel;
import net.skhu.mentoring.service.interfaces.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://182.209.240.203:81"})
@RequestMapping("/NoticeAPI")
public class SurveyRestController {
    @Autowired
    private SurveyService surveyService;

    @GetMapping("surveys/{type}")
    public ResponseEntity<?> fetchSurveyListByType(@PathVariable SurveyType type){
        return ResponseEntity.ok(surveyService.fetchSurveyListByType(type));
    }

    @GetMapping("survey/{surveyId}")
    public ResponseEntity<?> fetchSurveyById(@PathVariable long surveyId){
        Survey survey = surveyService.fetchSurveyById(surveyId);
        if(survey == null) return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(survey);
    }

    @PostMapping("survey/{userId}")
    public ResponseEntity<String> executeSurveyCreating(@PathVariable String userId, @RequestBody SurveyModel surveyModel){
        return surveyService.executeSurveyCreating(userId, surveyModel);
    }

    @PutMapping("survey/{surveyId}")
    public ResponseEntity<String> executeSurveyUpdating(@PathVariable Long surveyId, @RequestBody SurveyModel surveyModel){
        return surveyService.executeSurveyUpdating(surveyId, surveyModel);
    }

    @DeleteMapping("survey/{surveyId}")
    public ResponseEntity<String> executeSurveyRemoving(@PathVariable Long surveyId){
        return surveyService.executeSurveyRemoving(surveyId);
    }

    @DeleteMapping("surveys")
    public ResponseEntity<String> executeSurveyRemovingMultiple(@RequestBody List<Long> ids){
        return surveyService.executeSurveyRemovingMultiple(ids);
    }
}
