package th.com.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ChangeFtpUsersDLSpeed {
//	public static void main(String[] args) {
//		// Tskill *xlight*
//		try {
//
//			Runtime.getRuntime().exec("cmd /c start D:/Xlight/kill.bat");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		change("D:/Xlight/ftpd.users", "thccb", 1111);
//		try {
//			Runtime.getRuntime().exec("cmd /c start D:/Xlight/start.bat");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public static void change(String path, String user, int downloadrate, int uploadrate) {
		String filePath = Define.FTP_USERS_FILE_PATH;
		// String filepath = path;
		try {
			FileReader fr;
			StringBuffer sb = new StringBuffer();
			try {
				fr = new FileReader(filePath);
				// 可以换成工程目录下的其他文本文件
				BufferedReader br = new BufferedReader(fr);
				String line = null;
				boolean needChange = false;
				while ((line = br.readLine()) != null) {
					if (line.contains(user)) {
						needChange = true;
					}
					if (needChange && line.contains("downloadrate")) {
						// 设置下载速度
						line = "ftpserver.user." + user +".downloadrate=" + downloadrate*1000;
					}
					if (needChange && line.contains("uploadrate")) {
						// 设置上传速度
						line = "ftpserver.user." + user +".uploadrate=" + uploadrate*1000;
					}
					if (!line.contains(user)) {
						needChange = false;
					}
					sb.append(line + "\n");
					
				}

				br.close();
				fr.close();

				System.out.println(sb.toString());

				FileWriter writer = new FileWriter(filePath);
				writer.write(sb.toString());
				writer.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String readFileFromPath(String filePath) {
		try {
			FileReader fr;
			StringBuffer sb = new StringBuffer();
			try {
				fr = new FileReader(filePath);
				// 可以换成工程目录下的其他文本文件
				BufferedReader br = new BufferedReader(fr);
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				return sb.toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
