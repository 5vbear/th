/**
 * ====================================================================
 *  Copyright(C) 2004 SoFT
 *  All Right Reserved.
 *
 *   @since   2004.2.1
 *   @author  SoFT
 *   $Id: DBConnecter.java 9645 2011-01-27 13:20:58Z txf $
 * ====================================================================
 */

package th.com.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/*------------------------------------------------------------------------------
 /  クラス名   ： DBConnecter
 /-----------------------------------------------------------------------------*/
/**
 * DB接続管理クラス<br>
 * データベースとの接続処理を管理する。
 */
public class DBConnecter {

	/** データソース格納ハッシュテーブル */
	private static Hashtable dsTable;

	/*------------------------------------------------------------------------------
	 /  メソッド名 ： getDBConnection
	 /-----------------------------------------------------------------------------*/
	/**
	 * DBコネクション取得<br>
	 * 指定されたデータソースからコネクションを取得し、返却する。
	 * 
	 * @param name
	 *            データソース名
	 * @return Connection DBコネクション
	 * @throws SQLException
	 *             DB処理異常時
	 * @throws NamingException
	 *             データソース名異常時
	 */
	public static Connection getDBConnection( String name ) throws SQLException, NamingException {

		Connection con = null; // DBコネクション
		DataSource ds = null; // データソース

		// データソースを取得する
		ds = getDataSource( name );

// 2010/04/20 matsui del start
		// Log.debug("name中身"+ name);
		// Log.debug("dsの中身"+ ds);
// 2010/04/20 matsui del end

		if ( ds != null ) {
			// コネクション取得
			con = ds.getConnection();
			// オートコミットをオフにする
			con.setAutoCommit( false );

			if ( con.getAutoCommit() == false ) {
				// abort対策としてロールバックを実行
				con.rollback();
			}
		}

		// DBコネクション返却
		return con;
	}

	/**
	 * データソース取得 InitialContextからデータソースを取得する
	 * 
	 * @param String
	 *            name DataSource名
	 * @return Dataource データソース
	 * @throws NamingException
	 *             データソース名異常時
	 */
	private static DataSource getDataSource( String name ) throws NamingException {

		// データソース格納ハッシュテーブルが存在しない場合は生成
		if ( dsTable == null ) {
			dsTable = new Hashtable();
		}

		// ハッシュテーブルからデータソース取得
		DataSource ds = (DataSource) dsTable.get( name );
		// 存在しない場合はInitialContextとから取得
		if ( ds == null ) {
			InitialContext ic = new InitialContext();
			ds = (DataSource) ic.lookup( name );
			// ハッシュテーブルにデータソースを設定
			dsTable.put( name, ds );
		}

		// 返却
		return ds;
	}

}
