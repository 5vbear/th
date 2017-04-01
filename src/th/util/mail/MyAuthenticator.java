package th.util.mail;

import javax.mail.*;

/**
 * 
 * Descriptions权限验证类
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
public class MyAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator( String username, String password ) {
		this.userName = username;
		this.password = password;
	}

	//
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication( userName, password );
	}
}
