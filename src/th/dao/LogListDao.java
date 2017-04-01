package th.dao;

import java.sql.SQLException;
import java.util.HashMap;

import th.entity.LogListBean;

public class LogListDao extends BaseDao {

	public HashMap<String, String>[] searchAdvertising(LogListBean bean) {
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();	
		stringBuffer.append( "SELECT " );
		stringBuffer.append( " TAP.BILL_NAME," );
		stringBuffer.append( " TCM.MACHINE_MARK," );
		stringBuffer.append( " TO_CHAR(TAPH.START_PLAY_TIME,'YY-MM-DD HH24:MI:SS') AS START_PLAY_TIME" );
		stringBuffer.append( " FROM " );
		stringBuffer.append( " tb_ad_play_history TAPH," );
		stringBuffer.append( " TB_CCB_MACHINE TCM," );
		stringBuffer.append( " TB_AD_PLAYBILL TAP" );
		stringBuffer.append( " WHERE" );
		stringBuffer.append( " TAPH.MACHINE_ID = TCM.MACHINE_ID" );
		stringBuffer.append( " AND TAP.BILL_ID = TAPH.BILL_ID" );

		//Where条件
		//检索时间(开始时间)
		if (bean.getSearch_time_start() != null && !"".equals(bean.getSearch_time_start())) {
			stringBuffer.append(" AND START_PLAY_TIME >= '");
			stringBuffer.append(bean.getSearch_time_start()+" 00:00:00");
			stringBuffer.append("'");
		}
		
		//检索时间(结束时间)
		if (bean.getSearch_time_end() != null && !"".equals(bean.getSearch_time_end())) {
			stringBuffer.append(" AND START_PLAY_TIME <= '");
			stringBuffer.append(bean.getSearch_time_end()+" 23:59:59");
			stringBuffer.append("'");
		}
		
		//节目单
		if (bean.getBill_name() != null && !"".equals(bean.getBill_name())) {
			stringBuffer.append(" AND TAP.BILL_NAME like '%");
			stringBuffer.append(bean.getBill_name());
			stringBuffer.append("%'");
		}
		
		//端机标识
		if (bean.getMachine_mark() != null && !"".equals(bean.getMachine_mark())) {
			stringBuffer.append(" AND TCM.MACHINE_MARK like '%");
			stringBuffer.append(bean.getMachine_mark());
			stringBuffer.append("%'");
		}
		
		stringBuffer.append(" ORDER BY START_PLAY_TIME DESC");
		
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return beans;
	}

	public HashMap<String, String>[] searchAdvertisingClickLog(LogListBean bean) {

		
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();	
		stringBuffer.append( "SELECT" );
		stringBuffer.append( " TAP.BILL_NAME," );
		stringBuffer.append( " TAL.LAYOUT_NAME," );
		stringBuffer.append( " TAPH.MEDIA_FILE_NAME," );
		stringBuffer.append( " TCM.MACHINE_MARK," );
		stringBuffer.append( " TO_CHAR(TAPH.CLICK_TIME,'YY-MM-DD HH24:MI:SS') AS CLICK_TIME" );
		stringBuffer.append( " FROM" );
		stringBuffer.append( " tb_ad_play_history TAPH," );
		stringBuffer.append( " TB_CCB_MACHINE TCM," );
		stringBuffer.append( " TB_AD_PLAYBILL TAP," );
		stringBuffer.append( " TB_AD_LAYOUT TAL" );
		stringBuffer.append( " WHERE" );
		stringBuffer.append( " TAPH.MACHINE_ID = TCM.MACHINE_ID" );
		stringBuffer.append( " AND TAP.BILL_ID = TAPH.BILL_ID" );
		stringBuffer.append( " AND TAP.LAYOUT_ID = TAL.LAYOUT_ID" );
		stringBuffer.append( " AND TAPH.CLICK_STATUS = '1'" );
		
		
		
		//Where条件
		//检索时间(开始时间)
		if (bean.getSearch_time_start() != null && !"".equals(bean.getSearch_time_start())) {
			stringBuffer.append(" AND CLICK_TIME >= '");
			stringBuffer.append(bean.getSearch_time_start()+" 00:00:00");
			stringBuffer.append("'");
		}
		
		//检索时间(结束时间)
		if (bean.getSearch_time_end() != null && !"".equals(bean.getSearch_time_end())) {
			stringBuffer.append(" AND CLICK_TIME <= '");
			stringBuffer.append(bean.getSearch_time_end()+" 23:59:59");
			stringBuffer.append("'");
		}
		
		//节目单
		if (bean.getBill_name() != null && !"".equals(bean.getBill_name())) {
			stringBuffer.append(" AND TAP.BILL_NAME like '%");
			stringBuffer.append(bean.getBill_name());
			stringBuffer.append("%'");
		}
		
		//素材
		if (bean.getMedia_file_name() != null && !"".equals(bean.getMedia_file_name())) {
			stringBuffer.append(" AND TAPH.MEDIA_FILE_NAME like '%");
			stringBuffer.append(bean.getMedia_file_name());
			stringBuffer.append("%'");
		}
		
		stringBuffer.append(" ORDER BY CLICK_TIME DESC");
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return beans;
	}

