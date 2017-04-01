// ファイル名 : DBConnecter.java
// バージョン : $Id: DBConnecter.java 9645 2011-01-27 13:20:58Z txf $

package th.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * DB接続管理クラス<br>
 * データベースとの接続処理を管理する。
 */
class DBConnecter {

	/** データソース格納ハッシュテーブル */
	private static Hashtable dsTable;

	/** 
	 * DBコネクション取得<br>
	 * 指定されたデータソースからコネクションを取得し、返却する。
	 * @param name データソース名
	 * @return Connection DBコネクション
	 * @exception SQLException DB処理異常時
	 */
	public static Connection getDBConnection( String name )
			throws SQLException {

		Connection con = null;	//DBコネクション
		DataSource ds  = null;	//データソース

		try {
			// データソース取得
			ds = getDataSource( name );

			if( ds != null ) {
				// コネクション取得
				con = ds.getConnection();
				// オートコミットをオフにする
				con.setAutoCommit( false );
                // abort 対策としてロールバックを実行
                con.rollback();
			}
		} catch ( NamingException ex ) {
			throw new SQLException( ex.toString( true ) );
		}

		return con;
	}

	/**
	 * データソース取得
	 * InitialContextからデータソースを取得する
	 * @param String name DataSource名
	 * @return Dataource データソース
	 * @exception NamingException データソース名異常時
	 */
	private static DataSource getDataSource( String name )
			throws NamingException {

		// データソース格納ハッシュテーブルが存在しない場合は生成
		if( dsTable == null ) {
			dsTable = new Hashtable();
		}

		// ハッシュテーブルからデータソース取得
		DataSource ds = ( DataSource ) dsTable.get( name );

		// 存在しない場合はInitialContextとから取得
		if( ds == null ) {
			InitialContext ic = new InitialContext();
			ds = ( DataSource ) ic.lookup( name );
			dsTable.put( name, ds );
		}

		return ds;
	}
}
