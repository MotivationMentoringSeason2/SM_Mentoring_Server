package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.enumeration.ResultStatus;
import net.skhu.mentoring.model.MentoAppicationModel;
import net.skhu.mentoring.vo.MentoVO;
import net.skhu.mentoring.vo.PersonVO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TeamService {
    List<MentoVO> fetchMentoListBySemesterId(final Long semesterId);
    List<MentoVO> fetchMentoListByStatus(final ResultStatus status);
    MentoVO fetchMentoInfoByTeamId(final Long teamId);
    PersonVO fetchMentoringTeamPersonByTeamId(final Long teamId);

    ResponseEntity<String> executeMentoApplicate(final MentoAppicationModel mentoAppicationModel, final MultipartFile advFile, final String mento) throws IOException;
    ResponseEntity<String> executeUpdateMentoApplicate(final MentoAppicationModel mentoAppicationModel, final MultipartFile advFile, final String mento) throws IOException;
    ResponseEntity<String> executeUpdateMentoStatus(final Long teamId, final ResultStatus status);
    ResponseEntity<String> executeCancelMentoApplicate(final String mento);
    ResponseEntity<String> executeRemoveMentoRegister(final String mento);
}
