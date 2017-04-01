/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

public class FaultBean {
	private String orgId; // 组织ID
	private String orgName; // 组织名称
	private String machineMark;// 端机标识
	private String faultNum; // 故障次数
	private String faultType; // 故障类型
	private String sortKey; // 排序

	public FaultBean(){
		this.orgId = "";
		this.orgName = "";
		this.machineMark = "";
		this.faultNum = "";
		this.sortKey = "";
		this.faultType = "";
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getMachineMark() {
		return machineMark;
	}

	public void setMachineMark(String machineMark) {
		this.machineMark = machineMark;
	}

	public String getFaultNum() {
		return faultNum;
	}

	public void setFaultNum(String faultNum) {
		this.faultNum = faultNum;
	}

	public String getFaultType() {
		return faultType;
	}

	public void setFaultType(String faultType) {
		this.faultType = faultType;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
}
