package th.entity.interactive;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import th.util.StringUtils;

public class ServerTimeGetBean extends BaseUploadBean {

	public static final String	REQ_PARAM_PROXY_HOST		= "proxyhost";
	public static final String	REQ_PARAM_PROXY_PORT		= "proxyport";

	public static String[]		paramCheckArray			= { REQ_PARAM_MAC, REQ_PARAM_PROXY_HOST, REQ_PARAM_PROXY_PORT };

	public ServerTimeGetBean() {

	}

	public ServerTimeGetBean(HttpServletRequest request) {
		
		setMac(request.getParameter(REQ_PARAM_MAC));
		proxyHost = request.getParameter(REQ_PARAM_PROXY_HOST).toString();
		proxyPort = request.getParameter(REQ_PARAM_PROXY_PORT).toString();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[mac=").append(this.getMac());
		sb.append(",machinId=").append(this.getMachineId());
		sb.append(",proxyHost=").append(this.getProxyHost());
		sb.append(",proxyPort=").append(this.getProxyPort());
		sb.append("]");
		return sb.toString();
	}


	private String		proxyHost	= "";
	private String		proxyPort	= "";

	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * @param proxyHost the proxyHost to set
	 */
	public void setProxyHost( String proxyHost ) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @return the proxyPort
	 */
	public String getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort the proxyPort to set
	 */
	public void setProxyPort( String proxyPort ) {
		this.proxyPort = proxyPort;
	}

}
