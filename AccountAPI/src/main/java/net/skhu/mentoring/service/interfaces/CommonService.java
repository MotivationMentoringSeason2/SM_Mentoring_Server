package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.AvailableTimeModel;
import net.skhu.mentoring.model.EmployeeSignModel;
import net.skhu.mentoring.model.ProfessorSignModel;
import net.skhu.mentoring.model.StudentSignModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface CommonService {
    StudentSignModel fetchCurrentStudentInfo(final Principal principal);
    ProfessorSignModel fetchCurrentProfessorInfo(final Principal principal);
    EmployeeSignModel fetchCurrentEmployeeInfo(final Principal principal);
    ResponseEntity<String> executeSavingMyAvailableTime(final Principal principal, final List<AvailableTimeModel> timetable);
    ResponseEntity<String> executeProfileUploading(final MultipartFile file, final Principal principal) throws IOException;
}
