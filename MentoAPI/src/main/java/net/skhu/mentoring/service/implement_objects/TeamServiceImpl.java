package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Menti;
import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.domain.TeamAdvertiseFile;
import net.skhu.mentoring.enumeration.ResultStatus;
import net.skhu.mentoring.model.MentoApplicationModel;
import net.skhu.mentoring.repository.MentiRepository;
import net.skhu.mentoring.repository.SemesterRepository;
import net.skhu.mentoring.repository.SubjectRepository;
import net.skhu.mentoring.repository.TeamAdvertiseRepository;
import net.skhu.mentoring.repository.TeamRepository;
import net.skhu.mentoring.service.interfaces.TeamService;
import net.skhu.mentoring.vo.CareerBriefVO;
import net.skhu.mentoring.vo.MentoVO;
import net.skhu.mentoring.vo.MentoringTokenVO;
import net.skhu.mentoring.vo.PersonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private TeamAdvertiseRepository teamAdvertiseRepository;

    @Autowired
    private MentiRepository mentiRepository;

    private String getFileSuffix(final String fileName) {
        int infix = fileName.lastIndexOf('.');
        return fileName.substring(infix, fileName.length());
    }

    private void uploadingTeamAdvFile(final MultipartFile advFile, final Team team) throws IOException {
        Optional<TeamAdvertiseFile> teamAdvFile = teamAdvertiseRepository.findByTeam(team);
        if(teamAdvFile.isPresent()){
            TeamAdvertiseFile teamAdvertiseFile = teamAdvFile.get();
            teamAdvertiseFile.setFileName(advFile.getOriginalFilename());
            teamAdvertiseFile.setFileSize(advFile.getSize());
            teamAdvertiseFile.setFileData(advFile.getBytes());
            teamAdvertiseFile.setFileSuffix(this.getFileSuffix(advFile.getOriginalFilename()).toUpperCase());
            teamAdvertiseFile.setUploadDate(LocalDateTime.now());
            teamAdvertiseRepository.save(teamAdvertiseFile);
        } else {
            TeamAdvertiseFile newTeamAdvFile = new TeamAdvertiseFile(0L, team, advFile.getOriginalFilename(), advFile.getSize(), advFile.getBytes(), this.getFileSuffix(advFile.getOriginalFilename()).toUpperCase(), LocalDateTime.now());
            teamAdvertiseRepository.save(newTeamAdvFile);
        }
    }

    @Override
    public MentoringTokenVO fetchCurrentMentoringToken(final String userId) {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if(semester.isPresent()) {
            Optional<Team> team = teamRepository.findBySemesterAndStatusAndMento(semester.get(), ResultStatus.PERMIT, userId);
            return team.isPresent() ? MentoringTokenVO.builtToVO(team.get(), "MENTO") : null;
        } else return null;
    }

    @Override
    public List<MentoVO> fetchMentoListBySemesterId(final Long semesterId) {
        Optional<Semester> semester = semesterRepository.findById(semesterId);
        if(semester.isPresent()){
            return teamRepository.findBySemester(semester.get()).stream()
                    .map(team -> {
                        Optional<TeamAdvertiseFile> advFile = teamAdvertiseRepository.findByTeam(team);
                        return MentoVO.builtToVO(team, advFile.isPresent() ? advFile.get() : null);
                    })
                    .collect(Collectors.toList());
        } else return null;
    }

    @Override
    public List<MentoVO> fetchMentoListByStatus(final ResultStatus status){
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if(semester.isPresent()) {
            return teamRepository.findBySemesterAndStatus(semester.get(), status).stream()
                    .map(team -> {
                        Optional<TeamAdvertiseFile> advFile = teamAdvertiseRepository.findByTeam(team);
                        return MentoVO.builtToVO(team, advFile.isPresent() ? advFile.get() : null);
                    })
                    .collect(Collectors.toList());
        } else return null;
    }

    @Override
    public List<CareerBriefVO> fetchMentoBriefInfoByIdentity(final String mento) {
        List<Team> mentoInfos = teamRepository.findByMentoOrderByIdDesc(mento);
        return mentoInfos.stream()
                .map(team -> CareerBriefVO.builtToVO(team, (int) mentiRepository.countByTeam(team)))
                .collect(Collectors.toList());
    }

    @Override
    public MentoVO fetchMentoInfoByTeamId(final Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            return team.map(t -> {
                Optional<TeamAdvertiseFile> advFile = teamAdvertiseRepository.findByTeam(t);
                return MentoVO.builtToVO(t, advFile.isPresent() ? advFile.get() : null);
            }).get();
        } else return null;
    }

    @Override
    public PersonVO fetchMentoringTeamPersonByTeamId(final Long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            List<Menti> mentis = mentiRepository.findByTeam(team.get());
            return PersonVO.builtToVO(team.get(), mentis);
        } else return null;
    }

    @Override
    public MentoApplicationModel fetchUpdateMentoApplicationModel(final String mento) {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if(semester.isPresent()) {
            Optional<Team> team = teamRepository.findByMentoAndSemester(mento, semester.get());
            if(team.isPresent()) return MentoApplicationModel.builtToVO(team.get());
            else return null;
        } else return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeMentoApplicate(final MentoApplicationModel mentoApplicationModel, final MultipartFile advFile, final String mento) throws IOException {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if(semester.isPresent()) {
            if(!teamRepository.existsByMentoAndSemester(mento, semester.get())){
                Team team = new Team(0L,
                    semester.get(),
                    mento,
                    mentoApplicationModel.getTeamName(),
                    mentoApplicationModel.getPerson(),
                    mentoApplicationModel.getAdvertise(),
                    mentoApplicationModel.getQualify(),
                    ResultStatus.LOADING,
                    mentoApplicationModel.getSubjects().stream()
                        .map(sId -> subjectRepository.findById(sId))
                        .filter(subject -> subject.isPresent())
                        .map(subject -> subject.get())
                        .collect(Collectors.toList())
                );
                Team createTeam = teamRepository.save(team);
                if(advFile != null){
                    this.uploadingTeamAdvFile(advFile, createTeam);
                }
                return ResponseEntity.ok("멘토 신청이 완료 되었습니다. 멘토 신청 결과는 멘티 신청 이후에 나옵니다.");
            }
            else return new ResponseEntity<>("이미 멘토를 신청 하였습니다. 멘토 신청은 1학기에 1회 가능합니다. 신청 결과 페이지로 이동합니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else return new ResponseEntity<>("멘토 신청 중 학기 설정에 대한 논리적인 문제가 발생했습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeUpdateMentoApplicate(final MentoApplicationModel mentoApplicationModel, final MultipartFile advFile, final String mento) throws IOException {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if(semester.isPresent()) {
            Optional<Team> team = teamRepository.findByMentoAndSemester(mento, semester.get());
            if(team.isPresent()) {
                Team updateTeam = team.get();
                updateTeam.setSubjects(mentoApplicationModel.getSubjects().stream()
                    .map(sId -> subjectRepository.findById(sId))
                    .filter(subject -> subject.isPresent())
                    .map(subject -> subject.get())
                    .collect(Collectors.toList())
                );
                updateTeam.setQualify(mentoApplicationModel.getQualify());
                updateTeam.setName(mentoApplicationModel.getTeamName());
                updateTeam.setPerson(mentoApplicationModel.getPerson());
                updateTeam.setAdvertise(mentoApplicationModel.getAdvertise());
                updateTeam.setStatus(updateTeam.getStatus());
                updateTeam.setMento(mento);
                teamRepository.save(updateTeam);
                if (advFile != null) {
                    this.uploadingTeamAdvFile(advFile, updateTeam);
                }
                return ResponseEntity.ok("멘토 신청 수정이 완료 되었습니다. 멘토 신청 결과는 멘티 신청 이후에 나옵니다.");
            }
            else return new ResponseEntity<>("멘토를 신청하지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else return new ResponseEntity<>("멘토 수정 중 학기 설정에 대한 논리적인 문제가 발생했습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    public ResponseEntity<String> executeUpdateMentoStatus(final Long teamId, final ResultStatus status) {
        Optional<Team> team = teamRepository.findById(teamId);
        if(team.isPresent()){
            Team updateTeam = team.get();
            updateTeam.setStatus(status);
            teamRepository.save(updateTeam);
            return ResponseEntity.ok(String.format("%s 팀의 신청 결과가 %s 로 바뀌었습니다.", updateTeam.getName(), status == ResultStatus.PERMIT ? "허가" : status == ResultStatus.REJECT ? "반려" : "보류"));
        } else return new ResponseEntity<>("멘토 신청 내역이 존재하지 않습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeCancelMentoApplicate(final String mento) {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if(semester.isPresent()) {
            if(teamRepository.existsByMentoAndSemester(mento, semester.get())){
                teamRepository.deleteByMentoAndSemester(mento, semester.get());
                return ResponseEntity.ok(String.format("이번 학기에 신청한 %s 님의 멘토 신청 내역이 삭제 되었습니다.", mento));
            } else {
                return new ResponseEntity<>(String.format("이번 학기에 신청한 %s 님의 멘토 신청 내역이 존재하지 않습니다.", mento), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            }
        } else return new ResponseEntity<>("멘토 취소 중 학기 설정에 대한 논리적인 문제가 발생했습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemoveMentoRegister(final String mento) {
        teamRepository.deleteByMento(mento);
        return ResponseEntity.ok(String.format("탈퇴한 회원 %s 님의 모든 멘토 신청 내역이 삭제 되었습니다.", mento));
    }
}
