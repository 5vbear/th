/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import th.action.report.ReportCommonAction;
import th.entity.ClientBean;
import th.entity.ClientUseBean;
import th.entity.PageBean;
import th.user.User;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ClientDAO extends BaseDao {

	public List<ClientBean> getClientInfo( String userId ) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "	SELECT DISTINCT A.MACHINE_NAME,				" );
		stringBuffer.append( "	B.CPU_FREQUENCY,B.OS,				" );
		stringBuffer.append( "	B.DISK_SIZE,B.MEMORY_SIZE,			" );
		stringBuffer.append( "	B.VERSION							" );
		stringBuffer.append( "	FROM TB_CCB_MACHINE A,				" );
		stringBuffer.append( "	TB_MACHINE_ENVIRONMENT B			" );
		stringBuffer.append( "	WHERE A.MACHINE_ID = B.MACHINE_ID	" );

		String allOrgIds = this.getOrgInfo( userId );
		String orgId = this.getUseOrgInfo( userId );
		if ( "''".equals( allOrgIds ) ) {
			allOrgIds = "'" + orgId + "'";
		}
		else {
			allOrgIds = allOrgIds + ",'" + orgId + "'";
		}
		stringBuffer.append( "	AND A.ORG_ID IN (" );
		stringBuffer.append( allOrgIds );
		stringBuffer.append( ")" );
		stringBuffer.append( "	ORDER BY A.MACHINE_NAME	" );
		ResultSet rs = null;
		List<ClientBean> list = new ArrayList<ClientBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					ClientBean clientBean = new ClientBean();
					clientBean.setCpuFrequency( rs.getString( "CPU_FREQUENCY" ) );
					clientBean.setDiskMemory( rs.getString( "DISK_SIZE" ) );
					clientBean.setMachineName( rs.getString( "MACHINE_NAME" ) );
					clientBean.setOs( rs.getString( "OS" ) );
					clientBean.setRamMemory( rs.getString( "MEMORY_SIZE" ) );
					clientBean.setVersion( rs.getString( "VERSION" ) );
					list.add( clientBean );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public String getOrgInfo( String orgId ) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "	SELECT 								" );
		stringBuffer.append( "	 SUBORG (O.ORG_ID) AS SUB_ORG_ID	" );
		stringBuffer.append( "	FROM								" );
		stringBuffer.append( "		TB_CCB_ORGANIZATION O			" );
		stringBuffer.append( "	WHERE  								" );
		stringBuffer.append( "		O.ORG_ID = 					   '" );
		stringBuffer.append( orgId );
		stringBuffer.append( "'" );
		ResultSet rs = null;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		String allOrgIds = "";
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				if ( rs.next() ) {
					allOrgIds = rs.getString( "sub_org_id" );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		allOrgIds = allOrgIds.replace( ",", "','" );
		allOrgIds = "'" + allOrgIds + "'";
		return allOrgIds;
	}

	public String getUseOrgInfo( String userId ) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "	SELECT 								" );
		stringBuffer.append( "	 ORG_ID								" );
		stringBuffer.append( "	FROM								" );
		stringBuffer.append( "		TB_CCB_USER			" );
		stringBuffer.append( "	WHERE  								" );
		stringBuffer.append( "		USER_ID = 					   '" );
		stringBuffer.append( userId );
		stringBuffer.append( "'" );
		ResultSet rs = null;
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		String orgId = "";
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				if ( rs.next() ) {
					orgId = rs.getString( "ORG_ID" );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		return orgId;
	}

	public List<String> getOsList() {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "	SELECT DISTINCT OS			" );
		stringBuffer.append( "	FROM TB_MACHINE_ENVIRONMENT	" );

		ResultSet rs = null;
		List<String> list = new ArrayList<String>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					list.add( rs.getString( "OS" ) );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<ClientBean> searchClientInfo( String orgId, String selectedOs, String softwareVersion, String cpuFreq,
			String diskSize, String ramSize, String userId, String macType,String macName ) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "	SELECT  DISTINCT A.MACHINE_MARK,				" );
		stringBuffer.append( "	B.CPU_FREQUENCY,B.OS,B.MACHINE_KIND,				" );
		stringBuffer.append( "	B.DISK_SIZE,B.MEMORY_SIZE,			" );
		stringBuffer.append( "	B.VERSION,B.MACHINE_TYPE							" );
		stringBuffer.append( "	FROM TB_CCB_MACHINE A,				" );
		stringBuffer.append( "	TB_MACHINE_ENVIRONMENT B			" );
		stringBuffer.append( "	WHERE A.MACHINE_ID = B.MACHINE_ID	" );
		if ( !"allOrgIds".equals( orgId ) && !"-".equals( orgId.trim() ) ) {
			stringBuffer.append( "	AND A.ORG_ID IN				   (" );

			String allOrgIds = ReportCommonAction.getFirstOrgIdsByParentId( orgId );
			if ( null==allOrgIds ) {
				return new ArrayList<ClientBean>();
			}
			stringBuffer.append( allOrgIds );
			stringBuffer.append( ")									" );
		}
		if ( null != selectedOs && !"".equals( selectedOs.trim() ) && !"-".equals( selectedOs ) ) {
			stringBuffer.append( "	AND B.OS = 	'" );
			stringBuffer.append( selectedOs );
			stringBuffer.append( "'				 " );
		}
		if ( null != softwareVersion && !"".equals( softwareVersion.trim() ) ) {
			stringBuffer.append( "	AND B.VERSION = 	'" );
			stringBuffer.append( softwareVersion );
			stringBuffer.append( "'				 		 " );
		}
		if ( null != cpuFreq && !"".equals( cpuFreq.trim() ) ) {
			stringBuffer.append( "	AND B.CPU_FREQUENCY = 	'" );
			stringBuffer.append( cpuFreq );
			stringBuffer.append( "'				 			 " );
		}
		if ( null != diskSize && !"".equals( diskSize.trim() ) ) {
			stringBuffer.append( "	AND B.DISK_SIZE = 	'" );
			stringBuffer.append( diskSize );
			stringBuffer.append( "'				 	     " );
		}
		if ( null != ramSize && !"".equals( ramSize.trim() ) ) {
			stringBuffer.append( "	AND B.MEMORY_SIZE = 	'" );
			stringBuffer.append( ramSize );
			stringBuffer.append( "'				 	     " );
		}
		if ( null != macName && !"".equals( macName.trim() ) ) {
			stringBuffer.append( "	AND A.MACHINE_MARK like 	'%" );
			stringBuffer.append( macName.toUpperCase() );
			stringBuffer.append( "%'				 		 " );
		}
		if(!"0".equals(macType)){
			String[] typeArray = macType.split("_");
			stringBuffer.append( "	AND B.OS like 	'%" );
			stringBuffer.append( typeArray[0] );
			stringBuffer.append( "%' " );
			stringBuffer.append( "	AND B.MACHINE_KIND = 	'" );
			stringBuffer.append( typeArray[1].toLowerCase() );
			stringBuffer.append( "' " );
		}
		stringBuffer.append( "	ORDER BY A.MACHINE_MARK	" );
		
		HashMap<String, String> typeMap = this.getMacType();
		String macTypeTemp = "";
		String osTemp = "";
		ResultSet rs = null;
		List<ClientBean> list = new ArrayList<ClientBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					ClientBean clientBean = new ClientBean();
					clientBean.setCpuFrequency( rs.getString( "CPU_FREQUENCY" ) );
					clientBean.setDiskMemory( rs.getString( "DISK_SIZE" ) );
					clientBean.setMachineName( rs.getString( "MACHINE_MARK" ) );
					osTemp = rs.getString( "OS" );
					clientBean.setOs( osTemp );
					clientBean.setRamMemory( rs.getString( "MEMORY_SIZE" ) );
					clientBean.setVersion( rs.getString( "VERSION" ) );
					if(-1 != osTemp.indexOf("Win")){
						osTemp = "Win";
					}
					macTypeTemp = osTemp + "_" + rs.getString( "MACHINE_KIND" );
					clientBean.setMacType(typeMap.get(macTypeTemp));
					list.add( clientBean );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return list;
	}
	public List<ClientBean> searchClientInfoMachine( String orgId, String selectedOs, String softwareVersion, String cpuFreq,
			String diskSize, String ramSize, String userId, String macType,String macName ) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "	SELECT  DISTINCT A.MACHINE_MARK,				" );
		stringBuffer.append( "	B.CPU_FREQUENCY,B.OS,B.MACHINE_KIND,				" );
		stringBuffer.append( "	B.DISK_SIZE,B.MEMORY_SIZE,			" );
		stringBuffer.append( "	B.VERSION,B.MACHINE_TYPE							" );
		stringBuffer.append( "	FROM TB_CCB_MACHINE A,				" );
		stringBuffer.append( "	TB_MACHINE_ENVIRONMENT B			" );
		stringBuffer.append( "	WHERE A.MACHINE_ID = B.MACHINE_ID	" );
		if ( !"allOrgIds".equals( orgId ) && !"-".equals( orgId.trim() ) ) {
			stringBuffer.append( "	AND A.ORG_ID IN				   (" );

			String allOrgIds = ReportCommonAction.getFirstOrgIdsByParentIdMachine( orgId );
			if ( null==allOrgIds ) {
				return new ArrayList<ClientBean>();
			}
			stringBuffer.append( allOrgIds );
			stringBuffer.append( ")									" );
		}
		if ( null != selectedOs && !"".equals( selectedOs.trim() ) && !"-".equals( selectedOs ) ) {
			stringBuffer.append( "	AND B.OS = 	'" );
			stringBuffer.append( selectedOs );
			stringBuffer.append( "'				 " );
		}
		if ( null != softwareVersion && !"".equals( softwareVersion.trim() ) ) {
			stringBuffer.append( "	AND B.VERSION = 	'" );
			stringBuffer.append( softwareVersion );
			stringBuffer.append( "'				 		 " );
		}
		if ( null != cpuFreq && !"".equals( cpuFreq.trim() ) ) {
			stringBuffer.append( "	AND B.CPU_FREQUENCY = 	'" );
			stringBuffer.append( cpuFreq );
			stringBuffer.append( "'				 			 " );
		}
		if ( null != diskSize && !"".equals( diskSize.trim() ) ) {
			stringBuffer.append( "	AND B.DISK_SIZE = 	'" );
			stringBuffer.append( diskSize );
			stringBuffer.append( "'				 	     " );
		}
		if ( null != ramSize && !"".equals( ramSize.trim() ) ) {
			stringBuffer.append( "	AND B.MEMORY_SIZE = 	'" );
			stringBuffer.append( ramSize );
			stringBuffer.append( "'				 	     " );
		}
		if ( null != macName && !"".equals( macName.trim() ) ) {
			stringBuffer.append( "	AND A.MACHINE_MARK like 	'%" );
			stringBuffer.append( macName.toUpperCase() );
			stringBuffer.append( "%'				 		 " );
		}
		if(!"0".equals(macType)){
			String[] typeArray = macType.split("_");
			stringBuffer.append( "	AND B.OS like 	'%" );
			stringBuffer.append( typeArray[0] );
			stringBuffer.append( "%' " );
			stringBuffer.append( "	AND B.MACHINE_KIND = 	'" );
			stringBuffer.append( typeArray[1].toLowerCase() );
			stringBuffer.append( "' " );
		}
		stringBuffer.append( "	ORDER BY A.MACHINE_MARK	" );
		
		HashMap<String, String> typeMap = this.getMacType();
		String macTypeTemp = "";
		String osTemp = "";
		ResultSet rs = null;
		List<ClientBean> list = new ArrayList<ClientBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					ClientBean clientBean = new ClientBean();
					clientBean.setCpuFrequency( rs.getString( "CPU_FREQUENCY" ) );
					clientBean.setDiskMemory( rs.getString( "DISK_SIZE" ) );
					clientBean.setMachineName( rs.getString( "MACHINE_MARK" ) );
					osTemp = rs.getString( "OS" );
					clientBean.setOs( osTemp );
					clientBean.setRamMemory( rs.getString( "MEMORY_SIZE" ) );
					clientBean.setVersion( rs.getString( "VERSION" ) );
					if(-1 != osTemp.indexOf("Win")){
						osTemp = "Win";
					}
					macTypeTemp = osTemp + "_" + rs.getString( "MACHINE_KIND" );
					clientBean.setMacType(typeMap.get(macTypeTemp));
					list.add( clientBean );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return list;
	}
	public HashMap<String, String> getMacType() {
		ResultSet rs = null;
		HashMap<String, String> map = new HashMap<String, String>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( "select os||'_'||machine_kind as mactype, type_name from tb_machine_type" );
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					map.put(rs.getString( "MACTYPE" ),rs.getString( "TYPE_NAME" ));
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return map;
	}
	
	public List<ClientBean> pageChannel( String pageType, PageBean pageBean, String orgId, String selectedOs, String softwareVersion, String cpuFreq,
			String diskSize, String ramSize, String macType,String macName ) {
		List<ClientBean> list = new ArrayList<ClientBean>();
		
		list = this.searchClientInfo( orgId, selectedOs, softwareVersion, cpuFreq, diskSize, ramSize, "",macType,macName );
		
		pageBean.setInfoCount( list.size() );

		int totalPageNum = ( list.size() + pageBean.getPageSize() - 1 ) / pageBean.getPageSize();

		pageBean.setPageCount( totalPageNum );

		if ( "first".equals( pageType ) ) {
			if ( list.size() >= 10 ) {
				list = list.subList( 0, 10 );
			}
			else {
				list = list.subList( 0, list.size() );
			}
		}
		else if ( "next".equals( pageType ) ) {
			if ( pageBean.getNextPageNo() == list.size() / pageBean.getPageSize() ) {
				list = list.subList( pageBean.getCurrentPage() * pageBean.getPageSize(), list.size() );
			}
			else if ( list.size() < pageBean.getNextPageNo() * pageBean.getPageSize() ) {
				if ( pageBean.getPageCount() == pageBean.getNextPageNo() ) {

					list = list.subList( ( pageBean.getPageCount() - 1 ) * pageBean.getPageSize(), list.size() );
				}
				else {
					list = list.subList( pageBean.getNextPageNo() * pageBean.getPageSize(), list.size() );
				}

			}
			else {
				list = list.subList( pageBean.getCurrentPage() * pageBean.getPageSize(), pageBean.getNextPageNo()
						* pageBean.getPageSize() );
			}
		}
		else if ( "previous".equals( pageType ) ) {
			if ( pageBean.getCurrentPage() <= 1 ) {
				if ( list.size() >= 10 ) {
					list = list.subList( 0, 10 );
				}
				else {
					list = list.subList( 0, list.size() );
				}

			}
			else {
				list = list.subList( ( pageBean.getPreviousPageNo() - 1 ) * pageBean.getPageSize(),
						pageBean.getPreviousPageNo() * pageBean.getPageSize() );
			}
		}
		else if ( "last".equals( pageType ) ) {
			if ( pageBean.getCurrentPage() * pageBean.getPageSize() >= pageBean.getInfoCount() ) {
				list = list.subList( (pageBean.getCurrentPage() - 1) * pageBean.getPageSize(), pageBean.getInfoCount() );
			}
			else {
				list = list.subList( list.size() / pageBean.getPageSize() * pageBean.getPageSize(), list.size() );
			}
		}
		else {
			if ( pageBean.getCurrentPage() * pageBean.getPageSize() >= pageBean.getInfoCount() ) {
				list = list.subList( pageBean.getPreviousPageNo() * pageBean.getPageSize(), pageBean.getInfoCount() );
			}
			else {
				list = list.subList( ( pageBean.getCurrentPage() - 1 ) * pageBean.getPageSize(),
						pageBean.getCurrentPage() * pageBean.getPageSize() );
			}

		}
		return list;
	}

	public List<ClientUseBean> getAllClientUseData( String userId ) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "	SELECT	DISTINCT				" );
		stringBuffer.append( "		TAB3.ORG_NAME,				" );
		stringBuffer.append( "		TAB2.CLICK_COUNT,			" );
		stringBuffer.append( "		TAB2.VALID_USE_TIME,		" );
		stringBuffer.append( "		TAB2.RUNNING_TIME			" );
		stringBuffer.append( "	FROM							" );
		stringBuffer.append( "		TB_CCB_MACHINE TAB1,		" );
		stringBuffer.append( "		TB_MACHINE_USE_HISTORY TAB2," );
		stringBuffer.append( "		TB_CCB_ORGANIZATION TAB3	" );
		stringBuffer.append( "	WHERE							" );
		stringBuffer.append( "		TAB1.MACHINE_ID = TAB2.MACHINE_ID	" );
		stringBuffer.append( "	AND TAB1.ORG_ID = TAB3.ORG_ID	" );

		// 前一天
		Calendar calendar = Calendar.getInstance();
		calendar.add( Calendar.DATE, -1 );
		Date date = calendar.getTime();
		DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
		stringBuffer.append( "	AND TAB2.DATE =   			   '" );
		stringBuffer.append( df.format( date ) );
		stringBuffer.append( "'" );
		
		String allOrgIds = this.getOrgInfo( userId );
		String orgId = this.getUseOrgInfo( userId );
		if ( "''".equals( allOrgIds ) ) {
			allOrgIds = "'" + orgId + "'";
		}
		else {
			allOrgIds = allOrgIds + ",'" + orgId + "'";
		}
		stringBuffer.append( "	AND TAB3.ORG_ID IN (" );
		stringBuffer.append( allOrgIds );
		stringBuffer.append( ")						" );
		
		stringBuffer.append( "	ORDER BY TAB2.VALID_USE_TIME asc 	" );
		ResultSet rs = null;
		List<ClientUseBean> list = new ArrayList<ClientUseBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					ClientUseBean clientUseBean = new ClientUseBean();
					clientUseBean.setOrgName( rs.getString( "ORG_NAME" ) );
					clientUseBean.setClickCount( rs.getString( "CLICK_COUNT" ) );
					String validTime = rs.getString( "VALID_USE_TIME" );
					clientUseBean.setValidTime( validTime );
					String runningTime = rs.getString( "RUNNING_TIME" );
					clientUseBean.setOpenTime( runningTime );
					NumberFormat formatter = new DecimalFormat( "0.00" );
					Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
					String useRate = formatter.format( rate );
					clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
					clientUseBean.setSortKey( "useRate" );
					list.add( clientUseBean );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		// 排序
		Collections.sort( list );
		return list;
	}

	public List<ClientUseBean> getDayReportData( String orgId, String day, String dataType, String sequenceType,
			String userId ) {
		StringBuffer stringBufferAllData = new StringBuffer();
		stringBufferAllData.append( "	SELECT	DISTINCT				" );
		stringBufferAllData.append( "		TAB3.ORG_NAME,				" );
		stringBufferAllData.append( "		TAB2.CLICK_COUNT,			" );
		stringBufferAllData.append( "		TAB2.VALID_USE_TIME,		" );
		stringBufferAllData.append( "		TAB2.RUNNING_TIME,			" );
		stringBufferAllData.append( "		(TAB2.RUNNING_TIME - TAB2.VALID_USE_TIME) as FREE_TIME			" );
		stringBufferAllData.append( "	FROM							" );
		stringBufferAllData.append( "		TB_CCB_MACHINE TAB1,		" );
		stringBufferAllData.append( "		TB_MACHINE_USE_HISTORY TAB2," );
		stringBufferAllData.append( "		TB_CCB_ORGANIZATION TAB3	" );
		stringBufferAllData.append( "	WHERE							" );
		stringBufferAllData.append( "		TAB1.MACHINE_ID = TAB2.MACHINE_ID	" );
		stringBufferAllData.append( "	AND TAB1.ORG_ID = TAB3.ORG_ID	" );
		stringBufferAllData.append( "	AND TAB2.DATE = 			   '" );
		stringBufferAllData.append( day );
		stringBufferAllData.append( "'" );

		stringBufferAllData.append( "	AND (TAB3.ORG_ID IN (" );
//		String allOrgIds = this.getOrgInfo( orgId );
		String allOrgIds = ReportCommonAction.getFirstOrgIdsByParentId( orgId );
		if ( null == allOrgIds) {
			stringBufferAllData.append( "'" );
			stringBufferAllData.append( orgId );
			stringBufferAllData.append( "'))									" );
		}
		else {
			stringBufferAllData.append( allOrgIds  );
			stringBufferAllData.append( "))									" );
		}

		StringBuffer stringBufferDetailData = new StringBuffer();

		stringBufferDetailData.append( "	SELECT DISTINCT	" );
		stringBufferDetailData.append( "		T2.ORG_NAME," );
		stringBufferDetailData.append( "		T1.MACHINE_ID," );
		stringBufferDetailData.append( "		T1.MACHINE_MARK," );
		stringBufferDetailData.append( "		'网银自动设备' AS MACHINE_TYPE," );
		stringBufferDetailData.append( "		T3.RUNNING_TIME," );
		stringBufferDetailData.append( "		T3.VALID_USE_TIME," );
		stringBufferDetailData.append( "		(T3.RUNNING_TIME - T3.VALID_USE_TIME) AS FREE_TIME" );
		stringBufferDetailData.append( "	FROM" );
		stringBufferDetailData.append( "		TB_CCB_MACHINE T1," );
		stringBufferDetailData.append( "		TB_CCB_ORGANIZATION T2," );
		stringBufferDetailData.append( "		TB_MACHINE_USE_HISTORY T3" );
		stringBufferDetailData.append( "	WHERE" );
		stringBufferDetailData.append( "		T1.ORG_ID = T2.ORG_ID" );
		stringBufferDetailData.append( "		AND T1.MACHINE_ID = T3.MACHINE_ID" );
		stringBufferDetailData.append( "	AND (T2.ORG_ID IN (" );
		if ( null == allOrgIds ) {
			stringBufferDetailData.append( "'" );
			stringBufferDetailData.append( orgId );
			stringBufferDetailData.append( "'))									" );
		}
		else {
			stringBufferDetailData.append( allOrgIds );
			stringBufferDetailData.append( "))									" );
		}
		stringBufferDetailData.append( "		AND T3.DATE = 					'" );
		stringBufferDetailData.append( day );
		stringBufferDetailData.append( "'" );

		if ( "1".equals( sequenceType ) ) {
			stringBufferDetailData.append( "	ORDER BY FREE_TIME ASC 	" );
		}
		else {
			stringBufferDetailData.append( "	ORDER BY FREE_TIME DESC 	" );
		}

		StringBuffer stringBuffer = null;
		if ( "allData".equals( dataType ) ) {
			stringBuffer = stringBufferAllData;
		}
		if ( "detailData".equals( dataType ) ) {
			stringBuffer = stringBufferDetailData;
		}

		ResultSet rs = null;
		List<ClientUseBean> list = new ArrayList<ClientUseBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				if ( "allData".equals( dataType ) ) {
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setOrgName( rs.getString( "ORG_NAME" ) );
						clientUseBean.setClickCount( rs.getString( "CLICK_COUNT" ) );
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );

						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}

						list.add( clientUseBean );
					}
				}
				if ( "detailData".equals( dataType ) ) {
					HashMap<String, String> MTmap = new MonitorDAO().getMacTypeByMacID();
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setBranches( rs.getString( "ORG_NAME" ) );
						clientUseBean.setMachineId( rs.getString( "MACHINE_MARK" ) );
						if(MTmap.containsKey(rs.getString( "MACHINE_ID" ))){
							clientUseBean.setMachineType( MTmap.get(rs.getString( "MACHINE_ID" )) );
						} else {
							clientUseBean.setMachineType("未知类型");
						}
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );
						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						list.add( clientUseBean );
					}
				}

			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		// 排序
		Collections.sort( list );
		return list;
	}

	public List<ClientUseBean> getWeekReportData( String orgId, String week, String dataType, String sequenceType,
			String userId ) {
		StringBuffer stringBufferAllData = new StringBuffer();
		stringBufferAllData.append( "	SELECT	DISTINCT				" );
		stringBufferAllData.append( "		TAB3.ORG_NAME,				" );
		stringBufferAllData.append( "		TAB2.CLICK_COUNT,			" );
		stringBufferAllData.append( "		TAB2.VALID_USE_TIME,		" );
		stringBufferAllData.append( "		TAB2.RUNNING_TIME,			" );
		stringBufferAllData.append( "		(TAB2.RUNNING_TIME - TAB2.VALID_USE_TIME) as FREE_TIME			" );
		stringBufferAllData.append( "	FROM							" );
		stringBufferAllData.append( "		TB_CCB_MACHINE TAB1,		" );
		stringBufferAllData.append( "		TB_MACHINE_USE_HISTORY TAB2," );
		stringBufferAllData.append( "		TB_CCB_ORGANIZATION TAB3	" );
		stringBufferAllData.append( "	WHERE							" );
		stringBufferAllData.append( "		TAB1.MACHINE_ID = TAB2.MACHINE_ID	" );
		stringBufferAllData.append( "	AND TAB1.ORG_ID = TAB3.ORG_ID	" );
		stringBufferAllData.append( "	AND TAB2.DATE BETWEEN   		'" );

		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		Date date = null;
		try {
			date = sdf.parse( week );
		}
		catch ( ParseException e ) {
			e.printStackTrace();
		}
		c.setTime( date );
		c.set( Calendar.DAY_OF_WEEK, Calendar.SUNDAY );
		Date first = c.getTime();
		c.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
		Date last = c.getTime();

		stringBufferAllData.append( sdf.format( first ) );
		stringBufferAllData.append( "' AND '" );
		stringBufferAllData.append( sdf.format( last ) );
		stringBufferAllData.append( "'" );
		stringBufferAllData.append( "	AND (TAB1.ORG_ID IN (" );
//		String allOrgIds = this.getOrgInfo( orgId );
		String allOrgIds = ReportCommonAction.getFirstOrgIdsByParentId( orgId );
		if ( null == allOrgIds ) {
			stringBufferAllData.append( "'" );
			stringBufferAllData.append( orgId );
			stringBufferAllData.append( "'))									" );
		}
		else {
			stringBufferAllData.append( allOrgIds );
			stringBufferAllData.append( "))									" );
		}

		StringBuffer stringBufferDetailData = new StringBuffer();
		stringBufferDetailData.append( "	SELECT	DISTINCT " );
		stringBufferDetailData.append( "		T2.ORG_NAME," );
		stringBufferDetailData.append( "		T1.MACHINE_ID," );
		stringBufferDetailData.append( "		T1.MACHINE_MARK," );
		stringBufferDetailData.append( "		'网银自动设备' AS MACHINE_TYPE," );
		stringBufferDetailData.append( "		T3.RUNNING_TIME," );
		stringBufferDetailData.append( "		T3.VALID_USE_TIME," );
		stringBufferDetailData.append( "		(T3.RUNNING_TIME - T3.VALID_USE_TIME) AS FREE_TIME" );
		stringBufferDetailData.append( "	FROM" );
		stringBufferDetailData.append( "		TB_CCB_MACHINE T1," );
		stringBufferDetailData.append( "		TB_CCB_ORGANIZATION T2," );
		stringBufferDetailData.append( "		TB_MACHINE_USE_HISTORY T3" );
		stringBufferDetailData.append( "	WHERE" );
		stringBufferDetailData.append( "		T1.ORG_ID = T2.ORG_ID" );
		stringBufferDetailData.append( "		AND T1.MACHINE_ID = T3.MACHINE_ID" );
		stringBufferDetailData.append( "	AND (T2.ORG_ID IN (" );
		if ( null == allOrgIds ) {
			stringBufferDetailData.append( "'" );
			stringBufferDetailData.append( orgId );
			stringBufferDetailData.append( "'))									" );
		}
		else {
			stringBufferDetailData.append( allOrgIds );
			stringBufferDetailData.append( "))									" );
		}

		stringBufferDetailData.append( " AND T3.DATE BETWEEN								   '" );
		stringBufferDetailData.append( sdf.format( first ) );
		stringBufferDetailData.append( "' AND '" );
		stringBufferDetailData.append( sdf.format( last ) );
		stringBufferDetailData.append( "'" );
		StringBuffer stringBuffer = null;
		if ( "allData".equals( dataType ) ) {
			stringBuffer = stringBufferAllData;
		}
		if ( "detailData".equals( dataType ) ) {
			stringBuffer = stringBufferDetailData;
		}
		ResultSet rs = null;
		List<ClientUseBean> list = new ArrayList<ClientUseBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
// stmt.setLong( 1, Long.parseLong( userId ) );
			rs = stmt.executeQuery();
			if ( rs != null ) {
				if ( "allData".equals( dataType ) ) {
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setOrgName( rs.getString( "ORG_NAME" ) );
						clientUseBean.setClickCount( rs.getString( "CLICK_COUNT" ) );
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );

						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}

						list.add( clientUseBean );
					}
				}
				if ( "detailData".equals( dataType ) ) {
					HashMap<String, String> MTmap = new MonitorDAO().getMacTypeByMacID();
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setBranches( rs.getString( "ORG_NAME" ) );
						clientUseBean.setMachineId( rs.getString( "MACHINE_MARK" ) );
						if(MTmap.containsKey(rs.getString( "MACHINE_ID" ))){
							clientUseBean.setMachineType( MTmap.get(rs.getString( "MACHINE_ID" )) );
						} else {
							clientUseBean.setMachineType("未知类型");
						}
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );

						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}

						list.add( clientUseBean );
					}
				}

			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		// 排序
		Collections.sort( list );
		return list;
	}

	public List<ClientUseBean> getMonthReportData( String orgId, String month, String dataType, String sequenceType,
			String userId ) {
		StringBuffer stringBufferAllData = new StringBuffer();
		stringBufferAllData.append( "	SELECT	DISTINCT				" );
		stringBufferAllData.append( "		TAB3.ORG_NAME,				" );
		stringBufferAllData.append( "		TAB2.CLICK_COUNT,			" );
		stringBufferAllData.append( "		TAB2.VALID_USE_TIME,		" );
		stringBufferAllData.append( "		TAB2.RUNNING_TIME,			" );
		stringBufferAllData.append( "		(TAB2.RUNNING_TIME - TAB2.VALID_USE_TIME) as FREE_TIME			" );
		stringBufferAllData.append( "	FROM							" );
		stringBufferAllData.append( "		TB_CCB_MACHINE TAB1,		" );
		stringBufferAllData.append( "		TB_MACHINE_USE_HISTORY TAB2," );
		stringBufferAllData.append( "		TB_CCB_ORGANIZATION TAB3	" );
		stringBufferAllData.append( "	WHERE							" );
		stringBufferAllData.append( "		TAB1.MACHINE_ID = TAB2.MACHINE_ID	" );
		stringBufferAllData.append( "	AND TAB1.ORG_ID = TAB3.ORG_ID	" );
		stringBufferAllData.append( "	AND TAB2.DATE BETWEEN	   '" );

		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		Date date = null;
		try {
			date = sdf.parse( month );
		}
		catch ( ParseException e ) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime( date );
		calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMinimum( Calendar.DAY_OF_MONTH ) );
		String monthStart = sdf.format( calendar.getTime() );
		calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );
		String monthEnd = sdf.format( calendar.getTime() );

		stringBufferAllData.append( monthStart );
		stringBufferAllData.append( "' AND '" );
		stringBufferAllData.append( monthEnd );
		stringBufferAllData.append( "'" );
		stringBufferAllData.append( "	AND (TAB3.ORG_ID IN (" );
