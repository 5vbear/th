
package th.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;


public class DBAccess {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	/** DBコネクション */
	protected Connection con = null;
	/** SQLStatement */
	protected PreparedStatement stmt = null;

	/**
	 * コンストラクタ
	 */
	public DBAccess() {

	}

	/**
	 * DBの接続処理を行います。
	 * @exception SQLException DB接続に失敗した場合
	 */
	public void connection() throws SQLException {

		final String FUNCTION_NAME = "connection() ";

		logger.debug( FUNCTION_NAME + "start" );
		if( con != null ) {
			return;
		}

		String dataSourceName = null;

		try {
			// データソース名取得
			dataSourceName = LocalProperties.getProperty( "USR_DB_SOURCE_NAME" );
		} catch ( LocalPropertiesException ex ) {
			ex.printStackTrace();
			logger.error( "get DataSourceName error" , ex );
			throw new SQLException( ex.toString());
		}
		logger.debug( FUNCTION_NAME + "DataSourceName = " + dataSourceName );

		// DBコネクション取得
		con = DBConnecter.getDBConnection( dataSourceName );
		logger.debug( FUNCTION_NAME + "connection succeeded" );
	}

	/**
	 * DBのコミット処理を行います。
	 * @exception SQLException DB例外発生時
	 */
	public void commit() throws SQLException {

		final String FUNCTION_NAME = "commit() ";

		logger.debug( FUNCTION_NAME + "start" );

		if( con != null ) {
			try {
				con.commit();
				logger.debug( FUNCTION_NAME + "commit succeeded" );
			} catch ( SQLException ex ) {
				logger.error( FUNCTION_NAME + "commit error..." , ex );
				// ロールバック
				con.rollback();
				// release処理
				releaseConnection();
				throw ex;
			}
		}
		logger.debug( FUNCTION_NAME + "end" );
	}

	/**
	 * DBのロールバック処理を行います。
	 * @exception SQLException DB例外発生時
	 */
	public void rollback() throws SQLException {

		final String FUNCTION_NAME = "rollback() ";

		logger.debug( FUNCTION_NAME + "start" );

		if( con != null ) {
			try {
				con.rollback();
				logger.debug( FUNCTION_NAME + "rollback succeeded" );
			} catch ( SQLException ex ) {
				logger.error( FUNCTION_NAME + "rollback error..." , ex );
				// release処理
				releaseConnection();
				throw ex;
			}
		}
		logger.debug( FUNCTION_NAME + "end" );
	}

	/**
	 * DBコネクションのリリース処理を行います。
	 * @exception SQLException DB例外発生時
	 */
	public void releaseConnection() throws SQLException {

		final String FUNCTION_NAME = "releaseConnection() ";

		logger.debug( FUNCTION_NAME + "start" );

		SQLException ex = null;

		try {
			// Statement解放
			if( stmt != null ) {
				stmt.close();
				logger.debug( FUNCTION_NAME + "releaseStatement succeeded" );
			}
		} catch ( SQLException e ) {
			logger.error( FUNCTION_NAME + "releaseStatement error reason = " + e.toString() );
			ex = e;
		} finally {
			stmt = null;
		}

		try {
			// Connection解放
			if( con != null ) {
                try {
                    // DBテーブルに対する共有および占有ロック解除
                    con.rollback();
                } catch ( SQLException e ) {
                    // コネクションオブジェクトがnullではなく、かつ
                    // rollback()の呼び出しにて例外が発生する場合、
                    // AutoCommitの値がtrueであるとし、何もしない。
                }
				con.close();
				logger.debug( FUNCTION_NAME + "releaseConnection succeeded" );
			}
		} catch ( SQLException e ) {
			logger.error( FUNCTION_NAME + "releaseConnection error reason = " + e.toString() );
			ex = e;
		} finally {
			con = null;
		}

		// 例外が発生した場合、スローする
		if( ex != null ) {
			throw ex;
		}
		logger.debug( FUNCTION_NAME + "end" );
	}

	/**
	 * データの登録処理を行います。
	 * @return int 登録した行数
	 * @exception SQLException DB例外発生時
	 */
	protected int insert() throws SQLException {

		final String FUNCTION_NAME = "insert() ";

		logger.debug( FUNCTION_NAME + "start" );

		// 接続されていない場合
		if( con == null || stmt == null ) {
			return 0;
		}

		try {
			// SQL実行
			return stmt.executeUpdate();
		} catch ( SQLException ex ) {
			logger.error( FUNCTION_NAME + "SQL insert execution failure..." , ex );
			// ロールバック
			rollback();
			// release処理
			releaseConnection();

			throw ex;
		} finally {
			// Statement開放
// 20100729 ModelUpdateData ebihara mod S
//			stmt.close();
			if( stmt != null ) stmt.close();
// 20100729 ModelUpdateData ebihara mod E
			stmt = null;
			logger.debug( FUNCTION_NAME + "end" );
		}
	}
	
