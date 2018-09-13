package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Intro;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntroVO {
    private long id;
    private String context;
    private String writer;

    public static IntroVO builtToVO(Intro intro){
        return new IntroVO(intro.getId(), intro.getContext(), intro.getWriter());
    }
}
