package th.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import th.db.DBAccess;
import th.entity.UpdateHistorySearchBean;
import th.entity.interactive.PositionUploadBean;
import th.util.StringUtils;

public class AppManagementDao extends DBAccess {

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
		return query("SELECT 'org_'||org_id AS id,'org_'||parent_org_id AS pId,org_name AS name,'org' AS mactype FROM tb_ccb_organization", false);
	}
	public HashMap[] getAllOrgByJsonFormatFen(String orgIds) throws SQLException {
		return query("SELECT 'org_'||org_id AS id,'org_'||parent_org_id AS pId,org_name AS name,'org' AS mactype FROM tb_ccb_organization WHERE org_id in"+orgIds, false);
	}
	/**
	 * 取得机器与组织的对应关系(3=未审核除外)
	 * @param orgID
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMachineInfoWithOrgByJsonFormat() throws SQLException {
		return query(
				"SELECT 'mac_'||machine_id AS id,'org_'||org_id AS pId,machine_mark AS name,'mac' AS mactype FROM TB_CCB_MACHINE WHERE status <>'3' AND org_id IS NOT NULL ORDER BY org_id",
				false);
	}
	public HashMap[] getMachineInfoWithOrgByJsonFormatFen(String orgIds) throws SQLException {
		return query(
				"SELECT 'mac_'||machine_id AS id,'org_'||org_id AS pId,machine_mark AS name,'mac' AS mactype FROM TB_CCB_MACHINE WHERE status <>'3' AND org_id IS NOT NULL AND org_id IN "+orgIds+" ORDER BY org_id",
				false);
	}
	public HashMap[] getMachineInfoByMachineIds(String ids) throws SQLException {
		return query("SELECT * FROM tb_ccb_machine WHERE status <>'3' AND machine_id IN (" + ids + ")", true);
	}

	public int getUpdateDataCount() throws SQLException {
		HashMap[] map = query("SELECT COUNT(*) COUNT	 FROM TB_APPLICATION_MANAGEMENT T,TB_EBANK_DEVICE_MANAGEMENT T1 WHERE T.DEV_ID=T1.DEV_ID ", true);
		if (map == null || map.length == 0) {
			return 0;
		}
		return Integer.parseInt(map[0].get("COUNT").toString());
	}
	public int getUpdateDataCount(String appName,String versionType,String osType) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append( "SELECT COUNT(*) COUNT	 FROM TB_APPLICATION_MANAGEMENT T,TB_EBANK_DEVICE_MANAGEMENT T1 WHERE T.DEV_ID=T1.DEV_ID " );
		
		if (StringUtils.isNotBlank(appName)) {
			sb.append("and T.APP_NAME like '%" + appName + "%' ");
		}
		if (StringUtils.isNotBlank(versionType)&&!versionType.equals( "all" )) {
			sb.append("and T.VERSION_TYPE ='" + versionType +"' ");
		}
		if (StringUtils.isNotBlank(osType)&&!osType.equals( "all" )) {
			sb.append("and T.DEV_ID =" + osType +" ");
		}
		HashMap[] map = query(sb.toString(), true);

		if (map == null || map.length == 0) {
			return 0;
		}
		return Integer.parseInt(map[0].get("COUNT").toString());
	}
	
	public boolean deleteApp(String ids) throws SQLException {
		String FUNCTION_NAME = "deleteApp() ";
		int result = 0;
		try {
			String sql = "DELETE FROM TB_APPLICATION_MANAGEMENT d WHERE d.app_id IN (" + ids + ") ";
			if (con == null) connection();
			stmt = con.prepareStatement(sql);
			logger.info(FUNCTION_NAME + "sql = " + sql);

			if (con == null) connection();
			stmt = con.prepareStatement(sql);
			stmt.clearParameters();
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
		return true;
	}
	public HashMap[] getUpdateData(int start, int pageSize, String ids,String appName ,String versionType ,String osType) throws SQLException {
		StringBuffer sb = new StringBuffer();
		
//		sb.append("SELECT * from TB_APPLICATION_MANAGEMENT where 1=1 ");
//		sb.append( "SELECT T.APP_ID AS APP_ID,T.DEV_ID AS DEV_ID,T.APP_NAME AS APP_NAME,T.DESCRIPTION AS DESCRIPTION,T.DL_URL AS DL_URL,"
//				+ "T.ICON_URL AS ICON_URL,T.OPERATETIME AS OPERATETIME,	T.OPERATOR AS OPERATOR,(CASE T.STATUS	WHEN  '0' THEN '未上架'"
//				+ "ELSE  '已上架'	END	) AS STATUS	 FROM TB_APPLICATION_MANAGEMENT T where 1=1" );
		sb.append( "SELECT T.APP_ID AS APP_ID,T.DEV_ID AS DEV_ID,T1.DEV_OS AS DEV_OS ,T.APP_NAME AS APP_NAME,T.DESCRIPTION AS DESCRIPTION,T.VERSION AS VERSION,T.DL_URL AS DL_URL,T.VERSION_TYPE AS VERSION_TYPE,T.PACKAGE_NAME AS PACKAGE_NAME ,"
				+ "T.ICON_URL AS ICON_URL,T.OPERATETIME AS OPERATETIME,	T.OPERATOR AS OPERATOR,(CASE T.STATUS	WHEN  '0' THEN '未上架'"
				+ "ELSE  '已上架'	END	) AS STATUS	 FROM TB_APPLICATION_MANAGEMENT T,TB_EBANK_DEVICE_MANAGEMENT T1 WHERE T.DEV_ID=T1.DEV_ID " );
		if (StringUtils.isNotBlank(appName)) {
			sb.append("and T.APP_NAME like '%" + appName + "%' ");
		}
		if (StringUtils.isNotBlank(versionType)&&!versionType.equals( "all" )) {
			sb.append("and T.VERSION_TYPE ='" + versionType +"' ");
		}
		if (StringUtils.isNotBlank(osType)&&!osType.equals( "all" )) {
			sb.append("and T.DEV_ID =" + osType +" ");
		}
		
		if (StringUtils.isNotBlank(ids)) {
			sb.append("and app_id IN (" + ids + ") ");
		}
		sb.append("ORDER BY operatetime DESC ");
		if (pageSize != -1) {
			sb.append(" LIMIT " + pageSize + " OFFSET " + start);
		}

		return query(sb.toString(), true);
	}
	public HashMap[] getUpdateData(int start, int pageSize, String ids) throws SQLException {
		StringBuffer sb = new StringBuffer();
		
		
		
		
//		sb.append("SELECT * from TB_APPLICATION_MANAGEMENT where 1=1 ");
		
		
//		sb.append( "SELECT T.APP_ID AS APP_ID,T.DEV_ID AS DEV_ID,T.APP_NAME AS APP_NAME,T.DESCRIPTION AS DESCRIPTION,T.DL_URL AS DL_URL,"
//				+ "T.ICON_URL AS ICON_URL,T.OPERATETIME AS OPERATETIME,	T.OPERATOR AS OPERATOR,(CASE T.STATUS	WHEN  '0' THEN '未上架'"
//				+ "ELSE  '已上架'	END	) AS STATUS	 FROM TB_APPLICATION_MANAGEMENT T where 1=1" );
		sb.append( "SELECT T.APP_ID AS APP_ID,T.DEV_ID AS DEV_ID,T1.DEV_OS AS DEV_OS ,T.APP_NAME AS APP_NAME,T.DESCRIPTION AS DESCRIPTION,T.VERSION AS VERSION,T.DL_URL AS DL_URL,T.VERSION_TYPE AS VERSION_TYPE,T.PACKAGE_NAME AS PACKAGE_NAME ,"
				+ "T.ICON_URL AS ICON_URL,T.OPERATETIME AS OPERATETIME,	T.OPERATOR AS OPERATOR,(CASE T.STATUS	WHEN  '0' THEN '未上架'"
				+ "ELSE  '已上架'	END	) AS STATUS	 FROM TB_APPLICATION_MANAGEMENT T,TB_EBANK_DEVICE_MANAGEMENT T1 WHERE T.DEV_ID=T1.DEV_ID " );
	

		
		if (StringUtils.isNotBlank(ids)) {
			sb.append("and app_id IN (" + ids + ") ");
		}
		sb.append("ORDER BY operatetime DESC ");
		if (pageSize != -1) {
			sb.append(" LIMIT " + pageSize + " OFFSET " + start);
		}

		return query(sb.toString(), true);
	}

	public HashMap[] getAllMachineData() throws SQLException {
		return query("SELECT * FROM tb_ccb_machine m ORDER BY m.machine_id ASC", true);
	}

	public int getSystemUpdateManagementCount(String ids,String status) throws SQLException {
		String FUNCTION_NAME = "getSystemUpdateManagement() ";
		if (StringUtils.isBlank(ids) || StringUtils.isBlank(status)) {
			return 0;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT count(1) COUNT FROM tb_system_update_management d WHERE d.file_id IN (" + ids + ") AND d.status='"+status+"'");
		HashMap[] result = query(sb.toString(), true);
		if(result == null||result.length==0){
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
//		sb.append("	VALUES(nextval('SEQ_TB_SYSTEM_UPDATE_MANAGEMENT'),?,?,?,?,?)");
		sb.append("	VALUES(nextval('SEQ_TB_SYSTEM_UPDATE_MANAGEMENT'),?,?,?,now(),?)");

		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "machineId: " + machineId);
		logger.info(FUNCTION_NAME + "fileId: " + fileId);
		logger.info(FUNCTION_NAME + "status: " + status);
//		logger.info(FUNCTION_NAME + "date: " + date.toString());
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
//			stmt.setDate(i++, new java.sql.Date(date.getTime()));
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

	public int updateCommandManagement(int machineId, String commandContent, String status, int commandId, Date operatetime) throws SQLException {
		String FUNCTION_NAME = "updateCommandManagement() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();

		sb.append("UPDATE tb_command_management ");
		sb.append("SET status = ?, ");
		sb.append("command_content = ?, ");
		sb.append("operatetime = '"+StringUtils.getYyyyMMddHHmmss(operatetime)+"' ");
//		sb.append("operatetime = now() ");
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
//			stmt.setDate(i++, new java.sql.Date(operatetime.getTime()));
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
		sb.append("SET location_time = '"+StringUtils.getYyyyMMddHHmmss(bean.getTime())+"', ");
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
//			stmt.setDate(i++, new java.sql.Date(bean.getTime().getTime()));
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
		logger.info( FUNCTION_NAME + "start" );
		
		long macId = uhsb.getMacId();
		String macMark = uhsb.getMacMark();
		long fileId = uhsb.getFileId();
		String fileName = uhsb.getFileName();
		String startTime = uhsb.getStartTime();
		String endTime = uhsb.getEndTime();

		try{

			StringBuffer sb = new StringBuffer();
		
			sb.append( "select tum.id, tcm.machine_mark, tud.file_name, tud.description, " );
			sb.append( "(case when tum.status = '0' then '升级成功' else '待升级' end) as status, tum.operatetime, tcu.real_name " );
			sb.append( "from tb_system_update_management tum, " );
			sb.append( "tb_system_update_data tud, " );
			sb.append( "tb_ccb_machine tcm, " );
			sb.append( "tb_ccb_user tcu " );
			sb.append( "where tum.file_id = tud.file_id " );
			sb.append( "and tum.machine_id = tcm.machine_id " );
			sb.append( "and tum.operator = tcu.user_id " );
			if(macId!=-1){
				sb.append( "and tum.machine_id = " + macId + " " );
			}
			if(!"".equals( macMark )){
				sb.append( "and tcm.machine_mark like '%" + macMark + "%' " );
			}
			if(fileId!=-1){
				sb.append( "and tum.file_id = " + fileId + " " );
			}
			if(!"".equals( fileName )){
				sb.append( "and tud.file_name like '%" + fileName + "%' " );
			}
			sb.append( "and tum.operatetime >= '" + startTime + " 00:00:00' " );
			sb.append( "and tum.operatetime < '" + endTime + " 23:59:59' " );
			sb.append( "order by tum.id asc " );
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "machine_id = " + macId);
			logger.info(FUNCTION_NAME + "machine_mark = " + macMark);
			logger.info(FUNCTION_NAME + "file_id = " + fileId);
			logger.info(FUNCTION_NAME + "file_name = " + fileName);
			logger.info(FUNCTION_NAME + "时间范围(起始) = " + startTime + " 00:00:00 ");
			logger.info(FUNCTION_NAME + "时间范围(结束) = " + endTime + " 23:59:59 ");


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

	/**
	 * @param req
	 */
	public void insertApp( HttpServletRequest req ) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param appName
	 * @param description
	 * @param dlUrl
	 * @param iconUrl
	 */
	public int insertApp( String appName, String description, String version,String dlUrl, String iconUrl ,int userId,int dev_id,String versionType,String packageName,String versionCode) throws SQLException {
		String FUNCTION_NAME = "insertApp() ";
		logger.info( FUNCTION_NAME + "start" );

			StringBuffer sb = new StringBuffer();
		
			sb.append( "insert into TB_APPLICATION_MANAGEMENT(app_id,app_name,description,version,dl_url,icon_url,operatetime,operator,dev_id,version_type,package_name,version_code)" );
			sb.append("	VALUES(nextval('SEQ_TB_APPLICATION_MANAGEMENT'),?,?,?,?,?,now(),?,?,?,?,?)");
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "appName = " + appName);
			logger.info(FUNCTION_NAME + "description = " + description);
			logger.info(FUNCTION_NAME + "version = " + version);
			logger.info(FUNCTION_NAME + "dlUrl = " + dlUrl);
			logger.info(FUNCTION_NAME + "iconUrl = " + iconUrl);
			logger.info(FUNCTION_NAME + "userId = " + userId);
			logger.info(FUNCTION_NAME + "versionType = " + versionType);
			logger.info(FUNCTION_NAME + "packageName = " + packageName);
			logger.info(FUNCTION_NAME + "versionCode = " + versionCode);
			int result = 0;

			try {
				if (con == null) connection();
				stmt = con.prepareStatement(sb.toString());
				stmt.clearParameters();
				int i = 1;
				stmt.setString(i++, appName);
				stmt.setString(i++, description);
				stmt.setString(i++, version);
				stmt.setString(i++, dlUrl);
				stmt.setString(i++, iconUrl);
				stmt.setInt(i++, userId);
				stmt.setInt(i++, dev_id);
				stmt.setString(i++, versionType);
				stmt.setString(i++, packageName);
				stmt.setString(i++, versionCode);
				
				result = stmt.executeUpdate();
			} catch (SQLException e) {
				throw e;
			} finally {
				if (!con.getAutoCommit()) {
					con.commit();
				}
				releaseConnection();
				logger.info(FUNCTION_NAME + "end");
			}

			return result;
	}
	/**
	 * @param appName
	 * @param description
	 * @param dlUrl
	 * @param iconUrl
	 */
	public int updateApp( String app_id, String appName, String description, String version,String dlUrl, String iconUrl ,int userId,int dev_id,String versionType,String packageName,String versionCode) throws SQLException {
		String FUNCTION_NAME = "updateApp() ";
		logger.info( FUNCTION_NAME + "start" );

		StringBuffer sb = new StringBuffer();
		try {
			String queryString = "SELECT PACKAGE_NAME,VERSION_CODE FROM TB_APPLICATION_MANAGEMENT T WHERE T.APP_ID="
					+ app_id;

			HashMap[] appData = query( queryString, true );
			String packageNameTemp = "";
			String versionCodeTemp = "";
			if ( appData != null && appData.length > 0 ) {
				packageNameTemp = (String) appData[0].get( "PACKAGE_NAME" );
				versionCodeTemp = (String) appData[0].get( "VERSION_CODE" );
			}
			if ( !StringUtils.isBlank( packageNameTemp ) && StringUtils.isBlank( packageName ) ) {
				packageName = packageNameTemp;
			}
			if ( !StringUtils.isBlank( versionCodeTemp ) && StringUtils.isBlank( versionCode ) ) {
				versionCode = versionCodeTemp;
			}

		}
		catch ( SQLException e ) {
			throw e;
		}
		
		
			sb.append( "update TB_APPLICATION_MANAGEMENT set dev_id=?,app_name=?,description=?,version=?,dl_url=?,icon_url=?,operatetime=now(),operator=? ,version_type =? ,package_name=?,version_code=? where app_id=?" );
//			sb.append("	VALUES(nextval('SEQ_TB_APPLICATION_MANAGEMENT'),?,?,?,?,now(),?,?)");
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "app_id = " + app_id);
			logger.info(FUNCTION_NAME + "appName = " + appName);
			logger.info(FUNCTION_NAME + "description = " + description);
			logger.info(FUNCTION_NAME + "version = " + version);
			logger.info(FUNCTION_NAME + "dlUrl = " + dlUrl);
			logger.info(FUNCTION_NAME + "iconUrl = " + iconUrl);
			logger.info(FUNCTION_NAME + "userId = " + userId);
			logger.info(FUNCTION_NAME + "versionType = " + versionType);
			logger.info(FUNCTION_NAME + "packageName = " + packageName);
			logger.info(FUNCTION_NAME + "versionCode = " + versionCode);
			
			logger.info(FUNCTION_NAME + "dev_id = " + dev_id);

			int result = 0;

			try {
				if (con == null) connection();
				stmt = con.prepareStatement(sb.toString());
				stmt.clearParameters();
				int i = 1;
				stmt.setInt(i++, dev_id);
				stmt.setString(i++, appName);
				stmt.setString(i++, description);
				stmt.setString(i++, version);
				
				stmt.setString(i++, dlUrl);
				stmt.setString(i++, iconUrl);
				stmt.setInt(i++, userId);
				stmt.setString(i++, versionType);
				stmt.setString(i++, packageName);
				stmt.setString(i++, versionCode);
				
				stmt.setInt(i++, Integer.parseInt( app_id));
				result = stmt.executeUpdate();
			} catch (SQLException e) {
				throw e;
			} finally {
				if (!con.getAutoCommit()) {
					con.commit();
				}
				releaseConnection();
				logger.info(FUNCTION_NAME + "end");
			}

			return result;
	}
	
	public int updateApp( String app_ids, int status) throws SQLException {
		String FUNCTION_NAME = "insertApp() ";
		logger.info( FUNCTION_NAME + "start" );

			StringBuffer sb = new StringBuffer();
		
			sb.append( "update TB_APPLICATION_MANAGEMENT set status=? " );
			sb.append("	where app_id IN (" + app_ids + ")");
			
			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			


			int result = 0;

			try {
				if (con == null) connection();
				stmt = con.prepareStatement(sb.toString());
				stmt.clearParameters();
				int i = 1;
				stmt.setInt(i++, status);
			//stmt.setString(i++, app_id);
				
				result = stmt.executeUpdate();
			} catch (SQLException e) {
				throw e;
			} finally {
				if (!con.getAutoCommit()) {
					con.commit();
				}
				releaseConnection();
				logger.info(FUNCTION_NAME + "end");
			}

			return result;
	}
	
	public HashMap[] getAllOSTypes() throws SQLException {

		String FUNCTION_NAME = "getAllOSType() ";
		logger.info( FUNCTION_NAME + "start" );
		
		try{

			StringBuffer sb = new StringBuffer();
		
			sb.append( "SELECT DISTINCT os FROM tb_machine_environment ORDER BY os" );
			
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
}
