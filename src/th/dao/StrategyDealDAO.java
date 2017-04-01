/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.SQLException;
import java.util.HashMap;

import th.entity.StrategyBean;
import th.entity.TaskBean;
import th.entity.UserBean;

/**
 * Descriptions
 *
 * @version 2013-9-11
 * @author PSET
 * @since JDK1.6
 *
 */
public class StrategyDealDAO extends BaseDao {
	
	public StrategyDealDAO(){

	}
	
	public HashMap[] getAllStrategies() throws SQLException {

		String FUNCTION_NAME = "getAllStrategies() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_strategy order by operatetime desc" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public String getObjEndByObjBegin(String objBegin) throws SQLException {

		String FUNCTION_NAME = "getObjEndByObjBegin() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select object_end from tb_ccb_strategy where object_begin = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "object_begin = " + objBegin);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, objBegin );

			String rtnStr = null;
			HashMap[] map = select();
			if(map!=null&&map.length==1){
				rtnStr = (String) map[0].get( "OBJECT_END" );
			}

			return rtnStr;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public StrategyBean getStrategyBeanByStgID(long stgId) throws SQLException {

		String FUNCTION_NAME = "getStrategyBeanByStgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_strategy where stg_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, stgId );

			HashMap[] map = select();
			StrategyBean stb = new StrategyBean();
			
			if(map!=null&&map.length==1){
				stb.setStgId( Long.parseLong( (String) map[0].get("STG_ID")) );
				stb.setStgName((String) map[0].get( "STG_NAME" ));
				stb.setStgType( (String) map[0].get( "STG_TYPE" ) );
				stb.setObjBegin( (String) map[0].get( "OBJECT_BEGIN" ) );
				stb.setObjEnd( (String) map[0].get( "OBJECT_END" ) );
				stb.setOperateTime( (String) map[0].get( "OPERATETIME" ) );
				stb.setOperator( Long.parseLong( (String) map[0].get("OPERATOR")) );
			}
			

			return stb;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public boolean checkStrategyExist(String objBegin) throws SQLException {

		String FUNCTION_NAME = "checkStrategyExist() ";
		logger.info( FUNCTION_NAME + "start" );

		boolean retFlg = false;
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select count(1) from tb_ccb_strategy where object_begin = ? " );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "object_begin = " + objBegin);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, objBegin );

			HashMap[] map = select();
			
			int count = 0;
			if(map!=null&&map.length==1){
				count = Integer.parseInt( (String) map[0].get( "COUNT" ));
				if(count>0){
					retFlg = true;
				}
			}

			return retFlg;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getStrategyRecordByObjBegin(String objBegin) throws SQLException {

		String FUNCTION_NAME = "getStrategyRecordByObjBegin() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_ccb_strategy where object_begin = ? " );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "object_begin = " + objBegin);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, objBegin );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public boolean checkDefRoleExist(String roleName) throws SQLException {

		String FUNCTION_NAME = "checkDefRoleExist() ";
		logger.info( FUNCTION_NAME + "start" );

		boolean retFlg = false;
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select count(1) from tb_ccb_strategy tcs, tb_ccb_role tcr " );
			sb.append( "where tcs.object_end = (tcr.role_id || '') and tcr.role_name = ? " );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "ROLE_NAME = " + roleName);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, roleName );

			HashMap[] map = select();
			
			int count = 0;
			if(map!=null&&map.length==1){
				count = Integer.parseInt( (String) map[0].get( "COUNT" ));
				if(count>0){
					retFlg = true;
				}
			}

			return retFlg;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public boolean checkDefRoleExistByRoleId(String roleId) throws SQLException {

		String FUNCTION_NAME = "checkDefRoleExistByRoleId() ";
		logger.info( FUNCTION_NAME + "start" );

		boolean retFlg = false;
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select count(1) from tb_ccb_strategy where object_end = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "object_end = " + roleId);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, roleId );

			HashMap[] map = select();
			
			int count = 0;
			if(map!=null&&map.length==1){
				count = Integer.parseInt( (String) map[0].get( "COUNT" ));
				if(count>0){
					retFlg = true;
				}
			}

