/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.dao.AvailablePage.AvailablePageDAO;
import th.entity.AvailablePageBean;
import th.entity.ChannelBean;
import th.entity.PageBean;
import th.util.FileTools;
import th.util.ftp.FtpUtils;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ChannelDAO extends BaseDao {
	protected Logger logger = Logger.getLogger(this.getClass());

	public ChannelDAO() {

	}

	public int insertChannel(AvailablePageBean channelBean, String status) throws SQLException {

		List orgList = new AvailablePageDAO().getSubOrgList(channelBean
				.getOrgId() + "");
		if (orgList.size() == 1 && "".equals(orgList.get(0))) {
			orgList = new ArrayList();
		}
		orgList.add(channelBean.getOrgId());
		Long flag = new AvailablePageDAO().selectFlag();
		for (int i = 0; i < orgList.size(); i++) {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("	INSERT INTO				");
			stringBuffer.append("	TB_CCB_CHANNEL(			");
			stringBuffer.append("	CHANNEL_ID,CHANNEL_NAME,");
			stringBuffer.append("	CHANNEL_URL,CHANNEL_TYPE,");
			stringBuffer.append("	STATUS,OPERATETIME,	ORG_ID,	");
			stringBuffer.append("	OPERATOR,FLAG,ENTERPRISES_TYPE) 				");
			stringBuffer
					.append(" VALUES(nextval( 'SEQ_TB_CCB_CHANNEL' ),?,?,?,?,?,?,?,?,?)");
			logger.info("将会被执行的SQL语句: " + stringBuffer.toString());
			try {
				connection();
				stmt = con.prepareStatement(stringBuffer.toString());
				stmt.clearParameters();
				stmt.setString(1, channelBean.getRequestName());
				stmt.setString(2, channelBean.getRequestURL());
				stmt.setString(3, channelBean.getType());
				stmt.setString(4, status);
				stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
				stmt.setLong(6, Long.parseLong(orgList.get(i).toString()));
				stmt.setLong(7, channelBean.getOperator());
				stmt.setLong(8, flag);
				stmt.setString(9, channelBean.getEnterType());
			} catch (SQLException e) {
				e.printStackTrace();
			}

			int result = 0;
			try {
				result = stmt.executeUpdate();
				con.commit();
				// try {
				// this.updateCommand(channelBean.getType());
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
			} catch (SQLException ex) {
				ex.printStackTrace();
				try {
					rollback();

					releaseConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} finally {
				try {
					releaseConnection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return 0;
	}

	public int insertChannel(ChannelBean channelBean) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	INSERT INTO				");
		stringBuffer.append("	TB_CCB_CHANNEL(			");
		stringBuffer.append("	CHANNEL_ID,CHANNEL_NAME,");
		stringBuffer.append("	CHANNEL_URL,CHANNEL_TYPE,");
		stringBuffer.append("	STATUS,OPERATETIME,		");
		stringBuffer.append("	OPERATOR) 				");
		stringBuffer.append("	VALUES(?,?,?,?,?,?,?)		");
		long sequence = 0;
		try {
			sequence = getSequence("SEQ_TB_CCB_CHANNEL");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			connection();
			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.clearParameters();
			stmt.setLong(1, sequence);
			stmt.setString(2, channelBean.getChannelName());
			stmt.setString(3, channelBean.getChannelPath());
			stmt.setString(4, channelBean.getChannelType());
			stmt.setString(5, "1");
			stmt.setDate(6, this.getOperateTime());
			stmt.setInt(7, Integer.parseInt(channelBean.getOperator()));
		} catch (SQLException e) {
			e.printStackTrace();
		}

		int result = 0;
		try {
			result = stmt.executeUpdate();
			con.commit();
			// try {
			// //this.updateCommand(channelBean.getChannelType());
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		} catch (SQLException ex) {
			try {
				rollback();

				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public HashMap[] getAllChannelByOrgIDInit(String channelType, String orgID) {//初始化页面用

		// String orgIds =new AvailablePageDAO().getSuperOrgIdByOrgId(orgID);
		// if (orgIds == null) {
		// orgIds = "-1";
		// }
		String orgIds = orgID;
		logger.info("当前组织的组织list： " + orgIds);
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT CHANNEL_ID,CHANNEL_NAME, ");
		stringBuffer.append("	CHANNEL_URL,CHANNEL_TYPE, ");
		stringBuffer.append("	STATUS,OPERATETIME,OPERATOR,FLAG,ENTERPRISES_TYPE ");
		stringBuffer.append("	FROM TB_CCB_CHANNEL	 ");
		stringBuffer.append("	WHERE STATUS IN ('1','2') ");
		stringBuffer.append("   AND CHANNEL_TYPE = '");
		stringBuffer.append(channelType);
		stringBuffer.append("' ");
		stringBuffer.append("  AND org_id in ");
		stringBuffer.append("(" + orgIds + ")");
		stringBuffer.append(" ORDER BY operatetime ASC	");
		logger.info("被执行的sql语句是: " + stringBuffer.toString());
		HashMap[] map = null;
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.clearParameters();

			map = select();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}
		// if ( rs != null ) {
		// while ( rs.next() ) {
		// ChannelBean channelBean = new ChannelBean();
		// channelBean.setChannelId( rs.getLong( "CHANNEL_ID" ) );
		// channelBean.setChannelName( rs.getString( "CHANNEL_NAME" ) );
		// channelBean.setChannelPath( rs.getString( "CHANNEL_URL" ) );
		// channelBean.setChannelType( rs.getString( "CHANNEL_TYPE" ) );
		// channelBean.setOperator( rs.getString( "OPERATOR" ) );
		// channelBean.setStatus( rs.getString( "STATUS" ) );
		// list.add( channelBean );
		// }
		// }
	}

	
	public HashMap[] getAllChannelByOrgID(String channelType, String orgID) {

		// String orgIds =new AvailablePageDAO().getSuperOrgIdByOrgId(orgID);
		// if (orgIds == null) {
		// orgIds = "-1";
		// }
		String orgIds = orgID;
		logger.info("当前组织的组织list： " + orgIds);
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT CHANNEL_ID,CHANNEL_NAME, ");
		stringBuffer.append("	CHANNEL_URL,CHANNEL_TYPE, ");
		stringBuffer.append("	STATUS,OPERATETIME,OPERATOR,FLAG,ENTERPRISES_TYPE ");
		stringBuffer.append("	FROM TB_CCB_CHANNEL	 ");
		stringBuffer.append("	WHERE STATUS IN ('1') ");
		stringBuffer.append("   AND CHANNEL_TYPE = '");
		stringBuffer.append(channelType);
		stringBuffer.append("' ");
		stringBuffer.append("  AND org_id in ");
		stringBuffer.append("(" + orgIds + ")");
		stringBuffer.append(" ORDER BY operatetime ASC	");
		logger.info("被执行的sql语句是: " + stringBuffer.toString());
		HashMap[] map = null;
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.clearParameters();

			map = select();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}
		// if ( rs != null ) {
		// while ( rs.next() ) {
		// ChannelBean channelBean = new ChannelBean();
		// channelBean.setChannelId( rs.getLong( "CHANNEL_ID" ) );
		// channelBean.setChannelName( rs.getString( "CHANNEL_NAME" ) );
		// channelBean.setChannelPath( rs.getString( "CHANNEL_URL" ) );
		// channelBean.setChannelType( rs.getString( "CHANNEL_TYPE" ) );
		// channelBean.setOperator( rs.getString( "OPERATOR" ) );
		// channelBean.setStatus( rs.getString( "STATUS" ) );
		// list.add( channelBean );
		// }
		// }
	}

	// catch ( SQLException e ) {
	// e.printStackTrace();
	// }
	// finally {
	// try {
	// releaseConnection();
	// }
	// catch ( SQLException e ) {
	// e.printStackTrace();
	// }
	// }
	// return list;

	public List<ChannelBean> getAllChannelData(String userId, String channelType) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT CHANNEL_ID,CHANNEL_NAME,	");
		stringBuffer.append("	CHANNEL_URL,CHANNEL_TYPE,		");
		stringBuffer.append("	STATUS,OPERATETIME,OPERATOR		");
		stringBuffer.append("	FROM TB_CCB_CHANNEL				");
		stringBuffer.append("	WHERE STATUS IN ('1','2')       ");
		stringBuffer.append("  AND CHANNEL_TYPE = '");
		stringBuffer.append(channelType);
		stringBuffer.append("'									");
		stringBuffer.append("  AND OPERATOR = '");
		stringBuffer.append(userId);
		stringBuffer.append("'");
		stringBuffer.append("	ORDER BY CHANNEL_ID ASC	");

		ResultSet rs = null;
		List<ChannelBean> list = new ArrayList<ChannelBean>();
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ChannelBean channelBean = new ChannelBean();
					channelBean.setChannelId(rs.getLong("CHANNEL_ID"));
					channelBean.setChannelName(rs.getString("CHANNEL_NAME"));
					channelBean.setChannelPath(rs.getString("CHANNEL_URL"));
					channelBean.setChannelType(rs.getString("CHANNEL_TYPE"));
					channelBean.setOperator(rs.getString("OPERATOR"));
					channelBean.setStatus(rs.getString("STATUS"));
					list.add(channelBean);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<ChannelBean> searchChannelData(String channelName,
			String isEnabled, String userId, String channelType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT CHANNEL_ID,CHANNEL_NAME,	");
		stringBuffer.append("	CHANNEL_URL,CHANNEL_TYPE,		");
		stringBuffer.append("	STATUS,OPERATETIME,OPERATOR		");
		stringBuffer.append("	FROM TB_CCB_CHANNEL				");
		if (channelName != null) {
			stringBuffer.append("	WHERE CHANNEL_NAME LIKE       '%");
			stringBuffer.append(channelName);
			stringBuffer.append("%'");
		} else {
			stringBuffer.append("	WHERE 1=1 ");
		}
		if (isEnabled != null && !"".equals(isEnabled)
				&& !"0".equals(isEnabled)) {
			stringBuffer.append("	AND STATUS = '");
			stringBuffer.append(isEnabled);
			stringBuffer.append("'");
		} else {
			stringBuffer.append("	AND STATUS IN ('1','2')");
		}
		stringBuffer.append(" AND OPERATOR = 0");// TODO 先改成只有管理员有权限添加
		stringBuffer.append(" AND CHANNEL_TYPE = '");
		stringBuffer.append(channelType);
		stringBuffer.append("'							");
		stringBuffer.append("	ORDER BY CHANNEL_ID ASC 	");

		ResultSet rs = null;
		List<ChannelBean> list = new ArrayList<ChannelBean>();
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ChannelBean channelBean = new ChannelBean();
					channelBean.setChannelId(rs.getLong("CHANNEL_ID"));
					channelBean.setChannelName(rs.getString("CHANNEL_NAME"));
					channelBean.setChannelPath(rs.getString("CHANNEL_URL"));
					channelBean.setChannelType(rs.getString("CHANNEL_TYPE"));
					channelBean.setOperator(rs.getString("OPERATOR"));
					channelBean.setStatus(rs.getString("STATUS"));
					list.add(channelBean);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public boolean deleteChannelByID(String flag, String orgID) {
		String orgValues = new AvailablePageDAO().getSubOrgById(String
				.valueOf(orgID));
		if ("".equals(orgValues)) {
			orgValues = orgID;
		} else {
			orgValues = orgValues + "," + orgID;
		}

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	DELETE FROM TB_CCB_CHANNEL ");
		stringBuffer.append("	WHERE FLAG  IN (");
		stringBuffer.append(flag);
		stringBuffer.append(")	AND ORG_ID IN (");
		stringBuffer.append(orgValues);
		stringBuffer.append(")");
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {

			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.executeUpdate();
			con.commit();

			// @SuppressWarnings("rawtypes")
			// HashMap[] map = this.getAllChannelType(channelIds);

			return true;
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public boolean deleteChannel(String channelIds) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	UPDATE TB_CCB_CHANNEL		");
		stringBuffer.append("	SET STATUS = '3'			");
		stringBuffer.append("	WHERE CHANNEL_ID  IN 		   (");
		stringBuffer.append(channelIds);
		stringBuffer.append(")									");
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {

			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.executeUpdate();
			con.commit();

			// @SuppressWarnings("rawtypes")
			// HashMap[] map = this.getAllChannelType(channelIds);
			//
			// for (int i = 0; i < map.length; i++) {
			// String channelType = (String) map[i].get("CHANNEL_TYPE");
			// this.updateCommand(channelType);
			// }

			// TODO 判断是否全部删除成功
			return true;
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
			}
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public int stopChannel(String flag) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	UPDATE TB_CCB_CHANNEL		");
		stringBuffer.append("	SET STATUS = '2'			");
		stringBuffer.append("	WHERE FLAG  IN  	   (");
		stringBuffer.append(flag);
		stringBuffer.append(")");
		int result = 0;
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {

			stmt = con.prepareStatement(stringBuffer.toString());
			result = stmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 启用
	 * @param allOpenChannelId
	 * @return
	 */
	public int openChannel(String flag) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	UPDATE TB_CCB_CHANNEL		");
		stringBuffer.append("	SET STATUS = '1'			");
		stringBuffer.append("	WHERE FLAG  IN  	   (");
		stringBuffer.append(flag);
		stringBuffer.append(")");
		logger.info("开启频道使用的sql语句是:"+stringBuffer.toString());
		int result = 0;
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			result = stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int stopChannel(String flag, String orgID) {
		String orgValues = new AvailablePageDAO().getSubOrgById(String
				.valueOf(orgID));
		if ("".equals(orgValues)) {
			orgValues = orgID;
		} else {
			orgValues = orgValues + "," + orgID;
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	UPDATE TB_CCB_CHANNEL		");
		stringBuffer.append("	SET STATUS = '2'			");
		stringBuffer.append("	WHERE FLAG  IN  	   (");
		stringBuffer.append(flag);
		stringBuffer.append(")	AND ORG_ID IN (");
		stringBuffer.append(orgValues);
		stringBuffer.append(")");
		int result = 0;
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {

			stmt = con.prepareStatement(stringBuffer.toString());
			result = stmt.executeUpdate();
			con.commit();

		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
			}
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 启用
	 * @param allOpenChannelId
	 * @return
	 */
	public int openChannel(String flag, String orgID) {
		String orgValues = new AvailablePageDAO().getSubOrgById(String
				.valueOf(orgID));
		if ("".equals(orgValues)) {
			orgValues = orgID;
		} else {
			orgValues = orgValues + "," + orgID;
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	UPDATE TB_CCB_CHANNEL		");
		stringBuffer.append("	SET STATUS = '1'			");
		stringBuffer.append("	WHERE FLAG  IN  	   (");
		stringBuffer.append(flag);
		stringBuffer.append(")	AND ORG_ID IN (");
		stringBuffer.append(orgValues);
		stringBuffer.append(")");
		logger.info("开启频道使用的sql语句是:"+stringBuffer.toString());
		int result = 0;
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			result = stmt.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public java.sql.Date getOperateTime() {

		java.util.Date utilDate = new java.util.Date();
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

		return sqlDate;
	}

	public List<ChannelBean> pageChannel(String pageType, PageBean pageBean,
			String userId, String isEnabled, String channelType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT CHANNEL_ID,CHANNEL_NAME,	");
		stringBuffer.append("	CHANNEL_URL,CHANNEL_TYPE,		");
		stringBuffer.append("	STATUS,OPERATETIME,OPERATOR		");
		stringBuffer.append("	FROM TB_CCB_CHANNEL				");
		stringBuffer.append("  WHERE OPERATOR IN ('");
		stringBuffer.append(userId);
		stringBuffer.append("','0')");
		if (!"0".equals(isEnabled)) {
			stringBuffer.append("	AND STATUS ='");
			stringBuffer.append(isEnabled);
			stringBuffer.append("'				");
		} else {
			stringBuffer.append("	AND STATUS IN('1','2')	");
		}
		stringBuffer.append(" AND CHANNEL_TYPE = '");
		stringBuffer.append(channelType);
		stringBuffer.append("'						");
		stringBuffer.append("	ORDER BY CHANNEL_ID ASC ");
		ResultSet rs = null;
		List<ChannelBean> list = new ArrayList<ChannelBean>();
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ChannelBean channelBean = new ChannelBean();
					channelBean.setChannelId(rs.getLong("CHANNEL_ID"));
					channelBean.setChannelName(rs.getString("CHANNEL_NAME"));
					channelBean.setChannelPath(rs.getString("CHANNEL_URL"));
					channelBean.setChannelType(rs.getString("CHANNEL_TYPE"));
					channelBean.setOperator(rs.getString("OPERATOR"));
					channelBean.setStatus(rs.getString("STATUS"));
					list.add(channelBean);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		pageBean.setInfoCount(list.size());

		int totalPageNum = (list.size() + pageBean.getPageSize() - 1)
				/ pageBean.getPageSize();

		pageBean.setPageCount(totalPageNum);

		if ("first".equals(pageType)) {
			if (list.size() >= 10) {
				list = list.subList(0, 10);
			} else {
				list = list.subList(0, list.size());
			}
		} else if ("next".equals(pageType)) {
			if (pageBean.getNextPageNo() == list.size()
					/ pageBean.getPageSize()) {
				if (pageBean.getCurrentPage() == pageBean.getNextPageNo()) {
					list = list.subList((pageBean.getCurrentPage() - 1)
							* pageBean.getPageSize(), list.size());
				} else {
					list = list.subList(
							pageBean.getCurrentPage() * pageBean.getPageSize(),
							list.size());
				}
			} else if (list.size() < pageBean.getNextPageNo()
					* pageBean.getPageSize()) {
				if (pageBean.getPageCount() == pageBean.getNextPageNo()) {
					list = list.subList((pageBean.getPageCount() - 1)
							* pageBean.getPageSize(), list.size());
				} else {
					list = list.subList(
							pageBean.getNextPageNo() * pageBean.getPageSize(),
							list.size());
				}
			} else {
				list = list.subList(
						pageBean.getCurrentPage() * pageBean.getPageSize(),
						pageBean.getNextPageNo() * pageBean.getPageSize());
			}
		} else if ("previous".equals(pageType)) {
			if (pageBean.getCurrentPage() <= 1) {
				if (list.size() >= 10) {
					list = list.subList(0, 10);
				} else {
					list = list.subList(0, list.size());
				}
			} else {
				list = list.subList((pageBean.getPreviousPageNo() - 1)
						* pageBean.getPageSize(), pageBean.getPreviousPageNo()
						* pageBean.getPageSize());
			}
		} else if ("last".equals(pageType)) {
			if (pageBean.getPageCount() == pageBean.getNextPageNo()) {
				list = list.subList(
						(pageBean.getCurrentPage() - 1)
								* pageBean.getPageSize(),
						pageBean.getInfoCount());
			} else {
				list = list.subList(list.size() / pageBean.getPageSize()
						* pageBean.getPageSize(), list.size());
			}
		} else {
			if (pageBean.getCurrentPage() * pageBean.getPageSize() >= pageBean
					.getInfoCount()) {
				list = list.subList(
						pageBean.getPreviousPageNo() * pageBean.getPageSize(),
						pageBean.getInfoCount());
			} else {
				list = list.subList(
						(pageBean.getCurrentPage() - 1)
								* pageBean.getPageSize(),
						pageBean.getCurrentPage() * pageBean.getPageSize());
			}

		}
		return list;
	}

	public ChannelBean editChannel(ChannelBean channelBean) {
		ResultSet rs = null;
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT CHANNEL_ID,CHANNEL_NAME,	");
		stringBuffer.append("	CHANNEL_URL,CHANNEL_TYPE,		");
		stringBuffer.append("	STATUS,OPERATETIME,OPERATOR		");
		stringBuffer.append("	FROM TB_CCB_CHANNEL				");
		stringBuffer.append("	WHERE CHANNEL_ID  = ?			");
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {

			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.clearParameters();
			stmt.setLong(1, channelBean.getChannelId());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		try {
			rs = stmt.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					channelBean.setChannelName(rs.getString("CHANNEL_NAME"));
					channelBean.setChannelPath(rs.getString("CHANNEL_URL"));
					channelBean.setChannelType(rs.getString("CHANNEL_TYPE"));
					channelBean.setOperator(rs.getString("OPERATOR"));
					channelBean.setStatus(rs.getString("STATUS"));
				}
			}

		} catch (SQLException ex) {
			try {
				rollback();

				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return channelBean;
	}

	public int saveEditChannel(ChannelBean channelBean) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	UPDATE TB_CCB_CHANNEL SET	");
		stringBuffer.append("	CHANNEL_NAME = ?,	");
		stringBuffer.append("	CHANNEL_URL = ?,			");
		stringBuffer.append("	CHANNEL_TYPE = ?,			");
		stringBuffer.append("	STATUS = ?,					");
		stringBuffer.append("	OPERATETIME = ?,			");
		stringBuffer.append("	OPERATOR = ?				");
		stringBuffer.append("	WHERE CHANNEL_ID = ?		");

		try {
			connection();
			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.clearParameters();
			stmt.setString(1, channelBean.getChannelName());
			stmt.setString(2, channelBean.getChannelPath());
			stmt.setString(3, channelBean.getChannelType());
			stmt.setString(4, "1");
			stmt.setDate(5, this.getOperateTime());
			stmt.setInt(6, Integer.parseInt(channelBean.getOperator()));
			stmt.setLong(7, channelBean.getChannelId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int result = 0;
		try {
			result = stmt.executeUpdate();
			con.commit();

			// try {
			// this.updateCommand(channelBean.getChannelType());
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
		} catch (SQLException ex) {
			try {
				rollback();
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public int checkChannelName(String channelName) {
		ResultSet rs = null;
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT	");
		stringBuffer.append("		COUNT (1)	");
		stringBuffer.append("	FROM	");
		stringBuffer.append("		TB_CCB_CHANNEL	");
		stringBuffer.append("	WHERE	");
		stringBuffer.append("		CHANNEL_NAME = 	'");
		stringBuffer.append(channelName);
		stringBuffer.append("'");
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		int result = 0;
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					result = rs.getInt(1);
				}
			}
		} catch (SQLException ex) {
			try {
				rollback();
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public List<ChannelBean> getAllChannelDataTranslate(String channelType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT CHANNEL_ID,CHANNEL_NAME,	");
		stringBuffer.append("	CHANNEL_URL						");
		stringBuffer.append("	FROM TB_CCB_CHANNEL				");
		stringBuffer.append("	WHERE STATUS =  '1'			    ");
		stringBuffer.append("	AND  CHANNEL_TYPE =  		   '");
		stringBuffer.append(channelType);
		stringBuffer.append("'");

		ResultSet rs = null;
		List<ChannelBean> list = new ArrayList<ChannelBean>();
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ChannelBean channelBean = new ChannelBean();
					channelBean.setChannelId(rs.getLong("CHANNEL_ID"));
					channelBean.setChannelName(rs.getString("CHANNEL_NAME"));
					channelBean.setChannelPath(rs.getString("CHANNEL_URL"));
					list.add(channelBean);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public HashMap[] getAllMachineId() throws SQLException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT MAC,MACHINE_ID FROM TB_CCB_MACHINE 	");
		ResultSet rs = null;
		if (con == null) {
			connection();
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			return select();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public HashMap[] getAllChannelType(String channelIds) throws SQLException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer
				.append("	SELECT CHANNEL_TYPE FROM TB_CCB_CHANNEL WHERE CHANNEL_ID IN (");
		stringBuffer.append(channelIds);
		stringBuffer.append(")");
		if (con == null) {
			connection();
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			return select();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 更新指令、生成文件
	 * 
	 * @param channelType
	 *            0-频道 1-快速入口
	 * @throws IOException
	 * @throws SQLException
	 */
	public void updateCommand(String channelType, HashMap[] mapList)
			throws IOException, SQLException {

		// List<ChannelBean> list =
		// this.getAllChannelDataTranslate(channelType);

		String suffix = "";
		String updateChannelListFilePath = "";
		int commandId = 19;
		try {
			suffix = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");
			if ("0".equals(channelType)) {
				// 频道
				updateChannelListFilePath = LocalProperties
						.getProperty("FTP_DOWNLOAD_FILE_PATH_CHANNEL");
			} else if ("1".equals(channelType)) {
				// 快速入口
				updateChannelListFilePath = LocalProperties
						.getProperty("FTP_DOWNLOAD_FILE_PATH_SHORTCUT");
				commandId = 20;
			}
		} catch (LocalPropertiesException e) {
			e.printStackTrace();
		}

		for (int i = 0; i < mapList.length; i++) {
			String mac = (String) mapList[i].get("MAC");
			long machineId = Long.parseLong((String) mapList[i]
					.get("MACHINE_ID"));

			this.uploadChannlListFileToFTP(mac, updateChannelListFilePath,
					channelType);

			// 修改指令表
			String commandContent = FileTools.buildFullFilePath(
					updateChannelListFilePath, mac + suffix);

			this.updateCommandManagement(machineId, "1", commandContent,
					commandId);

		}

	}

	/**
	 * 更新指令、生成文件
	 * 
	 * @param {String} channelType
	 * @param {long} machineId
	 * @throws IOException
	 * @throws SQLException
	 */
	public void updateCommand(String channelType, long machineId)
			throws IOException, SQLException {

		List<ChannelBean> list = this.getAllChannelDataTranslate(channelType);

		String suffix = "";
		String updateChannelListFilePath = "";
		int commandId = 19;
		try {
			suffix = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");
			if ("0".equals(channelType)) {
				// 频道
				updateChannelListFilePath = LocalProperties
						.getProperty("FTP_DOWNLOAD_FILE_PATH_CHANNEL");
			} else if ("1".equals(channelType)) {
				// 快速入口
				updateChannelListFilePath = LocalProperties
						.getProperty("FTP_DOWNLOAD_FILE_PATH_SHORTCUT");
				commandId = 20;
			}
		} catch (LocalPropertiesException e) {
			e.printStackTrace();
		}

		String mac = this.getMacByMachineId(machineId);

		this.uploadChannlListFileToFTP(mac, updateChannelListFilePath,
				channelType);

		// 修改指令表
		String commandContent = FileTools.buildFullFilePath(
				updateChannelListFilePath, mac + suffix);

		this.updateCommandManagement(machineId, "1", commandContent, commandId);

	}

	public String getMacByMachineId(long machineId) throws SQLException {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT MAC FROM TB_CCB_MACHINE 	");
		stringBuffer.append("  WHERE MACHINE_ID = '");
		stringBuffer.append(machineId);
		stringBuffer.append("'");
		if (con == null) {
			connection();
		}
		String mac = "";
		ResultSet rs = null;
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					mac = rs.getString("MAC");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return mac;
	}

	public int updateCommandManagement(long machineID, String status,
			String commandContent, int commandID) {
		String FUNCTION_NAME = "updateCommandManagement() ";
		logger.info(FUNCTION_NAME + "start");

		int ret = 0;

		StringBuffer sb = new StringBuffer("");

		sb.append("UPDATE tb_command_management ");
		sb.append("SET status=?, command_content=?, operatetime=now() ");
		sb.append("WHERE command_id=? AND machine_id=? ");

		// SQL文ログ出し
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		logger.info(FUNCTION_NAME + "pl.machineID = " + machineID);
		logger.info(FUNCTION_NAME + "pl.status = " + status);
		logger.info(FUNCTION_NAME + "pl.commandID = " + commandID);
		logger.info(FUNCTION_NAME + "pl.commandContent = " + commandContent);

		try {
			if (con == null) {
				connection();
			}

			stmt = con.prepareStatement(sb.toString());
			stmt.setString(1, status);
			stmt.setString(2, commandContent);
			stmt.setInt(3, commandID);
			stmt.setLong(4, machineID);

			ret = stmt.executeUpdate();
			commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
			logger.info(FUNCTION_NAME + "end");

		}
		return ret;
	}

	/**
	 * STEP2.2: 向FTP上传频道文件
	 * 
	 * @param mac
	 * @param updatechannelListFilePath
	 * @param channelList
	 * @return
	 */

	private void uploadChannlListFileToFTP(String mac,
			String channelListFileFTPPath, String channleType)
			throws IOException {

		String FUNCTION_NAME = "uploadChannlListFileToFTP";

		// 系统配置的路径如 C:/tmp/channl
		String macIniLocalTmpPath = LocalProperties
				.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_CHANNEL");
		String suffix = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");
		// 当前用户的临时文件夹,执行完成后会自动删除 如 1377321668371
		String tmpDir = String.valueOf(System.currentTimeMillis());
		// 临时目录 如 C:/tmp/channl/1377321668371
		String tmpeFilePath = FileTools.buildFullFilePath(macIniLocalTmpPath,
				tmpDir);
		logger.info(FUNCTION_NAME + "频道文件本地临时目录: " + macIniLocalTmpPath);
		logger.info(FUNCTION_NAME + "频道文件后缀: " + suffix);

		// STEP1: 生成对应的临时目录,如 C:/tmp/channl/1377321668371
		FileTools.mkdirs(tmpeFilePath);

		// STEP1x: 得到当前机器的channeID list
		List<ChannelBean> channelList = null;
		try {
			channelList = this.getChannelListByMac(mac, channleType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// STEP2: 向ini文件中写入数据,输出目录为: 如 C:/tmp/1377321668371
		logger.info("获得到的channel list的条数是："+channelList.size());
		writePackageInfoToIniFile(channelList,
				FileTools.buildFullFilePath(tmpeFilePath, mac + suffix));

		// STEP3: 上传文件列表,读取路径如 C:/tmp/1377321668371
		FtpUtils.uploadFile(FileTools.getSubFiles(tmpeFilePath),
				channelListFileFTPPath);

		// STEP4: 移除临时文件,如 C:/tmp/1377321668371下面所有文件
		FileTools.removeSubFiles(tmpeFilePath);
		// STEP5: 删除临时文件夹
		FileTools.removeDirByDirName(macIniLocalTmpPath, tmpDir);

	}

	/**
	 * 向Mac.ini文件中写入信息.每行用回车转行.新生成的文件保存至path目录中.
	 * 
	 * <pre>
	 * Desc  
	 * @param packagedsArray
	 * @param path
	 * @return
	 * @throws IOException
	 * @author PSET
	 * @refactor PSET
	 * @date   2013年8月20日 下午2:59:25
	 * </pre>
	 */
	private void writePackageInfoToIniFile(List channelList, String path)
			throws IOException {
		String FUNCTION_NAME = "writePackageInfoToIniFile";
		logger.info(FUNCTION_NAME + " 临时文件目录: " + path);
		// FileWriter fw = new FileWriter( new File( path ) );
		// BufferedWriter bw = new BufferedWriter( fw );
		OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
				path), "UTF-8");
		for (int i = 0; i < channelList.size(); i++) {

			ChannelBean channelBean = (ChannelBean) channelList.get(i);
			Long channelId = channelBean.getChannelId();
			String channelName = channelBean.getChannelName();
			String channelPath = channelBean.getChannelPath();
			// bw.write( channelId + "," + channelName + "," + channelPath );
			// bw.newLine();
			String data = channelId + "," + channelName + "," + channelPath
					+ "\t\n";
			out.write(data);
			out.flush();
		}
		out.close();
		// bw.close();
		// fw.close();
	}

	public List<ChannelBean> getReportData(String channelName,
			String isEnabled, String channelType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("	SELECT	");
		stringBuffer.append("		T1.CHANNEL_ID,");
		stringBuffer.append("		T1.CHANNEL_NAME,");
		stringBuffer.append("		T1.CHANNEL_URL,");
		stringBuffer.append("		T1.CHANNEL_TYPE,");
		stringBuffer.append("		T1.STATUS,");
		stringBuffer.append("		T1.OPERATETIME,");
		stringBuffer.append("		T2.REAL_NAME");
		stringBuffer.append("	FROM");
		stringBuffer.append("		TB_CCB_CHANNEL T1,TB_CCB_USER T2	");
		stringBuffer.append("	WHERE T1.OPERATOR = T2.USER_ID 	");
		if (null != channelName && !"".equals(channelName)) {
			stringBuffer.append("	 AND T1.CHANNEL_NAME LIKE '%");
			stringBuffer.append(channelName);
			stringBuffer.append("%'");
		}
		if (!"0".equals(isEnabled)) {
			stringBuffer.append(" AND T1.STATUS = '");
			stringBuffer.append(isEnabled);
			stringBuffer.append("'");
		} else {
			stringBuffer.append(" AND T1.STATUS IN  ('1','2') ");
		}
		stringBuffer.append(" AND T1.CHANNEL_TYPE = '");
		stringBuffer.append(channelType);
		stringBuffer.append("'							");
		stringBuffer.append("	ORDER BY T1.CHANNEL_ID asc 	");
		ResultSet rs = null;
		List<ChannelBean> list = new ArrayList<ChannelBean>();
		if (con == null) {
			try {
				connection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement(stringBuffer.toString());
			rs = stmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					ChannelBean channelBean = new ChannelBean();
					channelBean.setChannelId(rs.getLong("CHANNEL_ID"));
					channelBean.setChannelName(rs.getString("CHANNEL_NAME"));
					channelBean.setChannelPath(rs.getString("CHANNEL_URL"));
					channelBean.setOperator(rs.getString("REAL_NAME"));

					String status = rs.getString("STATUS");
					if ("1".equals(status)) {
						channelBean.setStatus("启用");
					} else if ("2".equals(status)) {
						channelBean.setStatus("停用");
					}
					list.add(channelBean);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<ChannelBean> getChannelListByMac(String mac, String type)
			throws SQLException {
		List<ChannelBean> list = new ArrayList<ChannelBean>();

		try {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("SELECT CHANNEL_ID,CHANNEL_NAME, ");
			stringBuffer.append("CHANNEL_URL,CHANNEL_TYPE, ");
			stringBuffer.append("STATUS,OPERATETIME,OPERATOR,FLAG ");
			stringBuffer.append("FROM TB_CCB_CHANNEL	 ");
			stringBuffer.append("WHERE channel_type = ?");
			stringBuffer
					.append(" AND STATUS='1' AND org_id in (SELECT org_id from tb_ccb_machine where mac=? ) ORDER BY OPERATETIME ASC");
			logger.info("将要被执行的sql语句是: " + stringBuffer.toString());
			if (con == null)
				connection();

			stmt = con.prepareStatement(stringBuffer.toString());
			stmt.clearParameters();
			stmt.setString(1, type);
			stmt.setString(2, mac);
			HashMap[] map = select();
			for (int i = 0; i < map.length; i++) {
				ChannelBean channelBean = new ChannelBean();
				channelBean.setChannelId(Long.parseLong(map[i]
						.get("CHANNEL_ID").toString()));
				channelBean.setChannelName(map[i].get("CHANNEL_NAME")
						.toString());
				channelBean
						.setChannelPath(map[i].get("CHANNEL_URL").toString());
				list.add(channelBean);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			releaseConnection();

		}
		return list;

	}
	public List<ChannelBean> getChannelListByMacByQuick(String mac, String type)
	throws SQLException {
List<ChannelBean> list = new ArrayList<ChannelBean>();

try {
	StringBuffer stringBuffer = new StringBuffer();
	stringBuffer.append("SELECT CHANNEL_ID,CHANNEL_NAME, ");
	stringBuffer.append("CHANNEL_URL,CHANNEL_TYPE, ");
	stringBuffer.append("STATUS,OPERATETIME,OPERATOR,FLAG ");
	stringBuffer.append("FROM TB_CCB_CHANNEL	 ");
	stringBuffer.append("WHERE channel_type = ?");
	stringBuffer
			.append(" AND STATUS in ('1','2') AND org_id in (SELECT org_id from tb_ccb_machine where mac=? )");
	logger.info("将要被执行的sql语句是: " + stringBuffer.toString());
	if (con == null)
		connection();

	stmt = con.prepareStatement(stringBuffer.toString());
	stmt.clearParameters();
	stmt.setString(1, type);
	stmt.setString(2, mac);
	HashMap[] map = select();
	for (int i = 0; i < map.length; i++) {
		ChannelBean channelBean = new ChannelBean();
		channelBean.setChannelId(Long.parseLong(map[i]
				.get("CHANNEL_ID").toString()));
		channelBean.setChannelName(map[i].get("CHANNEL_NAME")
				.toString());
		channelBean
				.setChannelPath(map[i].get("CHANNEL_URL").toString());
		list.add(channelBean);
	}
} catch (Exception ex) {
	ex.printStackTrace();
} finally {
	releaseConnection();

}
return list;

}

}
