package th.dao.machine;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define;
import th.user.User;


/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class ConfigDAO extends MachineDAO {

	/**
	 * 
	 * 
	 * @return
	 * @throws SQLException 
	*/
	public HashMap[] getCfgList() throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT md.module_id mdid, ");
		sb.append("		  md.module_name mdname, ");
		sb.append("		  md.description mddesc, ");
		sb.append("		  to_char(md.operatetime, 'yyyy-mm-dd hh24:mi:ss') ctime, ");
		sb.append("		  md.operator uid, ");
		sb.append("		  us.name uname ");
		sb.append("  FROM tb_machine_config_module md, tb_ccb_user us ");
		sb.append(" WHERE md.operator = us.user_id ");
		sb.append(" order by md.operatetime desc");
		
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
	 * @param cfgIds
	 * @param orgid
	 * @return
	 * @throws SQLException 
	*/
	public int insertCfg(String cfgIds, String orgid, String userId) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO tb_module_management( ");
		sb.append("		  	   mapping_id, ");
		sb.append("		  	   module_id, ");
		sb.append("		  	   org_id, ");
		sb.append("		  	   operatetime, ");
		sb.append("		  	   operator) ");
		sb.append("VALUES (nextval('seq_tb_module_management'), ?, ?, current_timestamp, ?); ");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(cfgIds));
			stmt.setLong(2, Long.parseLong(orgid));
			stmt.setLong(3, Long.parseLong(userId));
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}

	/**
	 * 
	 * 
	 * @param cfgIds
	 * @param orgid
	 * @return
	 * @throws SQLException 
	*/
	public int updateCfg(String cfgIds, String orgid) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_module_management ");
		sb.append("   set module_id = ? ");
		sb.append(" where org_id = ? ");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(cfgIds));
			stmt.setLong(2, Long.parseLong(orgid));
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}	
	
	public int isDistributed(String orgid) throws Exception{
		StringBuilder sb = new StringBuilder();
		sb.append("select count(org_id) cnt from tb_module_management where org_id = ? ");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(orgid));
			HashMap[] map = select();
			int result = Integer.parseInt((String)map[0].get("CNT"));
			return result;
		} finally  {
			releaseConnection();
		}	
	}
	
	public int deleteCfgs(String cfgIds) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tb_machine_config_module t");
		sb.append(" where t.module_id in ( ");
		sb.append(cfgIds);
		sb.append("	)");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			int result = delete();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}

	/**
	 * 
	 * 
	 * @param cfgIds
	 * @throws SQLException 
	*/
	public int deleteCfgMG(String cfgIds) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tb_module_management t");
		sb.append(" where t.module_id in ( ");
		sb.append(cfgIds);
		sb.append("	)");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			int result = delete();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}


	/**
	 * 
	 * 
	 * @param req
	 * @return
	 * @throws SQLException 
	*/
	public int editData(HttpServletRequest req) throws SQLException {
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");
		String userId = user.getId();
		String cfgid = req.getParameter("cfgid");
		String mdname = req.getParameter("mdname");
		String mddesc = req.getParameter("mddesc");
		String hotime = req.getParameter("hotime");
		String motime = req.getParameter("motime");
		String hctime = req.getParameter("hctime");
		String mctime = req.getParameter("mctime");
		String protimes = req.getParameter("protime");
		int protime;
		try{
			protime = Integer.parseInt(protimes);
		}catch (Exception e) {
			protime = 0;
		}
		String propath = req.getParameter("propath");

		try {
			connection();
			stmt = con.prepareStatement(generalUpdateCfgSql());
			stmt.clearParameters();
			stmt.setString(1, mdname);
			Date otime = new Date(2013, 8, 21, Integer.parseInt(hotime), Integer.parseInt(motime));
			Date ctime = new Date(2013, 8, 21, Integer.parseInt(hctime), Integer.parseInt(mctime));
			stmt.setTimestamp(2, new Timestamp(otime.getTime()));
			stmt.setTimestamp(3, new Timestamp(ctime.getTime()));
			stmt.setInt(4, protime);
			stmt.setString(5, propath);
			stmt.setString(6, mddesc);
			stmt.setLong(7, Long.parseLong(userId));
			stmt.setLong(8, Long.parseLong(cfgid));
			int result = update();
			commit();
			return result;
		}
		finally {
			releaseConnection();
		}		
	}

	/**
	 * 
	 * 
	 * @param req
	 * @return
	 * @throws SQLException 
	*/

	
	
	public int addData(HttpServletRequest req) throws Exception {
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");
		String userId = user.getId();
		String mdname = req.getParameter("mdname");
		String mddesc = req.getParameter("mddesc");
		String hotime;
	
			hotime = req.getParameter("hotime")==null?LocalProperties.getProperty("MACHINE_CONFIG_HOTIME"):req.getParameter("hotime");
		String motime = req.getParameter("motime")==null?LocalProperties.getProperty("MACHINE_CONFIG_MOTIME"):req.getParameter("motime");
		String hctime = req.getParameter("hctime")==null?LocalProperties.getProperty("MACHINE_CONFIG_HCTIME"):req.getParameter("hctime");
		String mctime = req.getParameter("mctime")==null?LocalProperties.getProperty("MACHINE_CONFIG_MCTIME"):req.getParameter("mctime");
		String protimes = req.getParameter("protime")==null?LocalProperties.getProperty("MACHINE_CONFIG_PROTIME"):req.getParameter("protime");
		String itemName = req.getParameter("itemName");
		String screenCopyDurationStr = req.getParameter("screenCopyDuration")==null?LocalProperties.getProperty("MACHINE_CONFIG_SCREENCOPYDURATION"):req.getParameter("screenCopyDuration");
		String screenCopyIntervalStr = req.getParameter("screenCopyInterval")==null?LocalProperties.getProperty("MACHINE_CONFIG_SCREENCOPYINTERVAL"):req.getParameter("screenCopyInterval");
		String serverUrl = req.getParameter("serverUrl")==null?LocalProperties.getProperty("HOST_SERVER"):req.getParameter("serverUrl");
		String serverIp = req.getParameter("serverIp")==null?LocalProperties.getProperty("FTP_SERVER_HOST"):req.getParameter("serverIp");
		String commandTime = req.getParameter("time")==null?LocalProperties.getProperty("MACHINE_CONFIG_COMMAND_TIME"):req.getParameter("time");
		int protime;
		try{
			protime = Integer.parseInt(protimes);
		}catch (Exception e) {
			protime = 0;
		}
		int screenCopyDuration;
		try{
			screenCopyDuration = Integer.parseInt(screenCopyDurationStr);
		}catch (Exception e) {
			screenCopyDuration = 0;
		}
		
		int commandTime_int;
		try{
			commandTime_int = Integer.parseInt(commandTime);
		}catch (Exception e) {
			commandTime_int = 0;
		}
		int screenCopyInterval;
		try{
			screenCopyInterval = Integer.parseInt(screenCopyIntervalStr);
		}catch (Exception e) {
			screenCopyInterval = 0;
		}
		String propath = req.getParameter("propath")==null?"":req.getParameter("propath");

		try {
			connection();
			stmt = con.prepareStatement(generalInsertCfgSql());
			stmt.clearParameters();
			stmt.setString(1, mdname);
			Date otime =new Date();
			if(hotime==null&&motime==null){
				
				stmt.setTimestamp(2,null );
			}else{
				 otime = new Date(2013, 8, 21, Integer.parseInt(hotime), Integer.parseInt(motime));
				stmt.setTimestamp(2, new Timestamp(otime.getTime()));
			}
			Date ctime =new Date();
			if(hctime==null&&mctime==null){
				stmt.setTimestamp(3, null);

			}else{
				 ctime = new Date(2013, 8, 21, Integer.parseInt(hctime), Integer.parseInt(mctime));
				stmt.setTimestamp(3, new Timestamp(ctime.getTime()));

			}
			
			
			stmt.setInt(4, protime);
			stmt.setString(5, propath);
			stmt.setString(6, mddesc);
			stmt.setLong(7, Long.parseLong(userId));
			
			stmt.setInt(8, screenCopyDuration);
			stmt.setInt(9, screenCopyInterval);
			stmt.setString(10, serverUrl);
			stmt.setString(11, serverIp);
			stmt.setString(12, itemName);
			stmt.setInt(13, commandTime_int);
			int result = insert();
			commit();
			return result;
		}
		finally {
			releaseConnection();
		}		
	}

	
	/**
	 * 
	 * 
	 * @param req
	 * @param cfgid
	 * @throws SQLException 
	*/
	public int addData(HttpServletRequest req, String cfgid) throws SQLException {
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");
		String userId = user.getId();
		String mddesc = req.getParameter("desc");
		String hotime = req.getParameter("hotime");
		String motime = req.getParameter("motime");
		String hctime = req.getParameter("hctime");
		String mctime = req.getParameter("mctime");
		String protimes = req.getParameter("protime");
		int protime;
		try{
			protime = Integer.parseInt(protimes);
		}catch (Exception e) {
			protime = 30;
		}
		String scrtimes = req.getParameter("scrtime");
		int scrtime;
		try{
			scrtime = Integer.parseInt(scrtimes);
		}catch (Exception e) {
			scrtime = 30;
		}
		String ivltimes = req.getParameter("ivltime");
		int ivltime;
		try{
			ivltime = Integer.parseInt(ivltimes);
		}catch (Exception e) {
			ivltime = 5;
		}
		String propath = req.getParameter("propath");
		String serverUrl = req.getParameter("serverUrl");
		String ftpIp = req.getParameter("ftpIp");

		try {
			connection();
			stmt = con.prepareStatement(generalInsertCfgSql());
			stmt.clearParameters();
			stmt.setString(1, "默认模版名称");
			Date otime = new Date(2013, 8, 21, Integer.parseInt(hotime), Integer.parseInt(motime));
			Date ctime = new Date(2013, 8, 21, Integer.parseInt(hctime), Integer.parseInt(mctime));
			stmt.setTimestamp(2, new Timestamp(otime.getTime()));
			stmt.setTimestamp(3, new Timestamp(ctime.getTime()));
			stmt.setInt(4, protime);
			stmt.setString(5, propath);
			stmt.setString(6, mddesc);
			stmt.setLong(7, Long.parseLong(userId));
			stmt.setInt(8, scrtime);
			stmt.setInt(9, ivltime);
			stmt.setLong(10, Long.parseLong(cfgid));
			stmt.setString(11, serverUrl);
			stmt.setString(12, ftpIp);
			int result = insert();
			commit();
			return result;
		}
		finally {
			releaseConnection();
		}
	}


	/**
	 * 
	 * 
	 * @param req
	 * @return
	 * @throws SQLException 
	*/
	public int editData(HttpServletRequest req, String cfgid) throws SQLException {
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");
		String userId = user.getId();
		String mddesc = req.getParameter("desc");
		String hotime = req.getParameter("hotime");
		String motime = req.getParameter("motime");
		String hctime = req.getParameter("hctime");
		String mctime = req.getParameter("mctime");
		String protimes = req.getParameter("protime");
		int protime;
		try{
			protime = Integer.parseInt(protimes);
		}catch (Exception e) {
			protime = 30;
		}
		String scrtimes = req.getParameter("scrtime");
		int scrtime;
		try{
			scrtime = Integer.parseInt(scrtimes);
		}catch (Exception e) {
			scrtime = 30;
		}
		String ivltimes = req.getParameter("ivltime");
		int ivltime;
		try{
			ivltime = Integer.parseInt(ivltimes);
		}catch (Exception e) {
			ivltime = 5;
		}
		String propath = req.getParameter("propath");
		String serverUrl = req.getParameter("serverUrl");
		String ftpIp = req.getParameter("ftpIp");

		try {
			connection();
			stmt = con.prepareStatement(generalUpdateCfgSql());
			stmt.clearParameters();
			stmt.setString(1, "默认模版名称");
			Date otime = new Date(2013, 8, 21, Integer.parseInt(hotime), Integer.parseInt(motime));
			Date ctime = new Date(2013, 8, 21, Integer.parseInt(hctime), Integer.parseInt(mctime));
			stmt.setTimestamp(2, new Timestamp(otime.getTime()));
			stmt.setTimestamp(3, new Timestamp(ctime.getTime()));
			stmt.setInt(4, protime);
			stmt.setString(5, propath);
			stmt.setString(6, mddesc);
			stmt.setLong(7, Long.parseLong(userId));
			stmt.setInt(8, scrtime);
			stmt.setInt(9, ivltime);
			stmt.setString(10, serverUrl);
			stmt.setString(11, ftpIp);
			stmt.setLong(12, Long.parseLong(cfgid));
			int result = update();
			commit();
			return result;
		}
		finally {
			releaseConnection();
		}		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String generalInsertCfgSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO tb_machine_config_module( ");
		sb.append("		  	   module_name, ");
		sb.append("		  	   machine_start_time, ");
		sb.append("		  	   machine_shutdown_time, ");
		sb.append("		  	   screen_protect_time, ");
		sb.append("		  	   write_protect_dirs, ");
		sb.append("		  	   description, ");
		sb.append("		  	   operator, ");
		sb.append("		  	   screen_copy_duration, ");
		sb.append("		  	   screen_copy_interval, ");
		sb.append("		  	   module_id, ");
		sb.append("		  	   server_url, ");
		sb.append("		  	   ftp_server_ip, ");
		sb.append("		  	   operatetime, ");
		sb.append("		  	   itemname, command_time) ");
		sb.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, nextval( 'SEQ_TB_MACHINE_INFORMATION' ), ?, ?, current_timestamp, ?,?); ");
		return sb.toString();
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String generalUpdateCfgSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE tb_machine_config_module ");
		sb.append("	  SET module_name = ?, ");
		sb.append("		  machine_start_time = ?, ");
		sb.append("		  machine_shutdown_time = ?, ");
		sb.append("		  screen_protect_time = ?, ");
		sb.append("		  write_protect_dirs = ?, ");
		sb.append("		  description = ?, ");
		sb.append("		  operator = ?, ");
		sb.append("		  screen_copy_duration = ?, ");
		sb.append("		  screen_copy_interval = ?, ");
		sb.append("		  server_url = ?, ");
		sb.append("		  ftp_server_ip = ?, ");
		sb.append("		  operatetime = current_timestamp ");
		sb.append(" WHERE module_id = ? ");
		return sb.toString();
	}

	/**
	 * 
	 * 
	 * @param cfgid
	 * @return
	 * @throws SQLException 
	*/
	public HashMap[] getOrgList(String cfgid) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT org_name oname ");
		sb.append("  from tb_module_management md, tb_ccb_organization org");
		sb.append(" where md.org_id = org.org_id");
		sb.append("   and module_id = ?");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(cfgid));
			return select();
		} finally  {
			releaseConnection();
		}		
	}

	/**
	 * 
	 * 
	 * @param macIDStd
	 * @param updateZipFilePath 
	 * @param operType
	 * @return
	 * @throws SQLException 
	*/
	public int updateCommand(String macIDStd, String updateZipFilePath) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_command_management ");
		sb.append("   set status = 1, ");
		sb.append("       command_content = ?, ");
		sb.append("       operatetime = current_timestamp ");
		sb.append(" where machine_id in ( ");
		sb.append(macIDStd);
		sb.append("	)");
		sb.append("	  and command_id = 23");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setString(1, updateZipFilePath);
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	public String generalCfgid() throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT nextval('seq_tb_machine_information') cfgid ");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			HashMap[] maps = select();
			if(maps!=null && maps.length > 0){
				return maps[0].get("CFGID").toString();
			}else{
				return "1";
			}
		} finally  {
			releaseConnection();
		}		
	}
	
	/**
	 * 
	 * 根据条件取得机器配置列表
	 * @return
	 * @throws SQLException 
	*/
	public HashMap[] searchmachineConfig(String name,String user,String fromTime,String toTime) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT md.*,md.module_id mdid, ");
		sb.append("		  md.module_name mdname, ");
		sb.append("		  md.description mddesc, ");
		sb.append("		  to_char(md.operatetime, 'yyyy-mm-dd hh24:mi:ss') ctime, ");
		sb.append("		  md.operator uid, ");
		sb.append("		  us.name uname ");
		sb.append("  FROM tb_machine_config_module md, tb_ccb_user us ");
		sb.append(" WHERE md.operator = us.user_id ");
		if(name!=null&&!"".equals(name)){
			sb.append(" AND md.module_name like '%");
			sb.append(name);
			sb.append("%' ");
		}
		if(user!=null&&!"".equals(user)){
			sb.append(" AND us.name like '%");
			sb.append(user);
			sb.append("%' ");
		}
		//添加时间From
		if (fromTime != null && !"".equals(fromTime)) {
			sb.append(" AND to_char(md.operatetime,'YYYY-MM-DD') >= '");
			sb.append(fromTime);
			sb.append("'");
		}
		//添加时间To
		if (toTime != null && !"".equals(toTime)) {
			sb.append(" AND to_char(md.operatetime,'YYYY-MM-DD') <= '");
			sb.append(toTime);
			sb.append("'");
		}
		sb.append(" order by md.operatetime desc");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			return select();
		} finally  {
			releaseConnection();
		}	
	}
}
