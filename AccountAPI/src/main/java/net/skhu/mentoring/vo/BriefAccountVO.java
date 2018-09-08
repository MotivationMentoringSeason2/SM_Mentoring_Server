package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.domain.Account;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BriefAccountVO {
    private Long id;
    private String identity;
    private String name;
    private String type;
    private String department;

    public static BriefAccountVO builtToAccountVO(Account account, String type, String department){
        return new BriefAccountVO(account.getId(), account.getIdentity(), account.getName(), type, department);
    }
}
