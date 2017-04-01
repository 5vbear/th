package th.util.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.log4j.Logger;

import th.com.property.LocalProperties;
import th.com.util.Define;

public class FtpUtils {

	private static final Logger	logger	= Logger.getLogger(FtpUtils.class);

	public static FTPClient getFTPClient() {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(LocalProperties.getProperty("FTP_SERVER_HOST"), Integer.parseInt(LocalProperties.getProperty("FTP_SERVER_PORT")));
			ftpClient.login(LocalProperties.getProperty("FTP_SERVER_USER"), LocalProperties.getProperty("FTP_SERVER_PASSWORD"));
		} catch (SocketException e) {
			e.printStackTrace();
			logger.error(" getFTPClient error" + e);
		} catch (IOException e) {
			logger.error(" getFTPClient error" + e);
			e.printStackTrace();
		}
		return ftpClient;
	}

	public static FTPFile[] getFiles(String path) {
		FTPClient ftpClient = getFTPClient();
		try {
			return ftpClient.listFiles(path);
		} catch (IOException e) {
			logger.error(" isFileExisted error" + e);
			e.printStackTrace();
			return null;
		}
	}

	public static boolean isFileExisted(String fileName, String path) {

		FTPFile[] fileArray = getFiles(path);
		if (fileArray == null) {
			return false;
		}
		for (FTPFile ftpFile : fileArray) {
			if (ftpFile.getName().equals(fileName)) {
				return true;
			}
		}
		return false;
	}

	public static boolean uploadFile(String uploadFile,String uploadFtpPath) {
		FTPClient ftpClient = getFTPClient();
		int reply = ftpClient.getReplyCode();
		try {
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return false;
			}
			ftpClient.changeWorkingDirectory(uploadFtpPath);
			File file = new File(uploadFile);
			FileInputStream io = new FileInputStream(uploadFile);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile(file.getName(), io);
			io.close();
			ftpClient.logout();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteFile(String deleteFile,String deleteFtpPath) {
		FTPClient ftpClient = getFTPClient();
		int reply = ftpClient.getReplyCode();
		try {
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return false;
			}
			ftpClient.changeWorkingDirectory(deleteFtpPath);
			FTPFile[] fs = ftpClient.listFiles();
			for (FTPFile ff : fs) {
				if (!ff.isDirectory()&&ff.getName().equals(deleteFile)) {
					ftpClient.deleteFile(ff.getName());
				}
			}
			ftpClient.logout();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean uploadFile(List<File> uploadFileList,String uploadFtpPath) {
		FTPClient ftpClient = getFTPClient();
		int reply = ftpClient.getReplyCode();
		try {
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return false;
			}
			ftpClient.changeWorkingDirectory(uploadFtpPath);
			for (File uploadFile : uploadFileList) {
				FileInputStream io = new FileInputStream(uploadFile);
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftpClient.storeFile(uploadFile.getName(), io);
				io.close();
			}
			ftpClient.logout();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean downloadFile(String downloadFile, String downloadFtpPath) {

		// 初始表示下载失败
		boolean success = false;
		// 创建FTPClient对象
		FTPClient ftpClient = getFTPClient();
		int reply = ftpClient.getReplyCode();
		try {
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				return false;
			}
			// 转到指定下载目录
			ftpClient.changeWorkingDirectory(downloadFtpPath);
			ftpClient.setControlEncoding("UTF-8");
			//设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			
			FTPFile[] files = ftpClient.listFiles(downloadFtpPath);
			for (FTPFile ff : files) {
		        if (ff.getName().equals(downloadFile)) {
		        	// 客户端保存文件路径生成
					File file = new File(Define.DEFALUT_FTP_DOWNLOAD_PATH + downloadFile);
					FileOutputStream fos = new FileOutputStream(file);
					String dlFileName = downloadFtpPath + "/" + downloadFile;
					ftpClient.retrieveFile(new String(dlFileName.getBytes("UTF-8"), "ISO-8859-1"), fos);
					fos.flush();
					fos.close();
					ftpClient.logout();
					// 下载成功
					success = true;
					break;
		        }
		    }					
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {

			if (ftpClient.isConnected()) {

				try {
					ftpClient.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
					throw new RuntimeException("关闭FTP连接发生异常！", ioe);
				}
			}
		}		

		return success;

	}

}
