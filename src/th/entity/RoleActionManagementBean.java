/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 *
 */
public class RoleActionManagementBean {
	
	private long mappingId;
	private long actionId;
	private long roleId;
	private String operateTime;
	private long operator;
	
	public RoleActionManagementBean() {
		this.mappingId=-1;
		this.actionId=-1;
		this.roleId=-1;
		this.operateTime="";
		this.operator=-1;
	}
	
	/**
	 * @return the mappingId
	 */
	public long getMappingId() {
		return mappingId;
	}

	/**
	 * @param mappingId the mappingId to set
	 */
	public void setMappingId( long mappingId ) {
		this.mappingId = mappingId;
	}

	/**
	 * @return the actionId
	 */
	public long getActionId() {
		return actionId;
	}

	/**
	 * @param actionId the actionId to set
	 */
	public void setActionId( long actionId ) {
		this.actionId = actionId;
	}

	/**
	 * @return the roleId
	 */
	public long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId( long roleId ) {
		this.roleId = roleId;
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
