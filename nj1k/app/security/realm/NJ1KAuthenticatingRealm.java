package security.realm;

import java.util.HashSet;
import java.util.Set;

import models.RoleEntity;
import models.UserEntity;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.i18n.Messages;

public class NJ1KAuthenticatingRealm extends AuthorizingRealm {

	private static final Logger logger = LoggerFactory.getLogger(NJ1KAuthenticatingRealm.class);
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		
		if (token instanceof UsernamePasswordToken) {
			UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
			if (usernamePasswordToken.getUsername() != null && usernamePasswordToken.getPassword() != null) {
				UserEntity user = getUser(usernamePasswordToken.getUsername());
				
				if (user != null) {
					return getAuthenticationInfo(user);
				}
			}
		}
		
		throw new AuthenticationException(Messages.get("auth.failed"));
	}

	@Override
	public String getName() {
		return "NJ1K Realm";
	}
	
	private UserEntity getUser(String email) {
		return UserEntity.findByEmail(email);
	}

	private Set<String> getUserRoles(String email) {
		UserEntity user = getUser(email);
		Set<String> roles = new HashSet<String>(user.roles.size());
		
		for (RoleEntity r : user.roles) {
			roles.add(r.roleName);
		}
		
		return roles;
	}
	
	private SimpleAuthenticationInfo getAuthenticationInfo(UserEntity user) {
		if (user.salt == null) {
			logger.trace("Authenticating user with old hashing scheme");
			return new SimpleAuthenticationInfo(user.email, user.password, getName());
		} 
		else {
			logger.trace("Authenticating user with new hashing scheme");

			SimpleHash decodedHash = Sha256Hash.fromBase64String(user.password);
			ByteSource salt = new SimpleByteSource(Base64.decode(user.salt));
			
			return new SimpleAuthenticationInfo(user.email, decodedHash, salt, getName());
		}
	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		AuthorizationInfo authInfo = new SimpleAuthorizationInfo();
		
		if (principals.getPrimaryPrincipal() instanceof String) {
			authInfo = new SimpleAuthorizationInfo(getUserRoles((String) principals.getPrimaryPrincipal()));
		}
		
		return authInfo;
	}
}
