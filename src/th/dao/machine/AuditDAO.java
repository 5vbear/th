package th.dao.machine;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class AuditDAO extends MachineDAO {

	public HashMap[] getMacAuditList() throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("(select t.machine_id mid,");
		sb.append("		  t.machine_mark mmark, ");
		sb.append("		  t.ip mip, ");
		sb.append("		  t.machine_name cname, ");
		sb.append("		  t.mac mmac, ");
		sb.append("		  '未知类型' mtype, ");
		sb.append("		  t.remark, ");
		sb.append("		  to_char(t.operatetime, 'yyyy-mm-dd hh24:mi:ss') rtime ");
		sb.append("  from tb_ccb_machine t");
		sb.append(" where t.org_id is null ");
		sb.append("   and (t.dev_id is null or t.dev_id not in (select e.dev_id from tb_ebank_device_management e)) ");
		sb.append(" union ");
		sb.append("select t.machine_id mid,");
		sb.append("		  t.machine_mark mmark, ");
		sb.append("		  t.ip mip, ");
		sb.append("		  t.machine_name cname, ");
		sb.append("		  t.mac mmac, ");
		sb.append("		  e.dev_os mtype, ");
		sb.append("		  t.remark, ");
		sb.append("		  to_char(t.operatetime, 'yyyy-mm-dd hh24:mi:ss') rtime ");
		sb.append("  from tb_ccb_machine t, tb_ebank_device_management e");
		sb.append(" where t.dev_id = e.dev_id ");
		sb.append("   and t.org_id is null)");
		sb.append(" order by rtime desc");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			return select();
		} finally  {
			releaseConnection();
		}		
		
	}
	public HashMap[] getMacInfoByMac(String mac) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("select t.machine_id mid,");
		sb.append("		  t.machine_mark mmark, ");
		sb.append("		  t.ip mip, ");
		sb.append("		  t.machine_name cname, ");
		sb.append("		  t.mac mmac, ");
		sb.append("		  to_char(t.operatetime, 'yyyy-mm-dd hh24:mi:ss') rtime ");
		sb.append("  from tb_ccb_machine t");
		sb.append(" where t.org_id is null");
		sb.append("   and t.mac = ? ");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearBatch();
			stmt.setString(1, mac);
			return select();
		} finally  {
			releaseConnection();
		}		
		
	}
	
	public int deleteMacs(String macIds) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tb_ccb_machine t");
		sb.append(" where t.machine_id in ( ");
		sb.append(macIds);
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
	 * @param macIds
	 * @param orgid
	 * @return
	*/
	public int auditMacs(String macIds, String orgid) throws SQLException {
		try {
			connection();
			stmt = con.prepareStatement(createCommandSql());
			String[] macId = macIds.split(",");
			for(int i = 0; i < macId.length; i++){
				insertMacCommandBatch(macId[i]);
			}
			insertBatch();
			stmt = con.prepareStatement(createAuditSql(macIds, orgid));
			stmt.clearParameters();
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}
	
	/**
	 * @throws SQLException 
	 * 
	 * 
	*/
	private void insertMacCommandBatch(String macId) throws SQLException {
		for(int i = 1; i <= 33; i++){
			stmt.setLong(1, Long.parseLong(macId));
			if(i==3||i==16||i==19||i==20||i==23||i==24||i==25||i==26||i==27){
				stmt.setString(2, "1");
			}else{
				stmt.setString(2, "0");
			}
			stmt.setInt(3, i);
			stmt.addBatch();
		}
	}

	private String createCommandSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO tb_command_management( ");
		sb.append("		  	   id, ");
		sb.append("		  	   machine_id, ");
		sb.append("		  	   status, ");
		sb.append("		  	   command_id, ");
		sb.append("		  	   operatetime) ");
		sb.append("VALUES (nextval('seq_tb_command_managemen'), ?, ?, ?, current_timestamp); ");
		return sb.toString();
	}
	
	private String createAuditSql(String macIds, String orgid) {
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_ccb_machine ");
		sb.append("   set org_id = ");
		sb.append(orgid);
		sb.append("     , status = 0 ");
		sb.append(" where machine_id in ( ");
		sb.append(macIds);
		sb.append("	)");
		return sb.toString();
	}
	
	public int auditRemark(String mac, String orgid) throws SQLException {
		try {
			connection();
			stmt = con.prepareStatement("update tb_ccb_machine set remark='"+orgid+",'||(select org_name from tb_ccb_organization where org_id="+orgid+") where mac='"+mac+"' ");
			stmt.clearParameters();
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}
}
