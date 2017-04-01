/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.util.mail;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define;
import th.dao.SysConfDAO;
import th.entity.SysServBean;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
public class MailUtil {
	//日志对象
	private static Log logger = LogFactory.getLog( MailUtil.class );
	//邮件信息对象
	private static MailSenderInfo mailInfo;

	//设置发送邮件需要的各种属性
	private static MailSenderInfo prepare( String[] toAddress, String subject, String content )
			throws SQLException {
		if ( mailInfo == null ) {
			mailInfo = new MailSenderInfo();
			
			try {
				SysServBean sysServBean = new SysConfDAO().getSysConfInfo();
				// 设置smtp服务器
				mailInfo.setMailServerHost( sysServBean.getSmtpHost() );
				// 设置端口号 "25"
				mailInfo.setMailServerPort( String.valueOf(sysServBean.getSmtpPort()) );
				// 设置用户名
				mailInfo.setUserName( sysServBean.getMailAuthNick() );
				// 设置密码
				mailInfo.setPassword( sysServBean.getMailAuthPass() );

				// 设置源邮箱地址
				mailInfo.setFromAddress( sysServBean.getMailFrom() );
			}
			catch ( SQLException e ) {
				logger.error( "read DB error", e );
				throw e;
			}
			// 默认设置为需要身份验证
			mailInfo.setValidate( true );
		}

		// 设置目标邮箱地址 此变量需要作为参数传递过来
		mailInfo.setToAddress( toAddress );
		// 设置邮件标题 此变量需要作为参数传递过来
		mailInfo.setSubject( subject );
		// 设置邮箱内容 此变量需要作为参数传递过来
		mailInfo.setContent( content );
		return mailInfo;
	}
	
	//设置发送邮件需要的各种属性
	private static MailSenderInfo prepare( String[] toAddress, String subject, String content, String[] attachfileName )
			throws SQLException {
		if ( mailInfo == null ) {
			mailInfo = new MailSenderInfo();
			
			try {
				SysServBean sysServBean = new SysConfDAO().getSysConfInfo();
				// 设置smtp服务器
				mailInfo.setMailServerHost( sysServBean.getSmtpHost() );
				// 设置端口号 "25"
				mailInfo.setMailServerPort( String.valueOf(sysServBean.getSmtpPort()) );
				// 设置用户名
				mailInfo.setUserName( sysServBean.getMailAuthNick() );
				// 设置密码
				mailInfo.setPassword( sysServBean.getMailAuthPass() );

				// 设置源邮箱地址
				mailInfo.setFromAddress( sysServBean.getMailFrom() );
			} catch (SQLException ex) {
				logger.error( "read DB error", ex );
				throw ex;
			}
			// 默认设置为需要身份验证
			mailInfo.setValidate( true );
		}

		// 设置目标邮箱地址 此变量需要作为参数传递过来
		mailInfo.setToAddress( toAddress );
		// 设置邮件标题 此变量需要作为参数传递过来
		mailInfo.setSubject( subject );
		// 设置邮箱内容 此变量需要作为参数传递过来
		mailInfo.setContent( content );
		// 设置附件文件路径 此变量需要作为参数传递过来
		mailInfo.setAttachFileNames( attachfileName );
		return mailInfo;
	}

	/**
	 * 通过DB检测邮件发送功能是否开启
	 * @return
	 */
	public static boolean checkMailStatus(){
		
		boolean retFlg = false;
		SysConfDAO scd = new SysConfDAO();
		SysServBean msb = new SysServBean();
		
		try {
			msb = scd.getSysConfInfo();
			String serStatus = msb.getSerStatus();
			if(Define.CHAR_IDENTIFY_ONE.equals( serStatus )){
				retFlg = true;
			}
			return retFlg;
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		
		return retFlg;
		
	}
	
	// 对外提供的发送邮件的接口
	/**
	 * 发送文本邮件(不支持发送附件)
	 * 
	 * @throws LocalPropertiesException
	 * 
	 */
	public boolean sendTextMail( String[] toAddress, String subject, String content ) throws SQLException {
		
		boolean result = false;
		if(checkMailStatus()){
		// 设置邮件的各种参数
		MailSenderInfo mailInfo = prepare( toAddress, subject, content );
		MailSender ms = new MailSender();
		logger.info( "sendMail_test: 准备发送邮件" );
		result = ms.sendTextMail( mailInfo );// 发送文体格式
		logger.info( "sendMail_test: 邮件发送结束,其发送状态是 " + result );
		}	
		return result;

	}

	// 对外提供的发送邮件的接口
	/**
	 * 发送Html邮件(不支持发送附件)
	 * @throws LocalPropertiesException
	 * 
	 */
	public boolean sendHtmlMail( String[] toAddress, String subject, String content ) throws SQLException {
		boolean result = false;
		if(checkMailStatus()){
		// 设置邮件的各种参数
		MailSenderInfo mailInfo = prepare( toAddress, subject, content );
		
		MailSender ms = new MailSender();
		result = ms.sendHtmlMail( mailInfo );// 发送html格式
		}	
		return result;
	}

	/**
	 * 发送Html邮件(支持发送附件)
	 * @throws LocalPropertiesException
	 * @throws SQLException
	 * 
	 */
	public boolean sendHtmlMailWithAttachfile( String[] toAddress, String subject, String content, String[] attachfileName ) throws SQLException {
		boolean result = false;
		if(checkMailStatus()){
		// 设置邮件的各种参数
		MailSenderInfo mailInfo = prepare( toAddress, subject, content, attachfileName );
		
		MailSender ms = new MailSender();
		result = ms.sendHtmlMail( mailInfo );// 发送html格式
		}
		return result;
	}

}
