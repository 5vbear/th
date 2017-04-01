/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.report;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import th.action.BaseAction;
import th.action.OrgDealAction;
import th.com.util.Define4Report;
import th.dao.ReportDAO;
import th.dao.machine.MachineDAO;
import th.entity.ChannelUseBean;
import th.entity.DeviceRunningBean;
import th.entity.MachineOpenRateBean;
import th.util.DateUtil;

/**
 * Descriptions
 * 
 * @version 2013-8-31
 * @author PSET
 * @since JDK1.6
 * 
 */
public class DeviceRunningChartAction extends BaseAction {
	private void fillResultList( List<HashMap<String, String>> targetList, List<HashMap<String, String>> sourceList ) {
		if ( targetList == null || sourceList == null ) {
			return;
		}
		List<HashMap<String, String>> fillList = new ArrayList<HashMap<String, String>>();
		if ( targetList.size() == 0 ) {
			fillList = sourceList;
		}

		for ( int i = 0; i < sourceList.size(); i++ ) {
			HashMap<String, String> sourceMap = sourceList.get( i );
			String sourceOrgId = (String) sourceMap.get( "ORG_ID" );
			boolean toFill = false;
			for ( int j = 0; j < targetList.size(); j++ ) {
				HashMap<String, String> targetMap = targetList.get( j );
				String targetOrgId = (String) targetMap.get( "ORG_ID" );
				if ( sourceOrgId.equals( targetOrgId ) ) {

					// 为了排序将开机率和正常率保存
					String openRate = targetMap.get( "OPEN_RATE" );
					String normalRate = targetMap.get( "NORMAL_RATE" );
					String averageChannel = String.valueOf(targetMap.get( "AVERAGE_CHANNEL" ));
					if ( openRate != null && !openRate.equals( "" ) ) {
						sourceMap.put( "OPEN_RATE", openRate );
					}
					else if ( normalRate != null && !normalRate.equals( "" ) ) {
						sourceMap.put( "NORMAL_RATE", normalRate );
					}
					else if ( averageChannel != null && !averageChannel.equals( "" ) ) {
						sourceMap.put( "AVERAGE_CHANNEL", averageChannel );
					}

					toFill = false;
					break;
				}
				toFill = true;
			}
			if ( toFill ) {
// sourceMap.put( "value", "0" );
				fillList.add( sourceMap );
			}
		}
		targetList.addAll( fillList );
	}
	public int getTotalPageCount(List orgNodesList){
		int totalPageCount=0;
		try {
			totalPageCount = ReportCommonAction.getTotalPageCount( (ArrayList) orgNodesList );
		}
		catch ( Exception e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totalPageCount;
	}
	//
	public List<DeviceRunningBean> getResultList( String selectedOrgId, String startEndTime ) {

		List<DeviceRunningBean> resultList = new ArrayList<DeviceRunningBean>();

		String startTime = startEndTime.split( "_" )[0];
		String endTime = startEndTime.split( "_" )[1];
//		 List<HashMap<String, String>> orgList=getOrgList(selectedOrgId);
		 OrgDealAction orgDealAction = new OrgDealAction();
			List<HashMap<String, String>> orgNodesList = orgDealAction.getFirstChildNodesByOrgId( Long
					.parseLong( selectedOrgId ) );
//			boolean result = new MachineDAO().isExistMacInCurrentOrg(selectedOrgId);
			List<HashMap<String, String>> orgList = new ArrayList<HashMap<String, String>>();
			for ( int i = 0; i < orgNodesList.size(); i++ ) {
				HashMap oneNode = orgNodesList.get( i );
				HashMap oneOrg = new HashMap();
				oneOrg.put( "ORG_ID", oneNode.get( "ORG_ID" ) );
				oneOrg.put( "ORG_NAME", oneNode.get( "ORG_NAME" ) );
//				if(selectedOrgId.equals(oneNode.get( "ORG_ID" ).toString()) && !result){
//					continue;
//				}
				orgList.add( oneOrg );
			}
		try {

		
//			getTotalPageCount(orgNodesList);
//			int totalPageCount = ReportCommonAction.getTotalPageCount( (ArrayList) orgNodesList );

			MachineOpenRateAction mor = new MachineOpenRateAction();
			String openRateSortKey = "openRate";
			ArrayList<MachineOpenRateBean> openRateList = mor.getSummaryReport( selectedOrgId, startTime, endTime,
					openRateSortKey );
			List<HashMap<String, String>> openRateMapList = new ArrayList();
			for ( int i = 0; i < openRateList.size(); i++ ) {
				MachineOpenRateBean machineOpenRateBean = openRateList.get( i );
				HashMap openRateMap = new HashMap();
				openRateMap.put( "ORG_ID", machineOpenRateBean.getOrgId() );
				openRateMap.put( "ORG_NAME", machineOpenRateBean.getOrgName() );
				openRateMap.put( "OPEN_RATE", machineOpenRateBean.getOpenRate() );
//				if(selectedOrgId.equals(machineOpenRateBean.getOrgId()) && !result){
//					continue;
//				}
				openRateMapList.add( openRateMap );
			}
			fillResultList( openRateMapList, orgList );

			String normalRateSortKey = "normalRate";
			ArrayList<MachineOpenRateBean> normalRateList = mor.getSummaryReport( selectedOrgId, startTime, endTime,
					normalRateSortKey );
			List<HashMap<String, String>> normalRateMapList = new ArrayList();
			for ( int i = 0; i < normalRateList.size(); i++ ) {
				MachineOpenRateBean machineOpenRateBean = normalRateList.get( i );
				HashMap normalRateMap = new HashMap();
				normalRateMap.put( "ORG_ID", machineOpenRateBean.getOrgId() );
				normalRateMap.put( "ORG_NAME", machineOpenRateBean.getOrgName() );
				normalRateMap.put( "NORMAL_RATE", machineOpenRateBean.getNormalRate() );
//				if(selectedOrgId.equals(machineOpenRateBean.getOrgId()) && !result){
//					continue;
//				}
				normalRateMapList.add( normalRateMap );
			}
			fillResultList( normalRateMapList, orgList );

			// 台均频道使用次数
			ChannelUseAction cu = new ChannelUseAction();
			ArrayList<String> channelList = cu.getDistAllChannels();
			HashMap<String, Long> channelUseMap = cu.getChannelUseSummary( selectedOrgId, startTime, endTime, "" );

			List<HashMap<String, String>> averageChannelMapList = new ArrayList();

			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getFirstOrgIdsByParentId( selectedOrgId );
			HashMap[] orgs = reportDAO.getOrgsByIds( orgIds );
			if ( orgs != null && orgs.length > 0 ) {
				for ( int orgIndex = 0; orgIndex < orgs.length; orgIndex++ ) {
					HashMap averageMap = new HashMap();
					HashMap org = orgs[orgIndex];
					String orgId = (String) org.get( "ORG_ID" );
					String orgName = (String) org.get( "ORG_NAME" );

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
					}

					long totalMachineCount = 0;// 总台数
					if ( orgIdList != null && orgIdList.size() > 0 ) {
						for ( int i = 0; i < orgIdList.size(); i++ ) {
							String id = orgIdList.get( i );
							long orgTotalMachineCount = channelUseMap.containsKey( id ) ? channelUseMap.get( id ) : 0;
							totalMachineCount = totalMachineCount + orgTotalMachineCount;
						}

					}

// long averageCount = totalMachineCount == 0 ? 0 : totalCount / totalMachineCount;
					double averageCount = 0d;
					if ( totalMachineCount == 0 ) {
						averageCount = 0;
					}
					else {
						double tcDou = Double.parseDouble( String.valueOf( totalCount ) );
						double tmcDou = Double.parseDouble( String.valueOf( totalMachineCount ) );
						averageCount = cu.round( tcDou / tmcDou, 2, BigDecimal.ROUND_HALF_UP );
					}

					averageMap.put( "ORG_ID", orgId );
					averageMap.put( "ORG_NAME", orgName );
					averageMap.put( "AVERAGE_CHANNEL", averageCount );
//					if(selectedOrgId.equals(orgId) && !result){
//						continue;
//					}
					averageChannelMapList.add( averageMap );
				}
				Collections.sort( averageChannelMapList, new Comparator() {
					public int compare( Object c, Object d ) {
						int firstAvr = Double.valueOf( String.valueOf( ( (HashMap) c ).get( "AVERAGE_CHANNEL" ) ) )
								.intValue();
						int secondAvr = Double.valueOf( String.valueOf( ( (HashMap) d ).get( "AVERAGE_CHANNEL" ) ) )
								.intValue();
						int avr = secondAvr - firstAvr;
						return avr;
					}
				} );

				fillResultList( averageChannelMapList, orgList );
			}

			// 对综合排名进行排序 开机率和正常运行率之和高的排在前面
			Collections.sort( orgList, new Comparator() {
				public int compare( Object a, Object b ) {
					String firstOrStr = (String) ( (HashMap) a ).get( "OPEN_RATE" );
					int firstOR = Integer.parseInt( firstOrStr.substring( 0, firstOrStr.length() - 1 ) );
					String secondOrStr = (String) ( (HashMap) b ).get( "OPEN_RATE" );
					int secondOR = Integer.parseInt( secondOrStr.substring( 0, secondOrStr.length() - 1 ) );
					String firstNrStr = (String) ( (HashMap) a ).get( "NORMAL_RATE" );
					int firstNR = Integer.parseInt( firstNrStr.substring( 0, firstNrStr.length() - 1 ) );
					String secondNrStr = (String) ( (HashMap) b ).get( "NORMAL_RATE" );
					int secondNR = Integer.parseInt( secondNrStr.substring( 0, secondNrStr.length() - 1 ) );
					int firstAvr = Double.valueOf( String.valueOf( ( (HashMap) a ).get( "AVERAGE_CHANNEL" ) ) )
							.intValue();
					int secondAvr = Double.valueOf( String.valueOf( ( (HashMap) b ).get( "AVERAGE_CHANNEL" ) ) )
							.intValue();
					int avr = secondAvr - firstAvr;
					int or = secondOR - firstOR;
					int nr = secondNR - firstNR;
					return or + nr + avr;
				}
			} );

			for ( int i = 0; i < orgList.size(); i++ ) {

				HashMap<String, String> oneOrg = orgList.get( i );
				String multiOrgName = oneOrg.get( "ORG_NAME" );

				HashMap<String, String> openRateMap = openRateMapList.get( i );
				String openRateName = openRateMap.get( "ORG_NAME" );
				String openRate = openRateMap.get( "OPEN_RATE" );

				HashMap<String, String> normalRateMap = normalRateMapList.get( i );
				String normalRateOrgName = normalRateMap.get( "ORG_NAME" );
				String normalRate = normalRateMap.get( "NORMAL_RATE" );

				HashMap<String, String> avrMap = averageChannelMapList.get( i );
				String avrOrgName = avrMap.get( "ORG_NAME" );
				String avr = String.valueOf( avrMap.get( "AVERAGE_CHANNEL" ) );

				DeviceRunningBean deviceRunningBean = new DeviceRunningBean();
				deviceRunningBean.setRank( ( i + 1 ) + "" );
// resultMap.put( "RANK", ( i + 1 ) + "" );
				deviceRunningBean.setMultiOrgName( multiOrgName );
// resultMap.put( "MULTI_ORG_NAME", multiOrgName );
				deviceRunningBean.setOpenRateOrgName( openRateName );
// resultMap.put( "OPEN_RATE_ORG_NAME", openRateName );
				if ( openRate == null || openRate.equals( "" ) ) {
					openRate = "0";
				}

				deviceRunningBean.setOpenRate( openRate );
// resultMap.put( "OPEN_RATE", openRate );
				deviceRunningBean.setNormalRateOrgName( normalRateOrgName );
// resultMap.put( "NORMAL_RATE_ORG_NAME", normalRateOrgName );
				if ( normalRate == null || normalRate.equals( "" ) ) {
					normalRate = "0";
				}
				deviceRunningBean.setNormalRate( normalRate );
// resultMap.put( "NORMAL_RATE", normalRate );

				deviceRunningBean.setAvrOrgname( avrOrgName );
				if ( avr == null || avr.equals( "" ) ) {
					avr = "0.0";
				}
				deviceRunningBean.setAvrNum( avr );
				resultList.add( deviceRunningBean );

			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

		return resultList;
}
	/**
	 * 取得频道使用Chart数据
	 * 
	 * @param selectedOrgId
	 * @param timeType
	 *            统计时间类型 （1－月度，2－年度）
	 * @param time
	 *            选择时间
	 * @return
	 */
	public List<List<String>> getChannelUseChart( String selectedOrgId, String timeType, String time ) {
		List<List<String>> resultList = new ArrayList<List<String>>();
		try {
			HashMap<String, Long> channelUseHashMap = new HashMap<String, Long>();
			if ( Define4Report.CHART_TIME_TYPE_MONTH.equals( timeType ) ) {
				channelUseHashMap = this.getMonthChannelUse( selectedOrgId, time );

			}
			else if ( Define4Report.CHART_TIME_TYPE_YEAR.equals( timeType ) ) {
				channelUseHashMap = this.getYearChannelUse( selectedOrgId, time );
			}

			List<String> dateList = this.getDateList( timeType, time );
			if ( dateList != null && dateList.size() > 0 ) {
				for ( int i = 0; i < dateList.size(); i++ ) {
					String key = dateList.get( i );
					long clickCount = ( channelUseHashMap != null && channelUseHashMap.containsKey( key ) ) ? channelUseHashMap
							.get( key ) : 0;

					List itemList = new ArrayList<String>();
					itemList.add( key );
					itemList.add( clickCount + "" );
					resultList.add( itemList );
				}
			}

		}
		catch ( Exception e ) {
			// TODO: handle exception
		}

		return resultList;

	}

	/**
	 * 取得开机率Chart数据
	 * 
	 * @param selectedOrgId
	 * @param timeType
	 *            统计时间类型 （1－月度，2－年度）
	 * @param time
	 *            选择时间
	 * @return
	 */
	public HashMap<String, List<Double>> getMachineOpenRateChart( String selectedOrgId, String timeType, String time ) {
		HashMap<String, List<Double>> resultHashMap = new HashMap<String, List<Double>>();
		try {
			ReportDAO reportDAO = new ReportDAO();

			HashMap<String, Long> machineOpenHashMap = new HashMap<String, Long>();
			HashMap<String, Long> machinePauseHashMap = new HashMap<String, Long>();
			long totalMachineCount = 0;
			String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
			if ( Define4Report.CHART_TIME_TYPE_MONTH.equals( timeType ) ) {
				String startTime = DateUtil.getStartTime( time, DateUtil.DATE_TYPE_MONTH );
				String endTime = DateUtil.getEndTime( time, DateUtil.DATE_TYPE_MONTH );
				machineOpenHashMap = this.getMonthMachineOpen( orgIds, startTime, endTime );
				machinePauseHashMap = this.getMonthMachinePause( orgIds, startTime, endTime );
				totalMachineCount = reportDAO.getOrgsMachineCount( orgIds, endTime );
			}
			else if ( Define4Report.CHART_TIME_TYPE_YEAR.equals( timeType ) ) {
				String startTime = DateUtil.getStartTime( time, DateUtil.DATE_TYPE_YEAR );
				String endTime = DateUtil.getEndTime( time, DateUtil.DATE_TYPE_YEAR );
				machineOpenHashMap = this.getYearMachineOpen( orgIds, startTime, endTime );
				machinePauseHashMap = this.getYearMachinePause( orgIds, startTime, endTime );
				totalMachineCount = reportDAO.getOrgsMachineCount( orgIds, endTime );
			}

			List<String> dateList = this.getDateList( timeType, time );
			List<Double> openRateList = new ArrayList<Double>();
			List<Double> normalRateList = new ArrayList<Double>();
			if ( dateList != null && dateList.size() > 0 ) {
				for ( int i = 0; i < dateList.size(); i++ ) {
					String key = dateList.get( i );
					long openMachineCount = ( machineOpenHashMap != null && machineOpenHashMap.containsKey( key ) ) ? machineOpenHashMap
							.get( key ) : 0;
					long pauseMachineCount = ( machinePauseHashMap != null && machinePauseHashMap.containsKey( key ) ) ? machinePauseHashMap
							.get( key ) : 0;

					// 正常运行率 = （总台数-报停台数）/ 总台数
					double doubleNormalRate = totalMachineCount == 0 ? 0
							: (double) ( totalMachineCount - pauseMachineCount ) / (double) totalMachineCount;
					// 开机率 = 开机台数/总台数
					double doubleOpenRate = totalMachineCount == 0 ? 0 : (double) openMachineCount
							/ (double) totalMachineCount;

					openRateList.add( doubleNormalRate );
					normalRateList.add( doubleOpenRate );

				}
			}

			resultHashMap.put( "openRateList", openRateList );
			resultHashMap.put( "normalRateList", normalRateList );

		}
		catch ( Exception e ) {
			// TODO: handle exception
		}

		return resultHashMap;

	}

	/**
	 * 取得月开机数据（按日统计）
	 * 
	 * @param orgIds
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Long> getMonthMachineOpen( String orgIds, String startTime, String endTime )
			throws Exception {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();

		ReportDAO reportDAO = new ReportDAO();
		HashMap[] machineOpenArray = reportDAO.getMonthMachineOpenChart( orgIds, startTime, endTime );
		if ( machineOpenArray != null && machineOpenArray.length > 0 ) {
			for ( int i = 0; i < machineOpenArray.length; i++ ) {
				String date = (String) machineOpenArray[i].get( "DATE" );
				long total = Long.valueOf( (String) machineOpenArray[i].get( "MACHINE_TOTAL" ) );
				String monthDate = DateUtil.formatDate( date, Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD,
						Define4Report.DATE_FORMAT_PATTERN_YYYYMMDD );
				resultHashMap.put( monthDate, total );
			}

		}
		return resultHashMap;
	}

	/**
	 * 取得月报停数据（按日统计）
	 * 
	 * @param orgIds
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Long> getMonthMachinePause( String orgIds, String startTime, String endTime )
			throws Exception {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();

		ReportDAO reportDAO = new ReportDAO();
		HashMap[] machineOpenArray = reportDAO.getMonthMachinePauseChart( orgIds, startTime, endTime );
		if ( machineOpenArray != null && machineOpenArray.length > 0 ) {
			for ( int i = 0; i < machineOpenArray.length; i++ ) {
				String date = (String) machineOpenArray[i].get( "DATE" );
				long total = Long.valueOf( (String) machineOpenArray[i].get( "MACHINE_TOTAL" ) );
				String monthDate = DateUtil.formatDate( date, Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD,
						Define4Report.DATE_FORMAT_PATTERN_YYYYMMDD );
				resultHashMap.put( monthDate, total );
			}

		}
		return resultHashMap;
	}

	/**
	 * 取得年开机数据（按月统计）
	 * 
	 * @param orgIds
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Long> getYearMachineOpen( String orgIds, String startTime, String endTime )
			throws Exception {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();

		ReportDAO reportDAO = new ReportDAO();
		HashMap[] machineOpenArray = reportDAO.getYearMachineOpenChart( orgIds, startTime, endTime );
		if ( machineOpenArray != null && machineOpenArray.length > 0 ) {
			for ( int i = 0; i < machineOpenArray.length; i++ ) {
				String date = (String) machineOpenArray[i].get( "DATE" );
				long total = Long.valueOf( (String) machineOpenArray[i].get( "MACHINE_TOTAL" ) );
				String monthDate = DateUtil.formatDate( date, Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD,
						Define4Report.DATE_FORMAT_PATTERN_YYYYMM );
				resultHashMap.put( monthDate, total );
			}

		}
		return resultHashMap;
	}

	/**
	 * 取得年报停数据（按月统计）
	 * 
	 * @param orgIds
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Long> getYearMachinePause( String orgIds, String startTime, String endTime )
			throws Exception {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();
		ReportDAO reportDAO = new ReportDAO();
		HashMap[] machineOpenArray = reportDAO.getYearMachinePauseChart( orgIds, startTime, endTime );
		if ( machineOpenArray != null && machineOpenArray.length > 0 ) {
			for ( int i = 0; i < machineOpenArray.length; i++ ) {
				String date = (String) machineOpenArray[i].get( "DATE" );
				long total = Long.valueOf( (String) machineOpenArray[i].get( "MACHINE_TOTAL" ) );
				String monthDate = DateUtil.formatDate( date, Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD,
						Define4Report.DATE_FORMAT_PATTERN_YYYYMM );
				resultHashMap.put( monthDate, total );
			}

		}
		return resultHashMap;
	}

	/**
	 * 取得月频道使用数据（按日统计）
	 * 
	 * @param selectedOrgId
	 * @param time
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Long> getMonthChannelUse( String selectedOrgId, String time ) throws Exception {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();

		String startTime = DateUtil.getStartTime( time, DateUtil.DATE_TYPE_MONTH );
		String endTime = DateUtil.getEndTime( time, DateUtil.DATE_TYPE_MONTH );

		ReportDAO reportDAO = new ReportDAO();
		String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
		HashMap[] monthChannelUseArray = reportDAO.getMonthChannelUseChart( orgIds, startTime, endTime );
		if ( monthChannelUseArray != null && monthChannelUseArray.length > 0 ) {
			for ( int i = 0; i < monthChannelUseArray.length; i++ ) {
				String date = (String) monthChannelUseArray[i].get( "DATE" );
				long total = Long.valueOf( (String) monthChannelUseArray[i].get( "TOTAL" ) );
				String monthDate = DateUtil.formatDate( date, Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD,
						Define4Report.DATE_FORMAT_PATTERN_YYYYMMDD );
				resultHashMap.put( monthDate, total );
			}

		}
		return resultHashMap;
	}

	/**
	 * 取得年频道使用数据（按月统计）
	 * 
	 * @param selectedOrgId
	 * @param time
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, Long> getYearChannelUse( String selectedOrgId, String time ) throws Exception {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();

		String startTime = DateUtil.getStartTime( time, DateUtil.DATE_TYPE_YEAR );
		String endTime = DateUtil.getEndTime( time, DateUtil.DATE_TYPE_YEAR );

		ReportDAO reportDAO = new ReportDAO();
		String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
		HashMap[] yearChannelUseArray = reportDAO.getYearChannelUseChart( orgIds, startTime, endTime );
		if ( yearChannelUseArray != null && yearChannelUseArray.length > 0 ) {
			for ( int i = 0; i < yearChannelUseArray.length; i++ ) {
				String date = (String) yearChannelUseArray[i].get( "DATE" );
				long total = Long.valueOf( (String) yearChannelUseArray[i].get( "TOTAL" ) );
				String yearMonth = DateUtil.formatDate( date, Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD,
						Define4Report.DATE_FORMAT_PATTERN_YYYYMM );
				resultHashMap.put( yearMonth, total );
			}

		}
		return resultHashMap;
	}

	/**
	 * 取得年对应的月份/月的所有日期
	 * 
	 * @param timeType
	 * @param time
	 * @return yyyyMMdd/yyyyMM的集合
	 * @throws Exception
	 */
	public List<String> getDateList( String timeType, String time ) {
		List<String> resultList = new ArrayList<String>();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat( Define4Report.DATE_FORMAT_PATTERN_YYYY_MM_DD );
			Date date = sdf.parse( time );
			if ( Define4Report.CHART_TIME_TYPE_MONTH.equals( timeType ) ) {
				SimpleDateFormat outSdf = new SimpleDateFormat( Define4Report.DATE_FORMAT_PATTERN_YYYYMMDD );
				Calendar cal = Calendar.getInstance();
				cal.setTime( date );
				cal.set( Calendar.DATE, 1 );
				int month = cal.get( Calendar.MONTH );
				while ( cal.get( Calendar.MONTH ) == month ) {
					Date monthDate = cal.getTime();
					resultList.add( outSdf.format( monthDate ) );
					cal.add( Calendar.DATE, 1 );
				}
			}
			else if ( Define4Report.CHART_TIME_TYPE_YEAR.equals( timeType ) ) {
				SimpleDateFormat outSdf = new SimpleDateFormat( Define4Report.DATE_FORMAT_PATTERN_YYYYMM );
				Calendar cal = Calendar.getInstance();
				cal.setTime( date );
				cal.set( Calendar.MONTH, 0 );
				int year = cal.get( Calendar.YEAR );
				while ( cal.get( Calendar.YEAR ) == year ) {
					Date monthDate = cal.getTime();
					resultList.add( outSdf.format( monthDate ) );
					cal.add( Calendar.MONTH, 1 );
				}

			}
		}
		catch ( Exception e ) {
			// TODO: handle exception
		}

		return resultList;

	}

}
