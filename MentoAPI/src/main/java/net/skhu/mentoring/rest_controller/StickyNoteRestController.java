package net.skhu.mentoring.rest_controller;
import net.skhu.mentoring.domain.StickyNote;
import net.skhu.mentoring.model.StickyNoteModel;
import net.skhu.mentoring.service.interfaces.StickyNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","http://182.209.240.203:81"})
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

    @GetMapping("stickyNote/identity/{identity}")
    public ResponseEntity<StickyNoteModel> fetchStickyNoteListByTeamId(@PathVariable String identity){
        return ResponseEntity.ok(stickyNoteService.findByTeamId(identity));
    }
    @PostMapping("stickyNote/create")
    public ResponseEntity<String> executeScheduleCreating(@RequestBody StickyNoteModel stickyNoteModel){
        System.out.print(stickyNoteModel);
        return stickyNoteService.createMemo(stickyNoteModel);
    }
}
