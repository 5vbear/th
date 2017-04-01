/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.SQLException;
import java.util.HashMap;

import th.entity.RoleActionManagementBean;
import th.entity.RoleBean;

/**
 * Descriptions
 *
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 *
 */
public class RoleDealDAO extends BaseDao{
		
	public RoleDealDAO(){

	}
		
	public HashMap[] getRoleByRoleName(String roleName) throws SQLException {

		String FUNCTION_NAME = "getRoleByRoleID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_role where role_name = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_name = " + roleName);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1 , roleName );
			

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public int checkRoleName(String roleName) throws SQLException {

		String FUNCTION_NAME = "checkRoleName() ";
		logger.info( FUNCTION_NAME + "start" );
		int cnt = 0;

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select count(1) from tb_ccb_role where role_name = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_name = " + roleName);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1 , roleName );

			HashMap[] map = select();
			
			if(map!=null&&map.length==1){
				cnt = Integer.parseInt( (String)map[0].get("COUNT") );
			}

			return cnt;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public String getRoleNameByRoleID(long roleId) throws SQLException {

		String FUNCTION_NAME = "getRoleNameByRoleID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select role_name from tb_ccb_role where role_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_id = " + roleId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1 , roleId );
			

			HashMap[] map = select();
			
			String roleName = "";
			if(map!=null&&map.length==1){
				roleName = map[0].get("ROLE_NAME").toString();
			}

			return roleName;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public long insertRoleRecord(RoleBean role) throws SQLException {

		String FUNCTION_NAME = "getRoleByRoleName() ";
		logger.info( FUNCTION_NAME + "start" );
		
		String roleName = role.getRoleName();
		String status = role.getStatus();
		long operator = role.getOperator();
		long roleId = getSequence( "seq_tb_ccb_role" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_ccb_role " );
			sb.append( "( role_id, role_name, status, operatetime, operator ) " );
			sb.append( "values (?,?,?,date_trunc( 'second', current_timestamp ),?)" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_id = " + roleId);
			logger.info(FUNCTION_NAME + "role_name = " + roleName);
			logger.info(FUNCTION_NAME + "operator = " + operator);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, roleId );
			stmt.setString( 2 , roleName );
			stmt.setString( 3 , status );
			stmt.setLong( 4, operator );

			insert();
			commit();
			
			return roleId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public long updateRoleRecord(RoleBean role) throws SQLException {

		String FUNCTION_NAME = "updateRoleRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		String roleName = role.getRoleName();
		long operator = role.getOperator();
		long roleId = role.getRoleId();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_ccb_role " );
			sb.append( "set role_name = ?, " );
			sb.append( "operatetime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "operator = ? " );
			sb.append( "where role_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_name = " + roleName);
			logger.info(FUNCTION_NAME + "operator = " + operator);
			logger.info(FUNCTION_NAME + "role_id = " + roleId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1 , roleName );
			stmt.setLong( 2, operator );
			stmt.setLong( 3, roleId );

			update();
			commit();
			
			return roleId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	
	public void insertRoleActinoMap(RoleActionManagementBean rab) throws SQLException {

		String FUNCTION_NAME = "insertRoleActinoMap() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long actionId = rab.getActionId();
		long roleId = rab.getRoleId();
		long operator = rab.getOperator();
		long mappingId = getSequence( "seq_tb_action_management" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_action_management " );
			sb.append( "( mapping_id, action_id, role_id, operatetime, operator ) " );
			sb.append( "values (?,?,?,date_trunc( 'second', current_timestamp ),?)" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "mapping_id = " + mappingId);
			logger.info(FUNCTION_NAME + "action_id = " + actionId);
			logger.info(FUNCTION_NAME + "role_id = " + roleId);
			logger.info(FUNCTION_NAME + "operator = " + operator);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, mappingId );
			stmt.setLong( 2, actionId );
			stmt.setLong( 3, roleId );
			stmt.setLong( 4, operator );

			insert();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public boolean checkRoleActionMappingExist(long actionId, long roleId) throws SQLException {
		
		String FUNCTION_NAME = "checkRoleActionMappingExist() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			boolean retrunFlg = false;
			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_action_management where action_id = ? and role_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "action_id = " + actionId);
			logger.info(FUNCTION_NAME + "role_id = " + roleId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, actionId );
			stmt.setLong( 2, roleId );
			

			HashMap[] map = select();
			
			if(map!=null&&map.length==1){
				retrunFlg = true;
			}

			return retrunFlg;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}

	public void deleteRoleByRoleID(long roleId) throws SQLException {

		String FUNCTION_NAME = "deleteRoleByRoleID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_ccb_role where role_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_id = " + roleId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, roleId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public void deleteRoleActionMappingByRoleID(long roleId) throws SQLException {

		String FUNCTION_NAME = "deleteRoleActionMappingByRoleID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_action_management where role_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_id = " + roleId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, roleId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public void deleteRoleOrganizeMappingByRoleID(long roleId) throws SQLException {

		String FUNCTION_NAME = "deleteRoleOrganizeMappingByRoleID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_role_management where role_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_id = " + roleId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, roleId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public HashMap[] getAllActions() throws SQLException {

		String FUNCTION_NAME = "getAllActions() ";
		logger.info( FUNCTION_NAME + "start" );
		
		try{
			
			StringBuffer sb = new StringBuffer();
			sb.append("select * from mt_ccb_action");

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

	public HashMap[] getActionsByRoleName(String roleName) throws SQLException {

		String FUNCTION_NAME = "getActionsByRoleName() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();
			sb.append("select mca.* ");
			sb.append("from mt_ccb_action mca, tb_action_management tam, tb_ccb_role tcr ");
			sb.append("where mca.action_id = tam.action_id ");
			sb.append("and tam.role_id = tcr.role_id ");
			sb.append("and tcr.role_name = ?");

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_name = " + roleName);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, roleName );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}
	
	public HashMap[] getActionsByRoleID(long roleId) throws SQLException {

		String FUNCTION_NAME = "getActionsByRoleID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();
			sb.append("select mca.* ");
			sb.append("from mt_ccb_action mca, tb_action_management tam ");
			sb.append("where mca.action_id = tam.action_id ");
			sb.append("and tam.role_id = ?");

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_id = " + roleId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, roleId );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}
	
	public HashMap[] getRolesByUserID(long userId) throws SQLException {

		String FUNCTION_NAME = "getRolesByUserID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();			
			sb.append("select tcr.* ");
			sb.append("from tb_ccb_role tcr, tb_role_management trm, ");
			sb.append("( select user_id, org_id, department_id from tb_ccb_user where user_id = ? ) tcu ");
			sb.append("where tcr.role_id = trm.role_id ");
			sb.append("and ((trm.object_type = '1' and trm.object_id = tcu.user_id) ");
			sb.append("or (trm.object_type = '2' and trm.object_id = tcu.org_id) ");
			sb.append("or (trm.object_type = '3' and trm.object_id = tcu.department_id))");

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "user_id = " + userId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, userId );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}
	
	public HashMap[] getRolesByODUStr(String orgIdStr, String dptIdStr, String userIdStr) throws SQLException {

		String FUNCTION_NAME = "getRolesByODUStr() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();			
			sb.append("select distinct role_id ");
			sb.append("from tb_role_management ");
			sb.append("where ((object_type = '2' and object_id in " + orgIdStr + ") ");
			if(dptIdStr!=null){
				sb.append("or (object_type = '3' and object_id in " + dptIdStr + ") ");
			}
			if(userIdStr!=null){
				sb.append("or (object_type = '1' and object_id in " + userIdStr + ") ");
			}
			sb.append(") order by role_id ");
			
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
	
	public HashMap[] getRoleMappingRecordsByRoleIdStr(String objType, String roleIdStr) throws SQLException {

		String FUNCTION_NAME = "getRoleMappingRecordsByRoleIdStr() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();	
			sb.append("select distinct object_id ");
			sb.append("from tb_role_management ");
			sb.append("where role_id in (" + roleIdStr + ") ");
			sb.append("and object_type = ? ");
			sb.append("order by object_id");
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "object_type = " + objType);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, objType );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}
	
	public HashMap[] getRolesByRoleIdStr(String roleIdStr) throws SQLException {

		String FUNCTION_NAME = "getRolesByRoleIdStr() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();			
			sb.append("select * from tb_ccb_role where role_id in " + roleIdStr );

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
	
	public HashMap[] getAllRoles() throws SQLException {

		String FUNCTION_NAME = "getAllRoles() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();			
			sb.append("select * from tb_ccb_role");
			
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
	
	public HashMap getActionByActionID(long actionId) throws SQLException {

		String FUNCTION_NAME = "getActionByActionID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();
			sb.append( "select * from mt_ccb_action where action_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "action_id = " + actionId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, actionId );

			HashMap[] map = select();
			HashMap temp = null;
			
			if(map!=null&&map.length==1){
				temp = map[0];
			}

			return temp;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}

}
