package net.skhu.mentoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.skhu.mentoring.model.AccountPagination;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindResultVO {
    private List<BriefAccountVO> accounts;
    private AccountPagination accountPagination;

    public static FindResultVO builtToVO(List<BriefAccountVO> accounts, AccountPagination accountPagination){
        return new FindResultVO(accounts, accountPagination);
    }
}
