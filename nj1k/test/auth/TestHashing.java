package auth;

import junit.framework.Assert;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import utils.PasswordUtil;

public class TestHashing {

	private Logger logger = LoggerFactory.getLogger(TestHashing.class);
	
	@Test
	public void testSalt() {
		
		ByteSource salt = PasswordUtil.generateSalt();
		
		String storedSalt = salt.toBase64();
		
		ByteSource restoredSalt = new SimpleByteSource(Base64.decode(storedSalt));
		
		Assert.assertTrue(restoredSalt.equals(salt));
	}
	
	@Test
	public void testHash() {
		
		String password = String.valueOf(PasswordUtil.generateRandomPassword());
		ByteSource salt = PasswordUtil.generateSalt();
		SimpleHash hash = PasswordUtil.generateHash(password, salt);
		
		String storedHash = hash.toBase64();
		
		SimpleHash decodedHash = Sha256Hash.fromBase64String(storedHash);
		
		Assert.assertTrue(decodedHash.equals(hash));
	}
}
