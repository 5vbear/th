/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import th.action.OrgDealAction;
import th.action.report.ReportCommonAction;

/**
 * Descriptions
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class FaultAO extends BaseDao {
	private Log logger = LogFactory.getLog( FaultAO.class.getName() );
	
	public FaultAO(){
		
	}
	
	public HashMap[] getFaultInfoByOrgID(int orgID, String startTime, String endTime, String macMark) throws SQLException {

		String FUNCTION_NAME = "getFaultInfoByOrgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
//			List<String> orgList = new OrgDealAction().getSubOrgList(String.valueOf(orgID));
//			
//			if(orgList != null && orgList.size() == 1 && "".equals(orgList.get(0))){
//				orgList.set(0, String.valueOf(orgID));
//			} else {
//				orgList.add(String.valueOf(orgID));
//			}
			String inString = ReportCommonAction.getFirstOrgIdsByParentId(String.valueOf(orgID));
//			for(int i=0;i<orgList.size();i++){   
//			    if(i>0){   
//			        inString+=", ";   
//			  
//			    }   
//			    inString+="?";   
//			} 
			
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("	o.org_name,");
			sb.append("	f.machine_mark,");
			sb.append("	f.fault_type, ");
			sb.append("	f.fault_num ");
			sb.append("FROM");
			sb.append("	tb_ccb_organization o, ");
			sb.append("	(");
			sb.append("		SELECT");
			sb.append("			cc.machine_mark,");
			sb.append("			cc.status,");
			sb.append("			cc.org_id,");
			sb.append("			fault.machine_id,");
			sb.append("			al.alert_name AS fault_type,");
			sb.append("			COUNT (fault.help_id) AS fault_num");
			sb.append("		FROM");
			sb.append("			tb_fault_management fault,");
			sb.append("			tb_ccb_machine cc,");
			sb.append("			mt_alert_management al ");
			sb.append("		WHERE");
			sb.append(" 	cc.MACHINE_ID = fault.MACHINE_ID and ");
			sb.append(" 	al.alert_id = fault.help_id and ");
			if(!"".equals(macMark)){
				sb.append(" 	cc.MACHINE_MARK like '%"+macMark.toUpperCase()+"%' and ");
			}
			sb.append(" 	fault.operatetime >= TO_TIMESTAMP('"+startTime+"','YYYYMMDDHH24MISS') and ");
			sb.append(" 	fault.operatetime < TO_TIMESTAMP('"+endTime+"','YYYYMMDDHH24MISS') ");
			sb.append(" group by ");
			sb.append("			cc.machine_mark,");
			sb.append("			fault.machine_id,");
			sb.append("			al.alert_name,");
			sb.append("			cc.status,");
			sb.append("			cc.org_id");
			sb.append(" ) f ");
			sb.append("WHERE");
			sb.append("	o.org_id = f.org_id ");
			//端机状态 != 3:未审核
			sb.append("	and f.status <> '3'");
			sb.append("	and o.org_id in(" + inString + ")");
			sb.append("	order by o.org_id,f.machine_id,f.fault_num desc");
			
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			//logger.info(FUNCTION_NAME + "role_id = " + orgID);

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
//			for(int j=0;j<orgList.size();j++){   
//				stmt.setLong(j+1, Long.parseLong(orgList.get(j)));
//			}

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
}
