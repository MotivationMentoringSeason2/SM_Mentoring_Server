package net.skhu.mentoring.service.interfaces;

import net.skhu.mentoring.domain.Account;

public interface ProviderLoginService {
    Account provideLogin(final String identity, final String password);
}
