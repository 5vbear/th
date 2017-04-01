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
public class ChannelUseOrgBean implements Comparable<Object> {
	private String orgId; // 组织ID
	private String orgName; // 组织名称
	private ArrayList<ChannelUseBean> channelUseList = new ArrayList<ChannelUseBean>();// 频道点击列表
	private long totalCount; // 总次数
	private long totalMachineCount; // 总台数
	private double averageCount; // 每台平均次数
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
	 * @return the averageCount
	 */
	public double getAverageCount() {
		return averageCount;
	}

	/**
	 * @param averageCount
	 *            the averageCount to set
	 */
	public void setAverageCount( double averageCount ) {
		this.averageCount = averageCount;
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
			result = this.compareLong( Long.valueOf( ( (ChannelUseOrgBean) o ).orgId ), Long.valueOf( this.orgId ) );
			// result = this.orgName.compareTo( ( (ChannelUseOrgBean) o ).orgName );
		}
		else if ( "totalCount".endsWith( key ) ) {
			result = this.compareLong( this.totalCount, ( (ChannelUseOrgBean) o ).totalCount );
		}
		else if ( "totalMachineCount".endsWith( key ) ) {
			result = this.compareLong( this.totalMachineCount, ( (ChannelUseOrgBean) o ).totalMachineCount );
		}

		else if ( "averageCount".endsWith( key ) ) {
			result = this.compareDouble( this.averageCount, ( (ChannelUseOrgBean) o ).averageCount );
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
