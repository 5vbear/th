/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

import th.com.util.Define;

public class GuestBookBean {

	private long ideaId;
	private String ideaType;
	private long machineID;
	private String ideaContent;
	private String operateTime;
	private long operator;
	private String machineName;
	private String machineMark;
	private String orgName;
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

	public GuestBookBean() {

		this.ideaId = -1;
		this.machineID = -1;
		this.ideaType = "";
		this.ideaContent = "";
		this.operateTime = "";
		this.operator = -1;
		this.machineName = "";
		this.machineMark = "";
		this.orgName = "";
	}

	public long getIdeaId() {
		return ideaId;
	}

	public void setIdeaId(long ideaId) {
		this.ideaId = ideaId;
	}

	public String getIdeaType() {
		return ideaType;
	}

	public void setIdeaType(String ideaType) {
		this.ideaType = ideaType;
	}

	public long getMachineID() {
		return machineID;
	}

	public void setMachineID(long machineID) {
		this.machineID = machineID;
	}

	public String getIdeaContent() {
		return ideaContent;
	}

	public void setIdeaContent(String ideaContent) {
		this.ideaContent = ideaContent;
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
	
}
