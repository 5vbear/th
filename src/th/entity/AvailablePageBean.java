package th.entity;

import java.util.Date;


public class AvailablePageBean {
	/** 白名单bean*/
	//网页名称
	private String requestName;
	//网页URl
	private String requestURL;
	//状态
	private String type;
	//组织ID
	private long  orgId;
	//修改日期
	private Date  editdate;
	//操作用户
	private long  operator;
	//企业类型
	private String enterType;
	/**
	 * @return the requestName
	 */
	public String getRequestName() {
		return requestName;
	}
	/**
	 * @param requestName the requestName to set
	 */
	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}
	/**
	 * @return the requestURL
	 */
	public String getRequestURL() {
		return requestURL;
	}
	/**
	 * @param requestURL the requestURL to set
	 */
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * @return the editdate
	 */
	public Date getEditdate() {
		return editdate;
	}
	/**
	 * @param editdate the editdate to set
	 */
	public void setEditdate(Date editdate) {
		this.editdate = editdate;
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
	public void setOperator(long operator) {
		this.operator = operator;
	}
	public String getEnterType() {
		return enterType;
	}
	public void setEnterType(String enterType) {
		this.enterType = enterType;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AvailablePageBean [requestName=" + requestName
				+ ", requestURL=" + requestURL + ", type=" + type + ", orgId="
				+ orgId + ", editdate=" + editdate + ", operator=" + operator + ", enterType=" + enterType
				+ "]";
	}
	
}
