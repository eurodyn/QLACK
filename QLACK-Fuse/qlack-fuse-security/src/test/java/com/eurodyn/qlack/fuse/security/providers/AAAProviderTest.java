package com.eurodyn.qlack.fuse.security.providers;

import static org.junit.Assert.assertTrue;

import com.eurodyn.qlack.fuse.aaa.util.Md5PasswordEncoder;
import com.eurodyn.qlack.fuse.security.config.AuthConfig;
import com.eurodyn.qlack.fuse.security.config.IntegrationTestConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfig.class, AuthConfig.class})
@SpringBootTest
public class AAAProviderTest {

    @Autowired
    private AAAUsernamePasswordProvider authenticationProvider;

    private UsernamePasswordAuthenticationToken bcryptValidToken;
    private UsernamePasswordAuthenticationToken bcryptInvalidToken;
    private UsernamePasswordAuthenticationToken aaaLegacyValidToken;
    private UsernamePasswordAuthenticationToken aaaLegacyInvalidToken;

    @Before
    public void setUp() {
        bcryptValidToken = new UsernamePasswordAuthenticationToken("bcrypt-user", "user");
        bcryptInvalidToken = new UsernamePasswordAuthenticationToken("bcrypt-user", "user2");
        aaaLegacyValidToken = new UsernamePasswordAuthenticationToken("aaa-legacy-user", "user");
        aaaLegacyInvalidToken = new UsernamePasswordAuthenticationToken("aaa-legacy-user", "user2");
    }

    @Test
    public void authenticateWithBCrypt_CorrectCredentials_ShouldPass() {
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        Authentication auth = authenticationProvider.authenticate(bcryptValidToken);

        assertTrue(auth.isAuthenticated());
    }

    @Test(expected = BadCredentialsException.class)
    public void authenticateWithBCryptPassEncoder_WrongCredentials_ShouldThrowException() {
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        authenticationProvider.authenticate(bcryptInvalidToken);
    }

    @Test
    public void authenticateWithAAALegacyPassEncoder_CorrectCredentials_ShouldPass() {
        authenticationProvider.setPasswordEncoder(new Md5PasswordEncoder());

        Authentication auth = authenticationProvider.authenticate(aaaLegacyValidToken);

        assertTrue(auth.isAuthenticated());
    }

    @Test(expected = BadCredentialsException.class)
    public void authenticateWithAAALegacyPassEncoder_WrongCredentials_ShouldThrowException() {
        authenticationProvider.setPasswordEncoder(new Md5PasswordEncoder());

        authenticationProvider.authenticate(aaaLegacyInvalidToken);
    }

}