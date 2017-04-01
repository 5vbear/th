package th.dao.machine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import th.com.util.Define4Report;
import th.entity.AlarmBean;
import th.util.DateUtil;
import th.util.StringUtils;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class AlertDAO extends MachineDAO {

	public HashMap[] getMacAlertList(HttpServletRequest req, String orgIds) throws Exception{
		
		String operType = req.getParameter("operType");
		
		String anyTypeStartTime = req.getParameter("anyTypeStartTime");
		
		if(anyTypeStartTime == null || "".equals(anyTypeStartTime)){
			anyTypeStartTime = DateUtil.getYesterdayDate(Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD);
		}
		
		anyTypeStartTime = DateUtil.getStartTime(anyTypeStartTime, DateUtil.DATE_TYPE_DAY);
		
		
		String alertinfo = (String) req.getParameter("alertinfo");
		
		if(StringUtils.isBlank(operType)){
			operType = "1";
		}
		req.setAttribute("operType", operType);
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT alert.id aid, ");
		sb.append("		  mac.machine_mark mmark, ");
		sb.append("		  '紧急' alevel, ");
		sb.append("		  amgt.alert_name atype, ");
		sb.append("		  alert.description adesc, ");
		sb.append("		  to_char(alert.time , 'yyyy-mm-dd hh24:mi:ss') atime, ");
		sb.append("		  case alert.status when '1' then '未处理' when '2' then '已派修' when '3' then '修理完成' else '已处理' end astatus ");
		sb.append("  FROM tb_monitor_alert alert, tb_ccb_machine mac, mt_alert_management amgt ");
		sb.append(" WHERE alert.machine_id = mac.machine_id ");
		sb.append("   AND alert.alert_id = amgt.alert_id ");
		sb.append("   AND mac.org_id in ( " + orgIds + " ) " );
		sb.append("   AND alert.status = ? " );
		sb.append("   AND alert.time >= TO_TIMESTAMP(?,'YYYYMMDD')");
		if( !(alertinfo==null||"".equals(alertinfo) )){
			sb.append("   AND alert.DESCRIPTION like '%"+alertinfo+"%'");
		}
		sb.append(" order by alert.time");
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setString(1, operType);
			stmt.setString(2, anyTypeStartTime);

			return select();
		} finally  {
			releaseConnection();
		}		
		
	}
	

	public HashMap[] getMacAlertDetail(String alertIds, String orgSelect) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT alert.description adesc, ");
		sb.append("  to_char(alert.time , 'yyyy-mm-dd hh24:mi:ss') atime, ");
		sb.append("  case alert.status when '1' then '未处理' when '2' then '已派修' when '3' then '修理完成' else '已处理' end astatus  , ");
		sb.append("  amgt.alert_name , ");
		sb.append("  mac.mac, ");
		sb.append("  mac.machine_name, ");
		sb.append("  mac.machine_mark, ");
		sb.append("  mac.ip, ");
		sb.append("  mac.branch_name, ");
		sb.append("  mac.branch_address, ");
		sb.append("  mac.MANeGER_NAME, ");
		sb.append("  mac.CONTACT_NAME, ");
		sb.append("  mac.OPERATETIME, ");
		sb.append("  mac.OPERATOR ");
		sb.append("  FROM tb_ccb_machine mac ,");
		sb.append("  tb_monitor_alert alert, ");
		sb.append("  mt_alert_management amgt ");
		sb.append("  where alert.machine_id = mac.machine_id ");
		sb.append("  and alert.alert_id = amgt.alert_id ");
		sb.append("  and alert.id =  " + alertIds );
		
		try{
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
	 * @param macIds
	 * @param orgid
	 * @return
	*/
	public int audit(String alertIds) throws SQLException {
		try {
			connection();
			stmt = con.prepareStatement(createAuditSql(alertIds));
			stmt.clearParameters();
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
	 * @param macIds
	 * @param orgid
	 * @return
	*/
	public int repair(String alertIds, String realname) throws SQLException {
		try {
			connection();
			stmt = con.prepareStatement(createRepairSql(alertIds, realname));
			stmt.clearParameters();
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
	 * @param macIds
	 * @param orgid
	 * @return
	*/
	public int deal(String alertIds) throws SQLException {
		try {
			connection();
			stmt = con.prepareStatement(createDealSql(alertIds));
			stmt.clearParameters();
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}
	
	private String createAuditSql(String alertIds) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE tb_monitor_alert ");
		sb.append("   SET status = '4' ");
		sb.append(" where id in ( ");
		sb.append(alertIds);
		sb.append("	)");
		return sb.toString();
	}
	
	private String createDealSql(String alertIds) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE tb_monitor_alert ");
		sb.append("   SET status = '3' ");
		sb.append(" where id in ( ");
		sb.append(alertIds);
		sb.append("	)");
		return sb.toString();
	}
	
	private String createRepairSql(String alertIds, String realname) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE tb_monitor_alert ");
		sb.append("   SET status = '2' ");
		sb.append(" , ASSIGE_TIME = current_timestamp ");
		sb.append(" , REPAIRER = '" + realname + "'");
		sb.append(" where id in ( ");
		sb.append(alertIds);
		sb.append("	)");
		return sb.toString();
	}
	
	/**
	 * 获取机器ID
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public HashMap[] getMachineId() throws SQLException {
		String FUNCTION_NAME = "getMachineId() ";
		try {

			StringBuffer sb = new StringBuffer();
			sb.append("select MACHINE_ID from TB_CCB_MACHINE where STATUS ='0' OR STATUS ='2'");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();
			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	/**
	 * 插入监控（告警）信息
	 * @param List
	 * @return
	 */
	public int[] insertAlert( List<AlarmBean> alarmList ) {
		StringBuffer stringBuffer = new StringBuffer();
		String FUNCTION_NAME = "getMachineId() ";
		stringBuffer.append( "INSERT INTO TB_MONITOR_ALERT( " );
		stringBuffer.append( " ID, " );
		stringBuffer.append( " MACHINE_ID," );
		stringBuffer.append( " TIME," );
		stringBuffer.append( " ALERT_ID," );
		stringBuffer.append( " STATUS)" );
		stringBuffer.append( " VALUES(nextval( 'SEQ_TB_MONITOR_ALERT' ),?," );
		stringBuffer.append("date_trunc( 'second', current_timestamp )," );
		stringBuffer.append("?,'1')" );

		logger.info(FUNCTION_NAME + "sql = " + stringBuffer.toString());
		for (int i = 0; i < alarmList.size(); i++) {
			logger.info(FUNCTION_NAME + "MachineID = " + alarmList.get(i).getMachineID());
			logger.info(FUNCTION_NAME + "AlertID = " + alarmList.get(i).getAlertID());
		}
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		int[] result = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			for (int i = 0; i < alarmList.size(); i++) {
				stmt.setLong( 1, Long.parseLong(alarmList.get(i).getMachineID()) );
				stmt.setInt( 2, Integer.parseInt(alarmList.get(i).getAlertID() ));
				stmt.addBatch();
			}
			result = insertBatch();
			commit();
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}catch ( Exception ex ) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return result;
	}
}
