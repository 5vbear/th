package th.entity;

import java.util.Date;

public class AwardBean {

	/* 标识符*/
	private String item;
	/*奖品id*/
	private long id;
	/*奖品名称*/
	private String awardName;
	/*奖品数量*/
	private int awardNum;
	/*奖品有效开始时间*/
	private Date startDate;
	/*奖品有效结束时间*/
	private Date endDate;
	/*奖品logo路径*/
	private String logoUrl;
	/*奖品上架时间*/
	private Date createTime;
	/*每天点击次数*/
	private int dailyHits;
	private String currentOrgID;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAwardName() {
		return awardName;
	}
	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}
	public int getAwardNum() {
		return awardNum;
	}
	public void setAwardNum(int awardNum) {
		this.awardNum = awardNum;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getDailyHits() {
		return dailyHits;
	}
	public void setDailyHits(int dailyHits) {
		this.dailyHits = dailyHits;
	}
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getCurrentOrgID() {
		return currentOrgID;
	}
	public void setCurrentOrgID(String currentOrgID) {
		this.currentOrgID = currentOrgID;
	}
	@Override
	public String toString() {
		return "AwardBean [id=" + id + ", awardName=" + awardName
				+ ", awardNum=" + awardNum + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", logoUrl=" + logoUrl
				+ ", createTime=" + createTime + ", dailyHits=" + dailyHits
				+ ", currentOrgID=" + currentOrgID
				+ "]";
	}
	
}
