package utils;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class PasswordUtil {

	private static final String VOCABULARY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final int PASSWORD_LEN = 12;
	private static final int ITERATIONS = 1024;
	
	public static char[] generateRandomPassword() {
		
		Random rng = new SecureRandom();
		char[] password = new char[PASSWORD_LEN];
		
		for (int i = 0; i < password.length; i++) {
			password[i] = VOCABULARY.charAt((int) (rng.nextDouble() * VOCABULARY.length()));
		}
		
		return password;
	}
	
	public static ByteSource generateSalt() {
		return new SecureRandomNumberGenerator().nextBytes();
	}
	
	public static SimpleHash generateHash(String password, ByteSource salt) {
		return new Sha256Hash(password, salt, ITERATIONS);
	}
}
