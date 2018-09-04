package net.skhu.mentoring.component;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.service.interfaces.ProviderLoginService;
import net.skhu.mentoring.vo.PrincipalVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SecurityAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private PrincipalGenerator principalGenerator;

    @Autowired
    private ProviderLoginService providerLoginService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String identity = authentication.getName();
        String passwd = authentication.getCredentials().toString();
        return this.authenticate(identity, passwd);
    }

    private Authentication authenticate(String identity, String password) throws AuthenticationException {
        Account account = providerLoginService.provideLogin(identity, password);
        if (account == null) return null;

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        grantedAuthorities.add(new SimpleGrantedAuthority(principalGenerator.fetchRoleWithAccount(account)));

        PrincipalVO principalVO = principalGenerator.fetchPrincipalVOWithAccount(account);
        return new CheckedAuthentication(identity, password, grantedAuthorities, principalVO);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    public class CheckedAuthentication extends UsernamePasswordAuthenticationToken {
        private static final long serialVersionUID = 1L;
        private PrincipalVO principalVO;

        public CheckedAuthentication(String loginId, String password, List<GrantedAuthority> grantedAuthorities, PrincipalVO principalVO) {
            super(loginId, password, grantedAuthorities);
            this.principalVO = principalVO;
        }

        public PrincipalVO getPrincipalVO() {
            return this.principalVO;
        }

        public void setPrincipalVO(PrincipalVO principalVO) {
            this.principalVO = principalVO;
        }
    }
}
