/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.report;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import th.action.MonitorAction;
import th.action.OrgDealAction;
import th.com.property.LocalProperties;
import th.com.util.Define4Report;
import th.dao.OrgDealDAO;
import th.dao.ReportDAO;
import th.user.User;
import th.util.DateUtil;
import th.util.FileTools;
import th.util.StringUtils;
import th.util.ftp.FtpUtils;
import edu.emory.mathcs.backport.java.util.Collections;

/**
 * Descriptions
 * 
 * @version 2013-8-20
 * @author PSET
 * @since JDK1.6
 * 
 */
public class ReportCommonAction {
	private static final Logger	logger	= Logger.getLogger(FtpUtils.class);
	public static final String FTP_FILE_TYPE_CHANNEL = "channel";
	public static final String FTP_FILE_TYPE_ADVERT = "advertisement";
	public static final String FTP_UPLOAD_FILE_BACKUP_DIR_NAME = "backup";
	public static final String FTP_UPLOAD_FILE_BACKUPERROR_DIR_NAME = "backuperror";
	/**
	 * 取得查询结果的总页数
	 * 
	 * @param sourceList
	 * @return
	 */
	public static int getTotalPageCount( ArrayList list ) throws Exception {
		int totalPageCount = 0;
		int viewCount = 10;
		if ( list != null && list.size() > 0 ) {
			int length = list.size();
			totalPageCount = ( (double) length / (double) viewCount ) > ( length / viewCount ) ? length / viewCount + 1
					: length / viewCount;
		}

		return totalPageCount;

	}
	
	public static int getTotalPageCount( int length ) throws Exception {
		int totalPageCount = 0;
		int viewCount = 10;
		if ( length > 0 ) {
			
			totalPageCount = ( (double) length / (double) viewCount ) > ( length / viewCount ) ? length / viewCount + 1
					: length / viewCount;
		}

		return totalPageCount;

	}

	/**
	 * 取得组织及组织下子组织的orgId，以,分隔。如：1,2,3
	 * 
	 * @param parentId
	 * @return
	 */
	public static String getOrgIdsByParentId( String parentId ) {
		StringBuffer sb = new StringBuffer( parentId );
		OrgDealDAO orgDealDAO = new OrgDealDAO();
		String orgIds = orgDealDAO.getSubOrg( parentId );
		if ( orgIds != null && orgIds.trim().length() > 0 ) {
			sb.append( "," + orgIds );
		}
		return sb.toString();
	}
	
	
	/**
	 * 取得该组织下第一级银行的orgId, 如果组织下无下一级分行,则列其本身，以,分隔。如：1,2,3
	 * 
	 * @param parentId
	 * @return
	 */
	public static String getFirstOrgIdsByParentId( String parentId ) {
		StringBuffer sb = new StringBuffer( parentId );
		OrgDealDAO orgDealDAO = new OrgDealDAO();
		
//		String orgIds = orgDealDAO.getSubOrg( parentId );
		
		String orgIds = null;
		try {
			HashMap[] childLevelOrgNodes = orgDealDAO.getFirstNodesByPIdList( parentId );
			if(childLevelOrgNodes!=null&&childLevelOrgNodes.length==0){
				childLevelOrgNodes = orgDealDAO.getCurOrgNodeByOrgId( Long.parseLong(parentId) );
			}
			orgIds = new OrgDealAction().getPIdFirstListByMap( childLevelOrgNodes );
		} catch (SQLException e) {
		}
		
		return orgIds;
	}
	public static String getFirstOrgIdsByParentIdMachine( String parentId ) {
		StringBuffer sb = new StringBuffer( parentId );
		OrgDealDAO orgDealDAO = new OrgDealDAO();
		
//		String orgIds = orgDealDAO.getSubOrg( parentId );
		
		String orgIds = null;
		try {
			boolean orgLevelFlg = orgDealDAO.getLeafByOrgId(parentId);
			HashMap[] childLevelOrgNodes = null;
			if(!orgLevelFlg){
				childLevelOrgNodes = orgDealDAO.getFirstNodesByPIdList( parentId );
			}else{
				childLevelOrgNodes = orgDealDAO.getFirstNodesByPIdListLeaf( parentId );
			}
			
			orgIds = new OrgDealAction().getPIdFirstListByMap( childLevelOrgNodes );
		} catch (SQLException e) {
		}
		
		return orgIds;
	}
	/**
	 * 取得组织及组织下子组织的orgId
	 * 
	 * @param orgId
	 * @return
	 */
	public static List<String> getOrgIdChildIds( String orgId ) {
		List<String> resultList = new ArrayList<String>();
		String orgIds = getOrgIdsByParentId( orgId );
		if ( !StringUtils.isBlank( orgIds ) ) {
			String[] orgIdArray = orgIds.split( "," );
			Collections.addAll( resultList, orgIdArray );
		}
		return resultList;
	}

