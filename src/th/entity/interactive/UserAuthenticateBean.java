/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity.interactive;

import javax.servlet.http.HttpServletRequest;

/**
 * Descriptions
 *
 * @version 2013年8月20日
 * @author PSET
 * @since JDK1.6
 *
 */
public class UserAuthenticateBean extends BaseUploadBean {

	public static final String	REQ_PARAM_USERNAME	= "username";
	public static final String	REQ_PARAM_PASSWORD	= "password";
	
	public static String[]		paramCheckArray				= { REQ_PARAM_MAC, REQ_PARAM_USERNAME, REQ_PARAM_PASSWORD };

	public UserAuthenticateBean(){
		
	}
	
	public UserAuthenticateBean(HttpServletRequest request) {
		this.setMac(request.getParameter(REQ_PARAM_MAC));
		username = request.getParameter(REQ_PARAM_USERNAME).toString();
		password = request.getParameter(REQ_PARAM_PASSWORD).toString();
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",machinId=").append(this.getMachineId());
		sb.append(",username=").append(username);
		sb.append(",password=").append(password);
		sb.append("]");
		return sb.toString();
	}
	
	private String		username	= "";
	private String		password	= "";

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername( String username ) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword( String password ) {
		this.password = password;
	}
	
	
	
}
