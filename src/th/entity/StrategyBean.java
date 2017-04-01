/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.entity;

/**
 * Descriptions
 *
 * @version 2013-9-11
 * @author PSET
 * @since JDK1.6
 *
 */
public class StrategyBean {

	private long stgId;
	private String stgName;
	private String stgType;
	private String objBegin;
	private String objEnd;
	private String operateTime;
	private long operator;

	public StrategyBean(){
		this.stgId = -1;
		this.stgName = "";
		this.stgType = "1";
		this.objBegin = "";
		this.objEnd = "";
		this.operateTime = "";
		this.operator = -1;
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
	 * @return the stgType
	 */
	public String getStgType() {
		return stgType;
	}

	/**
	 * @param stgType the stgType to set
	 */
	public void setStgType( String stgType ) {
		this.stgType = stgType;
	}

	/**
	 * @return the objBegin
	 */
	public String getObjBegin() {
		return objBegin;
	}

	/**
	 * @param objBegin the objBegin to set
	 */
	public void setObjBegin( String objBegin ) {
		this.objBegin = objBegin;
	}

	/**
	 * @return the objEnd
	 */
	public String getObjEnd() {
		return objEnd;
	}

	/**
	 * @param objEnd the objEnd to set
	 */
	public void setObjEnd( String objEnd ) {
		this.objEnd = objEnd;
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
