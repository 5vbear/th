package th.ftp.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import th.dao.BaseDao;

public class FtpDao extends BaseDao {
	public List<FtpInfoBean> getAllFtpUserInfo() throws SQLException {

		List<FtpInfoBean> appBeanList = new ArrayList<FtpInfoBean>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT  FTP_MANAGEMENT_ID,  ");
		sb.append("		   FTP_USER,  ");
		sb.append("        MAX_DOWNLOAD_SPEED,  ");
		sb.append("        MAX_UPLOAD_SPEED  ");
		sb.append("FROM    tb_ftp_user_management ");
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			HashMap[] result = select();
			for (int i = 0; i < result.length; i++) {
				FtpInfoBean ftpInfoBean = new FtpInfoBean();
				ftpInfoBean.setFtpMangId(Long.parseLong((String) result[i].get("FTP_MANAGEMENT_ID")));
				ftpInfoBean.setFtpuser((String) result[i].get("FTP_USER"));
				ftpInfoBean.setMaxDownloadSpeed((String) result[i]
						.get("MAX_DOWNLOAD_SPEED"));
				ftpInfoBean.setMaxUploadSpeed((String) result[i]
						.get("MAX_UPLOAD_SPEED"));
				appBeanList.add(ftpInfoBean);
			}
		} finally {
			releaseConnection();
		}
		return appBeanList;
	}
	
	public HashMap[] getFtpUserInfoById(Long mangId) throws SQLException {

		List<FtpInfoBean> appBeanList = new ArrayList<FtpInfoBean>();
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT  FTP_MANAGEMENT_ID,  ");
		sb.append("		   FTP_USER,  ");
		sb.append("        MAX_DOWNLOAD_SPEED,  ");
		sb.append("        MAX_UPLOAD_SPEED  ");
		sb.append("FROM    tb_ftp_user_management ");
		if(null != mangId){
			sb.append("WHERE   FTP_MANAGEMENT_ID = ? ");
		}
		
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			if(null != mangId){
				stmt.setLong(1, mangId);
			}
		
			HashMap[] result = select();
			return result;
			
		} finally {
			releaseConnection();
		}
		
	}
	
	public int updateFtpUserInfoById(Long mangId, String uploadSpeed, String downloadSpeed) throws SQLException {

		List<FtpInfoBean> appBeanList = new ArrayList<FtpInfoBean>();
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE  tb_ftp_user_management  ");
		sb.append("SET        MAX_DOWNLOAD_SPEED = ? , ");
		sb.append("        MAX_UPLOAD_SPEED = ? ");
		sb.append("WHERE   FTP_MANAGEMENT_ID = ? ");
		int result = 0;
		try {
			connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.setString(1, downloadSpeed);
			stmt.setString(2, uploadSpeed);
			stmt.setLong(3, mangId);
		
			result = update();
			commit();
			
		} finally {
			releaseConnection();
		}
		return result;
	}
}
