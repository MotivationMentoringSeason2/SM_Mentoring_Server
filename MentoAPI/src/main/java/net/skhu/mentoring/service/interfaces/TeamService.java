package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.TeamAdvertiseFile;
import net.skhu.mentoring.enumeration.ResultStatus;
import net.skhu.mentoring.model.MentoApplicationModel;
import net.skhu.mentoring.vo.AdminAppVO;
import net.skhu.mentoring.vo.CareerBriefVO;
import net.skhu.mentoring.vo.MentoVO;
import net.skhu.mentoring.vo.MentoringTokenVO;
import net.skhu.mentoring.vo.PersonVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeamService {
    MentoringTokenVO fetchCurrentMentoringToken(final String userId);
    MentoringTokenVO fetchAdminMentoringTokenByTeam(final Long teamId);
    List<MentoVO> fetchMentoListBySemesterId(final Long semesterId);
    List<MentoVO> fetchMentoListByStatus(final ResultStatus status);
    List<CareerBriefVO> fetchMentoBriefInfoByIdentity(final String mento);
    MentoVO fetchMentoInfoByTeamId(final Long teamId);
    PersonVO fetchMentoringTeamPersonByTeamId(final Long teamId);
    MentoApplicationModel fetchUpdateMentoApplicationModel(final String mento);
    List<AdminAppVO> fetchCurrentSemesterMentoApplication();
    TeamAdvertiseFile fetchTeamAdvertiseData(final Long advFileId);

    ResponseEntity<String> executeMentoApplicate(final MentoApplicationModel mentoApplicationModel, final MultipartFile advFile, final String mento) throws IOException;
    ResponseEntity<String> executeUpdateMentoApplicate(final MentoApplicationModel mentoApplicationModel, final MultipartFile advFile, final String mento) throws IOException;
    ResponseEntity<String> executeUpdateMentoStatus(final Long teamId, final ResultStatus status);
    ResponseEntity<String> executeCancelMentoApplicate(final String mento);
    ResponseEntity<String> executeRemoveMentoRegister(final String mento);
}
