package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.model.EmployeeSignModel;
import net.skhu.mentoring.model.ProfessorSignModel;
import net.skhu.mentoring.model.StudentSignModel;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.repository.EmployeeRepository;
import net.skhu.mentoring.repository.ProfessorRepository;
import net.skhu.mentoring.repository.StudentRepository;
import net.skhu.mentoring.service.interfaces.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public StudentSignModel fetchCurrentStudentInfo(final Principal principal) {
        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if(!account.getType().equals(UserType.STUDENT)) return null;
        else return StudentSignModel.builtToUpdateModel(studentRepository.findByIdentity(principal.getName()).get());
    }

    @Override
    public ProfessorSignModel fetchCurrentProfessorInfo(final Principal principal) {
        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if(!account.getType().equals(UserType.PROFESSOR)) return null;
        else return ProfessorSignModel.builtToUpdateModel(professorRepository.findByIdentity(principal.getName()).get());
    }

    @Override
    public EmployeeSignModel fetchCurrentEmployeeInfo(final Principal principal) {
        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if(account.getType().equals(UserType.EMPLOYEE)) return null;
        else return EmployeeSignModel.builtToUpdateModel(employeeRepository.findByIdentity(principal.getName()).get());
    }
}
