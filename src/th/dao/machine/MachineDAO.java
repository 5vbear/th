package th.dao.machine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hsqldb.Types;

import th.dao.BaseDao;
import th.entity.OrganizationBean;
import th.util.StringUtils;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class MachineDAO extends BaseDao {

	/**
	 * 
	 * 
	 * @param cfgid 
	 * @return
	 * @throws SQLException 
	*/
	public HashMap getCfgInfo(String cfgid) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("select module_name mdname, ");
		sb.append("		  to_char(machine_start_time, 'hh24') hotime, ");
		sb.append("		  to_char(machine_start_time, 'mi') motime, ");
		sb.append("		  to_char(machine_shutdown_time, 'hh24') hctime, ");
		sb.append("		  to_char(machine_shutdown_time, 'mi') mctime, ");
		sb.append("		  screen_protect_time protime, ");
		sb.append("		  write_protect_dirs propath, ");
		sb.append("		  to_char(machine_start_time, 'hh24:mi:ss') stime, ");
		sb.append("  	  to_char(machine_shutdown_time, 'hh24:mi:ss') ctime, ");
		sb.append("  	  screen_protect_time sptime, ");
		sb.append("  	  write_protect_dirs propath, ");
		sb.append("		  screen_copy_duration scrtime, ");
		sb.append("		  screen_copy_interval ivltime, ");
		sb.append("  	  server_url surl, ");
		sb.append("		  ftp_server_ip ftpip, ");
		sb.append("		  itemname itemname, ");
		sb.append("		  description mddesc, ");
		sb.append("		  command_time commandtime ");
		sb.append("  from tb_machine_config_module");
		sb.append(" where module_id = ?");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(cfgid));
			HashMap[] maps = select();
			if(maps!=null && maps.length > 0){
				return maps[0];
			}else{
				return new HashMap();
			}
		} finally  {
			releaseConnection();
		}		
	}
	
	public HashMap getCfgMng(String orgid) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * ");
		sb.append("  FROM tb_module_management");
		sb.append(" WHERE org_id = ?");
		sb.append(" ORDER BY operatetime DESC");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(orgid));
			HashMap[] maps = select();
			if(maps!=null && maps.length > 0){
				return maps[0];
			}else{
				return new HashMap();
			}
		} finally  {
			releaseConnection();
		}		
	}

	/**
	 * 
	 * 
	 * @param orgid
	 * @return
	 * @throws Exception 
	*/
	public Map getSupOrg(String orgid) throws SQLException {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT org_level olevel, parent_org_id poid ");
		sb.append("  FROM tb_ccb_organization");
		sb.append(" WHERE org_id = ?");
		sb.append(" ORDER BY operatetime DESC");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(orgid));
			HashMap[] maps = select();
			if(maps!=null && maps.length > 0){
				return maps[0];
			}else{
				return new HashMap();
			}
		} finally  {
			releaseConnection();
		}		
	}

	/**
	 * 
	 * 
	 * @param macIds
	 * @param orgid
	 * @param userid 
	 * @throws SQLException 
	*/
	public int saveTransfer(String macIds, String orgName, String userid) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO tb_machine_transfer_history( ");
		sb.append("		  	   history_id, ");
		sb.append("		  	   machine_id, ");
		sb.append("		  	   transfer_time, ");
		sb.append("		  	   transfer_destination, ");
		sb.append("		  	   operator, ");
		sb.append("		  	   operatetime) ");
		sb.append("VALUES (nextval('seq_tb_machine_use_history'), ?, current_timestamp, ?, ?, current_timestamp); ");

		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(macIds));
			stmt.setString(2, orgName);
			stmt.setLong(3, Long.parseLong(userid));
			int result = insert();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}

	public HashMap getMacInfo(String machineID) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("select * ");
		sb.append("  from tb_ccb_machine t");
		sb.append(" where t.machine_id = ?");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(machineID));
			HashMap[] maps = select();
			if(maps!=null && maps.length > 0){
				return maps[0];
			}else{
				return new HashMap();
			}
		} finally  {
			releaseConnection();
		}		
		
	}

	public HashMap[] getMacsInfoByOrgid(String orgid) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("select mac.machine_id mid, ");
		sb.append("		  mac");
		sb.append("  from tb_ccb_machine mac, tb_machine_environment env");
		sb.append(" where mac.machine_id = env.machine_id");
		sb.append(" and mac.org_id in (");
		sb.append(orgid);
		sb.append(")");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			return select();
		} finally  {
			releaseConnection();
		}		
		
	}

	public HashMap[] getSubMacsByParentOrgid(String orgid) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("select mac.machine_id mid ");
		sb.append("  from tb_ccb_machine mac");
		sb.append(" where mac.org_id in (");
		sb.append(orgid);
		sb.append(")");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			return select();
		} finally  {
			releaseConnection();
		}		
		
	}

	public HashMap[] getAllMacsInfoByOrgid(String orgids) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("select mac.machine_id mid ");
		sb.append("  FROM tb_ccb_organization org, tb_ccb_machine mac");
		sb.append(" WHERE org.org_id = mac.org_id");
		sb.append(" AND org.org_id not in (SELECT org_id FROM tb_module_management)");
		sb.append(" AND mac.org_id in (");
		sb.append(orgids);
		sb.append(")");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			return select();
		} finally  {
			releaseConnection();
		}		
		
	}

	/**
	 * 
	 * 
	 * @param macIdStd 
	 * @return
	 * @throws SQLException 
	*/
	public Map[] getDeployInfo(String macIdStd) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("select mac.machine_id mid,");
		sb.append("		  mac.machine_mark mmark, ");
		sb.append("		  org.org_id oid, ");
		sb.append("		  org.org_name oname, ");
		sb.append("		  mac.branch_name bname, ");
		sb.append("		  mac.branch_address baddress, ");
		sb.append("		  mac.branch_no bno, ");
		sb.append("		  mac.free_repair_year ryears, ");
		sb.append("		  mac.maneger_name maname, ");
		sb.append("		  mac.contact_name coname, ");
		sb.append("		  mac.contact_telephone tphone, ");
		sb.append("		  mac.contact_cellphone cphone, ");
		sb.append("		  to_char(mac.open_time, 'hh24') hotime, ");
		sb.append("		  to_char(mac.open_time, 'mi') motime, ");
		sb.append("		  to_char(mac.close_time, 'hh24') hctime, ");
		sb.append("		  to_char(mac.close_time, 'mi') mctime, ");
		sb.append("		  mac.open_date odate, ");
		sb.append("		  to_char(mac.location_time, 'yyyy-mm-dd hh24:mi:ss') ltime, ");
		sb.append("		  mac.location_longitude llon, ");
		sb.append("		  mac.location_latitude llat, ");
		sb.append("		  mac.location_radius lrad, ");
		sb.append("		  mac.location_name lname, ");
		sb.append("		  mac.proxy_host, ");
		sb.append("		  mac.proxy_port, ");
		sb.append("		  mac.operatetime rtime ");
		sb.append("  from tb_ccb_machine mac, tb_ccb_organization org ");
		sb.append(" where mac.org_id = org.org_id ");
		sb.append("	  and mac.machine_id = ?");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(macIdStd));
			return select();
		} finally  {
			releaseConnection();
		}		
	}

	public int updateHardInfoByMacID(HttpServletRequest req) throws SQLException {
		
		String macIdStd = req.getParameter("macIdStd");
		String cpu = StringUtils.transStr(req.getParameter("cpu"));
		String oSystem = StringUtils.transStr(req.getParameter("oSystem"));
		String hardNum = StringUtils.transStr(req.getParameter("hardNum"));
		String memoryNum = StringUtils.transStr(req.getParameter("memoryNum"));
		if(StringUtils.isBlank(macIdStd)){
			return 0;
		}
		
		String FUNCTION_NAME = "updateHardInfoByMacID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "UPDATE tb_machine_environment " );
			sb.append( "SET cpu_frequency = ?," );
			sb.append( " os = ?," );
			sb.append( " disk_size = ?," );
			sb.append( " memory_size = ? " );
			sb.append( "WHERE " );
			sb.append( "	machine_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1 , cpu );
			stmt.setString( 2 , oSystem );
			if(!"".equals(hardNum)){
				stmt.setInt( 3, Integer.parseInt(hardNum));
			} else {
				stmt.setNull( 3, Types.INTEGER);
			}
			if(!"".equals(memoryNum)){
				stmt.setInt( 4, Integer.parseInt(memoryNum));
			} else {
				stmt.setNull( 4, Types.INTEGER);
			}
			
			stmt.setLong(5, Long.parseLong(macIdStd));
			int result = stmt.executeUpdate();
			return result;
		} finally {
			// 提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	/**
	 * 
	 * 
	 * @param macIdStd
	 * @return
	 * @throws SQLException 
	*/
	public Map getMoreInfo(String macIdStd) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT machine_id mid,");
		sb.append("		  case machine_type when '1' then '网络自动设备' else '其他' end mtype, ");
		sb.append("		  manufacture_date mdate, ");
		sb.append("		  manufacturer mfactory, ");
		sb.append("		  device_no dno, ");
		sb.append("		  cpu_frequency cpufreq, ");
		sb.append("		  os, ");
		sb.append("		  disk_size dsize, ");
		sb.append("		  memory_size msize, ");
		sb.append("		  \"version\" oversion, ");
		sb.append("		  browser_name browname, ");
		sb.append("		  machine_kind, ");
		sb.append("		  browser_version browversion ");
		sb.append("  from tb_machine_environment ");
		sb.append(" where machine_id = ?");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(macIdStd));
			HashMap[] maps = select();
			if(maps!=null && maps.length > 0){
				return maps[0];
			}else{
				return new HashMap();
			}
		} finally  {
			releaseConnection();
		}	
	}

	/**
	 * 
	 * 
	 * @param macIdStd
	 * @return
	 * @throws SQLException 
	*/
	public Map[] getAlertInfo(String macIdStd) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT to_char(pause_time, 'yyyy-mm-dd hh24:mi:ss') ptime,  ");
		sb.append("		  to_char(resume_time, 'yyyy-mm-dd hh24:mi:ss') rtime ");
		sb.append("  FROM tb_machine_pause_history ");
		sb.append(" WHERE machine_id = ? ");
		sb.append(" ORDER BY ptime ");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(macIdStd));
			return select();
		} finally  {
			releaseConnection();
		}	
	}

	/**
	 * 
	 * 
	 * @param macIdStd
	 * @return
	 * @throws SQLException 
	*/
	public Map[] getHistoryInfo(String macIdStd) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT transfer_time ttime, transfer_destination tdest ");
		sb.append("  FROM tb_machine_transfer_history ");
		sb.append(" WHERE machine_id = ? ");
		sb.append(" ORDER BY ttime ");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(macIdStd));
			return select();
		} finally  {
			releaseConnection();
		}	
	}

	public HashMap getMacConfig(String machineID) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT to_char(mod.machine_start_time, 'hh24:mi:ss') stime, ");
		sb.append("  	  to_char(mod.machine_shutdown_time, 'hh24:mi:ss') ctime, ");
		sb.append("  	  mod.screen_protect_time sptime, ");
		sb.append("  	  mod.write_protect_dirs propath, ");
		sb.append("		  screen_copy_duration scrtime, ");
		sb.append("  	  server_url surl, ");
		sb.append("		  ftp_server_ip ftpip, ");
		sb.append("		  screen_copy_interval ivltime ");
		sb.append("  FROM tb_ccb_machine mac, tb_module_management mdmg, tb_machine_config_module mod ");
		sb.append(" WHERE mac.org_id = mdmg.org_id ");
		sb.append("   AND mdmg.module_id = mod.module_id ");
		sb.append("   AND mac.machine_id = ? ");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(machineID));
			HashMap[] maps = select();
			if(maps!=null && maps.length > 0){
				return maps[0];
			}else{
				return new HashMap();
			}
		} finally  {
			releaseConnection();
		}		
		
	}
	
	public boolean isExistMacInCurrentOrg(String orgID){
		StringBuilder sb = new StringBuilder();
		sb.append("select machine_id ");
		sb.append("  from tb_ccb_machine ");
		sb.append(" where org_id = ? and status not in('3','4')");
		boolean result = false;
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(orgID));
			HashMap[] maps = select();
			if(maps!=null && maps.length > 0){
				result = true;
			}
		} catch (SQLException e){
			logger.debug(e.getMessage());
		}finally  {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				logger.debug(ee.getMessage());
			}
		}		
		return result;
	}
	
}
