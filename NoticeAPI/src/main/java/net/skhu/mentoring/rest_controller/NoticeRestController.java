package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.domain.Type;
import net.skhu.mentoring.model.NoticePagination;
import net.skhu.mentoring.model.OptionModel;
import net.skhu.mentoring.model.PostModel;
import net.skhu.mentoring.service.interfaces.NoticeIntegrateService;
import net.skhu.mentoring.vo.NoticePostBriefVO;
import net.skhu.mentoring.vo.NoticePostMainVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("type/view")
    public ResponseEntity<List<Type>> fetchNoticeTypes(){
        return ResponseEntity.ok(noticeIntegrateService.fetchNoticeTypes());
    }

    @GetMapping("type/view/{typeId}")
    public ResponseEntity<Type> fetchNoticeTypeSingle(@PathVariable Long typeId){
        Type type = noticeIntegrateService.fetchTypeById(typeId);
        if(type != null) return ResponseEntity.ok(type);
        else return ResponseEntity.noContent().build();
    }

    @GetMapping("post/search_by")
    public ResponseEntity<List<OptionModel>> fetchPostSearchByOptions(){
        return ResponseEntity.ok(noticeIntegrateService.fetchSearchBy());
    }

    @GetMapping("post/order_by")
    public ResponseEntity<List<OptionModel>> fetchPostOrderByOptions(){
        return ResponseEntity.ok(noticeIntegrateService.fetchOrderBy());
    }

    @GetMapping("post/view")
    public ResponseEntity<List<NoticePostBriefVO>> fetchPostListByPaginationModel(NoticePagination noticePagination){
        return ResponseEntity.ok(noticeIntegrateService.fetchByPaginationModel(noticePagination));
    }

    @GetMapping("post/view/{postId}")
    public ResponseEntity<NoticePostMainVO> fetchPostViewByPostId(@PathVariable Long postId){
        NoticePostMainVO postVO = noticeIntegrateService.fetchPostById(postId);
        if(postVO != null) return ResponseEntity.ok(postVO);
        else return ResponseEntity.noContent().build();
    }

    @PostMapping("post/create/{userId}")
    public ResponseEntity<String> executeCreatePost(@PathVariable String userId, @RequestBody PostModel postModel){
        return noticeIntegrateService.executeCreatePost(postModel, userId);
    }
}
