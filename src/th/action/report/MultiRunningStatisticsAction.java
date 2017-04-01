/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.report;

import java.math.BigDecimal;
import java.sql.SQLException;
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
import th.entity.AdvertPlayBean;
import th.entity.ChannelUseBean;
import th.entity.ChannelUseOrgBean;
import th.entity.DeviceRunningBean;
import th.entity.MachineOpenRateBean;
import th.entity.MultiRunningStatisticsBean;
import th.util.DateUtil;

/**
 * Descriptions
 * 
 * @version 2013-8-31
 * @author PSET
 * @since JDK1.6
 * 
 */
public class MultiRunningStatisticsAction extends BaseAction {
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
	// 与设备运行考核表中同名方法功能不同
	private List<HashMap<String, String>> fillResultList( List<HashMap<String, String>> targetList,
			List<HashMap<String, String>> sourceList ) {
		if ( targetList == null || sourceList == null ) {
			return null; // 此时有问题
		}
		List<HashMap<String, String>> newTargetList = new ArrayList<HashMap<String, String>>();
		if ( targetList.size() == 0 ) {
			newTargetList = sourceList;
		}
		else {
			for ( int i = 0; i < sourceList.size(); i++ ) {
				HashMap<String, String> sourceMap = sourceList.get( i );
				String sourceOrgId = (String) sourceMap.get( "ORG_ID" );
				boolean toFill = false;
				HashMap<String, String> tempTargetMap = new HashMap<String, String>();

				for ( int j = 0; j < targetList.size(); j++ ) {
					HashMap<String, String> targetMap = targetList.get( j );
					String targetOrgId = (String) targetMap.get( "ORG_ID" );
					if ( sourceOrgId.equals( targetOrgId ) ) {
						tempTargetMap = targetMap;
						toFill = true;
						break;
					}
					toFill = false;
				}
				if ( toFill ) {
					newTargetList.add( tempTargetMap );
				}
				else {
					newTargetList.add( sourceMap );
				}

			}
		}

		targetList = newTargetList;

		return targetList;
	}

