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
public class DeployDAO extends BaseDao {
	private Log logger = LogFactory.getLog( DeployDAO.class.getName() );
	
	public DeployDAO(){
		
	}
	
	public HashMap[] getDeployInfoByInfo(int orgID, String startTime, String endTime, String macMark, String macType) throws SQLException {

		String FUNCTION_NAME = "getDeployInfoByInfo() ";
		logger.info( FUNCTION_NAME + "start" );
		
		macType = th.util.StringUtils.isNotBlank(macType) ? macType : "0";
		String[] str=macType.split("_");

		try{
//			List<String> orgList = new OrgDealAction().getSubOrgList(String.valueOf(orgID));
//			
//			if(orgList != null && orgList.size() == 1 && "".equals(orgList.get(0))){
//				orgList.set(0, String.valueOf(orgID));
//			} else {
//				orgList.add(String.valueOf(orgID));
//			}
//			String inString = "";
//			for(int i=0;i<orgList.size();i++){   
//			    if(i>0){   
//			        inString+=", ";   
//			  
//			    }   
//			    inString+="?";   
//			}
			String inString = ReportCommonAction.getFirstOrgIdsByParentId(String.valueOf(orgID));
			
			StringBuffer sb = new StringBuffer();
			sb.append("	SELECT");
			sb.append("		mac.machine_id,");
			sb.append("		org.org_name,");
			sb.append("		mac.machine_mark,");
			sb.append("		mac.branch_name,");
			sb.append("		mac.maneger_name,");
			sb.append("		mac.contact_telephone,");
			sb.append("		mac.branch_address,");
			sb.append("		mac.branch_no,");
			sb.append("		mac.free_repair_year,");
			sb.append("		mac.contact_name,");
			sb.append("		mac.contact_cellphone,");
			sb.append("		mac.contact_emall,");
			sb.append("		(");
			sb.append("			to_char(mac.open_time, 'hh24') || ':' || to_char(mac.open_time, 'mi') || '--' || to_char(mac.close_time, 'hh24') || ':' || to_char(mac.close_time, 'mi')");
			sb.append("		) AS work_time,");
			sb.append("		mac.open_date,");
			sb.append("		mac.location_longitude AS lon,");
			sb.append("		mac.location_latitude AS lat,");
			sb.append("		to_char(");
			sb.append("			mac.operatetime,");
			sb.append("			'yyyy-mm-dd hh24:mi:ss'");
			sb.append("		) reg_time,");
			sb.append("env.os,env.machine_kind");
//			sb.append("			CASE env.machine_type");
//			sb.append("			WHEN '1' THEN");
//			sb.append("				'网银自动设备'");
//			sb.append("			ELSE");
//			sb.append("				'其他'");
//			sb.append("			END");
//			sb.append("		) AS mac_type");
			sb.append("	FROM");
			sb.append("		tb_ccb_organization AS org,");
			sb.append("		tb_ccb_machine AS mac,");
			sb.append("		tb_machine_environment AS env");
			sb.append("	WHERE");
			sb.append("		mac.org_id = org.org_id");
			sb.append("	AND env.machine_id = mac.machine_id");
			sb.append("	AND mac.status <> '3'");
			sb.append("	AND mac.status <> '4'");
			sb.append("	AND mac.org_id IN (" + inString + ") ");
			if(!"0".equals(str[0]) && !"0".equals(str[1])){
				sb.append("	and env.os like '%" + str[0] + "%'" );
				sb.append("	and env.machine_kind = '" + str[1] + "'" );
//				sb.append("	and mt.os = '" + str[0] + "'" );
//				sb.append("	and mt.machine_kind = '" + str[1] + "'" );
			}
			if(!"".equals(macMark)){
				sb.append("	AND mac.machine_mark LIKE '%"+macMark.toUpperCase()+"%'");
			}
			sb.append("	AND mac.operatetime >= TO_TIMESTAMP(");
			sb.append("		'"+startTime+"',");
			sb.append("		'YYYYMMDDHH24MISS'");
			sb.append("	)");
			sb.append("	AND mac.operatetime < TO_TIMESTAMP(");
			sb.append("		'"+endTime+"',");
			sb.append("		'YYYYMMDDHH24MISS'");
			sb.append("	)");
			sb.append("	ORDER BY");
			sb.append("		mac.operatetime DESC");
			
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

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
