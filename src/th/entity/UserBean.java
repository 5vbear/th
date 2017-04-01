/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-8-17
 * @author PSET
 * @since JDK1.6
 *
 */
public class UserBean {
	
	private long userId;
	private long orgId;
	private long dptId;
	private String nickName;
	private String password;
	private String userName;
	private String email;
	private String address;
	private String fixedTel;
	private String mobilePhone;
	private String userDesp;
	private String userType;
	private String userStatus;
	private String operateTime;
	private long operator;
	
	// 添加一个部门名称，方便参数传值
	private String dptName;
	

	public UserBean(){
		this.userId = -1;
		this.orgId = -1;
		this.dptId = -1;
		this.nickName = "";
		this.password = "";
		this.userName = "";
		this.email = "";
		this.address = "";
		this.fixedTel = "";
		this.mobilePhone = "";
		this.userDesp = "";
		this.userStatus = "1";
		this.userType = "0";
		this.operateTime = "";
		this.operator = -1;
		
		this.dptName = "";
	}
	
	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId( long userId ) {
		this.userId = userId;
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
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName( String nickName ) {
		this.nickName = nickName;
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail( String email ) {
		this.email = email;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress( String address ) {
		this.address = address;
	}

	/**
	 * @return the fixedTel
	 */
	public String getFixedTel() {
		return fixedTel;
	}

	/**
	 * @param fixedTel the fixedTel to set
	 */
	public void setFixedTel( String fixedTel ) {
		this.fixedTel = fixedTel;
	}

	/**
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobilePhone( String mobilePhone ) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return the userDesp
	 */
	public String getUserDesp() {
		return userDesp;
	}

	/**
	 * @param userDesp the userDesp to set
	 */
	public void setUserDesp( String userDesp ) {
		this.userDesp = userDesp;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType( String userType ) {
		this.userType = userType;
	}
	
	/**
	 * @return the userStatus
	 */
	public String getUserStatus() {
		return userStatus;
	}

	/**
	 * @param userStatus the userStatus to set
	 */
	public void setUserStatus( String userStatus ) {
		this.userStatus = userStatus;
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


}
