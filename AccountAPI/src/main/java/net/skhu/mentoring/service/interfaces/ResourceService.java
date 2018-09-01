package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.AvailableTime;
import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Profile;

import java.util.List;

public interface ResourceService {
    boolean fetchExistAccount(final String identity);
    List<Department> fetchAllDepartments();
    List<AvailableTime> fetchEachAvailableTimes(final String identity);
    Profile fetchEachProfile(final String identity);
}
