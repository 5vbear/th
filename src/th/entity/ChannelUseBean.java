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
public class ChannelUseBean {
	private String channelId;//频道ID
	private String channelName; //频道名称
	private long clickCount; //点击次数
	private long machineCount; //机器数
	
	/**
	 * @return the channelId
	 */
	public String getChannelId() {
		return channelId;
	}
	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId( String channelId ) {
		this.channelId = channelId;
	}
	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	}
	/**
	 * @param channelName the channelName to set
	 */
	public void setChannelName( String channelName ) {
		this.channelName = channelName;
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
	 * @return the machineCount
	 */
	public long getMachineCount() {
		return machineCount;
	}
	/**
	 * @param machineCount the machineCount to set
	 */
	public void setMachineCount( long machineCount ) {
		this.machineCount = machineCount;
	}
	
}
