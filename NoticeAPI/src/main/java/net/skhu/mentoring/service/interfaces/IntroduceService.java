package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.DetailModel;
import net.skhu.mentoring.model.IntroModel;
import net.skhu.mentoring.vo.DetailVO;
import net.skhu.mentoring.vo.IntroVO;
import net.skhu.mentoring.vo.IntroduceVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IntroduceService {
    List<IntroduceVO> fetchIntroduceList();

    List<IntroVO> fetchIntroList();
    IntroVO fetchByIntroId(final Long id);
    ResponseEntity<String> executeCreatingIntro(final String userId, final IntroModel introModel);
    ResponseEntity<String> executeUpdatingIntro(final String userId, final IntroModel introModel);
    ResponseEntity<String> executeRemovingIntros(final List<Long> introIds);

    List<DetailVO> fetchDetailList(final Long introId);
    ResponseEntity<String> executeCreatingDetail(final Long introId, final String userId, final DetailModel detailModel);
    ResponseEntity<String> executeUpdatingDetail(final String userId, final DetailModel detailModel);
    ResponseEntity<String> executeRemovingDetails(final List<Long> detailIds);
}
