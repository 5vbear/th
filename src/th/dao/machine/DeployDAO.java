package th.dao.machine;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class DeployDAO extends MachineDAO {

	public HashMap[] getMacList(String orgIds) throws SQLException{
		StringBuilder sb = new StringBuilder();
		sb.append("select mac.machine_id mid,");
		sb.append("		  org.org_name oname, ");
		sb.append("		  mac.machine_mark mmark, ");
		sb.append("		  mac.branch_name bname, ");
		sb.append("		  mac.maneger_name maname, ");
		sb.append("		  mac.contact_telephone tphone, ");
		sb.append("		  me.os os, ");
		sb.append("		  me.machine_kind machine_kind, ");
		sb.append("		  to_char(mac.operatetime, 'yyyy-mm-dd hh24:mi:ss') rtime ");
		sb.append("  from tb_ccb_machine mac, tb_ccb_organization org,tb_machine_environment me ");
		sb.append(" where mac.org_id = org.org_id and mac.machine_id = me.machine_id ");
		sb.append("and mac.org_id in (");
		sb.append(orgIds);
		sb.append(" )");
		sb.append(" order by mac.operatetime desc");
		
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
	 * @param orgIdsByParentId
	 * @return
	 * @throws SQLException 
	*/
	public Map[] getOrgMaps(String orgIds) throws SQLException {
		StringBuffer sb = new StringBuffer();
		sb.append( " SELECT org_id oid," );
		sb.append( " 	    org_name oname " );
		sb.append( " FROM tb_ccb_organization " );
		sb.append( " WHERE org_id in ( " + orgIds + " ) " );
		try {
			connection();
			stmt = con.prepareStatement( sb.toString() );
			stmt.clearParameters();
			return select();
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
	public int saveData(HttpServletRequest req) throws SQLException {
		String macIdStd = req.getParameter("macIdStd");
		String orgId = req.getParameter("orgId");
		String bname = req.getParameter("bname");
		String baddr = req.getParameter("baddr");
		String bno = req.getParameter("bno");
		String ryears = req.getParameter("ryears");
		String maname = req.getParameter("maname");
		String coname = req.getParameter("coname");
		String tphone = req.getParameter("tphone");
		String cphone = req.getParameter("cphone");
		String hotime = req.getParameter("hotime");
		String motime = req.getParameter("motime");
		String hctime = req.getParameter("hctime");
		String mctime = req.getParameter("mctime");
		String odate = req.getParameter("odate");
		try {
			connection();
			stmt = con.prepareStatement(generalUpdateMacSql());
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(orgId));
			stmt.setString(2, bname);
			stmt.setString(3, baddr);
			stmt.setString(4, bno);
			stmt.setString(5, ryears);
			stmt.setString(6, maname);
			stmt.setString(7, coname);
			stmt.setString(8, tphone);
			stmt.setString(9, cphone);
			Date otime = new Date(2013, 8, 21, Integer.parseInt(hotime), Integer.parseInt(motime));
			Date ctime = new Date(2013, 8, 21, Integer.parseInt(hctime), Integer.parseInt(mctime));
			stmt.setTimestamp(10, new Timestamp(otime.getTime()));
			stmt.setTimestamp(11, new Timestamp(ctime.getTime()));
			stmt.setString(12, odate);
			stmt.setLong(13, Long.parseLong(macIdStd));
			int result = update();
			commit();
			return result;
		}
		finally {
			releaseConnection();
		}
		
	}
	
	private String generalUpdateMacSql(){
		StringBuffer sb = new StringBuffer();
		sb.append( " UPDATE tb_ccb_machine " );
		sb.append( " 	SET org_id=?, " );
		sb.append( " 	    branch_name=?, " );
		sb.append( " 	    branch_address=?, " );
		sb.append( " 	    branch_no=?, " );
		sb.append( " 	    free_repair_year=?, " );
		sb.append( " 	    maneger_name=?, " );
		sb.append( " 	    contact_name=?, " );
		sb.append( " 	    contact_telephone=?, " );
		sb.append( " 	    contact_cellphone=?, " );
		sb.append( " 	    open_time=?, " );
		sb.append( " 	    close_time=?, " );
		sb.append( " 	    open_date=? " );
		sb.append( " WHERE machine_id=? " );
		return sb.toString();
	}
}
