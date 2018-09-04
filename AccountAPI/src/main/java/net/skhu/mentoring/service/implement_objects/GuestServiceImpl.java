package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Employee;
import net.skhu.mentoring.domain.Professor;
import net.skhu.mentoring.domain.Student;
import net.skhu.mentoring.enumeration.StudentStatus;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.model.EmployeeSignModel;
import net.skhu.mentoring.model.ProfessorSignModel;
import net.skhu.mentoring.model.StudentSignModel;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.repository.DepartmentRepository;
import net.skhu.mentoring.repository.EmployeeRepository;
import net.skhu.mentoring.repository.ProfessorRepository;
import net.skhu.mentoring.repository.StudentRepository;
import net.skhu.mentoring.service.interfaces.GuestService;
import net.skhu.mentoring.util.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GuestServiceImpl implements GuestService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    private boolean confirmMainAndMulti(long departmentId, List<Long> multiDepartments) {
        return multiDepartments.contains(departmentId);
    }

    @Override
    @Transactional
    public ResponseEntity<String> studentSignMessage(StudentSignModel studentSignModel) {
        Department department;
        List<Department> multiDepartments;

        if (accountRepository.existsByIdentity(studentSignModel.getIdentity()))
            return new ResponseEntity<>("회원 아이디가 이미 존재합니다. 다시 시도 바랍니다.", HttpStatus.CONFLICT);

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

        Student createStudent = new Student(0L, UserType.STUDENT, studentSignModel.getGender(), department, studentSignModel.getName(), studentSignModel.getIdentity(), Encryption.encrypt(studentSignModel.getPassword(), Encryption.MD5), studentSignModel.getPhone(), studentSignModel.getEmail(), studentSignModel.getGrade(), StudentStatus.NORMAL, false, multiDepartments);
        Student resultStudent = studentRepository.save(createStudent);

        if (resultStudent.getId() != null)
            return new ResponseEntity<>("학생 회원 가입 과정이 완료 되었습니다. 로그인을 진행하시면 됩니다.", HttpStatus.CREATED);
        else
            return new ResponseEntity<>("학생 회원 가입 도중에 서버에서 문제가 발생하였습니다. 최대한 빠른 조치를 취하겠습니다.", HttpStatus.FORBIDDEN);
    }

    @Override
    @Transactional
    public ResponseEntity<String> professorSignMessage(ProfessorSignModel professorSignModel) {
        Department department;
        List<Department> multiDepartments;

        if (accountRepository.existsByIdentity(professorSignModel.getIdentity()))
            return new ResponseEntity<>("회원 아이디가 이미 존재합니다. 다시 시도 바랍니다.", HttpStatus.CONFLICT);

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

        Professor createProfessor = new Professor(0L, UserType.PROFESSOR, professorSignModel.getGender(), department, professorSignModel.getName(), professorSignModel.getIdentity(), Encryption.encrypt(professorSignModel.getPassword(), Encryption.MD5), professorSignModel.getPhone(), professorSignModel.getEmail(), professorSignModel.getOfficePhone(), professorSignModel.getOfficePlace(), professorSignModel.getHasChairman(), multiDepartments);
        Professor resultProfessor = professorRepository.save(createProfessor);
        if (resultProfessor.getId() != null)
            return new ResponseEntity<>("교수 회원 가입 과정이 완료 되었습니다. 로그인을 진행하시면 됩니다.", HttpStatus.CREATED);
        else
            return new ResponseEntity<>("교수 회원 가입 도중에 서버에서 문제가 발생하였습니다. 최대한 빠른 조치를 취하겠습니다.", HttpStatus.FORBIDDEN);
    }

    @Override
    @Transactional
    public ResponseEntity<String> employeeSignMessage(EmployeeSignModel employeeSignModel) {
        List<Department> departments;

        if (accountRepository.existsByIdentity(employeeSignModel.getIdentity()))
            return new ResponseEntity<>("회원 아이디가 이미 존재합니다. 다시 시도 바랍니다.", HttpStatus.CONFLICT);

        if (employeeSignModel.getDepartments().size() > 0)
            departments = employeeSignModel.getDepartments().stream()
                    .map(departmentId -> departmentRepository.getOne(departmentId))
                    .collect(Collectors.toList());
        else departments = new ArrayList<>();

        Employee createEmployee = new Employee(0L, UserType.PROFESSOR, employeeSignModel.getGender(), null, employeeSignModel.getName(), employeeSignModel.getIdentity(), Encryption.encrypt(employeeSignModel.getPassword(), Encryption.MD5), employeeSignModel.getPhone(), employeeSignModel.getEmail(), employeeSignModel.getOfficePhone(), employeeSignModel.getOfficePlace(), departments);
        Employee resultEmployee = employeeRepository.save(createEmployee);
        if (resultEmployee.getId() != null)
            return new ResponseEntity<>("직원 회원 가입 과정이 완료 되었습니다. 로그인을 진행하시면 됩니다.", HttpStatus.CREATED);
        else
            return new ResponseEntity<>("직원 회원 가입 도중에 서버에서 문제가 발생하였습니다. 최대한 빠른 조치를 취하겠습니다.", HttpStatus.FORBIDDEN);
    }
}
