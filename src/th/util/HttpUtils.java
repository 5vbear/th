package th.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HttpUtils {

	private static int	connectionTimeout	= 10;

	public static String access(String urlstring) throws IOException {

		InputStream inputStream = null;
		InputStreamReader inputReader = null;
		HttpURLConnection conn = null;
		try {
			if (StringUtils.isBlank(urlstring)) {
				return "";
			}
			StringBuffer buffer = new StringBuffer(1024);
			URL url = new URL(urlstring);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(connectionTimeout * 1000);
			conn.setReadTimeout(connectionTimeout * 1000);
			inputStream = conn.getInputStream();
			inputReader = new InputStreamReader(inputStream, "UTF-8");
			char chs[] = new char[512];
			int length = 0;
			while ((length = inputReader.read(chs)) > 0) {
				buffer.append(chs, 0, length);
			}
			return buffer.toString();
		} finally {

			if (inputReader != null) {
				try {
					inputReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				inputReader = null;
			}
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				inputStream = null;
			}
			if (conn != null) {
				try {
					conn.disconnect();
				} catch (Exception e) {
					e.printStackTrace();
				}
				conn = null;
			}

		}
	}

	public static void setConnectionTimeout(int timeoutInSeconds) {
		connectionTimeout = timeoutInSeconds;
	}

}
