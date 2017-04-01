/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

import th.com.util.Define;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
public class MachineBean {

	private long machineId;
	private String mac;
	private String machineName;
	private String machineMark;
	private String status;
	private String operateTime;
	private String longitude;
	private String latitude;
	private String locationName;
	private String iconPath;
	private long operator;
	private long orgID;
	private String os;
	private String machine_kind;
	private String orgName;
	private String macType;
	private String branchName;
	private String branchAddress;
	private String branchNumber;
	private String managerName;
	private String contactTel;
	private String contactMobile;
	private String contactEmail;
	private String regTime;
	private String freeRepairYear;
	private String contactName;
	private String workTime;
	private String openDate;
	private String showMacType;
	
	// 检索结果总数
	private int total_num = 0;
	// 检索结果现在页
	private int point_num = 0;
	// 检索结果每页显示行数
	private int view_num = Define.VIEW_NUM;;
	// 排序方式
	private String sortField = "";
	// 排序类型
	private String sortType = "";

	public MachineBean() {

		this.machineId = -1;
		this.orgID = -1;
		this.mac = "";
		this.operator = -1;
		this.machineName = "";
		this.machineMark = "";
		this.status = "";
		this.operateTime = "";
		this.longitude = "";
		this.latitude = "";
		this.locationName = "";
		this.iconPath = "";
		this.orgName = "";
		this.macType = "";
		this.branchName = "";
		this.branchAddress = "";
		this.branchNumber = "";
		this.managerName = "";
		this.regTime = "";
		this.freeRepairYear = "";
		this.contactName = "";
		this.workTime = "";
		this.openDate = "";
		this.contactTel = "";
		this.contactMobile = "";
		this.contactEmail = "";
		this.showMacType = "";

	}

	public long getMachineId() {
		return machineId;
	}

	public void setMachineId(long machineId) {
		this.machineId = machineId;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getMachineMark() {
		return machineMark;
	}

	public void setMachineMark(String machineMark) {
		this.machineMark = machineMark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public long getOperator() {
		return operator;
	}

	public void setOperator(long operator) {
		this.operator = operator;
	}

	public long getOrgID() {
		return orgID;
	}

	public void setOrgID(long orgID) {
		this.orgID = orgID;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public int getTotal_num() {
		return total_num;
	}

	public void setTotal_num(int total_num) {
		this.total_num = total_num;
	}

	public int getPoint_num() {
		return point_num;
	}

	public void setPoint_num(int point_num) {
		this.point_num = point_num;
	}

	public int getView_num() {
		return view_num;
	}

	public void setView_num(int view_num) {
		this.view_num = view_num;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getMacType() {
		return macType;
	}

	public void setMacType(String macType) {
		this.macType = macType;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getBranchAddress() {
		return branchAddress;
	}

	public void setBranchAddress(String branchAddress) {
		this.branchAddress = branchAddress;
	}

	public String getBranchNumber() {
		return branchNumber;
	}

	public void setBranchNumber(String branchNumber) {
		this.branchNumber = branchNumber;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getRegTime() {
		return regTime;
	}

	public void setRegTime(String regTime) {
		this.regTime = regTime;
	}

	public String getFreeRepairYear() {
		return freeRepairYear;
	}

	public void setFreeRepairYear(String freeRepairYear) {
		this.freeRepairYear = freeRepairYear;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getOpenDate() {
		return openDate;
	}

	public void setOpenDate(String openDate) {
		this.openDate = openDate;
	}

	public String getContactTel() {
		return contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	
		public String getMachine_kind() {
		return machine_kind;
	}

	public void setMachine_kind(String machine_kind) {
		this.machine_kind = machine_kind;
	}

	public String getShowMacType() {
		return showMacType;
	}

	public void setShowMacType(String showMacType) {
		this.showMacType = showMacType;
	}
}
