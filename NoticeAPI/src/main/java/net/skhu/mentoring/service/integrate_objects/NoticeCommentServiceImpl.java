package net.skhu.mentoring.service.integrate_objects;

import net.skhu.mentoring.domain.Comment;
import net.skhu.mentoring.domain.Post;
import net.skhu.mentoring.model.CommentModel;
import net.skhu.mentoring.repository.CommentRepository;
import net.skhu.mentoring.repository.PostRepository;
import net.skhu.mentoring.service.interfaces.NoticeCommentService;
import net.skhu.mentoring.vo.NoticeCommentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoticeCommentServiceImpl implements NoticeCommentService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<NoticeCommentVO> fetchCommentListByPostId(final Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(comment -> NoticeCommentVO.builtToVO(comment))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeCreatingComment(final String userId, final CommentModel commentModel) {
        Optional<Post> post = postRepository.findById(commentModel.getPostId());
        if(post.isPresent()){
            Comment comment = new Comment();
            comment.setId(0L);
            comment.setPost(post.get());
            comment.setContext(commentModel.getContext());
            comment.setWriter(userId);
            comment.setWrittenDate(LocalDateTime.now());
            commentRepository.save(comment);
            return ResponseEntity.ok("현재 게시물에 댓글이 저장 되었습니다.");
        } else return new ResponseEntity<>("게시물이 존재하지 않아 댓글이 저장되지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeUpdatingComment(final Long commentId, final CommentModel commentModel) {
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent()){
            Comment updateComment = comment.get();
            updateComment.setContext(commentModel.getContext());
            updateComment.setWrittenDate(LocalDateTime.now());
            commentRepository.save(updateComment);
            return ResponseEntity.ok("선택하신 댓글의 내용이 수정 되었습니다.");
        } else return new ResponseEntity<>("선택하신 댓글이 존재하지 않아 댓글이 수정되지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemovingComment(final Long commentId) {
        if(commentRepository.existsById(commentId)){
            commentRepository.deleteById(commentId);
            return ResponseEntity.ok("선택하신 댓글이 삭제 되었습니다.");
        } else return new ResponseEntity<>("선택하신 댓글이 존재하지 않아 댓글이 삭제되지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemovingMultipleComments(final List<Long> ids) {
        if(commentRepository.existsByIdIn(ids)){
            commentRepository.deleteByIdIn(ids);
            return ResponseEntity.ok("선택하신 댓글들이 삭제 되었습니다.");
        } else return new ResponseEntity<>("선택하신 댓글들이 존재하지 않아 댓글이 삭제되지 않았습니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
    }

    @Override
    @Transactional
    public ResponseEntity<String> executeRemovingByUserId(final String userId) {
        if(commentRepository.existsByWriter(userId)){
            commentRepository.deleteByWriter(userId);
            return ResponseEntity.ok(String.format("삭제 하려는 회원 %s의 댓글이 모두 삭제 되었습니다.", userId));
        } else return ResponseEntity.ok(String.format("삭제 하려는 회원 %s의 댓글이 존재하지 않습니다.", userId));
    }
}
