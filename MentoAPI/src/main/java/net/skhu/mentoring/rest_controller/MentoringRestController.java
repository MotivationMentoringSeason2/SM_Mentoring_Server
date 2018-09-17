package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.model.MentoAppicationModel;
import net.skhu.mentoring.service.interfaces.MentiService;
import net.skhu.mentoring.service.interfaces.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("MentoAPI/mentoring")
public class MentoringRestController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private MentiService mentiService;

    @GetMapping("teams/{semesterId}")
    public ResponseEntity<String> fetchTeamListBySemesterId(@PathVariable Long semesterId){
        return ResponseEntity.ok("학기 별 멘토링 팀을 불러옵니다.");
    }

    @GetMapping("team/{teamId}")
    public ResponseEntity<String> fetchTeamById(@PathVariable Long teamId){
        return ResponseEntity.ok("팀 번호로 팀 정보(멘토링 정보)를 가져옵니다.");
    }

    @GetMapping("team/persons/{teamId}")
    public ResponseEntity<String> fetchTeamPersonById(@PathVariable Long teamId){
        return ResponseEntity.ok("팀 번호로 멘토와 멘티 아이디를 가져옵니다.");
    }

    @PostMapping("team")
    public ResponseEntity<String> executeMentoApplicate(@RequestBody MentoAppicationModel mentoAppicationModel){
        return ResponseEntity.ok("멘토 신청이 완료 되었습니다.");
    }

    @PutMapping("team/{teamId}")
    public ResponseEntity<String> executeMentoApplicateUpdating(@PathVariable Long teamId, @RequestBody MentoAppicationModel mentoAppicationModel){
        return ResponseEntity.ok("멘토 신청 내역이 수정 되었습니다.");
    }

    @DeleteMapping("team/{teamId}")
    public ResponseEntity<String> executeMentoApplicateReleasing(@PathVariable Long teamId){
        return ResponseEntity.ok("멘토 신청이 취소 되었습니다.");
    }

    @DeleteMapping("team/{mento}")
    public ResponseEntity<String> executeRemovingByTeamMento(@PathVariable String mento){
        return ResponseEntity.ok("탈퇴한 팀장 아이디로 멘토 정보를 없앱니다.");
    }

    @PostMapping("menti/{teamId}")
    public ResponseEntity<String> executeMentiApplicate(@PathVariable Long teamId){
        return ResponseEntity.ok("멘티 신청이 완료 되었습니다.");
    }

    @DeleteMapping("menti/{mentiId}")
    public ResponseEntity<String> executeMentiApplicateReleasing(@PathVariable Long mentiId){
        return ResponseEntity.ok("멘티 신청이 취소 되었습니다.");
    }

    @DeleteMapping("menti/{userId}")
    public ResponseEntity<String> executeRemovingByTeamMenti(@PathVariable String userId){
        return ResponseEntity.ok("탈퇴한 멘티 아이디로 멘티 신청 내역을 없앱니다.");
    }
}