//		String allOrgIds = this.getOrgInfo( orgId );
		String allOrgIds = ReportCommonAction.getFirstOrgIdsByParentId( orgId );
		if ( null == allOrgIds ) {
			stringBufferAllData.append( "'" );
			stringBufferAllData.append( orgId );
			stringBufferAllData.append( "'))									" );
		}
		else {
			stringBufferAllData.append( allOrgIds );
			stringBufferAllData.append( "))									" );
		}

		StringBuffer stringBufferDetailData = new StringBuffer();
		stringBufferDetailData.append( "	SELECT	DISTINCT " );
		stringBufferDetailData.append( "		T2.ORG_NAME," );
		stringBufferDetailData.append( "		T1.MACHINE_ID," );
		stringBufferDetailData.append( "		T1.MACHINE_MARK," );
		stringBufferDetailData.append( "		'网银自动设备' AS MACHINE_TYPE," );
		stringBufferDetailData.append( "		T3.RUNNING_TIME," );
		stringBufferDetailData.append( "		T3.VALID_USE_TIME," );
		stringBufferDetailData.append( "		(T3.RUNNING_TIME - T3.VALID_USE_TIME) AS FREE_TIME" );
		stringBufferDetailData.append( "	FROM" );
		stringBufferDetailData.append( "		TB_CCB_MACHINE T1," );
		stringBufferDetailData.append( "		TB_CCB_ORGANIZATION T2," );
		stringBufferDetailData.append( "		TB_MACHINE_USE_HISTORY T3" );
		stringBufferDetailData.append( "	WHERE" );
		stringBufferDetailData.append( "		T1.ORG_ID = T2.ORG_ID" );
		stringBufferDetailData.append( "		AND T1.MACHINE_ID = T3.MACHINE_ID" );
		stringBufferDetailData.append( "	AND (T2.ORG_ID IN (" );
		if ( null == allOrgIds ) {
			stringBufferDetailData.append( "'" );
			stringBufferDetailData.append( orgId );
			stringBufferDetailData.append( "'))									" );
		}
		else {
			stringBufferDetailData.append( allOrgIds );
			stringBufferDetailData.append( "))									" );
		}

		stringBufferDetailData.append( "		AND T3.DATE BETWEEN								   '" );
		stringBufferDetailData.append( monthStart );
		stringBufferDetailData.append( "' AND '" );
		stringBufferDetailData.append( monthEnd );
		stringBufferDetailData.append( "'" );
		StringBuffer stringBuffer = null;
		if ( "allData".equals( dataType ) ) {
			stringBuffer = stringBufferAllData;
		}
		if ( "detailData".equals( dataType ) ) {
			stringBuffer = stringBufferDetailData;
		}
		ResultSet rs = null;
		List<ClientUseBean> list = new ArrayList<ClientUseBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
