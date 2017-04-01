package th.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import th.entity.DepartmentBean;
import th.entity.SearchConditionBean;
import th.entity.UserBean;
import th.user.User;

public class UserDao extends BaseDao {
	
	public UserDao(){
		
	}
	
	public User getUser(String username, String password) {
		logger.info("开始查找用户，当前用户的用户名： " + username + "密码： " + password);
		// select u.user_id,
		// u.org_id,u.department_id,u.name,u.password,u.real_name,u.email,u.address,u.telephone,u.cellphone,u.description,u.status,u.operatetime,u.operatetime
		// from tb_ccb_user u where u.name='administrator' and
		// u.password='administrator'

		ResultSet rs = null;
		User user = null;
		StringBuffer sb = new StringBuffer();
		sb.append("select u.user_id,u.org_id,u.department_id,u.name,u.password,u.real_name,u.email,u.address,u.telephone,u.cellphone,u.description,u.type,u.status,u.operatetime,u.operator");
		sb.append(" from tb_ccb_user u ");
		sb.append(" where");
		sb.append(" u.name=? and u.password=? ");
		logger.info("执行的SQL语句是" + sb.toString());

		try {
			this.connection();
			PreparedStatement stat = con.prepareStatement(sb.toString());
			stat.setString(1, username);
			stat.setString(2, password);
			rs = stat.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					logger.info("成功检索到用户");
					user = new User();
					user.setId(rs.getString("user_id"));
					user.setName(rs.getString("name"));
					user.setOrg_id(rs.getString("org_id"));
					user.setDepartment_id(rs.getString("department_id"));
					user.setAddress(rs.getString("address"));
					user.setCellphone(rs.getString("cellphone"));
					user.setDescription(rs.getString("description"));
					user.setEmail(rs.getString("email"));
					user.setOperatetime(rs.getString("operatetime"));
					user.setOperator(rs.getString("operator"));
					user.setReal_name(rs.getString("real_name"));
					user.setType(rs.getString("type"));
					user.setStatus(rs.getString("status"));
					user.setTelephone(rs.getString("telephone"));
				}
				if (user == null) {
					logger.info("检索用户失败。未能检索到用户名为： " + username);
				}
			} else {
				logger.info("检索用户失败。未能检索到用户名为： " + username);
			}

		} catch (Throwable e) {
			logger.info("检索用户失败。未能检索到用户名为： " + username);
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return user;
	}
	
	public HashMap[] getAllUsersByOrgId(long orgId) throws SQLException {

		String FUNCTION_NAME = "getAllUsersByOrgId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_user where org_id = ?" );

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
	
	public HashMap[] getAllUsersByOrgIdStr(String inOrgIdStr) throws SQLException {

		String FUNCTION_NAME = "getAllUsersByOrgIdStr() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_user where org_id in " + inOrgIdStr );

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
	
	public HashMap[] getAllUsersByDptId(long dptId) throws SQLException {

		String FUNCTION_NAME = "getAllUsersByDptId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_user where department_id = ?" );

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
	
	public HashMap[] getUsersByODUStr(String orgIdStr, String dptIdStr, String userIdStr) throws SQLException {

		String FUNCTION_NAME = "getUsersByODUStr() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();	
			sb.append("select distinct user_id ");
			sb.append("from tb_ccb_user ");
			sb.append("where user_id in " + userIdStr + " ");
			if(orgIdStr!=null){
				sb.append("or org_id in " + orgIdStr + " ");
			}
			if(dptIdStr!=null){
				sb.append("or department_id in " + dptIdStr + " ");
			}
			sb.append("order by user_id ");
			
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
	
	public HashMap[] getUsersByUserIdStr(String idStr, String userIdStr) throws SQLException {

		String FUNCTION_NAME = "getUsersByUserIdStr() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			
			StringBuffer sb = new StringBuffer();	
			sb.append("select " + idStr.toLowerCase() + " ");
			sb.append("from tb_ccb_user ");
			sb.append("where user_id in " + userIdStr);
			
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
	
	public UserBean getUserBeanByUserId(long userId) throws SQLException {

		String FUNCTION_NAME = "getUserBeanByUserId() ";
		logger.info( FUNCTION_NAME + "start" );
		
		UserBean userBean = null;

		try{

			StringBuffer sb = new StringBuffer();
			// administrator用户的情况
			if(userId == 0){
				sb.append( "select tcu.* " );
				sb.append( "from tb_ccb_user tcu " );
				sb.append( "where tcu.user_id = ?" );
			}else{
				sb.append( "select tcu.*, tcd.department_name " );
				sb.append( "from tb_ccb_user tcu, tb_ccb_department tcd " );
				sb.append( "where tcu.department_id = tcd.department_id " );
				sb.append( "and tcu.user_id = ?" );
			}
			

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
			
			if(map!=null&&map.length==1){
				userBean = new UserBean();
				userBean.setUserId( Long.parseLong( (String)map[0].get( "USER_ID" ) ) );
				userBean.setOrgId( Long.parseLong( (String)map[0].get( "ORG_ID" ) ) );
				userBean.setNickName( (String)map[0].get( "NAME" ) );
				userBean.setPassword( (String)map[0].get( "PASSWORD" ) );
				userBean.setUserName( (String)map[0].get( "REAL_NAME" ) );
				userBean.setEmail( (String)map[0].get( "EMAIL" ) );
				userBean.setAddress( (String)map[0].get( "ADDRESS" ) );
				userBean.setFixedTel( (String)map[0].get( "TELEPHONE" ) );
				userBean.setMobilePhone( (String)map[0].get( "CELLPHONE" ) );
				userBean.setUserDesp( (String)map[0].get( "DESCRIPTION" ) );
				userBean.setUserType( (String)map[0].get( "TYPE" ) );
				userBean.setUserStatus( (String)map[0].get( "STATUS" ) );
				userBean.setOperateTime( (String)map[0].get( "OPERATETIME" ) );
				userBean.setOperator( Long.parseLong( (String)map[0].get( "OPERATOR" ) ) );
				// administrator用户的情况
				if(userId == 0){
					userBean.setDptId( 0 );
					userBean.setDptName( "" );
				}else{
					userBean.setDptId( Long.parseLong( (String)map[0].get( "DEPARTMENT_ID" ) ) );
					userBean.setDptName( (String)map[0].get( "DEPARTMENT_NAME" ) );
				}			
				
			}

			return userBean;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getUsersBySearchCondition(SearchConditionBean scb) throws SQLException {

		String FUNCTION_NAME = "getUsersBySearchCondition() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long orgId = scb.getOrgId();
		long dptId = scb.getDptId();
		String orgName = scb.getOrgName();
		String dptName = scb.getDptName();
		String managerName = scb.getManagerName();
		String userName = scb.getUserName();
		String  searchOrgStr = scb.getSearchOrgStr();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select tcu.*,tcd.department_name, tco.org_name " );
			sb.append( "from tb_ccb_user tcu, tb_ccb_department tcd, tb_ccb_organization tco " );
			sb.append( "where tcu.department_id = tcd.department_id " );
			sb.append( "and tcu.org_id = tco.org_id " );
			sb.append( "and tco.org_id in " + searchOrgStr + " " );
			if(orgId!=-1){
				sb.append( "and tco.org_id = " + orgId + " " );
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
			if(!"".equals( userName )){
				sb.append( "and tcu.real_name = '" + userName + "' " );
			}
			sb.append( "order by tcu.operatetime desc" );
			

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "department_id = " + dptId);
			logger.info(FUNCTION_NAME + "org_name = " + orgName);
			logger.info(FUNCTION_NAME + "department_name = " + dptName);
			logger.info(FUNCTION_NAME + "manager_name = " + managerName);
			logger.info(FUNCTION_NAME + "real_name = " + userName);


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
	
	public long insertUserRecord(UserBean ub) throws SQLException {

		String FUNCTION_NAME = "insertUserRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long userId = getSequence( "seq_tb_ccb_user" );
		long orgId = ub.getOrgId();
		long dptId = ub.getDptId();
		String nickName = ub.getNickName();
		String password = ub.getPassword();
		String userName = ub.getUserName();
		String email = ub.getEmail();
		String fixedTel = ub.getFixedTel();
		String mobilePhone = ub.getMobilePhone();
		String userDesp = ub.getUserDesp();
		long operator = ub.getOperator();
		String address = ub.getAddress();
		String userStatus = ub.getUserStatus();
		String userType = ub.getUserType();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_ccb_user " );
			sb.append( "( user_id, org_id, department_id, name, password, real_name, email, address, telephone, cellphone, description, type,status, operatetime, operator ) " );
			sb.append( "values (?,?,?,?,?,?,?,?,?,?,?,?,?,date_trunc( 'second', current_timestamp ),?)"  );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "user_id = " + userId);
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "department_id = " + dptId);
			logger.info(FUNCTION_NAME + "name = " + nickName);
			logger.info(FUNCTION_NAME + "password = " + password);
			logger.info(FUNCTION_NAME + "real_name = " + userName);
			logger.info(FUNCTION_NAME + "email = " + email);
			logger.info(FUNCTION_NAME + "address = " + address);
			logger.info(FUNCTION_NAME + "telephone = " + fixedTel);
			logger.info(FUNCTION_NAME + "cellphone = " + mobilePhone);
			logger.info(FUNCTION_NAME + "description = " + userDesp);
			logger.info(FUNCTION_NAME + "type = " + userType);
			logger.info(FUNCTION_NAME + "status = " + userStatus);
			logger.info(FUNCTION_NAME + "operator = " + operator);



			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, userId );
			stmt.setLong( 2, orgId );
			stmt.setLong( 3, dptId );
			stmt.setString( 4 , nickName );
			stmt.setString( 5, password );
			stmt.setString( 6, userName );
			
			stmt.setString( 7, email );
			stmt.setString( 8, address );
			stmt.setString( 9, fixedTel );
			stmt.setString( 10, mobilePhone );
			stmt.setString( 11, userDesp );
			stmt.setString( 12, userType );
			stmt.setString( 13, userStatus );
			stmt.setLong( 14 , operator );

			insert();
			commit();
			
			return userId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void updateUserRecord(UserBean ub) throws SQLException {

		String FUNCTION_NAME = "updateUserRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long userId = ub.getUserId();
		long orgId = ub.getOrgId();
		long dptId = ub.getDptId();
		String password = ub.getPassword();
		String userName = ub.getUserName();
		String email = ub.getEmail();
		String fixedTel = ub.getFixedTel();
		String mobilePhone = ub.getMobilePhone();
		String userDesp = ub.getUserDesp();
		String userType = ub.getUserType();
		long operator = ub.getOperator();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_ccb_user " );
			sb.append( "set org_id = ?, " );
			sb.append( "department_id = ?, " );
			sb.append( "password = ?, " );
			sb.append( "real_name = ?, " );
			sb.append( "email = ?, " );
			sb.append( "telephone = ?, " );
			sb.append( "cellphone = ?, " );
			sb.append( "description = ?, " );
			sb.append( "type = ?, " );	
			sb.append( "operatetime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "operator = ? " );
			sb.append( "where user_id = ?" );
			

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "user_id = " + userId);
			logger.info(FUNCTION_NAME + "org_id = " + orgId);
			logger.info(FUNCTION_NAME + "department_id = " + dptId);
			logger.info(FUNCTION_NAME + "password = " + password);
			logger.info(FUNCTION_NAME + "real_name = " + userName);
			logger.info(FUNCTION_NAME + "email = " + email);
			logger.info(FUNCTION_NAME + "telephone = " + fixedTel);
			logger.info(FUNCTION_NAME + "cellphone = " + mobilePhone);
			logger.info(FUNCTION_NAME + "description = " + userDesp);
			logger.info(FUNCTION_NAME + "type = " + userType);
			logger.info(FUNCTION_NAME + "operator = " + operator);



			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, orgId );
			stmt.setLong( 2, dptId );
			stmt.setString( 3 , password );
			stmt.setString( 4, userName );
			stmt.setString( 5, email );			
			stmt.setString( 6, fixedTel );
			stmt.setString( 7, mobilePhone );
			stmt.setString( 8, userDesp );
			stmt.setString( 9, userType );
			stmt.setLong( 10 , operator );
			stmt.setLong( 11, userId );

			update();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void updateUserPassword(UserBean ub) throws SQLException {

		String FUNCTION_NAME = "updateUserPassword() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long userId = ub.getUserId();
		String password = ub.getPassword();
		long operator = ub.getOperator();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_ccb_user " );
			sb.append( "set password = ?, " );
			sb.append( "operatetime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "operator = ? " );
			sb.append( "where user_id = ?" );
			

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "password = " + password);
			logger.info(FUNCTION_NAME + "operator = " + operator);
			logger.info(FUNCTION_NAME + "user_id = " + userId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, password );
			stmt.setLong( 2 , operator );
			stmt.setLong( 3, userId );

			update();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void deleteUserByUserID(long UserId) throws SQLException {

		String FUNCTION_NAME = "deleteUserByUserID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_ccb_user where user_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "user_id = " + UserId);


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
	
	
	public long getUserIdByNickName(String nickname) throws SQLException {

		String FUNCTION_NAME = "getUserIdByNickName() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long userId = -1;

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_user where name = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "name = " + nickname);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1 , nickname );
			

			HashMap[] map = select();

			if(map!=null&&map.length==1){
				userId = Long.parseLong( (String) map[0].get( "USER_ID" ) );
			}
			
			return userId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
}
