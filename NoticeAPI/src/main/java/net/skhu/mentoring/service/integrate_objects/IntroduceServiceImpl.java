package net.skhu.mentoring.service.integrate_objects;

import net.skhu.mentoring.domain.Detail;
import net.skhu.mentoring.domain.Intro;
import net.skhu.mentoring.model.DetailModel;
import net.skhu.mentoring.model.IntroModel;
import net.skhu.mentoring.repository.DetailRepository;
import net.skhu.mentoring.repository.IntroRepository;
import net.skhu.mentoring.service.interfaces.IntroduceService;
import net.skhu.mentoring.vo.DetailVO;
import net.skhu.mentoring.vo.IntroVO;
import net.skhu.mentoring.vo.IntroduceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IntroduceServiceImpl implements IntroduceService {
    @Autowired
    private IntroRepository introRepository;

    @Autowired
    private DetailRepository detailRepository;

    @Override
    public List<IntroduceVO> fetchIntroduceList() {
        List<Intro> intros = introRepository.findAll();
        return intros.stream()
                .map(intro -> IntroduceVO.builtToVO(intro, detailRepository.findByIntro(intro)))
                .collect(Collectors.toList());
    }

    @Override
    public List<IntroVO> fetchIntroList() {
        return introRepository.findAll().stream()
            .map(intro -> IntroVO.builtToVO(intro))
            .collect(Collectors.toList());
    }

    @Override
    public IntroVO fetchByIntroId(final Long id) {
        Optional<Intro> intro = introRepository.findById(id);
        if(intro.isPresent()) return IntroVO.builtToVO(intro.get());
        else return null;
    }

    @Override
    public ResponseEntity<String> executeCreatingIntro(final String userId, final IntroModel introModel) {
        Intro createIntro = new Intro();
        createIntro.setId(0L);
        createIntro.setWriter(userId);
        createIntro.setContext(introModel.getContext());
        introRepository.save(createIntro);
        return ResponseEntity.ok("새로운 제목이 저장되었습니다.");
    }

    @Override
    public ResponseEntity<String> executeUpdatingIntro(final String userId, final IntroModel introModel) {
        Optional<Intro> intro = introRepository.findById(introModel.getIntroId());
        if(intro.isPresent()){
            Intro updateIntro = intro.get();
            updateIntro.setWriter(userId);
            updateIntro.setContext(introModel.getContext());
            introRepository.save(updateIntro);
            return ResponseEntity.ok("선택하신 제목 수정 작업이 완료 되었습니다.");
        } else return new ResponseEntity<>("선택하신 제목이 없어서 수정이 진행되지 않았습니다.", HttpStatus.NOT_MODIFIED);
    }

    @Override
    public ResponseEntity<String> executeRemovingIntros(final List<Long> introIds) {
        introRepository.deleteByIdIn(introIds);
        if(!introRepository.existsByIdIn(introIds))
            return ResponseEntity.ok("선택하신 제목의 내용 일부가 삭제되었습니다.");
        else return new ResponseEntity<>("선택하신 제목의 내용 삭제가 완료되지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    public List<DetailVO> fetchDetailList(final Long introId) {
        Optional<Intro> intro = introRepository.findById(introId);
        if(intro.isPresent()) {
            return detailRepository.findByIntro(intro.get()).stream()
                .map(detail -> DetailVO.builtToVO(detail))
                .collect(Collectors.toList());
        } else return null;
    }

    @Override
    public ResponseEntity<String> executeCreatingDetail(final Long introId, final String userId, final DetailModel detailModel) {
        Optional<Intro> intro = introRepository.findById(introId);
        if(intro.isPresent()) {
            Detail detail = new Detail();
            detail.setId(0L);
            detail.setIntro(intro.get());
            detail.setWriter(userId);
            detail.setContext(detailModel.getContext());
            detailRepository.save(detail);
            return ResponseEntity.ok("새로운 세부 사항이 저장되었습니다.");
        } else {
            return new ResponseEntity<>("세부 사항을 저장하기 위한 제목이 존재하지 않습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }

    @Override
    public ResponseEntity<String> executeUpdatingDetail(final String userId, final DetailModel detailModel) {
        if(detailRepository.existsById(detailModel.getDetailId())){
            Detail detail = detailRepository.findById(detailModel.getDetailId()).get();
            detail.setContext(detailModel.getContext());
            detail.setWriter(userId);
            detailRepository.save(detail);
            return ResponseEntity.ok("선택하신 세부 사항 수정 작업이 완료 되었습니다.");
        } else return new ResponseEntity<>("선택하신 세부 사항이 없어서 수정이 진행되지 않았습니다.", HttpStatus.NOT_MODIFIED);
    }

    @Override
    public ResponseEntity<String> executeRemovingDetails(final List<Long> detailIds) {
        detailRepository.deleteByIdIn(detailIds);
        if(!detailRepository.existsByIdIn(detailIds))
            return ResponseEntity.ok("선택하신 세부 사항의 내용 일부가 삭제되었습니다.");
        else return new ResponseEntity<>("선택하신 세부 사항의 내용 삭제가 완료되지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }
}
