package net.skhu.mentoring.service.implement_objects;
import net.skhu.mentoring.domain.Menti;
import net.skhu.mentoring.domain.StickyNote;

import net.skhu.mentoring.domain.Team;
import net.skhu.mentoring.model.StickyNoteModel;
import net.skhu.mentoring.repository.MentiRepository;
import net.skhu.mentoring.repository.StickyNoteRepository;
import net.skhu.mentoring.repository.TeamRepository;
import net.skhu.mentoring.service.interfaces.StickyNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StickyNoteServiceImpl implements StickyNoteService {
    @Autowired
    private StickyNoteRepository stickyNoteRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MentiRepository mentiRepository;


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




    @Override
    @Transactional
    public ResponseEntity<String> createMemo(StickyNoteModel stickyNoteModel) {
        Optional<Team> team = teamRepository.findById(stickyNoteModel.getTeamId());
        if(team.isPresent()) {
            StickyNote stickyNote = new StickyNote();
            stickyNote.setTeam(team.get());
            stickyNote.setWriter(stickyNoteModel.getWriter());
            stickyNote.setContext(stickyNoteModel.getContext());
            stickyNote.setWrittenDate(LocalDateTime.now());
            stickyNoteRepository.save(stickyNote);

            return ResponseEntity.ok("메모가 추가되었습니다.");
        }
        else {
            return new ResponseEntity<>("멘토 방이 없는 팀입니다.", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }

    }

    @Override
    public StickyNoteModel findByTeamId(String identity) {
        Optional<Menti> menti =mentiRepository.findByUserId(identity);
        Optional<Team> team =teamRepository.findByMento(identity);
        if(menti.isPresent()) {
            StickyNoteModel stickyNote = new StickyNoteModel();
            stickyNote.setTeamId(menti.get().getTeam().getId());
            return stickyNote;
        }
        else if(team.isPresent()){
            StickyNoteModel stickyNote = new StickyNoteModel();
            stickyNote.setTeamId(team.get().getId());
            return stickyNote;
        }
        else{
            StickyNoteModel stickyNote = new StickyNoteModel();
            stickyNote.setTeamId((long) 0);
            return stickyNote;
        }

    }
}
