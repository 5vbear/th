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
public class ChannelBean {
	
	//频道ID
	long channelId = 0;
	
	/**
	 * @return the channelId
	 */
	public long getChannelId() {
		return channelId;
	}

	/**
	 * @param channelId the channelId to set
	 */
	public void setChannelId( long channelId ) {
		this.channelId = channelId;
	}

	// 频道名称
	String channelName = "";

	// 频道路径
	String channelPath = "";

	// 频道类型 0-频道 1-快速入口
	String channelType = "";

	// 频道状态 1-启用 2-停用 3-删除
	String status = "";

	// 操作人
	String operator = "";

	/**
	 * @return the channelName
	 */
	public String getChannelName() {
		return channelName;
	}

	/**
	 * @param channelName
	 *            the channelName to set
	 */
	public void setChannelName( String channelName ) {
		this.channelName = channelName;
	}

	/**
	 * @return the channelPath
	 */
	public String getChannelPath() {
		return channelPath;
	}

	/**
	 * @param channelPath
	 *            the channelPath to set
	 */
	public void setChannelPath( String channelPath ) {
		this.channelPath = channelPath;
	}

	/**
	 * @return the channelType
	 */
	public String getChannelType() {
		return channelType;
	}

	/**
	 * @param channelType
	 *            the channelType to set
	 */
	public void setChannelType( String channelType ) {
		this.channelType = channelType;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus( String status ) {
		this.status = status;
	}

	/**
	 * @return the operator
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator( String operator ) {
		this.operator = operator;
	}
}
