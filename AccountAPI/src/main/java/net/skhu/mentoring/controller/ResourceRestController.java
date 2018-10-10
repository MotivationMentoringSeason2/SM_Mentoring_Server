package net.skhu.mentoring.controller;

import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Profile;
import net.skhu.mentoring.model.MentoringModel;
import net.skhu.mentoring.service.interfaces.ResourceService;
import net.skhu.mentoring.vo.AvailableTimeVO;
import net.skhu.mentoring.vo.BriefAccountVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/AccountAPI/resource")
public class ResourceRestController {
    @Autowired
    private ResourceService resourceService;

    private HttpHeaders generateImageHeader(Profile profile) {
        HttpHeaders header = new HttpHeaders();
        switch (profile.getFileSuffix()) {
            case JPG:
            case JPEG:
                header.setContentType(MediaType.IMAGE_JPEG);
                break;
            case PNG:
                header.setContentType(MediaType.IMAGE_PNG);
                break;
            case GIF:
                header.setContentType(MediaType.IMAGE_GIF);
                break;
        }
        return header;
    }

    @GetMapping("exist_account/{identity}")
    public ResponseEntity<Boolean> fetchExistAccount(@PathVariable String identity) {
        return ResponseEntity.ok(resourceService.fetchExistAccount(identity));
    }

    @GetMapping("departments")
    public ResponseEntity<?> fetchAllDepartments() {
        List<Department> departments = resourceService.fetchAllDepartments();
        if (departments.size() > 0)
            return ResponseEntity.ok(departments);
        else return ResponseEntity.noContent().build();
    }

    @GetMapping("available_times/{identity}")
    public ResponseEntity<?> fetchEachAvailableTimes(@PathVariable String identity) {
        List<AvailableTimeVO> availableTimes = resourceService.fetchEachAvailableTimes(identity);
        if (availableTimes != null)
            return ResponseEntity.ok(availableTimes);
        else return ResponseEntity.noContent().build();
    }

    @PostMapping("available_times")
    public ResponseEntity<?> fetchMentoringAvailableTimes(@RequestBody MentoringModel mentoringModel){
        return ResponseEntity.ok(resourceService.fetchEachMentoringAvailableTimes(mentoringModel));
    }

    @GetMapping("profile/{identity}")
    public ResponseEntity<?> fetchEachProfile(@PathVariable String identity) {
        Profile profile = resourceService.fetchEachProfile(identity);
        if (profile != null) {
            HttpHeaders headers = this.generateImageHeader(profile);
            return new ResponseEntity<>(profile.getFileData(), headers, HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("account/name/{identity}")
    public ResponseEntity<String> fetchAccountNameByIdentity(@PathVariable String identity){
        String result = resourceService.fetchAccountNameByIdentity(identity);
        if(result != null) return ResponseEntity.ok(result);
        else return ResponseEntity.noContent().build();
    }

    @GetMapping("account/brief/{identity}")
    public ResponseEntity<BriefAccountVO> fetchBriefAccountInfo(@PathVariable String identity){
        BriefAccountVO accountVO = resourceService.fetchBriefAccountInfoByIdentity(identity);
        if(accountVO != null) return ResponseEntity.ok(accountVO);
        else return ResponseEntity.noContent().build();
    }

    @PostMapping("account/brief")
    public ResponseEntity<?> fetchBriefAccountInfoAboutMentoring(@RequestBody MentoringModel mentoringModel){
        List<BriefAccountVO> briefAccountVOs = new ArrayList<BriefAccountVO>();
        BriefAccountVO accountVO = resourceService.fetchBriefAccountInfoByIdentity(mentoringModel.getMento());
        if(accountVO != null) briefAccountVOs.add(accountVO);
        for(String menti : mentoringModel.getMentis()){
            BriefAccountVO tmpVO = resourceService.fetchBriefAccountInfoByIdentity(menti);
            if(tmpVO != null) briefAccountVOs.add(tmpVO);
        }
        return ResponseEntity.ok(briefAccountVOs);
    }
}
