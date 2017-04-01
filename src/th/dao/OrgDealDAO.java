/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import th.entity.OrganizationBean;
import th.entity.RoleObjectManagementBean;

/**
 * Descriptions
 *
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 *
 */
public class OrgDealDAO extends BaseDao {
		
	public OrgDealDAO(){

	}
	
	public HashMap[] getCurOrgNodeByUserID(long userId) throws SQLException {

		String FUNCTION_NAME = "getCurOrgNodeByUserID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select tco.* from ");
			sb.append( "tb_ccb_organization tco, tb_ccb_user tcu " );
			sb.append( "where tco.org_id = tcu.org_id " );
			sb.append( "and tcu.user_id = ?" );

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
			stmt.setLong( 1 , userId );
			

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getCurOrgNodeByOrgId(long orgId) throws SQLException {

		String FUNCTION_NAME = "getCurOrgNodeByOrgId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_organization where org_id = ?");

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
	
	public long getOrgIdByOrgName(String orgName) throws SQLException {

		String FUNCTION_NAME = "getOrgIdByOrgName() ";
		logger.info( FUNCTION_NAME + "start" );

		long orgId = -1;
		
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select org_id from tb_ccb_organization where org_name = ?");

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "org_name = " + orgName);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1 , orgName );
			

			HashMap[] map = select();

			if(map!=null&&map.length==1){
				orgId = Long.parseLong( (String) map[0].get( "ORG_ID" ) );
			}
			
			return orgId;


		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public long getTopParentOrgID() throws SQLException {

		String FUNCTION_NAME = "getTopParentOrgID() ";
		logger.info( FUNCTION_NAME + "start" );

		long orgId = -1;
		
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select org_id from tb_ccb_organization where org_level=(select min(org_level) from tb_ccb_organization);");

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

			if(map!=null&&map.length==1){
				orgId = Long.parseLong( (String) map[0].get( "ORG_ID" ) );
			}
			
			return orgId;


		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getAllOrganizations() throws SQLException {

		String FUNCTION_NAME = "getAllOrganizations() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_organization");
			
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
	
	public HashMap[] getChildNodesByPIdList(String pIdList) throws SQLException {

		String FUNCTION_NAME = "getChildNodesByPIdList() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_organization where parent_org_id in ");
			sb.append( pIdList );
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "pIdList = " + pIdList);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
//			stmt.setString( 1, pIdList );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	public HashMap[] getChildNodesByOrgIdClient(String pIdList) throws SQLException {

		String FUNCTION_NAME = "getChildNodesByOrgIdClient() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_organization where parent_org_id in ");
			sb.append( pIdList );
			sb.append( " and org_level != '2'" );
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "pIdList = " + pIdList);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
//			stmt.setString( 1, pIdList );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	public HashMap[] getFirstNodesByPIdList(String pIdList) throws SQLException {

		String FUNCTION_NAME = "getChildNodesByPIdList() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_organization where parent_org_id in ( ");
			sb.append( pIdList );
			sb.append( " )" );
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "pIdList = " + pIdList);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
//			stmt.setString( 1, pIdList );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public long insertOrgRecord(OrganizationBean org) throws SQLException {

		String FUNCTION_NAME = "insertOrgRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long orgId = getSequence( "seq_tb_ccb_organization" );
		String orgName = org.getOrgName();
		long parentOrgId = org.getParentOrgId();
		long operator = org.getOperator();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_ccb_organization " );
			sb.append( "( org_id, org_level, org_name, parent_org_id, org_no, machine_start_time, " );
			sb.append( "machine_shutdown_time, screen_protect_time, write_protect_dirs, operatetime, operator ) " );
			sb.append( "values (?,?,?,?,?,");
			if(org.getMacStartTime()==""){
				sb.append( "null," );
			}else{
				sb.append( "'" + org.getMacStartTime() + "',");
			}
			if(org.getMacShutdownTime()==""){
				sb.append( "null," );
			}else{
				sb.append( "'" + org.getMacShutdownTime() + "',");
			}
			sb.append( "?,?,date_trunc( 'second', current_timestamp ),?)"  );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "org_name = " + orgName);
			logger.info(FUNCTION_NAME + "parent_org_id = " + parentOrgId);
			logger.info(FUNCTION_NAME + "operator = " + operator);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, orgId );
			stmt.setString( 2, org.getOrgLevel() );
			stmt.setString( 3 , orgName );
			stmt.setLong( 4, parentOrgId );
			stmt.setString( 5, org.getOrgNo() );
			stmt.setInt( 6, org.getScreenProtectTime() );
			stmt.setString( 7, org.getWrithDir() );
			stmt.setLong( 8 , operator );

			insert();
			commit();
			
			return orgId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void updateOrgNamebyOrgId(OrganizationBean org) throws SQLException {

		String FUNCTION_NAME = "updateOrgNamebyOrgId() ";
		logger.info( FUNCTION_NAME + "start" );
		
		String orgName = org.getOrgName();
		long orgId = org.getOrgId();
		long operator = org.getOperator();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_ccb_organization " );
			sb.append( "set org_name = ?, " );
			sb.append( "operatetime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "operator = ? " );
			sb.append( "where org_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "org_name = " + orgName);
			logger.info(FUNCTION_NAME + "operator = " + operator);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1 , orgName );
			stmt.setLong( 2 , operator );
			stmt.setLong( 3, orgId );			
	
			update();
			commit();
			

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void deleteOrgByOrgID(long orgId) throws SQLException {

		String FUNCTION_NAME = "deleteOrgByOrgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_ccb_organization where org_id = ?" );

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
			stmt.setLong( 1, orgId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public void deleteDepartmentsByOrgID(long orgId) throws SQLException {

		String FUNCTION_NAME = "deleteDepartmentsByOrgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_ccb_department where org_id = ?" );

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
			stmt.setLong( 1, orgId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public HashMap[] getDepartmentsByOrgID(long orgId) throws SQLException {

		String FUNCTION_NAME = "getDepartmentsByOrgID() ";
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
			stmt.setLong( 1, orgId );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public HashMap[] getRolesByOrgID(long orgId) throws SQLException {

		String FUNCTION_NAME = "getRolesByOrgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_role_management where object_type = '2' and object_id = ?" );

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
			stmt.setLong( 1, orgId );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public HashMap[] getRolesByDptID(long dptId) throws SQLException {

		String FUNCTION_NAME = "getRolesByDptID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_role_management where object_type = '3' and object_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "dpt_id = " + dptId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, dptId );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public void deleteRoleOrgMappingByOrgID(long orgId) throws SQLException {

		String FUNCTION_NAME = "deleteRoleOrgMappingByOrgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_role_management where object_type = '2' and object_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "object_id = " + orgId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, orgId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public void deleteRoleDptMappingByDptId(long dptId) throws SQLException {

		String FUNCTION_NAME = "deleteRoleDptMappingByDptId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_role_management where object_type = '3' and object_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "object_id = " + dptId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, dptId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public void deleteRoleUserMappingByUserId(long UserId) throws SQLException {

		String FUNCTION_NAME = "deleteRoleUserMappingByUserId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_role_management where object_type = '1' and object_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "object_id = " + UserId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, UserId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public void insertRoleObjectMap(RoleObjectManagementBean romb) throws SQLException {

		String FUNCTION_NAME = "insertRoleObjectMap() ";
		logger.info( FUNCTION_NAME + "start" );
		

		long mappingId = getSequence( "seq_tb_role_management" );
		long roleId = romb.getRoleId();
		String objectType = romb.getObjectType();
		long objectId = romb.getObjectId();
		long operator = romb.getOperator();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_role_management " );
			sb.append( "( mapping_id, role_id, object_id, object_type, operatetime, operator ) " );
			sb.append( "values (?,?,?,?,date_trunc( 'second', current_timestamp ),?)" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "mapping_id = " + mappingId);
			logger.info(FUNCTION_NAME + "role_id = " + roleId);
			logger.info(FUNCTION_NAME + "object_id = " + objectId);
			logger.info(FUNCTION_NAME + "object_type = " + objectType);
			logger.info(FUNCTION_NAME + "operator = " + operator);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, mappingId );
			stmt.setLong( 2, roleId );
			stmt.setLong( 3, objectId );
			stmt.setString( 4, objectType );
			stmt.setLong( 5, operator );

			insert();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public boolean isRoleObjectMappingExist(RoleObjectManagementBean romb) throws SQLException {

		String FUNCTION_NAME = "isRoleObjectMappingExist() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long roleId = romb.getRoleId();
		String objectType = romb.getObjectType();
		long objectId = romb.getObjectId();
		
		boolean existFlg = false;

		try{

			StringBuffer sb = new StringBuffer();
			
			sb.append( "select * from tb_role_management where role_id = ? " );
			sb.append( "and object_id = ? and object_type = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "role_id = " + roleId);
			logger.info(FUNCTION_NAME + "object_id = " + objectId);
			logger.info(FUNCTION_NAME + "object_type = " + objectType);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, roleId );
			stmt.setLong( 2, objectId );
			stmt.setString( 3, objectType );

			HashMap[] map = select();
			if(map!=null&&map.length>0){
				existFlg = true;
			}
			
			return existFlg;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}

	/**
	 * 由组织ID获取下级组织ID
	 * @param orgId
	 * @return 字符串类型，逗号(,)分隔
	 * @throws SQLException 
	 */
	public String getSubOrg(String orgId) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;
		sb.append(" SELECT");
		sb.append(" suborg(o.org_id) as sub_org_id");
		sb.append(" FROM");
		sb.append(" tb_ccb_organization o");
		sb.append(" WHERE");
		sb.append(" o.org_id = ?");
		try {
			if (con == null){
				connection();
			}
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(orgId));
			rs = stmt.executeQuery();
			if(rs.next()){
				return rs.getString("sub_org_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				releaseConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("end");
		}
		return null;
	}
	
	public HashMap[] getMachineInfosByOrgid(String inOrgid) throws SQLException {

		String FUNCTION_NAME = "getMachineInfosByOrgid() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			
			sb.append( "select * from tb_ccb_machine where org_id in " + inOrgid );

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
	
	public void updateOrgDetail(Long orgId,String orgMark,String orgFullName,String orgWebsite,String orgIntroduction,String orgDescription,String orgStatus,String contacter,Long contacterPhone,String contacterMailBox,String contacterIdNumber,String otherContact,String orgCreatetime,String orgCreator) throws SQLException{
		String FUNCTION_NAME = "updateOrgDetail() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			
			sb.append( "update tb_ccb_organization set org_mark = '" + orgMark +"'");
			sb.append( ",org_full_name = '" + orgFullName +"'");
			sb.append( ", org_website = '" + orgWebsite + "'");
			sb.append( ", org_introduction = '" + orgIntroduction +"'");
			sb.append( ", org_description = '" + orgDescription +"'");
			sb.append( ", contacter = '" + contacter +"'");
			sb.append( ", contacter_phone = " + contacterPhone);
			sb.append( ", contacter_mailbox = '" + contacterMailBox +"'");
			sb.append( ", contacter_id_number = '" + contacterIdNumber +"'");
			sb.append( ", other_contacte = '" + otherContact +"'");
			sb.append( ", org_status = '" + orgStatus +"'");
			sb.append( ", org_createtime = '"  +orgCreatetime+"'");
			sb.append( ", org_creator = '" + orgCreator +"'");
			sb.append( " where org_id = " + orgId );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			update();
			commit();
		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	public boolean getLeafByOrgId(String orgId) throws SQLException{
		String FUNCTION_NAME = "getOrgLevelByOrgId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_organization where parent_org_id ="+orgId);
			
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
//			stmt.setString( 1, orgId );

			HashMap[] map = select();

			if(map==null||map.length==0){
				return true;
			}else{
				return false;
			}

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}
	public HashMap[] getFirstNodesByPIdListLeaf(String pIdList) throws SQLException {

		String FUNCTION_NAME = "getChildNodesByPIdList() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_organization where org_id in ( ");
			sb.append( pIdList );
			sb.append( " )" );
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "pIdList = " + pIdList);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
//			stmt.setString( 1, pIdList );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
}
