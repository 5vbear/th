/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 * 
 * @version 2013-8-27
 * @author PSET
 * @since JDK1.6
 * 
 */
public class DeviceRunningBean {
	private String rank;
	private String multiOrgName;
	private String normalRateOrgName;
	private String normalRate;
	private String openRateOrgName;
	private String openRate;
	private String avrOrgname;
	private String avrNum;

	/**
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * @param rank
	 *            the rank to set
	 */
	public void setRank( String rank ) {
		this.rank = rank;
	}

	/**
	 * @return the multiOrgName
	 */
	public String getMultiOrgName() {
		return multiOrgName;
	}

	/**
	 * @param multiOrgName
	 *            the multiOrgName to set
	 */
	public void setMultiOrgName( String multiOrgName ) {
		this.multiOrgName = multiOrgName;
	}

	/**
	 * @return the normalRateOrgName
	 */
	public String getNormalRateOrgName() {
		return normalRateOrgName;
	}

	/**
	 * @param normalRateOrgName
	 *            the normalRateOrgName to set
	 */
	public void setNormalRateOrgName( String normalRateOrgName ) {
		this.normalRateOrgName = normalRateOrgName;
	}

	/**
	 * @return the normalRate
	 */
	public String getNormalRate() {
		return normalRate;
	}

	/**
	 * @param normalRate
	 *            the normalRate to set
	 */
	public void setNormalRate( String normalRate ) {
		this.normalRate = normalRate;
	}

	/**
	 * @return the openRateOrgName
	 */
	public String getOpenRateOrgName() {
		return openRateOrgName;
	}

	/**
	 * @param openRateOrgName
	 *            the openRateOrgName to set
	 */
	public void setOpenRateOrgName( String openRateOrgName ) {
		this.openRateOrgName = openRateOrgName;
	}

	/**
	 * @return the openRate
	 */
	public String getOpenRate() {
		return openRate;
	}

	/**
	 * @param openRate
	 *            the openRate to set
	 */
	public void setOpenRate( String openRate ) {
		this.openRate = openRate;
	}

	public String getAvrOrgname() {
		return avrOrgname;
	}

	public void setAvrOrgname(String avrOrgname) {
		this.avrOrgname = avrOrgname;
	}

	public String getAvrNum() {
		return avrNum;
	}

	public void setAvrNum(String avrNum) {
		this.avrNum = avrNum;
	}

}
