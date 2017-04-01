/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-9-12
 * @author PSET
 * @since JDK1.6
 *
 */
public class EBankDeviceBean {
	
	private long devId;
	private String devOs;
	private String devDesp;
	private String operateTime;
	private long operator;
	
	public EBankDeviceBean(){
		this.devId = -1;
		this.devOs = "";
		this.devDesp = "";
		this.operateTime = "";
		this.operator = -1;
	}
	
	/**
	 * @return the devId
	 */
	public long getDevId() {
		return devId;
	}

	/**
	 * @param devId the devId to set
	 */
	public void setDevId( long devId ) {
		this.devId = devId;
	}

	/**
	 * @return the devOs
	 */
	public String getDevOs() {
		return devOs;
	}

	/**
	 * @param devOs the devOs to set
	 */
	public void setDevOs( String devOs ) {
		this.devOs = devOs;
	}

	/**
	 * @return the devDesp
	 */
	public String getDevDesp() {
		return devDesp;
	}

	/**
	 * @param devDesp the devDesp to set
	 */
	public void setDevDesp( String devDesp ) {
		this.devDesp = devDesp;
	}

	/**
	 * @return the operateTime
	 */
	public String getOperateTime() {
		return operateTime;
	}

	/**
	 * @param operateTime the operateTime to set
	 */
	public void setOperateTime( String operateTime ) {
		this.operateTime = operateTime;
	}

	/**
	 * @return the operator
	 */
	public long getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator( long operator ) {
		this.operator = operator;
	}

	

}
