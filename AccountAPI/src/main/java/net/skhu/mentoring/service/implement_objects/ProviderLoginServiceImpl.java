package net.skhu.mentoring.service.implement_objects;

import net.skhu.mentoring.domain.Account;
import net.skhu.mentoring.exception.CustomException;
import net.skhu.mentoring.repository.AccountRepository;
import net.skhu.mentoring.service.interfaces.ProviderLoginService;
import net.skhu.mentoring.util.Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProviderLoginServiceImpl implements ProviderLoginService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account provideLogin(final String identity, final String password) {
        if(identity == null || identity.isEmpty())
            throw new CustomException("ID의 값을 입력하지 않았습니다.", HttpStatus.NO_CONTENT);
        Optional<Account> account = accountRepository.findByIdentity(identity);
        if(!account.isPresent())
            throw new CustomException("존재하지 않는 사용자의 ID를 입력하였습니다.", HttpStatus.NO_CONTENT);
        else {
            String encryptPassword = Encryption.encrypt(password, Encryption.MD5);
            Account tmpAccount = account.get();
            if(!tmpAccount.getPassword().equals(encryptPassword))
                throw new CustomException("사용자의 비밀번호가 일치하지 않습니다.", HttpStatus.NO_CONTENT);
            else
                return tmpAccount;
        }
    }
}
