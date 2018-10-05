package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.StickyNote;
import net.skhu.mentoring.model.StickyNoteModel;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StickyNoteService {
    List<StickyNote> fetchAllStickyNote();
    List<StickyNote> fetchAllStickyNoteByTeamId(final long teamId);
    ResponseEntity<String> createMemo(final StickyNoteModel stickyNoteModel);
    StickyNoteModel findByTeamId(String identity);
}
