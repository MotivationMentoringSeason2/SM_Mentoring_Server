package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.EmployeeSignModel;
import net.skhu.mentoring.model.ProfessorSignModel;
import net.skhu.mentoring.model.StudentSignModel;

import java.security.Principal;

public interface CommonService {
    StudentSignModel fetchCurrentStudentInfo(final Principal principal);
    ProfessorSignModel fetchCurrentProfessorInfo(final Principal principal);
    EmployeeSignModel fetchCurrentEmployeeInfo(final Principal principal);
}
