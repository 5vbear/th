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
public class MachineOpenDetailBean implements Comparable<Object> {
	private String orgId; // 组织ID
	private String orgName;// 组织名称
	private String machineId; // 机器ID
	private String machineName; // 机器名称
	private long openCount; // 开机次数
	private long pauseCount; // 报停次数
	private long currentStatus; // 当前状态
	private String currentStatusValue; // 当前状态的值
	private double doubleNormalRate; // 正常运行率（小数）
	private String normalRate; // 正常运行率（百分比）
	private String sortKey; // 排序

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
	 * @return the machineId
	 */
	public String getMachineId() {
		return machineId;
	}

	/**
	 * @param machineId
	 *            the machineId to set
	 */
	public void setMachineId( String machineId ) {
		this.machineId = machineId;
	}

	/**
	 * @return the machineName
	 */
	public String getMachineName() {
		return machineName;
	}

	/**
	 * @param machineName
	 *            the machineName to set
	 */
	public void setMachineName( String machineName ) {
		this.machineName = machineName;
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
	 * @return the currentStatus
	 */
	public long getCurrentStatus() {
		return currentStatus;
	}

	/**
	 * @param currentStatus
	 *            the currentStatus to set
	 */
	public void setCurrentStatus( long currentStatus ) {
		this.currentStatus = currentStatus;
	}

	/**
	 * @return the currentStatusValue
	 */
	public String getCurrentStatusValue() {
		return currentStatusValue;
	}

	/**
	 * @param currentStatusValue
	 *            the currentStatusValue to set
	 */
	public void setCurrentStatusValue( String currentStatusValue ) {
		this.currentStatusValue = currentStatusValue;
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
		if ( "detailOrgName".endsWith( key ) ) {
			// result = this.orgName.compareTo( ( (MachineOpenDetailBean) o ).orgName );
			result = this.compareLong( Long.valueOf( ( (MachineOpenDetailBean) o ).orgId ), Long.valueOf( this.orgId ) );
		}
		else if ( "detailOpenCount".endsWith( key ) ) {
			result = this.compareLong( this.openCount, ( (MachineOpenDetailBean) o ).openCount );
		}
		else if ( "detailPauseCount".endsWith( key ) ) {
			result = this.compareLong( this.pauseCount, ( (MachineOpenDetailBean) o ).pauseCount );
		}
		else if ( "detailCurrentStatus".endsWith( key ) ) {
			result = this.compareLong( this.currentStatus, ( (MachineOpenDetailBean) o ).currentStatus );
		}
		else if ( "detailNormalRate".endsWith( key ) ) {
			result = this.compareDouble( this.doubleNormalRate, ( (MachineOpenDetailBean) o ).doubleNormalRate );
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
