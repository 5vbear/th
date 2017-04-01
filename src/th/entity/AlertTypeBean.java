package th.entity;


public class AlertTypeBean {
	/** 告警通知方式信息*/
	//告知id
	private long alertId = -1;
	//组织id
	private long orgId = -2;
	//告知方式id
	private String alertType = "";
	/**
	 * @return the alertId
	 */
	public long getAlertId() {
		return alertId;
	}
	/**
	 * @param alertId the alertId to set
	 */
	public void setAlertId(long alertId) {
		this.alertId = alertId;
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
	 * @return the alertType
	 */
	public String getAlertType() {
		return alertType;
	}
	/**
	 * @param alertType the alertType to set
	 */
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}


}
