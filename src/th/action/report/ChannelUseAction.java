/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.report;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import th.action.BaseAction;
import th.dao.ReportDAO;
import th.entity.ChannelUseBean;
import th.entity.ChannelUseDetailBean;
import th.entity.ChannelUseHeaderBean;
import th.entity.ChannelUseOrgBean;

/**
 * 频道使用统计处理类
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ChannelUseAction extends BaseAction {
	/**
	 * 取得组织一个时间段内的汇总数据报表
	 * 
	 * @param selectedOrgId
	 * @param channelList
	 * @param startTime
	 *            YYYYMMDD
	 * @param endTime
	 *            YYYYMMDD
	 * @param sortKey
	 * @return
	 */
	public ArrayList<ChannelUseOrgBean> getSummaryReport( String selectedOrgId, ArrayList<String> channelList,
			String startTime, String endTime, String sortKey, String macType ) {
		ArrayList<ChannelUseOrgBean> orgChannelUseList = new ArrayList<ChannelUseOrgBean>();
		try {
			HashMap<String, Long> channelUseMap = this.getChannelUseSummary( selectedOrgId, startTime, endTime, macType );

			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getFirstOrgIdsByParentId( selectedOrgId );
			HashMap[] orgs = reportDAO.getOrgsByIds( orgIds );
			if ( orgs != null && orgs.length > 0 ) {
				for ( int orgIndex = 0; orgIndex < orgs.length; orgIndex++ ) {
					HashMap org = orgs[orgIndex];
					String orgId = (String) org.get( "ORG_ID" );
					String orgName = (String) org.get( "ORG_NAME" );

					ChannelUseOrgBean orgChannelUse = new ChannelUseOrgBean();
					orgChannelUse.setOrgId( orgId );
					orgChannelUse.setOrgName( orgName );

					long totalCount = 0;// 总台次

					List<String> orgIdList = ReportCommonAction.getOrgIdChildIds( orgId );

					ArrayList<ChannelUseBean> channelUseList = new ArrayList<ChannelUseBean>();
					for ( int channelIndex = 0; channelIndex < channelList.size(); channelIndex++ ) {
						String channel = channelList.get( channelIndex );
						String channelFlg = channel.split( "," )[0];
						String channelName = channel.split( "," )[1];

						long orgClickCount = 0;
						if ( orgIdList != null && orgIdList.size() > 0 ) {
							for ( int i = 0; i < orgIdList.size(); i++ ) {
								String id = orgIdList.get( i );
								String orgChannelKey = id + "," + channelFlg;
								long clickCount = channelUseMap.containsKey( orgChannelKey ) ? channelUseMap
										.get( orgChannelKey ) : 0;
								orgClickCount = orgClickCount + clickCount;
							}

						}

						totalCount = totalCount + orgClickCount;

						ChannelUseBean channelUse = new ChannelUseBean();
						channelUse.setChannelId( channelFlg );
						channelUse.setChannelName( channelName );
						channelUse.setClickCount( orgClickCount );
						channelUseList.add( channelUse );
					}

					long totalMachineCount = 0;// 总台数
					if ( orgIdList != null && orgIdList.size() > 0 ) {
						for ( int i = 0; i < orgIdList.size(); i++ ) {
							String id = orgIdList.get( i );
							long orgTotalMachineCount = channelUseMap.containsKey( id ) ? channelUseMap.get( id ) : 0;
							totalMachineCount = totalMachineCount + orgTotalMachineCount;
						}

					}

//					long averageCount = totalMachineCount == 0 ? 0 : totalCount / totalMachineCount;
					double averageCount = 0d;
					if(totalMachineCount == 0){
						averageCount = 0;
					}else{
						double tcDou = Double.parseDouble( String.valueOf( totalCount ) );
						double tmcDou = Double.parseDouble( String.valueOf( totalMachineCount ) );
						averageCount = this.round( tcDou/tmcDou, 2, BigDecimal.ROUND_HALF_UP );
					}

					orgChannelUse.setTotalCount( totalCount );
					orgChannelUse.setTotalMachineCount( totalMachineCount );
					orgChannelUse.setAverageCount( averageCount );
					orgChannelUse.setChannelUseList( channelUseList );
					orgChannelUse.setSortKey( sortKey );
					orgChannelUseList.add( orgChannelUse );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		// 排序
		Collections.sort( orgChannelUseList );

		return orgChannelUseList;

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
	public ArrayList<ChannelUseDetailBean> getDetailReport( String selectedOrgId, ArrayList<String> channelList,
			String startTime, String endTime, String sortKey, String macType ) {
		ArrayList<ChannelUseDetailBean> channelUseDetailList = new ArrayList<ChannelUseDetailBean>();

		try {
			HashMap<String, Long> orgMachineChannelMap = new HashMap<String, Long>();

			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getFirstOrgIdsByParentId( selectedOrgId );
			HashMap<String, ChannelUseDetailBean> orgMachineHashMap = new HashMap<String, ChannelUseDetailBean>();
			HashMap[] channelUseMapArray = reportDAO.getChannelUseSummary( orgIds, startTime, endTime, macType );
			if ( channelUseMapArray != null && channelUseMapArray.length > 0 ) {
				for ( int i = 0; i < channelUseMapArray.length; i++ ) {
					HashMap channelUseMap = channelUseMapArray[i];
					String orgId = (String) channelUseMap.get( "ORG_ID" );
					String orgName = (String) channelUseMap.get( "ORG_NAME" );
					String machineId = (String) channelUseMap.get( "MACHINE_ID" );
					String machineName = (String) channelUseMap.get( "MACHINE_MARK" );
					String channelId = (String) channelUseMap.get( "CHANNEL_ID" );
					String channelFlg = (String) channelUseMap.get( "FLAG" );
					long clickCount = Long.valueOf( (String) channelUseMap.get( "CLICK_COUNT" ) );

					long channelClickCount = clickCount;
					String orgMachineChannel = orgId + "," + machineId + "," + channelFlg;
					if ( orgMachineChannelMap.containsKey( orgMachineChannel ) ) {
						channelClickCount = orgMachineChannelMap.get( orgMachineChannel );
						channelClickCount = channelClickCount + clickCount;

					}
					orgMachineChannelMap.put( orgMachineChannel, channelClickCount );

					String orgMachine = orgId + "," + machineId;
					if ( !orgMachineHashMap.containsKey( orgMachine ) ) {
						ChannelUseDetailBean channelUseDetail = new ChannelUseDetailBean();
						channelUseDetail.setOrgId( orgId );
						channelUseDetail.setOrgName( orgName );
						channelUseDetail.setMachineId( machineId );
						channelUseDetail.setMachineName( machineName );
						channelUseDetailList.add( channelUseDetail );
						orgMachineHashMap.put( orgMachine, channelUseDetail );
					}

				}
			}

			if ( channelUseDetailList.size() > 0 ) {
				for ( int i = 0; i < channelUseDetailList.size(); i++ ) {
					ChannelUseDetailBean channelUseDetail = channelUseDetailList.get( i );
					String orgId = channelUseDetail.getOrgId();
					String machineId = channelUseDetail.getMachineId();

					long totalCount = 0;// 总次数
					ArrayList<ChannelUseBean> channelUseList = new ArrayList<ChannelUseBean>();
					for ( int channelIndex = 0; channelIndex < channelList.size(); channelIndex++ ) {
						String channel = channelList.get( channelIndex );
						String channelFlg = channel.split( "," )[0];
						String channelName = channel.split( "," )[1];
						String key = orgId + "," + machineId + "," + channelFlg;
						long clickCount = orgMachineChannelMap.containsKey( key ) ? orgMachineChannelMap.get( key ) : 0;
						totalCount = totalCount + clickCount;

						ChannelUseBean channelUse = new ChannelUseBean();
						channelUse.setChannelId( channelFlg );
						channelUse.setChannelName( channelName );
						channelUse.setClickCount( clickCount );
						channelUseList.add( channelUse );
					}

					channelUseDetail.setTotalCount( totalCount );
					channelUseDetail.setChannelUseList( channelUseList );
					channelUseDetail.setSortKey( sortKey );

				}

			}

		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		// 排序
		Collections.sort( channelUseDetailList );

		return channelUseDetailList;

	}

	/**
	 * 取得报表表格的频道表头
	 * 
	 * @param channelList
	 * @return
	 */
	public ArrayList<ChannelUseHeaderBean> getReportTableHeader( ArrayList<String> channelList ) {
		ArrayList<ChannelUseHeaderBean> tableHeaderList = new ArrayList<ChannelUseHeaderBean>();
		if ( channelList != null && channelList.size() > 0 ) {
			for ( int i = 0; i < channelList.size(); i++ ) {
				String channel = channelList.get( i );
				String channelFlg = channel.split( "," )[0];
				String channelName = channel.split( "," )[1];

				ChannelUseHeaderBean channelUseHeader = new ChannelUseHeaderBean();
				channelUseHeader.setChannelId( channelFlg );
				channelUseHeader.setChannelName( channelName );
				tableHeaderList.add( channelUseHeader );

			}
		}

		return tableHeaderList;

	}

	/**
	 * 取得所有频道
	 * 
	 * @return
	 */
	public ArrayList<String> getAllChannels() {
		ArrayList<String> channelList = new ArrayList<String>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			HashMap[] channels = reportDAO.getAllChannel();
			if ( channels != null && channels.length > 0 ) {
				for ( int i = 0; i < channels.length; i++ ) {
					HashMap channel = channels[i];
					String channelId = (String) channel.get( "CHANNEL_ID" );
					String channelName = (String) channel.get( "CHANNEL_NAME" );
					String value = channelId + "," + channelName;
					channelList.add( value );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return channelList;

	}
	
	/**
	 * 取得flag不重复的所有频道
	 * 
	 * @return
	 */
	public ArrayList<String> getDistAllChannels() {
		ArrayList<String> channelList = new ArrayList<String>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			HashMap[] channels = reportDAO.getDistAllChannel();
			if ( channels != null && channels.length > 0 ) {
				for ( int i = 0; i < channels.length; i++ ) {
					HashMap channel = channels[i];
					String channelFlg = (String) channel.get( "FLAG" );
					String channelName = (String) channel.get( "CHANNEL_NAME" );
					channelName = channelName + ("0".equals((String) channel.get( "ENTERPRISES_TYPE" ))?"(频道)":"(企业主页)");
					String value = channelFlg + "," + channelName;
					channelList.add( value );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return channelList;

	}
	/**
	 * 取得flag不重复的所有频道
	 * 
	 * @return
	 */
	public ArrayList<String> getDistAllChannelsFen(String orgId) {
		ArrayList<String> channelList = new ArrayList<String>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			HashMap[] channels = reportDAO.getDistAllChannelFen(orgId);
			if ( channels != null && channels.length > 0 ) {
				for ( int i = 0; i < channels.length; i++ ) {
					HashMap channel = channels[i];
					String channelFlg = (String) channel.get( "FLAG" );
					String channelName = (String) channel.get( "CHANNEL_NAME" );
					channelName = channelName + ("0".equals((String) channel.get( "ENTERPRISES_TYPE" ))?"(频道)":"(企业主页)");
					String value = channelFlg + "," + channelName;
					channelList.add( value );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return channelList;

	}
	/**
	 * 取得组织
	 * 
	 * @param orgIds
	 * @return
	 */
	private HashMap<String, String> getOrganizations( String orgIds ) {
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
		try {
			ReportDAO reportDAO = new ReportDAO();
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
	 * 取得组织的频道的点击次数和组织的机器数
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 * @param endTime
	 * @return HashMap<key, value> key:orgId,channelFlg(组织ID,频道FLAG);value:channelClickCount(频道点击次数)
	 *         key:orgId(组织ID);value:machineCount(机器数)
	 */
	public HashMap<String, Long> getChannelUseSummary( String selectedOrgId, String startTime, String endTime, String macType ) {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
			HashMap[] channelUseMapArray = reportDAO.getChannelUseSummary( orgIds, startTime, endTime, macType );
			HashMap<String, Long> orgMachineMap = new HashMap<String, Long>();
			if ( channelUseMapArray != null && channelUseMapArray.length > 0 ) {
				for ( int i = 0; i < channelUseMapArray.length; i++ ) {
					HashMap channelUseMap = channelUseMapArray[i];
					String orgId = (String) channelUseMap.get( "ORG_ID" );
					String machineId = (String) channelUseMap.get( "MACHINE_ID" );
					String channelId = (String) channelUseMap.get( "CHANNEL_ID" );
					String channelFlg = (String) channelUseMap.get( "FLAG" );
					long clickCount = Long.valueOf( (String) channelUseMap.get( "CLICK_COUNT" ) );

					long channelClickCount = clickCount;
					String orgChannelKey = orgId + "," + channelFlg;
					if ( resultHashMap.containsKey( orgChannelKey ) ) {
						channelClickCount = resultHashMap.get( orgChannelKey );
						channelClickCount = channelClickCount + clickCount;

					}

					// 当此组织的机器不包含此机器时，机器数加1
					String orgMachineKey = orgId + "," + machineId;
					if ( !orgMachineMap.containsKey( orgMachineKey ) ) {
						long machineCount = resultHashMap.containsKey( orgId ) ? resultHashMap.get( orgId ) + 1 : 1;
						resultHashMap.put( orgId, machineCount );

						orgMachineMap.put( orgMachineKey, machineCount );

					}

					resultHashMap.put( orgChannelKey, channelClickCount );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultHashMap;

	}
	
	/**
	 * 浮点数小数点截取方法
	 * @param v 浮点数
	 * @param scale 小数点后截取位数
	 * @param round_mode  四舍五入方式
	 * @return
	 */
	public double round(double v, int scale, int round_mode) {

		if(scale<0){
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		return b.setScale(scale, round_mode).doubleValue();

	}

}
