package com.eurodyn.qlack.fuse.security.manager;

import static org.junit.Assert.assertTrue;

import com.eurodyn.qlack.fuse.aaa.util.Md5PasswordEncoder;
import com.eurodyn.qlack.fuse.security.config.IntegrationTestConfig;
import java.util.function.Supplier;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.support.GenericWebApplicationContext;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {IntegrationTestConfig.class})
@SpringBootTest
public class AAAProviderTest {

    @Autowired
    private AAAProvider aaaProvider;

    @Autowired
    private GenericWebApplicationContext context;

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
    @DirtiesContext
    public void authenticateWithBCrypt_CorrectCredentials_ShouldPass() {
        context.registerBean(BCryptPasswordEncoder.class, (Supplier<BCryptPasswordEncoder>) BCryptPasswordEncoder::new);

        Authentication auth = aaaProvider.authenticate(bcryptValidToken);

        assertTrue(auth.isAuthenticated());
    }

    @Test(expected = BadCredentialsException.class)
    @DirtiesContext
    public void authenticateWithBCryptPassEncoder_WrongCredentials_ShouldThrowException() {
        context.registerBean(BCryptPasswordEncoder.class, (Supplier<BCryptPasswordEncoder>) BCryptPasswordEncoder::new);

        aaaProvider.authenticate(bcryptInvalidToken);
    }

    @Test
    @DirtiesContext
    public void authenticateWithAAALegacyPassEncoder_CorrectCredentials_ShouldPass() {
        context.registerBean(Md5PasswordEncoder.class, Md5PasswordEncoder::new);

        Authentication auth = aaaProvider.authenticate(aaaLegacyValidToken);

        assertTrue(auth.isAuthenticated());
    }

    @Test(expected = BadCredentialsException.class)
    @DirtiesContext
    public void authenticateWithAAALegacyPassEncoder_WrongCredentials_ShouldThrowException() {
        context.registerBean(Md5PasswordEncoder.class, Md5PasswordEncoder::new);

        aaaProvider.authenticate(aaaLegacyInvalidToken);
    }

}