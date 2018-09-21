package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Menti;
import net.skhu.mentoring.domain.Semester;
import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.enumeration.ResultStatus;
import net.skhu.mentoring.model.MentiApplicationModel;
import net.skhu.mentoring.repository.MentiRepository;
import net.skhu.mentoring.repository.SemesterRepository;
import net.skhu.mentoring.repository.TeamRepository;
import net.skhu.mentoring.service.interfaces.MentiService;
import net.skhu.mentoring.vo.MentiAppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MentiServiceImpl implements MentiService {
    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MentiRepository mentiRepository;

    @Override
    public List<MentiAppVO> fetchCurrentMentiAppInfo(final String userId) {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if (semester.isPresent()) {
            List<Team> teams = teamRepository.findBySemesterAndStatus(semester.get(), ResultStatus.LOADING);
            return teams.stream()
                    .map(team -> MentiAppVO.builtToVO(team, (int) mentiRepository.countByTeam(team), mentiRepository.existsByUserIdAndTeam(userId, team)))
                    .sorted(Comparator.comparing(MentiAppVO::getHasApplicated, Comparator.reverseOrder()))
                    .collect(Collectors.toList());
        } else return null;
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeCreateMentiApplication(final MentiApplicationModel mentiApplicationModel) {
        Optional<Semester> semester = semesterRepository.findByCurrentSemester();
        if (semester.isPresent()) {
            if(teamRepository.existsByMentoAndSemester(mentiApplicationModel.getMenti(), semester.get())){
                return new ResponseEntity<>(String.format("%s 님은 이번 학기 멘토를 신청하였기 때문에 이번 학기에는 시스템 상 멘티 신청이 불가능합니다. 양해 부탁 드립니다.", mentiApplicationModel.getMenti()), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
            } else {
                if(mentiRepository.existsByUserIdAndTeamSemester(mentiApplicationModel.getMenti(), semester.get()))
                    return new ResponseEntity<>(String.format("%s 님은 이번 학기에 이미 다른 멘티를 신청하였습니다. 다른 멘티를 신청하기 위해 이전에 신청한 멘티를 취소하시길 바랍니다.", mentiApplicationModel.getMenti()), HttpStatus.NON_AUTHORITATIVE_INFORMATION);

                Optional<Team> team = teamRepository.findById(mentiApplicationModel.getTeamId());
                if(!team.isPresent())
                    return new ResponseEntity<>("멘티 신청을 위한 팀이 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);

                Menti menti = new Menti(0L, mentiApplicationModel.getMenti(), team.get());
                mentiRepository.save(menti);
                return ResponseEntity.ok(String.format("%s 님의 %s 팀 멘티 신청이 완료 되었습니다.", mentiApplicationModel.getMenti(), team.get().getName()));
            }
        } else return new ResponseEntity<>("멘티 등록 중 학기 설정에 대한 논리적인 문제가 발생했습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemoveMentiApplication(final MentiApplicationModel mentiApplicationModel) {
        Optional<Team> team = teamRepository.findById(mentiApplicationModel.getTeamId());
        if(!team.isPresent()){
            if(mentiRepository.existsByUserIdAndTeam(mentiApplicationModel.getMenti(), team.get())){
                mentiRepository.deleteByUserIdAndTeam(mentiApplicationModel.getMenti(), team.get());
                return ResponseEntity.ok(String.format("%s 님의 %s 팀 멘티 취소 작업이 완료 되었습니다.", mentiApplicationModel.getMenti(), team.get().getName()));
            } else return new ResponseEntity<>(String.format("%s 님은 %s 팀의 멘티를 신청하지 않았습니다. 그래서 취소 작업이 진행되지 않았습니다.", mentiApplicationModel.getMenti(), team.get().getName()), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        } else return new ResponseEntity<>("선택하신 팀이 존재하지 않습니다. 다시 시도 바랍니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemoveByMentiUser(final String userId) {
        mentiRepository.deleteByUserId(userId);
        return ResponseEntity.ok(String.format("탈퇴한 회원 %s 님의 모든 멘티 신청 내역이 삭제 되었습니다.", userId));
    }
}
