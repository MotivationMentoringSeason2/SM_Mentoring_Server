package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.model.SemesterModel;
import net.skhu.mentoring.repository.SemesterRepository;
import net.skhu.mentoring.service.interfaces.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SemesterServiceImpl implements SemesterService {
    @Autowired
    private SemesterRepository semesterRepository;

    @Override
    public List<Semester> fetchAllSemesterList() {
        return semesterRepository.findAll();
    }

    @Override
    public Semester fetchSemesterById(final Long semesterId) {
        Optional<Semester> semester = semesterRepository.findById(semesterId);
        if(semester.isPresent()) return semester.get();
        else return null;
    }

    @Override
    public Semester fetchCurrentSemester() {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if(semester.isPresent()) return semester.get();
        else return null;
    }

    @Override
    public ResponseEntity<String> executeCreateSemester(final SemesterModel semesterModel) {
        Optional<Semester> semester = semesterRepository.findByName(semesterModel.getName());
        if(!semester.isPresent()){
            Semester createSemester = new Semester(0L, semesterModel.getName(), semesterModel.getStartDate(), semesterModel.getEndDate());
            semesterRepository.save(createSemester);
            return ResponseEntity.ok(String.format("%s 가 추가 되었습니다.", semesterModel.getName()));
        } else return new ResponseEntity<>("입력하신 학기는 이미 존재합니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    public ResponseEntity<String> executeRemoveSemester(final Long semesterId) {
        if(semesterRepository.existsById(semesterId)){
            semesterRepository.deleteById(semesterId);
            return ResponseEntity.ok("선택하신 학기가 삭제 되었습니다. 삭제하신 데이터는 열람이 불가능합니다.");
        } else return new ResponseEntity<>("선택하신 학기가 존재하지 않아 삭제 작업을 진행하지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
}
