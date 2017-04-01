/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

import java.util.ArrayList;

/**
 * Descriptions
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ChannelUseDetailBean implements Comparable<Object> {
	private String machineId; // 机器ID
	private String machineName; // 机器名称
	private String orgId; // 组织ID
	private String orgName; // 组织名称
	private ArrayList<ChannelUseBean> channelUseList = new ArrayList<ChannelUseBean>();// 频道点击列表
	private long totalCount; // 总次数
	private String sortKey; // 排序

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
	 * @return the channelUseList
	 */
	public ArrayList<ChannelUseBean> getChannelUseList() {
		return channelUseList;
	}

	/**
	 * @param channelUseList
	 *            the channelUseList to set
	 */
	public void setChannelUseList( ArrayList<ChannelUseBean> channelUseList ) {
		this.channelUseList = channelUseList;
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
			// result = this.orgName.compareTo( ( (ChannelUseDetailBean) o ).orgName );
			result = this.compareLong( Long.valueOf( ( (ChannelUseDetailBean) o ).orgId ), Long.valueOf( this.orgId ) );
		}
		else if ( "detailMachineName".endsWith( key ) ) {
			result = this.machineName.compareTo( ( (ChannelUseDetailBean) o ).machineName );
		}
		else if ( "detailTotalCount".endsWith( key ) ) {
			result = this.compareLong( this.totalCount, ( (ChannelUseDetailBean) o ).totalCount );
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