	//
	public List<MultiRunningStatisticsBean> getResult(String selectedOrgId,
			String startEndTime ) throws SQLException{
		
		String startTime = startEndTime.split( "_" )[0];
		String endTime = startEndTime.split( "_" )[1];

		OrgDealAction orgDealAction = new OrgDealAction();
		List<HashMap<String, String>> orgNodesList = orgDealAction.getFirstChildNodesByOrgId( Long
				.parseLong( selectedOrgId ) );
		List<HashMap<String, String>> orgList = new ArrayList<HashMap<String, String>>();
		for ( int i = 0; i < orgNodesList.size(); i++ ) {
			HashMap oneNode = orgNodesList.get( i );
			HashMap oneOrg = new HashMap();
			oneOrg.put( "ORG_ID", oneNode.get( "ORG_ID" ) );
			oneOrg.put( "ORG_NAME", oneNode.get( "ORG_NAME" ) );
			orgList.add( oneOrg );
		}



		MachineOpenRateAction mor = new MachineOpenRateAction();
		String sortKey = "orgId";
		ArrayList<MachineOpenRateBean> openRateList = mor.getSummaryReport( selectedOrgId, startTime, endTime,
				sortKey );
		List<HashMap<String, String>> openRateMapList = new ArrayList();
		for ( int i = 0; i < openRateList.size(); i++ ) {
			MachineOpenRateBean machineOpenRateBean = openRateList.get( i );
			HashMap openRateMap = new HashMap();
			openRateMap.put( "ORG_ID", machineOpenRateBean.getOrgId() );
			openRateMap.put( "ORG_NAME", machineOpenRateBean.getOrgName() );
			openRateMap.put( "OPEN_RATE", machineOpenRateBean.getOpenRate() );
			openRateMapList.add( openRateMap );
		}
		openRateMapList = fillResultList( openRateMapList, orgList );

		AdvertPlayAction ap = new AdvertPlayAction();
		ArrayList<AdvertPlayBean> advertPlayList = ap.getSummaryReport( selectedOrgId, startTime, endTime, sortKey, "", "", "", "" );
		List<HashMap<String, String>> advertPlayMapList = new ArrayList();
		for ( int i = 0; i < advertPlayList.size(); i++ ) {
			AdvertPlayBean advertPlayBean = advertPlayList.get( i );
			HashMap advertPlayMap = new HashMap();
			advertPlayMap.put( "ORG_ID", advertPlayBean.getOrgId() );
			advertPlayMap.put( "ORG_NAME", advertPlayBean.getOrgName() );
			// 日均广告播放时长
			advertPlayMap.put( "AVERAGE_PLAY_TIME", String.valueOf( advertPlayBean.getAveragePlayTime() ) );// 平均和日均是不是一个概念？
			// 日均广告点击
			advertPlayMap.put( "AVERAGE_CLICK_COUNT", String.valueOf( advertPlayBean.getAverageClickCount() ) );// 平均和日均是不是一个概念？
			advertPlayMapList.add( advertPlayMap );
		}
		advertPlayMapList = fillResultList( advertPlayMapList, orgList );

		ChannelUseAction cu = new ChannelUseAction();
		ArrayList<String> channelList = cu.getAllChannels();
		// ArrayList<String> tableHeaderList = cu.getReportTableHeader( channelList, Define4Report.DATA_TYPE_SUMMARY
	//);
		ArrayList<ChannelUseOrgBean> averageCountList = cu.getSummaryReport( selectedOrgId, channelList, startTime,
				endTime, sortKey, "" );

		List<HashMap<String, String>> averageCountMapList = new ArrayList();
		for ( int i = 0; i < averageCountList.size(); i++ ) {
			ChannelUseOrgBean channelUseOrgBean = averageCountList.get( i );
			HashMap averageCountMap = new HashMap();
			averageCountMap.put( "ORG_ID", channelUseOrgBean.getOrgId() );
			averageCountMap.put( "ORG_NAME", channelUseOrgBean.getOrgName() );
			// 台日均频道点击
			averageCountMap.put( "AVERAGE_COUNT", String.valueOf( channelUseOrgBean.getAverageCount() ) );// 平均和日均是不是一个概念？
			averageCountMapList.add( averageCountMap );
		}
		averageCountMapList = fillResultList( averageCountMapList, orgList );

		ReportDAO reportDAO = new ReportDAO();
		String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
		HashMap[] channelClickMapArray = reportDAO.getChannelClickSummary( orgIds, startTime, endTime );

		List<HashMap<String, String>> channelClickMapList = new ArrayList();
		for ( int i = 0; i < channelClickMapArray.length; i++ ) {
			HashMap channelClickMap = channelClickMapArray[i];
			HashMap oneMap = new HashMap();
			oneMap.put( "ORG_ID", channelClickMap.get( "ORG_ID" ) );
			oneMap.put( "ORG_NAME", channelClickMap.get( "ORG_NAME" ) );

			oneMap.put( "MAX_CLICK_COUNT", channelClickMap.get( "MAX_CLICK_COUNT" ) + "" );
			oneMap.put( "MIN_CLICK_COUNT", channelClickMap.get( "MIN_CLICK_COUNT" ) + "" );
			// 台日最高最低频道点击
			channelClickMapList.add( channelClickMap );
		}

		channelClickMapList = fillResultList( channelClickMapList, orgList );

		List<MultiRunningStatisticsBean> resultList = new ArrayList<MultiRunningStatisticsBean>();
		for ( int i = 0; i < orgList.size(); i++ ) {

			HashMap<String, String> oneOrg = orgList.get( i );
			String multiOrgName = oneOrg.get( "ORG_NAME" );

			HashMap<String, String> openRateMap = openRateMapList.get( i );
			String openRateName = openRateMap.get( "ORG_NAME" );
			String openRate = openRateMap.get( "OPEN_RATE" );

			HashMap<String, String> advertPlayMap = advertPlayMapList.get( i );
	//String advertPlayName = advertPlayMap.get( "ORG_NAME" );
			String averagePlayTime = advertPlayMap.get( "AVERAGE_PLAY_TIME" );
			String averageClickCount = advertPlayMap.get( "AVERAGE_CLICK_COUNT" );

			HashMap<String, String> averageCountMap = averageCountMapList.get( i );
			String averageCountName = averageCountMap.get( "ORG_NAME" );
			String averageCount = averageCountMap.get( "AVERAGE_COUNT" );

			HashMap<String, String> channelClickMap = channelClickMapList.get( i );

			String maxClickCount = channelClickMap.get( "MAX_CLICK_COUNT" );
			String minClickCount = channelClickMap.get( "MIN_CLICK_COUNT" );

			MultiRunningStatisticsBean multiRunningStatisticsBean = new MultiRunningStatisticsBean();
			multiRunningStatisticsBean.setRank( ( i + 1 ) + "" );
	//resultMap.put( "RANK", ( i + 1 ) + "" );
			multiRunningStatisticsBean.setMultiOrgName( multiOrgName );
	//resultMap.put( "MULTI_ORG_NAME", multiOrgName );
	//resultMap.put( "OPEN_RATE_ORG_NAME", openRateName );
			if ( openRate == null || openRate.equals( "" ) ) {
				openRate = "0";
			}
			multiRunningStatisticsBean.setOpenRate( openRate );
	//resultMap.put( "OPEN_RATE", openRate );

	//resultMap.put( "ADVERT_PLAY_ORG_NAME", advertPlayName );

			if ( averagePlayTime == null || averagePlayTime.equals( "" ) ) {
				averagePlayTime = "0";
			}
			multiRunningStatisticsBean.setAveragePlayTime( averagePlayTime );
	//resultMap.put( "AVERAGE_PLAY_TIME", averagePlayTime );

			if ( averageClickCount == null || averageClickCount.equals( "" ) ) {
				averageClickCount = "0";
			}
			multiRunningStatisticsBean.setAverageClickCount( averageClickCount );
	//resultMap.put( "AVERAGE_CLICK_COUNT", averageClickCount );

			if ( averageCount == null || averageCount.equals( "" ) ) {
				averageCount = "0";
			}
			multiRunningStatisticsBean.setAverageCount( averageCount );
	//resultMap.put( "AVERAGE_COUNT", averageCount );

			if ( maxClickCount == null || maxClickCount.equals( "" ) ) {
				maxClickCount = "0";
			}
			else {
				maxClickCount = maxClickCount + "";
			}
			multiRunningStatisticsBean.setMaxClickCount( maxClickCount );
	//resultMap.put( "MAX_CLICK_COUNT", maxClickCount );

			if ( minClickCount == null || minClickCount.equals( "" ) ) {
				minClickCount = "0";
			}
			else {
				minClickCount = minClickCount + "";
			}
			multiRunningStatisticsBean.setMinClickCount( minClickCount );
	//resultMap.put( "MIN_CLICK_COUNT", minClickCount );

			resultList.add( multiRunningStatisticsBean );
		}
		return resultList;
	}
}
