package th.ftp.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;


public class FTPServerInitServlet implements ServletContextListener {
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger.getLogger(FTPServerInitServlet.class);
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// 启动ftp服务器
		FTPServer.create();
		logger.info("启动ftp服务器完成");
	}


}
