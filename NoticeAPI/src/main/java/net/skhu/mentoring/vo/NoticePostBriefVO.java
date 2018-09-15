package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticePostBriefVO {
    private Long id;
    private String title;
    private String writer;
    private int views;
    public static NoticePostBriefVO builtToVO(Post post){
        return new NoticePostBriefVO(post.getId(), post.getTitle(), post.getWriter(), post.getViews());
    }
}
