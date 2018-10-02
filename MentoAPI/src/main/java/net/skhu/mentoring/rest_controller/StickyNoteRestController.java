package net.skhu.mentoring.rest_controller;
import net.skhu.mentoring.domain.StickyNote;
import net.skhu.mentoring.service.interfaces.StickyNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("MentoAPI")
public class StickyNoteRestController {

    @Autowired
    private StickyNoteService stickyNoteService;

    @GetMapping("stickyNote")
    public ResponseEntity<List<StickyNote>> fetchStickyNoteList(){
        return ResponseEntity.ok(stickyNoteService.fetchAllStickyNote());
    }

    @GetMapping("stickyNote/{teamId}")
    public ResponseEntity<List<StickyNote>> fetchStickyNoteListByTeamId(@PathVariable Long teamId){
        return ResponseEntity.ok(stickyNoteService.fetchAllStickyNoteByTeamId(teamId));
    }

}
