package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.model.SemesterModel;
import net.skhu.mentoring.service.interfaces.SemesterService;
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
public class SemesterRestController {
    @Autowired
    private SemesterService semesterService;

    @GetMapping("semesters")
    public ResponseEntity<List<Semester>> fetchSubjectList(){
        return ResponseEntity.ok(semesterService.fetchAllSemesterList());
    }

    @GetMapping("semester/{semesterId}")
    public ResponseEntity<?> fetchSemesterById(@PathVariable Long semesterId){
        Semester semester = semesterService.fetchSemesterById(semesterId);
        return semester != null ? ResponseEntity.ok(semester) : ResponseEntity.noContent().build();
    }

    @GetMapping("semester/current")
    public ResponseEntity<?> fetchCurrentSemester(){
        Semester semester = semesterService.fetchCurrentSemester();
        return semester != null ? ResponseEntity.ok(semester) : ResponseEntity.noContent().build();
    }

    @PostMapping("semester")
    public ResponseEntity<String> executeSemesterCreating(@RequestBody SemesterModel semesterModel){
        return semesterService.executeCreateSemester(semesterModel);
    }

    @DeleteMapping("semester/{semesterId}")
    public ResponseEntity<String> executeSemesterRemoving(@PathVariable Long semesterId){
        return semesterService.executeRemoveSemester(semesterId);
    }
}
