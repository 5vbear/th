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
public class AdvertPlayDetailBean implements Comparable<Object> {
	private String orgId; //组织ID
	private String orgName; //组织名称
	private String machineId; //机器ID
	private String machineName; //机器名称	
	private String mediaName;//素材名称
	private long realPlayTime; //实际播放时长（秒）
	private long clickCount; // 点击次数
	private String sortKey;  // 排序
	private String startPlayTime;
	private String endPlayTime;
	private String clickStatus;
	private String billId;
	private String billName;
	private String layoutName;
	
	
	public String getLayoutName() {
		return layoutName;
	}
	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}
	public String getBillName() {
		return billName;
	}
	public void setBillName(String billName) {
		this.billName = billName;
	}
	/**
	 * @return the orgId
	 */
	public String getOrgId() {
		return orgId;
	}
	/**
	 * @param orgId the orgId to set
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
	 * @param orgName the orgName to set
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
	 * @param machineId the machineId to set
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
	 * @param machineName the machineName to set
	 */
	public void setMachineName( String machineName ) {
		this.machineName = machineName;
	}
	/**
	 * @return the mediaName
	 */
	public String getMediaName() {
		return mediaName;
	}
	/**
	 * @param mediaName the mediaName to set
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
	 * @param realPlayTime the realPlayTime to set
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
	 * @param clickCount the clickCount to set
	 */
	public void setClickCount( long clickCount ) {
		this.clickCount = clickCount;
	}
	/**
	 * @return the sortKey
	 */
	public String getSortKey() {
		return sortKey;
	}
	/**
	 * @param sortKey the sortKey to set
	 */
	public void setSortKey( String sortKey ) {
		this.sortKey = sortKey;
	}
	
	public String getStartPlayTime() {
		return startPlayTime;
	}
	public void setStartPlayTime(String startPlayTime) {
		this.startPlayTime = startPlayTime;
	}
	public String getEndPlayTime() {
		return endPlayTime;
	}
	public void setEndPlayTime(String endPlayTime) {
		this.endPlayTime = endPlayTime;
	}
	public String getClickStatus() {
		return clickStatus;
	}
	public void setClickStatus(String clickStatus) {
		this.clickStatus = clickStatus;
	}
	
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	/**
	 * 通过排序key比较两个对象
	 */
	public int compareTo( Object o ) {
		int result = 0;
		String key = this.sortKey;
		if ( "detailMachineName".endsWith( key ) ) {
			result = this.machineName.compareTo( ( (AdvertPlayDetailBean) o ).machineName );
		}		
		else if ( "detailOrgName".endsWith( key ) ) {
			//result = this.orgName.compareTo( ( (AdvertPlayDetailBean) o ).orgName );
			result = this.compareLong( Long.valueOf( ( (AdvertPlayDetailBean) o ).orgId ), Long.valueOf( this.orgId ) );
		}
		else if ( "detailMediaName".endsWith( key ) ) {
			result = this.mediaName.compareTo( ( (AdvertPlayDetailBean) o ).mediaName );
		}		
		else if ( "detailRealPlayTime".endsWith( key ) ) {
			result = this.compareLong( this.realPlayTime, ( (AdvertPlayDetailBean) o ).realPlayTime );
		}
		else if ( "detailClickCount".endsWith( key ) ) {
			result = this.compareLong( this.clickCount, ( (AdvertPlayDetailBean) o ).clickCount );
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
		return this.compareDouble( (double)x, (double)y );
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
