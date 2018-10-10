package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.domain.Post;
import net.skhu.mentoring.domain.Type;
import net.skhu.mentoring.model.NoticePagination;
import net.skhu.mentoring.model.OptionModel;
import net.skhu.mentoring.model.PostModel;
import net.skhu.mentoring.service.interfaces.NoticeIntegrateService;
import net.skhu.mentoring.vo.NoticeListIntegrateVO;
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
    public ResponseEntity<NoticeListIntegrateVO> fetchPostListByPaginationModel(NoticePagination noticePagination){
        return ResponseEntity.ok(NoticeListIntegrateVO.buildToVO(noticeIntegrateService.fetchByPaginationModel(noticePagination), noticePagination));
    }

    @GetMapping("post/{postId}")
    public ResponseEntity<NoticePostMainVO> fetchPostViewByPostId(@PathVariable Long postId){
        NoticePostMainVO postVO = noticeIntegrateService.fetchPostById(postId);
        if(postVO != null) return ResponseEntity.ok(postVO);
        else return ResponseEntity.noContent().build();
    }

    @GetMapping("post/model/{postId}")
    public ResponseEntity<PostModel> fetchPostModelById(@PathVariable Long postId){
        PostModel postModel = noticeIntegrateService.fetchPostModelById(postId);
        if(postModel != null) return ResponseEntity.ok(postModel);
        else return ResponseEntity.noContent().build();
    }

    @PostMapping("post/{userId}")
    public ResponseEntity<?> executeCreatingPost(@PathVariable String userId, @RequestBody PostModel postModel){
        Post post = noticeIntegrateService.executeCreatingPost(postModel, userId);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.noContent().build();
    }

    @PutMapping("post/{postId}")
    public ResponseEntity<?> executeUpdatingPost(@PathVariable Long postId, @RequestBody PostModel postModel){
        Post post = noticeIntegrateService.executeUpdatingPost(postId, postModel);
        return post != null ? ResponseEntity.ok(post) : ResponseEntity.noContent().build();
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
