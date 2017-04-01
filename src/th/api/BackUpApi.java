package th.api;

import java.util.HashMap;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import th.com.util.Define;

public class BackUpApi {
	  /** ホスト名 */
    private String hostName;
    /** ポート番号 */
    private int portNo;
    /** 要求URL */
    private String sendUrl;

    /** ログ出力用クラス名 */
    private String className;
    
    protected Logger logger = Logger.getLogger(this.getClass());

    /**
     * コンストラクタ<br>
     * 引数をインスタンス変数に設定する。
     */
    public BackUpApi(String hostName,int portNo,String sendUrl) {
        //インスタンス変数にパラメータを設定する

        this.hostName   = hostName;
        this.portNo     = portNo;
        this.sendUrl    = sendUrl;
        //ログ出力用にクラス名を取得
        className = "BackUpApi";


    }

    /**
     * パラメータを受け取り、リクエストの送受信を行う。
     * パラメータはエンコードされているものとし、このメソッドでは
     * エンコードしない。
     * @param param パラメータHashMap
     * @throws Exception 
     */
    public void communicate(HashMap param)  throws Exception{
    	
        try {
        	HttpClient client = new HttpClient();
  	      	client.getHostConfiguration().setHost( hostName , portNo, "http" );
  	      	PostMethod httpMethod = new PostMethod( sendUrl );
  	      	setParam( httpMethod, param );
  	      	 

            // HTTP/1.0で実施する
            httpMethod.setHttp11( false );
            String charset = "charset=" + "UTF-8";
            String content = Define.CONTENT_TYPE + "; " + charset;
            httpMethod.setRequestHeader ( "Content-type", content );

            logger.debug( "bodyAsString:" + httpMethod.getRequestBodyAsString());
            
            logger.debug("送信実行");
            int status = client.executeMethod( httpMethod );
       
            logger.debug( "送信実行結果コード：" + status );
            
        }
    	catch ( Exception e ) {
    		logger.error(e);
            throw e;
		} 
        
    }
    /**
     * パラメータの設定を行う。
     * @param param PostMethod
     * @param param パラメータ
     */
    private void setParam( PostMethod httpMethod, HashMap param ){

    	logger.debug("===== パラメータ設定開始 =====");

        // パラメータなし
        if ( param == null ){
        	logger.debug("param is null");
            return;
        }

        Set set = param.keySet();
        String[] keyArray = (String[])set.toArray( new String[ 0 ] );
        NameValuePair[] nvp = new NameValuePair[ set.size() ];
        // パラメータ設定
        StringBuffer sb = new StringBuffer();
        for ( int i = 0; i < keyArray.length; i++ ){
            String key = keyArray[ i ];
            sb.append(" [").append(key);
            String value = ( String )param.get( key );
            sb.append(" = ").append(value).append("]");
            nvp[ i ] = new NameValuePair( key, value );
        }
        logger.debug( sb.toString() );
        
        // クエリーパラメータ設定
        httpMethod.setRequestBody( nvp );
        httpMethod.setRequestContentLength( PostMethod.CONTENT_LENGTH_AUTO );

    }

}
