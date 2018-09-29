package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.component.JwtTokenProvider;
import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.Employee;
import net.skhu.mentoring.domain.Professor;
import net.skhu.mentoring.domain.Student;
import net.skhu.mentoring.enumeration.StudentStatus;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.model.AccountPagination;
import net.skhu.mentoring.model.OptionModel;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.repository.EmployeeRepository;
import net.skhu.mentoring.repository.ProfessorRepository;
import net.skhu.mentoring.repository.StudentRepository;
import net.skhu.mentoring.service.interfaces.AdminService;
import net.skhu.mentoring.vo.BriefAccountVO;
import net.skhu.mentoring.vo.EmployeeViewVO;
import net.skhu.mentoring.vo.ProfessorViewVO;
import net.skhu.mentoring.vo.StudentViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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

    @Override
    @Transactional
    public ResponseEntity<?> executeAppointChairman(final Principal principal, final HttpServletRequest request, final Long id) {
        if(!this.tokenValidation(principal, request)) return null;
        Optional<Account> myAccount = accountRepository.findByIdentity(principal.getName());
        if(myAccount.get().getType().equals(UserType.STUDENT))
            return new ResponseEntity<>("관리자 학생에게는 권한이 없습니다. 학과 교수나 담당 직원에게 연락 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            Account tmpAccount = account.get();
            switch(tmpAccount.getType()){
                case UserType.STUDENT :
                    if(!studentRepository.existsByDepartmentIdAndHasChairmanIsTrue(tmpAccount.getDepartment().getId())){
                        Student student = studentRepository.findByIdentity(tmpAccount.getIdentity()).get();
                        student.setHasChairman(true);
                        studentRepository.save(student);
                        return new ResponseEntity<>(String.format("%s 님이 %s 회장으로 바뀌었습니다.", student.getName(), student.getDepartment().getName()), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(String.format("%s 회장이 이미 존재합니다. 다시 시도 바랍니다.", tmpAccount.getDepartment().getName()), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
                    }
                case UserType.PROFESSOR :
                    if(!professorRepository.existsByDepartmentIdAndHasChairmanIsTrue(tmpAccount.getDepartment().getId())){
                        Professor professor = professorRepository.findByIdentity(tmpAccount.getIdentity()).get();
                        professor.setHasChairman(true);
                        professorRepository.save(professor);
                        return new ResponseEntity<>(String.format("%s 님이 %s 장으로 바뀌었습니다.", professor.getName(), professor.getDepartment().getName()), HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(String.format("%s 장이 이미 존재합니다. 다시 시도 바랍니다.", tmpAccount.getDepartment().getName()), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
                    }
                default :
                    return new ResponseEntity<>("직원은 회장과 학과장 설정이 필요 없습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
        } else return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<?> executeAppointMentoring(final Principal principal, final HttpServletRequest request, final String userId, final String method) {
        if(!this.tokenValidation(principal, request)) return ResponseEntity.noContent().build();
        Optional<Account> account = accountRepository.findByIdentity(userId);
        if(account.isPresent()){
            Account tmpAccount = account.get();
            if(tmpAccount.getType().equals(UserType.STUDENT)){
                Student student = studentRepository.findByIdentity(userId).get();
                switch(method){
                    case "mento" :
                        student.setStatus(StudentStatus.MENTO);
                        studentRepository.save(student);
                        return ResponseEntity.ok(String.format("%s 님의 권한이 멘토로 바뀌었습니다.", student.getName()));
                    case "menti" :
                        student.setStatus(StudentStatus.MENTI);
                        studentRepository.save(student);
                        return ResponseEntity.ok(String.format("%s 님의 권한이 멘티로 바뀌었습니다.", student.getName()));
                }
            }
            return new ResponseEntity<>(String.format("%s 님은 학생이 아니어 멘토링 등급으로 바꿀 수 없습니다.", tmpAccount.getName()), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
        else return new ResponseEntity<>("계정 정보가 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<?> executeResettingStudentStatus(final Principal principal, final HttpServletRequest request) {
        if(!this.tokenValidation(principal, request)) return ResponseEntity.noContent().build();
        List<Student> students = studentRepository.findAll();
        for(Student student : students){
            student.setStatus(StudentStatus.NORMAL);
            studentRepository.save(student);
        }
        return new ResponseEntity<>("모든 학생의 정보가 일반 학생으로 돌아왔습니다.", HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<?> executeReleaseChairman(Principal principal, HttpServletRequest request, Long id) {
        if(!this.tokenValidation(principal, request)) return null;
        Optional<Account> myAccount = accountRepository.findByIdentity(principal.getName());
        if(myAccount.get().getType().equals(UserType.STUDENT))
            return new ResponseEntity<>("관리자 학생에게는 권한이 없습니다. 학과 교수나 담당 직원에게 연락 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()) {
            Account tmpAccount = account.get();
            switch (tmpAccount.getType()) {
                case UserType.STUDENT:
                    Student student = studentRepository.findByIdentity(tmpAccount.getIdentity()).get();
                    student.setHasChairman(false);
                    studentRepository.save(student);
                    return new ResponseEntity<>(String.format("%s 님의 회장 해지 작업이 완료되었습니다.", student.getName()), HttpStatus.OK);
                case UserType.PROFESSOR:
                    Professor professor = professorRepository.findByIdentity(tmpAccount.getIdentity()).get();
                    professor.setHasChairman(false);
                    professorRepository.save(professor);
                    return new ResponseEntity<>(String.format("%s 님의 학과장 해지 작입어 완료되었습니다.", professor.getName()), HttpStatus.OK);
                default:
                    return new ResponseEntity<>("직원은 회장과 학과장 설정이 필요 없습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
        } else return new ResponseEntity<>("존재하지 않는 회원입니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
}
