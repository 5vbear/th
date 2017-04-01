/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
public class MachineOpenRateBean implements Comparable<Object> {
	private String orgId; // 组织ID
	private String orgName; // 组织名称
	private long totalCount; // 总台次
	private long totalMachineCount; // 总台数
	private long openCount; // 开机台次
	private long openMachineCount; // 开机台数
	private long pauseCount; // 报停台次
	private long pauseMachineCount; // 报停台数
	private double doubleNormalRate; // 正常运行率（小数）
	private double doubleOpenRate; // 开机率（小数）
	private String normalRate; // 正常运行率（百分比）
	private String openRate; // 开机率（百分比）
	private String sortKey; // 排序

	public MachineOpenRateBean() {
		this.totalCount = 0;
		this.totalMachineCount = 0;
		this.openCount = 0;
		this.openMachineCount = 0;
		this.pauseCount = 0;
		this.pauseMachineCount = 0;
		this.doubleNormalRate = 0;
		this.doubleOpenRate = 0;
		this.normalRate = "";
		this.openRate = "";
		this.sortKey = "orgName";
	}

	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}

	/**
	 * @param orgId
	 *            the orgId to set
	 */
	public void setOrgId( String orgId ) {
		this.orgId = orgId;
	}

	/**
	 * @return the orgName
	 */
	public String getOrgName() {
		return orgName;
	}

	/**
	 * @param orgName
	 *            the orgName to set
	 */
	public void setOrgName( String orgName ) {
		this.orgName = orgName;
	}

	/**
	 * @return the totalCount
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * @param totalCount
	 *            the totalCount to set
	 */
	public void setTotalCount( long totalCount ) {
		this.totalCount = totalCount;
	}

	/**
	 * @return the totalMachineCount
	 */
	public long getTotalMachineCount() {
		return totalMachineCount;
	}

	/**
	 * @param totalMachineCount
	 *            the totalMachineCount to set
	 */
	public void setTotalMachineCount( long totalMachineCount ) {
		this.totalMachineCount = totalMachineCount;
	}

	/**
	 * @return the openCount
	 */
	public long getOpenCount() {
		return openCount;
	}

	/**
	 * @param openCount
	 *            the openCount to set
	 */
	public void setOpenCount( long openCount ) {
		this.openCount = openCount;
	}

	/**
	 * @return the openMachineCount
	 */
	public long getOpenMachineCount() {
		return openMachineCount;
	}

	/**
	 * @param openMachineCount
	 *            the openMachineCount to set
	 */
	public void setOpenMachineCount( long openMachineCount ) {
		this.openMachineCount = openMachineCount;
	}

	/**
	 * @return the pauseCount
	 */
	public long getPauseCount() {
		return pauseCount;
	}

	/**
	 * @param pauseCount
	 *            the pauseCount to set
	 */
	public void setPauseCount( long pauseCount ) {
		this.pauseCount = pauseCount;
	}

	/**
	 * @return the pauseMachineCount
	 */
	public long getPauseMachineCount() {
		return pauseMachineCount;
	}

	/**
	 * @param pauseMachineCount
	 *            the pauseMachineCount to set
	 */
	public void setPauseMachineCount( long pauseMachineCount ) {
		this.pauseMachineCount = pauseMachineCount;
	}

	/**
	 * @return the doubleNormalRate
	 */
	public double getDoubleNormalRate() {
		return doubleNormalRate;
	}

	/**
	 * @param doubleNormalRate
	 *            the doubleNormalRate to set
	 */
	public void setDoubleNormalRate( double doubleNormalRate ) {
		this.doubleNormalRate = doubleNormalRate;
	}

	/**
	 * @return the doubleOpenRate
	 */
	public double getDoubleOpenRate() {
		return doubleOpenRate;
	}

	/**
	 * @param doubleOpenRate
	 *            the doubleOpenRate to set
	 */
	public void setDoubleOpenRate( double doubleOpenRate ) {
		this.doubleOpenRate = doubleOpenRate;
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

	/**
	 * @return the sortKey
	 */
	public String getSortKey() {
		return sortKey;
	}

	/**
	 * @param sortKey
	 *            the sortKey to set
	 */
	public void setSortKey( String sortKey ) {
		this.sortKey = sortKey;
	}

	/**
	 * 通过排序key比较两个对象
	 */
	public int compareTo( Object o ) {
		int result = 0;
		String key = this.sortKey;
		if ( "orgName".endsWith( key ) ) {
			// result = this.orgName.compareTo( ( (MachineOpenRateBean) o ).orgName );
			result = this.compareLong( Long.valueOf( ( (MachineOpenRateBean) o ).orgId ), Long.valueOf( this.orgId ) );
		}
		else if ( "totalCount".endsWith( key ) ) {
			result = this.compareLong( this.totalCount, ( (MachineOpenRateBean) o ).totalCount );
		}
		else if ( "totalMachineCount".endsWith( key ) ) {
			result = this.compareLong( this.totalMachineCount, ( (MachineOpenRateBean) o ).totalMachineCount );
		}
		else if ( "openCount".endsWith( key ) ) {
			result = this.compareLong( this.openCount, ( (MachineOpenRateBean) o ).openCount );
		}
		else if ( "openMachineCount".endsWith( key ) ) {
			result = this.compareLong( this.openMachineCount, ( (MachineOpenRateBean) o ).openMachineCount );
		}
		else if ( "pauseCount".endsWith( key ) ) {
			result = this.compareLong( this.pauseCount, ( (MachineOpenRateBean) o ).pauseCount );
		}
		else if ( "pauseMachineCount".endsWith( key ) ) {
			result = this.compareLong( this.pauseMachineCount, ( (MachineOpenRateBean) o ).pauseMachineCount );
		}
		else if ( "normalRate".endsWith( key ) ) {
			result = this.compareDouble( this.doubleNormalRate, ( (MachineOpenRateBean) o ).doubleNormalRate );
		}
		else if ( "openRate".endsWith( key ) ) {
			result = this.compareDouble( this.doubleOpenRate, ( (MachineOpenRateBean) o ).doubleOpenRate );
		}

		return result;

	}

	/**
	 * 比较两个long型数字
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int compareLong( long x, long y ) {
		return this.compareDouble( (double) x, (double) y );
	}

	/**
	 * 比较两个double型数字
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private int compareDouble( double x, double y ) {
		int compareResult = 0;
		if ( x == y ) {
			compareResult = 0;
		}
		else if ( x > y ) {
			compareResult = -1;
		}
		else if ( x < y ) {
			compareResult = 1;
		}
		return compareResult;
	}

}
