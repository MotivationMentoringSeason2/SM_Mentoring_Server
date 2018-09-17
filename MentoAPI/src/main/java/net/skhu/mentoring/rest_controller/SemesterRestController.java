package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.model.SubjectModel;
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

@RestController
@CrossOrigin
@RequestMapping("MentoAPI")
public class SemesterRestController {
    @Autowired
    private SemesterService semesterService;

    @GetMapping("semesters")
    public ResponseEntity<String> fetchSubjectList(){
        return ResponseEntity.ok("등록된 학기 목록을 가져옵니다.");
    }

    @GetMapping("semester/{semesterId}")
    public ResponseEntity<String> fetchSemesterById(@PathVariable Long semesterId){
        return ResponseEntity.ok("학기 목록 중 하나를 가져옵니다.");
    }

    @GetMapping("semester/current")
    public ResponseEntity<String> fetchCurrentSemester(){
        return ResponseEntity.ok("현재 해당되는 학기를 가져옵니다.");
    }

    @PostMapping("semester")
    public ResponseEntity<String> executeSemesterCreating(@RequestBody SubjectModel subjectModel){
        return ResponseEntity.ok("현재 작성한 과목을 실제로 추가합니다.");
    }

    @DeleteMapping("semester/{semesterId}")
    public ResponseEntity<String> executeSemesterRemoving(@PathVariable Long semesterId){
        return ResponseEntity.ok("관리자가 나중에 기재된 과목들 중에 필요 없는 과목을 삭제합니다.");
    }
}
