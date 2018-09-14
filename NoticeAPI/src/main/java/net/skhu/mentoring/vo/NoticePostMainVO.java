package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Post;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticePostMainVO {
    private Long id;
    private String title;
    private String context;
    private String writer;
    private Integer views;
    private LocalDateTime writtenDate;
    public static NoticePostMainVO builtToVO(Post post){
        return new NoticePostMainVO(post.getId(), post.getTitle(), post.getContext(), post.getWriter(), post.getViews(), post.getWrittenDate());
    }
}
