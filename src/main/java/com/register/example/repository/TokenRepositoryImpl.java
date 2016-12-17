package com.register.example.repository;

import com.register.example.entity.tokens.PersistentLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;

@Repository("tokenRepositoryDao")
@Transactional
@Slf4j
public class TokenRepositoryImpl implements PersistentTokenRepository {

    @Autowired
    CustomPersistentTokenRepository persistentTokenRepository;

    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        log.info("Creating Token for user : {}", token.getUsername());
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setUsername(token.getUsername());
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setLast_used(token.getDate());
        persistentTokenRepository.save(persistentLogin);
    }

    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        log.info("Fetch Token if any for seriesId : {}", seriesId);
        PersistentLogin persistentLogin = persistentTokenRepository.findOne(seriesId);

        return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(),
                persistentLogin.getToken(), persistentLogin.getLast_used());

    }

    @Override
    public void removeUserTokens(String username) {
        log.info("Removing Token if any for user : {}", username);
        persistentTokenRepository.deletePersistentLoginByUsername(username);

    }

    @Override
    public void updateToken(String seriesId, String tokenValue, Date lastUsed) {
        log.info("Updating Token for seriesId : {}", seriesId);
        PersistentLogin persistentLogin = persistentTokenRepository.findOne(seriesId);
        persistentLogin.setToken(tokenValue);
        persistentLogin.setLast_used(lastUsed);
        persistentTokenRepository.save(persistentLogin);
    }
}
