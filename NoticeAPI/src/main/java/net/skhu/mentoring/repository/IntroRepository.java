package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.Intro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroRepository extends JpaRepository<Intro, Long> {
}
