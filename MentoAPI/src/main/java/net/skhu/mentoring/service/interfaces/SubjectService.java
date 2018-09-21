package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.Subject;
import net.skhu.mentoring.model.SubjectModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SubjectService {
    List<Subject> fetchAllSubjects();
    Subject fetchBySubjectId(final Long subjectId);
    ResponseEntity<String> executeCreateSubject(final SubjectModel subjectModel);
    ResponseEntity<String> executeRemoveSubject(final List<Long> ids);
}
