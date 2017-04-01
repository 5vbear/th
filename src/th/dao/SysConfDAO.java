/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.SQLException;
import java.util.HashMap;

import th.entity.SysServBean;
import th.entity.OrganizationBean;

/**
 * Descriptions
 *
 * @version 2013-9-7
 * @author PSET
 * @since JDK1.6
 *
 */
public class SysConfDAO extends BaseDao {

	public SysConfDAO(){

	}
	
	public SysServBean getSysConfInfo() throws SQLException {

		String FUNCTION_NAME = "getSysConfInfo() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select * from mt_sys_server_config");

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
			
			SysServBean ssb = new SysServBean();
			
			if(map!=null&&map.length==1){
				
				ssb.setConfId( Integer.parseInt((String) map[0].get( "CONF_ID" )) );
				ssb.setSmtpHost( (String) map[0].get( "SMTP_HOST" ) );
				ssb.setSmtpPort( Long.parseLong((String) map[0].get( "SMTP_PORT" )) );
				ssb.setMailFrom( (String) map[0].get( "MAIL_FROM" ) );
				ssb.setMailAuthNick( (String) map[0].get( "MAIL_AUTH_NICKNAME" ) );
				ssb.setMailAuthPass( (String) map[0].get( "MAIL_AUTH_PASSWORD" ) );
				ssb.setMsgGateURL( (String) map[0].get( "MESSAGE_GATE_URL" ) );
				ssb.setGateAuthNick( (String) map[0].get( "GATE_AUTH_NICKNAME" ) );
				ssb.setGateAuthPass( (String) map[0].get( "GATE_AUTH_PASSWORD" ) );
				ssb.setSerStatus( (String) map[0].get( "MAIL_STATUS" ) );
		
			}

			return ssb;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public int checkExists() throws SQLException {

		String FUNCTION_NAME = "checkExists() ";
		logger.info( FUNCTION_NAME + "start" );

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "select count(1) from mt_sys_server_config");

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
			
			int result = 0;
			
			if(map!=null&&map.length==1){
				result = Integer.parseInt( (String) map[0].get( "COUNT" ) );
			}

			return result;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void insertMailServInfo(SysServBean msb) throws SQLException {

		String FUNCTION_NAME = "insertMailServInfo() ";
		logger.info( FUNCTION_NAME + "start" );
		
		int confId = msb.getConfId();
		String selSmtpHost = msb.getSmtpHost();
		long selSmtpPort = msb.getSmtpPort();
		String selMailFrom = msb.getMailFrom();
		String selMailAuthNick = msb.getMailAuthNick();
		String selMailAuthPass = msb.getMailAuthPass();
		String selSerStatus = msb.getSerStatus();

		
		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into mt_sys_server_config ");
			sb.append( "( conf_id, smtp_host, smtp_port, mail_from, mail_auth_nickname, mail_auth_password, mail_status, message_gate_url, gate_auth_nickname, gate_auth_password ) " );
			sb.append( "values (?,?,?,?,?,?,?,'','','')");

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "conf_id = " + confId);
			logger.info(FUNCTION_NAME + "smtp_host = " + selSmtpHost);
			logger.info(FUNCTION_NAME + "smtp_port = " + selSmtpPort);
			logger.info(FUNCTION_NAME + "mail_from = " + selMailFrom);
			logger.info(FUNCTION_NAME + "mail_auth_nickname = " + selMailAuthNick);
			logger.info(FUNCTION_NAME + "mail_auth_password = " + selMailAuthPass);
			logger.info(FUNCTION_NAME + "mail_status = " + selSerStatus);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			int count = 1;
			stmt.setInt( count++, confId );
			stmt.setString( count++, selSmtpHost );
			stmt.setLong( count++, selSmtpPort );
			stmt.setString( count++, selMailFrom );
			stmt.setString( count++, selMailAuthNick );
			stmt.setString( count++, selMailAuthPass );
			stmt.setString( count++, selSerStatus );

