/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 * 
 * @version 2013-8-14
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ClientUseBean implements Comparable<Object> {

	// all data
	private String orgName;
	private String clickCount;
	private String validTime;
	private String openTime;
	private Double useRate;
	private Double freeUseRate;
	private int index;
	private String rate;
	/**
	 * @return the rate
	 */
	public String getRate() {
		return rate;
	}

	/**
	 * @param rate the rate to set
	 */
	public void setRate( String rate ) {
		this.rate = rate;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex( int index ) {
		this.index = index;
	}

	/**
	 * @return the freeUseRate
	 */
	public Double getFreeUseRate() {
		return freeUseRate;
	}

	/**
	 * @param freeUseRate the freeUseRate to set
	 */
	public void setFreeUseRate( Double freeUseRate ) {
		this.freeUseRate = freeUseRate;
	}

	private String sortKey; // 排序

	// detail data
	private String branches;// 分行

	/**
	 * @return the branches
	 */
	public String getBranches() {
		return branches;
	}

	/**
	 * @param branches
	 *            the branches to set
	 */
	public void setBranches( String branches ) {
		this.branches = branches;
	}

	/**
	 * @return the departId
	 */
	public String getDepartId() {
		return departId;
	}

	/**
	 * @param departId
	 *            the departId to set
	 */
	public void setDepartId( String departId ) {
		this.departId = departId;
	}

	/**
	 * @return the departName
	 */
	public String getDepartName() {
		return departName;
	}

	/**
	 * @param departName
	 *            the departName to set
	 */
	public void setDepartName( String departName ) {
		this.departName = departName;
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
	 * @return the machineType
	 */
	public String getMachineType() {
		return machineType;
	}

	/**
	 * @param machineType
	 *            the machineType to set
	 */
	public void setMachineType( String machineType ) {
		this.machineType = machineType;
	}

	/**
	 * @return the freeTime
	 */
	public String getFreeTime() {
		return freeTime;
	}

	/**
	 * @param freeTime
	 *            the freeTime to set
	 */
	public void setFreeTime( String freeTime ) {
		this.freeTime = freeTime;
	}

	private String departId;
	private String departName;
	private String machineId;
	private String machineType;
	private String freeTime;

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
	 * @return the clickCount
	 */
	public String getClickCount() {
		return clickCount;
	}

	/**
	 * @param clickCount
	 *            the clickCount to set
	 */
	public void setClickCount( String clickCount ) {
		this.clickCount = clickCount;
	}

	/**
	 * @return the validTime
	 */
	public String getValidTime() {
		return validTime;
	}

	/**
	 * @param validTime
	 *            the validTime to set
	 */
	public void setValidTime( String validTime ) {
		this.validTime = validTime;
	}

	/**
	 * @return the openTime
	 */
	public String getOpenTime() {
		return openTime;
	}

	/**
	 * @param openTime
	 *            the openTime to set
	 */
	public void setOpenTime( String openTime ) {
		this.openTime = openTime;
	}

	/**
	 * @return the useRate
	 */
	public Double getUseRate() {
		return useRate;
	}

	/**
	 * @param useRate
	 *            the useRate to set
	 */
	public void setUseRate( Double useRate ) {
		this.useRate = useRate;
	}

	/**
	 * 通过排序key比较两个对象
	 */
	public int compareTo( Object o ) {
		int result = 0;
		String key = this.sortKey;
		if ( "useRate".endsWith( key ) ) {
			result = this.compareDouble( this.useRate, ( (ClientUseBean) o ).useRate );
		}
		if ( "freeUseRate".endsWith( key ) ) {
			result = this.compareDouble( this.freeUseRate, ( (ClientUseBean) o ).freeUseRate );
		}
		return result;

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