	/**
	 * 取得用户的组织
	 * 
	 * @param req
	 * @throws Exception
	 */
	public static String getOrgNodes( HttpServletRequest req ) throws Exception {
		User user = (User) req.getSession().getAttribute( "user_info" );
		long orgId = Long.parseLong( user.getOrg_id() );

		OrgDealAction orgDealAction = new OrgDealAction();
		List<HashMap> orgNodesList = orgDealAction.getChildNodesByOrgId( orgId );

		MonitorAction monitorAction = new MonitorAction();
		String orgNodes = monitorAction.buildOrgOption( orgNodesList );

		return orgNodes;

	}
	
	/**
	 * 取得用户的组织
	 * 当前用户org_level=0时 显示总行和省级分行
	 * 当前用户org_level=1时 只显示当前省级分行
	 * 当前用户org_level=2时 返回"------"
	 * 
	 * @param req
	 * @throws Exception
	 */
	public static String getOrgOrderNodes( HttpServletRequest req ) {
		User user = (User) req.getSession().getAttribute( "user_info" );
		long orgId = Long.parseLong( user.getOrg_id() );

		OrgDealAction orgDealAction = new OrgDealAction();
		int orgLevel = orgDealAction.getCurOrgLevelbyOrgId(orgId);
		List<HashMap> orgNodesList = new ArrayList<HashMap>();;
		if (0 == orgLevel){
			orgNodesList = orgDealAction.getFirstAndSelfChildNodesByOrgId( orgId );
		} else if (1 == orgLevel){
			orgNodesList = orgDealAction.getCurrentNodesByOrgId( orgId );
		} else if (2 == orgLevel){
			orgNodesList = null;//
		} else {
			orgNodesList = null;
		}
		
		MonitorAction monitorAction = new MonitorAction();
		String orgNodes = monitorAction.buildOrderOrgOption( orgNodesList );

		return orgNodes;

	}

	/**
	 * 小数转百分比数
	 * 
	 * @param decimal
	 * @return
	 */
	public static String decimalFormat( double decimal ) {
		if ( decimal == 0 ) {
			return "0%";
		}
		String pattern = "00%"; // 00：保留小数点后两位
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getPercentInstance();
		decimalFormat.applyPattern( pattern );
		return decimalFormat.format( decimal );
	}

	/**
	 * 取得翻页显示的数据 （每页显示10条数据）
	 * 
	 * @param pageNumber
	 * @param sourceList
	 * @return
	 */
	public static ArrayList getViewList( int pageNumber, ArrayList sourceList ) throws Exception {
		int viewCount = 10;
		ArrayList resultList = new ArrayList();
		if ( sourceList != null && sourceList.size() > 0 ) {
			int start = ( pageNumber - 1 ) * viewCount;
			int end = pageNumber * viewCount;
			int copyCount = sourceList.size() < end ? ( sourceList.size() - start ) : viewCount;
			Object[] resultArray = new Object[copyCount];
			Object[] sourceArray = sourceList.toArray( new Object[sourceList.size()] );
			System.arraycopy( sourceArray, start, resultArray, 0, copyCount );
			Collections.addAll( resultList, resultArray );
		}

		return resultList;

	}

