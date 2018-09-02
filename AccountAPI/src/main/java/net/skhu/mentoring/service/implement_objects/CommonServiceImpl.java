package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.AvailableTime;
import net.skhu.mentoring.domain.Profile;
import net.skhu.mentoring.enumeration.Day;
import net.skhu.mentoring.enumeration.ImageSuffix;
import net.skhu.mentoring.enumeration.UserType;
import net.skhu.mentoring.model.AvailableTimeModel;
import net.skhu.mentoring.model.EmployeeSignModel;
import net.skhu.mentoring.model.ProfessorSignModel;
import net.skhu.mentoring.model.StudentSignModel;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.repository.AvailableTimeRepository;
import net.skhu.mentoring.repository.EmployeeRepository;
import net.skhu.mentoring.repository.ProfessorRepository;
import net.skhu.mentoring.repository.ProfileRepository;
import net.skhu.mentoring.repository.StudentRepository;
import net.skhu.mentoring.service.interfaces.CommonService;
import net.skhu.mentoring.util.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
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
    private ProfileRepository profileRepository;

    private boolean timetableVaildation(List<AvailableTimeModel> timetable){
        for(Day d : Day.values()){
            List<AvailableTimeModel> dayTimetable = timetable.stream().filter(t -> t.getDay() == d).collect(Collectors.toList());
            if(dayTimetable.size() > 1){
                for(int k=0;k<dayTimetable.size();k++){
                    AvailableTimeModel tmpModel = dayTimetable.get(k);
                    for(int l=0;l<dayTimetable.size();l++){
                        if(l != k){
                            AvailableTimeModel anotherModel = dayTimetable.get(l);
                            if(tmpModel.isNotVaildRange(anotherModel)) return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private String fileNameEncryption(final String fileName) {
        int infix=fileName.lastIndexOf('.');
        String fileSuffix = fileName.substring(infix, fileName.length());
        String filePrefix = Encryption.encrypt(fileName.substring(0, infix-1), Encryption.SHA256);
        return filePrefix + fileSuffix;
    }

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

    @Override
    @Transactional
    public ResponseEntity<String> executeSavingMyAvailableTime(final Principal principal, final List<AvailableTimeModel> timetable){
        if(timetable.size() <= 0)
            return new ResponseEntity<>("시간대를 하나라도 설정하고 난 이후에 시도 바랍니다.", HttpStatus.FORBIDDEN);
        Account account = accountRepository.findByIdentity(principal.getName()).get();
        if(timetable.size() > 1) Collections.sort(timetable);
        if(!this.timetableVaildation(timetable))
            return new ResponseEntity<>("가능한 시간대는 시간이 겹치지 않도록 작성 바랍니다.", HttpStatus.FORBIDDEN);
        if(availableTimeRepository.existsByAccount(account)){
            availableTimeRepository.deleteByAccount(account);
        }
        for(AvailableTimeModel atm : timetable){
            availableTimeRepository.save(new AvailableTime(0L, account, atm.getDay(), atm.getStartTime(), atm.getEndTime()));
        }
        return ResponseEntity.ok("가능한 시간대 설정이 완료 되었습니다.");
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeProfileUploading(final MultipartFile file, final Principal principal) throws IOException {
        byte[] fileBytes = file.getBytes();
        if(fileBytes == null || fileBytes.length <= 0)
            return new ResponseEntity<>("파일 형식이 잘 못 되었습니다. 다시 시도 바랍니다.", HttpStatus.FORBIDDEN);

        Account account = accountRepository.findByIdentity(principal.getName()).get();
        Optional<Profile> profile = profileRepository.findByAccount(account);

        String fileName = file.getOriginalFilename();
        int infix = fileName.lastIndexOf('.');
        String fileSuffix = fileName.substring(infix + 1, fileName.length());
        Profile tmpProfile;
        if(!profile.isPresent()){
            tmpProfile = new Profile(0L, account, this.fileNameEncryption(fileName), fileBytes.length, fileBytes, ImageSuffix.valueOf(fileSuffix.toUpperCase()), LocalDateTime.now());
        }else{
            Profile beforeProfile = profile.get();
            tmpProfile = new Profile(beforeProfile.getId(), account, this.fileNameEncryption(fileName), fileBytes.length, fileBytes, ImageSuffix.valueOf(fileSuffix.toUpperCase()), LocalDateTime.now());
        }
        Profile resultProfile = profileRepository.save(tmpProfile);
        if(resultProfile.getId() != 0L)
            return new ResponseEntity<>("프로필 사진 설정이 완료 되었습니다.", HttpStatus.OK);
        else
            return new ResponseEntity<>("프로필 사진 설정 중에 서버에서 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
    }
}