package th.dao.machine;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import th.dao.ClientDAO;
import javax.servlet.http.HttpServletRequest;

import th.action.MonitorAction;
import th.action.OrgDealAction;
import th.user.User;
import th.util.StringUtils;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class RepairDAO extends MachineDAO {

	public HashMap[] getMacAlertList(HttpServletRequest req) throws SQLException{
		String SelectOrg = req.getParameter("SelectOrg");
		String machineName = req.getParameter("machineName");
		String operateTime_s = req.getParameter("operateTime_s");
		String operateTime_e = req.getParameter("operateTime_e");
		req.setAttribute("SelectOrg", SelectOrg);
		req.setAttribute("machineName", machineName);
		req.setAttribute("operateTime_s", operateTime_s);
		req.setAttribute("operateTime_e", operateTime_e);
		if(machineName!=null) {
			try {
				machineName = new String(machineName.getBytes("iso8859-1"),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		boolean isMachineName = true;
		if(StringUtils.isBlank(machineName)){
			isMachineName = false;
		}
		boolean isSelectOrg = true;
		if(StringUtils.isBlank(SelectOrg)){
			isSelectOrg = false;
		}
		boolean isOperateTime_s = true;
		if(StringUtils.isBlank(operateTime_s)){
			isOperateTime_s = false;
		}		
		boolean isOperateTime_e = true;
		if(StringUtils.isBlank(operateTime_e)){
			isOperateTime_e = false;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT alert.id aid, ");
		sb.append("		  mac.machine_mark mmark, ");
		sb.append("		  amgt.alert_name atype, ");
		sb.append("		  alert.description adesc, ");
		sb.append("		  to_char(alert.time , 'yyyy-mm-dd hh24:mi:ss') atime ");
		sb.append("  FROM tb_monitor_alert alert, tb_ccb_machine mac, mt_alert_management amgt ");
		sb.append(" WHERE alert.machine_id = mac.machine_id ");
		sb.append("   AND alert.alert_id = amgt.alert_id ");
		sb.append("   AND alert.status in ( '2' ) " );
		if(isSelectOrg){
		String allOrgIds = new ClientDAO().getOrgInfo( SelectOrg );
			
			if ( "''".equals( allOrgIds ) ) {
				allOrgIds = "'" + SelectOrg + "'";
			} else {
				allOrgIds = allOrgIds + ",'" + SelectOrg + "'";
			}
			sb.append( "	AND mac.ORG_ID IN (" );
			sb.append( allOrgIds );
			sb.append( ")" );
		}
		if(isMachineName){
		sb.append("   AND mac.MACHINE_MARK like ? " );
		}
		if(isOperateTime_s){
			operateTime_s =operateTime_s+"000000";
			sb.append(" AND mac.OPERATETIME >= TO_TIMESTAMP('"+operateTime_s+"','YYYY-MM-DDHH24MISS') " );
		}
		if(isOperateTime_e){
			operateTime_e =operateTime_e+"235959";
			sb.append("  AND mac.OPERATETIME <= TO_TIMESTAMP('"+operateTime_e+"','YYYY-MM-DDHH24MISS') " );
			}
		sb.append(" order by alert.time desc");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			int i =1;
			if(isMachineName){
				stmt.setString(i, "%"+machineName+"%");
				i++;
			}

			return select();
		} finally  {
			releaseConnection();
		}		
		
	}

	/**
	 * 
	 * 
	 * @param fid
	 * @return
	 * @throws SQLException 
	*/
	public HashMap getRepairInfoById(String fid,int type) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT alert.id aid, ");
		sb.append("		  mac.machine_mark mmark, ");
		if(type==1){
			sb.append("		  amgt.alert_name atype, ");
		} else if(type==2){
			sb.append("		  amgt.ALERT_ID atype, ");
		}
		sb.append("		  alert.description adesc, ");
		sb.append("		  to_char(alert.time , 'yyyy-mm-dd hh24:mi') atime, ");
		sb.append("		  alert.TREATMENT TREATMENT, ");
		sb.append("		  alert.DIVIDER DIVIDER, ");
		sb.append("		  alert.ASSIGE_TIME ASSIGN_TIME, ");
		sb.append("		  alert.REPAIRER REPAIRER, ");
		sb.append("		  alert.START_TIME START_TIME, ");
		sb.append("		  alert.END_TIME END_TIME, ");
		sb.append("		  alert.REPAIR_CONTENT REPAIR_CONTENT, ");
		sb.append("		  alert.CONFIRM_TIME CONFIRM_TIME, ");
		sb.append("		  alert.CONFIRMOR CONFIRMOR, ");
		sb.append("		  alert.REMARK REMARK, ");
		sb.append("		  alert.DEPARTMENT_ID DEPARTMENT_ID ");
		sb.append("  FROM tb_monitor_alert alert, tb_ccb_machine mac, mt_alert_management amgt ");
		sb.append(" WHERE alert.machine_id = mac.machine_id ");
		sb.append("   AND alert.alert_id = amgt.alert_id ");					
		sb.append("   AND alert.status in ( '2' ) " );
		sb.append(" AND alert.id = ? ");
		sb.append(" order by alert.time desc");
		

		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(fid));

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
	 * @param fid
	 * @return
	 * @throws SQLException 
	*/
	public HashMap getOrgIdByDepartmentId(String department_id) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT org_id ");
		sb.append("  FROM tb_ccb_department ");
		sb.append(" where department_id = ? ");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Integer.parseInt(department_id));

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
	 * @param fid
	 * @return
	 * @throws SQLException 
	*/
	public String updateRepairInfoById(String fid,HttpServletRequest req) throws SQLException {
		
		String departmentId = req.getParameter("SelectAjax");
		String atype = req.getParameter("ATYPE");
		String adesc = req.getParameter("ADESC");
		String atime = req.getParameter("ATIME")+req.getParameter("ATIME_hh")+req.getParameter("ATIME_mi");
		String treatment = req.getParameter("TREATMENT");
		String divider = req.getParameter("DIVIDER");
		String assign_time = req.getParameter("ASSIGN_TIME")+req.getParameter("ASSIGN_TIME_hh")+req.getParameter("ASSIGN_TIME_mi");
		String repairer = req.getParameter("REPAIRER");
		String start_time = req.getParameter("START_TIME")+req.getParameter("START_TIME_hh")+req.getParameter("START_TIME_mi");
		String end_time = req.getParameter("END_TIME")+req.getParameter("END_TIME_hh")+req.getParameter("END_TIME_mi");
		String repair_content = req.getParameter("REPAIR_CONTENT");
		String confirmor = req.getParameter("CONFIRMOR");
		String remark = req.getParameter("REMARK");
		String confirm_time = req.getParameter("CONFIRM_TIME")+req.getParameter("CONFIRM_TIME_hh")+req.getParameter("CONFIRM_TIME_mi");
		String status = "2";
		if(!"".equals(confirm_time)&&!"null".equals(confirm_time)&&confirm_time!=null){
			status = "3";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_monitor_alert ");
		sb.append("		  set TIME  = TO_TIMESTAMP('"+atime+"','YYYY-MM-DDHH24MI'), ");
		sb.append("		  ALERT_ID = ?, ");
		sb.append("		  DESCRIPTION = ?, ");	
		sb.append("		  STATUS = ?, ");				
		sb.append("		  TREATMENT = ?, ");
		sb.append("		  DIVIDER = ?, ");
		sb.append("		  ASSIGE_TIME  = TO_TIMESTAMP('"+assign_time+"','YYYY-MM-DDHH24MI'), ");
		sb.append("		  REPAIRER = ?, ");
		sb.append("		  START_TIME  = TO_TIMESTAMP('"+start_time+"','YYYY-MM-DDHH24MI'), ");
		sb.append("		  END_TIME  = TO_TIMESTAMP('"+end_time+"','YYYY-MM-DDHH24MI'), ");
		sb.append("		  REPAIR_CONTENT = ?, ");
		if("3".equals(status)){
			sb.append("		  CONFIRM_TIME = TO_TIMESTAMP('"+confirm_time+"','YYYY-MM-DDHH24MI'), ");
		}
		sb.append("		  CONFIRMOR = ?, ");
		sb.append("		  REMARK = ?, ");
		sb.append("		  DEPARTMENT_ID = ? ");
		sb.append(" WHERE id = ? ");

		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setInt(1, Integer.parseInt(atype));
			stmt.setString(2, adesc);
			stmt.setString(3, status);
			stmt.setString(4, treatment);
			stmt.setString(5, divider);
			stmt.setString(6, repairer);
			stmt.setString(7, repair_content);
			stmt.setString(8, confirmor);
			stmt.setString(9, remark);
			stmt.setInt(10, Integer.parseInt(departmentId));
			stmt.setLong(11, Long.parseLong(fid));
			
			update();
			commit();
			return status;
		} finally  {
			releaseConnection();
		}		
		
	}

	public void insertFaqInfo(String fid,HttpServletRequest req) throws SQLException{
		User user = (User) req.getSession().getAttribute( "user_info" );
		
		String atype = req.getParameter("ATYPE");
		String confirm_time = req.getParameter("CONFIRM_TIME")+req.getParameter("CONFIRM_TIME_hh")+req.getParameter("CONFIRM_TIME_mi");
		String operator = user.getId() ;
		String adesc = req.getParameter("ADESC");
		String treatment = req.getParameter("TREATMENT");
		String remark = req.getParameter("REMARK");
		String departmentId = req.getParameter("SelectAjax");
		String repairer = req.getParameter("REPAIRER");
		StringBuilder sb = new StringBuilder();
		sb.append("insert into tb_fault_management  ");
		sb.append("	( FAULT_ID, MACHINE_ID, HELP_ID, OPERATETIME, OPERATOR, QUESTION, ANSWER, REMARK, DEPARTMENT_ID,REPAIRMAN ) ");
		sb.append("values (nextval('SEQ_TB_FAULT_MANAGEMENT'),");
		sb.append("(SELECT MACHINE_ID FROM TB_MONITOR_ALERT WHERE ID = ? ),?,");
		sb.append("TO_TIMESTAMP('"+confirm_time+"','YYYY-MM-DDHH24MI'),?,?,?,?,?,?)");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setInt(1, Integer.parseInt(fid));
			stmt.setInt(2, Integer.parseInt(atype));
			stmt.setInt(3, Integer.parseInt(operator));
			stmt.setString(4, adesc);
			stmt.setString(5, treatment);
			stmt.setString(6, remark);
			stmt.setInt(7, Integer.parseInt(departmentId));
			stmt.setString(8, repairer);

			insert();
			commit();
		} finally  {
			releaseConnection();
		}	
	}
}
