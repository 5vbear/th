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
public class DepartmentBean {
	
	private long dptId;
	private long orgId;
	private String dptName;
	private String managerName;
	private String managerTel;
	private String operateTime;
	private long operator;
	private String dptDescription;
	private String managerMail;
	private String otherContacts;
	
	
	public DepartmentBean(){
		this.dptId = -1;
		this.orgId = -1;
		this.dptName = "";
		this.managerName = "";
		this.managerTel = "";
		this.operateTime = "";
		this.operator = -1;
		this.dptDescription = "";
		this.managerMail = "";
		this.otherContacts = "";
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
	 * @return the managerTel
	 */
	public String getManagerTel() {
		return managerTel;
	}

	/**
	 * @param managerTel the managerTel to set
	 */
	public void setManagerTel( String managerTel ) {
		this.managerTel = managerTel;
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

	/**
	 * @return the dptDescription
	 */
	public String getDptDescription() {
		return dptDescription;
	}

	/**
	 * @param dptDescription the dptDescription to set
	 */
	public void setDptDescription( String dptDescription ) {
		this.dptDescription = dptDescription;
	}

	/**
	 * @return the managerMail
	 */
	public String getManagerMail() {
		return managerMail;
	}

	/**
	 * @param managerMail the managerMail to set
	 */
	public void setManagerMail( String managerMail ) {
		this.managerMail = managerMail;
	}

	/**
	 * @return the otherContacts
	 */
	public String getOtherContacts() {
		return otherContacts;
	}

	/**
	 * @param otherContacts the otherContacts to set
	 */
	public void setOtherContacts( String otherContacts ) {
		this.otherContacts = otherContacts;
	}



}
