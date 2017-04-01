/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 *
 */
public class OrganizationBean {
	
	private long orgId;
	private String orgLevel;
	private String orgName;
	private long parentOrgId;
	private String orgNo;
	private String macStartTime;
	private String macShutdownTime;
	private int screenProtectTime;
	private String writhDir;
	private String operateTime;
	private long operator;
	
	public OrganizationBean(){
		
		this.orgId = -1;
		this.orgLevel = "";
		this.orgName = "";
		this.parentOrgId = -1;
		this.orgNo = "";
		this.macStartTime = "";
		this.macShutdownTime = "";
		this.screenProtectTime = 0;
		this.writhDir = "";
		this.operateTime = "";
		this.operator = -1;
		
	}
	
	/**
	 * @return the orgId
	 */
	public long getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId the orgId to set
	 */
	public void setOrgId( long orgId ) {
		this.orgId = orgId;
	}

	/**
	 * @return the orgLevel
	 */
	public String getOrgLevel() {
		return orgLevel;
	}

	/**
	 * @param orgLevel the orgLevel to set
	 */
	public void setOrgLevel( String orgLevel ) {
		this.orgLevel = orgLevel;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName the orgName to set
	 */
	public void setOrgName( String orgName ) {
		this.orgName = orgName;
	}

	/**
	 * @return the parentOrgId
	 */
	public long getParentOrgId() {
		return parentOrgId;
	}

	/**
	 * @param parentOrgId the parentOrgId to set
	 */
	public void setParentOrgId( long parentOrgId ) {
		this.parentOrgId = parentOrgId;
	}

	/**
	 * @return the orgNo
	 */
	public String getOrgNo() {
		return orgNo;
	}

	/**
	 * @param orgNo the orgNo to set
	 */
	public void setOrgNo( String orgNo ) {
		this.orgNo = orgNo;
	}

	/**
	 * @return the macStartTime
	 */
	public String getMacStartTime() {
		return macStartTime;
	}

	/**
	 * @param macStartTime the macStartTime to set
	 */
	public void setMacStartTime( String macStartTime ) {
		this.macStartTime = macStartTime;
	}

	/**
	 * @return the macShutdownTime
	 */
	public String getMacShutdownTime() {
		return macShutdownTime;
	}

	/**
	 * @param macShutdownTime the macShutdownTime to set
	 */
	public void setMacShutdownTime( String macShutdownTime ) {
		this.macShutdownTime = macShutdownTime;
	}

	/**
	 * @return the screenProtectTime
	 */
	public int getScreenProtectTime() {
		return screenProtectTime;
	}

	/**
	 * @param screenProtectTime the screenProtectTime to set
	 */
	public void setScreenProtectTime( int screenProtectTime ) {
		this.screenProtectTime = screenProtectTime;
	}

	/**
	 * @return the writhDir
	 */
	public String getWrithDir() {
		return writhDir;
	}

	/**
	 * @param writhDir the writhDir to set
	 */
	public void setWrithDir( String writhDir ) {
		this.writhDir = writhDir;
	}

	/**
	 * @return the operateTime
	 */
	public String getOperateTime() {
		return operateTime;
	}

	/**
	 * @param operateTime the operateTime to set
	 */
	public void setOperateTime( String operateTime ) {
		this.operateTime = operateTime;
	}

	/**
	 * @return the operator
	 */
	public long getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator( long operator ) {
		this.operator = operator;
	}


}
