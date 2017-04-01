package th.dao.machine;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import th.action.MonitorAction;
import th.action.OrgDealAction;
import th.action.report.ReportCommonAction;
import th.dao.ClientDAO;
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
public class FaqDAO extends MachineDAO {

	public HashMap[] getFaqList(HttpServletRequest req) throws SQLException{
		String faqType = req.getParameter("faqType");
		String machineName = req.getParameter("machineName");

		String machineType = req.getParameter("machineType");
		String SelectOrg = req.getParameter("SelectOrg");
		boolean isFaqType = true;
		if(StringUtils.isBlank(faqType)){
			isFaqType = false;
		}
		boolean isMachineName = true;
		if(StringUtils.isBlank(machineName)){
			isMachineName = false;
		}
		boolean isMachineType = true;
		if(StringUtils.isBlank(machineType)){
			isMachineType = false;
		}
		boolean isSelectOrg = true;
		if(StringUtils.isBlank(SelectOrg)){
			isSelectOrg = false;
		}
		User user = (User) req.getSession().getAttribute( "user_info" );
		List<HashMap> oList = new OrgDealAction().getChildNodesByOrgId( Long.parseLong( user.getOrg_id() ) );
		req.setAttribute( "MONITOR_ORG_LIST", new MonitorAction().buildOrgOption( oList ) );
		req.setAttribute("faqType", faqType);
		req.setAttribute("machineName", machineName);
		req.setAttribute("machineType", machineType);
		req.setAttribute("SelectOrg", SelectOrg);
		
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fm.REMARK, ");
		sb.append("		  fm.Fault_ID, ");
		sb.append("		  fm.REPAIRMAN, ");
		sb.append("		  fm.QUESTION, ");
		sb.append("		  case fm.HELP_ID when '1' then '非法进程' when '2' then '非法服务' when '3' then 'cpu负荷过高' when '4' then '内存负荷过高' when '5' then '硬盘容量不足' when '6' then '上行速率过慢' when '7' then '下行速率过慢' when '8' then '非法访问率过高' else '断线报警' end HELP_ID, ");
		sb.append("		  fm.ANSWER, ");
		sb.append("		  co.ORG_NAME, ");
		sb.append("		  cd.DEPARTMENT_NAME, ");
		sb.append("		  cm.MACHINE_MARK, ");
		sb.append("		  case me.MACHINE_TYPE when '1' then '网银自动设备'end MACHINE_TYPE, ");
		sb.append("		  me.MANUFACTURER,me.os,me.machine_kind ");
		sb.append("  FROM TB_FAULT_MANAGEMENT fm, ");
		sb.append("  TB_CCB_MACHINE cm, ");
		sb.append("  TB_MACHINE_ENVIRONMENT me, ");
		sb.append("  TB_CCB_ORGANIZATION co, ");
		sb.append("  TB_CCB_DEPARTMENT cd ");		
		sb.append(" WHERE fm.MACHINE_ID = cm.MACHINE_ID ");
		sb.append(" AND me.MACHINE_ID = cm.MACHINE_ID ");
		sb.append(" AND cm.ORG_ID = co.ORG_ID ");
		sb.append(" AND fm.DEPARTMENT_ID = cd.DEPARTMENT_ID ");
		if(isFaqType){
			sb.append(" AND fm.HELP_ID =? ");
		}
		if(isMachineName){
			sb.append(" AND cm.MACHINE_MARK like ? ");
		}
		if(isMachineType){
			if(!"0".equals(machineType)){
				String[] typeArray = machineType.split("_");
				sb.append( "	AND me.os like 	'%" );
				sb.append( typeArray[0] );
				sb.append( "%' " );
				sb.append( "	AND me.machine_kind = 	'" );
				sb.append( typeArray[1].toLowerCase() );
				sb.append( "' " );
			}
		}
		if(isSelectOrg){
//			String allOrgIds = new ClientDAO().getOrgInfo( SelectOrg );
			String allOrgIds = ReportCommonAction.getFirstOrgIdsByParentId( SelectOrg );
			
			if ( null == allOrgIds) {
				return new HashMap[0];
			}
			sb.append( "	AND cm.ORG_ID IN (" );
			sb.append( allOrgIds );
			sb.append( ")" );
		
		}
		try {
			if ( con == null ) {
				try {
					connection();
				}
				catch ( SQLException e ) {
					e.printStackTrace();
				}
			}
			stmt = con.prepareStatement(sb.toString());
			int i =1;
			if(isFaqType){
				stmt.setInt(i, Integer.parseInt(faqType));
				i++;
			}
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
	public HashMap getFaqInfoById(String fid,int type) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT fm.REMARK, ");
		sb.append("		  fm.Fault_ID, ");
		sb.append("		  fm.REPAIRMAN, ");
		sb.append("		  fm.QUESTION, ");
		if(type==1){
		sb.append("		  case fm.HELP_ID when '1' then '非法进程' when '2' then '非法服务' when '3' then 'cpu负荷过高' when '4' then '内存负荷过高' when '5' then '硬盘容量不足' when '6' then '上行速率过慢' when '7' then '下行速率过慢' when '8' then '非法访问率过高' else '断线报警' end HELP_ID, ");
		} else if(type==2){
		sb.append("		  fm.HELP_ID, ");				
		}
		sb.append("		  fm.ANSWER, ");
		sb.append("		  co.ORG_NAME, ");
		sb.append("		  cd.DEPARTMENT_NAME, ");
		sb.append("		  cm.MACHINE_MARK, ");
		sb.append("		  case me.MACHINE_TYPE when '1' then '网银自动设备'end MACHINE_TYPE, ");
		sb.append("		  me.MANUFACTURER,me.os,me.machine_kind ");
		sb.append("  FROM TB_FAULT_MANAGEMENT fm, ");
		sb.append("  TB_CCB_MACHINE cm, ");
		sb.append("  TB_MACHINE_ENVIRONMENT me, ");
		sb.append("  TB_CCB_ORGANIZATION co, ");
		sb.append("  TB_CCB_DEPARTMENT cd ");		
		sb.append(" WHERE fm.MACHINE_ID = cm.MACHINE_ID ");
		sb.append(" AND me.MACHINE_ID = cm.MACHINE_ID ");
		sb.append(" AND cm.ORG_ID = co.ORG_ID ");
		sb.append(" AND fm.DEPARTMENT_ID = cd.DEPARTMENT_ID ");
		sb.append(" AND fm.Fault_ID = ? ");

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
	public void updateFaqInfoById(String fid,HttpServletRequest req) throws SQLException {
		String faqType = req.getParameter("faqType");
		String question = (String)req.getParameter("question");
		String answer = (String)req.getParameter("answer");
		String remark = (String)req.getParameter("remark");
		String repairman = (String)req.getParameter("repairman");

		StringBuilder sb = new StringBuilder();
		sb.append("update TB_FAULT_MANAGEMENT ");
		sb.append("		  set question = ?, ");
		sb.append("		  answer = ?, ");
		sb.append("		  remark = ?, ");	
		sb.append("		  repairman = ?, ");				
		sb.append("		  HELP_ID = ? ");	
		sb.append(" WHERE Fault_ID = ? ");

		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setString(1, question);
			stmt.setString(2, answer);
			stmt.setString(3, remark);
			stmt.setString(4, repairman);
			stmt.setInt(5, Integer.parseInt(faqType));
			stmt.setLong(6, Long.parseLong(fid));

			update();
			commit();
		} finally  {
			releaseConnection();
		}		
		
	}
}
