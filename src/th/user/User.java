package th.user;

import java.util.List;

import th.com.util.Define;
import th.dao.ActionDealDAO;

public class User {
	private String id;
	
	private String org_id;
	
	
	private List<String> actionList;
	public String getOrg_id() {
		return org_id;
	}
	public void setOrg_id(String org_id) {
		this.org_id = org_id;
	}
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}

	private String department_id;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType( String type ) {
		this.type = type;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOperatetime() {
		return operatetime;
	}
	public void setOperatetime(String operatetime) {
		this.operatetime = operatetime;
	}
	
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperator() {
		return operator;
	}

	public void setActionList(List<String> actionList) {
		this.actionList = actionList;
	}
	public List<String> getActionList() {
		return actionList;
	}

	private String name;
	private String real_name;
	private String email;
	private String address;
	private String telephone;
	private String cellphone;
	private String description;
	private String type;
	private String status;
	private String operatetime;
	private String operator;
	
	/**
	 * 权限判定成员方法
	 * @param orgId 用户执行操作模块所在组织ID
	 * @param actionId 用户执行操作所需权限ID
	 * @return
	 */
	public boolean hasRight(String orgId,String actionId){

		if(Define.ADMIN.equals(this.name)){
			return true;
		}
		if(null == orgId||"".equals(orgId)||
				null == actionId||"".equals(actionId)||
					null == this.id||"".equals(this.id)){
			return false;
		}
		ActionDealDAO actionDao = new ActionDealDAO();
		boolean isAuthorized = actionDao.isAuthorized(this.id, this.org_id,orgId,actionId);
		return isAuthorized;
	}
}
