package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Detail;
import net.skhu.mentoring.domain.Intro;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IntroduceVO {
    private long introId;
    private String introContext;
    private List<String> detailContext;
    public static IntroduceVO builtToVO(Intro intro, List<Detail> details){
        return new IntroduceVO(intro.getId(), intro.getContext(), details.stream().map(Detail::getContext).collect(Collectors.toList()));
    }
}
