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
public class RoleObjectManagementBean {
	
	private long mappingId;
	private long roleId;
	private long objectId;
	private String objectType;
	private String operateTime;
	private long operator;
	
	public RoleObjectManagementBean(){
		this.mappingId = -1;
		this.roleId = -1;
		this.objectId = -1;
		this.objectType = "";
		this.operateTime = "";
		this.operator = -1;
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
	 * @return the objectId
	 */
	public long getObjectId() {
		return objectId;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId( long objectId ) {
		this.objectId = objectId;
	}

	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return objectType;
	}

	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType( String objectType ) {
		this.objectType = objectType;
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
