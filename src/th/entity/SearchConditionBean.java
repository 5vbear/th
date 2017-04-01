/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-8-14
 * @author PSET
 * @since JDK1.6
 *
 */
public class SearchConditionBean {
	
	private long orgId;
	private long dptId;
	private String orgName;
	private String dptName;
	private String managerName;
	private String userName;
	private String searchOrgStr;
	
	public SearchConditionBean(){
		this.orgId = -1;
		this.dptId = -1;
		this.orgName = "";
		this.dptName = "";
		this.managerName = "";
		this.userName = "";
		this.searchOrgStr = "";
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
	 * @return the dptId
	 */
	public long getDptId() {
		return dptId;
	}

	/**
	 * @param dptId the dptId to set
	 */
	public void setDptId( long dptId ) {
		this.dptId = dptId;
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
	 * @return the dptName
	 */
	public String getDptName() {
		return dptName;
	}

	/**
	 * @param dptName the dptName to set
	 */
	public void setDptName( String dptName ) {
		this.dptName = dptName;
	}

	/**
	 * @return the managerName
	 */
	public String getManagerName() {
		return managerName;
	}

	/**
	 * @param managerName the managerName to set
	 */
	public void setManagerName( String managerName ) {
		this.managerName = managerName;
	}
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName( String userName ) {
		this.userName = userName;
	}

	/**
	 * @return the searchOrgStr
	 */
	public String getSearchOrgStr() {
		return searchOrgStr;
	}

	/**
	 * @param searchOrgStr the searchOrgStr to set
	 */
	public void setSearchOrgStr( String searchOrgStr ) {
		this.searchOrgStr = searchOrgStr;
	}


}
