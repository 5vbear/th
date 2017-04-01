/**
 * ====================================================================
 *  Copyright (C) 2010 Pioneer Corporation. All Rights Reserved
 *
 *   @since   2010.4.16
 *   @author  SoFT
 *   $Id: Initializer.java 9645 2011-01-27 13:20:58Z txf $
 * ====================================================================
 */

package th.com.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/*------------------------------------------------------------------------------
/  クラス名    ：  Initializer
/-----------------------------------------------------------------------------*/
/**
 * アプリケーション初期化クラス<br>
 * アプリケーションの起動時に初期化処理を実施します。
 */
public class Initializer implements ServletContextListener {

    private static final Object mutex  = new Object();

    /*-------------------------------------------------------------------------
    /  メソッド名：contextInitialized
    /------------------------------------------------------------------------*/
    /**
     * アプリケーションが初期化された通知を受け取ります。
     * 
     */
    public void contextInitialized( ServletContextEvent contextEvent ) {

        synchronized( mutex ) {

            System.out.println( "contextInitialized" );

            ServletContext sc = contextEvent.getServletContext();


        }

    }

    /*-------------------------------------------------------------------------
    /  メソッド名：contextDestroyed
    /------------------------------------------------------------------------*/
    /**
     * アプリケーションが破棄された通知を受け取ります。
     * 
     */
    public void contextDestroyed( ServletContextEvent contextEvent ) {

        synchronized( mutex ) {

            System.out.println( "contextDestroyed" );

        }

    }
}
