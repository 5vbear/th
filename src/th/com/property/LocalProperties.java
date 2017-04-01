// ファイル名 : LocalProperties.java
// バージョン : $Id: LocalProperties.java 9645 2011-01-27 13:20:58Z txf $

package th.com.property;

import java.io.*;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * リソースファイルからプロパティ値を読み込み、要求があった時に
 * 指定キーに対応する文字列を返します。
 */
public class LocalProperties {

	/** ファイル内容格納用Propertiesクラスインスタンス */
	private static Properties localizeDictionary = null;

	/**
	 * 初期処理を行います。初回実行時のみ処理を行い、それ以外は何もしません。
	 * このメソッドが正常に完了しないと、LocalPropertiesを利用することはできません。
	 * @throws LocalPropertiesException プロパティファイルの読み込みに失敗した場合
	 * @throws NullPointerException アプリケーションルートディレクトリが未設定の場合
	 */
	public static void init()
		throws LocalPropertiesException,NullPointerException {

		 //System.out.println("Property Start");
		//初回のみ実行
		if(localizeDictionary==null) {

			try{
				//イニシャルコンテキスト取得

				Context ctx = (Context)new InitialContext();
				//環境変数取得用コンテキスト取得

				Context envCtx = (Context)ctx.lookup("java:comp/env");
				//設定ファイル名取得
				String propertyFile = (String)envCtx.lookup("property_file_name");

				System.out.println("propertyFile : " + propertyFile);

				//ファイルの存在をチェックする
				File file = new File(propertyFile);
				if(!file.exists()) {

					throw new LocalPropertiesException("Initialization failure");
				}

				//ローカライズ辞書の生成
				localizeDictionary = new Properties();
				FileInputStream fin = new FileInputStream(file);
				localizeDictionary.load(new PropertiesInputStream(fin));
				fin.close();
			}
			catch (Exception ex) {
				localizeDictionary = null;
				throw new LocalPropertiesException(
					"Initialization failure : " + ex.toString());
			}
		}

	}

	/**
	 * 指定されたキーに対応する文字列を取得して返します。
	 * @param string キー
	 * @return 指定されたキーに対応する文字列。
	 * @throws LocalPropertiesException プロパティの読み込みに失敗した場合
	 */
	public static synchronized String getProperty(String string)
		throws LocalPropertiesException {

		// プロパティファイルが読み込まれていない場合はここで読み込む。
		if ( localizeDictionary == null ){
			init();
		}

		//キーをもとに読み込む
		String result = "";
//		String key = "\"" + string + "\"";
		try{
			result = localizeDictionary.getProperty(string);
//			result = result.substring(1, result.lastIndexOf("\""));
		}
		catch(StringIndexOutOfBoundsException ex) {
			throw new LocalPropertiesException(
				"Can not get property value for key " + string);
		}
		catch(NullPointerException ex) {
			throw new LocalPropertiesException(
				"Can not get property value for key " + string);
		}
		return result;

	}

}
