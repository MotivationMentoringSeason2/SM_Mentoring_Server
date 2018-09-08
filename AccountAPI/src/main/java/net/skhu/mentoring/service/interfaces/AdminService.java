package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.AccountPagination;
import net.skhu.mentoring.vo.BriefAccountVO;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

public interface AdminService {
    List<BriefAccountVO> fetchAccountListWithPagination(final Principal principal, final HttpServletRequest request, final AccountPagination accountPagination);
}
