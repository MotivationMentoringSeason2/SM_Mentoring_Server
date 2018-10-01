package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminAppVO {
    private MentoVO mentoVO;
    private PersonVO personVO;
    public static AdminAppVO builtToVO(MentoVO mentoVO, PersonVO personVO){
        return new AdminAppVO(mentoVO, personVO);
    }
}
