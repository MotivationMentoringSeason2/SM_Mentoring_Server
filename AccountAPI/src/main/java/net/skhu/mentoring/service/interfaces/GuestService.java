package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.EmployeeSignModel;
import net.skhu.mentoring.model.IdentityFindModel;
import net.skhu.mentoring.model.PasswordFindModel;
import net.skhu.mentoring.model.ProfessorSignModel;
import net.skhu.mentoring.model.StudentSignModel;
import org.springframework.http.ResponseEntity;

public interface GuestService {
    ResponseEntity<String> fetchFindAccountIdentity(final IdentityFindModel identityFindModel);
    ResponseEntity<String> studentSignMessage(final StudentSignModel studentSignModel);
    ResponseEntity<String> professorSignMessage(final ProfessorSignModel professorSignModel);
    ResponseEntity<String> employeeSignMessage(final EmployeeSignModel employeeSignModel);
    ResponseEntity<String> fetchFindAccountPassword(final PasswordFindModel PasswordFindModel);
}
