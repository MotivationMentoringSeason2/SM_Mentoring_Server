package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.domain.AvailableTime;
import net.skhu.mentoring.domain.Department;
import net.skhu.mentoring.domain.Profile;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.repository.AvailableTimeRepository;
import net.skhu.mentoring.repository.DepartmentRepository;
import net.skhu.mentoring.repository.ProfileRepository;
import net.skhu.mentoring.service.interfaces.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private AvailableTimeRepository availableTimeRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Override
    public boolean fetchExistAccount(final String identity) {
        return accountRepository.existsByIdentity(identity);
    }

    @Override
    public List<Department> fetchAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public List<AvailableTime> fetchEachAvailableTimes(final String identity) {
        Optional<Account> account = accountRepository.findByIdentity(identity);
        if (account.isPresent()) {
            return availableTimeRepository.findByAccount(account.get());
        } else return null;
    }

    @Override
    public Profile fetchEachProfile(String identity) {
        Optional<Account> account = accountRepository.findByIdentity(identity);
        if (account.isPresent()) {
            Optional<Profile> profile = profileRepository.findByAccount(account.get());
            return profile.isPresent() ? profile.get() : null;
        } else return null;
    }
}
