/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao.lottery;

import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import th.dao.BaseDao;
import th.util.StringUtils;

/**
 * Descriptions
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class LottoryDAO extends BaseDao {
	private Log logger = LogFactory.getLog(LottoryDAO.class.getName());

	public LottoryDAO() {

	}

	/**
	 * 获取白名单列表
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public HashMap[] getLotteryMessage(String orgId) throws SQLException {

		String FUNCTION_NAME = "getLotteryMessage() ";
		logger.info(FUNCTION_NAME + "start");

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" select ");
			sb.append(" award_id, award_name, award_num, start_date, end_date, logo_url,");
			sb.append(" status, daily_hits,end_date- start_date as day");
			sb.append(" FROM tb_lottery_award");
			sb.append(" where award_id in ");
			sb.append(" (SELECT award_id FROM tb_lottery_management where org_id  =?)");
			sb.append(" and status ='1' ");
			sb.append(" and now()> start_date ");
			sb.append(" and now()< end_date ");
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong(1, Long.parseLong(orgId));

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
	public void modifyLotteryMessage(String award_id,int award_num) throws SQLException {

		String FUNCTION_NAME = "modifyLotteryMessage() ";
		logger.info(FUNCTION_NAME + "start");

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" update ");
			sb.append(" tb_lottery_award");
			sb.append(" set award_num =?");
			sb.append(" where award_id =?");
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();
			stmt.setLong(1, award_num);
			stmt.setLong(2, Long.parseLong(award_id));

			 update();
			 commit();

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	/**
	 * 通过mac地址获取组织ID
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public String getOrgIdBymac(String mac) throws SQLException {
		String FUNCTION_NAME = "getOrgIdBymac() ";
		String orgID="";
		try {

			StringBuffer sb = new StringBuffer();
			sb.append("select ORG_ID from TB_CCB_MACHINE where STATUS <>'3' AND  mac= '"+ mac+"'");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();
			orgID = (String)map[0].get("ORG_ID");
			return orgID;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	
	/**
	 * 通过mac地址获取端机类型
	 * 
	 * @param
	 * @return HashMap[]
	 * @throws SQLException
	 */
	public String getTypeBymac(String mac) throws SQLException {
		String FUNCTION_NAME = "getTypeBymac() ";
		String macKind="";
		try {

			StringBuffer sb = new StringBuffer();
			sb.append("select e.machine_kind,e.os from tb_machine_environment e,tb_ccb_machine m where m.machine_id=e.machine_id and m.mac='"+ mac+"'");

			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();
			macKind = (String)map[0].get("MACHINE_KIND");
			return macKind;

		} finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}

	}
	/**
	 * 插入中奖信息
	 * 
	 * @param
	 * @return 
	 * @throws SQLException
	 */
	public int insertLotteryHistory(HttpServletRequest req) throws SQLException {
		String FUNCTION_NAME = "insertLotteryHistory() ";
		String awardID = req.getParameter("awardID");
		String userName = req.getParameter("userName");
		String phone = req.getParameter("phone");
		String address = req.getParameter("address");
		String zipCode = req.getParameter("zipCode");
		//status 0:未处理 1:已发送 2:已审核
		String status = "0";
		
		StringBuilder sb = new StringBuilder();
		sb.append("insert into TB_LOTTERY_HISTORY  ");
		sb.append("	( LOTTERY_HISTORY_ID, AWARD_ID, AWARD_NAME, OPERATE_TIME, NAME, PHONE, ADDRESS, ZIP_CODE, STATUS ) ");
		sb.append("values (nextval('SEQ_TB_LOTTERY_HISTORY'),");
		sb.append("?,(select award_name from tb_lottery_award where award_id=?),CURRENT_TIMESTAMP,?,?,?,?,?)");
		logger.info(FUNCTION_NAME + "sql = " + sb.toString());
		try {
			if (con == null){
				connection();
			}
			stmt = con.prepareStatement(sb.toString());
			stmt.setInt(1, Integer.parseInt(awardID));
			stmt.setInt(2, Integer.parseInt(awardID));
			stmt.setString(3, userName);
			stmt.setString(4, phone);
			stmt.setString(5, address);
			stmt.setString(6, zipCode);
			stmt.setString(7, status);
			int ret =insert();
			commit();
			return ret;

		} finally {
			releaseConnection();
			
			logger.info(FUNCTION_NAME + "end");

		}

	}

	public HashMap[] getAwardInfo(String startTime, String endTime, String awardName, String userName, String phone) throws SQLException {

		String FUNCTION_NAME = "getAwardInfo() ";
		logger.info( FUNCTION_NAME + "start" );
		awardName = StringUtils.isBlank( awardName ) ? "" : awardName;
		userName = StringUtils.isBlank( userName ) ? "" : userName;
		phone = StringUtils.isBlank( phone ) ? "" : phone;

		try{
			StringBuffer sb = new StringBuffer();
			sb.append("	SELECT");
			sb.append("		award_name,");
			sb.append("		name,");
			sb.append("		operate_time,");
			sb.append("		phone,");
			sb.append("		address,");
			sb.append("		zip_code");
			sb.append("	FROM");
			sb.append("		tb_lottery_history");
			sb.append("	WHERE");
			sb.append("		1=1 ");
			if(!"".equals(awardName)){
				sb.append("	AND award_name LIKE '%"+awardName+"%'");
			}
			if(!"".equals(userName)){
				sb.append("	AND name LIKE '%"+userName+"%'");
			}
			if(!"".equals(phone)){
				sb.append("	AND phone LIKE '%"+phone+"%'");
			}
			sb.append("	AND operate_time >= TO_TIMESTAMP(");
			sb.append("		'"+startTime+"',");
			sb.append("		'YYYYMMDDHH24MISS'");
			sb.append("	)");
			sb.append("	AND operate_time < TO_TIMESTAMP(");
			sb.append("		'"+endTime+"',");
			sb.append("		'YYYYMMDDHH24MISS'");
			sb.append("	)");
			sb.append("	ORDER BY");
			sb.append("		operate_time DESC");
			
			logger.info(FUNCTION_NAME + "sql = " + sb.toString());

			if (con == null)
				connection();

			stmt = con.prepareStatement(sb.toString());
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			releaseConnection();
			logger.info(FUNCTION_NAME + "end");

		}
		
	}
}
