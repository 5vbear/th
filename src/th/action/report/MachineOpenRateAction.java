/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.report;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import th.action.BaseAction;
import th.action.MonitorAction;
import th.com.property.LocalPropertiesException;
import th.com.util.Define4Report;
import th.dao.ReportDAO;
import th.entity.MachineOpenDetailBean;
import th.entity.MachineOpenRateBean;

/**
 * 开机率统计处理类
 * 
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 * 
 */
public class MachineOpenRateAction extends BaseAction {
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
	public ArrayList<MachineOpenRateBean> getSummaryReport( String orgIds, String startTime, String endTime,
			String sortKey ) {
		// 取得组织信息(orgId和orgName)
		ArrayList<MachineOpenRateBean> machineOpenRateList = this.getOrganizations( orgIds );
		// 根据时间段取得汇总数据
		HashMap<String, Long> totalMachineCountMap = this.getTotalMachineCount( orgIds, endTime );
		HashMap<String, String> openMap = this.getOpen( orgIds, startTime, endTime );
		HashMap<String, String> pauseMap = this.getPause( orgIds, startTime, endTime );

		// 遍历组织、在汇总数据中取得对应的统计结果
		if ( machineOpenRateList.size() > 0 ) {
			for ( int i = 0; i < machineOpenRateList.size(); i++ ) {
				MachineOpenRateBean machineOpenRate = machineOpenRateList.get( i );
				String orgId = machineOpenRate.getOrgId();
				List<String> orgIdList = ReportCommonAction.getOrgIdChildIds( orgId );

				long openCount = 0;
				long openMachineCount = 0;
				long pauseCount = 0;
				long pauseMachineCount = 0;
				long totalMachineCount = 0;
				if ( orgIdList != null && orgIdList.size() > 0 ) {
					for ( int orgIndex = 0; orgIndex < orgIdList.size(); orgIndex++ ) {
						String id = orgIdList.get( orgIndex );
						long orgTotalMachineCount = totalMachineCountMap.containsKey( id ) ? totalMachineCountMap
								.get( id ) : 0;
						String open = openMap.containsKey( id ) ? openMap.get( id ) : "";
						long orgOpenCount = open.indexOf( "_" ) > 0 ? Long.valueOf( open.split( "_" )[0] ) : 0;
						long orgOpenMachineCount = open.indexOf( "_" ) > 0 ? Long.valueOf( open.split( "_" )[1] ) : 0;

						String pause = pauseMap.containsKey( id ) ? pauseMap.get( id ) : "";
						long orgPauseCount = pause.indexOf( "_" ) > 0 ? Long.valueOf( pause.split( "_" )[0] ) : 0;
						long orgPauseMachineCount = pause.indexOf( "_" ) > 0 ? Long.valueOf( pause.split( "_" )[1] )
								: 0;

						openCount = openCount + orgOpenCount;
						openMachineCount = openMachineCount + orgOpenMachineCount;
						pauseCount = pauseCount + orgPauseCount;
						pauseMachineCount = pauseMachineCount + orgPauseMachineCount;
						totalMachineCount = totalMachineCount + orgTotalMachineCount;

					}

				}

				// 总台次 = 开机台次 + 报停台次
				long totalCount = openCount + pauseCount;
				// 正常运行率 = （总台数-报停台数）/ 总台数
				double doubleNormalRate = totalMachineCount == 0 ? 0
						: (double) ( totalMachineCount - pauseMachineCount ) / (double) totalMachineCount;
				String normalRate = ReportCommonAction.decimalFormat( doubleNormalRate );
				// 开机率 = 开机台数/总台数
				double doubleOpenRate = totalMachineCount == 0 ? 0 : (double) openMachineCount
						/ (double) totalMachineCount;
				String openRate = ReportCommonAction.decimalFormat( doubleOpenRate );

				machineOpenRate.setTotalCount( totalCount );
				machineOpenRate.setTotalMachineCount( totalMachineCount );
				machineOpenRate.setOpenCount( openCount );
				machineOpenRate.setOpenMachineCount( openMachineCount );
				machineOpenRate.setPauseCount( pauseCount );
				machineOpenRate.setPauseMachineCount( pauseMachineCount );
				machineOpenRate.setDoubleNormalRate( doubleNormalRate );
				machineOpenRate.setDoubleOpenRate( doubleOpenRate );
				machineOpenRate.setNormalRate( normalRate );
				machineOpenRate.setOpenRate( openRate );
				machineOpenRate.setSortKey( sortKey );

			}

		}

		// 排序
		Collections.sort( machineOpenRateList );

		return machineOpenRateList;

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
	 * @throws LocalPropertiesException 
	 */
	public ArrayList<MachineOpenDetailBean> getDetailReport( String selectedOrgId, String startTime, String endTime,
			String sortKey ) throws LocalPropertiesException {
		// 取得组织机器信息(orgId、orgName、MachineId、machineName、currentStatus)
		ArrayList<MachineOpenDetailBean> machineOpenDetailList = this
				.getOrgMachines( selectedOrgId, startTime, endTime );
		// 根据时间段取得汇总数据
		HashMap<String, Long> openMap = this.getMachineOpen( selectedOrgId, startTime, endTime );
		HashMap<String, Long> pauseMap = this.getMachinePause( selectedOrgId, startTime, endTime );

		// 遍历组织、在汇总数据中取得对应的统计结果
		if ( machineOpenDetailList.size() > 0 ) {
			for ( int i = 0; i < machineOpenDetailList.size(); i++ ) {
				MachineOpenDetailBean machineOpenDetail = machineOpenDetailList.get( i );
				String machineId = machineOpenDetail.getMachineId();
				long openCount = openMap.containsKey( machineId ) ? openMap.get( machineId ) : 0;
				long pauseCount = pauseMap.containsKey( machineId ) ? pauseMap.get( machineId ) : 0;

				// 总次数 = 开机次数 + 报停次数
				long totalCount = openCount + pauseCount;
				// 正常运行率 = 开机次数/ 总次数
				double doubleNormalRate = totalCount == 0 ? 0 : (double) openCount / (double) totalCount;
				String normalRate = ReportCommonAction.decimalFormat( doubleNormalRate );

				machineOpenDetail.setOpenCount( openCount );
				machineOpenDetail.setPauseCount( pauseCount );
				machineOpenDetail.setDoubleNormalRate( doubleNormalRate );
				machineOpenDetail.setNormalRate( normalRate );
				machineOpenDetail.setSortKey( sortKey );

			}

		}

		// 排序
		Collections.sort( machineOpenDetailList );

		return machineOpenDetailList;

	}

	/**
	 * 取得组织的机器的开机台数和开机台次
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 * @param endTime
	 * @return HashMap<key, value> key:orgId;value:openCount_openMachineCount(开机台次_开机台数)
	 */
	private HashMap<String, String> getOpen( String selectedOrgId, String startTime, String endTime ) {
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
			HashMap[] openMachineMapArray = reportDAO.getMachineOpenSummary( orgIds, startTime, endTime );
			HashMap<String, Long> orgOpenCountMap = new HashMap<String, Long>();
			HashMap<String, Long> orgOpenMachineCountMap = new HashMap<String, Long>();
			if ( openMachineMapArray != null && openMachineMapArray.length > 0 ) {
				for ( int i = 0; i < openMachineMapArray.length; i++ ) {
					HashMap openMachineMap = openMachineMapArray[i];
					String orgId = (String) openMachineMap.get( "ORG_ID" );
					long total = Long.valueOf( (String) openMachineMap.get( "TOTAL" ) );

					// 开机台次
					long openCount = total;
					if ( orgOpenCountMap.containsKey( orgId ) ) {
						openCount = orgOpenCountMap.get( orgId ) + total;
					}
					orgOpenCountMap.put( orgId, openCount );

					// 开机台数
					long openMachineCount = 1;
					if ( orgOpenMachineCountMap.containsKey( orgId ) ) {
						openMachineCount = orgOpenMachineCountMap.get( orgId ) + 1;
					}
					orgOpenMachineCountMap.put( orgId, openMachineCount );

					String resultMapVaule = openCount + "_" + openMachineCount;
					resultHashMap.put( orgId, resultMapVaule );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultHashMap;

	}

	/**
	 * 取得组织的机器的总台数（审核通过的机器数量，且没有报停的机器。端机信息管理表机器创建时间<小于时间段的最大时间。）
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 * @param endTime
	 * @return HashMap<key, value> key:orgId;value:total(总台数)
	 */
	private HashMap<String, Long> getTotalMachineCount( String selectedOrgId, String endTime ) {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();

		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
			HashMap[] orgMachines = reportDAO.getOrgMachineCount( orgIds, endTime );
			if ( orgMachines != null && orgMachines.length > 0 ) {
				for ( int i = 0; i < orgMachines.length; i++ ) {
					HashMap orgMachine = orgMachines[i];
					String orgId = (String) orgMachine.get( "ORG_ID" );
					long total = Long.valueOf( (String) orgMachine.get( "TOTAL" ) );
					resultHashMap.put( orgId, total );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultHashMap;

	}

	/**
	 * 取得组织的机器的报停台次和报停台数 报停台次：机器A第一次报停，报停+1，当前报停台数+1； 机器A第二次报停，报停+1，当前报停台数+0；
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 * @param endTime
	 * @return HashMap<key, value> key:orgId;value:pauseCount_pauseMachineCount(报停台次_报停台数)
	 */
	private HashMap<String, String> getPause( String selectedOrgId, String startTime, String endTime ) {
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
			HashMap[] pauseMachineMapArray = reportDAO.getMachinePauseSummary( orgIds, startTime, endTime );
			HashMap<String, Long> orgPauseCountMap = new HashMap<String, Long>();
			HashMap<String, Long> orgPauseMachineCountMap = new HashMap<String, Long>();
			if ( pauseMachineMapArray != null && pauseMachineMapArray.length > 0 ) {
				for ( int i = 0; i < pauseMachineMapArray.length; i++ ) {
					HashMap pauseMachineMap = pauseMachineMapArray[i];
					String orgId = (String) pauseMachineMap.get( "ORG_ID" );
					long total = Long.valueOf( (String) pauseMachineMap.get( "TOTAL" ) );

					// 报停台次
					long pauseCount = total;
					if ( orgPauseCountMap.containsKey( orgId ) ) {
						pauseCount = orgPauseCountMap.get( orgId ) + total;
					}
					orgPauseCountMap.put( orgId, pauseCount );

					// 报停台数
					long pauseMachineCount = 1;
					if ( orgPauseMachineCountMap.containsKey( orgId ) ) {
						pauseMachineCount = orgPauseMachineCountMap.get( orgId ) + 1;
					}
					orgPauseMachineCountMap.put( orgId, pauseMachineCount );

					String resultMapVaule = pauseCount + "_" + pauseMachineCount;
					resultHashMap.put( orgId, resultMapVaule );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultHashMap;

	}

	/**
	 * 取得组织
	 * 
	 * @param selectedOrgId
	 * @return
	 */
	private ArrayList<MachineOpenRateBean> getOrganizations( String selectedOrgId ) {
		ArrayList<MachineOpenRateBean> machineOpenRateList = new ArrayList<MachineOpenRateBean>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getFirstOrgIdsByParentId( selectedOrgId );
			HashMap[] orgs = reportDAO.getOrgsByIds( orgIds );
			if ( orgs != null && orgs.length > 0 ) {
				for ( int i = 0; i < orgs.length; i++ ) {
					HashMap org = orgs[i];
					String orgId = (String) org.get( "ORG_ID" );
					String orgName = (String) org.get( "ORG_NAME" );
					MachineOpenRateBean machineOpenRate = new MachineOpenRateBean();
					machineOpenRate.setOrgId( orgId );
					machineOpenRate.setOrgName( orgName );
					machineOpenRateList.add( machineOpenRate );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return machineOpenRateList;

	}

	/**
	 * 取得组织机器
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws LocalPropertiesException 
	 */
	private ArrayList<MachineOpenDetailBean> getOrgMachines( String selectedOrgId, String startTime, String endTime ) throws LocalPropertiesException {
		ArrayList<MachineOpenDetailBean> machineOpenDetailList = new ArrayList<MachineOpenDetailBean>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			MonitorAction monitorAction = new MonitorAction();
			String orgIds = ReportCommonAction.getFirstOrgIdsByParentId( selectedOrgId );
			HashMap[] orgMachines = reportDAO.getOrgMachine( orgIds, startTime, endTime );
			if ( orgMachines != null && orgMachines.length > 0 ) {
				for ( int i = 0; i < orgMachines.length; i++ ) {
					HashMap orgMachine = orgMachines[i];
					String orgId = (String) orgMachine.get( "ORG_ID" );
					String orgName = (String) orgMachine.get( "ORG_NAME" );
					String machineId = (String) orgMachine.get( "MACHINE_ID" );
					String machineName = (String) orgMachine.get( "MACHINE_MARK" );
					String currentStatus = (String) orgMachine.get( "STATUS" );

					if(monitorAction.isMachineBreak(machineId)){
						currentStatus = Define4Report.MACHINE_STATUS_OUTLINE;
					}
					
					MachineOpenDetailBean machineOpenDetail = new MachineOpenDetailBean();
					machineOpenDetail.setOrgId( orgId );
					machineOpenDetail.setOrgName( orgName );
					machineOpenDetail.setMachineId( machineId );
					machineOpenDetail.setMachineName( machineName );
					machineOpenDetail.setCurrentStatus( Long.valueOf( currentStatus ) );
					if ( Define4Report.MACHINE_STATUS_SERVICE.equals( currentStatus ) ) {
						machineOpenDetail.setCurrentStatusValue( Define4Report.MACHINE_STATUS_SERVICE_VALUE );
					}
					else if ( Define4Report.MACHINE_STATUS_PAUSE.equals( currentStatus ) ) {
						machineOpenDetail.setCurrentStatusValue( Define4Report.MACHINE_STATUS_PAUSE_VALUE );
					}
					else if ( Define4Report.MACHINE_STATUS_ADVERTISING.equals( currentStatus ) ) {
						machineOpenDetail.setCurrentStatusValue( Define4Report.MACHINE_STATUS_ADVERTISING_VALUE );
					}
					else if ( Define4Report.MACHINE_STATUS_UNAPPROVED.equals( currentStatus ) ) {
						machineOpenDetail.setCurrentStatusValue( Define4Report.MACHINE_STATUS_UNAPPROVED_VALUE );
					}
					else if ( Define4Report.MACHINE_STATUS_OUTLINE.equals( currentStatus ) ) {
						machineOpenDetail.setCurrentStatusValue( Define4Report.MACHINE_STATUS_OUTLINE_VALUE );
					}
					machineOpenDetailList.add( machineOpenDetail );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return machineOpenDetailList;

	}

	/**
	 * 取得组织的机器的开机次数
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 * @param endTime
	 * @return HashMap<key, value> key:machineId;value:openCount
	 */
	private HashMap<String, Long> getMachineOpen( String selectedOrgId, String startTime, String endTime ) {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
			HashMap[] openMachineMapArray = reportDAO.getMachineOpenSummary( orgIds, startTime, endTime );
			if ( openMachineMapArray != null && openMachineMapArray.length > 0 ) {
				for ( int i = 0; i < openMachineMapArray.length; i++ ) {
					HashMap pauseMachineMap = openMachineMapArray[i];
					String machineId = (String) pauseMachineMap.get( "MACHINE_ID" );
					long total = Long.valueOf( (String) pauseMachineMap.get( "TOTAL" ) );

					resultHashMap.put( machineId, total );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultHashMap;

	}

	/**
	 * 取得组织的机器的报停台次和报停次数
	 * 
	 * @param selectedOrgId
	 * @param startTime
	 * @param endTime
	 * @return HashMap<key, value> key:machineId;value:pauseCount
	 */
	private HashMap<String, Long> getMachinePause( String selectedOrgId, String startTime, String endTime ) {
		HashMap<String, Long> resultHashMap = new HashMap<String, Long>();
		try {
			ReportDAO reportDAO = new ReportDAO();
			String orgIds = ReportCommonAction.getOrgIdsByParentId( selectedOrgId );
			HashMap[] pauseMachineMapArray = reportDAO.getMachinePauseSummary( orgIds, startTime, endTime );
			if ( pauseMachineMapArray != null && pauseMachineMapArray.length > 0 ) {
				for ( int i = 0; i < pauseMachineMapArray.length; i++ ) {
					HashMap pauseMachineMap = pauseMachineMapArray[i];
					String machineId = (String) pauseMachineMap.get( "MACHINE_ID" );
					long total = Long.valueOf( (String) pauseMachineMap.get( "TOTAL" ) );

					// 报停次数
					resultHashMap.put( machineId, total );

				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}

		return resultHashMap;

	}

}
