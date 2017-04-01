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
public class MultiRunningStatisticsBean {
	private String rank;
	private String multiOrgName;
	private String maxClickCount;
	private String minClickCount;
	private String averagePlayTime;
	private String averageClickCount;
	private String averageCount;

	/**
	 * @return the averageCount
	 */
	public String getAverageCount() {
		return averageCount;
	}

	/**
	 * @param averageCount
	 *            the averageCount to set
	 */
	public void setAverageCount( String averageCount ) {
		this.averageCount = averageCount;
	}

	private String openRate;

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
	 * @return the maxClickCount
	 */
	public String getMaxClickCount() {
		return maxClickCount;
	}

	/**
	 * @param maxClickCount
	 *            the maxClickCount to set
	 */
	public void setMaxClickCount( String maxClickCount ) {
		this.maxClickCount = maxClickCount;
	}

	/**
	 * @return the minClickCount
	 */
	public String getMinClickCount() {
		return minClickCount;
	}

	/**
	 * @param minClickCount
	 *            the minClickCount to set
	 */
	public void setMinClickCount( String minClickCount ) {
		this.minClickCount = minClickCount;
	}

	/**
	 * @return the averagePlayTime
	 */
	public String getAveragePlayTime() {
		return averagePlayTime;
	}

	/**
	 * @param averagePlayTime
	 *            the averagePlayTime to set
	 */
	public void setAveragePlayTime( String averagePlayTime ) {
		this.averagePlayTime = averagePlayTime;
	}

	/**
	 * @return the averageCount
	 */
	public String getAverageClickCount() {
		return averageClickCount;
	}

	/**
	 * @param averageCount
	 *            the averageCount to set
	 */
	public void setAverageClickCount( String averageClickCount ) {
		this.averageClickCount = averageClickCount;
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

}