// stmt.setLong( 1, Long.parseLong( userId ) );
			rs = stmt.executeQuery();
			if ( rs != null ) {
				if ( "allData".equals( dataType ) ) {
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setOrgName( rs.getString( "ORG_NAME" ) );
						clientUseBean.setClickCount( rs.getString( "CLICK_COUNT" ) );
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );

						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}

						list.add( clientUseBean );
					}
				}
				if ( "detailData".equals( dataType ) ) {
					HashMap<String, String> MTmap = new MonitorDAO().getMacTypeByMacID();
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setBranches( rs.getString( "ORG_NAME" ) );
						clientUseBean.setMachineId( rs.getString( "MACHINE_MARK" ) );
						if(MTmap.containsKey(rs.getString( "MACHINE_ID" ))){
							clientUseBean.setMachineType( MTmap.get(rs.getString( "MACHINE_ID" )) );
						} else {
							clientUseBean.setMachineType("未知类型");
						}
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );

						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}

						list.add( clientUseBean );
					}
				}

			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		// 排序
		Collections.sort( list );
		return list;
	}

	public List<ClientUseBean> getAnyTimeReportData( String orgId, String startTime, String endTime, String dataType,
			String sequenceType, String userId ) {
		startTime = startTime.replaceAll( " ", "" );
		endTime = endTime.replaceAll( " ", "" );
		StringBuffer stringBufferAllData = new StringBuffer();
		stringBufferAllData.append( "	SELECT	DISTINCT				" );
		stringBufferAllData.append( "		TAB3.ORG_NAME,				" );
		stringBufferAllData.append( "		TAB2.CLICK_COUNT,			" );
		stringBufferAllData.append( "		TAB2.VALID_USE_TIME,		" );
		stringBufferAllData.append( "		TAB2.RUNNING_TIME,			" );
		stringBufferAllData.append( "		(TAB2.RUNNING_TIME - TAB2.VALID_USE_TIME) as FREE_TIME			" );
		stringBufferAllData.append( "	FROM							" );
		stringBufferAllData.append( "		TB_CCB_MACHINE TAB1,		" );
		stringBufferAllData.append( "		TB_MACHINE_USE_HISTORY TAB2," );
		stringBufferAllData.append( "		TB_CCB_ORGANIZATION TAB3	" );
		stringBufferAllData.append( "	WHERE							" );
		stringBufferAllData.append( "		TAB1.MACHINE_ID = TAB2.MACHINE_ID	" );
		stringBufferAllData.append( "	AND TAB1.ORG_ID = TAB3.ORG_ID	" );
		stringBufferAllData.append( "	AND TAB2.DATE BETWEEN		   '" );
		stringBufferAllData.append( startTime );
		stringBufferAllData.append( "' AND '" );
		stringBufferAllData.append( endTime );

		stringBufferAllData.append( "'	AND (TAB3.ORG_ID IN (" );
//		String allOrgIds = this.getOrgInfo( orgId );
		String allOrgIds = ReportCommonAction.getFirstOrgIdsByParentId( orgId );
		if ( null == allOrgIds ) {
			stringBufferAllData.append( "'" );
			stringBufferAllData.append( orgId );
			stringBufferAllData.append( "'))									" );
		}
		else {
			stringBufferAllData.append( allOrgIds );
			stringBufferAllData.append( "))									" );
		}

		StringBuffer stringBufferDetailData = new StringBuffer();
		stringBufferDetailData.append( "	SELECT	DISTINCT													" );
		stringBufferDetailData.append( "		T2.ORG_NAME," );
		stringBufferDetailData.append( "		T1.MACHINE_ID," );
		stringBufferDetailData.append( "		T1.MACHINE_MARK," );
		stringBufferDetailData.append( "		'网银自动设备' AS MACHINE_TYPE," );
		stringBufferDetailData.append( "		T3.RUNNING_TIME," );
		stringBufferDetailData.append( "		T3.VALID_USE_TIME," );
		stringBufferDetailData.append( "		(T3.RUNNING_TIME - T3.VALID_USE_TIME) AS FREE_TIME" );
		stringBufferDetailData.append( "	FROM" );
		stringBufferDetailData.append( "		TB_CCB_MACHINE T1," );
		stringBufferDetailData.append( "		TB_CCB_ORGANIZATION T2," );
		stringBufferDetailData.append( "		TB_MACHINE_USE_HISTORY T3" );
		stringBufferDetailData.append( "	WHERE" );
		stringBufferDetailData.append( "		T1.ORG_ID = T2.ORG_ID" );
		stringBufferDetailData.append( "		AND T1.MACHINE_ID = T3.MACHINE_ID" );
		stringBufferDetailData.append( "	AND (T2.ORG_ID IN (" );
		if ( null == allOrgIds ) {
			stringBufferDetailData.append( "'" );
			stringBufferDetailData.append( orgId );
			stringBufferDetailData.append( "'))									" );
		}
		else {
			stringBufferDetailData.append( allOrgIds );
			stringBufferDetailData.append( "))									" );
		}

		stringBufferDetailData.append( "		AND T3.DATE BETWEEN								   '" );
		stringBufferDetailData.append( startTime );
		stringBufferDetailData.append( "' AND '" );
		stringBufferDetailData.append( endTime );
		stringBufferDetailData.append( "'" );
		StringBuffer stringBuffer = null;
		if ( "allData".equals( dataType ) ) {
			stringBuffer = stringBufferAllData;
		}
		if ( "detailData".equals( dataType ) ) {
			stringBuffer = stringBufferDetailData;
		}
		ResultSet rs = null;
		List<ClientUseBean> list = new ArrayList<ClientUseBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
// stmt.setLong( 1, Long.parseLong( userId ) );
			rs = stmt.executeQuery();
			if ( rs != null ) {
				if ( "allData".equals( dataType ) ) {
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setOrgName( rs.getString( "ORG_NAME" ) );
						clientUseBean.setClickCount( rs.getString( "CLICK_COUNT" ) );
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );

						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}

						list.add( clientUseBean );
					}
				}
				if ( "detailData".equals( dataType ) ) {
					HashMap<String, String> MTmap = new MonitorDAO().getMacTypeByMacID();
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setBranches( rs.getString( "ORG_NAME" ) );
						clientUseBean.setMachineId( rs.getString( "MACHINE_MARK" ) );
						if(MTmap.containsKey(rs.getString( "MACHINE_ID" ))){
							clientUseBean.setMachineType( MTmap.get(rs.getString( "MACHINE_ID" )) );
						} else {
							clientUseBean.setMachineType("未知类型");
						}
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );

						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}

						list.add( clientUseBean );
					}
				}

			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		// 排序
		Collections.sort( list );
		return list;
	}

	public List<ClientBean> getReportData( HttpServletRequest request ) {

		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append( "	SELECT DISTINCT A.MACHINE_MARK,		" );
		stringBuffer.append( "	B.CPU_FREQUENCY,B.OS,				" );
		stringBuffer.append( "	B.DISK_SIZE,B.MEMORY_SIZE,			" );
		stringBuffer.append( "	B.VERSION							" );
		stringBuffer.append( "	FROM TB_CCB_MACHINE A,				" );
		stringBuffer.append( "	TB_MACHINE_ENVIRONMENT B			" );
		stringBuffer.append( "	WHERE A.MACHINE_ID = B.MACHINE_ID	" );
		User user = (User) request.getSession().getAttribute( "user_info" );
		String macType = request.getParameter( "macType" ).trim();
		if(!"0".equals(macType)){
			if("1".equals(macType)){
				stringBuffer.append( " 	and B.machine_type='"+macType+"' " );
			} else {
				stringBuffer.append( " 	and B.machine_type<>'1' " );
			}
		}
		String userId = user.getId();
		
		String allOrgIds = this.getOrgInfo( userId );
		String orgId = this.getUseOrgInfo( userId );
		if ( "''".equals( allOrgIds ) ) {
			allOrgIds = "'" + orgId + "'";
		}
		else {
			allOrgIds = allOrgIds + ",'" + orgId + "'";
		}
		stringBuffer.append( "	AND A.ORG_ID IN (" );
		stringBuffer.append( allOrgIds );
		stringBuffer.append( ")" );
		
		String os = (String) request.getParameter( "selectOsId" );
		if ( os != null && !"".equals( os ) && !"-".equals( os ) ) {
			stringBuffer.append( " AND B.OS like '%" );
			stringBuffer.append( os );
			stringBuffer.append( "%'" );
		}
		String softwareVersion = (String) request.getParameter( "softwareVersion" );
		if ( softwareVersion != null && !"".equals( softwareVersion ) ) {
			stringBuffer.append( " AND B.VERSION like '%" );
			stringBuffer.append( softwareVersion );
			stringBuffer.append( "%'" );
		}
		String cpuFreq = (String) request.getParameter( "cpuFreq" );
		if ( null != cpuFreq && !"".equals( cpuFreq ) ) {
			stringBuffer.append( " AND B.CPU_FREQUENCY like '%" );
			stringBuffer.append( cpuFreq );
			stringBuffer.append( "%'" );
		}
		String diskSize = (String) request.getParameter( "diskSize" );
		if ( null != diskSize && !"".equals( diskSize ) ) {
			stringBuffer.append( " AND B.DISK_SIZE like '%" );
			stringBuffer.append( diskSize );
			stringBuffer.append( "%'" );
		}
		String ramSize = (String) request.getParameter( "diskSize" );
		if ( null != ramSize && !"".equals( ramSize ) ) {
			stringBuffer.append( " AND B.MEMORY_SIZE like '%" );
			stringBuffer.append( ramSize );
			stringBuffer.append( "%'" );
		}
		stringBuffer.append( "	ORDER BY A.MACHINE_MARK	" );
		
		ResultSet rs = null;
		List<ClientBean> list = new ArrayList<ClientBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			rs = stmt.executeQuery();
			if ( rs != null ) {
				while ( rs.next() ) {
					ClientBean clientBean = new ClientBean();
					clientBean.setCpuFrequency( rs.getString( "CPU_FREQUENCY" ) + " MHz" );
					clientBean.setDiskMemory( rs.getString( "DISK_SIZE" ) + " GB" );
					clientBean.setMachineName( rs.getString( "MACHINE_MARK" ) );
					clientBean.setOs( rs.getString( "OS" ) );
					clientBean.setRamMemory( rs.getString( "MEMORY_SIZE" ) + " KB" );
					clientBean.setVersion( rs.getString( "VERSION" ) );
					list.add( clientBean );
				}
			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public List<ClientUseBean> getClientUseReportData( HttpServletRequest request ) {

		StringBuffer stringBufferAllData = new StringBuffer();
		stringBufferAllData.append( "	SELECT	DISTINCT				" );
		stringBufferAllData.append( "		TAB3.ORG_NAME,				" );
		stringBufferAllData.append( "		TAB2.CLICK_COUNT,			" );
		stringBufferAllData.append( "		TAB2.VALID_USE_TIME,		" );
		stringBufferAllData.append( "		TAB2.RUNNING_TIME,			" );
		stringBufferAllData.append( "		(TAB2.RUNNING_TIME - TAB2.VALID_USE_TIME) as FREE_TIME			" );
		stringBufferAllData.append( "	FROM							" );
		stringBufferAllData.append( "		TB_CCB_MACHINE TAB1,		" );
		stringBufferAllData.append( "		TB_MACHINE_USE_HISTORY TAB2," );
		stringBufferAllData.append( "		TB_CCB_ORGANIZATION TAB3	" );
		stringBufferAllData.append( "	WHERE							" );
		stringBufferAllData.append( "		TAB1.MACHINE_ID = TAB2.MACHINE_ID	" );
		stringBufferAllData.append( "	AND TAB1.ORG_ID = TAB3.ORG_ID	" );

		String searchTimeType = request.getParameter( "searchTimeType" );
		if ( "day".equals( searchTimeType ) ) {
			String day = request.getParameter( "selectDateDay" );
			stringBufferAllData.append( "	AND TAB2.DATE = 			   '" );
			stringBufferAllData.append( day );
			stringBufferAllData.append( "'" );
			request.setAttribute( "day", day );
		}
		else if ( "week".equals( searchTimeType ) ) {
			String week = request.getParameter( "selectDateWeek" );

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			Date date = null;
			try {
				date = sdf.parse( week );
			}
			catch ( ParseException e ) {
				e.printStackTrace();
			}
			c.setTime( date );
			c.set( Calendar.DAY_OF_WEEK, Calendar.SUNDAY );
			Date first = c.getTime();
			c.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
			Date last = c.getTime();

			stringBufferAllData.append( "	AND TAB2.DATE BETWEEN '" );
			stringBufferAllData.append( sdf.format( first ) );
			stringBufferAllData.append( "' AND '" );
			stringBufferAllData.append( sdf.format( last ) );
			stringBufferAllData.append( "'" );
			request.setAttribute( "week", week );
		}
		else if ( "month".equals( searchTimeType ) ) {
			String month = request.getParameter( "selectDateMonth" );

			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			Date date = null;
			try {
				date = sdf.parse( month );
			}
			catch ( ParseException e ) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( date );
			calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMinimum( Calendar.DAY_OF_MONTH ) );
			String monthStart = sdf.format( calendar.getTime() );
			calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );
			String monthEnd = sdf.format( calendar.getTime() );

			stringBufferAllData.append( "	AND TAB2.DATE BETWEEN '" );
			stringBufferAllData.append( monthStart );
			stringBufferAllData.append( "' AND '" );
			stringBufferAllData.append( monthEnd );
			stringBufferAllData.append( "'" );
			request.setAttribute( "month", month );
		}
		else if ( "anyTime".equals( searchTimeType ) ) {
			String startTime = request.getParameter( "selectDateAnyStart" );
			String endTime = request.getParameter( "selectDateAnyEnd" );
			startTime = startTime.replaceAll( " ", "" );
			endTime = endTime.replaceAll( " ", "" );
			stringBufferAllData.append( "	AND TAB2.DATE BETWEEN		   '" );
			stringBufferAllData.append( startTime );
			stringBufferAllData.append( "' AND '" );
			stringBufferAllData.append( endTime );
			stringBufferAllData.append( "'" );
			request.setAttribute( "startTime", startTime );
			request.setAttribute( "endTime", endTime );
		}

		String orgId = (String) request.getParameter( "selectOrgName" );
//		String allOrgIds = this.getOrgInfo( orgId );
		String allOrgIds = ReportCommonAction.getFirstOrgIdsByParentId( orgId );
		if ( "''".equals( allOrgIds ) ) {
			stringBufferAllData.append( " AND TAB1.ORG_ID = '" );
			stringBufferAllData.append( orgId );
			stringBufferAllData.append( "'					 " );
		}
		else {
			stringBufferAllData.append( " AND TAB1.ORG_ID IN (" );
			stringBufferAllData.append( allOrgIds + ",'" + orgId );
			stringBufferAllData.append( "')							" );
		}

		StringBuffer stringBufferDetailData = new StringBuffer();

		stringBufferDetailData.append( "	SELECT DISTINCT	" );
		stringBufferDetailData.append( "		T2.ORG_NAME," );
		stringBufferDetailData.append( "		T1.MACHINE_ID," );
		stringBufferDetailData.append( "		T1.MACHINE_MARK," );
		stringBufferDetailData.append( "		'网银自动设备' AS MACHINE_TYPE," );
		stringBufferDetailData.append( "		T3.RUNNING_TIME," );
		stringBufferDetailData.append( "		T3.VALID_USE_TIME," );
		stringBufferDetailData.append( "		(T3.RUNNING_TIME - T3.VALID_USE_TIME) AS FREE_TIME" );
		stringBufferDetailData.append( "	FROM" );
		stringBufferDetailData.append( "		TB_CCB_MACHINE T1," );
		stringBufferDetailData.append( "		TB_CCB_ORGANIZATION T2," );
		stringBufferDetailData.append( "		TB_MACHINE_USE_HISTORY T3" );
		stringBufferDetailData.append( "	WHERE" );
		stringBufferDetailData.append( "		T1.ORG_ID = T2.ORG_ID" );
		stringBufferDetailData.append( "		AND T1.MACHINE_ID = T3.MACHINE_ID" );
		stringBufferDetailData.append( "	AND (T2.ORG_ID IN (" );
		if ( "''".equals( allOrgIds ) ) {
			stringBufferDetailData.append( "'" );
			stringBufferDetailData.append( orgId );
			stringBufferDetailData.append( "')									" );
		}
		else {
			stringBufferDetailData.append( allOrgIds + ",'" + orgId );
			stringBufferDetailData.append( "'))									" );
		}
		if ( "day".equals( searchTimeType ) ) {
			String day = request.getParameter( "selectDateDay" );
			stringBufferDetailData.append( "	AND T3.DATE = 			   '" );
			stringBufferDetailData.append( day );
			stringBufferDetailData.append( "'" );
			request.setAttribute( "day", day );
		}
		else if ( "week".equals( searchTimeType ) ) {
			String week = request.getParameter( "selectDateWeek" );

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			Date date = null;
			try {
				date = sdf.parse( week );
			}
			catch ( ParseException e ) {
				e.printStackTrace();
			}
			c.setTime( date );
			c.set( Calendar.DAY_OF_WEEK, Calendar.SUNDAY );
			Date first = c.getTime();
			c.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
			Date last = c.getTime();

			stringBufferDetailData.append( "	AND T3.DATE BETWEEN '" );
			stringBufferDetailData.append( sdf.format( first ) );
			stringBufferDetailData.append( "' AND '" );
			stringBufferDetailData.append( sdf.format( last ) );
			stringBufferDetailData.append( "'" );
			request.setAttribute( "week", week );
		}
		else if ( "month".equals( searchTimeType ) ) {
			String month = request.getParameter( "selectDateMonth" );

			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			Date date = null;
			try {
				date = sdf.parse( month );
			}
			catch ( ParseException e ) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( date );
			calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMinimum( Calendar.DAY_OF_MONTH ) );
			String monthStart = sdf.format( calendar.getTime() );
			calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );
			String monthEnd = sdf.format( calendar.getTime() );

			stringBufferDetailData.append( "	AND T3.DATE  BETWEEN '" );
			stringBufferDetailData.append( monthStart );
			stringBufferDetailData.append( "' AND '" );
			stringBufferDetailData.append( monthEnd );
			stringBufferDetailData.append( "'" );
			request.setAttribute( "month", month );
		}
		else if ( "anyTime".equals( searchTimeType ) ) {
			String startTime = request.getParameter( "selectDateAnyStart" );
			String endTime = request.getParameter( "selectDateAnyEnd" );
			startTime = startTime.replaceAll( " ", "" );
			endTime = endTime.replaceAll( " ", "" );
			stringBufferDetailData.append( "	AND T3.DATE BETWEEN		   '" );
			stringBufferDetailData.append( startTime );
			stringBufferDetailData.append( "' AND '" );
			stringBufferDetailData.append( endTime );
			stringBufferDetailData.append( "'" );
			request.setAttribute( "startTime", startTime );
			request.setAttribute( "endTime", endTime );
		}

		String sequenceType = (String) request.getParameter( "sequenceType" );
		if ( "1".equals( sequenceType ) ) {
			stringBufferDetailData.append( "	ORDER BY FREE_TIME ASC 	" );
		}
		else {
			stringBufferDetailData.append( "	ORDER BY FREE_TIME DESC 	" );
		}

		String dataType = (String) request.getParameter( "dataType" );
		StringBuffer stringBuffer = null;
		if ( "allData".equals( dataType ) ) {
			stringBuffer = stringBufferAllData;
		}
		if ( "detailData".equals( dataType ) ) {
			stringBuffer = stringBufferDetailData;
		}

		ResultSet rs = null;
		List<ClientUseBean> list = new ArrayList<ClientUseBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				if ( "allData".equals( dataType ) ) {
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setOrgName( rs.getString( "ORG_NAME" ) );
						clientUseBean.setClickCount( rs.getString( "CLICK_COUNT" ) );
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );

						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setRate( String.valueOf( Double.parseDouble( useRate ) * 100 ) + "%" );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );

							String rateReport = String.valueOf( Double.parseDouble( useRate ) * 100 );

							if ( rateReport.substring( rateReport.indexOf( "." ), rateReport.length() ).length() > 2 ) {
								rateReport = rateReport.substring( 0, rateReport.indexOf( "." ) + 2 );
							}
							clientUseBean.setRate( rateReport );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}

						list.add( clientUseBean );
					}
				}
				if ( "detailData".equals( dataType ) ) {
					HashMap<String, String> MTmap = new MonitorDAO().getMacTypeByMacID();
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setBranches( rs.getString( "ORG_NAME" ) );
						clientUseBean.setMachineId( rs.getString( "MACHINE_MARK" ) );
						if(MTmap.containsKey(rs.getString( "MACHINE_ID" ))){
							clientUseBean.setMachineType( MTmap.get(rs.getString( "MACHINE_ID" )) );
						} else {
							clientUseBean.setMachineType("未知类型");
						}
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );
						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						list.add( clientUseBean );
					}
				}

			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		// 排序
		Collections.sort( list );
		List<ClientUseBean> data = new ArrayList<ClientUseBean>();
		for ( int i = 0; i < list.size(); i++ ) {
			ClientUseBean channelBean = list.get( i );
			channelBean.setIndex( i + 1 );
			data.add( channelBean );
		}
		return data;
	}

	public List<ClientUseBean> getClientUseData( String searchTimeType, String dateTime, String startTime, String endTime, String orgId, String sequenceType, String dataType ) {

		StringBuffer stringBufferAllData = new StringBuffer();
		stringBufferAllData.append( "	SELECT	DISTINCT				" );
		stringBufferAllData.append( "		TAB3.ORG_NAME,				" );
		stringBufferAllData.append( "		TAB2.CLICK_COUNT,			" );
		stringBufferAllData.append( "		TAB2.VALID_USE_TIME,		" );
		stringBufferAllData.append( "		TAB2.RUNNING_TIME,			" );
		stringBufferAllData.append( "		(TAB2.RUNNING_TIME - TAB2.VALID_USE_TIME) as FREE_TIME			" );
		stringBufferAllData.append( "	FROM							" );
		stringBufferAllData.append( "		TB_CCB_MACHINE TAB1,		" );
		stringBufferAllData.append( "		TB_MACHINE_USE_HISTORY TAB2," );
		stringBufferAllData.append( "		TB_CCB_ORGANIZATION TAB3	" );
		stringBufferAllData.append( "	WHERE							" );
		stringBufferAllData.append( "		TAB1.MACHINE_ID = TAB2.MACHINE_ID	" );
		stringBufferAllData.append( "	AND TAB1.ORG_ID = TAB3.ORG_ID	" );

		if ( "day".equals( searchTimeType ) ) {
			stringBufferAllData.append( "	AND TAB2.DATE = 			   '" );
			stringBufferAllData.append( dateTime );
			stringBufferAllData.append( "'" );
		}
		else if ( "week".equals( searchTimeType ) ) {

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			Date date = null;
			try {
				date = sdf.parse( dateTime );
			}
			catch ( ParseException e ) {
				e.printStackTrace();
			}
			c.setTime( date );
			c.set( Calendar.DAY_OF_WEEK, Calendar.SUNDAY );
			Date first = c.getTime();
			c.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
			Date last = c.getTime();

			stringBufferAllData.append( "	AND TAB2.DATE BETWEEN '" );
			stringBufferAllData.append( sdf.format( first ) );
			stringBufferAllData.append( "' AND '" );
			stringBufferAllData.append( sdf.format( last ) );
			stringBufferAllData.append( "'" );
		}
		else if ( "month".equals( searchTimeType ) ) {
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			Date date = null;
			try {
				date = sdf.parse( dateTime );
			}
			catch ( ParseException e ) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( date );
			calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMinimum( Calendar.DAY_OF_MONTH ) );
			String monthStart = sdf.format( calendar.getTime() );
			calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );
			String monthEnd = sdf.format( calendar.getTime() );

			stringBufferAllData.append( "	AND TAB2.DATE BETWEEN '" );
			stringBufferAllData.append( monthStart );
			stringBufferAllData.append( "' AND '" );
			stringBufferAllData.append( monthEnd );
			stringBufferAllData.append( "'" );
		}
		else if ( "anyTime".equals( searchTimeType ) ) {
			startTime = startTime.replaceAll( " ", "" );
			endTime = endTime.replaceAll( " ", "" );
			stringBufferAllData.append( "	AND TAB2.DATE BETWEEN		   '" );
			stringBufferAllData.append( startTime );
			stringBufferAllData.append( "' AND '" );
			stringBufferAllData.append( endTime );
			stringBufferAllData.append( "'" );
		}

		String allOrgIds = this.getOrgInfo( orgId );
		if ( "''".equals( allOrgIds ) ) {
			stringBufferAllData.append( " AND TAB1.ORG_ID = '" );
			stringBufferAllData.append( orgId );
			stringBufferAllData.append( "'					 " );
		}
		else {
			stringBufferAllData.append( " AND TAB1.ORG_ID IN (" );
			stringBufferAllData.append( allOrgIds + ",'" + orgId );
			stringBufferAllData.append( "')							" );
		}

		StringBuffer stringBufferDetailData = new StringBuffer();

		stringBufferDetailData.append( "	SELECT DISTINCT	" );
		stringBufferDetailData.append( "		T2.ORG_NAME," );
		stringBufferDetailData.append( "		T1.MACHINE_ID," );
		stringBufferDetailData.append( "		'网银自动设备' AS MACHINE_TYPE," );
		stringBufferDetailData.append( "		T3.RUNNING_TIME," );
		stringBufferDetailData.append( "		T3.VALID_USE_TIME," );
		stringBufferDetailData.append( "		(T3.RUNNING_TIME - T3.VALID_USE_TIME) AS FREE_TIME" );
		stringBufferDetailData.append( "	FROM" );
		stringBufferDetailData.append( "		TB_CCB_MACHINE T1," );
		stringBufferDetailData.append( "		TB_CCB_ORGANIZATION T2," );
		stringBufferDetailData.append( "		TB_MACHINE_USE_HISTORY T3" );
		stringBufferDetailData.append( "	WHERE" );
		stringBufferDetailData.append( "		T1.ORG_ID = T2.ORG_ID" );
		stringBufferDetailData.append( "		AND T1.MACHINE_ID = T3.MACHINE_ID" );
		stringBufferDetailData.append( "	AND (T2.ORG_ID IN (" );
		if ( "''".equals( allOrgIds ) ) {
			stringBufferDetailData.append( "'" );
			stringBufferDetailData.append( orgId );
			stringBufferDetailData.append( "')									" );
		}
		else {
			stringBufferDetailData.append( allOrgIds + ",'" + orgId );
			stringBufferDetailData.append( "'))									" );
		}
		if ( "day".equals( searchTimeType ) ) {
			stringBufferDetailData.append( "	AND T3.DATE = 			   '" );
			stringBufferDetailData.append( dateTime );
			stringBufferDetailData.append( "'" );
		}
		else if ( "week".equals( searchTimeType ) ) {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			Date date = null;
			try {
				date = sdf.parse( dateTime );
			}
			catch ( ParseException e ) {
				e.printStackTrace();
			}
			c.setTime( date );
			c.set( Calendar.DAY_OF_WEEK, Calendar.SUNDAY );
			Date first = c.getTime();
			c.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
			Date last = c.getTime();

			stringBufferDetailData.append( "	AND T3.DATE BETWEEN '" );
			stringBufferDetailData.append( sdf.format( first ) );
			stringBufferDetailData.append( "' AND '" );
			stringBufferDetailData.append( sdf.format( last ) );
			stringBufferDetailData.append( "'" );
		}
		else if ( "month".equals( searchTimeType ) ) {

			SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
			Date date = null;
			try {
				date = sdf.parse( dateTime );
			}
			catch ( ParseException e ) {
				e.printStackTrace();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.setTime( date );
			calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMinimum( Calendar.DAY_OF_MONTH ) );
			String monthStart = sdf.format( calendar.getTime() );
			calendar.set( Calendar.DAY_OF_MONTH, calendar.getActualMaximum( Calendar.DAY_OF_MONTH ) );
			String monthEnd = sdf.format( calendar.getTime() );

			stringBufferDetailData.append( "	AND T3.DATE  BETWEEN '" );
			stringBufferDetailData.append( monthStart );
			stringBufferDetailData.append( "' AND '" );
			stringBufferDetailData.append( monthEnd );
			stringBufferDetailData.append( "'" );
		}
		else if ( "anyTime".equals( searchTimeType ) ) {
			startTime = startTime.replaceAll( " ", "" );
			endTime = endTime.replaceAll( " ", "" );
			stringBufferDetailData.append( "	AND T3.DATE BETWEEN		   '" );
			stringBufferDetailData.append( startTime );
			stringBufferDetailData.append( "' AND '" );
			stringBufferDetailData.append( endTime );
			stringBufferDetailData.append( "'" );
		}

		if ( "1".equals( sequenceType ) ) {
			stringBufferDetailData.append( "	ORDER BY FREE_TIME ASC 	" );
		}
		else {
			stringBufferDetailData.append( "	ORDER BY FREE_TIME DESC 	" );
		}

		StringBuffer stringBuffer = null;
		if ( "allData".equals( dataType ) ) {
			stringBuffer = stringBufferAllData;
		}
		if ( "detailData".equals( dataType ) ) {
			stringBuffer = stringBufferDetailData;
		}

		ResultSet rs = null;
		List<ClientUseBean> list = new ArrayList<ClientUseBean>();
		if ( con == null ) {
			try {
				connection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		try {
			stmt = con.prepareStatement( stringBuffer.toString() );
			stmt.clearParameters();
			rs = stmt.executeQuery();
			if ( rs != null ) {
				if ( "allData".equals( dataType ) ) {
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setOrgName( rs.getString( "ORG_NAME" ) );
						clientUseBean.setClickCount( rs.getString( "CLICK_COUNT" ) );
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );

						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setRate( String.valueOf( Double.parseDouble( useRate ) * 100 ) + "%" );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );

							String rateReport = String.valueOf( Double.parseDouble( useRate ) * 100 );

							if ( rateReport.substring( rateReport.indexOf( "." ), rateReport.length() ).length() > 2 ) {
								rateReport = rateReport.substring( 0, rateReport.indexOf( "." ) + 2 );
							}
							clientUseBean.setRate( rateReport );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}

						list.add( clientUseBean );
					}
				}
				if ( "detailData".equals( dataType ) ) {
					while ( rs.next() ) {
						ClientUseBean clientUseBean = new ClientUseBean();
						clientUseBean.setBranches( rs.getString( "ORG_NAME" ) );
						clientUseBean.setMachineId( rs.getString( "MACHINE_ID" ) );
						clientUseBean.setMachineType( rs.getString( "MACHINE_TYPE" ) );
						String validTime = rs.getString( "VALID_USE_TIME" );
						clientUseBean.setValidTime( validTime );
						String runningTime = rs.getString( "RUNNING_TIME" );
						clientUseBean.setOpenTime( runningTime );
						NumberFormat formatter = new DecimalFormat( "0.00" );
						Double rate = new Double( Double.parseDouble( validTime ) / Double.parseDouble( runningTime ) );
						String useRate = formatter.format( rate );
						String freeTime = rs.getString( "FREE_TIME" );
						clientUseBean.setFreeTime( freeTime );

						Double freeRateDouble = new Double( Double.parseDouble( freeTime )
								/ Double.parseDouble( runningTime ) );
						String freeRate = formatter.format( freeRateDouble );

						if ( "1".equals( sequenceType ) ) {// 升序
							clientUseBean.setSortKey( "freeUseRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						else if ( "0".equals( sequenceType ) ) {// 降序
							clientUseBean.setSortKey( "useRate" );
							clientUseBean.setUseRate( Double.parseDouble( useRate ) * 100 );
							clientUseBean.setFreeUseRate( Double.parseDouble( freeRate ) * 100 );
						}
						list.add( clientUseBean );
					}
				}

			}
		}
		catch ( SQLException e ) {
			e.printStackTrace();
		}
		finally {
			try {
				releaseConnection();
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
		}
		// 排序
		Collections.sort( list );
		List<ClientUseBean> data = new ArrayList<ClientUseBean>();
		for ( int i = 0; i < list.size(); i++ ) {
			ClientUseBean channelBean = list.get( i );
			channelBean.setIndex( i + 1 );
			data.add( channelBean );
		}
		return data;
	}
}
