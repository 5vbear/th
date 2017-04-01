//Copyright(c)2006 Pioneer Corporation. All Rights Reserved
//File名 : ResponseOut.java

package th.util;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ResponseOut {

    /** コンテキストタイプ：テキスト */
    public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    public static final String CONTENT_TYPE_TEXT_PLAIN_GB2312 = "text/plain;charset=gb2312";
    /** コンテキストタイプ：テキスト */
    public static final String CONTENT_TYPE_TEXT_PLANE = "text/plane";
    /** コンテキストタイプ：HTML */
    public static final String CONTENT_TYPE_TEXT_HTML  = "text/html";
    /** コンテキストタイプ：バイナリ */
    public static final String CONTENT_TYPE_BINARY     = "binary/cuma_usr";
    /** コンテキストタイプ：XML */
    public static final String CONTENT_TYPE_XML     = "text/xml";

    /** レスポンス出力用 */
    DataOutputStream out = null;
    /** コンテントタイプ */
    String contentType;
    /** レスポンス向きポインタ */
    HttpServletResponse res;

    /**
     * コンストラクタ
     * @param response レスポンス
     * @throws IOException
     */
    public ResponseOut(HttpServletResponse response) throws IOException{
        //レスポンス保持
        this.res = response;

        //デフォルトのコンテントタイプ設定
        this.setContentType(CONTENT_TYPE_TEXT_HTML);
    }

    /**
     * コンストラクタ
     * @param response レスポンス
     * @param contentType 設定するコンテキストタイプ
     * @throws IOException
     */
    public ResponseOut(HttpServletResponse response, String contentType)
                                                     throws IOException{
        //レスポンス保持
        this.res = response;
        //コンテントタイプ設定
        this.setContentType(contentType);
    }

    /**
     * コンテントタイプの設定
     * @param type 設定するコンテキストタイプ
     */
    public void setContentType(String type){
        this.contentType = type;
        this.res.setContentType(this.contentType);
    }

    /**
     * コンテントタイプの返却
     * @param type
     * @return 設定されているコンテントタイプ
     */
    public String getContentType(){
        return this.contentType;
    }

    /**
     * 出力ストリームの取得
     * @throws IOException
     */
    public void open() throws IOException{
        //既にストリームを取得していた場合、クローズする
        if(out != null){
            out.close();
        }
        //コンテントタイプ設定
        res.setContentType(this.contentType);
        //出力用ストリーム確保
        out = new DataOutputStream(res.getOutputStream());
    }

    /**
     * 出力用ストリームのクローズ
     * @throws IOException
     */
    public void close() throws IOException{
        if(out != null){
            out.close();
        }
    }

    /**
     * テキストでレスポンス出力
     * @param resStr 出力する文字列
     */
    public void write(String resStr) throws IOException{
        if(out == null){
            throw new IOException("出力ストリームが取得されていません。");
        }
        //出力
        out.writeBytes(resStr);
    }

    /**
     * バイナリでレスポンス出力
     * @param resBry 出力するバイナリデータ
     */
    public void write(byte[] resBinary) throws IOException{
        if(out == null){
            throw new IOException("出力ストリームが取得されていません。");
        }
        //出力
        out.write(resBinary);
    }
}