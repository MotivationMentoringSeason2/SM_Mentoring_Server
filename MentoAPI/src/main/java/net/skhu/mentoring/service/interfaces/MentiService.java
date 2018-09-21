package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.MentiApplicationModel;
import net.skhu.mentoring.vo.MentiAppVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface MentiService {
    List<MentiAppVO> fetchCurrentMentiAppInfo(final String userId);
    ResponseEntity<String> executeCreateMentiApplication(final MentiApplicationModel mentiApplicationModel);
    ResponseEntity<String> executeRemoveMentiApplication(final MentiApplicationModel mentiApplicationModel);
    ResponseEntity<String> executeRemoveByMentiUser(final String userId);
}
