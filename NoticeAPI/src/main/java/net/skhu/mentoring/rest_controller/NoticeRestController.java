package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.domain.Type;
import net.skhu.mentoring.model.NoticePagination;
import net.skhu.mentoring.model.OptionModel;
import net.skhu.mentoring.model.PostModel;
import net.skhu.mentoring.service.interfaces.NoticeCommentService;
import net.skhu.mentoring.service.interfaces.NoticeFileService;
import net.skhu.mentoring.service.interfaces.NoticeIntegrateService;
import net.skhu.mentoring.vo.NoticePostBriefVO;
import net.skhu.mentoring.vo.NoticePostMainVO;
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
@RequestMapping("/NoticeAPI/notice")
public class NoticeRestController {
    @Autowired
    private NoticeIntegrateService noticeIntegrateService;

    @GetMapping("types")
    public ResponseEntity<List<Type>> fetchNoticeTypes(){
        return ResponseEntity.ok(noticeIntegrateService.fetchNoticeTypes());
    }

    @GetMapping("type/{typeId}")
    public ResponseEntity<Type> fetchNoticeTypeSingle(@PathVariable Long typeId){
        Type type = noticeIntegrateService.fetchTypeById(typeId);
        if(type != null) return ResponseEntity.ok(type);
        else return ResponseEntity.noContent().build();
    }

    @GetMapping("options/sb_elements")
    public ResponseEntity<List<OptionModel>> fetchPostSearchByOptions(){
        return ResponseEntity.ok(noticeIntegrateService.fetchSearchBy());
    }

    @GetMapping("options/ob_elements")
    public ResponseEntity<List<OptionModel>> fetchPostOrderByOptions(){
        return ResponseEntity.ok(noticeIntegrateService.fetchOrderBy());
    }

    @GetMapping("posts")
    public ResponseEntity<List<NoticePostBriefVO>> fetchPostListByPaginationModel(NoticePagination noticePagination){
        return ResponseEntity.ok(noticeIntegrateService.fetchByPaginationModel(noticePagination));
    }

    @GetMapping("post/{postId}")
    public ResponseEntity<NoticePostMainVO> fetchPostViewByPostId(@PathVariable Long postId){
        NoticePostMainVO postVO = noticeIntegrateService.fetchPostById(postId);
        if(postVO != null) return ResponseEntity.ok(postVO);
        else return ResponseEntity.noContent().build();
    }

    @PostMapping("post/{userId}")
    public ResponseEntity<String> executeCreatingPost(@PathVariable String userId, @RequestBody PostModel postModel){
        return noticeIntegrateService.executeCreatingPost(postModel, userId);
    }

    @PutMapping("post/{postId}")
    public ResponseEntity<String> executeUpdatingPost(@PathVariable Long postId, @PathVariable String userId, @RequestBody PostModel postModel){
        return noticeIntegrateService.executeUpdatingPost(postId, postModel);
    }

    @DeleteMapping("post/{postId}")
    public ResponseEntity<String> executeRemovingPost(@PathVariable Long postId){
        return noticeIntegrateService.executeRemovingPost(postId);
    }

    @DeleteMapping("posts")
    public ResponseEntity<String> executeRemovingPostMultiple(@RequestBody List<Long> ids){
        return noticeIntegrateService.executeRemovingMultiplePosts(ids);
    }

    @DeleteMapping("posts/{userId}")
    public ResponseEntity<String> executeRemovingPostByWriter(@PathVariable String userId){
        return noticeIntegrateService.executeRemovingByUserId(userId);
    }
}
