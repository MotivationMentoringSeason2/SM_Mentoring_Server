package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostModel {
    private long typeId;
    private String writer;
    private String title;
    private String context;

    public static PostModel builtToModel(Post post){
        return new PostModel(post.getType() != null ? post.getType().getId() : null, post.getWriter(), post.getTitle(), post.getContext());
    }
}
