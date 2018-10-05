package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.StickyNote;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StickyNoteModel {
    private Long teamId;
    private String writer;
    private String context;

    public static StickyNoteModel builtToVO(StickyNote stickyNote){
        return new StickyNoteModel(stickyNote.getTeam().getId(), stickyNote.getWriter(),stickyNote.getContext());
    }

}
