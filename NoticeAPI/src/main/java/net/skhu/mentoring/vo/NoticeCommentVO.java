package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Comment;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeCommentVO {
    private Long id;
    private String context;
    private String writer;
    private LocalDateTime writtenDate;
    public static NoticeCommentVO builtToVO(Comment comment){
        return new NoticeCommentVO(comment.getId(), comment.getContext(), comment.getWriter(), comment.getWrittenDate());
    }
}
