/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-8-17
 * @author PSET
 * @since JDK1.6
 *
 */
public class InternetBankingBean {
	
	private long devId;
	private String FatoryId;
	private String snNum;
	private String devType;
	private String remark;
	private String devStatus;
	private String operateTime;
	private long operator;
	public InternetBankingBean(){
		
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
	 * @return the fatoryId
	 */
	public String getFatoryId() {
		return FatoryId;
	}
	/**
	 * @param fatoryId the fatoryId to set
	 */
	public void setFatoryId( String fatoryId ) {
		FatoryId = fatoryId;
	}
	/**
	 * @return the snNum
	 */
	public String getSnNum() {
		return snNum;
	}
	/**
	 * @param snNum the snNum to set
	 */
	public void setSnNum( String snNum ) {
		this.snNum = snNum;
	}
	/**
	 * @return the devType
	 */
	public String getDevType() {
		return devType;
	}
	/**
	 * @param devType the devType to set
	 */
	public void setDevType( String devType ) {
		this.devType = devType;
	}
	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark( String remark ) {
		this.remark = remark;
	}
	/**
	 * @return the devStatus
	 */
	public String getDevStatus() {
		return devStatus;
	}
	/**
	 * @param devStatus the devStatus to set
	 */
	public void setDevStatus( String devStatus ) {
		this.devStatus = devStatus;
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
