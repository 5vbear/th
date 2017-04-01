/**
 * ====================================================================
 *  Copyright(C) 2004 SoFT
 *  All Right Reserved.
 *
 *   @since   2004.2.1
 *   @author  SoFT
 *   $Id: ComMetaDB.java 9645 2011-01-27 13:20:58Z txf $
 * ====================================================================
 */

package th.com.db;

import java.sql.Connection;
import java.sql.SQLException;

/*------------------------------------------------------------------------------
/  クラス名   ： LoginUserDataObject
/-----------------------------------------------------------------------------*/
/**
 * 各機能DBクラスのスーパークラス<br>
 * DBコネクションを保持し、コミット、ロールバック、切断を提供する。
 */
public class ComMetaDB {

    /** DBコネクション */
    protected Connection con;
    /** ログ出力用クラス名 */
    private static String className;

/*------------------------------------------------------------------------------
/  メソッド名 ： ComMetaDB
/-----------------------------------------------------------------------------*/
/**
 * コンストラクタ
 */
    public ComMetaDB() {
        className = "ComMetaDB";
    }

/*------------------------------------------------------------------------------
/  メソッド名 ： dbCommit
/-----------------------------------------------------------------------------*/
/** 
 * DBのコミット処理を行う
 * @throws SQLException DB例外発生時
 */
    public void dbCommit() throws SQLException {
        if(con != null){
        	try{
        		con.commit();
        	}catch(SQLException ex){
        		con.rollback();
        		releaseConnection();
        		throw ex;
        	}
        }
    }

/*------------------------------------------------------------------------------
/  メソッド名 ： dbRollback
/-----------------------------------------------------------------------------*/
/** 
 * DBのロールバック処理を行う
 * @throws SQLException DB例外発生時
 */
    public void dbRollback() throws SQLException {
        if(con != null){
        	try{
        		con.rollback();
        	}catch(SQLException ex){
        		releaseConnection();
        		throw ex;
        	}
        }
    }

/*------------------------------------------------------------------------------
/  メソッド名 ： releaseConnection
/-----------------------------------------------------------------------------*/
/** 
 * DBコネクションのリリース処理を行う
 * @throws SQLException DB例外発生時
 */
    public void releaseConnection() throws SQLException {
    	try{
    		if(con != null) {
    			try {
	                // DBテーブルに対する共有および占有ロック解除
	                con.rollback();
	            } catch ( SQLException e ) {
	                // コネクションオブジェクトがnullではなく、かつ
	                // rollback()の呼び出しにて例外が発生する場合、
	                // AutoCommitの値がtrueであるとし、何もしない。
	            }
	            con.close();
	            con = null;
// 2010/04/20 matsui del start            
	            //Log.debug(className + " DBコネクションclose:releaseConnection");
// 2010/04/20 matsui del end
	        }
        } catch ( SQLException e ) {
            throw e;
        } finally {
            con = null;
        }
    }
/*------------------------------------------------------------------------------
/  メソッド名 ： finalize
/-----------------------------------------------------------------------------*/
/** 
 * DBコネクションのリリースもれを防ぐ
 * @throws Throwable 例外発生時
 */
//    protected void finalize() throws Throwable{
//        if(con != null) {
//            con.close();
//            con = null;
//            Log.debug(className + " DBコネクションclose:finalize");
//        }
//    }
}
