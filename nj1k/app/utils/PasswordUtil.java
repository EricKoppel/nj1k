package utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

import security.shiro.authentication.NJ1KCredentialsMatcher;

public class PasswordUtil {

	private static final String VOCABULARY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int PASSWORD_LEN = 12;
	
	public static Character[] generateRandomPassword() {
		Random rng = new SecureRandom();
		return IntStream.range(0, PASSWORD_LEN)
		.mapToObj(i -> VOCABULARY.charAt((int) (rng.nextDouble() * VOCABULARY.length())))
		.toArray(Character[]::new);
	}
	
	public static ByteSource generateSalt() {
		return new SecureRandomNumberGenerator().nextBytes();
	}
	
	public static SimpleHash generateHash(char[] password, ByteSource salt) {
		return new Sha256Hash(password, salt, NJ1KCredentialsMatcher.SHA_ITERATIONS);
	}
}
