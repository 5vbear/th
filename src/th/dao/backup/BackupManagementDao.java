package th.dao.backup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import th.dao.BaseDao;
import th.management.backup.BackupManagement;

public class BackupManagementDao extends BaseDao{
	
	private Log logger = LogFactory.getLog(BackupManagementDao.class.getName());
	private static String CLASS_NAME = "BackupManagementDao ";
	/**
	 * 监听器获取db备份设置信息
	 * @return
	 * @throws SQLException
	 */
	public HashMap getBackupMassage() throws SQLException {
		// TODO Auto-generated method stub
		logger.info("CLASS_NAME: "+CLASS_NAME+"METHOD>>getBackupMassage() start...");
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append("M .clear_internal, ");
		sb.append("M .backup_internal, ");
		sb.append("M .status ");
		sb.append("FROM ");
		sb.append("tb_backup_management M ");
		sb.append("ORDER BY ");
		sb.append("M .operatetime DESC ");
		sb.append("LIMIT 1 ");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			logger.info("SQL = "+sb.toString());
			HashMap[] map = select();
			if(null ==map||map.length==0){
				return null;
			}
			return map[0];
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally  {
			releaseConnection();
			logger.info("CLASS_NAME: "+CLASS_NAME+"METHOD>>getBackupMassage() end...");
		}
	}
	/**
	 * 获取db备份设置信息
	 * @return
	 * @throws SQLException
	 */
	public HashMap<String, String> getInfo() throws SQLException{
		logger.info("CLASS_NAME: "+CLASS_NAME+"METHOD>>getInfo() start...");
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT ");
		sb.append("M .backup_internal, ");
		sb.append("M .clear_internal, ");
		sb.append("M .status, ");
		sb.append("M .operatetime,");
		sb.append("u.name, ");
		sb.append("M .description, ");
		sb.append("M .clear_description ");
		sb.append("FROM ");
		sb.append("tb_backup_management M, ");
		sb.append("tb_ccb_user u ");
		sb.append("where u.user_id = M.operator ");
		sb.append("ORDER BY ");
		sb.append("M .operatetime DESC ");
		sb.append("LIMIT 1 ");
		HashMap[] map = null;
		try {
			connection();
			logger.info("SQL = "+sb.toString());
			stmt = con.prepareStatement(sb.toString());
			ResultSet rSet = stmt.executeQuery();
			HashMap<String, String> submap = new HashMap<String, String>();
			while(rSet.next()){
				SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String internal = rSet.getString(1);
				String clear_internal = rSet.getString(2);
				String status = rSet.getString(3);
				Date time = rSet.getTimestamp(4);
				String name = rSet.getString(5);
				String description = rSet.getString(6);
				String clear_description = rSet.getString(7);
				logger.info("BACKUP_INTERNAL = "+internal);
				logger.info("CLEAR_INTERNAL = "+clear_internal);
				logger.info("STATUS = "+status);
				logger.info("OPERATETIME = "+time);
				logger.info("NAME = "+name);
				logger.info("DESCRIPTION = "+description);
				logger.info("CLEAR_DESCRIPTION = "+clear_description);
				submap.put("BACKUP_INTERNAL", internal);
				submap.put("CLEAR_INTERNAL", clear_internal);
				submap.put("STATUS", status);
				submap.put("OPERATETIME", s.format(time));
				submap.put("NAME", name);
				submap.put("DESCRIPTION", description);
				submap.put("CLEAR_DESCRIPTION", clear_description);
			}
			logger.info("CLASS_NAME: "+CLASS_NAME+"METHOD>>getInfo() end...");
			return submap;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally  {
			releaseConnection();
		}
		return null;
	}
	/**
	 * 新建备份计划
	 * @param userId
	 * @param internal
	 * @param description 
	 * @param clear_description 
	 * @param clear_internal 
	 * @param stauts
	 * @throws SQLException 
	 */
	public void addBackupPlan(long userId,String internal,String status, String description, String clear_internal, String clear_description) throws SQLException{
		StringBuilder sb = new StringBuilder();
		logger.info("CLASS_NAME: "+CLASS_NAME+"METHOD>>addBackupPlan() start...");
		sb.append("INSERT INTO tb_backup_management VALUES ( ");
		sb.append("nextval('SEQ_TB_BACKUP_MANAGEMENT'),?,?,?,?,?, now(),?");
		sb.append(")");

		logger.info("BACKUP_INTERNAL = "+internal);
		logger.info("CLEAR_INTERNAL = "+clear_internal);
		logger.info("STATUS = "+status);
		logger.info("OPERATETIME = "+new Date());
		logger.info("OPERATOR = "+userId);
		logger.info("DESCRIPTION = "+description);
		logger.info("CLEAR_DESCRIPTION = "+clear_description);
		try {
			connection();
			logger.info("SQL = "+sb.toString());
			stmt = con.prepareStatement(sb.toString());
			stmt.setInt(1, Integer.parseInt(internal));
			stmt.setInt(2, Integer.parseInt(clear_internal));
			stmt.setString(3, description);
			stmt.setString(4, clear_description);
			stmt.setString(5, status);
			stmt.setLong(6, userId);
			update();
			commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new SQLException("SQL execute error!");
		} finally  {
			releaseConnection();
		}

		logger.info("CLASS_NAME: "+CLASS_NAME+"METHOD>>addBackupPlan() end...");
	}
}
