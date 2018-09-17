package net.skhu.mentoring.rest_controller;

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
    public ResponseEntity<String> fetchSubjectList(){
        return ResponseEntity.ok("현재 등록된 과목 목록을 불러옵니다.");
    }

    @GetMapping("subject/{subjectId}")
    public ResponseEntity<String> fetchSubjectById(@PathVariable Long subjectId){
        return ResponseEntity.ok("현재 등록된 과목 중 하나를 불러옵니다.");
    }

    @PostMapping("subject")
    public ResponseEntity<String> executeSubjectCreating(@RequestBody SubjectModel subjectModel){
        return ResponseEntity.ok("현재 작성한 과목을 실제로 추가합니다.");
    }

    @DeleteMapping("subject/{subjectId}")
    public ResponseEntity<String> executeSubjectRemoving(@PathVariable Long subjectId){
        return ResponseEntity.ok("관리자가 나중에 기재된 과목들 중에 필요 없는 과목을 삭제합니다.");
    }

    @DeleteMapping("subjects")
    public ResponseEntity<String> executeSubjectRemovingMultiple(@RequestBody List<Long> ids){
        return ResponseEntity.ok("관리자가 여러 과목들을 삭제합니다.");
    }
}
