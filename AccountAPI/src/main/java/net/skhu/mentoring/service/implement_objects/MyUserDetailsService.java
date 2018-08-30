package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.component.PrincipalGenerator;
import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private PrincipalGenerator principalGenerator;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(final String identity) throws UsernameNotFoundException {
        Optional<Account> account = accountRepository.findByIdentity(identity);

        if (account == null) {
            throw new UsernameNotFoundException("User '" + identity + "' not found");
        }

        Account tmpAccount = account.get();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority(principalGenerator.fetchRoleWithAccount(tmpAccount)));

        return org.springframework.security.core.userdetails.User
                .withUsername(identity)
                .password(tmpAccount.getPassword())
                .authorities(grantedAuthorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}