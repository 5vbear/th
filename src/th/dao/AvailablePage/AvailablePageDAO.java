/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao.AvailablePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

import th.com.util.Define;
import th.dao.BaseDao;
import th.entity.AvailablePageBean;

/**
 * Descriptions
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class AvailablePageDAO extends BaseDao {
	protected Logger logger = Logger.getLogger(this.getClass());

	public AvailablePageDAO() {

	}

	/**
	 * 获取白名单列表
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public HashMap[] getAvailableList(String orgID) throws SQLException {

		String FUNCTION_NAME = "getAvailableList() ";
		logger.info(FUNCTION_NAME + "start");
		// String orgIds = getSuperOrgIdByOrgId(orgID);
		String orgIds = orgID;
		if (orgIds == null) {
			orgIds = "-1";
		}
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select REQUEST_ID,REQUEST_NAME,REQUEST_URL,ORG_ID,OPERATETIME,OPERATOR,FLAG from TB_REQUEST_MANAGEMENT where STATUS='1' AND ORG_ID in("
					+ orgIds + ") ORDER BY REQUEST_ID DESC");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	public HashMap[] getSearchInfoByNameUrl(String channelName,String channelUrl,String orgId,String channelType) throws SQLException{
		String FUNCTION_NAME = "getSearchInfoByNameUrl() ";
		logger.info(FUNCTION_NAME + "start");
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select channel_id as REQUEST_ID,flag as FLAG,channel_name as REQUEST_NAME,channel_url as REQUEST_URL,enterprises_type,status from TB_CCB_CHANNEL where STATUS IN ('1','2') AND ORG_ID = '"+ orgId +"'"  );
			sb.append( " AND CHANNEL_TYPE = '0'");
			if(!"".equals( channelName )){
				sb.append( " AND channel_name like '%"+ channelName+"%'" );
			}
		    if(!"null".equals( channelUrl )){
		    	sb.append( " AND channel_url like '%"+channelUrl+"%'" );
		    }
		    sb.append(" ORDER BY operatetime ASC");
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();
			
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
	}

	public HashMap[] getChannelList(String orgID) throws SQLException {// 获取频道列表

		String FUNCTION_NAME = "getAvailableList() ";
		logger.info(FUNCTION_NAME + "start");
		String orgIds = getSuperOrgIdByOrgId(orgID);
		if (orgIds == null) {
			orgIds = "-1";
		}
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select REQUEST_ID,REQUEST_NAME,REQUEST_URL,ORG_ID,OPERATETIME,OPERATOR,FLAG from TB_REQUEST_MANAGEMENT where STATUS='1' AND ORG_ID in("
					+ orgIds + ") ORDER BY REQUEST_ID DESC");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	public HashMap[] getQuickList(String orgID) throws SQLException {// 获取快速入口列表

		String FUNCTION_NAME = "getAvailableList() ";
		logger.info(FUNCTION_NAME + "start");
		String orgIds = getSuperOrgIdByOrgId(orgID);
		if (orgIds == null) {
			orgIds = "-1";
		}
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select REQUEST_ID,REQUEST_NAME,REQUEST_URL,ORG_ID,OPERATETIME,OPERATOR,FLAG from TB_REQUEST_MANAGEMENT where STATUS='1' AND ORG_ID in("
					+ orgIds + ") ORDER BY REQUEST_ID DESC");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/**
	 * 获取企业文档管理列表
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public HashMap[] getDocumentList(String orgID) throws SQLException {

		String FUNCTION_NAME = "getDocumentList() ";
		logger.info(FUNCTION_NAME + "start");
		// String orgIds = getSuperOrgIdByOrgId(orgID);
		String orgIds = orgID;
		if (orgIds == null) {
			orgIds = "-1";
		}
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select DOCUMENT_ID,DOCUMENT_NAME,DOCUMENT,ORG_ID,OPERATETIME,OPERATOR from TB_DOCUMENT_MANAGEMENT where STATUS='1' AND ORG_ID in("
					+ orgIds + ") ORDER BY DOCUMENT_ID DESC");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/**
	 * 获取企业文档管理列表
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public HashMap[] getDocumentById(String documentId) throws SQLException {

		String FUNCTION_NAME = "getDocumentById() ";
		logger.info(FUNCTION_NAME + "start");

		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select DOCUMENT_ID,DOCUMENT_NAME,DOCUMENT,ORG_ID,OPERATETIME,OPERATOR from TB_DOCUMENT_MANAGEMENT where DOCUMENT_ID ="+documentId);

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	/**
	 * 获取白名单列表
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public HashMap[] getSubAvailableList(String orgID) throws SQLException {

		String FUNCTION_NAME = "getAvailableList() ";
		logger.info(FUNCTION_NAME + "start");
		String orgIds = getSubOrg(orgID);
		if (orgIds == null) {
			orgIds = "-1";
		}
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select REQUEST_ID,REQUEST_NAME,REQUEST_URL,ORG_ID,OPERATETIME,OPERATOR from TB_REQUEST_MANAGEMENT where STATUS='1' AND ORG_ID in("
					+ orgIds + ") ORDER BY REQUEST_ID DESC");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/**
	 * 通过机器ID获取组织ID
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public String getOrgIdBymacId(String macId) throws SQLException {
		String FUNCTION_NAME = "getOrgIdBymacId() ";
		String orgID = "";
		try {

			StringBuffer sb = new StringBuffer();
			sb.append("select ORG_ID from TB_CCB_MACHINE where STATUS <>'3' AND MACHINE_ID ="
					+ macId);

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();
			orgID = (String) map[0].get("ORG_ID");
			return orgID;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/**
	 * 获取白名单列表
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public HashMap[] getAvailableListByRequestId(String requestId)
			throws SQLException {

		String FUNCTION_NAME = "getAvailableList() ";
		logger.info(FUNCTION_NAME + "start");

		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select REQUEST_ID,REQUEST_NAME,REQUEST_URL,ORG_ID,OPERATETIME,OPERATOR from TB_REQUEST_MANAGEMENT where STATUS='1' AND REQUEST_ID=? ORDER BY REQUEST_ID DESC");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(requestId));

			HashMap[] map = select();

			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	public long selectFlag() throws SQLException {

		try {
			StringBuffer sb = new StringBuffer();
			sb.append("select nextval( 'SEQ_tb_ccb_channel') as FLAG ");

			logger.info("要执行的sql语句是: " + sb.toString());

			if (con == null)
				connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			HashMap[] map = select();
			return Long.parseLong(map[0].get("FLAG").toString());

		} finally {
			releaseConnection();

		}
	}

	/**
	 * 媒体素材添加
	 * 
	 * @param bean
	 * @return
	 */
	public int insertAvailablePage(AvailablePageBean bean) throws SQLException {

		List orgList = this.getSubOrgList(bean.getOrgId() + "");
		orgList.add(bean.getOrgId());
		Long flag = selectFlag();
		for (int i = 0; i < orgList.size(); i++) {
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("INSERT INTO TB_REQUEST_MANAGEMENT( ");
			stringBuffer.append(" REQUEST_ID, ");
			stringBuffer.append(" REQUEST_NAME,");
			stringBuffer.append(" REQUEST_URL,");
			stringBuffer.append(" STATUS,");
			stringBuffer.append(" ORG_ID,FLAG, ");
			stringBuffer.append(" OPERATETIME,");
			stringBuffer.append(" OPERATOR)");

			stringBuffer
					.append(" VALUES(nextval( 'SEQ_TB_REQUEST_MANAGEMENT' ),?,?,?,?,?,?,?)");
			logger.info("将要执行的sql语句是: " + stringBuffer.toString());
			if (con == null) {
				try {
					connection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			int result = -1;
			try {
				stmt = con.prepareStatement(stringBuffer.toString());
				stmt.clearParameters();
				// 白名单内容

				stmt.setString(1, bean.getRequestName());
				stmt.setString(2, bean.getRequestURL());
				stmt.setString(3, Define.AVAILABLE_PAGE_TYPE_ADD);
				stmt.setLong(4, Long.parseLong(orgList.get(i).toString()));
				stmt.setLong(5, flag);
				stmt.setTimestamp(6, new Timestamp(new Date().getTime()));
				stmt.setLong(7, bean.getOperator());

				result = insert();

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
			}
		}
		return 0;
	}

	
	/**
	 * 企业文档管理添加
	 * 
	 * @param bean
	 * @return
	 */
	public int downDocument(AvailablePageBean bean) throws SQLException {

		List orgList = this.getSubOrgList(bean.getOrgId() + "");
		Long flag = selectFlag();
		for (int i = 0; i < orgList.size(); i++) {
			if("".equals(orgList.get(i))){
				continue;
			}
			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("INSERT INTO TB_DOCUMENT_MANAGEMENT( ");
			stringBuffer.append(" DOCUMENT_ID, ");
			stringBuffer.append(" DOCUMENT_NAME,");
			stringBuffer.append(" DOCUMENT,");
			stringBuffer.append(" STATUS,");
			stringBuffer.append(" ORG_ID,");
			stringBuffer.append(" OPERATETIME,");
			stringBuffer.append(" OPERATOR)");

			stringBuffer
					.append(" VALUES(nextval( 'SEQ_TB_DOCUMENT_MANAGEMENT' ),?,?,?,?,?,?)");
			logger.info("将要执行的sql语句是: " + stringBuffer.toString());
			if (con == null) {
				try {
					connection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			int result = -1;
			try {
				stmt = con.prepareStatement(stringBuffer.toString());
				stmt.clearParameters();
				// 白名单内容

				stmt.setString(1, bean.getRequestName());
				stmt.setString(2, bean.getRequestURL());
				stmt.setString(3, Define.AVAILABLE_PAGE_TYPE_ADD);
				stmt.setLong(4, Long.parseLong(orgList.get(i).toString()));
				stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
				stmt.setLong(6, bean.getOperator());

				result = insert();

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
			}
		}
		return 0;
	}
	
	/**
	 * 企业文档下发
	 * 
	 * @param bean
	 * @return
	 */
	public int insertDocument(AvailablePageBean bean) throws SQLException {

		List orgList = this.getSubOrgList(bean.getOrgId() + "");
		orgList.add(bean.getOrgId());
		Long flag = selectFlag();

			StringBuffer stringBuffer = new StringBuffer();
			stringBuffer.append("INSERT INTO TB_DOCUMENT_MANAGEMENT( ");
			stringBuffer.append(" DOCUMENT_ID, ");
			stringBuffer.append(" DOCUMENT_NAME,");
			stringBuffer.append(" DOCUMENT,");
			stringBuffer.append(" STATUS,");
			stringBuffer.append(" ORG_ID,");
			stringBuffer.append(" OPERATETIME,");
			stringBuffer.append(" OPERATOR)");

			stringBuffer
					.append(" VALUES(nextval( 'SEQ_TB_DOCUMENT_MANAGEMENT' ),?,?,?,?,?,?)");
			logger.info("将要执行的sql语句是: " + stringBuffer.toString());
			if (con == null) {
				try {
					connection();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			int result = -1;
			try {
				stmt = con.prepareStatement(stringBuffer.toString());
				stmt.clearParameters();
				// 白名单内容

				stmt.setString(1, bean.getRequestName());
				stmt.setString(2, bean.getRequestURL());
				stmt.setString(3, Define.AVAILABLE_PAGE_TYPE_ADD);
				stmt.setLong(4, bean.getOrgId());
				stmt.setTimestamp(5, new Timestamp(new Date().getTime()));
				stmt.setLong(6, bean.getOperator());

				result = insert();

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
			}
		
		return 0;
	}
	
	/**
	 * 白名单删除
	 * 
	 * @param bean
	 * @return
	 */
	public int deleteAvailables(String flag, String orgID) throws SQLException {
		StringBuffer stringBuffer = new StringBuffer();
		String orgValues = getSubOrgById(String.valueOf(orgID));
		if ("".equals(orgValues)) {
			orgValues = orgID;
		} else {
			orgValues = orgValues + "," + orgID;
		}
		stringBuffer
				.append("UPDATE TB_REQUEST_MANAGEMENT SET STATUS='3' WHERE FLAG IN ("
						+ flag + ") AND org_id in (" + orgValues + ")");
		logger.info("将要执行的sql语句是: " + stringBuffer.toString());
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

			// DB更新
			result = update();
			commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	/**
	 * 企业文档管理删除
	 * 
	 * @param bean
	 * @return
	 */
	public int deleteDocument(String flag, String orgID) throws SQLException {
		StringBuffer stringBuffer = new StringBuffer();
		String orgValues = getSubOrgById(String.valueOf(orgID));
		if ("".equals(orgValues)) {
			orgValues = orgID;
		} else {
			orgValues = orgValues + "," + orgID;
		}
		stringBuffer
				.append("UPDATE TB_DOCUMENT_MANAGEMENT SET STATUS='3' WHERE DOCUMENT_ID IN ("
						+ flag + ") AND org_id in (" + orgValues + ")");
		logger.info("将要执行的sql语句是: " + stringBuffer.toString());
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

			// DB更新
			result = update();
			commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * 企业文档管理修改
	 * 
	 * @param bean
	 * @return
	 */
	public int updateDocument(String requestId ,AvailablePageBean bean) throws SQLException {
		StringBuffer stringBuffer = new StringBuffer();
		String documentName=bean.getRequestName();
		String document=bean.getRequestURL();

		stringBuffer
				.append("UPDATE TB_DOCUMENT_MANAGEMENT SET DOCUMENT_NAME='"+documentName+"', DOCUMENT ='"+document+"' WHERE DOCUMENT_ID ="+requestId);
		logger.info("将要执行的sql语句是: " + stringBuffer.toString());
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

			// DB更新
			result = update();
			commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}

	public int updateChannel(String requestId ,AvailablePageBean bean) throws SQLException {
		StringBuffer stringBuffer = new StringBuffer();
		String channelName=bean.getRequestName();
		String channelUrl=bean.getRequestURL();
		String channelEnterType = bean.getEnterType();

		stringBuffer
				.append("UPDATE TB_CCB_CHANNEL SET channel_name='"+channelName+"', channel_url ='"+channelUrl+"',ENTERPRISES_TYPE='"+channelEnterType+"' WHERE channel_id ="+requestId);
		logger.info("将要执行的sql语句是: " + stringBuffer.toString());
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

			// DB更新
			result = update();
			commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * @param orgID
	 * @param orderType
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMachineInfoByOrgID(long orgID) throws SQLException {

		String FUNCTION_NAME = "getMachineInfoByOrgID() ";
		logger.info(FUNCTION_NAME + "start");

		try {
			String orgValues = getSubOrgById(String.valueOf(orgID));
			if ("".equals(orgValues)) {
				orgValues += orgID;
			} else {
				orgValues += "," + orgID;
			}

			StringBuffer sb = new StringBuffer();
			sb.append("SELECT");
			sb.append("	o.org_id,");
			sb.append("	o.org_name,");
			sb.append("	o.org_level,");
			sb.append("	o.parent_org_id,");
			sb.append("	m.machine_id,");
			sb.append("	m.machine_name,");
			sb.append("	m.MAC,");
			sb.append("	m.STATUS ");
			sb.append("FROM");
			sb.append("	tb_ccb_machine m,");
			sb.append("	tb_ccb_organization o ");
			sb.append("WHERE");
			sb.append("	o.org_id = m.org_id ");
			// 端机状态 != 3:未审核
			sb.append("	and m.status <> '3'");
			sb.append("	and o.org_id in(" + orgValues + ")");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			HashMap[] map = select();

			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	/**
	 * 组织id获取下级组织id，返回字符串类型，以逗号（,）分隔
	 * 
	 * @param orgId
	 * @return
	 * @throws SQLException
	 */
	public String getSubOrgById(String orgId) {
		String subOrgStr = getSubOrg(orgId);
		return subOrgStr;
	}

	/**
	 * 组织id获取下级组织id，返回字符串数组
	 * 
	 * @param orgId
	 * @return
	 * @throws SQLException
	 */
	public List<String> getSubOrgList(String orgId) {
		List<String> orgList = new ArrayList<String>();
		String subOrg = getSubOrgById(orgId);
		orgList = StringToList(subOrg);
		return orgList;
	}

	/**
	 * 字符串转字符串数组
	 * 
	 * @param subOrg
	 * @return
	 */
	private List<String> StringToList(String subOrg) {
		// TODO Auto-generated method stub
		List<String> returnList = new ArrayList<String>();
		String[] orgs = subOrg.split(",");
		for (int i = 0; i < orgs.length; i++) {
			returnList.add(orgs[i]);
		}
		return returnList;
	}

	/**
	 * 由组织ID获取下级组织ID
	 * 
	 * @param orgId
	 * @return 字符串类型，逗号(,)分隔
	 * @throws SQLException
	 */
	public String getSubOrg(String orgId) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;
		sb.append(" SELECT");
		sb.append(" suborg(o.org_id) as sub_org_id");
		sb.append(" FROM");
		sb.append(" tb_ccb_organization o");
		sb.append(" WHERE");
		sb.append(" o.org_id = ?");
		try {
			if (con == null) {
				connection();
			}
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(orgId));
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("sub_org_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("end");
		}
		return null;
	}

	/**
	 * 由组织ID获取上级组织ID
	 * 
	 * @param orgId
	 * @return 字符串类型，逗号(,)分隔
	 * @throws SQLException
	 */
	public String getSuperOrgIdByOrgId(String orgId) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		ResultSet rs = null;
		sb.append(" SELECT");
		sb.append(" superorgs(o.org_id) as super_org_id");
		sb.append(" FROM");
		sb.append(" tb_ccb_organization o");
		sb.append(" WHERE");
		sb.append(" o.org_id = ?");
		try {
			if (con == null) {
				connection();
			}
			stmt = con.prepareStatement(sb.toString());
			stmt.setLong(1, Long.parseLong(orgId));
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getString("super_org_id");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				releaseConnection();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("end");
		}
		return null;
	}

	public HashMap[] getAllOrganizations(String orgIds) throws SQLException {

		String FUNCTION_NAME = "getAllOrganizations() ";
		logger.info(FUNCTION_NAME + "start");

		try {

			StringBuffer sb = new StringBuffer();
			sb.append("select * from tb_ccb_organization where ORG_ID in ("
					+ orgIds + ")");

			// SQL文ログ出し
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			// DB未接続の場合、DB接続
			if (con == null)
				connection();

			// SQL文をセット
			stmt = con.prepareStatement(sb.toString());
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		} finally {
			// release処理
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}

	public int updateCommandManagement(int machineId, String commandContent,
			String status, int commandId, Date operatetime) throws SQLException {
		String FUNCTION_NAME = "updateCommandManagement() ";
		logger.info(FUNCTION_NAME + "start");
		StringBuffer sb = new StringBuffer();

		sb.append("UPDATE tb_command_management ");
		sb.append("SET status = ?, ");
		sb.append("command_content = ?, ");
		// sb.append("operatetime =? ");
		sb.append("operatetime = now() ");
		sb.append("WHERE machine_id = ? ");
		sb.append("AND command_id = ? ");
		logger.info(FUNCTION_NAME + "sql= " + sb.toString());
		logger.info(FUNCTION_NAME + "machineId: " + machineId);
		logger.info(FUNCTION_NAME + "status: " + status);
		logger.info(FUNCTION_NAME + "commandId: " + commandId);
		logger.info(FUNCTION_NAME + "commandContent: " + commandContent);
		// logger.info(FUNCTION_NAME + "operatetime: " +
		// operatetime.toString());
		int result = 0;

		try {
			if (con == null)
				connection();
			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			int i = 1;
			stmt.setString(i++, status);
			stmt.setString(i++, commandContent);
			// stmt.setDate(i++, new java.sql.Date(operatetime.getTime()));
			stmt.setInt(i++, machineId);
			stmt.setInt(i++, commandId);
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (!con.getAutoCommit()) {
				con.commit();
			}
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");
		}

		return result;
	}
	
	public HashMap[] getChennalInfoByID(Long chennalId) throws SQLException {// 获取频道列表
		             
		String FUNCTION_NAME = "getChennalInfoByID() ";
		logger.info(FUNCTION_NAME + "start");
//		String orgIds = getSuperOrgIdByOrgId(orgID);
//		if (orgIds == null) {
//			orgIds = "-1";
//		}
		try {
			StringBuffer sb = new StringBuffer();
			sb.append("SELECT channel_name,channel_url,ENTERPRISES_TYPE FROM TB_CCB_CHANNEL where channel_id = "
					+ chennalId);

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();
			
			return map;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
}
