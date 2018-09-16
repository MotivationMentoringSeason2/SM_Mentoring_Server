package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.File;
import net.skhu.mentoring.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByPostId(Long postId);
    Optional<File> findByFileNameAndPost(String fileName, Post post);
    boolean existsByPostId(Long postId);
    void deleteByPostId(Long postId);
    void deleteByIdIn(List<Long> id);
}
