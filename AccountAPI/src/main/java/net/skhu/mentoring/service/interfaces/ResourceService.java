package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Profile;
import net.skhu.mentoring.model.MentoringModel;
import net.skhu.mentoring.vo.AvailableTimeVO;
import net.skhu.mentoring.vo.BriefAccountVO;

import java.util.List;
import java.util.Map;

public interface ResourceService {
    boolean fetchExistAccount(final String identity);
    List<Department> fetchAllDepartments();
    List<AvailableTimeVO> fetchEachAvailableTimes(final String identity);
    Map<String, List<AvailableTimeVO>> fetchEachMentoringAvailableTimes(final MentoringModel mentoringModel);
    Profile fetchEachProfile(final String identity);
    String fetchAccountNameByIdentity(final String identity);
    BriefAccountVO fetchBriefAccountInfoByIdentity(final String identity);
}
