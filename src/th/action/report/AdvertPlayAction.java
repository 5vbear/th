/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.report;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import th.action.BaseAction;
import th.com.util.Define4Report;
import th.dao.ReportDAO;
import th.entity.AdvertPlayBean;
import th.entity.AdvertPlayDetailBean;

/**
 * 开机率统计处理类
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
public class AdvertPlayAction extends BaseAction {

	/**
	 * 读取上传的广告文件
	 */
	public void readFile2DB() {

	}

	/**
	 * 取得组织一个时间段内的汇总数据报表
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 *            YYYYMMDD
	 * @param endTime
	 *            YYYYMMDD
	 * @param sortKey
	 * @return
	 */
	public ArrayList<AdvertPlayBean> getSummaryReport( String selectedOrgId, String startTime, String endTime,
			String sortKey ,String macType, String programListName, String materialName, String layoutName) {
		ArrayList<AdvertPlayBean> advertPlayList = new ArrayList<AdvertPlayBean>();
		HashMap<String, String> orgsMap = this.getOrganizations( selectedOrgId );
		HashMap<String, String> advertPlayMap = this.getAdvertPlaySummary( selectedOrgId, startTime, endTime, macType, programListName, materialName, layoutName );
		if ( advertPlayMap != null && !advertPlayMap.isEmpty() ) {
			Iterator<String> iter = advertPlayMap.keySet().iterator();
			while ( iter.hasNext() ) {
				String orgMedia = iter.next();
				String orgId = orgMedia.split( "," )[0];
				String mediaFileName = orgMedia.split( "," )[1];
				String orgName = orgsMap.containsKey( orgId ) ? orgsMap.get( orgId ) : "";

				List<String> orgIdList = ReportCommonAction.getOrgIdChildIds( orgId );

				String billName = "";
				String showLayoutName = "";
				String billId = "" ;
				long realPlayTime = 0;
				long clickCount = 0;
				long totalMachineCount = 0;
				if ( orgIdList != null && orgIdList.size() > 0 ) {
					for ( int orgIndex = 0; orgIndex < orgIdList.size(); orgIndex++ ) {
						String id = orgIdList.get( orgIndex );
						String key = id + "," + mediaFileName;
						String value = advertPlayMap.get( key );// realPlayTime,clickCount,totalMachineCount
						if ( value != null && value.indexOf( "," ) > 0 ) {
							String orgRealPlayTime = value.split( "," )[0];
							String orgClickCount = value.split( "," )[1];
							String orgTotalMachineCount = value.split( "," )[2];
							realPlayTime = realPlayTime + Long.valueOf( orgRealPlayTime );
							clickCount = clickCount + Long.valueOf( orgClickCount );
							totalMachineCount = totalMachineCount + Long.valueOf( orgTotalMachineCount );
							billId = value.split( "," )[3];
							billName = value.split( "," )[4];
							showLayoutName = value.split( "," )[5];
						}
					}

				}

				// 平均每台播放时长（秒）
				long averagePlayTime = realPlayTime / totalMachineCount;
				// 平均每台点击次数
				long averageClickCount = clickCount / totalMachineCount;

				AdvertPlayBean advertPlay = new AdvertPlayBean();
				advertPlay.setOrgId( orgId );
				advertPlay.setOrgName( orgName );
				advertPlay.setMediaName( mediaFileName );
				advertPlay.setRealPlayTime( realPlayTime );
				advertPlay.setTotalMachineCount( totalMachineCount );
				advertPlay.setClickCount( clickCount );
				advertPlay.setAverageClickCount( averageClickCount );
				advertPlay.setAveragePlayTime( averagePlayTime );
				advertPlay.setSortKey( sortKey );
				advertPlay.setBillId(billId);
				advertPlay.setShowBillName(billName);
				advertPlay.setLayoutName(showLayoutName);

				advertPlayList.add( advertPlay );
			}
		}

		// 排序
		Collections.sort( advertPlayList );

		return advertPlayList;

	}

	/**
	 * 取得组织一个时间段内的明细数据报表
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 *            YYYYMMDD
	 * @param endTime
	 *            YYYYMMDD
	 * @param sortKey
	 * @return
	 */
	public ArrayList<AdvertPlayDetailBean> getDetailReport( String selectedOrgId, String startTime, String endTime,
			String sortKey , String macType, String programListName, String materialName, String layoutName) {
		ArrayList<AdvertPlayDetailBean> advertPlayDetailList = new ArrayList<AdvertPlayDetailBean>();
		HashMap<String, String> orgsMap = this.getOrganizations( selectedOrgId );
		HashMap<String, String> advertPlayMap = this.getAdvertPlayDetail( selectedOrgId, startTime, endTime, macType, programListName, materialName, layoutName  );
		if ( advertPlayMap != null && !advertPlayMap.isEmpty() ) {
			Iterator<String> iter = advertPlayMap.keySet().iterator();
			while ( iter.hasNext() ) {
				String key = iter.next();
				String orgId = key.split( "," )[0];
				String mediaFileName = key.split( "," )[1];
				String machineId = key.split( "," )[2];
				String orgName = orgsMap.containsKey( orgId ) ? orgsMap.get( orgId ) : "";
				String value = advertPlayMap.get( key );// realPlayTime,clickCount,machineName
				String realPlayTime = value.split( "," )[0];
				String clickCount = value.split( "," )[1];
				String machineName = value.split( "," )[2];

				AdvertPlayDetailBean advertPlayDetail = new AdvertPlayDetailBean();
				advertPlayDetail.setOrgId( orgId );
				advertPlayDetail.setOrgName( orgName );
				advertPlayDetail.setMachineId( machineId );
				advertPlayDetail.setMachineName( machineName );
				advertPlayDetail.setMediaName( mediaFileName );
				advertPlayDetail.setRealPlayTime( Long.valueOf( realPlayTime ) );
				advertPlayDetail.setClickCount( Long.valueOf( clickCount ) );
				advertPlayDetail.setSortKey( sortKey );

				advertPlayDetailList.add( advertPlayDetail );
			}
		}

		// 排序
		Collections.sort( advertPlayDetailList );

		return advertPlayDetailList;

	}

	/**
	 * 取得组织
	 * 
	 * @param selectedOrgId
	 * @return
	 */
	private HashMap<String, String> getOrganizations( String selectedOrgId ) {
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getFirstOrgIdsByParentId( selectedOrgId );
			HashMap[] orgs = reportDAO.getOrgsByIds( orgIds );
			if ( orgs != null && orgs.length > 0 ) {
				for ( int i = 0; i < orgs.length; i++ ) {
					HashMap org = orgs[i];
					String orgId = (String) org.get( "ORG_ID" );
					String orgName = (String) org.get( "ORG_NAME" );
					resultHashMap.put( orgId, orgName );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultHashMap;

	}

	/**
	 * 取得时间段内组织的广告播放时长和点击次数
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 * @param endTime
	 * @return HashMap<key, value> key:orgId,mediaFileName(组织ID,素材文件名称)
	 *         value:realPlayTime,clickCount,totalMachineCount(实际播放时长,点击次数,总台数)
	 */
	private HashMap<String, String> getAdvertPlaySummary( String selectedOrgId, String startTime, String endTime, 
														  String macType, String programListName, String materialName, String layoutName ) {
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getFirstOrgIdsByParentId( selectedOrgId );
			HashMap<String, String> orgMediaMachineMap = new HashMap<String, String>();
			List<AdvertPlayDetailBean> advertPlayMapArray = reportDAO.getAdvertPlaySummary( orgIds, startTime, endTime,macType, programListName, materialName, layoutName );
			if ( advertPlayMapArray != null && advertPlayMapArray.size() > 0 ) {
				for ( int i = 0; i < advertPlayMapArray.size(); i++ ) {
					AdvertPlayDetailBean advertPlayMap = advertPlayMapArray.get(i);
					String orgId = advertPlayMap.getOrgId();
					String machineId = advertPlayMap.getMachineId();
					String mediaFileName = advertPlayMap.getMediaName();
					String startPlayTime = advertPlayMap.getStartPlayTime();
					String endPlayTime = advertPlayMap.getEndPlayTime();
					String clickStatus = advertPlayMap.getClickStatus();
					String billId = advertPlayMap.getBillId();
					String billName = advertPlayMap.getBillName();
					String showLayoutName = advertPlayMap.getLayoutName();

					long playTime = this.getPlayTime( startPlayTime, endPlayTime );
					long realPlayTime = playTime;
					long clickCount = 0;
					long totalMachineCount = 0;
					String key = orgId + "," + mediaFileName;
					if ( resultHashMap.containsKey( key ) ) {
						String value = resultHashMap.get( key );
						realPlayTime = Long.valueOf( value.split( "," )[0] );
						clickCount = Long.valueOf( value.split( "," )[1] );
						totalMachineCount = Long.valueOf( value.split( "," )[2] );
						realPlayTime = realPlayTime + playTime;

					}

					// 当广告是点击状态时，点击次数加1
					if ( Define4Report.ADVERT_CLICK_STATUS_EXIST.equals( clickStatus ) ) {
						clickCount = clickCount + 1;
					}

					// 当此组织播放广告素材的机器不包含次机器时，总台次加1
					String orgMediaMachine = orgId + "," + mediaFileName + "," + machineId;
					if ( !orgMediaMachineMap.containsKey( orgMediaMachine ) ) {
						totalMachineCount = totalMachineCount + 1;
						orgMediaMachineMap.put( orgMediaMachine, orgMediaMachine );
					}

					String value = realPlayTime + "," + clickCount + "," + totalMachineCount+ "," + billId + "," + billName + "," + showLayoutName;
					resultHashMap.put( key, value );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultHashMap;

	}

	/**
	 * 取得时间段内组织的机器的广告播放时长和点击次数
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 * @param endTime
	 * @return HashMap<key, value> key:orgId,mediaFileName,machineId(组织ID,素材文件名称,机器ID)
	 *         value:realPlayTime,clickCount,machineName(实际播放时长,点击次数,机器名称)
	 */
	private HashMap<String, String> getAdvertPlayDetail( String selectedOrgId, String startTime, String endTime,String macType, String programListName, String materialName, String layoutName ) {
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getFirstOrgIdsByParentId( selectedOrgId );
			List<AdvertPlayDetailBean> advertPlayMapArray = reportDAO.getAdvertPlaySummary( orgIds, startTime, endTime,macType, programListName, materialName, layoutName );
			if ( advertPlayMapArray != null && advertPlayMapArray.size() > 0 ) {
				for ( int i = 0; i < advertPlayMapArray.size(); i++ ) {
					AdvertPlayDetailBean advertPlayMap = advertPlayMapArray.get(i);
					String orgId = advertPlayMap.getOrgId();
					String machineId = advertPlayMap.getMachineId();
					String machineName = advertPlayMap.getMachineName();
					String mediaFileName = advertPlayMap.getMediaName();
					String startPlayTime = advertPlayMap.getStartPlayTime();
					String endPlayTime = advertPlayMap.getEndPlayTime();
					String clickStatus = advertPlayMap.getClickStatus();

					long playTime = this.getPlayTime( startPlayTime, endPlayTime );
					long realPlayTime = playTime;
					long clickCount = 0;
					String key = orgId + "," + mediaFileName + "," + machineId;
					if ( resultHashMap.containsKey( key ) ) {
						String value = resultHashMap.get( key );
						realPlayTime = Long.valueOf( value.split( "," )[0] );
						clickCount = Long.valueOf( value.split( "," )[1] );
						realPlayTime = realPlayTime + playTime;
					}

					// 当广告是点击状态时，点击次数加1
					if ( Define4Report.ADVERT_CLICK_STATUS_EXIST.equals( clickStatus ) ) {
						clickCount = clickCount + 1;
					}
					String value = realPlayTime + "," + clickCount + "," + machineName;
					resultHashMap.put( key, value );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultHashMap;

	}

	/**
	 * 取得广告播放时间
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private long getPlayTime( String startTime, String endTime ) {
		long playTime = 0L;
		try {
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat format = new SimpleDateFormat( pattern );
			Date startDate = format.parse( startTime );
			Date endDate = format.parse( endTime );
			long diff = endDate.getTime() - startDate.getTime();
			playTime = diff / 1000;

		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

		return playTime;

	}
}
