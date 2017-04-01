package th.dao.machine;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import th.util.StringUtils;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class DictDAO extends MachineDAO {

	public HashMap[] getDictList(HttpServletRequest req) throws SQLException{
		String macType = req.getParameter("macType");
		boolean isMacType = false;
		if(StringUtils.isNotBlank(macType)){
			isMacType = true;
		}
		req.setAttribute("macType", macType);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT operation_id oid, ");
		sb.append("		  os, ");
		sb.append("		  operation_name oname, ");
		sb.append("		  operatetime otime, ");
		sb.append("		  case operation_type when '1' then '进程' else '服务' end otype ");
		sb.append("  FROM tb_legal_operation_management ");
		if(isMacType){
			sb.append(" WHERE os = ? ");
		}
		sb.append(" ORDER BY os, operatetime DESC ");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			if(isMacType){
				stmt.setString(1, macType);
			}
			return select();
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
	public int addData(HttpServletRequest req) throws SQLException {
		String macType = req.getParameter("macType");
		String operType = req.getParameter("operType");
		String operName = req.getParameter("operName");
		try {
			connection();
			stmt = con.prepareStatement(generalInsertSql());
			stmt.clearParameters();
			stmt.setString(1, macType);
			stmt.setString(2, operName);
			stmt.setString(3, operType);
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
	 * @param cfgIds
	 * @throws SQLException 
	*/
	public int delete(String operIds) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("delete from tb_legal_operation_management t");
		sb.append(" where operation_id in ( ");
		sb.append(operIds);
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
	 * @return
	*/
	private String generalInsertSql() {
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO tb_legal_operation_management( ");
		sb.append("		  	   operation_id, os, operation_name, operation_type, operatetime) ");
		sb.append("VALUES (nextval('seq_tb_legal_operation_management'), ?, ?, ?, current_timestamp); ");
		return sb.toString();
	}
}
