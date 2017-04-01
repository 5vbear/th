/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import th.action.OrgDealAction;
import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define;
import th.entity.AlertTypeBean;
import th.entity.MachineBean;
import th.entity.MachineProcessBean;
import th.entity.MachineServiceBean;
import th.util.StringUtils;

/**
 * Descriptions
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class MonitorDAO extends BaseDao {
	private Log logger = LogFactory.getLog( MonitorDAO.class.getName() );
	
	public MonitorDAO(){
		
	}
	
	/**
	 * @param orgID
	 * @param macType
	 * @param orderType
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMachineInfoByOrgID(int orgID, String macType) throws SQLException {

		String FUNCTION_NAME = "getMachineInfoByOrgID() ";
		logger.info( FUNCTION_NAME + "start" );
		
		macType = th.util.StringUtils.isNotBlank(macType) ? macType : "0";
		String[] str=macType.split("_");
		try{
//			List<String> orgList = new OrgDealAction().getSubOrgList(String.valueOf(orgID));
			List<String> orgList = new ArrayList<String>();
			
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
			sb.append("	m.location_longitude,");
			sb.append("	m.location_latitude,");
			sb.append("	m.location_name,");
			sb.append("	m.STATUS,");
			sb.append(" e.os, ");
			sb.append(" e.machine_kind ");
			sb.append("FROM");
			sb.append("	tb_ccb_machine m,");
			sb.append("	tb_ccb_organization o ");
			sb.append("	,tb_machine_environment e ");
			sb.append("WHERE");
			sb.append("	o.org_id = m.org_id ");
			//过滤端机类型			
			sb.append("	and e.machine_id = m.machine_id ");
			if(!"0".equals(macType)){
				sb.append("	and e.os like '%" + str[0] + "%'" );
				sb.append("	and e.machine_kind = '" + str[1] + "'" );
			}
			//端机状态 != 3:未审核
			sb.append("	and m.status <> '3'");
			//端机状态 != 4:报废
			sb.append("	and m.status <> '4'");
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
	 * @param macType
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getBreakMachineInfoByOrderType(List<Integer> machineID, String breakTime, String macType) throws SQLException {

		String FUNCTION_NAME = "getBreakMachineInfoByOrderType() ";
		logger.info( FUNCTION_NAME + "start" );
		
		macType = th.util.StringUtils.isNotBlank(macType) ? macType : "0";
		String[] str=macType.split("_");
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
			sb.append("		tc.machine_name,");
			sb.append("		me.os,");
			sb.append("		me.machine_kind");
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
			sb.append("	,tb_machine_environment me ");
			sb.append("	WHERE");
			sb.append("		e.machine_id = tc.machine_id");
			sb.append("	AND e.diff > 0");
			//过滤端机类型
			sb.append("	and e.machine_id = me.machine_id ");
			if(!"0".equals(macType)){
				sb.append("	and me.os like '%" + str[0] + "%'" );
				sb.append("	and me.machine_kind = '" + str[1] + "'" );
			}
			
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
	/**
	 * 设置心跳
	 * @param machineID 机器名称
	 * @param commandID 画面设置命令编号
	 * @throws SQLException
	 */
	public void openStepOverCommand(long machineID, String commandID)
			throws SQLException {

		String FUNCTION_NAME = "openStepOverCommand() ";
		logger.info(FUNCTION_NAME + "start");

		int realCommandID = 0;
		// 刷新
		if (Define.MONITOR_RUNNING_REFREASH.equals(commandID)) {
			realCommandID = 1;
		}
		// 启用
		else if (Define.MONITOR_RUNNING_INUSE.equals(commandID)) {
			realCommandID = 3;
			updateCommonMangement(String.valueOf(machineID), 2, null, "0");
		}
		// 报停 锁定
		else if (Define.MONITOR_RUNNING_STOP.equals(commandID)) {
			realCommandID = 2;
			updateCommonMangement(String.valueOf(machineID), 3, null, "0");
		}
		// 重启
		else if (Define.MONITOR_RUNNING_RESTART.equals(commandID)) {
			realCommandID = 5;
		}
		// 关机
		else if (Define.MONITOR_RUNNING_SHUTDOWN.equals(commandID)) {
			realCommandID = 4;
		}
		// 远程
		else if (Define.MONITOR_RUNNING_REMOUTE.equals(commandID)) {
			realCommandID = 9;
		}
		// 清除数据
		else if (Define.MONITOR_CLEAR_DATA.equals(commandID)) {
			realCommandID = 8;
		}
		// 定位
		else if (Define.MONITOR_LOCATE.equals(commandID)) {
			realCommandID = 27;
		}
		// 启动截屏
		else if (Define.MONITOR_START_SCREEN_SHOT.equals(commandID)) {
			realCommandID = 6;
			updateCommonMangement(String.valueOf(machineID), 7, null, "0");
		}
		// 停止截屏
		else if (Define.MONITOR_STOP_SCREEN_SHOT.equals(commandID)) {
			realCommandID = 7;
			updateCommonMangement(String.valueOf(machineID), 6, null, "0");
		}
		// 开始广告播放
		else if (Define.MONITOR_START_ADV.equals(commandID)) {
			realCommandID = 10;
			updateCommonMangement(String.valueOf(machineID), 11, null, "0");
		}
		// 停止广告播放
		else if (Define.MONITOR_END_ADV.equals(commandID)) {
			realCommandID = 11;
			updateCommonMangement(String.valueOf(machineID), 10, null, "0");
		}
		// 开始播放临时广告
		else if (Define.MONITOR_START_TEMP_ADV.equals(commandID)) {
			realCommandID = 12;
			updateCommonMangement(String.valueOf(machineID), 13, null, "0");
		}
		// 停止播放临时广告
		else if (Define.MONITOR_END_TEMP_ADV.equals(commandID)) {
			realCommandID = 13;
			updateCommonMangement(String.valueOf(machineID), 12, null, "0");
		}
		// 锁定
		else if (Define.MONITOR_LOCK.equals(commandID)) {
			realCommandID = 28;
			updateCommonMangement(String.valueOf(machineID), 29, null, "0");
		}
		// 解锁
		else if (Define.MONITOR_UNLOCK.equals(commandID)) {
			realCommandID = 29;
			updateCommonMangement(String.valueOf(machineID), 28, null, "0");
		}

		if(realCommandID != 0){
			updateCommonMangement(String.valueOf(machineID), realCommandID, null, "1");
		}

		logger.info(FUNCTION_NAME + "end");

	}
	
	public HashMap[] getMacStatusList(String macIDStd) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT status,command_id ");
		sb.append("  FROM tb_command_management ");
		sb.append(" WHERE ");
		sb.append(" machine_id in ( ");
		sb.append(macIDStd);
		sb.append("   ) ");
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
	
	/**
	 * 取得端机设备信息
	 * @param machineID
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getEquipmentInfoByMachineID(long machineID) throws SQLException {

		String FUNCTION_NAME = "getEquipmentInfoByMachineID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("	SELECT");
			sb.append("		(");
			sb.append("			CASE");
			sb.append("			WHEN e.machine_type = '1' THEN '网银自动设备'");
			sb.append("			END");
			sb.append("		)AS machine_type,");
			sb.append("		to_char(");
			sb.append("			e.manufacture_date,");
			sb.append("			' yyyy-mm-dd hh24:mi:ss'");
			sb.append("		)AS manufacture_date,");
			sb.append("		e.manufacturer,");
			sb.append("		e.device_no,");
			sb.append("		M .free_repair_year");
			sb.append("	FROM");
			sb.append("		tb_ccb_machine M,");
			sb.append("		tb_machine_environment e");
			sb.append("	WHERE");
			sb.append("		M .machine_id = e.machine_id");
			sb.append("	AND M .machine_id = ?");
			

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "machine_id = " + machineID);

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1 , machineID );

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	/**
	 * 取得端机部署信息
	 * @param machineID
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getDeployInfoByMachineID(long machineID) throws SQLException {

		String FUNCTION_NAME = "getDeployInfoByMachineID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("	SELECT");
			sb.append("		o.org_name,");
			sb.append("		M .machine_name,");
			sb.append("		M .branch_name,");
			sb.append("		M .branch_address,");
			sb.append("		M .branch_no,");
			sb.append("		M .maneger_name,");
			sb.append("		M .contact_name,");
			sb.append("		M .contact_telephone,");
			sb.append("		M .contact_cellphone,");
			sb.append("		to_char(M .open_time, 'HH24:MI:SS')AS open_time,");
			sb.append("		to_char(M .close_time, 'HH24:MI:SS')AS close_time,");
			sb.append("		M .open_date");
			sb.append("	FROM");
			sb.append("		tb_ccb_machine M,");
			sb.append("		tb_ccb_organization o");
			sb.append("	WHERE");
			sb.append("    M .org_id = o.org_id");
			sb.append("	AND M .machine_id = ?");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "machine_id = " + machineID);

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1 , machineID );

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void insertMachinePauseHistory(long machineID, long userID) throws SQLException {

		String FUNCTION_NAME = "insertMachinePauseHistory() ";
		logger.info(FUNCTION_NAME + "start");

		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "INSERT INTO tb_machine_pause_history(" );
			sb.append( "	history_id," );
			sb.append( "	machine_id," );
			sb.append( "	pause_time," );
			sb.append( "	operatetime," );
			sb.append( "	operator" );
			sb.append( ")" );
			sb.append( "VALUES" );
			sb.append( "	(" );
			sb.append( "		nextval('SEQ_TB_MACHINE_INFORMATION')," );
			sb.append( "		?, " );
			sb.append( "		CURRENT_TIMESTAMP," );
			sb.append( "		CURRENT_TIMESTAMP," );
			sb.append( "		?" );
			sb.append( "	);" );
			
			logger.info(FUNCTION_NAME + "SQL = " + sb.toString());
			logger.info(FUNCTION_NAME + "MACHINE_ID = " + machineID);
			logger.info(FUNCTION_NAME + "COMMAND_ID = " + userID);


			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1, machineID );
			stmt.setLong( 2, userID );

			insert();
			commit();
		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	/**
	 * 端机启用时，更新恢复时间
	 * @param machineID
	 * @return
	 * @throws SQLException
	 */
	public int updateMachinePauseHistory(long machineID) throws SQLException {

		String FUNCTION_NAME = "updateMachinePauseHistory() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("update tb_machine_pause_history set resume_time=CURRENT_TIMESTAMP where machine_id=? and resume_time is null");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "machine_id = " + machineID);

		try {
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			stmt.setLong(1, machineID);

			int ret = stmt.executeUpdate();

			return ret;

		} finally {

			// 提交执行结果
			if (!con.getAutoCommit()) {
				con.commit();
			}

			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/**
	 * 取得机器与组织的对应关系(3=未审核,4=报废除外)
	 * @param orgID
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getRelationBetweenMachineInfoWithOrg() throws SQLException {

		String FUNCTION_NAME = "getRelationBetweenMachineInfoWithOrg() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select org_id,machine_id,machine_name,machine_mark from tb_ccb_machine where status <>'3' and status <>'4' and org_id is not null order by org_id");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	/**
	 * 取得机器与组织的对应关系(3=未审核,4=报废除外),关联环境表
	 * @param orgID
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getRelationBetweenMachineInfoWithOrgAndEnv() throws SQLException {

		String FUNCTION_NAME = "getRelationBetweenMachineInfoWithOrg() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select m.org_id,m.machine_id,m.machine_name,m.machine_mark from tb_ccb_machine m,tb_machine_environment e where m.machine_id=e.machine_id and m.status <>'3' and m.status <>'4' and m.org_id is not null order by m.org_id;");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	/**
	 * @return
	 * @throws SQLException
	 */
	public List<MachineBean> getRelationlistBetweenMachineInfoWithOrg() throws SQLException{
		List<MachineBean> list = new ArrayList<MachineBean>();
		HashMap[] hMap = getRelationBetweenMachineInfoWithOrg();
		if (null != hMap && hMap.length != 0){
			for (int i = 0; i < hMap.length; i++) {
				Iterator it = hMap[i].entrySet().iterator();
				MachineBean bean = new MachineBean();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					if ("MACHINE_ID".equals((String) entry.getKey())) {
						bean.setMachineId(Integer.valueOf((String)entry.getValue()));
						bean.setShowMacType(getMacTypeByMacID().get((String)entry.getValue()));
					} else if("MACHINE_NAME".equals((String) entry.getKey())){
						bean.setMachineName((String) entry.getValue());
					} else if("ORG_ID".equals((String) entry.getKey())){
						bean.setOrgID(Long.parseLong((String) entry.getValue()));
					} else if("MACHINE_MARK".equals((String) entry.getKey())){
						bean.setMachineMark((String) entry.getValue());
					}
				}
				list.add(bean);
			}
		}
		return list;
	}
	
	public List<MachineBean> getRelationlistBetweenMachineInfoWithOrgAndEnv() throws SQLException{
		List<MachineBean> list = new ArrayList<MachineBean>();
		HashMap[] hMap = getRelationBetweenMachineInfoWithOrgAndEnv();
		if (null != hMap && hMap.length != 0){
			for (int i = 0; i < hMap.length; i++) {
				Iterator it = hMap[i].entrySet().iterator();
				MachineBean bean = new MachineBean();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					if ("MACHINE_ID".equals((String) entry.getKey())) {
						bean.setMachineId(Integer.valueOf((String)entry.getValue()));
						bean.setShowMacType(getMacTypeByMacID().get((String)entry.getValue()));
					} else if("MACHINE_NAME".equals((String) entry.getKey())){
						bean.setMachineName((String) entry.getValue());
					} else if("ORG_ID".equals((String) entry.getKey())){
						bean.setOrgID(Long.parseLong((String) entry.getValue()));
					} else if("MACHINE_MARK".equals((String) entry.getKey())){
						bean.setMachineMark((String) entry.getValue());
					}
				}
				list.add(bean);
			}
		}
		return list;
	}
	
	/**
	 * 根据端机ID取得端机运行信息
	 * @param machineID
	 * @param pageTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMonitorCommonInfoByMachineID(long machineID, String pageTime) throws SQLException {

		String FUNCTION_NAME = "getMonitorCommonInfoByMachineID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT id, machine_id, time, cup_load, memory_load, disk_used, disk_unused, upload_rate, download_rate ");
			sb.append("FROM tb_monitor_common ");
			sb.append("where to_char(time,'yyyymmdd') =to_char(CURRENT_TIMESTAMP,'yyyymmdd') and machine_id=? and to_date(?,'YYYY-MM-DD hh24:mi:ss')<time ");
			sb.append("order by time desc");
			
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "machine_id = " + machineID);

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong(1, machineID);
			stmt.setString(2, pageTime);

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	/**
	 * 查询结束进程命令内容
	 * @param machineID
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] queryShutdownCommonContent(String machineID) throws SQLException {

		String FUNCTION_NAME = "queryShutdownCommonMangement() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select command_content ");
			sb.append("from tb_command_management ");
			sb.append("where machine_id=? and command_id=14 ");
			

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "machine_id = " + machineID);

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1 , Long.parseLong(machineID) );

			HashMap[] map = select();
			return map;
		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	/**
	 * 更新心跳管理表
	 * @param machineID
	 * @param commonID
	 * @param content
	 * @return
	 * @throws SQLException
	 */
	public int updateCommonMangement(String machineID, int commonID, String content, String status) throws SQLException {

		String FUNCTION_NAME = "updateCommonMangement() ";
		logger.info(FUNCTION_NAME + "start");

		StringBuffer sb = new StringBuffer();
		sb.append("update tb_command_management set operatetime=CURRENT_TIMESTAMP, status=?,command_content=? where machine_id=? and command_id=? ");

		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + " machine_id=" + machineID + "; command_id=" + commonID + "; command_content=" + content);

		try {
			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			stmt.setString(1, status);
			stmt.setString(2, content);
			stmt.setLong(3, Long.parseLong(machineID));
			stmt.setInt(4, commonID);

			int ret = stmt.executeUpdate();

			return ret;

		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	/** 
	 * 取得系统合法进程 服务列表
	 * @param machineID
	 * @param type  1:进程 2:服务
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] queryLegalOperation(String machineID, String type) throws SQLException {

		String FUNCTION_NAME = "queryLegalOperation() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("	SELECT");
			sb.append("		o.operation_name");
			sb.append("	FROM");
			sb.append("		tb_machine_environment e,");
			sb.append("		tb_legal_operation_management o");
			sb.append("	WHERE");
			sb.append("		o.os = e.os");
			sb.append("	AND e.machine_id =?");
			sb.append("	AND o.operation_type =?");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "machine_id = " + machineID);
			logger.info(FUNCTION_NAME + "o.operation_type = " + type);

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1 , Long.parseLong(machineID) );
			stmt.setString(2, type);

			HashMap[] map = select();
			return map;
		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	/** 
	 * 判断端机系统类型是否存在于合法列表
	 * @param machineID
	 * @param type  1:进程 2:服务
	 * @return
	 * @throws SQLException
	 */
	public boolean isExistLegalOperation(String machineID, String type) throws SQLException {

		String FUNCTION_NAME = "isExistLegalOperation() ";
		logger.info( FUNCTION_NAME + "start" );
		boolean result = false;
		try{
			StringBuffer sb = new StringBuffer();
			sb.append("	SELECT");
			sb.append("		operation_id");
			sb.append("	FROM");
			sb.append("		tb_legal_operation_management");
			
			sb.append("	WHERE");
			sb.append("		os IN (");
			sb.append("			SELECT");
			sb.append("				e.os");
			sb.append("			FROM");
			sb.append("				tb_machine_environment e");
			sb.append("			WHERE");
			sb.append("				e.machine_id =?");
			sb.append("		)");
			sb.append("	AND operation_type =?");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "machine_id = " + machineID);
			logger.info(FUNCTION_NAME + "o.operation_type = " + type);

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1 , Long.parseLong(machineID) );
			stmt.setString(2, type);

			HashMap[] map = select();
			if(null!=map && map.length>0){
				result = true;
			}
			return result;
		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	public void insertLegalProcessList(List<MachineProcessBean> list, String machineID) throws SQLException {
		try {
			if ( con == null ) {
				try {
					connection();
				}
				catch ( SQLException e ) {
					e.printStackTrace();
				}
			}
			
			//检索端机系统
			StringBuffer seqBuffer = new StringBuffer();
			seqBuffer.append( "select os from tb_machine_environment WHERE machine_id =" + machineID);
			String os = "";
			try {
				stmt = con.prepareStatement( seqBuffer.toString() );
				stmt.clearParameters();
				HashMap[] map = select();
				os = (String) map[0].get("OS");
			} catch ( SQLException e ) {
				e.printStackTrace();
			} catch ( Exception ex ) {
				ex.printStackTrace();
			}
			
			if ( con == null ) {
				try {
					connection();
				}
				catch ( SQLException e ) {
					e.printStackTrace();
				}
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO tb_legal_operation_management( ");
			sb.append("		  	   operation_id, os, operation_name, operation_type, operatetime) ");
			sb.append("VALUES (nextval('seq_tb_legal_operation_management'), ?, ?, '1', current_timestamp); ");
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			
			for (int i = 0; i < list.size(); i++) {
				stmt.setString(1, os);
				stmt.setString(2, list.get(i).getName());
				stmt.addBatch();
			}
			insertBatch();
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
		
	}
	
	public void insertLegalServiceList(List<MachineServiceBean> list, String machineID) throws SQLException {
		try {
			if ( con == null ) {
				try {
					connection();
				}
				catch ( SQLException e ) {
					e.printStackTrace();
				}
			}
			
			//检索端机系统
			StringBuffer seqBuffer = new StringBuffer();
			seqBuffer.append( "select os from tb_machine_environment WHERE machine_id =" + machineID);
			String os = "";
			try {
				stmt = con.prepareStatement( seqBuffer.toString() );
				stmt.clearParameters();
				HashMap[] map = select();
				os = (String) map[0].get("OS");
			} catch ( SQLException e ) {
				e.printStackTrace();
			} catch ( Exception ex ) {
				ex.printStackTrace();
			}
			
			if ( con == null ) {
				try {
					connection();
				}
				catch ( SQLException e ) {
					e.printStackTrace();
				}
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO tb_legal_operation_management( ");
			sb.append("		  	   operation_id, os, operation_name, operation_type, operatetime) ");
			sb.append("VALUES (nextval('seq_tb_legal_operation_management'), ?, ?, '2', current_timestamp); ");
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			for (int i = 0; i < list.size(); i++) {
				stmt.setString(1, os);
				stmt.setString(2, list.get(i).getName());
				stmt.addBatch();
			}
			//DB插入
			int[] results = insertBatch();
			if (results == null) {
				logger.info("db未插入数据");
			}
			commit();
		}
		finally {
			releaseConnection();
		}
		
	}
	/*private String toStrFromList(List<String> list) {
		if (list != null && list.size() != 1) {
			String str = "";
			for (int k = 0; k < list.size(); k++) {
				str += list.get(k) + ",";
			}
			return str.substring(0, str.length() - 1);
		} else {
			return "";
		}
	}*/

	/**
	 * 获取特定终端的警报类型
	 * @param orgID
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getAlertType(long orgID) throws SQLException {

		String FUNCTION_NAME = "getAlertType(orgID) ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select ALERT_ID,ORG_ID,ALERT_TYPE from TB_MONITOR_ALERT_TYPE where ORG_ID =?");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1 , orgID );

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	/**
	 * 获取特定终端的警报名称
	 * @param 
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public HashMap[] getAlertManagement() throws SQLException {

		String FUNCTION_NAME = "getAlertManagement() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("select ALERT_ID,ALERT_NAME from MT_ALERT_MANAGEMENT");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	
	/**
	 * alert信息添加
	 * @param bean
	 * @return
	 */
	public int[] insertAlertTpye( List list) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "INSERT INTO TB_MONITOR_ALERT_TYPE( " );
		stringBuffer.append( " ID, " );
		stringBuffer.append( " ALERT_ID, " );
		stringBuffer.append( " ORG_ID," );
		stringBuffer.append( " ALERT_TYPE)" );
		stringBuffer.append( " VALUES(nextval( 'SEQ_TB_MONITOR_ALERT_TYPE' ),?,?,?)" );
		long orgId = 0;
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
			//媒体文件上传数量
			for (int i = 0; i <list.size() ; i++) {
				AlertTypeBean bean = (AlertTypeBean)list.get(i);
				stmt.setLong( 1,bean.getAlertId() );		
				stmt.setLong( 2,bean.getOrgId());
				orgId = bean.getOrgId();
				stmt.setString( 3,bean.getAlertType() );
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
			} catch (Exception ee ) {
				ee.printStackTrace();
			}
		}
		
		return result;
	}
	

	/**
	 *  alert信息刪除
	 * @param bean
	 * @return
	 */
	
	public boolean deleteAlertTpye(  long orgID) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "DELETE FROM TB_MONITOR_ALERT_TYPE WHERE ORG_ID ="+orgID );
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			int result = stmt.executeUpdate();
			con.commit();
			if ( result == 1 ) {
				return true;
			}
			else {
				return false;
			}
		}
		catch ( SQLException e ) {
			try {
				con.rollback();
			}
			catch ( SQLException e1 ) {
			}
		}
		finally {
			try {
				releaseConnection();
			} catch (Exception ee ) {
				ee.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 判断是否有端机非法访问服务
	 * @return boolean true--非法; false--合法 
	 * @throws SQLException
	 * @throws LocalPropertiesException 
	 * @throws NumberFormatException 
	 */
	public boolean isUnauthAccess() throws SQLException, NumberFormatException, LocalPropertiesException {

		String FUNCTION_NAME = "isUnauthAccess() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("	m.machine_name,");
			sb.append("	m.mac,");
			sb.append("	m.ip ");
			sb.append("FROM");
			sb.append("	tb_ccb_machine m,");
			sb.append("	tb_newest_pulse_management n ");
			sb.append("WHERE");
			sb.append("	m.machine_id = n.machine_id ");
			sb.append("AND m.org_id IS NULL");
			
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();
			if(null != map && map.length > Integer.parseInt(LocalProperties.getProperty("ALERT_FFFWL_NUM"))){
				return true;
			} else {
				return false;
			}
		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}
	
	/** 
	 * 取得所有已设定邮件报警的组织及上级组织下用户的邮箱、端机，根据不同报警类型，确定是否报警
	 * @return HashMap
	 * @throws SQLException
	 */
	public HashMap[] getAlarmInfo() throws SQLException {

		String FUNCTION_NAME = "getAlarmInfo() ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("	SELECT");
			sb.append("		u.org_id,");
			sb.append("		u. name,");
			sb.append("		u.email,");
			sb.append("		m.alert_id,");
			sb.append("		m.alert_name,");
			sb.append("		e.machine_id,");
			sb.append("		e.machine_mark");
			sb.append("	FROM");
			sb.append("		tb_ccb_user u,");
			sb.append("		tb_ccb_machine e,");
			sb.append("		(");
			sb.append("			SELECT");
			sb.append("				t.org_id,");
			sb.append("				n.alert_id,");
			sb.append("				n.alert_name");
			sb.append("			FROM");
			sb.append("				mt_alert_management n,");
			sb.append("				tb_monitor_alert_type t");
			sb.append("			WHERE");
			sb.append("				t.alert_id = n.alert_id");
			sb.append("			AND t.alert_type = '2'");
			sb.append("		) m");
			sb.append("	WHERE");
			sb.append("		u.org_id = m.org_id");
			sb.append("	AND e.org_id = u.org_id");
			sb.append("	AND u.email IS NOT NULL");
			sb.append("	AND u.email <> ''");
			sb.append("	AND (e.status ='0' OR e.status ='2') ");
			sb.append("	ORDER BY");
			sb.append("		u.email,");
			sb.append("		m.alert_id,");
			sb.append("		e.machine_id");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();
			return map;
		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}
	}
	
	/**
	 * 取得端机common value
	 * @param fieldName
	 * @param machineID
	 * @return
	 * @throws SQLException
	 * @throws LocalPropertiesException 
	 */
	public HashMap[] getMaxValue(String fieldName, String machineID) throws SQLException, LocalPropertiesException {

		String FUNCTION_NAME = "getMaxValue ";
		logger.info( FUNCTION_NAME + "start" );

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("	SELECT");
			sb.append("		MAX("+fieldName+") as MAXVAL");
			sb.append("	FROM");
			sb.append("		tb_monitor_common");
			sb.append("	WHERE");
			sb.append("		machine_id = ?");
			sb.append("	AND TIME > CURRENT_TIMESTAMP - INTERVAL '" + LocalProperties.getProperty("ALERT_OPERATE_TIME") + "'");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setInt( 1 , Integer.parseInt(machineID) );

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
		
	/**
	 * 
	 * 远程接管消息存储
	 * @param macIDStd
	 * @param operType
	 * @return
	 * @throws SQLException 
	*/
	public int updateRemoteMenuMessage(String macIDStd, String operType, String content) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_command_management ");
		sb.append("   set status = '1', ");
		if(th.util.StringUtils.isNotBlank(content)){
			sb.append("   command_content ='" + content + "' , ");
		}
		sb.append("       operatetime = current_timestamp ");
		sb.append(" where machine_id in ( ");
		sb.append(macIDStd);
		sb.append("	)");
		sb.append("	  and command_id = ");
		sb.append(operType);
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}
	
	/**
	 * 
	 * 发送消息
	 * @param macIDStd
	 * @param userid 
	 * @param content 
	 * @throws SQLException 
	*/
	public void insertMessageHistory(String macIDStd, String content, String userid) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "INSERT INTO tb_ccb_message(" );
			sb.append( "	msg_id, machine_id, msg_content, operatetime, operator)" );
			sb.append( "VALUES (nextval('seq_tb_ccb_message'), ?, ?, CURRENT_TIMESTAMP, ?)" );


			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1, Long.parseLong(macIDStd) );
			stmt.setString( 2, content );
			stmt.setLong( 3, Long.parseLong(userid) );

			insert();
			commit();
		}
		finally {
			releaseConnection();

		}
	}
	
	/**
	 * 
	 * 更改消息状态
	 * @param macIDStd
	 * @param operType
	 * @return
	 * @throws SQLException 
	*/
	public int doOper(String macIDStd, String operType, String content) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_command_management ");
		sb.append("   set status = 1, ");
		if(StringUtils.isNotBlank(content)){
			sb.append("   command_content = ?, ");
		}
		sb.append("       operatetime = current_timestamp ");
		sb.append(" where machine_id in ( ");
		sb.append(macIDStd);
		sb.append("	)");
		sb.append("	  and command_id = ");
		sb.append(operType);
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			if(StringUtils.isNotBlank(content)){
				stmt.setString(1, content);
			}
			int result = update();
			commit();
			return result;
		} finally  {
			releaseConnection();
		}		
	}

	
	/**
	 * 
	 * 报废
	 * @param mac 
	 * @param machineID
	 * @throws SQLException 
	*/
	public int insertMachineRetirement(String mac, String machineID) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "INSERT INTO tb_machine_retirement(" );
			sb.append( "	retirement_id, mac, machine_id)" );
			sb.append( "VALUES (nextval('seq_tb_machine_retirement'), ?, ?)" );


			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString( 1, mac );
			stmt.setLong( 2 , Long.parseLong(machineID) );
			

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
	 * 机器mac取得
	 * @param mac 
	 * @throws SQLException 
	*/
	public HashMap[] getMachineMac(String machineID) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "SELECT mac FROM tb_ccb_machine WHERE machine_id = ?" );

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong( 1 , Long.parseLong(machineID) );

			HashMap[] map = select();
			return map;
		}
		finally {
			releaseConnection();

		}
	}
	

	/**
	 * 
	 * 机器状态更新
	 * @param machineID 
	 * @throws SQLException 
	*/
	public int updateMachineStatus(String machineID, String status) throws SQLException {
		StringBuilder sb = new StringBuilder();
		sb.append("update tb_ccb_machine ");
		sb.append("   set status = ? ");
		sb.append(" where machine_id = ? ");
		try {
			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString( 1, status );
			stmt.setLong( 2 , Long.parseLong(machineID) );
			
			int result = update();
			commit();
			return result;
		}
		finally {
			releaseConnection();

		}
	}
	
	/**
	 * 
	 * 查询报废的机器
	 * @param mac 
	 * @throws SQLException 
	*/
	public HashMap[] getRetirementMachine(String mac) throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "SELECT * FROM tb_machine_retirement WHERE mac = ?");

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setString( 1, mac );

			HashMap[] map = select();
			return map;
		}
		finally {
			releaseConnection();

		}
	}
	
	/**
	 * 
	 * 查询设备类型
	 * @throws SQLException 
	*/
	public HashMap[] getMacType() throws SQLException {
		try{
			StringBuffer sb = new StringBuffer();
			sb.append( "SELECT * FROM tb_machine_type ");

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();
			return map;
		}
		finally {
			releaseConnection();

		}
	}
	
	public HashMap<String, String> getType() throws SQLException {
		ResultSet rs = null;
		HashMap<String, String> map = new HashMap<String, String>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( "SELECT '9000000000000'||type_id as id,type_name as name FROM tb_machine_type;" );
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					map.put(rs.getString( "NAME" ),rs.getString( "ID" ));
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return map;
	}

	public HashMap<String, String> getMacTypeByMacID() {
		ResultSet rs = null;
		HashMap<String, String> map = new HashMap<String, String>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( "select e.machine_id, t.type_name from tb_machine_type t,tb_machine_environment e where e.os like '%'|| t.os ||'%' and e.machine_kind=t.machine_kind order by machine_id;" );
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					map.put(rs.getString( "MACHINE_ID" ),rs.getString( "TYPE_NAME" ));
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
