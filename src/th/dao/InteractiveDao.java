/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Descriptions
 * 
 * @version 2013-8-8
 * @author PSET
 * @since JDK1.6
 * 
 */
public class InteractiveDao extends BaseDao {

	/*-------------------------------------------------------------------------
	/  Method名 ： selectEndPointInfo()
	/------------------------------------------------------------------------*/
	public HashMap[] selectEndPointInfo(String mac) throws SQLException {

		String FUNCTION_NAME = "selectEndPointInfo() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("select * ");
		sb.append("from TB_CCB_MACHINE ");
		sb.append("where MAC = ? ");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setString(1, mac);

			HashMap[] map = select();

			return map;

		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/*-------------------------------------------------------------------------
	/  Method名 ： insertEndPointInfo()
	/------------------------------------------------------------------------*/
	public int insertEndPointInfo(String mac, long machineID, String endIP, String endName, String os, Timestamp startTime, Timestamp endTime)
			throws SQLException {

		String FUNCTION_NAME = "insertEndPointInfo() ";
		logger.info(FUNCTION_NAME + "start");
		logger.info(FUNCTION_NAME + "mac:" + mac);
		logger.info(FUNCTION_NAME + "machineID:" + machineID);

		//未审核通过时，端机状态为3:未审核
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO ");
		sb.append("TB_CCB_MACHINE (machine_id, mac, machine_name, machine_mark, ip, operatetime, status, dev_id, open_time, close_time ) ");
		sb.append("VALUES (?, ?, ?, 'ATM' || to_char( ?, 'FM00009' ),?, now(), '3', ");
		sb.append("(SELECT dev_id FROM tb_ebank_device_management WHERE dev_os = ? LIMIT 1),? ,?)");
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong(1, machineID);
			stmt.setString(2, mac);
			stmt.setString(3, endName);
			stmt.setLong(4, machineID);
			stmt.setString(5, endIP);
			stmt.setString(6, os);
			stmt.setTimestamp( 7, startTime );
			stmt.setTimestamp( 8, endTime );

			int ret = stmt.executeUpdate();

			return ret;

		} finally {
			
			//提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}
			
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}

	/*-------------------------------------------------------------------------
	/  Method名 ： getMachineID()
	/------------------------------------------------------------------------*/
	public long getMachineID() throws SQLException {

		String FUNCTION_NAME = "getMachineID() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("select nextval('SEQ_TB_CCB_MACHINE') as MACHINE_ID ");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();
			return Long.parseLong(map[0].get("MACHINE_ID").toString());

		} finally {

			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	

	/*-------------------------------------------------------------------------
	/  Method名 ： getEndPointCommand()
	/------------------------------------------------------------------------*/
	public HashMap[] getEndPointCommand(long machineID) throws SQLException {

		String FUNCTION_NAME = "getEndPointCommand() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, machine_id, status, command_id, command_content, operatetime ");
		sb.append("FROM tb_command_management ");
		sb.append("WHERE machine_id = ? and operatetime < now() and status = '1' ");
		sb.append("ORDER BY operatetime asc");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong(1, machineID);

			HashMap[] map = select();

			return map;

		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/*-------------------------------------------------------------------------
	/  Method名 ： getEndPointCommandStatus()
	/------------------------------------------------------------------------*/
	public String getEndPointCommandStatus(long machineID, int commandID) throws SQLException {

		String FUNCTION_NAME = "getEndPointCommandStatus() ";
		logger.info(FUNCTION_NAME + "start");

		String strRet = "";
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT status ");
		sb.append("FROM tb_command_management ");
		sb.append("WHERE machine_id = ? and command_id = ? ");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong(1, machineID);
			stmt.setInt(2, commandID);

			HashMap[] map = select();
			
			if(null!=map[0].get("STATUS")){
				strRet = map[0].get("STATUS").toString();
			}
			
			return strRet;

		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/*-------------------------------------------------------------------------
	/  Method名 ： getEndPointStartCommand()
	/------------------------------------------------------------------------*/
	public HashMap[] getEndPointStartCommand(long machineID)
			throws SQLException {

		String FUNCTION_NAME = "getEndPointStartCommand() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id, machine_id, status, command_id, command_content, operatetime ");
		sb.append("FROM tb_command_management ");
		sb.append("WHERE machine_id = ? and operatetime < now() and status = '1' and command_id = 2 ");
		sb.append("ORDER BY operatetime desc ");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong(1, machineID);

			HashMap[] map = select();

			return map;

		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/*-------------------------------------------------------------------------
	/  Method名 ： setCommandStatus()
	/------------------------------------------------------------------------*/
	public int setCommandStatus(long machineID, String commandID, String status)
			throws SQLException {

		String FUNCTION_NAME = "setCommandStatus() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE tb_command_management ");
		sb.append("SET  status=?,  command_content = null, operatetime=now() ");
		sb.append("WHERE machine_id = ? and command_id = ? ");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setString( 1, status );
			stmt.setLong( 2, machineID );
			stmt.setInt( 3, Integer.parseInt( commandID ) );

			int ret = stmt.executeUpdate();

			return ret;

		} finally {

			//提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}
			
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	/*-------------------------------------------------------------------------
	/  Method名 ： updateNewestPulse()
	/------------------------------------------------------------------------*/
	public int updateNewestPulse(long machineID)
			throws SQLException {
		String FUNCTION_NAME = "updateNewestPulse() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE tb_newest_pulse_management ");
		sb.append("SET newest_time=now() ");
		sb.append("WHERE machine_id=? ");
		sb.append("AND CAST ( now( ) AS DATE ) = CAST ( newest_time AS DATE ) ");
		
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong( 1, machineID );

			int ret = stmt.executeUpdate();

			return ret;

		} finally {

			//提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}
			
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	/*-------------------------------------------------------------------------
	/  Method名 ： insertNewestPulse()
	/------------------------------------------------------------------------*/
	public int insertNewestPulse(long machineID)
			throws SQLException {
		String FUNCTION_NAME = "insertNewestPulse() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO tb_newest_pulse_management(history_id, machine_id, newest_time) ");
		sb.append("VALUES (nextval('SEQ_TB_NEWEST_PULSE_MANAGEMENT'), ?, now()) ");
	
		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong( 1, machineID );

			int ret = stmt.executeUpdate();

			return ret;

		} finally {

			//提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}
			
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	/*-------------------------------------------------------------------------
	/  Method名 ： updateMachineStatus()
	/------------------------------------------------------------------------*/
	public int updateMachineStatus(String status, long machineID)
			throws SQLException {
		String FUNCTION_NAME = "updateMachineStatus() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE tb_ccb_machine SET status= ? WHERE machine_id= ? ");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setString( 1, status );
			stmt.setLong( 2, machineID );

			int ret = stmt.executeUpdate();

			return ret;

		} finally {

			//提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}
			
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}	
	

	/*-------------------------------------------------------------------------
	/  Method名 ： updateMachineEnableStatus()
	/------------------------------------------------------------------------*/
	public int updateMachineEnableStatus(String status, long machineID)
			throws SQLException {
		String FUNCTION_NAME = "updateMachineEnableStatus() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE tb_ccb_machine SET status = ( CASE WHEN status = '3' THEN ");
		sb.append("status ELSE ? END)  WHERE machine_id= ?");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setString( 1, status );
			stmt.setLong( 2, machineID );

			int ret = stmt.executeUpdate();

			return ret;

		} finally {

			//提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}
			
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	/*
	 * 升级包下发状态更新
	 */
	public int updateUpgradePackageStatus( long machineID )
			throws SQLException {
		String FUNCTION_NAME = "updateMachineEnableStatus() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("	UPDATE tb_system_update_management SET status='0', operatetime = now() ");
		sb.append("WHERE status='1' AND machine_id = ? ");
		sb.append("AND file_id IN ( SELECT file_id from tb_system_update_data WHERE dev_id is null AND new_dev_id is not null )");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong( 1, machineID );

			int ret = stmt.executeUpdate();

			return ret;

		} finally {

			//提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}
			
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	/*
	 * 系统升级包下发状态更新
	 */
	public int updateSystemUpgradePackageStatus( long machineID )
			throws SQLException {
		String FUNCTION_NAME = "updateSystemUpgradePackageStatus() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("	UPDATE tb_system_update_management SET status='0', operatetime = now() ");
		sb.append("WHERE status='1' AND machine_id = ? ");
		sb.append("AND file_id IN ( SELECT file_id from tb_system_update_data WHERE new_dev_id is null AND dev_id is not null )");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong( 1, machineID );

			int ret = stmt.executeUpdate();

			return ret;

		} finally {

			//提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}
			
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
}
