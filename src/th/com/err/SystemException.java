/**
 * ====================================================================
 *  Copyright(C) 2004 SoFT
 *  All Right Reserved.
 *
 *   @since   2004.2.1
 *   @author  SoFT
 *   $Id: SystemException.java 9645 2011-01-27 13:20:58Z txf $
 * ====================================================================
 */

package th.com.err;

import java.io.PrintWriter;

/**
 * システム例外クラス<br>
 * システム的な例外が発生し、業務を続行することが不可能な場合に使用する、
 * ITT用システム例外クラス。
 */
public class SystemException extends Exception {

    /** ネストされるException */
    public Throwable exception = null;

    /**
     * SystemExceptionコンストラクタ
     * @param e 発生したException
     */
    public SystemException(Throwable e){
        this.exception = e;
    }


    /**
     * getMessage()のオーバーライド
     * @return String 例外メッセージ
     */
    public String getMessage(){

        if(exception != null) {
            return exception.getMessage();
        } else {
            return super.getMessage();
        }
    }


    /**
     * printStackTrace()のオーバーライド
     */
    public void printStackTrace(){

        if(exception != null) {
            exception.printStackTrace();
        } else {
            super.printStackTrace();
        }
    }


    /**
     * printStackTrace(PrintWriter s)のオーバーライド
     */
    public void printStackTrace(PrintWriter s){

        if(exception != null) {
            exception.printStackTrace(s);
        } else {
            super.printStackTrace(s);
        }
    }
}
