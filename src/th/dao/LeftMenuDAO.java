/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.PropertyConfigurator;

import th.db.DBAccess;

/**
 * Descriptions
 *
 * @version 2013-8-8
 * @author PSET
 * @since JDK1.6
 *
 */
public class LeftMenuDAO extends DBAccess {
	

	
	/**
	 * @throws SQLException
	 */
	public LeftMenuDAO() throws SQLException {
		PropertyConfigurator.configure( "web/WEB-INF/conf/log4j.properties" );
	}
	
	public HashMap[] getMeunList() throws SQLException {

		String FUNCTION_NAME = "getMeunList() ";
		logger.info( FUNCTION_NAME + "start" );

		StringBuffer sb = new StringBuffer();
		sb.append("select *");
		sb.append(" from ");
		sb.append("tb_system_right");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());

		try{
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
