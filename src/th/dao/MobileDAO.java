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
public class MobileDAO extends BaseDao {
	private Log logger = LogFactory.getLog( MobileDAO.class.getName() );
	
	public MobileDAO(){
		
	}
	
	/**
	 * @param orgID
	 * @param orderType
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMachineInfoByOrgID(int orgID) throws SQLException {

		String FUNCTION_NAME = "getMachineInfoByOrgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			List<String> orgList = new OrgDealAction().getSubOrgList(String.valueOf(orgID));
			
			if(orgList != null && orgList.size() == 1 && "".equals(orgList.get(0))){
				orgList.set(0, String.valueOf(orgID));
			} else {
				orgList.add(String.valueOf(orgID));
			}
			String inString = "";
			for(int i=0;i<orgList.size();i++){   
			    if(i>0){   
			        inString+=", ";   
			  
			    }   
			    inString+="?";   
			} 
			
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("	o.org_id,");
			sb.append("	o.org_name,");
			sb.append("	o.org_level,");
			sb.append("	o.parent_org_id,");
			sb.append("	m.machine_id,");
			sb.append("	m.machine_name,");
			sb.append("	m.machine_mark,");
			sb.append("	m.STATUS ");
			sb.append("FROM");
			sb.append("	tb_ccb_machine m,");
			sb.append("	tb_ccb_organization o ");
			sb.append("WHERE");
			sb.append("	o.org_id = m.org_id ");
			//端机状态 != 3:未审核
			sb.append("	and m.status <> '3'");
			sb.append("	and o.org_id in(" + inString + ")");
			
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			//logger.info(FUNCTION_NAME + "role_id = " + orgID);

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			for(int j=0;j<orgList.size();j++){   
				stmt.setLong(j+1, Long.parseLong(orgList.get(j)));
			}

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	/**
	 * 取得线路中断端机信息
	 * @param machineID
	 * @param breakTime : 15 Minutes
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getBreakMachineInfoByOrderType(List<Integer> machineID, String breakTime) throws SQLException {

		String FUNCTION_NAME = "getBreakMachineInfoByOrderType() ";
		logger.info( FUNCTION_NAME + "start" );
		
		try{
			
			StringBuffer sb = new StringBuffer();
			String inString = "";
			for(int i=0;i<machineID.size();i++){   
			    if(i>0){   
			        inString+=", ";   
			  
			    }   
			    inString+="?";   
			} 
			
			sb.append("	SELECT");
			sb.append("		tc.machine_id,");
			sb.append("		tc.machine_mark,");
			sb.append("		tc.machine_name");
			sb.append("	FROM");
			sb.append("		(");
			sb.append("			SELECT");
			sb.append("				M .machine_id,");
			sb.append("				to_number(");
			sb.append("					to_char(");
			sb.append("						age(CURRENT_TIMESTAMP, M .ctime),");
			sb.append("						'HH24MISS'");
			sb.append("					),");
			sb.append("					'S9999999'");
			sb.append("				)AS diff");
			sb.append("			FROM");
			sb.append("				(");
			sb.append("					SELECT");
			sb.append("						(");
			sb.append("							MAX(newest_time)+ INTERVAL '" + breakTime + "'");
			sb.append("						)AS ctime,");
			sb.append("						machine_id");
			sb.append("					FROM");
			sb.append("						tb_newest_pulse_management");
			sb.append("					WHERE");
			sb.append("						machine_id IN(" + inString + ")");
			sb.append("					GROUP BY");
			sb.append("						machine_id");
			sb.append("				)M");
			sb.append("		)e,");
			sb.append("		tb_ccb_machine tc");
			sb.append("	WHERE");
			sb.append("		e.machine_id = tc.machine_id");
			sb.append("	AND e.diff > 0");
			
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			
			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			for(int j=0;j<machineID.size();j++){   
				stmt.setInt(j+1, machineID.get(j));   
			}
			HashMap[] map = select();
			
			return map;
		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getAppStoreList(String osName) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("select a.* from tb_application_management a, tb_ebank_device_management e where a.dev_id=e.dev_id and a.status='1' and e.dev_os like '%"+osName+"%' order by a.app_id");
		try {
			if (con == null)
				connection();
			stmt = con.prepareStatement(sb.toString());
			HashMap[] maps = select();
			return maps;
		} finally {
			releaseConnection();
		}
	}
	
	public HashMap[] getAppStoreUploadList(String osName) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("select a.app_name as name,a.package_name as packet,a.dl_url as downloadurl,a.version_code as versionnum,a.version as versiondisplay,a.description,a.icon_url as iconurl from tb_application_management a, tb_ebank_device_management e where a.dev_id=e.dev_id and a.status='1' and e.dev_os like '%"+osName+"%' order by a.app_id");
		try {
			if (con == null)
				connection();
			stmt = con.prepareStatement(sb.toString());
			HashMap[] maps = select();
			return maps;
		} finally {
			releaseConnection();
		}
	}

	public HashMap[] getGuestBookInfoByOrgID(int orgID) throws SQLException {

		String FUNCTION_NAME = "getGuestBookInfoByOrgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			List<String> orgList = new OrgDealAction().getSubOrgList(String.valueOf(orgID));
			
			if(orgList != null && orgList.size() == 1 && "".equals(orgList.get(0))){
				orgList.set(0, String.valueOf(orgID));
			} else {
				orgList.add(String.valueOf(orgID));
			}
			String inString = "";
			for(int i=0;i<orgList.size();i++){   
			    if(i>0){   
			        inString+=", ";   
			  
			    }   
			    inString+="?";   
			} 
			
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("	o.org_name,");
			sb.append("	m.machine_name,");
			sb.append("	m.machine_mark,");
			sb.append("	g.idea_id,");
			sb.append("	g.idea_type,");
			sb.append("	g.operatetime,");
			sb.append("	g.idea_content ");
			sb.append("FROM");
			sb.append("	tb_ccb_machine m,");
			sb.append("	tb_ccb_guestbook g,");
			sb.append("	tb_ccb_organization o ");
			sb.append("WHERE");
			sb.append("	o.org_id = m.org_id ");
			//端机状态 != 3:未审核
			sb.append("	and m.machine_id=g.machine_id ");
			sb.append("	and m.status <> '3'");
			sb.append("	and o.org_id in(" + inString + ") ");
			sb.append(" order by g.idea_id,g.idea_type ");
			
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			for(int j=0;j<orgList.size();j++){   
				stmt.setLong(j+1, Long.parseLong(orgList.get(j)));
			}

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	public HashMap[] getMessageInfoByOrgID(int orgID) throws SQLException {

		String FUNCTION_NAME = "getMessageInfoByOrgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			List<String> orgList = new OrgDealAction().getSubOrgList(String.valueOf(orgID));
			
			if(orgList != null && orgList.size() == 1 && "".equals(orgList.get(0))){
				orgList.set(0, String.valueOf(orgID));
			} else {
				orgList.add(String.valueOf(orgID));
			}
			String inString = "";
			for(int i=0;i<orgList.size();i++){   
			    if(i>0){   
			        inString+=", ";   
			  
			    }   
			    inString+="?";   
			} 
			
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("	o.org_name,");
			sb.append("	m.machine_name,");
			sb.append("	m.machine_mark,");
			sb.append("	u.name,");
			sb.append("	g.msg_id,");
			sb.append("	g.operatetime,");
			sb.append("	g.msg_content ");
			sb.append("FROM");
			sb.append("	tb_ccb_machine m,");
			sb.append("	tb_ccb_message g,");
			sb.append("	tb_ccb_user u,");
			sb.append("	tb_ccb_organization o ");
			sb.append("WHERE");
			sb.append("	o.org_id = m.org_id ");
			//端机状态 != 3:未审核
			sb.append("	and m.machine_id=g.machine_id ");
			sb.append("	and u.user_id = g.operator ");
			sb.append("	and m.status <> '3'");
			sb.append("	and o.org_id in(" + inString + ") ");
			sb.append(" order by g.msg_id ");
			
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			for(int j=0;j<orgList.size();j++){   
				stmt.setLong(j+1, Long.parseLong(orgList.get(j)));
			}

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
}
