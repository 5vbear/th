package th.entity;

public class AlarmBean {
	
	private String orgID;
	private String userName;
	private String email;
	/**
	 * alert_id---alert_name
	 * 1	                非法进程
	 * 2	                非法服务
	 * 3	      cpu负荷过高
	 * 4	                内存负荷过高
	 * 5	                硬盘容量不足
	 * 6	               上行速率过慢
	 * 7	               下行速率过慢
	 * 8	               非法访问率过高
	 * 9	               断线报警
	 */
	private String alertID;
	private String alertName;
	private String machineID;
	private String machineMark;

	public AlarmBean(){
	}

	public String getOrgID() {
		return orgID;
	}

	public void setOrgID(String orgID) {
		this.orgID = orgID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlertID() {
		return alertID;
	}

	public void setAlertID(String alertID) {
		this.alertID = alertID;
	}

	public String getAlertName() {
		return alertName;
	}

	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}

	public String getMachineID() {
		return machineID;
	}

	public void setMachineID(String machineID) {
		this.machineID = machineID;
	}

	public String getMachineMark() {
		return machineMark;
	}

	public void setMachineMark(String machineMark) {
		this.machineMark = machineMark;
	}
	
}
