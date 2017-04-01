package th.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import th.db.DBAccess;
import th.entity.UpdateHistorySearchBean;
import th.entity.interactive.PositionUploadBean;
import th.util.StringUtils;

public class UpdateManagementDao extends DBAccess {

	public HashMap[] query(String sql, boolean isToUpperCase) throws SQLException {
		String FUNCTION_NAME = "query() ";
		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sql);
			logger.info(FUNCTION_NAME + "sql = " + sql);
			stmt.clearParameters();
			HashMap[] map = select(isToUpperCase);
			return map;
		} finally {
			// release処理
			releaseConnection();
		}
	}

	public HashMap[] getAllOrgByJsonFormat() throws SQLException {
		return query("SELECT 'org_'||org_id AS id,'org_'||parent_org_id AS pId,org_name AS name,'org' AS mactype,'' AS os FROM tb_ccb_organization",
				false);
	}
	public HashMap[] getAllOrgByJsonFormatFen(String orgIds) throws SQLException {
		return query("SELECT 'org_'||org_id AS id,'org_'||parent_org_id AS pId,org_name AS name,'org' AS mactype,'' AS os FROM tb_ccb_organization WHERE org_id in"+orgIds,
				false);
	}
	/**
	 * 取得机器与组织的对应关系(3=未审核除外)
	 * @param orgID
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getSysMachineInfoWithOrgByJsonFormat() throws SQLException {
		return query(
				"SELECT 'mac_'|| a.machine_id AS id,'org_'||a.org_id AS pId,"
//				+ "a.machine_mark AS name,'mac' AS mactype,b.os AS os,c.dev_id AS dev_id "
				+ "a.machine_mark||'('||b.version||')' AS name,'mac' AS mactype,b.os AS os,c.dev_id AS dev_id,b.version AS version "
				+ "FROM TB_CCB_MACHINE a,tb_machine_environment b,tb_ebank_device_management c "
				+ "WHERE a.machine_id = b.machine_id AND a.status <>'3' AND a.org_id IS NOT NULL  AND b.os=c.dev_os ORDER BY a.org_id",
				false);
	}

	/**
	 * 取得机器与组织的对应关系(3=未审核除外)
	 * @param orgID
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMachineInfoWithOrgByJsonFormat() throws SQLException {
		return query(
				"SELECT 'mac_'|| a.machine_id AS id,'org_'||a.org_id AS pId,a.machine_mark AS name,'mac' AS mactype,b.os AS os,c.dev_id AS dev_id FROM TB_CCB_MACHINE a,tb_machine_environment b,tb_ebank_device_management c WHERE a.machine_id = b.machine_id AND a.status <>'3' AND a.org_id IS NOT NULL  AND b.os=c.dev_os ORDER BY a.org_id",
				false);
	}
	public HashMap[] getMachineInfoWithOrgByJsonFormatFen(String orgIds) throws SQLException {
		return query(
				"SELECT 'mac_'|| a.machine_id AS id,'org_'||a.org_id AS pId,a.machine_mark AS name,'mac' AS mactype,b.os AS os,c.dev_id AS dev_id FROM TB_CCB_MACHINE a,tb_machine_environment b,tb_ebank_device_management c WHERE a.machine_id = b.machine_id AND a.status <>'3' AND a.org_id IS NOT NULL  AND b.os=c.dev_os AND a.org_id in"+orgIds+" ORDER BY a.org_id",
				false);
	}
	public HashMap[] getIosMachineInfoWithOrgByJsonFormat() throws SQLException {
		return query(
				"SELECT 'mac_'|| a.machine_id AS id,'org_'||a.org_id AS pId,a.machine_mark AS name,'mac' AS mactype,b.os AS os,c.dev_id AS dev_id ,a.mac  AS MAC FROM TB_CCB_MACHINE a,tb_machine_environment b,tb_ebank_device_management c WHERE a.machine_id = b.machine_id AND a.status <>'3' AND a.org_id IS NOT NULL  AND b.os=c.dev_os AND b.os = 'IOS' ORDER BY a.org_id",
				false);
	}

	public HashMap[] getMachineInfoByMachineIds(String ids) throws SQLException {
		return query("SELECT * FROM tb_ccb_machine tcm LEFT JOIN TB_MACHINE_UPDATING tmc ON tcm.machine_id = tmc.machine_id WHERE tcm.status <>'3' AND tcm.machine_id IN (" + ids + ") AND tmc.machine_id IS NULL ", true);
	}

	public int getUpdateDataCount() throws SQLException {
		HashMap[] map = query("SELECT COUNT(*) COUNT FROM tb_system_update_data WHERE dev_id IS NULL and new_dev_id IS NOT NULL", true);

		if (map == null || map.length == 0) {
			return 0;
		}
		return Integer.parseInt(map[0].get("COUNT").toString());
	}

	public int getSysUpdateDataCount() throws SQLException {
		HashMap[] map = query("SELECT COUNT(*) COUNT FROM tb_system_update_data WHERE dev_id IS NOT NULL", true);

		if (map == null || map.length == 0) {
			return 0;
		}
		return Integer.parseInt(map[0].get("COUNT").toString());
	}

	public int deleteUpdateData(String ids) throws SQLException {
		String FUNCTION_NAME = "deleteUpdateData() ";
		if (StringUtils.isBlank(ids)) {
			return 0;
		}
		int result = 0;
		try {
			String sql = "DELETE FROM tb_system_update_data d WHERE d.file_id IN (" + ids + ") ";
			if (con == null) connection();
			stmt = con.prepareStatement(sql);
			logger.info(FUNCTION_NAME + "sql = " + sql);

			if (con == null) connection();
			stmt = con.prepareStatement(sql);
			stmt.clearParameters();
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

	public HashMap[] getUpdateData(int start, int pageSize, String ids) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT d.*,b.real_name,mm.dev_os FROM tb_system_update_data d LEFT JOIN tb_ccb_user b ON d.operator=b.user_id LEFT JOIN tb_ebank_device_management mm ON d.new_dev_id=mm.dev_id WHERE d.dev_id IS NULL and d.new_dev_id IS NOT NULL ");
		if (StringUtils.isNotBlank(ids)) {
			sb.append(" AND d.file_id IN (" + ids + ") ");
		}
		sb.append("ORDER BY d.operatetime DESC ");
		if (pageSize != -1) {
			sb.append(" LIMIT " + pageSize + " OFFSET " + start);
		}

		return query(sb.toString(), true);
	}

	public HashMap[] getSysUpdateData(int start, int pageSize, String ids) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT d.*,b.real_name,mm.dev_os");
		sb.append("   FROM tb_system_update_data d");
		sb.append("   LEFT JOIN tb_ccb_user b ON d.operator=b.user_id");
		sb.append("   LEFT JOIN tb_ebank_device_management mm ON d.dev_id=mm.dev_id");
		sb.append("  WHERE d.dev_id IS NOT NULL ");
		if (StringUtils.isNotBlank(ids)) {
			sb.append(" AND d.file_id IN (" + ids + ") ");
		}
		sb.append("ORDER BY d.operatetime DESC ");
		if (pageSize != -1) {
			sb.append(" LIMIT " + pageSize + " OFFSET " + start);
		}

		return query(sb.toString(), true);
	}

	public HashMap[] getAllMachineData() throws SQLException {
		return query("SELECT * FROM tb_ccb_machine m ORDER BY m.machine_id ASC", true);
	}

	public int getSystemUpdateManagementCount(String ids, String status) throws SQLException {
		String FUNCTION_NAME = "getSystemUpdateManagement() ";
		if (StringUtils.isBlank(ids) || StringUtils.isBlank(status)) {
			return 0;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT count(1) COUNT FROM tb_system_update_management d WHERE d.file_id IN (" + ids + ") AND d.status='" + status + "'");
		HashMap[] result = query(sb.toString(), true);
		if (result == null || result.length == 0) {
			return 0;
		}
		return Integer.parseInt(result[0].get("COUNT").toString());

	}

	public int insertSystemUpdateManagement(int machineId, int fileId, String status, Date date, int creataPerson) throws SQLException {
		String FUNCTION_NAME = "insertSystemUpdateManagement() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO	tb_system_update_management( ");
		sb.append("	id,machine_id,file_id,status,operatetime,operator) ");
		// sb.append("	VALUES(nextval('SEQ_TB_SYSTEM_UPDATE_MANAGEMENT'),?,?,?,?,?)");
		sb.append("	VALUES(nextval('SEQ_TB_SYSTEM_UPDATE_MANAGEMENT'),?,?,?,now(),?)");

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "machineId: " + machineId);
		logger.info(FUNCTION_NAME + "fileId: " + fileId);
		logger.info(FUNCTION_NAME + "status: " + status);
		// logger.info(FUNCTION_NAME + "date: " + date.toString());
		logger.info(FUNCTION_NAME + "creataPerson: " + creataPerson);
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setInt(i++, machineId);
			stmt.setInt(i++, fileId);
			stmt.setString(i++, status);
			// stmt.setDate(i++, new java.sql.Date(date.getTime()));
			stmt.setInt(i++, creataPerson);

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

	public int insertSystemUpdateData(String fileName, String fileFtpUrl, String status, int createUserId, String desc) throws SQLException {
		String FUNCTION_NAME = "insertSystemUpdateData() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO	tb_system_update_data( ");
		sb.append("	file_id,file_name,file_ftp_url,status,operatetime,operator,description) ");
		sb.append("	VALUES(nextval('SEQ_TB_SYSTEM_UPDATE_DATA'),?,?,?,now(),?,?)");

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "file_name: " + fileName);
		logger.info(FUNCTION_NAME + "file_ftp_url: " + fileFtpUrl);
		logger.info(FUNCTION_NAME + "status: " + status);
		logger.info(FUNCTION_NAME + "operator: " + createUserId);
		logger.info(FUNCTION_NAME + "description: " + desc);
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setString(i++, fileName);
			stmt.setString(i++, fileFtpUrl);
			stmt.setString(i++, status);
			stmt.setInt(i++, createUserId);
			stmt.setString(i++, desc);

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
	public int insertSystemUpdateData(String fileName, String fileFtpUrl, String status, int createUserId, String desc,int dev_id) throws SQLException {
		String FUNCTION_NAME = "insertSystemUpdateData() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO	tb_system_update_data( ");
		sb.append("	file_id,file_name,file_ftp_url,status,operatetime,operator,description,dev_id) ");
		sb.append("	VALUES(nextval('SEQ_TB_SYSTEM_UPDATE_DATA'),?,?,?,now(),?,?,?)");
		
		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "file_name: " + fileName);
		logger.info(FUNCTION_NAME + "file_ftp_url: " + fileFtpUrl);
		logger.info(FUNCTION_NAME + "status: " + status);
		logger.info(FUNCTION_NAME + "operator: " + createUserId);
		logger.info(FUNCTION_NAME + "description: " + desc);
		logger.info(FUNCTION_NAME + "dev_id: " + dev_id);
		int result = 0;
		
		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setString(i++, fileName);
			stmt.setString(i++, fileFtpUrl);
			stmt.setString(i++, status);
			stmt.setInt(i++, createUserId);
			stmt.setString(i++, desc);
			stmt.setInt(i++, dev_id);
			
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
	
	public int insertNewSystemUpdateData(String fileName, String fileFtpUrl, String status, int createUserId, String desc,int new_dev_id) throws SQLException {
		String FUNCTION_NAME = "insertSystemUpdateData() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();
		sb.append("	INSERT INTO	tb_system_update_data( ");
		sb.append("	file_id,file_name,file_ftp_url,status,operatetime,operator,description,new_dev_id) ");
		sb.append("	VALUES(nextval('SEQ_TB_SYSTEM_UPDATE_DATA'),?,?,?,now(),?,?,?)");
		
		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "file_name: " + fileName);
		logger.info(FUNCTION_NAME + "file_ftp_url: " + fileFtpUrl);
		logger.info(FUNCTION_NAME + "status: " + status);
		logger.info(FUNCTION_NAME + "operator: " + createUserId);
		logger.info(FUNCTION_NAME + "description: " + desc);
		logger.info(FUNCTION_NAME + "new_dev_id: " + new_dev_id);
		int result = 0;
		
		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setString(i++, fileName);
			stmt.setString(i++, fileFtpUrl);
			stmt.setString(i++, status);
			stmt.setInt(i++, createUserId);
			stmt.setString(i++, desc);
			stmt.setInt(i++, new_dev_id);
			
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

	public int updateCommandManagement(int machineId, String commandContent, String status, int commandId, Date operatetime) throws SQLException {
		String FUNCTION_NAME = "updateCommandManagement() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();

		sb.append("UPDATE tb_command_management ");
		sb.append("SET status = ?, ");
		sb.append("command_content = ?, ");
		sb.append("operatetime = '" + StringUtils.getYyyyMMddHHmmss(operatetime) + "' ");
		// sb.append("operatetime = now() ");
		sb.append("WHERE machine_id = ? ");
		sb.append("AND command_id = ? ");
		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "machineId: " + machineId);
		logger.info(FUNCTION_NAME + "status: " + status);
		logger.info(FUNCTION_NAME + "commandId: " + commandId);
		logger.info(FUNCTION_NAME + "commandContent: " + commandContent);
		logger.info(FUNCTION_NAME + "operatetime: " + operatetime.toString());
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setString(i++, status);
			stmt.setString(i++, commandContent);
			// stmt.setDate(i++, new java.sql.Date(operatetime.getTime()));
			stmt.setInt(i++, machineId);
			stmt.setInt(i++, commandId);
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

	public int updateCcbMachine(PositionUploadBean bean) throws SQLException {
		String FUNCTION_NAME = "updateCcbMachine() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();

		sb.append("UPDATE tb_ccb_machine ");
		sb.append("SET location_time = '" + StringUtils.getYyyyMMddHHmmss(bean.getTime()) + "', ");
		sb.append("location_latitude = ?, ");
		sb.append("location_longitude =?, ");
		sb.append("location_radius =?, ");
		sb.append("location_name =? ");
		sb.append("WHERE mac = ? ");
		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "PARMS: " + bean.toString());
		int result = 0;

		try {
			if (con == null) connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			// stmt.setDate(i++, new java.sql.Date(bean.getTime().getTime()));
			stmt.setDouble(i++, bean.getLatitude());
			stmt.setDouble(i++, bean.getLongitude());
			stmt.setDouble(i++, bean.getRadius());
			stmt.setString(i++, bean.getAddrName());
			stmt.setString(i++, bean.getMac());
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

	public HashMap[] getUpHistorysBySearchCondition(UpdateHistorySearchBean uhsb) throws SQLException {

		String FUNCTION_NAME = "getUpHistorysBySearchCondition() ";
		logger.info(FUNCTION_NAME + "start");

		long macId = uhsb.getMacId();
		String macMark = uhsb.getMacMark();
		long fileId = uhsb.getFileId();
		String fileName = uhsb.getFileName();
		String startTime = uhsb.getStartTime();
		String endTime = uhsb.getEndTime();

		try {

			StringBuffer sb = new StringBuffer();

			sb.append("select tum.id, tcm.machine_mark, tud.file_name, tud.description, ");
			sb.append("(case when tum.status = '0' then '升级成功' else '待升级' end) as status, tum.operatetime, tcu.real_name ");
			sb.append("from tb_system_update_management tum, ");
			sb.append("tb_system_update_data tud, ");
			sb.append("tb_ccb_machine tcm, ");
			sb.append("tb_ccb_user tcu ");
			sb.append("where tum.file_id = tud.file_id ");
			sb.append("and tum.machine_id = tcm.machine_id ");
			sb.append("and tum.operator = tcu.user_id ");
			sb.append("and tud.dev_id is null and tud.new_dev_id is not null ");
			if (macId != -1) {
				sb.append("and tum.machine_id = " + macId + " ");
			}
			if (!"".equals(macMark)) {
				sb.append("and tcm.machine_mark like '%" + macMark + "%' ");
			}
			if (fileId != -1) {
				sb.append("and tum.file_id = " + fileId + " ");
			}
			if (!"".equals(fileName)) {
				sb.append("and tud.file_name like '%" + fileName + "%' ");
			}
			if (!"".equals(startTime)) {
				sb.append("and tum.operatetime >= '" + startTime + " 00:00:00' ");
			}
			if (!"".equals(endTime)) {
				sb.append("and tum.operatetime < '" + endTime + " 23:59:59' ");
			} else {
				sb.append("and tum.operatetime < now() ");
			}
			//sb.append("order by tum.id asc ");
			sb.append("order by tum.operatetime desc ");

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "machine_id = " + macId);
			logger.info(FUNCTION_NAME + "machine_mark = " + macMark);
			logger.info(FUNCTION_NAME + "file_id = " + fileId);
			logger.info(FUNCTION_NAME + "file_name = " + fileName);
			logger.info(FUNCTION_NAME + "时间范围(起始) = " + startTime + " 00:00:00 ");
			logger.info(FUNCTION_NAME + "时间范围(结束) = " + endTime + " 23:59:59 ");

			// DB未接続の場合、DB接続
			if (con == null) connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public HashMap[] getSysUpHistorysBySearchCondition(UpdateHistorySearchBean uhsb) throws SQLException {
		
		String FUNCTION_NAME = "getUpHistorysBySearchCondition() ";
		logger.info(FUNCTION_NAME + "start");
		
		long macId = uhsb.getMacId();
		String macMark = uhsb.getMacMark();
		long fileId = uhsb.getFileId();
		String fileName = uhsb.getFileName();
		String startTime = uhsb.getStartTime();
		String endTime = uhsb.getEndTime();
		
		try {
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("select tum.id, tcm.machine_mark, tud.file_name, tud.description, ");
			sb.append("(case when tum.status = '0' then '升级成功' else '待升级' end) as status, tum.operatetime, tcu.real_name ");
			sb.append("from tb_system_update_management tum, ");
			sb.append("tb_system_update_data tud, ");
			sb.append("tb_ccb_machine tcm, ");
			sb.append("tb_ccb_user tcu ");
			sb.append("where tum.file_id = tud.file_id ");
			sb.append("and tum.machine_id = tcm.machine_id ");
			sb.append("and tum.operator = tcu.user_id ");
			sb.append("and tud.dev_id is not null ");
			if (macId != -1) {
				sb.append("and tum.machine_id = " + macId + " ");
			}
			if (!"".equals(macMark)) {
				sb.append("and tcm.machine_mark like '%" + macMark + "%' ");
			}
			if (fileId != -1) {
				sb.append("and tum.file_id = " + fileId + " ");
			}
			if (!"".equals(fileName)) {
				sb.append("and tud.file_name like '%" + fileName + "%' ");
			}
			if (!"".equals(startTime)) {
				sb.append("and tum.operatetime >= '" + startTime + " 00:00:00' ");
			}
			if (!"".equals(endTime)) {
				sb.append("and tum.operatetime < '" + endTime + " 23:59:59' ");
			} else {
				sb.append("and tum.operatetime < now() ");
			}
			sb.append("order by tum.id asc ");
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "machine_id = " + macId);
			logger.info(FUNCTION_NAME + "machine_mark = " + macMark);
			logger.info(FUNCTION_NAME + "file_id = " + fileId);
			logger.info(FUNCTION_NAME + "file_name = " + fileName);
			logger.info(FUNCTION_NAME + "时间范围(起始) = " + startTime + " 00:00:00 ");
			logger.info(FUNCTION_NAME + "时间范围(结束) = " + endTime + " 23:59:59 ");
			
			// DB未接続の場合、DB接続
			if (con == null) connection();
			
			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			
			HashMap[] map = select();
			
			return map;
			
		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
			
		}
		
	}

	public HashMap[] getAllOSTypes() throws SQLException {

		String FUNCTION_NAME = "getAllOSType() ";
		logger.info(FUNCTION_NAME + "start");

		try {

			StringBuffer sb = new StringBuffer();

			// sb.append( "SELECT DISTINCT os FROM tb_machine_environment ORDER BY os" );
			// sb.append( "SELECT dev_id, dev_os as os, description FROM tb_ebank_device_management ORDER BY os" );
			sb.append("SELECT dev_os as os FROM tb_ebank_device_management ORDER BY os");

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null) connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}
	
	
	public HashMap[] getAllOSInfo() throws SQLException {

		String FUNCTION_NAME = "getAllOSType() ";
		logger.info(FUNCTION_NAME + "start");

		try {

			StringBuffer sb = new StringBuffer();

			// sb.append( "SELECT DISTINCT os FROM tb_machine_environment ORDER BY os" );
			sb.append("SELECT dev_id as id, dev_os as os, description FROM tb_ebank_device_management ORDER BY os");
			// sb.append( "SELECT dev_os as os FROM tb_ebank_device_management ORDER BY os" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null) connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}

	public String getMachineMarkByMachineID(String machineID) throws SQLException {

		String FUNCTION_NAME = "getMachineMarkByMachineID() ";
		logger.info(FUNCTION_NAME + "start");

		try {

			StringBuffer sb = new StringBuffer();

			sb.append("SELECT machine_mark FROM tb_ccb_machine WHERE machine_id ="+machineID);

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null) connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

//			int i = 0;
//			stmt.setString(i++, machineID);
			
			HashMap[] map = select();
			
			return (String)map[0].get( "MACHINE_MARK" );

		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}

}
