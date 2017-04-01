package th.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import th.entity.DepartmentBean;
import th.entity.InternetBankingBean;
import th.entity.SearchConditionBean;
import th.entity.UserBean;
import th.user.User;

public class InternetBankingDao extends BaseDao {
	
	public InternetBankingDao(){
		
	}
	
	public boolean getDetailByFactotyIdAndSnNum(String factoryId,String snNum) throws SQLException{
		
		String FUNCTION_NAME = "getDetailByFactotyIdAndSnNum() ";
		logger.info( FUNCTION_NAME + "start" );
		
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "SELECT count(*)  " );
			sb.append( "from TB_CCB_EBANK_DEVICE " );
			sb.append( "where " );
			sb.append( " FACTORY_ID = '" + factoryId + "' " );
			sb.append( "  AND SN_NUM = '" + snNum + "' " );
			
			
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

			if(Integer.parseInt( map[0].get( "COUNT" ).toString() )>0){
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
	public int getFactorySnNumDetail(String factoryId,String snNum) throws SQLException{
		String FUNCTION_NAME = "getFactorySnNumDetail() ";
		logger.info( FUNCTION_NAME + "start" );
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "select count(*) " );
			sb.append( "from TB_CCB_EBANK_DEVICE " );
			sb.append( "where " );
			sb.append( " FACTORY_ID = '" + factoryId + "' " );
			sb.append( " AND SN_NUM = '" + snNum + "' " );
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "FACTORY_ID = " + factoryId);
			logger.info(FUNCTION_NAME + "SN_NUM = " + snNum);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			
			HashMap[] map = select();

			return Integer.parseInt( map[0].get( "COUNT" ).toString() );
		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}
	public HashMap[] getInternetBankingBySearchCondition(String searchCond) throws SQLException {

		String FUNCTION_NAME = "getInternetBankingBySearchCondition() ";
		logger.info( FUNCTION_NAME + "start" );
		
		String[] conds = searchCond.split( "," );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "select DEV_ID,FACTORY_ID,SN_NUM,DEV_TYPE,REMARK,DEV_STATUS " );
			sb.append( "from TB_CCB_EBANK_DEVICE " );
			sb.append( "where  0=0 " );
			if(!"null".equals( conds[0] )&&!"".equals( conds[0] )){
				sb.append( "and FACTORY_ID = '" + conds[0] + "' " );
			}
			if(!"null".equals( conds[1] )&&!"".equals( conds[1] )){
				sb.append( "and SN_NUM = '" + conds[1] + "' " );
			}
			if(!"null".equals( conds[2] )){
				sb.append( "and DEV_TYPE = '" + conds[2] + "' " );
			}
			sb.append( "order by DEV_ID desc" );
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "FACTORY_ID = " + conds[0]);
			logger.info(FUNCTION_NAME + "SN_NUM = " + conds[1]);
			logger.info(FUNCTION_NAME + "DEV_TYPE = " + conds[2]);


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
	public void insertInternetBanking(InternetBankingBean ibb) throws SQLException{
		String FUNCTION_NAME = "insertInternetBanking() ";
		logger.info( FUNCTION_NAME + "start" );
		
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into TB_CCB_EBANK_DEVICE " );
			sb.append( "( DEV_ID, FACTORY_ID, SN_NUM, DEV_TYPE, REMARK, DEV_STATUS,OPERATETIME, OPERATOR ) " );
			sb.append( "values (?,?,?,?,?,?,date_trunc( 'second', current_timestamp ),?)"  );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "DEV_ID = " + ibb.getDevId());
			logger.info(FUNCTION_NAME + "FACTORY_ID = " + ibb.getFatoryId());
			logger.info(FUNCTION_NAME + "SN_NUM = " + ibb.getSnNum());
			logger.info(FUNCTION_NAME + "DEV_TYPE = " + ibb.getDevType());
			logger.info(FUNCTION_NAME + "REMARK = " + ibb.getRemark());
			logger.info(FUNCTION_NAME + "DEV_STATUS = " + ibb.getDevStatus());
			logger.info(FUNCTION_NAME + "OPERATOR = " + ibb.getOperator());

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, ibb.getDevId() );
			stmt.setString( 2, ibb.getFatoryId() );
			stmt.setString( 3, ibb.getSnNum() );
			stmt.setString( 4 , ibb.getDevType() );
			stmt.setString( 5, ibb.getRemark() );
			stmt.setString( 6, ibb.getDevStatus() );
			stmt.setLong( 7, ibb.getOperator() );
			
//			stmt.setTimestamp( 0, x, cal );

			insert();
			commit();

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
	
	public String getTbCCBEbank() throws SQLException{
		String FUNCTION_NAME = "getTbCCBEbank() ";
		logger.info( FUNCTION_NAME + "start" );
		
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "SELECT nextval('SEQ_TB_CCB_EBANK_DEVICE')" );

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
			return map[0].get( "NEXTVAL" ).toString();
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
