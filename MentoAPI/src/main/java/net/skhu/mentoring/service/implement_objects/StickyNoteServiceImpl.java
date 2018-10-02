package net.skhu.mentoring.service.implement_objects;
import net.skhu.mentoring.domain.StickyNote;

import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.repository.StickyNoteRepository;
import net.skhu.mentoring.repository.TeamRepository;
import net.skhu.mentoring.service.interfaces.StickyNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class StickyNoteServiceImpl implements StickyNoteService {
    @Autowired
    private StickyNoteRepository stickyNoteRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Override
    public List<StickyNote> fetchAllStickyNote() {
        return stickyNoteRepository.findAll();
    }

    @Override
    public List<StickyNote> fetchAllStickyNoteByTeamId(long teamId) {
        Optional<Team> team = teamRepository.findById(teamId);

        if(team.isPresent()){
            return stickyNoteRepository.findByTeam(team.get());
        } else return null;
    }
}
