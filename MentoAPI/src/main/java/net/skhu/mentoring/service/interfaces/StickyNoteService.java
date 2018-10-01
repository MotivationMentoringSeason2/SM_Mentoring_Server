package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.StickyNote;

import java.util.List;

public interface StickyNoteService {
    List<StickyNote> fetchAllStickyNote();
    List<StickyNote> fetchAllStickyNoteByTeamId(final long teamId);

}
