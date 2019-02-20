/**
 * 
 */
package com.devopsbuddy.test.integration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.devopsbuddy.DevopsbuddyApplication;
import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.repositories.PasswordResetTokenRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.*;

/**
 * @author dave
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DevopsbuddyApplication.class)
public class PasswordResetTokenIntegrationTest extends AbstractIntegrationTest {

	@Value("${token.expiration.length.minutes}")
	private int expirationTimeInMinutes;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Rule
	public TestName testName = new TestName();

	@Before
	public void init() {
		Assert.assertFalse(expirationTimeInMinutes == 0);
	}

	@Test
	public void testTokenExpirationLength() throws Exception {
		User user = createUser(testName);
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());

		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		String token = UUID.randomUUID().toString();

		LocalDateTime expectedTime = now.plusMinutes(expirationTimeInMinutes);

		PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);

		LocalDateTime actualTime = passwordResetToken.getExpiryDate();
		Assert.assertNotNull(actualTime);
		Assert.assertEquals(expectedTime, actualTime);
	}

    @Test
    public void testFindTokenByTokenValue() throws Exception {

        User user = createUser(testName);
        String token = UUID.randomUUID().toString();
        LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

        createPasswordResetToken(token, user, now);

        PasswordResetToken retrievedPasswordResetToken = passwordResetTokenRepository.findByToken(token);
        Assert.assertNotNull(retrievedPasswordResetToken);
        Assert.assertNotNull(retrievedPasswordResetToken.getId());
        Assert.assertNotNull(retrievedPasswordResetToken.getUser());

    }

	@Test
	public void testDeleteToken() throws Exception {
		User user = createUser(testName);
		String token = UUID.randomUUID().toString();
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

		PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
		long tokenId = passwordResetToken.getId();
		passwordResetTokenRepository.deleteById(tokenId);

		PasswordResetToken shouldNotExistToken = passwordResetTokenRepository.findById(tokenId).orElse(null);
		assertNull(shouldNotExistToken);
	}

	@Test
	public void testCascadeDeleteFromUserEntity() throws Exception {
		User user = createUser(testName);
		String token = UUID.randomUUID().toString();
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

		PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
		passwordResetToken.getId();
		userRepository.deleteById(user.getId());

		Set<PasswordResetToken> shouldBeEmpty = passwordResetTokenRepository.findAllByUserId(user.getId());
		assertTrue(shouldBeEmpty.isEmpty());
	}

	@Test
	public void testMultupleTokensAreReturnedQueringByUserId() throws Exception {
		User user = createUser(testName);
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

		String token = UUID.randomUUID().toString();
		String token2 = UUID.randomUUID().toString();
		String token3 = UUID.randomUUID().toString();

		Set<PasswordResetToken> tokens = new HashSet<>();
		tokens.add(createPasswordResetToken(token, user, now));
		tokens.add(createPasswordResetToken(token2, user, now));
		tokens.add(createPasswordResetToken(token3, user, now));

		Set<PasswordResetToken> acutalTokens = passwordResetTokenRepository.findAllByUserId(user.getId());
		assertTrue(acutalTokens.size() == tokens.size());
		List<String> tokenAsList = tokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
		List<String> acutalTokensAsList = acutalTokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
		assertEquals(tokenAsList, acutalTokensAsList);
	}


	private PasswordResetToken createPasswordResetToken(String token, User user, LocalDateTime now) {
		PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, now, expirationTimeInMinutes);
		passwordResetTokenRepository.save(passwordResetToken);
		assertNotNull(passwordResetToken);
		return passwordResetToken;
	}


}
