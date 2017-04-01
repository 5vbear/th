package th.ftp.management;

import java.io.File;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.ClearTextPasswordEncryptor;
import org.apache.ftpserver.usermanager.PasswordEncryptor;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;

public class FtpServerUtils {
	private static final FtpServer server = null;

	public static void create() {
	}

	public static void destroy() {
	}

	public static void main(String[] args) throws Exception {
		FtpServerFactory serverFactory = new FtpServerFactory();
		ListenerFactory factory = new ListenerFactory();
		factory.setPort(21);
		serverFactory.addListener("default", factory.createListener());
		FtpServer server = serverFactory.createServer();
		PropertiesUserManagerFactory userManagerFactory = new PropertiesUserManagerFactory();
		userManagerFactory.setFile(new File("users.properties"));
		PasswordEncryptor passwordEncryptor = new ClearTextPasswordEncryptor();
		userManagerFactory.setPasswordEncryptor(passwordEncryptor);
		serverFactory.setUserManager(userManagerFactory.createUserManager());
		server.start();
	}
}
