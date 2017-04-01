package th.util.mail;   
/**   
* 发送邮件需要使用的基本信息 
*/    
import java.util.Properties;   
/**
 * 
 * Descriptions 邮件对象    用于保存发送邮件需要的各种信息
 *
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 *
 */
public class MailSenderInfo {    
    // 发送邮件的服务器的IP和端口    
    private String mailServerHost;    
    //端口号默认值是25
    private String mailServerPort = "25";  
    
    // 邮件发送者的地址    
    private String fromAddress;    
    // 邮件接收者的地址    
    private String[] toAddress;    
    // 登录邮件发送服务器的用户名和密码    
    private String userName;    
    private String password;    
    // 是否需要身份验证     默认是需要
    private boolean validate = true;    
    // 邮件主题    
    private String subject;    
    // 邮件的文本内容    
    private String content;    
    // 邮件附件的文件名    
    private String[] attachFileNames;
//    /** メール送信失敗時のタイムアウト時間（ミリ秒）*/
//    int timeout = 60 * 1000;
    /**   
      * 获得邮件会话属性   
      */    
    public Properties getProperties(){    
      Properties p = new Properties();    
      p.put("mail.smtp.host", this.mailServerHost);    
      p.put("mail.smtp.port", this.mailServerPort); 
//      //タイムアウト値の設定
//	  p.put("mail.smtp.connectiontimeout",String.valueOf(timeout));
//	  p.put("mail.smtp.timeout",String.valueOf(timeout));
//	  p.put("mail.transport.protocol", "smtp");
      p.put("mail.smtp.auth", validate ? "true" : "false");    
      return p;    
    }    
    //下面是各个属性的get set方法
    public String getMailServerHost() {    
      return mailServerHost;    
    }    
    public void setMailServerHost(String mailServerHost) {    
      this.mailServerHost = mailServerHost;    
    }   
    public String getMailServerPort() {    
      return mailServerPort;    
    }   
    public void setMailServerPort(String mailServerPort) {    
      this.mailServerPort = mailServerPort;    
    }   
    public boolean isValidate() {    
      return validate;    
    }   
    public void setValidate(boolean validate) {    
      this.validate = validate;    
    }   
    public String[] getAttachFileNames() {    
      return attachFileNames;    
    }   
    public void setAttachFileNames(String[] fileNames) {    
      this.attachFileNames = fileNames;    
    }   
    public String getFromAddress() {    
      return fromAddress;    
    }    
    public void setFromAddress(String fromAddress) {    
      this.fromAddress = fromAddress;    
    }   
    public String getPassword() {    
      return password;    
    }   
    public void setPassword(String password) {    
      this.password = password;    
    }   
    public String[] getToAddress() {    
      return toAddress;    
    }    
    public void setToAddress(String[] toAddress) {    
      this.toAddress = toAddress;    
    }    
    public String getUserName() {    
      return userName;    
    }   
    public void setUserName(String userName) {    
      this.userName = userName;    
    }   
    public String getSubject() {    
      return subject;    
    }   
    public void setSubject(String subject) {    
      this.subject = subject;    
    }   
    public String getContent() {    
      return content;    
    }   
    public void setContent(String textContent) {    
      this.content = textContent;    
    }    
}   