	/**
	 * データの登録処理を行います。
	 * @return int 登録した行数
	 * @exception SQLException DB例外発生時
	 */
	protected int[] insertBatch() throws SQLException {

		final String FUNCTION_NAME = "insertBatch() ";

		logger.debug( FUNCTION_NAME + "start" );

		// 接続されていない場合
		if( con == null || stmt == null ) {
			return new int[] {0};
		}

		try {
			// SQL実行
			return stmt.executeBatch();
		} catch ( SQLException ex ) {
			logger.error( FUNCTION_NAME + "SQL insert batch execution failure..." , ex );
			// ロールバック
			rollback();
			// release処理
			releaseConnection();

			throw ex;
		} finally {
			if( stmt != null ) stmt.close();
			stmt = null;
			logger.debug( FUNCTION_NAME + "end" );
		}
	}

	/**
	 * データの更新処理を行います。
	 * @return int 更新した行数
	 * @exception SQLException DB例外発生時
	 */
	protected int update() throws SQLException {

		final String FUNCTION_NAME = "update() ";

		logger.debug( FUNCTION_NAME + "start" );

		// 接続されていない場合
		if( con == null || stmt == null ) {
			return 0;
		}

		try {
			// SQL実行
			return stmt.executeUpdate();
		} catch ( SQLException ex ) {
			logger.error( FUNCTION_NAME + "SQL update execution failure..." , ex );
			// ロールバック
			rollback();
			// release処理
			releaseConnection();

			throw ex;
		} finally {
			// Statement開放
// 20100729 ModelUpdateData ebihara mod S
//			stmt.close();
			if( stmt != null ) stmt.close();
// 20100729 ModelUpdateData ebihara mod E
			stmt = null;
			logger.debug( FUNCTION_NAME + "end" );
		}
	}

	/**
	 * データの削除処理を行います。
	 * @return int 削除した行数
	 * @exception SQLException DB例外発生時
	 */
	protected int delete() throws SQLException {

		final String FUNCTION_NAME = "delete() ";

		logger.debug( FUNCTION_NAME + "start" );

		// 接続されていない場合
		if( con == null || stmt == null ) {
			return 0;
		}

		try {
			// SQL実行
			return stmt.executeUpdate();
		} catch ( SQLException ex ) {
			logger.error( FUNCTION_NAME + "SQL delete execution failure..." , ex );
			// ロールバック
			rollback();
			// release処理
			releaseConnection();

			throw ex;
		} finally {
			// Statement開放
// 20100729 ModelUpdateData ebihara mod S
//			stmt.close();
			if( stmt != null ) stmt.close();
// 20100729 ModelUpdateData ebihara mod E
			stmt = null;
			logger.debug( FUNCTION_NAME + "end" );
		}
	}


	protected HashMap[] select(boolean isToUpperCase) throws SQLException {
		
		final String FUNCTION_NAME = "select() ";
		
		logger.debug( FUNCTION_NAME + "start" );
		
		// 接続されていない場合
		if( con == null || stmt == null ) {
			return null;
		}
		
		// 検索結果
		ResultSet rSet = null;
		
		try {
			// SQL実行
			rSet = this.stmt.executeQuery();
			// テーブルに関する情報を取得
			ResultSetMetaData rsmd = rSet.getMetaData();
			Vector tmp = new Vector();
			
			while( rSet.next()) {
				HashMap map = new HashMap();
				// 検索結果をマッピングする
				for( int j = 0; j < rsmd.getColumnCount(); j++ ) {
					String key = rsmd.getColumnName( j + 1 );
					if(isToUpperCase)
						key = key.toUpperCase();
					Object val = rSet.getObject( key );
					
					// nullでない場合、Stringにキャストする
					if( val != null ) {
						val = val.toString().trim();
					}
					// キーと値をマッピングする
					map.put( key, val );
				}
				tmp.add( map );
			}
			
			HashMap[] map = new HashMap[ tmp.size() ];
			for ( int i = 0; i < tmp.size(); i ++ ) {
				map[ i ] = ( HashMap ) tmp.get( i );
			}
			return map;
		} catch ( SQLException ex ) {
			logger.error( FUNCTION_NAME + "SQL select execution failure..." , ex );
			// ロールバック
			rollback();
			// release処理
			releaseConnection();
			throw ex;
		} finally {
			// ResultSet解放
			if( rSet != null ) rSet.close();
			rSet = null;
			// Statement解放
			if( stmt != null ) stmt.close();
			stmt = null;
			logger.debug( FUNCTION_NAME + "end" );
		}
	}
	protected HashMap[] select() throws SQLException {
		return select(true);
	}

	/**
	 * シーケンス番号を取得します。
	 * 
	 * @param sequenceName シーケンス名称
	 * @return シーケンス番号
	 * @throws SQLException DB異常時
	 */
	protected long getSequence( String sequenceName )
		throws SQLException {

		final String FUNCTION_NAME = "getSequence() ";

		logger.debug( FUNCTION_NAME + "start" );

		// シーケンス取得SQL生成
		StringBuffer sb = new StringBuffer();
		sb.append( "select nextval( '" );
		sb.append( sequenceName );
		sb.append( "' )" );

		logger.info( FUNCTION_NAME + "sql = " + sb.toString() );
		logger.info( FUNCTION_NAME + "sequenceName = " + sequenceName );

		if( con == null ) connection();

		// SQL文をセット
		stmt = con.prepareStatement( sb.toString() );
		// パラメータ値をクリア
		stmt.clearParameters();

		HashMap[] map = select();
		// release処理
		releaseConnection();

		logger.debug( FUNCTION_NAME + "end" );

		return Long.parseLong( (String)map[0].get( "NEXTVAL" ) );

	}

}
