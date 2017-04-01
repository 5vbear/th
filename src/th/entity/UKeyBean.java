/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

public class UKeyBean {
	private String orgId; // 组织ID
	private String orgName; // 组织名称
	private String machineMark;// 端机标识
	private String useNum; // Ukey使用次数
	private String latestTime; // 最后一次使用时间
	private String sortKey; // 排序

	public UKeyBean(){
		this.orgId = "";
		this.orgName = "";
		this.machineMark = "";
		this.useNum = "";
		this.latestTime = "";
		this.sortKey = "";
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

	public String getUseNum() {
		return useNum;
	}

	public void setUseNum(String useNum) {
		this.useNum = useNum;
	}

	public String getLatestTime() {
		return latestTime;
	}

	public void setLatestTime(String latestTime) {
		this.latestTime = latestTime;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	
}
