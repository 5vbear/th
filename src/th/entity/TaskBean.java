/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2014-1-6
 * @author PSET
 * @since JDK1.6
 *
 */
public class TaskBean {

	private long stgId;
	private String stgName;
	private String stgDesp;
	private String sendInternal;
	private int sendTimeHour;
	private int sendTimeMinute;
	private int sendTimeSecond;
	private String sendType;
	private String deliverTerminal;
	private String deliverRoleList;
	private String reportNameList;
	private String status;
	private String operateTime;
	private long operator;
	private String taskStartTime;
	private String taskPreTime;
	private String taskNextTime;
	private String modifyTime;
	private long Modifyuser;


	public TaskBean(){
		this.stgId = -1;
		this.stgName = "";
		this.stgDesp = "";
		this.sendInternal = "";
		this.sendTimeHour = 0;
		this.sendTimeMinute = 0;
		this.sendTimeSecond = 0;
		this.sendType = "";
		this.deliverTerminal = "";
		this.deliverRoleList = "";
		this.reportNameList = "";
		this.status = "0";
		this.operateTime = "";
		this.operator = -1;
		this.taskStartTime = "";
		this.taskPreTime = "";
		this.taskNextTime = "";
		this.modifyTime = "";
		this.Modifyuser = -1;
	}

	/**
	 * @return the stgId
	 */
	public long getStgId() {
		return stgId;
	}


	/**
	 * @param stgId the stgId to set
	 */
	public void setStgId( long stgId ) {
		this.stgId = stgId;
	}


	/**
	 * @return the stgName
	 */
	public String getStgName() {
		return stgName;
	}


	/**
	 * @param stgName the stgName to set
	 */
	public void setStgName( String stgName ) {
		this.stgName = stgName;
	}


	/**
	 * @return the stgDesp
	 */
	public String getStgDesp() {
		return stgDesp;
	}


	/**
	 * @param stgDesp the stgDesp to set
	 */
	public void setStgDesp( String stgDesp ) {
		this.stgDesp = stgDesp;
	}


	/**
	 * @return the sendInternal
	 */
	public String getSendInternal() {
		return sendInternal;
	}


	/**
	 * @param sendInternal the sendInternal to set
	 */
	public void setSendInternal( String sendInternal ) {
		this.sendInternal = sendInternal;
	}


	/**
	 * @return the sendType
	 */
	public String getSendType() {
		return sendType;
	}


	/**
	 * @param sendType the sendType to set
	 */
	public void setSendType( String sendType ) {
		this.sendType = sendType;
	}


	/**
	 * @return the deliverTerminal
	 */
	public String getDeliverTerminal() {
		return deliverTerminal;
	}


	/**
	 * @param deliverTerminal the deliverTerminal to set
	 */
	public void setDeliverTerminal( String deliverTerminal ) {
		this.deliverTerminal = deliverTerminal;
	}


	/**
	 * @return the reportNameList
	 */
	public String getReportNameList() {
		return reportNameList;
	}


	/**
	 * @param reportNameList the reportNameList to set
	 */
	public void setReportNameList( String reportNameList ) {
		this.reportNameList = reportNameList;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}


	/**
	 * @param status the status to set
	 */
	public void setStatus( String status ) {
		this.status = status;
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


	/**
	 * @return the taskStartTime
	 */
	public String getTaskStartTime() {
		return taskStartTime;
	}


	/**
	 * @param taskStartTime the taskStartTime to set
	 */
	public void setTaskStartTime( String taskStartTime ) {
		this.taskStartTime = taskStartTime;
	}


	/**
	 * @return the taskPreTime
	 */
	public String getTaskPreTime() {
		return taskPreTime;
	}


	/**
	 * @param taskPreTime the taskPreTime to set
	 */
	public void setTaskPreTime( String taskPreTime ) {
		this.taskPreTime = taskPreTime;
	}


	/**
	 * @return the taskNextTime
	 */
	public String getTaskNextTime() {
		return taskNextTime;
	}


	/**
	 * @param taskNextTime the taskNextTime to set
	 */
	public void setTaskNextTime( String taskNextTime ) {
		this.taskNextTime = taskNextTime;
	}


	/**
	 * @return the modifyTime
	 */
	public String getModifyTime() {
		return modifyTime;
	}


	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime( String modifyTime ) {
		this.modifyTime = modifyTime;
	}


	/**
	 * @return the modifyuser
	 */
	public long getModifyuser() {
		return Modifyuser;
	}


	/**
	 * @param modifyuser the modifyuser to set
	 */
	public void setModifyuser( long modifyuser ) {
		Modifyuser = modifyuser;
	}

	/**
	 * @return the sendTimeHour
	 */
	public int getSendTimeHour() {
		return sendTimeHour;
	}

	/**
	 * @param sendTimeHour the sendTimeHour to set
	 */
	public void setSendTimeHour( int sendTimeHour ) {
		this.sendTimeHour = sendTimeHour;
	}

	/**
	 * @return the sendTimeMinute
	 */
	public int getSendTimeMinute() {
		return sendTimeMinute;
	}

	/**
	 * @param sendTimeMinute the sendTimeMinute to set
	 */
	public void setSendTimeMinute( int sendTimeMinute ) {
		this.sendTimeMinute = sendTimeMinute;
	}

	/**
	 * @return the sendTimeSecond
	 */
	public int getSendTimeSecond() {
		return sendTimeSecond;
	}

	/**
	 * @param sendTimeSecond the sendTimeSecond to set
	 */
	public void setSendTimeSecond( int sendTimeSecond ) {
		this.sendTimeSecond = sendTimeSecond;
	}

	/**
	 * @return the deliverRoleList
	 */
	public String getDeliverRoleList() {
		return deliverRoleList;
	}

	/**
	 * @param deliverRoleList the deliverRoleList to set
	 */
	public void setDeliverRoleList( String deliverRoleList ) {
		this.deliverRoleList = deliverRoleList;
	}



}
