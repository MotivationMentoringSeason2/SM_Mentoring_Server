package net.skhu.mentoring.service.interfaces;


import net.skhu.mentoring.exception.CustomException;

public interface TokenLoginService {
    String tokenLogin(final String identity, final String password) throws CustomException;
}
