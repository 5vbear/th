/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-9-7
 * @author PSET
 * @since JDK1.6
 *
 */
public class SysServBean {

	private int confId;
	private String smtpHost;
	private long smtpPort;
	private String mailFrom;
	private String mailAuthNick;
	private String mailAuthPass;
	private String msgGateURL;
	private String gateAuthNick;
	private String gateAuthPass;
	private String serStatus;

	public SysServBean(){
		this.confId = -1;
		this.smtpHost = "";
		this.smtpPort = 1080;
		this.mailFrom = "";
		this.mailAuthNick = "";
		this.mailAuthPass = "";
		this.msgGateURL = "";
		this.gateAuthNick = "";
		this.gateAuthPass = "";
		this.serStatus = "1";
	}

	/**
	 * @return the confId
	 */
	public int getConfId() {
		return confId;
	}

	/**
	 * @param confId the confId to set
	 */
	public void setConfId( int confId ) {
		this.confId = confId;
	}

	/**
	 * @return the smtpHost
	 */
	public String getSmtpHost() {
		return smtpHost;
	}

	/**
	 * @param smtpHost the smtpHost to set
	 */
	public void setSmtpHost( String smtpHost ) {
		this.smtpHost = smtpHost;
	}

	/**
	 * @return the smtpPort
	 */
	public long getSmtpPort() {
		return smtpPort;
	}

	/**
	 * @param smtpPort the smtpPort to set
	 */
	public void setSmtpPort( long smtpPort ) {
		this.smtpPort = smtpPort;
	}

	/**
	 * @return the mailFrom
	 */
	public String getMailFrom() {
		return mailFrom;
	}

	/**
	 * @param mailFrom the mailFrom to set
	 */
	public void setMailFrom( String mailFrom ) {
		this.mailFrom = mailFrom;
	}

	/**
	 * @return the mailAuthNick
	 */
	public String getMailAuthNick() {
		return mailAuthNick;
	}

	/**
	 * @param mailAuthNick the mailAuthNick to set
	 */
	public void setMailAuthNick( String mailAuthNick ) {
		this.mailAuthNick = mailAuthNick;
	}

	/**
	 * @return the mailAuthPass
	 */
	public String getMailAuthPass() {
		return mailAuthPass;
	}

	/**
	 * @param mailAuthPass the mailAuthPass to set
	 */
	public void setMailAuthPass( String mailAuthPass ) {
		this.mailAuthPass = mailAuthPass;
	}

	/**
	 * @return the msgGateURL
	 */
	public String getMsgGateURL() {
		return msgGateURL;
	}

	/**
	 * @param msgGateURL the msgGateURL to set
	 */
	public void setMsgGateURL( String msgGateURL ) {
		this.msgGateURL = msgGateURL;
	}

	/**
	 * @return the gateAuthNick
	 */
	public String getGateAuthNick() {
		return gateAuthNick;
	}

	/**
	 * @param gateAuthNick the gateAuthNick to set
	 */
	public void setGateAuthNick( String gateAuthNick ) {
		this.gateAuthNick = gateAuthNick;
	}

	/**
	 * @return the gateAuthPass
	 */
	public String getGateAuthPass() {
		return gateAuthPass;
	}

	/**
	 * @param gateAuthPass the gateAuthPass to set
	 */
	public void setGateAuthPass( String gateAuthPass ) {
		this.gateAuthPass = gateAuthPass;
	}
	
	/**
	 * @return the serStatus
	 */
	public String getSerStatus() {
		return serStatus;
	}

	/**
	 * @param serStatus the serStatus to set
	 */
	public void setSerStatus( String serStatus ) {
		this.serStatus = serStatus;
	}


}
