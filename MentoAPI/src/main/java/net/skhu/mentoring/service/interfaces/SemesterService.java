package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.model.SemesterModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SemesterService {
    List<Semester> fetchAllSemesterList();
    Semester fetchSemesterById(final Long semesterId);
    Semester fetchCurrentSemester();
    ResponseEntity<String> executeCreateSemester(final SemesterModel semesterModel);
    ResponseEntity<String> executeRemoveSemester(final Long semesterId);
}
