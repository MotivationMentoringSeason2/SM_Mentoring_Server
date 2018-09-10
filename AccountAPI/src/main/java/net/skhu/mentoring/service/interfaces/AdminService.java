package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.model.AccountPagination;
import net.skhu.mentoring.model.OptionModel;
import net.skhu.mentoring.vo.BriefAccountVO;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

public interface AdminService {
    List<OptionModel> getSearchByModel(final Principal principal, final HttpServletRequest request);
    List<OptionModel> getOrderByModel(final Principal principal, final HttpServletRequest request);
    List<BriefAccountVO> fetchAccountListWithPagination(final Principal principal, final HttpServletRequest request, final AccountPagination accountPagination);
    ResponseEntity<?> fetchAccountView(final Principal principal, final HttpServletRequest request, final Long id);
    ResponseEntity<?> executeAppointChairman(final Principal principal, final HttpServletRequest request, final Long id);
    ResponseEntity<?> executeReleaseChairman(final Principal principal, final HttpServletRequest request, final Long id);
}
