package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.component.JwtTokenProvider;
import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.Employee;
import net.skhu.mentoring.domain.Student;
import net.skhu.mentoring.enumeration.StudentStatus;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.model.AccountPagination;
import net.skhu.mentoring.model.OptionModel;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.repository.AvailableTimeRepository;
import net.skhu.mentoring.repository.DepartmentRepository;
import net.skhu.mentoring.repository.EmployeeRepository;
import net.skhu.mentoring.repository.ProfessorRepository;
import net.skhu.mentoring.repository.StudentRepository;
import net.skhu.mentoring.service.interfaces.AdminService;
import net.skhu.mentoring.vo.BriefAccountVO;
import net.skhu.mentoring.vo.EmployeeViewVO;
import net.skhu.mentoring.vo.ProfessorViewVO;
import net.skhu.mentoring.vo.StudentViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private boolean tokenValidation(final Principal principal, final HttpServletRequest request) {
        String jwtToken;
        String tokenLoginId;
        boolean hasAbleAccess;
        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if(account.getType().equals(UserType.EMPLOYEE) || account.getType().equals(UserType.PROFESSOR)) {
            hasAbleAccess = true;

        } else {
            Student student = studentRepository.findByIdentity(principal.getName()).get();
            hasAbleAccess = student.getHasChairman();
        }
        if (principal != null && hasAbleAccess) {
            jwtToken = jwtTokenProvider.resolveToken(request);
            tokenLoginId = jwtTokenProvider.getUsername(jwtToken);
        } else {
            return false;
        }
        return principal.getName().equals(tokenLoginId);
    }


    @Override
    public List<OptionModel> getSearchByModel(final Principal principal, final HttpServletRequest request) {
        if(!this.tokenValidation(principal, request)) return null;
        return accountRepository.searchBy;
    }

    @Override
    public List<OptionModel> getOrderByModel(final Principal principal, final HttpServletRequest request) {
        if(!this.tokenValidation(principal, request)) return null;
        return accountRepository.orderBy;
    }

    @Override
    public List<BriefAccountVO> fetchAccountListWithPagination(final Principal principal, final HttpServletRequest request, final AccountPagination accountPagination) {
        if(!this.tokenValidation(principal, request)) return null;
        List<Account> accounts = accountRepository.findAll(accountPagination);
        return accounts.stream()
                .map(account -> {
                    switch(account.getType()){
                        case UserType.STUDENT :
                            Student student = studentRepository.findByIdentity(account.getIdentity()).get();
                            String stdType = "";
                            switch(student.getStatus()){
                                case NORMAL :
                                    stdType = student.getHasChairman() ? "학생회장" : "일반학생";
                                    break;
                                case MENTI :
                                    stdType = student.getHasChairman() ? "학생회장 + 멘티" : "멘티학생";
                                    break;
                                case MENTO :
                                    stdType = student.getHasChairman() ? "학생회장 + 멘토" : "멘토학생";
                                    break;
                            }
                            return BriefAccountVO.builtToAccountVO(account, stdType, account.getDepartment().getName());
                        case UserType.PROFESSOR :
                            return BriefAccountVO.builtToAccountVO(account, "교수", account.getDepartment().getName());
                        case UserType.EMPLOYEE :
                            Employee employee = employeeRepository.findByIdentity(account.getIdentity()).get();
                            List<String> depts = employee.getDepartments().stream()
                                    .map(dept -> dept.getName())
                                    .collect(Collectors.toList());
                            return BriefAccountVO.builtToAccountVO(account, "직원", String.join(", ", depts));
                        default :
                            return BriefAccountVO.builtToAccountVO(account, "-", "-");
                    }
                }).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<?> fetchAccountView(final Principal principal, final HttpServletRequest request, final Long id) {
        if(!this.tokenValidation(principal, request)) return ResponseEntity.noContent().build();
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            Account tmpAccount = account.get();
            switch(tmpAccount.getType()){
                case UserType.STUDENT :
                    return ResponseEntity.ok(
                        StudentViewVO.builtToVO(studentRepository.findByIdentity(tmpAccount.getIdentity()).get())
                    );
                case UserType.PROFESSOR :
                    return ResponseEntity.ok(
                        ProfessorViewVO.builtToVO(professorRepository.findByIdentity(tmpAccount.getIdentity()).get())
                    );
                case UserType.EMPLOYEE :
                    return ResponseEntity.ok(
                        EmployeeViewVO.builtToVO(employeeRepository.findByIdentity(tmpAccount.getIdentity()).get())
                    );
                default :
                    return ResponseEntity.noContent().build();
            }
        } else return ResponseEntity.noContent().build();
    }
}
