package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Profile;
import net.skhu.mentoring.vo.AvailableTimeVO;

import java.util.List;

public interface ResourceService {
    boolean fetchExistAccount(final String identity);
    List<Department> fetchAllDepartments();
    List<AvailableTimeVO> fetchEachAvailableTimes(final String identity);
    Profile fetchEachProfile(final String identity);
    String fetchAccountNameByIdentity(final String identity);
}
