package security.authentication;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NJ1KCredentialsMatcher extends HashedCredentialsMatcher {

	private static final Logger logger = LoggerFactory.getLogger(NJ1KCredentialsMatcher.class);
	
	private HashedCredentialsMatcher getCredentialsMatcher(AuthenticationInfo info) {
		
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
		
		if (info instanceof SaltedAuthenticationInfo) {
			SaltedAuthenticationInfo saltedInfo = (SaltedAuthenticationInfo) info;
			
			if (saltedInfo.getCredentialsSalt() != null) {
				matcher.setHashIterations(this.getHashIterations());
				matcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
			}
			else {
				matcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);
			}
		}
		
		logger.trace("Returning {} credentials matcher with {} iterations", matcher.getHashAlgorithmName(), matcher.getHashIterations());
		
		return matcher;
	}
	
	public NJ1KCredentialsMatcher() {

	}
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		return getCredentialsMatcher(info).doCredentialsMatch(token, info);
	}
}