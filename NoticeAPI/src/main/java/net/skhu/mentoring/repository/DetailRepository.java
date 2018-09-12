package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Detail;
import net.skhu.mentoring.domain.Intro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailRepository extends JpaRepository<Detail, Long> {
    List<Detail> findByIntro(Intro intro);
    boolean existsByIdIn(List<Long> id);
    void deleteByIdIn(List<Long> id);
}
