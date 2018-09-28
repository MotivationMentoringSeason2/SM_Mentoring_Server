package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.MentiApplicationModel;
import net.skhu.mentoring.vo.CareerBriefVO;
import net.skhu.mentoring.vo.MentiAppVO;
import net.skhu.mentoring.vo.MentoringTokenVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MentiService {
    MentoringTokenVO fetchCurrentMentoringToken(final String userId);
    List<MentiAppVO> fetchCurrentMentiAppInfo(final String userId);
    List<CareerBriefVO> fetchMentiCareerList(final String userId);
    ResponseEntity<String> executeCreateMentiApplication(final MentiApplicationModel mentiApplicationModel);
    ResponseEntity<String> executeRemoveMentiApplication(final MentiApplicationModel mentiApplicationModel);
    ResponseEntity<String> executeRemoveByMentiUser(final String userId);
}
