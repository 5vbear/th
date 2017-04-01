/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import th.action.OrgDealAction;
import th.action.report.ReportCommonAction;
import th.com.util.Define;
import th.entity.AlertTypeBean;
import th.entity.MachineBean;

/**
 * Descriptions
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class UKeyDAO extends BaseDao {
	private Log logger = LogFactory.getLog( UKeyDAO.class.getName() );
	
	public UKeyDAO(){
		
	}
	
	public HashMap[] getUKeyInfoByOrgID(int orgID, String startTime, String endTime, String macMark, String useNum) throws SQLException {

		String FUNCTION_NAME = "getUKeyInfoByOrgID() ";
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
			sb.append("	m.machine_id,");
			sb.append("	m.machine_name,");
			sb.append("	m.machine_mark,");
			sb.append("	u.key_num, ");
			sb.append("	u.use_time ");
			sb.append("FROM");
			sb.append("	tb_ccb_machine m,");
			sb.append("	tb_ccb_organization o, ");
			sb.append("	(select uy.machine_id,count(uy.key_id) as key_num,max(uy.use_time) as use_time from tb_ukey_management uy,tb_ccb_machine cc where ");
			sb.append(" 	cc.MACHINE_ID = uy.MACHINE_ID and ");
			if(!"".equals(macMark)){
				sb.append(" 	cc.MACHINE_MARK like '%"+macMark.toUpperCase()+"%' and ");
			}
			sb.append(" 	uy.use_time >= TO_TIMESTAMP('"+startTime+"','YYYYMMDDHH24MISS') and ");
			sb.append(" 	uy.use_time < TO_TIMESTAMP('"+endTime+"','YYYYMMDDHH24MISS') ");
			sb.append(" group by uy.machine_id) u ");
			sb.append("WHERE");
			sb.append("	o.org_id = m.org_id ");
			sb.append("	and m.machine_id=u.machine_id ");
			//端机状态 != 3:未审核
			sb.append("	and m.status <> '3'");
			sb.append("	and o.org_id in(" + inString + ")");
			if(!"".equals(useNum)){
				sb.append(" and u.key_num>="+useNum);
			}
			sb.append("	order by o.org_id,m.machine_id");
			
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

		} catch(SQLException se){
			logger.error("SQLException = "+se.getMessage());
			return null;
		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
}
