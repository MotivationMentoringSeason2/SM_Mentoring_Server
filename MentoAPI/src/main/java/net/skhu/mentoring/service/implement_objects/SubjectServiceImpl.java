package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Subject;
import net.skhu.mentoring.model.SubjectModel;
import net.skhu.mentoring.repository.SubjectRepository;
import net.skhu.mentoring.service.interfaces.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;

    @Override
    public List<Subject> fetchAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public Subject fetchBySubjectId(final Long subjectId) {
        Optional<Subject> subject = subjectRepository.findById(subjectId);
        if(subject.isPresent()) return subject.get();
        else return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeCreateSubject(final SubjectModel subjectModel) {
        Optional<Subject> subject = subjectRepository.findByName(subjectModel.getName());
        if(subject.isPresent()){
            return new ResponseEntity<>("이미 존재하는 주제(과목)입니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else {
            subjectRepository.save(new Subject(0L, subjectModel.getName()));
            return ResponseEntity.ok("새로운 주제(과목)가 생성 되었습니다.");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemoveSubject(final List<Long> ids) {
        if(subjectRepository.existsByIdIn(ids)){
            subjectRepository.deleteByIdIn(ids);
            return ResponseEntity.ok("선택하신 주제(과목)이 삭제 되었습니다.");
        } else return new ResponseEntity<>("존재하지 않는 주제가 포함 되어 있습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
}