			insert();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void updateMailServInfo(SysServBean msb) throws SQLException {

		String FUNCTION_NAME = "updateMailServInfo() ";
		logger.info( FUNCTION_NAME + "start" );
		
		int confId = msb.getConfId();
		String selSmtpHost = msb.getSmtpHost();
		long selSmtpPort = msb.getSmtpPort();
		String selMailFrom = msb.getMailFrom();
		String selMailAuthNick = msb.getMailAuthNick();
		String selMailAuthPass = msb.getMailAuthPass();
		String selSerStatus = msb.getSerStatus();

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update mt_sys_server_config " );
			sb.append( "set smtp_host = ?, " );
			sb.append( "smtp_port = ?, " );
			sb.append( "mail_from = ?, " );
			sb.append( "mail_auth_nickname = ?, " );
			sb.append( "mail_auth_password = ?, " );
			sb.append( "mail_status = ? " );
			sb.append( "where conf_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "smtp_host = " + selSmtpHost);
			logger.info(FUNCTION_NAME + "smtp_port = " + selSmtpPort);
			logger.info(FUNCTION_NAME + "mail_from = " + selMailFrom);
			logger.info(FUNCTION_NAME + "mail_auth_nickname = " + selMailAuthNick);
			logger.info(FUNCTION_NAME + "mail_auth_password = " + selMailAuthPass);
			logger.info(FUNCTION_NAME + "mail_status = " + selSerStatus);
			logger.info(FUNCTION_NAME + "conf_id = " + confId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			int count = 1;
			stmt.setString( count++, selSmtpHost );
			stmt.setLong( count++, selSmtpPort );
			stmt.setString( count++, selMailFrom );
			stmt.setString( count++, selMailAuthNick );
			stmt.setString( count++, selMailAuthPass );
			stmt.setString( count++, selSerStatus );
			stmt.setInt( count++, confId );
	
			update();
			commit();
			

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void insertSysServInfo(SysServBean ssb) throws SQLException {

		String FUNCTION_NAME = "insertSysServInfo() ";
		logger.info( FUNCTION_NAME + "start" );
		
		int confId = ssb.getConfId();
		String selMsgGateURL = ssb.getMsgGateURL();
		String selGateAuthNick = ssb.getGateAuthNick();
		String selGateAuthPass = ssb.getGateAuthPass();	

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "insert into mt_sys_server_config ");
			sb.append( "( conf_id, smtp_host, smtp_port, mail_from, mail_auth_nickname, mail_auth_password, mail_status, message_gate_url, gate_auth_nickname, gate_auth_password ) " );
			sb.append( "values (?,'',0,'','','','',?,?,?)");

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "conf_id = " + confId);
			logger.info(FUNCTION_NAME + "message_gate_url = " + selMsgGateURL);
			logger.info(FUNCTION_NAME + "gate_auth_nickname = " + selGateAuthNick);
			logger.info(FUNCTION_NAME + "gate_auth_password = " + selGateAuthPass);

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setInt( 1, confId );
			stmt.setString( 2, selMsgGateURL );
			stmt.setString( 3, selGateAuthNick );
			stmt.setString( 4, selGateAuthPass );

			insert();
			commit();

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
	public void updateSysServInfo(SysServBean ssb) throws SQLException {

		String FUNCTION_NAME = "updateSysServInfo() ";
		logger.info( FUNCTION_NAME + "start" );
		
		int confId = ssb.getConfId();
		String selMsgGateURL = ssb.getMsgGateURL();
		String selGateAuthNick = ssb.getGateAuthNick();
		String selGateAuthPass = ssb.getGateAuthPass();	

		try{

			StringBuffer sb = new StringBuffer();
			sb.append( "update mt_sys_server_config " );
			sb.append( "set message_gate_url = ?, " );
			sb.append( "gate_auth_nickname = ?, " );
			sb.append( "gate_auth_password = ? " );
			sb.append( "where conf_id = ?" );

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());
			logger.info(FUNCTION_NAME + "message_gate_url = " + selMsgGateURL);
			logger.info(FUNCTION_NAME + "gate_auth_nickname = " + selGateAuthNick);
			logger.info(FUNCTION_NAME + "gate_auth_password = " + selGateAuthPass);
			logger.info(FUNCTION_NAME + "conf_id = " + confId);


			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, selMsgGateURL );
			stmt.setString( 2, selGateAuthNick );
			stmt.setString( 3, selGateAuthPass );
			stmt.setInt( 4, confId );
	
			update();
			commit();
			

		}
		finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
	
}