	public HashMap<String, String>[] systemOperationSearchLog(LogListBean bean) {

		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		
		StringBuffer stringBuffer = new StringBuffer();	
		
		stringBuffer.append( " SELECT * FROM (" );
		
		//配置
		stringBuffer.append( " SELECT" );
		stringBuffer.append( " TO_CHAR(TMM.OPERATETIME,'YYYY-MM-DD HH24:MI:SS') AS OPERATION_TIME," );
		stringBuffer.append( " TCU.NAME AS OPERATION_USER," );
		stringBuffer.append( " TMCM.MODULE_NAME AS MODULE_NAME," );
		stringBuffer.append( " '模板映射' AS OPERATION_DESCRIPTION," );	
		stringBuffer.append( " '配置' AS OPERATION_TYPE" );		
		stringBuffer.append( " FROM" );
		stringBuffer.append( " TB_MODULE_MANAGEMENT TMM," );
		stringBuffer.append( " TB_CCB_USER TCU," );
		stringBuffer.append( " TB_CCB_ORGANIZATION TCO," );
		stringBuffer.append( " TB_MACHINE_CONFIG_MODULE TMCM" );
		stringBuffer.append( " WHERE" );
		stringBuffer.append( " TMM.OPERATOR = TCU.USER_ID" );
		stringBuffer.append( " AND TMM.ORG_ID=TCO.ORG_ID" );
		stringBuffer.append( " AND TMM.MODULE_ID=TMCM.MODULE_ID" );
		
		
		
		stringBuffer.append( " UNION ALL" );
		//频道发布
		stringBuffer.append( " SELECT" );
		stringBuffer.append( " TO_CHAR(TCC.OPERATETIME,'YYYY-MM-DD HH24:MI:SS')AS OPERATION_TIME," );
		stringBuffer.append( " TCU. NAME AS OPERATION_USER," );
		stringBuffer.append( " TCC.CHANNEL_NAME AS MODULE_NAME," );
		stringBuffer.append( " '频道发布' AS OPERATION_DESCRIPTION," );
		stringBuffer.append( " '频道发布' AS OPERATION_TYPE" );
		stringBuffer.append( " FROM" );
		stringBuffer.append( " TB_CCB_CHANNEL TCC," );
		stringBuffer.append( " TB_CCB_USER TCU" );
		stringBuffer.append( " WHERE" );
		stringBuffer.append( " TCC.OPERATOR = TCU.USER_ID" );
		stringBuffer.append( " AND TCC.CHANNEL_TYPE = '0'" );
		
		
		stringBuffer.append( " UNION ALL" );
		
		//快速入口发布
		stringBuffer.append( " SELECT" );
		stringBuffer.append( " TO_CHAR(TCC.OPERATETIME,'YYYY-MM-DD HH24:MI:SS')AS OPERATION_TIME," );
		stringBuffer.append( " TCU. NAME AS OPERATION_USER," );
		stringBuffer.append( " TCC.CHANNEL_NAME AS MODULE_NAME," );
		stringBuffer.append( " '快速入口发布' AS OPERATION_DESCRIPTION," );
		stringBuffer.append( " '快速入口发布' AS OPERATION_TYPE" );
		stringBuffer.append( " FROM" );
		stringBuffer.append( " TB_CCB_CHANNEL TCC," );
		stringBuffer.append( " TB_CCB_USER TCU" );
		stringBuffer.append( " WHERE" );
		stringBuffer.append( " TCC.OPERATOR = TCU.USER_ID" );
		stringBuffer.append( " AND TCC.CHANNEL_TYPE = '1'" );
		
		
		stringBuffer.append( " UNION ALL" );
		
		//下发节目单
		stringBuffer.append( " SELECT" );
		stringBuffer.append( " TO_CHAR(TPM.OPERATETIME,'YYYY-MM-DD HH24:MI:SS')AS OPERATION_TIME," );
		stringBuffer.append( " TCU. NAME AS OPERATION_USER," );
		stringBuffer.append( " TAP.BILL_NAME AS MODULE_NAME," );
		stringBuffer.append( " '节目单下发' AS OPERATION_DESCRIPTION," );
		stringBuffer.append( " '下发节目单' AS OPERATION_TYPE" );
		stringBuffer.append( " FROM" );
		stringBuffer.append( " TB_PLAYBILL_MANAGEMENT TPM," );
		stringBuffer.append( " TB_AD_PLAYBILL TAP," );
		stringBuffer.append( " TB_CCB_USER TCU" );
		stringBuffer.append( " WHERE" );
		stringBuffer.append( " TPM.OPERATOR = TCU.USER_ID" );
		stringBuffer.append( " AND TPM.BILL_ID=TAP.BILL_ID" );

		
		stringBuffer.append( " UNION ALL" );
		
		//字幕下发
		stringBuffer.append( " SELECT" );
		stringBuffer.append( " TO_CHAR(TSM.OPERATETIME,'YYYY-MM-DD HH24:MI:SS')AS OPERATION_TIME," );
		stringBuffer.append( " TCU. NAME AS OPERATION_USER," );
		stringBuffer.append( " TAMS.SUBTITLES_NAME AS MODULE_NAME," );
		stringBuffer.append( " '字幕下发' AS OPERATION_DESCRIPTION," );
		stringBuffer.append( " '字幕下发' AS OPERATION_TYPE" );
		stringBuffer.append( " FROM" );
		stringBuffer.append( " TB_SUBTITLES_MANAGEMENT TSM," );
		stringBuffer.append( " TB_AD_MATERIAL_SUBTITLES TAMS," );
		stringBuffer.append( " TB_CCB_USER TCU" );
		stringBuffer.append( " WHERE" );
		stringBuffer.append( " TSM.OPERATOR = TCU.USER_ID" );
		stringBuffer.append( " AND TSM.SUBTITLES_ID=TAMS.SUBTITLES_ID" );
		
		stringBuffer.append( " UNION ALL" );

		//系统升级
		stringBuffer.append( " SELECT" );
		stringBuffer.append( " TO_CHAR(TSUM.OPERATETIME,'YYYY-MM-DD HH24:MI:SS')AS OPERATION_TIME," );
		stringBuffer.append( " TCU. NAME AS OPERATION_USER," );
		stringBuffer.append( " TSUD.FILE_NAME AS MODULE_NAME," );
		stringBuffer.append( " '升级包映射' AS OPERATION_DESCRIPTION," );
		stringBuffer.append( " '系统升级' AS OPERATION_TYPE" );
		stringBuffer.append( " FROM" );
		stringBuffer.append( " TB_SYSTEM_UPDATE_MANAGEMENT TSUM," );
		stringBuffer.append( " TB_SYSTEM_UPDATE_DATA TSUD," );
		stringBuffer.append( " TB_CCB_USER TCU" );
		stringBuffer.append( " WHERE" );
		stringBuffer.append( " TSUM.OPERATOR = TCU.USER_ID" );
		stringBuffer.append( " AND TSUM.FILE_ID=TSUD.FILE_ID" );
		stringBuffer.append( " AND TSUM.STATUS='0'" );

		stringBuffer.append( " ) AS RESULT" );
		stringBuffer.append( " WHERE" );
		stringBuffer.append( " 1=1 " );

		
		//Where条件
		//检索时间(开始时间)
		if (bean.getSearch_time_start() != null && !"".equals(bean.getSearch_time_start())) {
			stringBuffer.append(" AND OPERATION_TIME >= '");
			stringBuffer.append(bean.getSearch_time_start()+" 00:00:00");
			stringBuffer.append("'");
		}
		
		//检索时间(结束时间)
		if (bean.getSearch_time_end() != null && !"".equals(bean.getSearch_time_end())) {
			stringBuffer.append(" AND OPERATION_TIME<= '");
			stringBuffer.append(bean.getSearch_time_end()+" 23:59:59");
			stringBuffer.append("'");
		}
		
		//操作用户
		if (bean.getOperation_user() != null && !"".equals(bean.getOperation_user())) {
			stringBuffer.append(" AND OPERATION_USER like '%");
			stringBuffer.append(bean.getOperation_user());
			stringBuffer.append("%'");
		}
		
		//操作类型
		if (bean.getOperation_type() != null && !"".equals(bean.getOperation_type())) {		
			stringBuffer.append(" AND OPERATION_TYPE = '");
			stringBuffer.append(bean.getOperation_type());
			stringBuffer.append("'");
		}
		
		stringBuffer.append(" ORDER BY OPERATION_TIME DESC");
		
		HashMap<String, String>[] beans = null;
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			beans = select();
		} catch (SQLException ex) {
			logger.error( "SQL select execution failure..." , ex );
		} finally {
			try {
				releaseConnection();
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		}
		
		return beans;
	}

}
