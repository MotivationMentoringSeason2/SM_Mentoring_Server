package net.skhu.mentoring.rest_controller;

import net.skhu.mentoring.model.CommentModel;
import net.skhu.mentoring.service.interfaces.NoticeCommentService;
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
@RequestMapping("/NoticeAPI/notice/comment")
public class CommentRestController {
    @Autowired
    private NoticeCommentService noticeCommentService;

    @GetMapping("view/{postId}")
    public ResponseEntity<?> fetchByNoticePostId(@PathVariable Long postId){
        return ResponseEntity.ok(noticeCommentService.fetchCommentListByPostId(postId));
    }

    @PostMapping("create/{userId}")
    public ResponseEntity<String> executeCreatingNoticeComment(@PathVariable String userId, @RequestBody CommentModel commentModel){
        return noticeCommentService.executeCreatingComment(userId, commentModel);
    }

    @PutMapping("update/{commentId}")
    public ResponseEntity<String> executeUpdatingNoticeComment(@PathVariable Long commentId, @RequestBody CommentModel commentModel){
        return noticeCommentService.executeUpdatingComment(commentId, commentModel);
    }

    @DeleteMapping("remove/{commentId}")
    public ResponseEntity<String> executeRemovingNoticeComment(@PathVariable Long commentId){
        return noticeCommentService.executeRemovingComment(commentId);
    }

    @DeleteMapping("remove/multiple")
    public ResponseEntity<String> executeRemovingNoticeCommentMultiple(@RequestBody List<Long> ids){
        return noticeCommentService.executeRemovingMultipleComments(ids);
    }

    @DeleteMapping("remove/user/{userId}")
    public ResponseEntity<String> executeRemovingNoticeCommentByUserId(@PathVariable String userId){
        return noticeCommentService.executeRemovingByUserId(userId);
    }
}
