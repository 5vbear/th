package th.entity;

import java.util.Date;


public class LotteryBean {
	/** 抽奖bean*/
	//抽奖Id
	private String award_id;
	//抽奖名称
	private String award_name;
	//奖品数量
	private int award_num;
	//抽奖开始时间
	private Date  start_date;
	//抽奖结束时间
	private Date  end_date;
	//降临logo
	private String  logo_url;
	//预估点击次数
	private long  daily_hits;
	//抽奖天数
	private int   day;
	//抽奖概率
	private long   ranNum;
	private String userName;
	private String address;
	private String zipCode;
	private String phone;
	private String opertateTime;
	
	/**
	 * @return the award_id
	 */
	public String getAward_id() {
		return award_id;
	}
	/**
	 * @param award_id the award_id to set
	 */
	public void setAward_id(String award_id) {
		this.award_id = award_id;
	}
	/**
	 * @return the award_name
	 */
	public String getAward_name() {
		return award_name;
	}
	/**
	 * @param award_name the award_name to set
	 */
	public void setAward_name(String award_name) {
		this.award_name = award_name;
	}
	/**
	 * @return the award_num
	 */
	public int getAward_num() {
		return award_num;
	}
	/**
	 * @param award_num the award_num to set
	 */
	public void setAward_num(int award_num) {
		this.award_num = award_num;
	}
	/**
	 * @return the start_date
	 */
	public Date getStart_date() {
		return start_date;
	}
	/**
	 * @param start_date the start_date to set
	 */
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	/**
	 * @return the end_date
	 */
	public Date getEnd_date() {
		return end_date;
	}
	/**
	 * @param end_date the end_date to set
	 */
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	/**
	 * @return the logo_url
	 */
	public String getLogo_url() {
		return logo_url;
	}
	/**
	 * @param logo_url the logo_url to set
	 */
	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}
	/**
	 * @return the daily_hits
	 */
	public long getDaily_hits() {
		return daily_hits;
	}
	/**
	 * @param daily_hits the daily_hits to set
	 */
	public void setDaily_hits(long daily_hits) {
		this.daily_hits = daily_hits;
	}
	/**
	 * @return the day
	 */
	public int getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(int day) {
		this.day = day;
	}
	/**
	 * @return the ranNum
	 */
	public long getRanNum() {
		return ranNum;
	}
	/**
	 * @param ranNum the ranNum to set
	 */
	public void setRanNum(long ranNum) {
		this.ranNum = ranNum;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOpertateTime() {
		return opertateTime;
	}
	public void setOpertateTime(String opertateTime) {
		this.opertateTime = opertateTime;
	}
	
	
}
