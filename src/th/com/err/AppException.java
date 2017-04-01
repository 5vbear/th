/**
 * ====================================================================
 *  Copyright(C) 2004 SoFT
 *  All Right Reserved.
 *
 *   @since   2004.2.1
 *   @author  SoFT
 *   $Id: AppException.java 9645 2011-01-27 13:20:58Z txf $
 * ====================================================================
 */

package th.com.err;

/**
 * アプリケーション例外クラス<br>
 * アプリケーション的な例外が発生し場合に使用する。
 * 作成時に指定したメッセージを保持する。ITT用アプリケーション例外クラス。
 */
public class AppException extends Exception {

    /** 例外メッセージ */
    public String message;

    /**
     * AppExceptionコンストラクタ
     * @param message 例外メッセージ
     */
    public AppException(String message){
        this.message = message;
    }

    /**
     * メッセージ取得<br>
     * ThrowableクラスのgetMessage()をオーバーライドしたもの。
     * @return String 例外メッセージ
     */
    public String getMessage(){
        return message;
    }
}
