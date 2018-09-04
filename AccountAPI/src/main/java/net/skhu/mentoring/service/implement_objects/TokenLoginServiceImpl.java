package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.component.JwtTokenProvider;
import net.skhu.mentoring.component.PrincipalGenerator;
import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.exception.CustomException;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.service.interfaces.TokenLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenLoginServiceImpl implements TokenLoginService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PrincipalGenerator principalGenerator;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public String tokenLogin(final String identity, final String password) throws CustomException {
        try {
            Optional<Account> account = accountRepository.findByIdentity(identity);
            if (account != null) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(identity, password));
                return jwtTokenProvider.createToken(identity, principalGenerator.fetchRoleWithAccount(account.get()), principalGenerator.fetchPrincipalVOWithAccount(account.get()));
            } else throw new UsernameNotFoundException("존재하지 않은 아이디를 입력하셨습니다.");
        } catch (AuthenticationException e) {
            throw new CustomException(e.getMessage(), HttpStatus.NON_AUTHORITATIVE_INFORMATION);
        }
    }
}
