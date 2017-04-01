/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.SQLException;
import java.util.HashMap;

import th.entity.EBankDeviceBean;
import th.entity.StrategyBean;

/**
 * Descriptions
 *
 * @version 2013-9-12
 * @author PSET
 * @since JDK1.6
 *
 */
public class EBankDeviceDAO extends BaseDao {
	
	public EBankDeviceDAO(){
		
	}
	
	public HashMap[] getAllEBankDevices() throws SQLException {

		String FUNCTION_NAME = "getAllEBankDevices() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ebank_device_management order by operatetime desc" );

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
	
	public EBankDeviceBean getEBankDeviceBeanBydevID(long devId) throws SQLException {

		String FUNCTION_NAME = "getEBankDeviceBeanBydevID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ebank_device_management where dev_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "dev_id = " + devId);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, devId );

			HashMap[] map = select();
			EBankDeviceBean ebb = new EBankDeviceBean();
			
			if(map!=null&&map.length==1){
				ebb.setDevId( Long.parseLong( (String) map[0].get("DEV_ID")) );
				ebb.setDevOs( (String) map[0].get( "DEV_OS" ) );
				ebb.setDevDesp( (String) map[0].get( "DESCRIPTION" ) );
				ebb.setOperateTime( (String) map[0].get( "OPERATETIME" ) );
				ebb.setOperator( Long.parseLong( (String) map[0].get("OPERATOR")) );
			}
			

			return ebb;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public boolean checkEBankDeviceExist(String devOs) throws SQLException {

		String FUNCTION_NAME = "checkEBankDeviceExist() ";
		logger.info( FUNCTION_NAME + "start" );

		boolean retFlg = false;
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select count(1) from tb_ebank_device_management where dev_os = ? " );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "dev_os = " + devOs);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, devOs );

			HashMap[] map = select();
			
			int count = 0;
			if(map!=null&&map.length==1){
				count = Integer.parseInt( (String) map[0].get( "COUNT" ));
				if(count>0){
					retFlg = true;
				}
			}

			return retFlg;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public long insertEBankDeviceRecord(EBankDeviceBean ebb) throws SQLException {

		String FUNCTION_NAME = "insertEBankDeviceRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long devId = getSequence( "seq_tb_ebank_device_management" );
		String devOs = ebb.getDevOs();
		String devDesp = ebb.getDevDesp();
		long operator = ebb.getOperator();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_ebank_device_management " );
			sb.append( "( dev_id, dev_os, description, operatetime, operator ) " );
			sb.append( "values (?,?,?,date_trunc( 'second', current_timestamp ),?)"  );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "dev_id = " + devId);
			logger.info(FUNCTION_NAME + "dev_os = " + devOs);
			logger.info(FUNCTION_NAME + "description = " + devDesp);
			logger.info(FUNCTION_NAME + "operator = " + operator);
	
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, devId );
			stmt.setString( 2, devOs );
			stmt.setString( 3 , devDesp );
			stmt.setLong( 4 , operator );

			insert();
			commit();
			
			return devId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	
	public long insertEBankDeviceRecord(String devOs) throws SQLException {

		String FUNCTION_NAME = "insertEBankDeviceRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long devId = getSequence( "seq_tb_ebank_device_management" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_ebank_device_management " );
			sb.append( "( dev_id, dev_os, description, operatetime, operator ) " );
			sb.append( "values (?,?,'未审核端机类型',date_trunc( 'second', current_timestamp ),0)"  );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "dev_id = " + devId);
			logger.info(FUNCTION_NAME + "dev_os = " + devOs);
			logger.info(FUNCTION_NAME + "description = 未审核端机类型");
			logger.info(FUNCTION_NAME + "operator = 管理员");
	
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, devId );
			stmt.setString( 2, devOs );

			insert();
			commit();
			
			return devId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void updateEBankDeviceRecord(EBankDeviceBean ebb) throws SQLException {

		String FUNCTION_NAME = "updateEBankDeviceRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long devId = ebb.getDevId();
		String devOs = ebb.getDevOs();
		String devDesp = ebb.getDevDesp();
		long operator = ebb.getOperator();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_ebank_device_management " );
			sb.append( "set dev_os = ?, " );
			sb.append( "description = ?, " );
			sb.append( "operatetime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "operator = ? " );
			sb.append( "where dev_id = ?" );
			

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "dev_os = " + devOs);
			logger.info(FUNCTION_NAME + "description = " + devDesp);
			logger.info(FUNCTION_NAME + "operator = " + operator);
			logger.info(FUNCTION_NAME + "dev_id = " + devId);



			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, devOs );
			stmt.setString( 2 , devDesp );	
			stmt.setLong( 3 , operator );
			stmt.setLong( 4, devId );

			update();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void deleteEBankDeviceByDevID(long devId) throws SQLException {

		String FUNCTION_NAME = "deleteEBankDeviceByDevID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_ebank_device_management where dev_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "dev_id = " + devId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, devId );

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
