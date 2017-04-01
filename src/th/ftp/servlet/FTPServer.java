package th.ftp.servlet;

import java.io.File;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.log4j.Logger;

import th.com.util.Define;

public class FTPServer {
	private static FtpServer server = null;
	protected static Logger logger = Logger.getLogger(FTPServer.class);
	private static String CLASS_NAME = "FTPServer";
	
	public static void create() {
		if (server == null) {
			FtpServerFactory serverFactory = new FtpServerFactory();
			ListenerFactory factory = new ListenerFactory();
			factory.setPort(21);
			serverFactory.addListener("default", factory.createListener());
			server = serverFactory.createServer();
			PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
			userManagerFactory.setFile(new File(Define.FTP_USERS_FILE_PATH));
			logger.info("file path:" + Define.FTP_USERS_FILE_PATH);
			PasswordEncryptor passwordEncryptor = new ClearTextPasswordEncryptor();
			userManagerFactory.setPasswordEncryptor(passwordEncryptor);
			serverFactory
					.setUserManager(userManagerFactory.createUserManager());
			try {
				server.start();
			} catch (FtpException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error(CLASS_NAME + e.getMessage());
			}
		}
	}

	public static void destroy() {
		if (server != null) {
			server.stop();
			server = null;
		}
	}


}
