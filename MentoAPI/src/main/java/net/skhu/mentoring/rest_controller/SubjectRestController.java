package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.domain.Subject;
import net.skhu.mentoring.model.SubjectModel;
import net.skhu.mentoring.service.interfaces.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("MentoAPI")
public class SubjectRestController {
    @Autowired
    private SubjectService subjectService;

    @GetMapping("subjects")
    public ResponseEntity<List<Subject>> fetchSubjectList(){
        return ResponseEntity.ok(subjectService.fetchAllSubjects());
    }

    @PostMapping("subject")
    public ResponseEntity<String> executeSubjectCreating(@RequestBody SubjectModel subjectModel){
        return subjectService.executeCreateSubject(subjectModel);
    }

    @DeleteMapping("subjects")
    public ResponseEntity<String> executeSubjectRemovingMultiple(@RequestBody List<Long> ids){
        return subjectService.executeRemoveSubject(ids);
    }
}
