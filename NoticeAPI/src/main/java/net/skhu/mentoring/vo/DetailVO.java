package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Detail;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVO {
    private long id;
    private String context;
    private String writer;

    public static DetailVO builtToVO(Detail detail){
        return new DetailVO(detail.getId(), detail.getContext(), detail.getWriter());
    }
}
