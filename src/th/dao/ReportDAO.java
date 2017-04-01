/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define4Report;
import th.db.DBAccess;
import th.entity.AdvertPlayDetailBean;
import th.util.FileTools;

/**
 * Descriptions
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ReportDAO extends DBAccess {
	private Log logger = LogFactory.getLog( ReportDAO.class.getName() );

	public ReportDAO() {

	}

	/**
	 * 取得组织一段时间内的开机机器的汇总数据
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMachineOpenSummary( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getMachineOpenSummary() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	tcm.ORG_ID, " );
			sb.append( " 	tcm.MACHINE_ID, " );
			sb.append( " 	count(tnpm.NEWEST_TIME) as TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_NEWEST_PULSE_MANAGEMENT as tnpm, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	tnpm.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	NEWEST_TIME >= TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	NEWEST_TIME < TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by  " );
			sb.append( " 	tcm.MACHINE_ID " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织一段时间内的报停机器的汇总数据
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMachinePauseSummary( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getMachinePauseSummary() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	tcm.ORG_ID, " );
			sb.append( " 	tcm.MACHINE_ID, " );
			sb.append( " 	count(tmph.PAUSE_TIME) as TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_MACHINE_PAUSE_HISTORY as tmph, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	tmph.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	PAUSE_TIME >= TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	PAUSE_TIME < TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by  " );
			sb.append( " 	tcm.MACHINE_ID " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织一段时间内的机器的总台数（服务中或者广告中的机器）
	 * 
	 * @param orgIdList
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getOrgMachineCount( String orgIds, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getOrgMachineCount() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	ORG_ID, " );
			sb.append( " 	count(MACHINE_ID) as TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_CCB_MACHINE " );
			sb.append( " where " );
			sb.append( " 	(STATUS = '" + Define4Report.MACHINE_STATUS_SERVICE + "' or STATUS = '"
					+ Define4Report.MACHINE_STATUS_ADVERTISING + "')  and " );
			sb.append( " 	OPERATETIME < TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and" );
			sb.append( " 	ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by  " );
			sb.append( " 	ORG_ID " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织一段时间内的审批通过的机器
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getOrgMachine( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getOrgMachine() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	tco.ORG_ID, " );
			sb.append( " 	tco.ORG_NAME, " );
			sb.append( " 	tcm.MACHINE_ID, " );
			sb.append( " 	tcm.MACHINE_NAME, " );
			sb.append( " 	tcm.MACHINE_MARK, " );
			sb.append( " 	tcm.STATUS " );
			sb.append( " from " );
			sb.append( " 	TB_CCB_ORGANIZATION as tco, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	tco.ORG_ID=tcm.ORG_ID and " );
			sb.append( " 	tcm.OPERATETIME < TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tcm.STATUS <> '" + Define4Report.MACHINE_STATUS_UNAPPROVED + "' and " );
			sb.append( " 	tco.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by  " );
			sb.append( " 	tco.ORG_ID,tcm.MACHINE_ID " );
			sb.append( " order by  " );
			sb.append( " 	tco.ORG_ID " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, endTime );
			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织信息
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getOrgsByIds( String orgIds ) throws SQLException {
		String FUNCTION_NAME = "getOrgsByIds() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	ORG_ID, " );
			sb.append( " 	ORG_NAME " );
			sb.append( " from " );
			sb.append( " 	TB_CCB_ORGANIZATION " );
			sb.append( " where " );
			sb.append( " 	ORG_ID in ( " + orgIds + " ) " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织一段时间内的广告播放的汇总数据
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public List<AdvertPlayDetailBean> getAdvertPlaySummary( String orgIds, String startTime, String endTime,
			String macType, String programListName, String materialName, String layoutName) throws SQLException {
		String FUNCTION_NAME = "getAdvertPlaySummary() ";
		logger.info( FUNCTION_NAME + "start" );
		ResultSet rs = null;
		if("".equals(macType) || null == macType){
			macType = "0";
		}
		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	taph.ORG_ID, " );
			sb.append( " 	taph.MACHINE_ID, " );
			sb.append( " 	tamm.MEDIA_NAME, " );
			sb.append( " 	taph.START_PLAY_TIME, " );
			sb.append( " 	taph.END_PLAY_TIME, " );
			sb.append( " 	taph.CLICK_STATUS, " );
			sb.append( " 	tcm.MACHINE_NAME, " );
			sb.append( " 	tcm.MACHINE_MARK, " );
			sb.append(" 	taph.BILL_ID, ");
			sb.append("     tap.BILL_NAME, ");
			sb.append("     tal.LAYOUT_NAME");
			sb.append( "  from " );
			sb.append( " 	TB_CCB_MACHINE as tcm, " );
			sb.append( " 	TB_AD_PLAY_HISTORY as taph " );
			if(!"0".equals(macType)){
				sb.append( " 	,tb_machine_environment as env " );
			}
			
				sb.append( " 	,TB_AD_LAYOUT as tal " );
			
			
				sb.append( " 	,TB_AD_PLAYBILL as tap " );
			
			
				sb.append( " 	,TB_AD_MATERIAL_MEDIA as tamm " );
				sb.append(" 	,TB_AD_PLAYBILL_DETAIL as tapd");
			
			sb.append( " where " );
			sb.append( " 	taph.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	taph.END_PLAY_TIME >= TO_TIMESTAMP('"+startTime+"','YYYYMMDDHH24MISS') and " );
			sb.append( " 	taph.START_PLAY_TIME < TO_TIMESTAMP('"+endTime+"','YYYYMMDDHH24MISS') and " );
			sb.append( " 	taph.ORG_ID in ( " + orgIds + " ) " );
			if(!"0".equals(macType)){
				sb.append( " 	and env.machine_id=tcm.machine_id " );
				String[] typeArray = macType.split("_");
				sb.append( "	AND env.os like 	'%" );
				sb.append( typeArray[0] );
				sb.append( "%' " );
				sb.append( "	AND env.machine_kind = 	'" );
				sb.append( typeArray[1].toLowerCase() );
				sb.append( "' " );
			}
			
				sb.append(" and tap.BILL_NAME like '%" + programListName + "%'  ");
				sb.append(" and  taph.BILL_ID = tap.BILL_ID");
			
			
				sb.append(" and  tal.LAYOUT_NAME like '%" + layoutName + "%'  ");
				sb.append(" and tal.LAYOUT_ID = tap.LAYOUT_ID ");
			
			
				sb.append(" and tamm.MEDIA_NAME like '%" + materialName + "%'  ");
				sb.append(" and tapd.BILL_ID = taph.BILL_ID ");
				sb.append(" and tamm.MEDIA_ID = tapd.MEDIA_ID ");
			

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			//System.out.println("getAdvertPlaySummary = "+new java.util.Date());
			//HashMap[] map = select();
			
			List<AdvertPlayDetailBean> serviceList = new ArrayList<AdvertPlayDetailBean>();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					AdvertPlayDetailBean advertPlayDetailBean = new AdvertPlayDetailBean();
					advertPlayDetailBean.setOrgId(rs.getString( "ORG_ID" ));
					advertPlayDetailBean.setMachineId(rs.getString( "MACHINE_ID" ));
					advertPlayDetailBean.setMediaName(rs.getString( "MEDIA_NAME" ));
					advertPlayDetailBean.setStartPlayTime(rs.getString( "START_PLAY_TIME" ));
					advertPlayDetailBean.setEndPlayTime(rs.getString( "END_PLAY_TIME" ));
					advertPlayDetailBean.setClickStatus(rs.getString( "CLICK_STATUS" ));
					advertPlayDetailBean.setMachineName(rs.getString( "MACHINE_MARK" ));
					advertPlayDetailBean.setBillId(rs.getString( "BILL_ID" ));
					advertPlayDetailBean.setBillName(rs.getString("BILL_NAME"));
					advertPlayDetailBean.setLayoutName(rs.getString("LAYOUT_NAME"));
					
					serviceList.add(advertPlayDetailBean);
				}
			}
			
			//System.out.println("getAdvertPlaySummary = "+new java.util.Date());
			return serviceList;
		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得所有频道
	 * 
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getAllChannel() throws SQLException {
		String FUNCTION_NAME = "getAllChannel() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	CHANNEL_ID, " );
			sb.append( " 	CHANNEL_NAME, " );
			sb.append( " 	CHANNEL_TYPE " );
			sb.append( " from " );
			sb.append( " 	TB_CCB_CHANNEL " );
			sb.append( " where " );
			sb.append( " 	STATUS in ('1','2' ) " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}
	
	/**
	 * 取得flag不重复的所有频道
	 * 
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getDistAllChannel() throws SQLException {
		String FUNCTION_NAME = "getDistAllChannel() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( "select channel_id, channel_name, channel_type, flag, status,enterprises_type " );
			sb.append( "from tb_ccb_channel " );
//			sb.append( "where channel_id in (select max(channel_id) from tb_ccb_channel group by flag ) " );
			sb.append( "where " );
			sb.append( " status in ('1','2') and channel_type='0'" );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}
	/**
	 * 取得flag不重复的所有频道
	 * 
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getDistAllChannelFen(String orgId) throws SQLException {
		String FUNCTION_NAME = "getDistAllChannel() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( "select channel_id, channel_name, channel_type, flag, status,enterprises_type " );
			sb.append( "from tb_ccb_channel " );
//			sb.append( "where channel_id in (select max(channel_id) from tb_ccb_channel group by flag ) " );
			sb.append( "where " );
			sb.append( " status in ('1','2') and channel_type='0' and org_id = " + orgId );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织一段时间内的频道使用数据
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getChannelUseSummary( String orgIds, String startTime, String endTime, String macType ) throws SQLException {
		String FUNCTION_NAME = "getChannelUseSummary() ";
		logger.info( FUNCTION_NAME + "start" );

		macType = th.util.StringUtils.isNotBlank(macType) ? macType : "0";
		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	tco.ORG_ID, " );
			sb.append( " 	tco.ORG_NAME, " );
			sb.append( " 	tcm.MACHINE_ID, " );
			sb.append( " 	tcm.MACHINE_MARK, " );
			sb.append( " 	tcch.CHANNEL_ID, " );
			sb.append( "    tcc.FLAG, " );
			sb.append( " 	tcch.CLICK_COUNT " );
			sb.append( " from " );
			sb.append( " 	TB_CCB_ORGANIZATION as tco, " );
			sb.append( " 	TB_CHANNEL_CLICK_HISTORY as tcch, " );
			sb.append( " 	TB_CCB_CHANNEL tcc, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			if(!"0".equals(macType)){
				sb.append( " 	,tb_machine_environment as env " );
			}
			sb.append( " where " );
			sb.append( " 	DATE >= TO_DATE(?,'YYYYMMDD') and " );
			sb.append( " 	DATE < TO_DATE(?,'YYYYMMDD') and " );
			sb.append( " 	tcch.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	tco.ORG_ID = tcm.ORG_ID and " );
			sb.append( " 	tcch.CHANNEL_ID = tcc.CHANNEL_ID and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			
			if(!"0".equals(macType)){
				sb.append( " 	and env.machine_id=tcm.machine_id " );
				String[] typeArray = macType.split("_");
				sb.append( "	AND env.os like 	'%" );
				sb.append( typeArray[0] );
				sb.append( "%' " );
				sb.append( "	AND env.machine_kind = 	'" );
				sb.append( typeArray[1].toLowerCase() );
				sb.append( "' " );
			}
			
			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 批次插入频道使用履历
	 * 
	 * @param insertList
	 * @param machineId
	 * @throws Exception
	 */
	public void insertBatchChannelClickHistory( List<String> insertList, String machineId, Date date )
			throws SQLException {
		String FUNCTION_NAME = "insertBatchChannelClickHistory() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			if ( insertList == null || insertList.size() == 0 ) {
				return;
			}

			// SQL文の生成
			StringBuffer sb = new StringBuffer();
			sb.append( "insert into " );
			sb.append( "TB_CHANNEL_CLICK_HISTORY" );
			sb.append( " ( " );
			sb.append( " HISTORY_ID, " );
			sb.append( " DATE, " );
			sb.append( " MACHINE_ID, " );
			sb.append( " CHANNEL_ID, " );
			sb.append( " CLICK_COUNT " );
			sb.append( " ) values ( " );
			sb.append( "nextval('SEQ_TB_CHANNEL_CLICK_HISTORY') " );
			sb.append( " , ? " );
			sb.append( " , ? " );
			sb.append( " , ? " );
			sb.append( " , ? " );
			sb.append( " ) " );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}
			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();

			for ( int i = 0; i < insertList.size(); i++ ) {
				String lineContent = insertList.get( i );
				if ( lineContent.indexOf( "," ) < 1 ) {
					continue;
				}

				long channelId = Long.valueOf( lineContent.split( "," )[0] );
				int clickCount = Integer.parseInt( lineContent.split( "," )[1] );

				int pCnt = 1;
				stmt.setDate( pCnt++, date );
				stmt.setLong( pCnt++, Long.valueOf( machineId ) );
				stmt.setLong( pCnt++, channelId );
				stmt.setInt( pCnt++, clickCount );

				stmt.addBatch();

			}

			// SQL実施
			insertBatch();

		}
		finally {
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 批次插入广告播放履历
	 * 
	 * @param insertList
	 * @param machineId
	 * @throws LocalPropertiesException
	 * @throws Exception
	 */
	public void insertBatchAdvertPalyHistory( List<String> insertList, String orgId, String machineId )
			throws SQLException, LocalPropertiesException {
		String FUNCTION_NAME = "insertBatchAdvertPalyHistory() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			if ( insertList == null || insertList.size() == 0 ) {
				return;
			}
			// 取得Media File Name
			String mediaDir = LocalProperties.getProperty( "FTP_FILE_PATH_MEDIA" );
			HashMap<String, String> mediaFileNameMap = this.getMediaFileNameMap( insertList, mediaDir );

			// SQL文の生成
			StringBuffer sb = new StringBuffer();
			sb.append( "insert into " );
			sb.append( "TB_AD_PLAY_HISTORY" );
			sb.append( " ( " );
			sb.append( " HISTORY_ID, " );
			sb.append( " ORG_ID, " );
			sb.append( " MACHINE_ID, " );
			sb.append( " BILL_ID, " );
			sb.append( " START_PLAY_TIME, " );
			sb.append( " END_PLAY_TIME, " );
			sb.append( " MEDIA_FILE_NAME, " );
			sb.append( " CLICK_TIME, " );
			sb.append( " CLICK_STATUS " );
			sb.append( " ) values ( " );
			sb.append( "nextval('SEQ_TB_AD_PALY_HISTORY') " );
			sb.append( " , ? " );
			sb.append( " , ? " );
			sb.append( " , ? " );
			sb.append( " , TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') " );
			sb.append( " , TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') " );
			sb.append( " , ? " );
			sb.append( " , TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') " );
			sb.append( " , ? " );
			sb.append( " ) " );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			for ( int i = 0; i < insertList.size(); i++ ) {
				String lineContent = insertList.get( i );
				if ( lineContent.indexOf( "," ) < 1 || lineContent.split( "," ).length < 6 ) {
					continue;
				}

				long billId = Long.valueOf( lineContent.split( "," )[0].split( "_" )[1] );
				String mediaFileTempName = lineContent.split( "," )[2];
				String startPlayTime = lineContent.split( "," )[3];
				String endPlayTime = lineContent.split( "," )[4];
				String clickSatus = lineContent.split( "," )[5];

				String mediaUrl = FileTools.buildFullFilePath( mediaDir, mediaFileTempName );
				String mediaFileName = ( mediaFileNameMap != null && mediaFileNameMap.containsKey( mediaUrl ) ) ? mediaFileNameMap
						.get( mediaUrl ) : "";

				if ( StringUtils.isBlank( mediaFileName ) ) {
					continue;
				}
				startPlayTime = startPlayTime.replaceAll( Define4Report.DATE_REPLACEALL_REGEX, "" );
				endPlayTime = endPlayTime.replaceAll( Define4Report.DATE_REPLACEALL_REGEX, "" );
				String clickTime = startPlayTime;

				int pCnt = 1;
				stmt.setLong( pCnt++, Long.valueOf( orgId ) );
				stmt.setLong( pCnt++, Long.valueOf( machineId ) );
				stmt.setLong( pCnt++, billId );
				stmt.setString( pCnt++, startPlayTime );
				stmt.setString( pCnt++, endPlayTime );
				stmt.setString( pCnt++, mediaFileName );
				stmt.setString( pCnt++, clickTime );
				stmt.setString( pCnt++, clickSatus );

				stmt.addBatch();

			}

			// SQL実施
			insertBatch();

		}
		finally {
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得Media File Name
	 * 
	 * @param insertList
	 * @return
	 * @throws SQLException
	 * @throws LocalPropertiesException
	 */
	private HashMap<String, String> getMediaFileNameMap( List<String> insertList, String mediaDir ) throws SQLException {
		String FUNCTION_NAME = "getMediaFileNameMap() ";
		logger.info( FUNCTION_NAME + "start" );
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
		try {
			StringBuffer sbMediaUrl = new StringBuffer();
			HashMap<String, String> mediaUrlMap = new HashMap<String, String>();
			for ( int i = 0; i < insertList.size(); i++ ) {
				String lineContent = insertList.get( i );
				if ( lineContent.indexOf( "," ) < 1 || lineContent.split( "," ).length < 6 ) {
					continue;
				}

				String mediaFileTempName = lineContent.split( "," )[2];
				if ( mediaUrlMap.containsKey( mediaFileTempName ) ) {
					continue;
				}
				mediaUrlMap.put( mediaFileTempName, mediaFileTempName );

				String mediaUrl = FileTools.buildFullFilePath( mediaDir, mediaFileTempName );
				mediaUrl = "'" + mediaUrl + "'";
				if ( i != 0 ) {
					sbMediaUrl.append( "," );
				}
				sbMediaUrl.append( mediaUrl );

			}

			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	MEDIA_FILE_NAME, " );
			sb.append( " 	MEDIA_URL " );
			sb.append( " from " );
			sb.append( " 	TB_AD_MATERIAL_MEDIA " );
			sb.append( " where " );
			sb.append( " 	MEDIA_URL in(" + sbMediaUrl + ")" );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();

			HashMap[] maps = select();
			if ( maps != null && maps.length > 0 ) {
				for ( int i = 0; i < maps.length; i++ ) {
					String mediaUrl = (String) maps[i].get( "MEDIA_URL" );
					String mediaFileName = (String) maps[i].get( "MEDIA_FILE_NAME" );
					resultHashMap.put( mediaUrl, mediaFileName );

				}

			}

			return resultHashMap;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );
		}
	}

	/**
	 * 根据MAC地址取得组织ID和机器ID
	 * 
	 * @param mac
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getOrgMachineIdByMac( String mac ) throws SQLException {
		String FUNCTION_NAME = "getOrgMachineIdByMac() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	tco.ORG_ID, " );
			sb.append( " 	tcm.MACHINE_ID " );
			sb.append( " from " );
			sb.append( " 	TB_CCB_ORGANIZATION as tco, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	tcm.MAC = ? and tco.org_id=tcm.org_id" );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, mac );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织一段时间内的最大和最小频道点击次数 即 台日最高和最低频道点击次数
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getChannelClickSummary( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getChannelClickSummary() ";
		logger.info( FUNCTION_NAME + "start" );
		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	tco.ORG_ID, " );
			sb.append( " 	tco.ORG_NAME, " );
			sb.append( " 	max(tcch.click_count) as max_click_count, " );
			sb.append( " 	min(tcch.click_count) as min_click_count " );
			sb.append( " from " );
			sb.append( " 	TB_CCB_ORGANIZATION as tco, " );
			sb.append( " 	TB_CHANNEL_CLICK_HISTORY as tcch, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	DATE >= TO_DATE(?,'YYYYMMDD') and " );
			sb.append( " 	DATE < TO_DATE(?,'YYYYMMDD') and " );
			sb.append( " 	tcch.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	tco.ORG_ID = tcm.ORG_ID and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " 	group by tco.org_id " );
			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织月度每日的频道使用数据
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMonthChannelUseChart( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getMonthChannelUseChart() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	tcch.DATE as DATE, " );
			sb.append( " 	sum(tcch.CLICK_COUNT) as TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_CHANNEL_CLICK_HISTORY as tcch, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	DATE >= TO_DATE(?,'YYYYMMDD') and " );
			sb.append( " 	DATE < TO_DATE(?,'YYYYMMDD') and " );
			sb.append( " 	tcch.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by " );
			sb.append( " 	tcch.DATE " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得年度每月的频道使用数据
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getYearChannelUseChart( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getYearChannelUseChart() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	date_trunc('month', tcch.DATE) as DATE, " );
			sb.append( " 	sum(tcch.CLICK_COUNT) as TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_CHANNEL_CLICK_HISTORY as tcch, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	DATE >= TO_DATE(?,'YYYYMMDD') and " );
			sb.append( " 	DATE < TO_DATE(?,'YYYYMMDD') and " );
			sb.append( " 	tcch.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by " );
			sb.append( " 	date_trunc('month', tcch.DATE) " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得月度每日的开机台数
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMonthMachineOpenChart( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getMonthMachineOpenChart() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	date_trunc('day', tnpm.NEWEST_TIME) as DATE, " );
			sb.append( " 	count(tnpm.MACHINE_ID) as OPEN_TOTAL, " );
			sb.append( " 	count(distinct tnpm.MACHINE_ID) as MACHINE_TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_NEWEST_PULSE_MANAGEMENT as tnpm, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	tnpm.NEWEST_TIME >= TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tnpm.NEWEST_TIME < TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tnpm.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by " );
			sb.append( " 	date_trunc('day', tnpm.NEWEST_TIME) " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织月度每日的报停机器数据
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getMonthMachinePauseChart( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getMonthMachinePauseChart() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	date_trunc('day', tmph.PAUSE_TIME) as DATE, " );
			sb.append( " 	count(tmph.MACHINE_ID) as PAUSE_TOTAL, " );
			sb.append( " 	count(distinct tmph.MACHINE_ID) as MACHINE_TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_MACHINE_PAUSE_HISTORY as tmph, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	tmph.PAUSE_TIME >= TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tmph.PAUSE_TIME < TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tmph.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by " );
			sb.append( " 	date_trunc('day', tmph.PAUSE_TIME) " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得年度每月的开机率数据
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getYearMachineOpenChart( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getYearChannelUseChart() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	date_trunc('month', tnpm.NEWEST_TIME) as DATE, " );
			sb.append( " 	count(tnpm.MACHINE_ID) as OPEN_TOTAL, " );
			sb.append( " 	count(distinct tnpm.MACHINE_ID) as MACHINE_TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_NEWEST_PULSE_MANAGEMENT as tnpm, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	tnpm.NEWEST_TIME >= TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tnpm.NEWEST_TIME < TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tnpm.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by " );
			sb.append( " 	date_trunc('month', tnpm.NEWEST_TIME) " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织年度每月的报停机器数据
	 * 
	 * @param orgIds
	 *            多个组织的orgId字符串，orgId用 ,分隔。如：1,2,3
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getYearMachinePauseChart( String orgIds, String startTime, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getMachinePauseSummary() ";
		logger.info( FUNCTION_NAME + "start" );

		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	date_trunc('month', tmph.PAUSE_TIME) as DATE, " );
			sb.append( " 	count(tmph.MACHINE_ID) as PAUSE_TOTAL, " );
			sb.append( " 	count(distinct tmph.MACHINE_ID) as MACHINE_TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_MACHINE_PAUSE_HISTORY as tmph, " );
			sb.append( " 	TB_CCB_MACHINE as tcm " );
			sb.append( " where " );
			sb.append( " 	tmph.PAUSE_TIME >= TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tmph.PAUSE_TIME < TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and " );
			sb.append( " 	tmph.MACHINE_ID = tcm.MACHINE_ID and " );
			sb.append( " 	tcm.ORG_ID in ( " + orgIds + " ) " );
			sb.append( " group by " );
			sb.append( " 	date_trunc('month', tmph.PAUSE_TIME) " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, startTime );
			stmt.setString( 2, endTime );

			HashMap[] map = select();

			return map;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

	/**
	 * 取得组织及其子组织下一段时间内的机器的总台数（服务中或者广告中的机器）
	 * 
	 * @param orgIdList
	 * @param endTime
	 * @return
	 * @throws SQLException
	 */
	public long getOrgsMachineCount( String orgIds, String endTime ) throws SQLException {
		String FUNCTION_NAME = "getOrgMachineCountWithChild() ";
		logger.info( FUNCTION_NAME + "start" );
		long count = 0;
		try {
			StringBuffer sb = new StringBuffer();
			sb.append( " select " );
			sb.append( " 	count(MACHINE_ID) as TOTAL " );
			sb.append( " from " );
			sb.append( " 	TB_CCB_MACHINE " );
			sb.append( " where " );
			sb.append( " 	(STATUS = '" + Define4Report.MACHINE_STATUS_SERVICE + "' or STATUS = '"
					+ Define4Report.MACHINE_STATUS_ADVERTISING + "')  and " );
			sb.append( " 	OPERATETIME < TO_TIMESTAMP(?,'YYYYMMDDHH24MISS') and" );
			sb.append( " 	ORG_ID in ( " + orgIds + " ) " );

			// SQL文ログ出し
			logger.info( FUNCTION_NAME + "sql = " + sb.toString() );

			// DB未接続の場合、DB接続
			if ( con == null ) {
				connection();
			}

			// SQL文をセット
			stmt = con.prepareStatement( sb.toString() );
			// パラメータ値をクリア
			stmt.clearParameters();
			stmt.setString( 1, endTime );

			HashMap[] map = select();
			if ( map != null && map.length > 0 ) {
				count = Long.valueOf( (String) map[0].get( "TOTAL" ) );
			}

			return count;

		}
		finally {
			// release処理
			releaseConnection();
			logger.info( FUNCTION_NAME + "end" );

		}
	}

}
