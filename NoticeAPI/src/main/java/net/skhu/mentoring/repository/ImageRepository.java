package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Image;
import net.skhu.mentoring.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long postId);
    Optional<Image> findByFileNameAndPost(String fileName, Post post);
    void deleteByIdIn(List<Long> id);
}
