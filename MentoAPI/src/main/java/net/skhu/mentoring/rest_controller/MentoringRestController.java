package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.enumeration.ResultStatus;
import net.skhu.mentoring.model.MentiApplicationModel;
import net.skhu.mentoring.model.MentoApplicationModel;
import net.skhu.mentoring.service.interfaces.MentiService;
import net.skhu.mentoring.service.interfaces.TeamService;
import net.skhu.mentoring.vo.CareerBriefVO;
import net.skhu.mentoring.vo.MentiAppVO;
import net.skhu.mentoring.vo.MentoVO;
import net.skhu.mentoring.vo.MentoringTokenVO;
import net.skhu.mentoring.vo.PersonVO;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("MentoAPI/mentoring")
public class MentoringRestController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private MentiService mentiService;

    @GetMapping("teams/{semesterId}")
    public ResponseEntity<?> fetchTeamListBySemesterId(@PathVariable Long semesterId){
        List<MentoVO> mentoVOs = teamService.fetchMentoListBySemesterId(semesterId);
        return mentoVOs != null ? ResponseEntity.ok(mentoVOs) : ResponseEntity.noContent().build();
    }

    @GetMapping("teams/status/{status}")
    public ResponseEntity<List<MentoVO>> fetchMentoListByCurrentSemester(@PathVariable ResultStatus status){
        List<MentoVO> mentoVOs = teamService.fetchMentoListByStatus(status);
        return mentoVOs != null ? ResponseEntity.ok(mentoVOs) : ResponseEntity.noContent().build();
    }

    @GetMapping("teams/career/{mento}")
    public ResponseEntity<List<CareerBriefVO>> fetchBriefMentoListByIdentity(@PathVariable String mento){
        return ResponseEntity.ok(teamService.fetchMentoBriefInfoByIdentity(mento));
    }

    @GetMapping("team/{teamId}")
    public ResponseEntity<MentoVO> fetchTeamById(@PathVariable Long teamId){
        MentoVO mentoVO = teamService.fetchMentoInfoByTeamId(teamId);
        return mentoVO != null ? ResponseEntity.ok(mentoVO) : ResponseEntity.noContent().build();
    }

    @GetMapping("team/persons/{teamId}")
    public ResponseEntity<PersonVO> fetchTeamPersonById(@PathVariable Long teamId){
        return ResponseEntity.ok(teamService.fetchMentoringTeamPersonByTeamId(teamId));
    }

    @GetMapping("team/model/{mento}")
    public ResponseEntity<MentoApplicationModel> fetchUpdateMentoApplicationModel(@PathVariable String mento){
        MentoApplicationModel mentoApplicationModel = teamService.fetchUpdateMentoApplicationModel(mento);
        return mentoApplicationModel != null ? ResponseEntity.ok(mentoApplicationModel) : ResponseEntity.noContent().build();
    }

    @GetMapping("team/token/{userId}")
    public ResponseEntity<MentoringTokenVO> fetchCurrentMentoringTokenByMento(@PathVariable String userId){
        MentoringTokenVO mentoringTokenVO = teamService.fetchCurrentMentoringToken(userId);
        return mentoringTokenVO != null ? ResponseEntity.ok(mentoringTokenVO) : ResponseEntity.noContent().build();
    }

    @PostMapping(value = "team/{mento}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> executeMentoApplicate(@PathVariable String mento, @RequestPart("applicationModel") MentoApplicationModel mentoApplicationModel, @RequestPart("advFile") MultipartFile advFile) throws IOException {
        return teamService.executeMentoApplicate(mentoApplicationModel, advFile, mento);
    }

    @PutMapping(value = "team/info/{mento}")
    public ResponseEntity<String> executeMentoApplicateUpdatingOnlyInfos(@PathVariable String mento, @RequestBody MentoApplicationModel mentoApplicationModel) throws IOException {
        return teamService.executeUpdateMentoApplicate(mentoApplicationModel, null, mento);
    }

    @PutMapping(value = "team/{mento}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> executeMentoApplicateUpdatingWithFiles(@PathVariable String mento, @RequestPart("applicationModel") MentoApplicationModel mentoApplicationModel, @RequestPart("advFile") MultipartFile advFile) throws IOException{
        return teamService.executeUpdateMentoApplicate(mentoApplicationModel, advFile, mento);
    }

    @PutMapping("team/{teamId}/status/{status}")
    public ResponseEntity<String> executeMentoStatusUpdating(@PathVariable Long teamId, @PathVariable ResultStatus status){
        return teamService.executeUpdateMentoStatus(teamId, status);
    }

    @DeleteMapping("team/cancellation/{mento}")
    public ResponseEntity<String> executeCancelingCurrentSemester(@PathVariable String mento){
        return teamService.executeCancelMentoApplicate(mento);
    }

    @DeleteMapping("team/{mento}")
    public ResponseEntity<String> executeRemovingByTeamMento(@PathVariable String mento){
        return teamService.executeRemoveMentoRegister(mento);
    }

    @GetMapping("menti/infos/{userId}")
    public ResponseEntity<List<MentiAppVO>> fetchMentiAppInfos(@PathVariable String userId){
        return ResponseEntity.ok(mentiService.fetchCurrentMentiAppInfo(userId));
    }

    @GetMapping("mentis/career/{userId}")
    public ResponseEntity<List<CareerBriefVO>> fetchMentiCareerList(@PathVariable String userId){
        return ResponseEntity.ok(mentiService.fetchMentiCareerList(userId));
    }

    @GetMapping("menti/token/{userId}")
    public ResponseEntity<MentoringTokenVO> fetchCurrentMentoringTokenByMenti(@PathVariable String userId){
        MentoringTokenVO mentoringTokenVO = mentiService.fetchCurrentMentoringToken(userId);
        return mentoringTokenVO != null ? ResponseEntity.ok(mentoringTokenVO) : ResponseEntity.noContent().build();
    }

    @PostMapping("menti")
    public ResponseEntity<String> executeMentiApplicate(@RequestBody MentiApplicationModel mentiApplicationModel){
        return mentiService.executeCreateMentiApplication(mentiApplicationModel);
    }

    @DeleteMapping("menti")
    public ResponseEntity<String> executeMentiApplicateReleasing(@RequestBody MentiApplicationModel mentiApplicationModel){
        return mentiService.executeRemoveMentiApplication(mentiApplicationModel);
    }

    @DeleteMapping("menti/{userId}")
    public ResponseEntity<String> executeRemovingByTeamMenti(@PathVariable String userId){
        return mentiService.executeRemoveByMentiUser(userId);
    }
}
