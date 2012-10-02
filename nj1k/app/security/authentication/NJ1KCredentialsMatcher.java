package security.authentication;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

public class NJ1KCredentialsMatcher extends HashedCredentialsMatcher {

	public NJ1KCredentialsMatcher() {
		super.setHashAlgorithmName("MD5");
	}
	
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		return super.doCredentialsMatch(token, info);
	}
	
	@Override
	public void setHashAlgorithmName(String hashAlgorithmName) {
		super.setHashAlgorithmName("MD5");
	}
}
