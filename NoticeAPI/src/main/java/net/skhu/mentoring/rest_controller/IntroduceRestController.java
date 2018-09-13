package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.model.DetailModel;
import net.skhu.mentoring.model.IntroModel;
import net.skhu.mentoring.service.interfaces.IntroduceService;
import net.skhu.mentoring.vo.DetailVO;
import net.skhu.mentoring.vo.IntroVO;
import net.skhu.mentoring.vo.IntroduceVO;
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

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/NoticeAPI/introduce")
public class IntroduceRestController {
    @Autowired
    private IntroduceService introduceService;

    @GetMapping("view")
    public ResponseEntity<List<IntroduceVO>> fetchIntroduceView(){
        return ResponseEntity.ok(introduceService.fetchIntroduceList());
    }

    @GetMapping("intro/view")
    public ResponseEntity<?> fetchIntroList(){
        return ResponseEntity.ok(introduceService.fetchIntroList());
    }

    @GetMapping("intro/view/{introId}")
    public ResponseEntity<?> fetchIntroView(@PathVariable Long introId){
        IntroVO introVO = introduceService.fetchByIntroId(introId);
        if(introVO == null)
            return ResponseEntity.noContent().build();
        else return ResponseEntity.ok(introVO);
    }

    @PostMapping("intro/create/{userId}")
    public ResponseEntity<String> executeCreateIntro(@PathVariable String userId, @RequestBody IntroModel introModel){
        return introduceService.executeCreatingIntro(userId, introModel);
    }

    @PutMapping("intro/update/{userId}")
    public ResponseEntity<String> executeUpdateIntro(@PathVariable String userId, @RequestBody IntroModel introModel){
        return introduceService.executeUpdatingIntro(userId, introModel);
    }

    @DeleteMapping("intro/remove")
    public ResponseEntity<String> executeRemoveIntro(@RequestBody List<Long> introIds){
        return introduceService.executeRemovingIntros(introIds);
    }

    @GetMapping("detail/view/{introId}")
    public ResponseEntity<?> fetchDetailList(@PathVariable Long introId){
        List<DetailVO> detailVOs = introduceService.fetchDetailList(introId);
        if(detailVOs != null) return ResponseEntity.ok(detailVOs);
        else return ResponseEntity.noContent().build();
    }

    @PostMapping("detail/create/{introId}/{userId}")
    public ResponseEntity<String> executeCreateDetail(@PathVariable Long introId, @PathVariable String userId, @RequestBody DetailModel detailModel){
        return introduceService.executeCreatingDetail(introId, userId, detailModel);
    }

    @PutMapping("detail/update/{userId}")
    public ResponseEntity<String> executeUpdateDetail(@PathVariable String userId, @RequestBody DetailModel detailModel){
        return introduceService.executeUpdatingDetail(userId, detailModel);
    }

    @DeleteMapping("detail/remove")
    public ResponseEntity<String> executeRemoveDetails(@RequestBody List<Long> detailIds){
        return introduceService.executeRemovingDetails(detailIds);
    }
}