	/**
	 * 处理FTP上传的频道文件
	 * 
	 * @throws Exception
	 */
	public static void processFTPChannelFile() {
		try {
			
			String uploadPath = LocalProperties.getProperty( Define4Report.FTP_UPLOAD_FILE_PATH_CHANNEL );
			logger.debug(" uploadPath is :"+uploadPath);
			if ( null == uploadPath ) {
				return;
			}
			processFTPFile( uploadPath, FTP_FILE_TYPE_CHANNEL );
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	/**
	 * 处理FTP上传的广告文件
	 * 
	 * @throws Exception
	 */
	public static void processFTPAdvertFile() {
		logger.info(" processFTPAdvertFile");
		try {
			String uploadPath = LocalProperties.getProperty( Define4Report.FTP_UPLOAD_FILE_PATH_ADVERTISEMENT );
			logger.info(" uploadPath is:"+uploadPath);
			if ( null == uploadPath ) {
				return;
			}
			processFTPFile( uploadPath, FTP_FILE_TYPE_ADVERT );
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

	}

	/**
	 * 处理FTP上频道文件和广告文件
	 * 
	 * @param uploadPath
	 * @param backupPath
	 * @param ftpFileType
	 * @throws Exception
	 */
	private static void processFTPFile( String uploadPath, String ftpFileType ) throws Exception {
		if ( !FTP_FILE_TYPE_CHANNEL.equals( ftpFileType ) && !FTP_FILE_TYPE_ADVERT.equals( ftpFileType ) ) {
			return;
		}
		// 取得FTP频道目录下所有文件名列表
		List<String> fileNameList = new ArrayList<String>();
		FTPFile[] files = FtpUtils.getFiles( uploadPath );
		if ( files != null && files.length > 0 ) {
			for ( int i = 0; i < files.length; i++ ) {
				FTPFile file = files[i];
				if ( file.isDirectory() ) {
					continue;
				}
				fileNameList.add( file.getName() );
			}
		}
		// 遍历文件列表，处理文件（读取文件、将文件内容整理后插入DB）
		if ( fileNameList.size() > 0 ) {
			FTPClient ftpClient = null;
			InputStream inputStream = null;
			try {
				for ( int i = 0; i < fileNameList.size(); i++ ) {
					String fileName = fileNameList.get( i );
					try {
						
						// 读取文件每行的内容到List中
						ftpClient = FtpUtils.getFTPClient();
						ftpClient.changeWorkingDirectory( uploadPath );
						inputStream = ftpClient.retrieveFileStream( fileName );
						List<String> fileLineContentList = getFileLineContentList( inputStream );
						inputStream.close();
						// 根据文件类型插入到DB中，移动文件到备份目录
						if ( fileLineContentList == null || fileLineContentList.size() == 0 ) {
							
							//throw new Exception( "file content error." );
							ftpClient = FtpUtils.getFTPClient();
							ftpClient.changeWorkingDirectory( uploadPath );

							String backupPathNameError = FileTools.buildFullFilePath( FTP_UPLOAD_FILE_BACKUPERROR_DIR_NAME, fileName );
							boolean isSuccess = ftpClient.rename( fileName, backupPathNameError );
							
							continue;
						}
						processFileContent( fileName, fileLineContentList, ftpFileType, uploadPath );
					}
					catch ( Exception e ) {
						// 输出log
						e.printStackTrace();
					}

				}

			}
			finally {
				try {
					if ( ftpClient != null ) {
						ftpClient.disconnect();
					}
					if ( inputStream != null ) {
						inputStream.close();
					}
				}
				catch ( IOException e ) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 读取文件，文件每行的内容是以,分隔的。
	 * 
	 * @param in
	 * @return
	 */
	private static List<String> getFileLineContentList( InputStream inputStream ) throws Exception {
		BufferedReader bufferedReader = null;
		InputStreamReader inputStreamReader = null;
		List<String> resultList = new ArrayList<String>();
		try {
			inputStreamReader = new InputStreamReader( inputStream, "UTF-8" );
			bufferedReader = new BufferedReader( inputStreamReader );	
			String aLineContent=bufferedReader.readLine();
			while (aLineContent != null&& aLineContent.indexOf( "," ) > 0) {
				resultList.add( aLineContent );
				aLineContent=bufferedReader.readLine();
			}

//			while ( bufferedReader.ready() ) {
//				String aLineContent = bufferedReader.readLine();
//				if ( aLineContent != null && aLineContent.indexOf( "," ) > 0 ) {
//					resultList.add( aLineContent );
//
//				}
//			}	
		}
		catch ( IOException e ) {	
			e.printStackTrace();
		}
		finally {
			try {
				if ( bufferedReader != null ) {	
					bufferedReader.close();
				}
				if ( inputStreamReader != null ) {
					inputStreamReader.close();
				}
			}
			catch ( IOException e ) {	
				e.printStackTrace();
			}
		}
		System.out.println(resultList.size());	
		return resultList;

	}

	/**
	 * 处理文件内容(1、将文件内容插入DB； 2、移动文件到备份目录；3、文件移动成功后DB commit)
	 * 
	 * @param fileName
	 * @param insertList
	 * @param ftpFileType
	 * @param uploadPath
	 * @param backupPath
	 */
	private static void processFileContent( String fileName, List<String> insertList, String ftpFileType,
			String uploadPath ) {
		ReportDAO reportDAO = null;
		FTPClient ftpClient = null;
		try {
			// 根据文件名取得组织ID和机器ID
			if ( fileName.indexOf( "_" ) < 1 ) {
				throw new Exception( "file name error." );
			}
			String mac = fileName.split( "_" )[0];
			String sDate = fileName.split( "_" )[1];
			
			if ( fileName.indexOf( "_" ) > 1 ) {
				mac = fileName.substring(0, fileName.lastIndexOf("_"));
				sDate = fileName.substring(fileName.lastIndexOf("_") + 1);
			}
			reportDAO = new ReportDAO();
			HashMap[] map = reportDAO.getOrgMachineIdByMac( mac );
			if ( map == null || map.length < 1 ) {
				//throw new Exception( "mac error." );
				throw new Exception( "mac error." );
			}
			String orgId = (String) map[0].get( "ORG_ID" );
			String machineId = (String) map[0].get( "MACHINE_ID" );
			reportDAO.releaseConnection();
			// 插入DB
			reportDAO = new ReportDAO();
			if ( FTP_FILE_TYPE_CHANNEL.equals( ftpFileType ) ) {
				// 频道文件每行的内容:频道ID,点击次数
				java.sql.Date date = getSqlDate( sDate );
				reportDAO.insertBatchChannelClickHistory( insertList, machineId, date );

			}
			else if ( FTP_FILE_TYPE_ADVERT.equals( ftpFileType ) ) {
				// 节目单ID,布局ID,素材的文件名,开始播放时间,结束播放时间,点击状态
				reportDAO.insertBatchAdvertPalyHistory( insertList, orgId, machineId );
			}

			// 移动文件到备份目录
			ftpClient = FtpUtils.getFTPClient();
			ftpClient.changeWorkingDirectory( uploadPath );
			String backupPathName = FileTools.buildFullFilePath( FTP_UPLOAD_FILE_BACKUP_DIR_NAME, fileName );
			boolean isSuccess = ftpClient.rename( fileName, backupPathName );
			if ( !isSuccess ) {
				throw new Exception( "file backup error." );
			}

			// DB插入提交
			reportDAO.commit();

		}
		catch ( Exception e ) {
			try {
				// 移动文件到备份目录
				ftpClient = FtpUtils.getFTPClient();
				ftpClient.changeWorkingDirectory( uploadPath );
				String backupPathNameError = FileTools.buildFullFilePath( FTP_UPLOAD_FILE_BACKUPERROR_DIR_NAME, fileName );
				boolean isSuccess = ftpClient.rename( fileName, backupPathNameError );
				if ( !isSuccess ) {
					throw new Exception( "error file backup error." );
				}
				if ( reportDAO != null ) {
					reportDAO.rollback();
				}
				if ( ftpClient != null ) {
					ftpClient.disconnect();
				}
				
			}
			catch ( Exception ex ) {
				e.printStackTrace();
			}

		}
		finally {
			try {
				if ( reportDAO != null ) {
					reportDAO.releaseConnection();
				}
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 取得SQL使用的Date
	 * 
	 * @param sDate
	 *            YYYYMMDD
	 * @return
	 */
	private static java.sql.Date getSqlDate( String sDate ) {
		try {
			String pattern = "yyyyMMdd";
			long time = DateUtil.getDateTime( sDate, pattern );
			java.sql.Date date = new java.sql.Date( time );
			return date;
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}

		return null;
	}

}
