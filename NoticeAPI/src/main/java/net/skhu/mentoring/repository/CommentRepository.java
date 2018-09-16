package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Long postId);
    boolean existsByWriter(String writer);
    boolean existsByIdIn(List<Long> ids);
    void deleteByWriter(String writer);
    void deleteByIdIn(List<Long> id);
}
