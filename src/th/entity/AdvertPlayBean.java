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
public class AdvertPlayBean implements Comparable<Object> {
	private String orgId; // 组织ID
	private String orgName; // 组织名称
	private String mediaName;// 素材名称
	private long realPlayTime; // 实际播放时长（秒）
	private long clickCount; // 热链点击次数
	private long totalMachineCount; // 总台数
	private long averagePlayTime; // 平均每台播放时长（秒）
	private long averageClickCount; // 平均每台点击次数
	private String sortKey; // 排序
	private String billId;//节目单ID
	private String billName = "节目单";//
	private String showBillName;
	private String layoutName;

	
	public String getShowBillName() {
		return showBillName;
	}

	public void setShowBillName(String showBillName) {
		this.showBillName = showBillName;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
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
	 * @return the mediaName
	 */
	public String getMediaName() {
		return mediaName;
	}

	/**
	 * @param mediaName
	 *            the mediaName to set
	 */
	public void setMediaName( String mediaName ) {
		this.mediaName = mediaName;
	}

	/**
	 * @return the realPlayTime
	 */
	public long getRealPlayTime() {
		return realPlayTime;
	}

	/**
	 * @param realPlayTime
	 *            the realPlayTime to set
	 */
	public void setRealPlayTime( long realPlayTime ) {
		this.realPlayTime = realPlayTime;
	}

	/**
	 * @return the clickCount
	 */
	public long getClickCount() {
		return clickCount;
	}

	/**
	 * @param clickCount
	 *            the clickCount to set
	 */
	public void setClickCount( long clickCount ) {
		this.clickCount = clickCount;
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
	 * @return the averagePlayTime
	 */
	public long getAveragePlayTime() {
		return averagePlayTime;
	}

	/**
	 * @param averagePlayTime
	 *            the averagePlayTime to set
	 */
	public void setAveragePlayTime( long averagePlayTime ) {
		this.averagePlayTime = averagePlayTime;
	}

	/**
	 * @return the averageClickCount
	 */
	public long getAverageClickCount() {
		return averageClickCount;
	}

	/**
	 * @param averageClickCount
	 *            the averageClickCount to set
	 */
	public void setAverageClickCount( long averageClickCount ) {
		this.averageClickCount = averageClickCount;
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

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	/**
	 * 通过排序key比较两个对象
	 */
	public int compareTo( Object o ) {
		int result = 0;
		String key = this.sortKey;
		if ( "orgName".endsWith( key ) ) {
			// result = this.orgName.compareTo( ( (AdvertPlayBean) o ).orgName );
			result = this.compareLong( Long.valueOf( ( (AdvertPlayBean) o ).orgId ), Long.valueOf( this.orgId ) );
		}
		else if ( "mediaName".endsWith( key ) ) {
			result = this.mediaName.compareTo( ( (AdvertPlayBean) o ).mediaName );
		}
		else if ( "realPlayTime".endsWith( key ) ) {
			result = this.compareLong( this.realPlayTime, ( (AdvertPlayBean) o ).realPlayTime );
		}
		else if ( "clickCount".endsWith( key ) ) {
			result = this.compareLong( this.clickCount, ( (AdvertPlayBean) o ).clickCount );
		}
		else if ( "totalMachineCount".endsWith( key ) ) {
			result = this.compareLong( this.totalMachineCount, ( (AdvertPlayBean) o ).totalMachineCount );
		}
		else if ( "averagePlayTime".endsWith( key ) ) {
			result = this.compareLong( this.averagePlayTime, ( (AdvertPlayBean) o ).averagePlayTime );
		}
		else if ( "averageClickCount".endsWith( key ) ) {
			result = this.compareLong( this.averageClickCount, ( (AdvertPlayBean) o ).averageClickCount );
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
