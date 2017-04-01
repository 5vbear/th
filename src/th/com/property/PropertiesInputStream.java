// ファイル名 : PropertiesInputStream.java
// バージョン : $Id: PropertiesInputStream.java 9645 2011-01-27 13:20:58Z txf $

package th.com.property;

import java.io.*;

/**
 * Localeに対応したリソースファイルの読み込み処理を行います。
 */
class PropertiesInputStream extends FilterInputStream {

	/** 入力バッファ */
	BufferedReader reader;
	/** 読み込まれた文字 */
	int c;
	/** 読み込み位置 */
	int state;
	/** マーク文字 */
	int markedc;
	/** マーク位置 */
	int markedstate;

	/**
	 * 指定されたストリームで新しいPropertiesInputStreamを構築します。
	 * @param in 基礎の入力ストリーム
	 * @throws UnsupportedEncodingException 
	 */
	public PropertiesInputStream(InputStream in) throws UnsupportedEncodingException {
		super(in);
		reader = new BufferedReader( new InputStreamReader( in , "EUC-JP" ) );
	}

	/**
	 * 次のバイトデータを読み込みます。
	 * @return 次のバイトデータ。
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public int read() throws IOException {
		switch(state){
			case 0:
				c = reader.read();
				if(c <= 0x00ff) {
					return c;
				}
				state = 1;
				return (int)'\\';
			case 1:
				state = 2;
				return (int)'u';
			default:
				int uc = (c >> (4 * (5 - state))) & 0x000f;
				state++;
				if(state == 6) {
					state = 0;
				}
				return (uc < 10 ? uc + (int)'0' : uc - 10 + (int)'a');
		}
	}

	/**
	 * 入力ストリームからlenバイトまでのデータをバイト配列に読み込みます。
	 * @param b データが読み込まれるバッファ
	 * @param off データが書き込まれる配列 b の開始オフセット
	 * @param len 読み込むバイトの最大数
	 * @return バッファに読み込まれた全バイト数。
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public int read(byte b[], int off, int len) throws IOException {
		int count = 0;
		int i = 0;
		for(; off<len; off++){
			i = read();
			if(i == -1) {
				break;
			}
			b[off] = (byte)i;
			count++;
		}
		if (i == -1 && count == 0) {
			return -1;
		}
		return count;
	}

	/**
	 * 指定された長さ分読み込みをスキップします。
	 * @param n スキップするバイト数
	 * @return 実際にスキップされたバイト数
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public long skip(long n) throws IOException {
		long l = 0;
		int i = 0;
		for(; l<n; l++){
			i = read();
			if(i == -1){
				break;
			}
		}
		if(i == -1 && l == 0){
			return -1;
		}
		return l;
	}

	/**
	 * ストリームの現在位置にマークを設定します。
	 * @param readlimit マーク位置が無効になる前に読み込み可能な最大バイト数
	 */
	public synchronized void mark(int readlimit) {
		in.mark(readlimit);
		markedc = c;
		markedstate = state;
	}

	/**
	 * 入力ストリームへ直前にmarkメソッドが呼び出されたときのマーク位置に
	 * 再設定します。
	 * @throws IOException ストリームにマークが設定されていなかった場合、
	 *                     またはマークが無効になっていた場合
	 */
	public synchronized void reset() throws IOException {
		in.reset();
		c = markedc;
		state = markedstate;
	}

}
