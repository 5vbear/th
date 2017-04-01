// ファイル名 : LocalPropertiesException.java
// バージョン : $Id: LocalPropertiesException.java 9645 2011-01-27 13:20:58Z txf $

package th.com.property;

import java.io.IOException;

/**
 * LocalPropertiesの読み込みに失敗した場合にthrowされるExceptionです。
 */
public class LocalPropertiesException extends IOException {

	/** ストリーム固有識別子 */
	private static final long serialVersionUID = 7533076836301435968L;

	/**
	 * 詳細メッセージを指定しないでLocalPropertiesExceptionを構築します。
	 */
	protected LocalPropertiesException() {
		super();
	}

	/**
	 * 指定された詳細メッセージを持つLocalPropertiesExceptionを構築します。
	 * @ param s 詳細メッセージ
	 */
	protected LocalPropertiesException(String s) {
		super(s);
	}

}
