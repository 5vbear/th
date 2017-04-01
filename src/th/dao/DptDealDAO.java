/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.SQLException;
import java.util.HashMap;

import th.entity.DepartmentBean;
import th.entity.SearchConditionBean;

/**
 * Descriptions
 *
 * @version 2013-8-14
 * @author PSET
 * @since JDK1.6
 *
 */
public class DptDealDAO extends BaseDao {

	public DptDealDAO(){

	}
	
	public HashMap[] getAllDptsByOrgId(long orgId) throws SQLException {

		String FUNCTION_NAME = "getAllDptsByOrgId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_department where org_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "org_id = " + orgId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1 , orgId );
			

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getAllDptsByOrgIdStr(String inOrgIdStr) throws SQLException {

		String FUNCTION_NAME = "getAllDptsByOrgIdStr() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_department where org_id in " + inOrgIdStr );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getDepartmentByDptId(long dptId) throws SQLException {

		String FUNCTION_NAME = "getDepartmentByDptId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_department where department_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "department_id = " + dptId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1 , dptId );
			

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public DepartmentBean getDptBeanByDptId(long dptId) throws SQLException {

		String FUNCTION_NAME = "getDptBeanByDptId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_department where department_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "department_id = " + dptId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1 , dptId );
			
			DepartmentBean dpt = new DepartmentBean();
			HashMap[] map = select();
			
			if(map!=null&&map.length==1){
				dpt.setDptId( Long.parseLong( (String) map[0].get("DEPARTMENT_ID") ) );
				dpt.setOrgId( Long.parseLong( (String) map[0].get( "ORG_ID" ) ) );
				dpt.setDptName( (String) map[0].get( "DEPARTMENT_NAME" ) ) ;
				dpt.setDptDescription( (String) map[0].get( "DESCRIPTION" ) );
				dpt.setManagerName( (String) map[0].get( "MANAGER_NAME" ) );
				dpt.setManagerMail( (String) map[0].get( "MANAGER_EMAIL" ) );
				dpt.setManagerTel( (String) map[0].get( "MANAGER_TELEPHONE" ) );
				dpt.setOtherContacts( (String) map[0].get( "OTHER_CONTACTS" ) );
				dpt.setOperateTime( (String) map[0].get( "OPERATETIME" ) );
				dpt.setOperator( Long.parseLong( (String) map[0].get( "OPERATOR" ) ) );
			}

			return dpt;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public long getDptIdByDptNameAndOrgId(long orgId, String dptName) throws SQLException {

		String FUNCTION_NAME = "getDptIdByDptNameAndOrgId() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long dptId = -1;

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_department where org_id = ? and department_name = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "department_name = " + dptName);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1 , orgId );
			stmt.setString( 2 , dptName );
			

			HashMap[] map = select();

			if(map!=null&&map.length==1){
				dptId = Long.parseLong( (String) map[0].get( "DEPARTMENT_ID" ) );
			}
			
			return dptId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getDepartmentBySearchCondition(SearchConditionBean scb) throws SQLException {

		String FUNCTION_NAME = "getDepartmentBySearchCondition() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long orgId = scb.getOrgId();
		long dptId = scb.getDptId();
		String orgName = scb.getOrgName();
		String dptName = scb.getDptName();
		String managerName = scb.getManagerName();
		String  searchOrgStr = scb.getSearchOrgStr();

		try{

			StringBuffer sb = new StringBuffer();

			sb.append( "select tcd.*, tco.org_name " );
			sb.append( "from tb_ccb_department tcd, tb_ccb_organization tco " );
			sb.append( "where tcd.org_id = tco.org_id " );
			sb.append( "and tco.org_id in " + searchOrgStr + " " );
			if(orgId!=-1){
				sb.append( "and tcd.org_id = " + orgId + " " );
			}
			if(dptId!=-1){
				sb.append( "and tcd.department_id = " + dptId + " " );
			}
			if(!"".equals( orgName )){
				sb.append( "and tco.org_name = '" + orgName + "' " );
			}
			if(!"".equals( dptName )){
				sb.append( "and tcd.department_name = '" + dptName + "' " );
			}
			if(!"".equals( managerName )){
				sb.append( "and tcd.manager_name = '" + managerName + "' " );
			}
			sb.append( "order by tcd.operatetime desc" );
			

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "department_id = " + dptId);
			logger.info(FUNCTION_NAME + "org_name = " + orgName);
			logger.info(FUNCTION_NAME + "department_name = " + dptName);
			logger.info(FUNCTION_NAME + "manager_name = " + managerName);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public long insertDepartmentRecord(DepartmentBean dpt) throws SQLException {

		String FUNCTION_NAME = "insertDepartmentRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long dptId = getSequence( "seq_tb_ccb_organization" );
		long orgId = dpt.getOrgId();
		String dptName = dpt.getDptName();
		String managerName = dpt.getManagerName();
		String managerTel = dpt.getManagerTel();
		long operator = dpt.getOperator();
		String dptDescription = dpt.getDptDescription();
		String managerMail = dpt.getManagerMail();
		String otherContacts = dpt.getOtherContacts();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_ccb_department " );
			sb.append( "( department_id, org_id, department_name, manager_name, manager_telephone, operatetime, operator, description, manager_email, other_contacts ) " );
			sb.append( "values (?,?,?,?,?,date_trunc( 'second', current_timestamp ),?,?,?,?)"  );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "department_id = " + dptId);
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "department_name = " + dptName);
			logger.info(FUNCTION_NAME + "manager_name = " + managerName);
			logger.info(FUNCTION_NAME + "manager_telephone = " + managerTel);
			logger.info(FUNCTION_NAME + "operator = " + operator);
			logger.info(FUNCTION_NAME + "description = " + dptDescription);
			logger.info(FUNCTION_NAME + "manager_email = " + managerMail);
			logger.info(FUNCTION_NAME + "other_contacts = " + otherContacts);



			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, dptId );
			stmt.setLong( 2, orgId );
			stmt.setString( 3 , dptName );
			stmt.setString( 4, managerName );
			stmt.setString( 5, managerTel );
			stmt.setLong( 6 , operator );
			stmt.setString( 7, dptDescription );
			stmt.setString( 8, managerMail );
			stmt.setString( 9, otherContacts );

			insert();
			commit();
			
			return dptId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void updateDepartmentRecord(DepartmentBean dpt) throws SQLException {

		String FUNCTION_NAME = "updateDepartmentRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long dptId = dpt.getDptId();
		long orgId = dpt.getOrgId();
		String dptName = dpt.getDptName();
		String managerName = dpt.getManagerName();
		String managerTel = dpt.getManagerTel();
		long operator = dpt.getOperator();
		String dptDescription = dpt.getDptDescription();
		String managerMail = dpt.getManagerMail();
		String otherContacts = dpt.getOtherContacts();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_ccb_department " );
			sb.append( "set org_id = ?, " );
			sb.append( "department_name = ?, " );
			sb.append( "manager_name = ?, " );
			sb.append( "manager_telephone = ?, " );
			sb.append( "description = ?, " );
			sb.append( "manager_email = ?, " );
			sb.append( "other_contacts = ?, " );
			sb.append( "operatetime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "operator = ? " );
			sb.append( "where department_id = ?" );
			

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "department_name = " + dptName);
			logger.info(FUNCTION_NAME + "manager_name = " + managerName);
			logger.info(FUNCTION_NAME + "manager_telephone = " + managerTel);
			logger.info(FUNCTION_NAME + "operator = " + operator);
			logger.info(FUNCTION_NAME + "description = " + dptDescription);
			logger.info(FUNCTION_NAME + "manager_email = " + managerMail);
			logger.info(FUNCTION_NAME + "other_contacts = " + otherContacts);
			logger.info(FUNCTION_NAME + "department_id = " + dptId);



			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, orgId );
			stmt.setString( 2 , dptName );
			stmt.setString( 3, managerName );
			stmt.setString( 4, managerTel );			
			stmt.setString( 5, dptDescription );
			stmt.setString( 6, managerMail );
			stmt.setString( 7, otherContacts );
			stmt.setLong( 8 , operator );
			stmt.setLong( 9, dptId );

			update();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	
	
	
	public void updateDptInfo(DepartmentBean dpt) throws SQLException {

		String FUNCTION_NAME = "updateDptInfo() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long dptId = dpt.getDptId();
		long orgId = dpt.getOrgId();
		String dptName = dpt.getDptName();
		String managerName = dpt.getManagerName();
		String managerTel = dpt.getManagerTel();
		long operator = dpt.getOperator();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_ccb_department " );
			sb.append( "set org_id = ?, " );
			sb.append( "department_name = ?, " );
			sb.append( "manager_name = ?, " );
			sb.append( "manager_telephone = ?, " );
			sb.append( "operatetime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "operator = ? " );
			sb.append( "where department_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "department_id = " + dptId);
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "department_name = " + dptName);
			logger.info(FUNCTION_NAME + "manager_name = " + managerName);
			logger.info(FUNCTION_NAME + "manager_telephone = " + managerTel);
			logger.info(FUNCTION_NAME + "operator = " + operator);



			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, orgId );
			stmt.setString( 2 , dptName );
			stmt.setString( 3, managerName );
			stmt.setString( 4, managerTel );
			stmt.setLong( 5 , operator );
			stmt.setLong( 6, dptId );

			update();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void deleteDptByDptID(long DptId) throws SQLException {

		String FUNCTION_NAME = "deleteDptByDptID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_ccb_department where department_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "department_id = " + DptId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, DptId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	
	

}
