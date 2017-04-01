/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

import th.com.util.Define;

public class AppStoreBean {

	private long appId;
	private String appName;
	private String description;
	private String downloadUrl;
	private String iconUrl;
	private String operateTime;
	private String versionType;
	private String version;
	private long operator;
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

	public AppStoreBean() {

		this.appId = -1;
		this.operator = -1;
		this.appName = "";
		this.description = "";
		this.downloadUrl = "";
		this.operateTime = "";
		this.iconUrl = "";
		this.versionType = "";
		this.version = "";
	}

	public long getAppId() {
		return appId;
	}

	public void setAppId(long appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
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

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
