package th.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import th.entity.interactive.MachineAccessHistoryUploadBean;
import th.entity.interactive.MachineEnvUploadBean;
import th.entity.interactive.MachineUseHistoryUploadBean;
import th.entity.interactive.MonitorCommonUploadBean;
import th.entity.interactive.ServerTimeGetBean;
import th.entity.interactive.UKeyAccessBean;
import th.util.StringUtils;

public class UploadDao extends BaseDao {

	public int insertMachineUseHistory(MachineUseHistoryUploadBean bean) throws SQLException {
		String FUNCTION_NAME = "insertMachineUseHistory() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO	TB_MACHINE_USE_HISTORY( ");
		sb.append("	HISTORY_ID,MACHINE_ID,DATE,CLICK_COUNT,VALID_USE_TIME,RUNNING_TIME) ");
		sb.append("	VALUES(nextval('SEQ_TB_MACHINE_USE_HISTORY'),?,?,?,?,?)");

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS: " + bean.toString());
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setInt(i++, bean.getMachineId());
			stmt.setDate(i++, new java.sql.Date(bean.getStatisticsdate().getTime()));
			stmt.setInt(i++, bean.getClicktimes());
			stmt.setInt(i++, bean.getAccesstime());
			stmt.setInt(i++, bean.getRunningTime());

			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}

		return result;
	}
	
	public int insertMachineProxyInfo(ServerTimeGetBean bean)throws SQLException {
		String FUNCTION_NAME = "insertMachineProxyInfo() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		sb.append("	UPDATE tb_ccb_machine SET proxy_host = ?, proxy_port = ? WHERE machine_id = ? ");

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS: " + bean.toString());
		int result = 0;
		
		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setString(i++, bean.getProxyHost());
			stmt.setString(i++, bean.getProxyPort());
			stmt.setInt(i++, bean.getMachineId());

			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}

		return result;
	}

	public int insertMachineAccessHistory(MachineAccessHistoryUploadBean bean) throws SQLException {
		String FUNCTION_NAME = "insertMachineAccessHistory() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO	TB_MACHINE_ACCESS_HISTORY( ");
		sb.append("	HISTORY_ID,DATE,MACHINE_ID,ACCESS_COUNT,ILLEGAL_ACCESS_COUNT) ");
		sb.append("	VALUES(nextval('SEQ_TB_MACHINE_ACCESS_HISTORY'),?,?,?,?)");

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS: " + bean.toString());
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setDate(i++, new java.sql.Date(bean.getStatisticsdate().getTime()));
			stmt.setInt(i++, bean.getMachineId());
			stmt.setInt(i++, bean.getAccesstotaltimes());
			stmt.setInt(i++, bean.getIllegalvisittimes());

			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}

		return result;
	}

	public int insertMonitorCommon(MonitorCommonUploadBean bean) throws SQLException {
		String FUNCTION_NAME = "insertMonitorCommon() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		sb.append("	INSERT INTO	TB_MONITOR_COMMON( ");
		sb.append("	ID,MACHINE_ID,TIME,CUP_LOAD,MEMORY_LOAD,");
		sb.append("	DISK_USED,DISK_UNUSED,UPLOAD_RATE,DOWNLOAD_RATE) ");
		sb.append("	VALUES(nextval('SEQ_TB_MONITOR'),?,'"+sdf.format(bean.getTime())+"',?,?,");
		sb.append("	?,?,?,?)");

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS: " + bean.toString());
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setInt(i++, bean.getMachineId());


//			stmt.setString( i++, sdf.format(bean.getTime()) );
			
			stmt.setFloat(i++, bean.getCpuload());
			stmt.setFloat(i++, bean.getMemoryload());
			stmt.setFloat(i++, bean.getUseddisk());
			stmt.setFloat(i++, bean.getUnuseddisk());
			stmt.setFloat(i++, bean.getUploadrate());
			stmt.setFloat(i++, bean.getDownloadrate());

			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}

		return result;
	}

	public int insertUKeyManagement(UKeyAccessBean bean) throws SQLException {
		String FUNCTION_NAME = "insertUKeyManagement() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sb.append("	INSERT INTO	TB_UKEY_MANAGEMENT( ");
		sb.append("	KEY_ID,MACHINE_ID,USE_TIME) ");
		sb.append("	VALUES(nextval('SEQ_TB_UKEY_MANAGEMENT'),?,'"+sdf.format(bean.getAccesstime())+"')");

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS: " + bean.toString());
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setInt(i++, bean.getMachineId());
//			stmt.setString( i++, sdf.format(bean.getTime()) );

			result = stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}

		return result;
	}

	
	public int insertMachineEnv(MachineEnvUploadBean bean) throws SQLException {
		String FUNCTION_NAME = "insertMachineEnv() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO	TB_MACHINE_ENVIRONMENT( ");
		sb.append("	HISTORY_ID,MACHINE_ID,MACHINE_TYPE,MANUFACTURE_DATE,MANUFACTURER,");
		sb.append("	DEVICE_NO,MACHINE_KIND,CPU_FREQUENCY,OS,DISK_SIZE,MEMORY_SIZE,");
		sb.append("	VERSION,BROWSER_NAME,BROWSER_VERSION,OPERATETIME) ");
		sb.append("	VALUES(nextval('SEQ_TB_MACHINE_INFORMATION'),?,?,'"+StringUtils.getYyyyMMddHHmmss(bean.getManufacturedate())+"',?,");
		sb.append("	?,?,?,?,?,?,");
		sb.append("	?,?,?,now())");

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS: " + bean.toString());
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setInt(i++, bean.getMachineId());
			stmt.setString(i++, bean.getMachinetype());
			//stmt.setDate(i++, bean.getManufacturedate());
			stmt.setString(i++, bean.getManufacturer());
			stmt.setString(i++, bean.getDeviceno());
			stmt.setString(i++, bean.getMachineKind());
			stmt.setString(i++, bean.getCpurate());
			stmt.setString(i++, bean.getOs());
			stmt.setInt(i++, bean.getDisksize());
			stmt.setInt(i++, bean.getMemerysize());

			stmt.setString(i++, bean.getVersion());
			stmt.setString(i++, bean.getBrowsername());
			stmt.setString(i++, bean.getBrowserversion());

			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}

		return result;
	}

	/*
	 * 更新端机环境信息
	 */	
	public int updateMachineEnv(MachineEnvUploadBean bean) throws SQLException {
		String FUNCTION_NAME = "updateMachineEnv() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		sb.append("	UPDATE TB_MACHINE_ENVIRONMENT SET  MACHINE_TYPE =?, ");
		sb.append("	MANUFACTURE_DATE='"+StringUtils.getYyyyMMddHHmmss(bean.getManufacturedate())+"', ");
		sb.append("	MANUFACTURER=?,DEVICE_NO=?,MACHINE_KIND=?,CPU_FREQUENCY=?,OS=?,DISK_SIZE=?, ");
		sb.append(" MEMORY_SIZE=?,VERSION=?,BROWSER_NAME=?,BROWSER_VERSION=?,OPERATETIME=now() ");
		sb.append(" WHERE MACHINE_ID = ? "); 

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS: " + bean.toString());
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setString(i++, bean.getMachinetype());
			//stmt.setDate(i++, bean.getManufacturedate());
			stmt.setString(i++, bean.getManufacturer());
			stmt.setString(i++, bean.getDeviceno());
			stmt.setString(i++, bean.getMachineKind());
			stmt.setString(i++, bean.getCpurate());
			stmt.setString(i++, bean.getOs());
			stmt.setInt(i++, bean.getDisksize());
			stmt.setInt(i++, bean.getMemerysize());
			stmt.setString(i++, bean.getVersion());
			stmt.setString(i++, bean.getBrowsername());
			stmt.setString(i++, bean.getBrowserversion());
			stmt.setInt(i++, bean.getMachineId());

			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}

		return result;
	}

	/*
	 * 取得端机环境信息
	 */	
	public HashMap getEnvInfoByMachinId(int machinId) throws SQLException {

		String FUNCTION_NAME = "getMachinInfoByMac() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM TB_MACHINE_ENVIRONMENT WHERE MACHINE_ID=? ");

		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS : machinId=" + machinId);

		try {
			if (con == null) connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setInt(i++, machinId);
			
			HashMap[] infoMap = select();
			if (infoMap == null || infoMap.length == 0) {
				return null;
			}
			return infoMap[0];

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	public HashMap getMachinInfoByMac(String mac) throws SQLException {

		String FUNCTION_NAME = "getMachinInfoByMac() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT MACHINE_ID,OPEN_TIME,CLOSE_TIME FROM TB_CCB_MACHINE M WHERE M.MAC=?");

		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS : mac=" + mac);

		try {
			if (con == null) connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString(1, mac);
			HashMap[] infoMap = select();
			if (infoMap == null || infoMap.length == 0) {
				return null;
			}
			return infoMap[0];

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}

	public int getMachinIdByMac(String mac) throws SQLException {

		String FUNCTION_NAME = "getMachinIdByMac() ";
		logger.info(FUNCTION_NAME + "start");
		HashMap map = getMachinInfoByMac(mac);
		if (map == null) {
			logger.info(FUNCTION_NAME + "end");
			return 0;
		} else {
			logger.info(FUNCTION_NAME + "end");
			return Integer.parseInt(map.get("MACHINE_ID").toString());
		}

	}

	public boolean checkUserAuthenticate(String username, String password) throws SQLException {

		String FUNCTION_NAME = "getMachinIdByMac() ";
		logger.info(FUNCTION_NAME + "start");
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM tb_ccb_user WHERE NAME = ? AND PASSWORD = ?");

		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "PARAMS : username=" + username);
		logger.info(FUNCTION_NAME + "PARAMS : password=" + password);

		try {
			if (con == null) connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString(1, username);
			stmt.setString(2, password);
			HashMap[] userInfoMap = select();
			if (userInfoMap == null || userInfoMap.length == 0) {
				return false;
			}
			return true;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
		
	}
}
