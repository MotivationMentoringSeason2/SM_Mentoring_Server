package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.component.JwtTokenProvider;
import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.AvailableTime;
import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Employee;
import net.skhu.mentoring.domain.Professor;
import net.skhu.mentoring.domain.Profile;
import net.skhu.mentoring.domain.Student;
import net.skhu.mentoring.enumeration.Day;
import net.skhu.mentoring.enumeration.ImageSuffix;
import net.skhu.mentoring.enumeration.StudentStatus;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.exception.CustomException;
import net.skhu.mentoring.model.AvailableTimeModel;
import net.skhu.mentoring.model.EmployeeSignModel;
import net.skhu.mentoring.model.ProfessorSignModel;
import net.skhu.mentoring.model.StudentSignModel;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.repository.AvailableTimeRepository;
import net.skhu.mentoring.repository.DepartmentRepository;
import net.skhu.mentoring.repository.EmployeeRepository;
import net.skhu.mentoring.repository.ProfessorRepository;
import net.skhu.mentoring.repository.ProfileRepository;
import net.skhu.mentoring.repository.StudentRepository;
import net.skhu.mentoring.service.interfaces.CommonService;
import net.skhu.mentoring.util.Encryption;
import net.skhu.mentoring.vo.PrincipalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private AvailableTimeRepository availableTimeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private boolean tokenValidation(final Principal principal, final HttpServletRequest request) {
        String jwtToken;
        String currentLoginId;
        String tokenLoginId;
        if (principal != null) {
            jwtToken = jwtTokenProvider.resolveToken(request);
            currentLoginId = principal.getName();
            tokenLoginId = jwtTokenProvider.getUsername(jwtToken);
        } else {
            return false;
        }
        return currentLoginId.equals(tokenLoginId);
    }

    private boolean confirmMainAndMulti(final long departmentId, final List<Long> multiDepartments) {
        return multiDepartments.contains(departmentId);
    }

    private boolean timetableValidation(final List<AvailableTimeModel> timetable) {
        for (Day d : Day.values()) {
            List<AvailableTimeModel> dayTimetable = timetable.stream().filter(t -> t.getDay() == d).collect(Collectors.toList());
            if (dayTimetable.size() > 1) {
                for (int k = 0; k < dayTimetable.size(); k++) {
                    AvailableTimeModel tmpModel = dayTimetable.get(k);
                    for (int l = 0; l < dayTimetable.size(); l++) {
                        if (l != k) {
                            AvailableTimeModel anotherModel = dayTimetable.get(l);
                            if (!tmpModel.isValidRange(anotherModel)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private String fileNameEncryption(final String fileName) {
        int infix = fileName.lastIndexOf('.');
        String fileSuffix = fileName.substring(infix, fileName.length());
        String filePrefix = Encryption.encrypt(fileName.substring(0, infix - 1), Encryption.SHA256);
        return filePrefix + fileSuffix;
    }

    @Override
    public PrincipalVO fetchCurrentPrincipal(final Principal principal, final HttpServletRequest request) {
        if (!this.tokenValidation(principal, request))
            throw new CustomException("유효하지 않은 토큰입니다. 다시 시도 바랍니다.", HttpStatus.UNAUTHORIZED);

        Account account = accountRepository.findByIdentity(principal.getName()).get();
        String jwtToken = jwtTokenProvider.resolveToken(request);
        Date loginTime = jwtTokenProvider.getIssuedAt(jwtToken);

        switch (account.getType()) {
            case UserType.STUDENT:
                return PrincipalVO.builtToVOWithStudent(studentRepository.findByIdentity(principal.getName()).get(), LocalDateTime.ofInstant(loginTime.toInstant(), ZoneId.systemDefault()));
            case UserType.PROFESSOR:
                return PrincipalVO.builtToVOWithProfessor(professorRepository.findByIdentity(principal.getName()).get(), LocalDateTime.ofInstant(loginTime.toInstant(), ZoneId.systemDefault()));
            case UserType.EMPLOYEE:
                return PrincipalVO.builtToVOWithEmployee(employeeRepository.findByIdentity(principal.getName()).get(), LocalDateTime.ofInstant(loginTime.toInstant(), ZoneId.systemDefault()));
        }
        return null;
    }

    @Override
    public List<AvailableTimeModel> fetchCurrentAccountTimetableModel(Principal principal) {
        Account account = accountRepository.findByIdentity(principal.getName()).get();
        return availableTimeRepository.findByAccount(account).stream()
                .map(availableTime -> AvailableTimeModel.builtToModel(availableTime))
                .sorted(Comparator.comparingInt(AvailableTimeModel::getDayOrdinal))
                .collect(Collectors.toList());
    }

    @Override
    public StudentSignModel fetchCurrentStudentInfo(final Principal principal, final HttpServletRequest request) {
        if (!this.tokenValidation(principal, request))
            throw new CustomException("유효하지 않은 토큰입니다. 다시 시도 바랍니다.", HttpStatus.UNAUTHORIZED);

        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if (!account.getType().equals(UserType.STUDENT)) return null;
        else return StudentSignModel.builtToUpdateModel(studentRepository.findByIdentity(principal.getName()).get());
    }

    @Override
    public ProfessorSignModel fetchCurrentProfessorInfo(final Principal principal, final HttpServletRequest request) {
        if (!this.tokenValidation(principal, request))
            throw new CustomException("유효하지 않은 토큰입니다. 다시 시도 바랍니다.", HttpStatus.UNAUTHORIZED);

        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if (!account.getType().equals(UserType.PROFESSOR)) return null;
        else
            return ProfessorSignModel.builtToUpdateModel(professorRepository.findByIdentity(principal.getName()).get());
    }

    @Override
    public EmployeeSignModel fetchCurrentEmployeeInfo(final Principal principal, final HttpServletRequest request) {
        if (!this.tokenValidation(principal, request))
            throw new CustomException("유효하지 않은 토큰입니다. 다시 시도 바랍니다.", HttpStatus.UNAUTHORIZED);

        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if (account.getType().equals(UserType.EMPLOYEE)) return null;
        else return EmployeeSignModel.builtToUpdateModel(employeeRepository.findByIdentity(principal.getName()).get());
    }

    @Override
    public ResponseEntity<String> executeSavingCurrentStudentInfo(final Principal principal, final HttpServletRequest request, final StudentSignModel studentSignModel) {
        if (!this.tokenValidation(principal, request))
            throw new CustomException("유효하지 않은 토큰입니다. 다시 시도 바랍니다.", HttpStatus.UNAUTHORIZED);

        Department department;
        List<Department> multiDepartments;

        if (confirmMainAndMulti(studentSignModel.getDepartmentId(), studentSignModel.getMultiDepartments()))
            return new ResponseEntity<>("전공과 복수 전공의 유효성을 확인 바랍니다.", HttpStatus.CONFLICT);

        if (studentSignModel.getDepartmentId() != 0L)
            department = departmentRepository.getOne(studentSignModel.getDepartmentId());
        else return new ResponseEntity<>("전공 번호 오류입니다. 다시 시도 바랍니다.", HttpStatus.CONFLICT);

        if (studentSignModel.getMultiDepartments().size() > 0)
            multiDepartments = studentSignModel.getMultiDepartments().stream()
                    .map(departmentId -> departmentRepository.getOne(departmentId))
                    .collect(Collectors.toList());
        else multiDepartments = new ArrayList<>();

        Student mainStudent = studentRepository.findByIdentity(principal.getName()).get();
        Student updateStudent = new Student(mainStudent.getId(), UserType.STUDENT, studentSignModel.getGender(), department, studentSignModel.getName(), studentSignModel.getIdentity(), Encryption.encrypt(studentSignModel.getPassword(), Encryption.MD5), studentSignModel.getPhone(), studentSignModel.getEmail(), studentSignModel.getGrade(), mainStudent.getStatus(), mainStudent.getHasChairman(), multiDepartments);
        Student resultStudent = studentRepository.save(updateStudent);

        if (!resultStudent.equals(mainStudent))
            return new ResponseEntity<>("학생 회원 수정 과정이 완료 되었습니다. 로그인을 진행하시면 됩니다.", HttpStatus.OK);
        else
            return new ResponseEntity<>("학생 회원 수정 내용이 이전과 같습니다. 다시 시도 바랍니다.", HttpStatus.NOT_MODIFIED);
    }

    @Override
    public ResponseEntity<String> executeSavingCurrentProfessorInfo(final Principal principal, final HttpServletRequest request, final ProfessorSignModel professorSignModel) {
        if (!this.tokenValidation(principal, request))
            throw new CustomException("유효하지 않은 토큰입니다. 다시 시도 바랍니다.", HttpStatus.UNAUTHORIZED);

        Department department;
        List<Department> multiDepartments;

        if (confirmMainAndMulti(professorSignModel.getDepartmentId(), professorSignModel.getMultiDepartments()))
            return new ResponseEntity<>("주 학과와 담당 학과의 유효성을 확인 바랍니다.", HttpStatus.CONFLICT);

        if (professorSignModel.getDepartmentId() != 0L)
            department = departmentRepository.getOne(professorSignModel.getDepartmentId());
        else return new ResponseEntity<>("전공 번호 오류입니다. 다시 시도 바랍니다.", HttpStatus.CONFLICT);

        if (professorSignModel.getMultiDepartments().size() > 0)
            multiDepartments = professorSignModel.getMultiDepartments().stream()
                    .map(departmentId -> departmentRepository.getOne(departmentId))
                    .collect(Collectors.toList());
        else multiDepartments = new ArrayList<>();

        Professor mainProfessor = professorRepository.findByIdentity(principal.getName()).get();
        Professor updateProfessor = new Professor(mainProfessor.getId(), UserType.PROFESSOR, professorSignModel.getGender(), department, professorSignModel.getName(), professorSignModel.getIdentity(), Encryption.encrypt(professorSignModel.getPassword(), Encryption.MD5), professorSignModel.getPhone(), professorSignModel.getEmail(), professorSignModel.getOfficePhone(), professorSignModel.getOfficePlace(), professorSignModel.getHasChairman(), multiDepartments);
        Professor resultProfessor = professorRepository.save(updateProfessor);

        if (!resultProfessor.equals(mainProfessor))
            return new ResponseEntity<>("교수 회원 수정 과정이 완료 되었습니다. 로그인을 진행하시면 됩니다.", HttpStatus.OK);
        else
            return new ResponseEntity<>("교수 회원 수정 내용이 이전과 같습니다. 다시 시도 바랍니다.", HttpStatus.NOT_MODIFIED);
    }

    @Override
    public ResponseEntity<String> executeSavingCurrentEmployeeInfo(final Principal principal, final HttpServletRequest request, final EmployeeSignModel employeeSignModel) {
        if (!this.tokenValidation(principal, request))
            throw new CustomException("유효하지 않은 토큰입니다. 다시 시도 바랍니다.", HttpStatus.UNAUTHORIZED);

        List<Department> departments;

        if (employeeSignModel.getDepartments().size() > 0)
            departments = employeeSignModel.getDepartments().stream()
                    .map(departmentId -> departmentRepository.getOne(departmentId))
                    .collect(Collectors.toList());
        else departments = new ArrayList<>();

        Employee mainEmployee = employeeRepository.findByIdentity(principal.getName()).get();
        Employee updateEmployee = new Employee(mainEmployee.getId(), UserType.PROFESSOR, employeeSignModel.getGender(), null, employeeSignModel.getName(), employeeSignModel.getIdentity(), Encryption.encrypt(employeeSignModel.getPassword(), Encryption.MD5), employeeSignModel.getPhone(), employeeSignModel.getEmail(), employeeSignModel.getOfficePhone(), employeeSignModel.getOfficePlace(), departments);
        Employee resultEmployee = employeeRepository.save(updateEmployee);

        if (!resultEmployee.equals(mainEmployee))
            return new ResponseEntity<>("직원 회원 수정 과정이 완료 되었습니다. 로그인을 진행하시면 됩니다.", HttpStatus.OK);
        else
            return new ResponseEntity<>("직원 회원 수정 내용이 이전과 같습니다. 다시 시도 바랍니다.", HttpStatus.NOT_MODIFIED);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeSavingMyAvailableTime(final Principal principal, final HttpServletRequest request, final List<AvailableTimeModel> timetable) {
        if (!this.tokenValidation(principal, request))
            throw new CustomException("유효하지 않은 토큰입니다. 다시 시도 바랍니다.", HttpStatus.UNAUTHORIZED);

        if (timetable.size() <= 0)
            return new ResponseEntity<>("시간대를 하나라도 설정하고 난 이후에 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if (timetable.size() > 1) Collections.sort(timetable);
        if (!this.timetableValidation(timetable))
            return new ResponseEntity<>("가능한 시간대는 시간이 겹치지 않도록 작성 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        if (availableTimeRepository.existsByAccount(account)) {
            availableTimeRepository.deleteByAccount(account);
        }
        for (AvailableTimeModel atm : timetable) {
            availableTimeRepository.save(new AvailableTime(0L, account, atm.getDay(), atm.getStartTime(), atm.getEndTime()));
        }
        return ResponseEntity.ok("가능한 시간대 설정이 완료 되었습니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeProfileUploading(final MultipartFile file, final Principal principal, final HttpServletRequest request) throws IOException {
        if (!this.tokenValidation(principal, request))
            throw new CustomException("유효하지 않은 토큰입니다. 다시 시도 바랍니다.", HttpStatus.UNAUTHORIZED);

        byte[] fileBytes = file.getBytes();
        if (fileBytes == null || fileBytes.length <= 0)
            return new ResponseEntity<>("파일 형식이 잘 못 되었습니다. 다시 시도 바랍니다.", HttpStatus.FORBIDDEN);

        Account account = accountRepository.findByIdentity(principal.getName()).get();
        Optional<Profile> profile = profileRepository.findByAccount(account);

        String fileName = file.getOriginalFilename();
        int infix = fileName.lastIndexOf('.');
        String fileSuffix = fileName.substring(infix + 1, fileName.length());
        Profile tmpProfile;
        if (!profile.isPresent()) {
            tmpProfile = new Profile(0L, account, this.fileNameEncryption(fileName), fileBytes.length, fileBytes, ImageSuffix.valueOf(fileSuffix.toUpperCase()), LocalDateTime.now());
        } else {
            Profile beforeProfile = profile.get();
            tmpProfile = new Profile(beforeProfile.getId(), account, this.fileNameEncryption(fileName), fileBytes.length, fileBytes, ImageSuffix.valueOf(fileSuffix.toUpperCase()), LocalDateTime.now());
        }
        Profile resultProfile = profileRepository.save(tmpProfile);
        if (resultProfile.getId() != 0L)
            return new ResponseEntity<>("프로필 사진 설정이 완료 되었습니다.", HttpStatus.OK);
        else
            return new ResponseEntity<>("프로필 사진 설정 중에 서버에서 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
    }
}
