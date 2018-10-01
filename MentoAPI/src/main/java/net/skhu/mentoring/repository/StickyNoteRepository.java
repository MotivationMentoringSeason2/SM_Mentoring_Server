package net.skhu.mentoring.repository;

import net.skhu.mentoring.domain.StickyNote;
import net.skhu.mentoring.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StickyNoteRepository extends JpaRepository<StickyNote, Long> {
    List<StickyNote> findByTeam(Team team);

}
