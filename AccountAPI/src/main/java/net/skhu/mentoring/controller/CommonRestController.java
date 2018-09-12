package net.skhu.mentoring.controller;

import net.skhu.mentoring.exception.CustomException;
import net.skhu.mentoring.model.AvailableTimeModel;
import net.skhu.mentoring.model.EmployeeSignModel;
import net.skhu.mentoring.model.LoginModel;
import net.skhu.mentoring.model.ProfessorSignModel;
import net.skhu.mentoring.model.StudentSignModel;
import net.skhu.mentoring.service.interfaces.CommonService;
import net.skhu.mentoring.vo.PrincipalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/AccountAPI/common")
public class CommonRestController {
    @Autowired
    private CommonService commonService;

    @GetMapping("denied")
    public ResponseEntity<String> denied() {
        return new ResponseEntity<String>("Authorization Roles Denied 403 Error", HttpStatus.FORBIDDEN);
    }

    @GetMapping("principal")
    public ResponseEntity<?> fetchCurrentPrincipal(HttpServletRequest request, Principal principal) {
        try {
            return new ResponseEntity<PrincipalVO>(commonService.fetchCurrentPrincipal(principal, request), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity(e.getMessage(), e.getHttpStatus());
        }
    }

    @GetMapping("available_times")
    public ResponseEntity<?> fetchAvailableTimeModels(Principal principal) {
        List<AvailableTimeModel> availableTimes = commonService.fetchCurrentAccountTimetableModel(principal);
        return ResponseEntity.ok(availableTimes);
    }

    @PutMapping("available_times")
    public ResponseEntity<?> executeSavingAvailableTimes(HttpServletRequest request, Principal principal, @RequestBody List<AvailableTimeModel> timetables){
        return commonService.executeSavingMyAvailableTime(principal, request, timetables);
    }

    @GetMapping("sign_form/{type}")
    public ResponseEntity<?> fetchUpdateSignForm(HttpServletRequest request, Principal principal, @PathVariable String type){
        switch(type){
            case "STUDENT" :
                return ResponseEntity.ok(commonService.fetchCurrentStudentInfo(principal, request));
            case "PROFESSOR" :
                return ResponseEntity.ok(commonService.fetchCurrentProfessorInfo(principal, request));
            case "EMPLOYEE" :
                return ResponseEntity.ok(commonService.fetchCurrentEmployeeInfo(principal, request));
            default :
                return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("password_confirm")
    public ResponseEntity<?> executeCurrentPassword(HttpServletRequest request, Principal principal, @RequestBody LoginModel loginModel){
        return ResponseEntity.ok(commonService.executeConfirmCurrentPassword(principal, request, loginModel));
    }

    @PutMapping("sign_form/student")
    public ResponseEntity<String> executeUpdateStudentSignForm(HttpServletRequest request, Principal principal, @RequestBody StudentSignModel studentSignModel){
        return commonService.executeSavingCurrentStudentInfo(principal, request, studentSignModel);
    }

    @PutMapping("sign_form/professor")
    public ResponseEntity<String> executeUpdateProfessorSignForm(HttpServletRequest request, Principal principal, @RequestBody ProfessorSignModel professorSignModel){
        return commonService.executeSavingCurrentProfessorInfo(principal, request, professorSignModel);
    }

    @PutMapping("sign_form/employee")
    public ResponseEntity<String> executeUpdateEmployeeSignForm(HttpServletRequest request, Principal principal, @RequestBody EmployeeSignModel employeeSignModel){
        return commonService.executeSavingCurrentEmployeeInfo(principal, request, employeeSignModel);
    }

    @PutMapping(value = "profile/saving", consumes="multipart/form-data")
    public ResponseEntity<String> executeSavingProfile(HttpServletRequest request, Principal principal, @RequestPart("file") MultipartFile multipartFile) {
        try {
            commonService.executeProfileSaving(multipartFile, principal, request);
        } catch (IOException e) {
            return new ResponseEntity<String>("프로필 설정 중에 오류가 발생했습니다.", HttpStatus.NOT_MODIFIED);
        }
        return new ResponseEntity<String>("프로필 설정이 완료 되었습니다.", HttpStatus.OK);
    }

    @DeleteMapping("profile/releasing")
    public ResponseEntity<String> executeReleasingProfile(HttpServletRequest request, Principal principal) {
        return commonService.executeProfileReleasing(principal, request);
    }

    @DeleteMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
