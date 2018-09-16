package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.CommentModel;
import net.skhu.mentoring.vo.NoticeCommentVO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NoticeCommentService {
    List<NoticeCommentVO> fetchCommentListByPostId(final Long postId);
    ResponseEntity<String> executeCreatingComment(final String userId, final CommentModel commentModel);
    ResponseEntity<String> executeUpdatingComment(final Long commentId, final CommentModel commentModel);
    ResponseEntity<String> executeRemovingComment(final Long commentId);
    ResponseEntity<String> executeRemovingMultipleComments(final List<Long> ids);
    ResponseEntity<String> executeRemovingByUserId(final String userId);
}