			return retFlg;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public long insertStrategyRecord(StrategyBean stb) throws SQLException {

		String FUNCTION_NAME = "insertStrategyRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long stgId = getSequence( "seq_tb_ccb_strategy" );
		String stgName = stb.getStgName();
		String stgType = stb.getStgType();
		String objBegin = stb.getObjBegin();
		String objEnd = stb.getObjEnd();
		long operator = stb.getOperator();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_ccb_strategy " );
			sb.append( "( stg_id, stg_name, stg_type, object_begin, object_end, operatetime, operator ) " );
			sb.append( "values (?,?,?,?,?,date_trunc( 'second', current_timestamp ),?)"  );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);
			logger.info(FUNCTION_NAME + "stg_name = " + stgName);
			logger.info(FUNCTION_NAME + "stg_type = " + stgType);
			logger.info(FUNCTION_NAME + "object_begin = " + objBegin);
			logger.info(FUNCTION_NAME + "object_end = " + objEnd);
			logger.info(FUNCTION_NAME + "operator = " + operator);
	
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, stgId );
			stmt.setString( 2, stgName );
			stmt.setString( 3 , stgType );
			stmt.setString( 4, objBegin );
			stmt.setString( 5, objEnd );
			stmt.setLong( 6 , operator );

			insert();
			commit();
			
			return stgId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	
	public void updateStrategyRecord(StrategyBean stb) throws SQLException {

		String FUNCTION_NAME = "updateStrategyRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long stgId = stb.getStgId();
		String stgName = stb.getStgName();
		String stgType = stb.getStgType();
		String objBegin = stb.getObjBegin();
		String objEnd = stb.getObjEnd();
		long operator = stb.getOperator();
		
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_ccb_strategy " );
			sb.append( "set stg_name = ?, " );
			sb.append( "stg_type = ?, " );
			sb.append( "object_begin = ?, " );
			sb.append( "object_end = ?, " );
			sb.append( "operatetime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "operator = ? " );
			sb.append( "where stg_id = ?" );


			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "stg_name = " + stgName);
			logger.info(FUNCTION_NAME + "stg_type = " + stgType);
			logger.info(FUNCTION_NAME + "object_begin = " + objBegin);
			logger.info(FUNCTION_NAME + "object_end = " + objEnd);
			logger.info(FUNCTION_NAME + "operator = " + operator);
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);



			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, stgName );
			stmt.setString( 2 , stgType );
			stmt.setString( 3, objBegin );
			stmt.setString( 4, objEnd );			
			stmt.setLong( 5 , operator );
			stmt.setLong( 6, stgId );

			update();
			commit();
			
		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	
	public void deleteStrategyByStgID(long stgId) throws SQLException {

		String FUNCTION_NAME = "deleteStrategyByStgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_ccb_strategy where stg_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, stgId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public HashMap[] getAllTasks() throws SQLException {

		String FUNCTION_NAME = "getAllTasks() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_strategy_management order by modifytime desc" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getTasksByUserId(long userId) throws SQLException {

		String FUNCTION_NAME = "getTasksByUserId() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * " );
			sb.append( "from tb_strategy_management " );
			sb.append( "where operator = ? " );
			sb.append( "or modifyuser = ? " );
			sb.append( "order by modifytime desc" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "operator = " + userId);
			logger.info(FUNCTION_NAME + "modifyuser = " + userId);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, userId );
			stmt.setLong( 2, userId );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getTasksBySearchCondition(TaskBean tb) throws SQLException {

		String FUNCTION_NAME = "getTasksBySearchCondition() ";
		logger.info( FUNCTION_NAME + "start" );
		
		// 搜索条件取得
		String conStgName = tb.getStgName();
		String conSendInternal = tb.getSendInternal();
		String conSendType = tb.getSendType();
		long conOperator = tb.getOperator();
		long conModifyUser = tb.getModifyuser();
		

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * " );
			sb.append( "from tb_strategy_management " );
			sb.append( "where (operator = " + conOperator + " " );
			sb.append( "or modifyuser = " + conModifyUser + " ) " );
			if(!"".equals( conStgName )){
				sb.append( "and stg_name = '" + conStgName + "' " );
			}
			if(!"".equals( conSendInternal )){
				sb.append( "and send_internal = '" + conSendInternal + "' " );
			}
			if(!"".equals( conSendType )){
				sb.append( "and send_type = '" + conSendType + "' " );
			}
			sb.append( "order by modifytime desc" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "operator = " + conOperator);
			logger.info(FUNCTION_NAME + "modifyuser = " + conModifyUser);
			logger.info(FUNCTION_NAME + "stg_name = " + conStgName);
			logger.info(FUNCTION_NAME + "send_internal = " + conSendInternal);
			logger.info(FUNCTION_NAME + "send_type = " + conSendType);
			

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public TaskBean getTaskBeanByStgId(long stgId) throws SQLException {

		String FUNCTION_NAME = "getTaskBeanByStgId() ";
		logger.info( FUNCTION_NAME + "start" );
		
		TaskBean taskBean = null;

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from tb_strategy_management where stg_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, stgId );

			HashMap[] map = select();
			
			if(map!=null&&map.length==1){
				taskBean = new TaskBean();
				taskBean.setStgId( Long.parseLong( (String)map[0].get( "STG_ID" ) ) );
				taskBean.setStgName( (String)map[0].get( "STG_NAME" ) );
				taskBean.setStgDesp( (String)map[0].get( "STG_DESCRIPTION" ) );
				taskBean.setSendInternal( (String)map[0].get( "SEND_INTERNAL" ) );
				String sendTimeHour = (String) map[0].get( "SEND_TIME_HOUR" );
				if(sendTimeHour!=null&&!"".equals( sendTimeHour )){
					taskBean.setSendTimeHour( Integer.parseInt( sendTimeHour ) );
				}
				String sendTimeMinute = (String) map[0].get( "SEND_TIME_MINUTE" );
				if(sendTimeMinute!=null&&!"".equals( sendTimeMinute )){
					taskBean.setSendTimeMinute( Integer.parseInt( sendTimeMinute ) );
				}
				String sendTimeSecond = (String) map[0].get( "SEND_TIME_SECOND" );
				if(sendTimeSecond!=null&&!"".equals( sendTimeSecond )){
					taskBean.setSendTimeSecond( Integer.parseInt( sendTimeSecond ) );
				}
				taskBean.setSendType( (String)map[0].get( "SEND_TYPE" ) );
				taskBean.setDeliverTerminal( (String)map[0].get( "DELIVER_TERMINAL" ) );
				taskBean.setDeliverRoleList( (String)map[0].get( "DELIVER_ROLE_LIST" ) );
				taskBean.setReportNameList( (String)map[0].get( "REPORT_NAME_LIST" ) );
				taskBean.setStatus( (String)map[0].get( "STATUS" ) );
				taskBean.setOperateTime( (String)map[0].get( "OPERATETIME" ) );
				taskBean.setOperator( Long.parseLong( (String)map[0].get( "OPERATOR" ) ) );
				taskBean.setTaskStartTime( (String)map[0].get( "TASK_START_TIME" ) );
				taskBean.setTaskPreTime( (String)map[0].get( "TASK_PRE_TIME" ) );
				taskBean.setTaskNextTime( (String)map[0].get( "TASK_NEXT_TIME" ) );
				taskBean.setModifyTime( (String)map[0].get( "MODIFYTIME" ) );
				taskBean.setModifyuser( Long.parseLong( (String)map[0].get( "MODIFYUSER" ) ) );
				
			}

			return taskBean;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public long insertTaskRecord(TaskBean tb) throws SQLException {

		String FUNCTION_NAME = "insertTaskRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long stgId = getSequence( "seq_tb_strategy_management" );
		String stgName = tb.getStgName();
		String stgDesp = tb.getStgDesp();
		String sendInternal = tb.getSendInternal();
		int sendTimeHour = tb.getSendTimeHour();
		int sendTimeMinute = tb.getSendTimeMinute();
		int sendTimeSecond = tb.getSendTimeSecond();
		String sendType = tb.getSendType();
		String deliverTerminal = tb.getDeliverTerminal();
		String deleverRoleList = tb.getDeliverRoleList();
		String reportNameList = tb.getReportNameList();
		String status = tb.getStatus();
		long operator = tb.getOperator();
		String taskStartTime = tb.getTaskStartTime();
		String taskPreTime = tb.getTaskPreTime();
		String taskNextTime = tb.getTaskNextTime();
		long modifyUser = tb.getModifyuser();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into tb_strategy_management " );
			sb.append( "( stg_id, stg_name, stg_description, send_internal, send_time_hour, send_time_minute, " );
			sb.append( "send_time_second, send_type, deliver_terminal, deliver_role_list, report_name_list, ");
			sb.append( "status, operatetime, operator, task_start_time, task_pre_time, task_next_time, modifytime, modifyuser )" );
			sb.append( "values (?,?,?,?,?,?,?,?,?,?,?,?,date_trunc( 'second', current_timestamp ),?," );
			if(taskStartTime==null||"".equals(taskStartTime)){
				sb.append( "null," );
			}else{
				sb.append( "'" + taskStartTime + "'," );
			}
			if(taskPreTime==null||"".equals(taskPreTime)){
				sb.append( "null," );
			}else{
				sb.append( "'" + taskPreTime + "'," );
			}
			if(taskNextTime==null||"".equals(taskNextTime)){
				sb.append( "null," );
			}else{
				sb.append( "'" + taskNextTime + "'," );
			}
			sb.append( "date_trunc( 'second', current_timestamp ),?)" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);
			logger.info(FUNCTION_NAME + "stg_name = " + stgName);
			logger.info(FUNCTION_NAME + "stg_description = " + stgDesp);
			logger.info(FUNCTION_NAME + "send_internal = " + sendInternal);
			logger.info(FUNCTION_NAME + "send_time_hour = " + sendTimeHour);
			logger.info(FUNCTION_NAME + "send_time_minute = " + sendTimeMinute);
			logger.info(FUNCTION_NAME + "send_time_second = " + sendTimeSecond);
			logger.info(FUNCTION_NAME + "send_type = " + sendType);
			logger.info(FUNCTION_NAME + "deliver_terminal = " + deliverTerminal);
			logger.info(FUNCTION_NAME + "deliver_role_list = " + deleverRoleList);
			logger.info(FUNCTION_NAME + "report_name_list = " + reportNameList);
			logger.info(FUNCTION_NAME + "status = " + status);
			logger.info(FUNCTION_NAME + "operator = " + operator);
			logger.info(FUNCTION_NAME + "task_start_time = " + taskStartTime);
			logger.info(FUNCTION_NAME + "task_pre_time = " + taskPreTime);
			logger.info(FUNCTION_NAME + "task_next_time = " + taskNextTime);
			logger.info(FUNCTION_NAME + "modifyUser = " + modifyUser);
	
			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			
			int count = 1;
			stmt.setLong( count++, stgId );
			stmt.setString( count++, stgName );
			stmt.setString( count++, stgDesp );
			stmt.setString( count++, sendInternal );
			stmt.setInt( count++, sendTimeHour );
			stmt.setInt( count++, sendTimeMinute );
			stmt.setInt( count++, sendTimeSecond );
			stmt.setString( count++, sendType );
			stmt.setString( count++, deliverTerminal );
			stmt.setString( count++, deleverRoleList );
			stmt.setString( count++, reportNameList );
			stmt.setString( count++, status );
			stmt.setLong( count++, operator );
			stmt.setLong( count++, modifyUser );

			insert();
			commit();
			
			return stgId;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void updateTaskRecord(TaskBean tb) throws SQLException {

		String FUNCTION_NAME = "updateTaskRecord() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long stgId = tb.getStgId();
		String stgName = tb.getStgName();
		String stgDesp = tb.getStgDesp();
		String sendInternal = tb.getSendInternal();
		int sendTimeHour = tb.getSendTimeHour();
		int sendTimeMinute = tb.getSendTimeMinute();
		String sendType = tb.getSendType();
		String deliverTerminal = tb.getDeliverTerminal();
		String deliverRoleList = tb.getDeliverRoleList();
		String reportNameList = tb.getReportNameList();
		long modifyUser = tb.getModifyuser();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_strategy_management " );
			sb.append( "set stg_name = ?, " );
			sb.append( "stg_description = ?, " );
			sb.append( "send_internal = ?, " );
			sb.append( "send_time_hour = ?, " );
			sb.append( "send_time_minute = ?, " );
			sb.append( "send_type = ?, " );
			sb.append( "deliver_terminal = ?, " );
			sb.append( "deliver_role_list = ?, " );
			sb.append( "report_name_list = ?, " );
			sb.append( "modifytime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "modifyUser = ? " );
			sb.append( "where stg_id = ?" );
			

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "stg_name = " + stgName);
			logger.info(FUNCTION_NAME + "stg_description = " + stgDesp);
			logger.info(FUNCTION_NAME + "send_internal = " + sendInternal);
			logger.info(FUNCTION_NAME + "send_time_hour = " + sendTimeHour);
			logger.info(FUNCTION_NAME + "send_time_minute = " + sendTimeMinute);
			logger.info(FUNCTION_NAME + "send_type = " + sendType);
			logger.info(FUNCTION_NAME + "deliver_terminal = " + deliverTerminal);
			logger.info(FUNCTION_NAME + "deliver_role_list = " + deliverRoleList);
			logger.info(FUNCTION_NAME + "report_name_list = " + reportNameList);
			logger.info(FUNCTION_NAME + "modifyUser = " + modifyUser);
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);



			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			
			int count = 1;
			stmt.setString( count++, stgName );
			stmt.setString( count++, stgDesp );
			stmt.setString( count++, sendInternal );
			stmt.setInt( count++, sendTimeHour );			
			stmt.setInt( count++, sendTimeMinute );
			stmt.setString( count++, sendType );
			stmt.setString( count++, deliverTerminal );
			stmt.setString( count++, deliverRoleList );
			stmt.setString( count++, reportNameList );
			stmt.setLong( count++, modifyUser );
			stmt.setLong( count++, stgId );

			update();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void enableTaskStatus(TaskBean tb) throws SQLException {

		String FUNCTION_NAME = "enableTaskStatus() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long stgId = tb.getStgId();
		String status = tb.getStatus();
		long modifyUser = tb.getModifyuser();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_strategy_management " );
			sb.append( "set status = ?, " );
			sb.append( "task_start_time = date_trunc( 'second', current_timestamp ), " );
			sb.append( "modifytime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "modifyUser = ? " );
			sb.append( "where stg_id = ?" );
			

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "status = " + status);
			logger.info(FUNCTION_NAME + "modifyUser = " + modifyUser);
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			
			int count = 1;
			stmt.setString( count++, status );
			stmt.setLong( count++, modifyUser );
			stmt.setLong( count++, stgId );

			update();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void disableTaskStatus(TaskBean tb) throws SQLException {

		String FUNCTION_NAME = "disableTaskStatus() ";
		logger.info( FUNCTION_NAME + "start" );
		
		long stgId = tb.getStgId();
		String status = tb.getStatus();
		long modifyUser = tb.getModifyuser();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update tb_strategy_management " );
			sb.append( "set status = ?, " );
			sb.append( "modifytime = date_trunc( 'second', current_timestamp ), " );
			sb.append( "modifyUser = ? " );
			sb.append( "where stg_id = ?" );
			

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "status = " + status);
			logger.info(FUNCTION_NAME + "modifyUser = " + modifyUser);
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			
			int count = 1;
			stmt.setString( count++, status );
			stmt.setLong( count++, modifyUser );
			stmt.setLong( count++, stgId );

			update();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void deleteTaskRecordByStgID(long stgId) throws SQLException {

		String FUNCTION_NAME = "deleteTaskRecordByStgID() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "delete from tb_strategy_management where stg_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "stg_id = " + stgId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setLong( 1, stgId );

			delete();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	public HashMap[] getReportNameListByMarkIdStr(String sqlCond) throws SQLException {

		String FUNCTION_NAME = "getReportNameListByMarkIdStr() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			if(sqlCond==null||"".equals( sqlCond )){
				return null;
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append( "select * from mt_ccb_report where mark_id in " );
			sb.append( sqlCond );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public HashMap[] getAllReportNameList() throws SQLException {

		String FUNCTION_NAME = "getAllReportNameList() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from mt_ccb_report " );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}

}
