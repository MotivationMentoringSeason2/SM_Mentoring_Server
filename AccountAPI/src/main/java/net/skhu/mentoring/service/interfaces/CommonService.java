package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.AvailableTimeModel;
import net.skhu.mentoring.model.EmployeeSignModel;
import net.skhu.mentoring.model.ProfessorSignModel;
import net.skhu.mentoring.model.StudentSignModel;
import net.skhu.mentoring.vo.PrincipalVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

public interface CommonService {
    PrincipalVO fetchCurrentPrincipal(final Principal principal, final HttpServletRequest request);
    List<AvailableTimeModel> fetchCurrentAccountTimetableModel(final Principal principal);
    StudentSignModel fetchCurrentStudentInfo(final Principal principal, final HttpServletRequest request);
    ProfessorSignModel fetchCurrentProfessorInfo(final Principal principal, final HttpServletRequest request);
    EmployeeSignModel fetchCurrentEmployeeInfo(final Principal principal, final HttpServletRequest request);
    ResponseEntity<String> executeSavingCurrentStudentInfo(final Principal principal, final HttpServletRequest request, final StudentSignModel studentSignModel);
    ResponseEntity<String> executeSavingCurrentProfessorInfo(final Principal principal, final HttpServletRequest request, final ProfessorSignModel professorSignModel);
    ResponseEntity<String> executeSavingCurrentEmployeeInfo(final Principal principal, final HttpServletRequest request, final EmployeeSignModel employeeSignModel);
    ResponseEntity<String> executeSavingMyAvailableTime(final Principal principal, final HttpServletRequest request, final List<AvailableTimeModel> timetable);
    ResponseEntity<String> executeProfileUploading(final MultipartFile file, final Principal principal, final HttpServletRequest request) throws IOException;
}
