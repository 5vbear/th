/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.availablePage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.log4j.Logger;

import th.action.BaseAction;
import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.dao.ChannelDAO;
import th.dao.UploadDao;
import th.dao.AvailablePage.AvailablePageDAO;
import th.entity.AvailablePageBean;
import th.util.FileTools;
import th.util.StringUtils;
import th.util.ftp.FtpUtils;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class AuditAction extends BaseAction {
	protected Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 获取白名单List
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public HashMap[] getorg(String orgID) throws Exception, SQLException {
		String orgIds = new AvailablePageDAO().getSubOrgById(orgID);
		if (orgIds != null && !"".equals(orgIds)) {
			orgIds += "," + orgID;
		} else {
			orgIds = orgID;
		}
		HashMap[] maps = new AvailablePageDAO().getAllOrganizations(orgIds);
		return maps;
	}
	/**
	 * 查询频道列表
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 * @throws UnsupportedEncodingException 
	 */
	public HashMap[] getSearchInfoByNameUrl(String search_con_info,String orgID,String channelType) throws SQLException, UnsupportedEncodingException{
		HashMap[] maps = null;
		String[] searchValues = search_con_info.split( "," );
		String name= new String(searchValues[0]);
		String url = new String(searchValues[1]);
		maps = new AvailablePageDAO().getSearchInfoByNameUrl(name,url,orgID,channelType);
		return maps;
	}
	/**
	 * 获取组织
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public HashMap[] getAvailableList(String orgID, String type)
			throws Exception, SQLException {
		HashMap[] maps = null;
		if ("availablelist".equals(type)) {// 如果要检索的是白名单
			maps = new AvailablePageDAO().getAvailableList(orgID);
		} else if ("channel".equals(type)) {// 如果要检索的是频道
			HashMap[] result = new ChannelDAO()
					.getAllChannelByOrgIDInit("0", orgID);
			if (result != null) {
				maps = new HashMap[result.length];
				for (int i = 0; i < result.length; i++) {
					HashMap hm = new HashMap();
					hm.put("REQUEST_ID", result[i].get("CHANNEL_ID"));
					hm.put("REQUEST_NAME", result[i].get("CHANNEL_NAME"));
					hm.put("REQUEST_URL", result[i].get("CHANNEL_URL"));
					hm.put("FLAG", result[i].get("FLAG"));
					hm.put("ENTERPRISES_TYPE", result[i].get("ENTERPRISES_TYPE"));
					hm.put( "STATUS", result[i].get("STATUS") );
					maps[i] = hm;
				}
			}
		} else if ("quick".equals(type)) {// 如果要检索的是快速入口
			HashMap[] result = new ChannelDAO()
					.getAllChannelByOrgIDInit("1", orgID);
			if (result != null) {
				maps = new HashMap[result.length];
				for (int i = 0; i < result.length; i++) {
					HashMap hm = new HashMap();
					hm.put("REQUEST_ID", result[i].get("CHANNEL_ID"));
					hm.put("REQUEST_NAME", result[i].get("CHANNEL_NAME"));
					hm.put("REQUEST_URL", result[i].get("CHANNEL_URL"));
					hm.put("FLAG", result[i].get("FLAG"));
					maps[i] = hm;
				}
			}
		} else if ("document".equals(type)) {// 如果要检索的是企业文档管理
			HashMap[] result = new AvailablePageDAO().getDocumentList(orgID);

			if (result != null) {
				maps = new HashMap[result.length];
				for (int i = 0; i < result.length; i++) {
					HashMap hm = new HashMap();
					hm.put("REQUEST_ID", result[i].get("DOCUMENT_ID"));
					hm.put("REQUEST_NAME", result[i].get("DOCUMENT_NAME"));
					hm.put("REQUEST_URL", result[i].get("DOCUMENT"));
					maps[i] = hm;
				}
			}
		}

		return maps;
	}

	/**
	 * 获取组织
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public HashMap[] getDocumentById(String Id)
			throws Exception, SQLException {
		HashMap[] maps = null;
		
			HashMap[] result = new AvailablePageDAO().getDocumentById(Id);

			if (result != null) {
				maps = new HashMap[1];
			
					HashMap hm = new HashMap();
					hm.put("REQUEST_ID", result[0].get("DOCUMENT_ID"));
					hm.put("REQUEST_NAME", result[0].get("DOCUMENT_NAME"));
					hm.put("REQUEST_URL", result[0].get("DOCUMENT"));
					maps[0] = hm;
				
			}

		return maps;
	}

	/**
	 * 上层组织ID
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public HashMap[] getAvaiableBymacId(String macId) throws Exception,
			SQLException {
		String orgId = new AvailablePageDAO().getOrgIdBymacId(macId);
		HashMap[] maps = new AvailablePageDAO().getAvailableList(orgId);

		return maps;
	}

	/**
	 * 插入白名单
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public int insertAvailablePage(AvailablePageBean bean, String type)
			throws Exception, SQLException {
		int result = 0;
		if ("availablelist".equals(type)) {// 如果要插入的是白名单
			result = new AvailablePageDAO().insertAvailablePage(bean);
		} else if ("channel".equals(type)) {// 如果要插入的是频道
			result = new ChannelDAO().insertChannel(bean,"2");
		} else if ("quick".equals(type)) {// 如果要插入的是快速入口
			result = new ChannelDAO().insertChannel(bean,"1");
		} else if ("document".equals(type)) {// 如果要插入的是企业文档管理
			result = new AvailablePageDAO().insertDocument(bean);

		}
		return result;
	}

	/**
	 * 下发
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public int downData(AvailablePageBean bean)
			throws Exception, SQLException {
		int result = 0;

			result = new AvailablePageDAO().downDocument(bean);

		return result;
	}
	/**
	 * 修改企业文档
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public int updateDocument(String requestId ,AvailablePageBean bean)
			throws Exception, SQLException {

		int result = 0;
			result = new AvailablePageDAO().updateDocument(requestId ,bean);

		
		return result;
	}

	public int updateChannel(String requestId ,AvailablePageBean bean)throws Exception, SQLException {
		String[] idAndKeySet = requestId.split(",");
		StringBuffer idSB = new StringBuffer();
		StringBuffer keySB = new StringBuffer();
		for (int i = 0; i < idAndKeySet.length; i++) {
			String[] idAndKey = idAndKeySet[i].split("@");
			if (i != idAndKeySet.length - 1) {
				idSB.append(idAndKey[0] + ",");
				keySB.append(idAndKey[1] + ",");
			} else {
				idSB.append(idAndKey[0]);
				keySB.append(idAndKey[1]);

			}
		}
		int result = 0;
		result = new AvailablePageDAO().updateChannel(idSB.toString() ,bean);
		return result;
	}
	/**
	 * 停用channel
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public int stopChannel(String requestId, String type, String orgid)
			throws Exception, SQLException {
		int result = 0;
		String[] idAndKeySet = requestId.split(",");
		StringBuffer idSB = new StringBuffer();
		StringBuffer keySB = new StringBuffer();
		for (int i = 0; i < idAndKeySet.length; i++) {
			String[] idAndKey = idAndKeySet[i].split("@");
			if (i != idAndKeySet.length - 1) {
				idSB.append(idAndKey[0] + ",");
				keySB.append(idAndKey[1] + ",");
			} else {
				idSB.append(idAndKey[0]);
				keySB.append(idAndKey[1]);

			}
		}
		if ("availablelist".equals(type)) {// 如果要删除的是白名单
			result = new AvailablePageDAO().deleteAvailables(keySB.toString(),
					orgid);
		} else if ("channel".equals(type)) {
			new ChannelDAO().stopChannel( keySB.toString(),orgid);
		} else if ("quick".equals(type)) {
			new ChannelDAO().deleteChannelByID(keySB.toString(), orgid);
		} else if ("document".equals(type)) {// 如果要删除的是企业文档管理
			result = new AvailablePageDAO().deleteDocument(idSB.toString(),
					orgid);
		}
		return result;
	}
	
	/**
	 * 启用channel
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public int openChannel(String requestId, String type, String orgid)
			throws Exception, SQLException {
		logger.info("要启用频道："+requestId);
		int result = 0;
		String[] idAndKeySet = requestId.split(",");
		StringBuffer idSB = new StringBuffer();
		StringBuffer keySB = new StringBuffer();
		for (int i = 0; i < idAndKeySet.length; i++) {
			String[] idAndKey = idAndKeySet[i].split("@");
			if (i != idAndKeySet.length - 1) {
				idSB.append(idAndKey[0] + ",");
				keySB.append(idAndKey[1] + ",");
			} else {
				idSB.append(idAndKey[0]);
				keySB.append(idAndKey[1]);

			}
		}
		if ("availablelist".equals(type)) {// 如果要启用的是白名单
			result = new AvailablePageDAO().deleteAvailables(keySB.toString(),
					orgid);
		} else if ("channel".equals(type)) {
			new ChannelDAO().openChannel( keySB.toString(),orgid);
		} else if ("quick".equals(type)) {
			new ChannelDAO().deleteChannelByID(keySB.toString(), orgid);
		} else if ("document".equals(type)) {// 如果要启用的是企业文档管理
			result = new AvailablePageDAO().deleteDocument(idSB.toString(),
					orgid);
		}
		return result;
	}
	/**
	 * 删除白名单
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public int deleteAvailables(String requestId, String type, String orgid)
			throws Exception, SQLException {

		int result = 0;
		String[] idAndKeySet = requestId.split(",");
		StringBuffer idSB = new StringBuffer();
		StringBuffer keySB = new StringBuffer();
		for (int i = 0; i < idAndKeySet.length; i++) {
			String[] idAndKey = idAndKeySet[i].split("@");
			if (i != idAndKeySet.length - 1) {
				idSB.append(idAndKey[0] + ",");
				keySB.append(idAndKey[1] + ",");
			} else {
				idSB.append(idAndKey[0]);
				keySB.append(idAndKey[1]);

			}
		}
		if ("availablelist".equals(type)) {// 如果要删除的是白名单
			result = new AvailablePageDAO().deleteAvailables(keySB.toString(),
					orgid);
		} else if ("channel".equals(type)) {
			new ChannelDAO().deleteChannelByID(keySB.toString(), orgid);
		} else if ("quick".equals(type)) {
			new ChannelDAO().deleteChannelByID(keySB.toString(), orgid);
		} else if ("document".equals(type)) {// 如果要删除的是企业文档管理
			result = new AvailablePageDAO().deleteDocument(idSB.toString(),
					orgid);

		}
		return result;
	}

	/**
	 * 获取组织下的机器ID
	 * 
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public HashMap[] getMacList(String orgID) throws Exception, SQLException {
		HashMap[] resultMap = new AvailablePageDAO().getMachineInfoByOrgID(Long
				.parseLong(orgID));

		return resultMap;
	}

	/**
	 * 白名单下发
	 */

	public boolean sendUpdatePackagesToMachine(HashMap[] machinesArray,
			String orgID, String currentUserId) throws Exception {
		String FUNCTION_NAME = "sendUpdatePackagesToMachine";

		logger.info(FUNCTION_NAME + "start");

		try {
			String updateZipFilePath = LocalProperties
					.getProperty("FTP_DOWNLOAD_FILE_PATH_WHITELIST");
			logger.info(FUNCTION_NAME + "FTP下载的升级文件目录: " + updateZipFilePath);

			if (StringUtils.isBlank(updateZipFilePath)) {
				logger.error(FUNCTION_NAME + "FTP的路径为空!");
				return false;
			}

			// STEP1: 取得FTP当前目录下所有的ini文件
			// FTPFile[] ftpFileArray = FtpUtils.getFiles(updateZipFilePath);

			// STEP2: 根据选择的机器进行遍历,生成对应的 mac.ini文件
			for (HashMap machineMap : machinesArray) {

				// STEP2.1: 取得MAC地址
				String mac = (String) machineMap.get("MAC");
				String machineId = (String) machineMap.get("MACHINE_ID");

				if (mac == null) {
					continue;
				}
				logger.info(FUNCTION_NAME + "用户选择的MAC: " + mac);

				// STEP2.2: 如果该端机正在进行升级,不再升级
				// if (isUpdateFileExisted(ftpFileArray, mac)){
				// continue;
				// }

				// STEP2.3: 向DB中插入数据
				if (!insertSystemUpdateManagement(mac, updateZipFilePath)) {
					return false;
				}

				// STEP2.4: 向FTP上传文件
				uploadIniFileToFTP(getAvaiableBymacId(machineId),
						updateZipFilePath, mac);
			}
		} catch (LocalPropertiesException e) {
			e.printStackTrace();
			logger.error(FUNCTION_NAME + "FTP的路径为空!" + e);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(FUNCTION_NAME + "IO异常!" + e);
			return false;
		} finally {
			logger.info(FUNCTION_NAME + "end");
		}
		return true;
	}

	/**
	 * 企业文档下发
	 */

	public boolean sendDocumentToMachine(HashMap[] machinesArray, String orgID,
			String currentUserId) throws Exception {
		String FUNCTION_NAME = "sendDocumentToMachine";

		logger.info(FUNCTION_NAME + "start");

		try {
			String updateZipFilePath = LocalProperties
					.getProperty("FTP_DOWNLOAD_FILE_PATH_DOCUMENTLIST");
			logger.info(FUNCTION_NAME + "FTP下载的升级文件目录: " + updateZipFilePath);

			if (StringUtils.isBlank(updateZipFilePath)) {
				logger.error(FUNCTION_NAME + "FTP的路径为空!");
				return false;
			}

			// STEP1: 取得FTP当前目录下所有的ini文件
			// FTPFile[] ftpFileArray = FtpUtils.getFiles(updateZipFilePath);

			// STEP2: 根据选择的机器进行遍历,生成对应的 mac.ini文件
			for (HashMap machineMap : machinesArray) {

				// STEP2.1: 取得MAC地址
				String mac = (String) machineMap.get("MAC");
				String machineId = (String) machineMap.get("MACHINE_ID");

				if (mac == null) {
					continue;
				}
				logger.info(FUNCTION_NAME + "用户选择的MAC: " + mac);

				// STEP2.2: 如果该端机正在进行升级,不再升级
				// if (isUpdateFileExisted(ftpFileArray, mac)){
				// continue;
				// }

				// STEP2.3: 向DB中插入数据
				if (!insertSystemUpdateManagement_document(mac, updateZipFilePath)) {
					return false;
				}

				// STEP2.4: 向FTP上传文件
				uploadIniFileToFTP_document(getAvaiableBymacId(machineId),
						updateZipFilePath, mac);
			}
		} catch (LocalPropertiesException e) {
			e.printStackTrace();
			logger.error(FUNCTION_NAME + "FTP的路径为空!" + e);
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(FUNCTION_NAME + "IO异常!" + e);
			return false;
		} finally {
			logger.info(FUNCTION_NAME + "end");
		}
		return true;
	}

	/**
	 * 生成mac.ini文件,并上传至FTP指定目录中
	 * 
	 * <pre>
	 * Desc  
	 * @param packagedsArray
	 * @param updateZipFilePath
	 * @param mac
	 * @throws IOException
	 * @author wilson
	 * @refactor wilson
	 * @date   2013年8月20日 下午3:15:24
	 * </pre>
	 */
	private void uploadIniFileToFTP(HashMap[] packagedsArray,
			String updateZipFilePath, String mac) throws IOException {

		String FUNCTION_NAME = "uploadIniFileToFTP";
		String macIniLocalTmpPath = LocalProperties
				.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_WHITELIST");

		String suffix = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");
		logger.info("写入记录条数： " + packagedsArray.length);
		logger.info(FUNCTION_NAME + "升级文件本地临时目录: " + macIniLocalTmpPath);
		logger.info(FUNCTION_NAME + "升级文件后缀: " + suffix);

		// STEP1: 生成对应的临时目录
		FileTools.mkdirs(macIniLocalTmpPath);

		// STEP2: 向ini文件中写入数据
		writePackageInfoToIniFile(packagedsArray,
				FileTools.buildFullFilePath(macIniLocalTmpPath, mac + suffix));

		// STEP3: 上传文件列表
		FtpUtils.uploadFile(FileTools.getSubFiles(macIniLocalTmpPath),
				updateZipFilePath);

		// STEP4: 移除临时文件
		FileTools.removeSubFiles(macIniLocalTmpPath);
	}

	/**
	 * 生成mac.ini文件,并上传至FTP指定目录中
	 * 
	 * <pre>
	 * Desc  
	 * @param packagedsArray
	 * @param updateZipFilePath
	 * @param mac
	 * @throws IOException
	 * @author wilson
	 * @refactor wilson
	 * @date   2013年8月20日 下午3:15:24
	 * </pre>
	 */
	private void uploadIniFileToFTP_document(HashMap[] packagedsArray,
			String updateZipFilePath, String mac) throws IOException {

		String FUNCTION_NAME = "uploadIniFileToFTP";
		String macIniLocalTmpPath = LocalProperties
				.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_DOCUMENTLIST");

		String suffix = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");
		logger.info(FUNCTION_NAME + "升级文件本地临时目录: " + macIniLocalTmpPath);
		logger.info(FUNCTION_NAME + "升级文件后缀: " + suffix);

		// STEP1: 生成对应的临时目录
		FileTools.mkdirs(macIniLocalTmpPath);

		// STEP2: 向ini文件中写入数据
		writePackageInfoToIniFile(packagedsArray,
				FileTools.buildFullFilePath(macIniLocalTmpPath, mac + suffix));

		// STEP3: 上传文件列表
		FtpUtils.uploadFile(FileTools.getSubFiles(macIniLocalTmpPath),
				updateZipFilePath);

		// STEP4: 移除临时文件
		FileTools.removeSubFiles(macIniLocalTmpPath);
	}

	private boolean insertSystemUpdateManagement(String mac, String iniUrl) {
		try {
			AvailablePageDAO dao = new AvailablePageDAO();
			int machineId = new UploadDao().getMachinIdByMac(mac);
			dao.updateCommandManagement(machineId, iniUrl + "/" + mac + ".ini",
					"1", 24, new Date());

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("insertSystemUpdateManagement" + "数据插入失败!" + e);
			return false;
		}
		return true;
	}

	private boolean insertSystemUpdateManagement_document(String mac, String iniUrl) {
		try {
			AvailablePageDAO dao = new AvailablePageDAO();
			int machineId = new UploadDao().getMachinIdByMac(mac);
			dao.updateCommandManagement(machineId, iniUrl + "/" + mac + ".ini",
					"1", 31, new Date());

		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("insertSystemUpdateManagement" + "数据插入失败!" + e);
			return false;
		}
		return true;
	}
	/**
	 * 向Mac.ini文件中写入信息.每行用回车转行.新生成的文件保存至path目录中.
	 * 
	 * <pre>
	 * Desc  
	 * @param packagedsArray
	 * @param path
	 * @return
	 * @throws IOException
	 * @author PSET
	 * @refactor PSET
	 * @date   2013年8月20日 下午2:59:25
	 * </pre>
	 */
	private void writePackageInfoToIniFile(HashMap[] packagedsArray, String path)
			throws IOException {
		String FUNCTION_NAME = "writePackageInfoToIniFile";
		logger.info(FUNCTION_NAME + " 临时文件目录: " + path);
		
		FileWriter fw = new FileWriter(new File(path));
		BufferedWriter bw = new BufferedWriter(fw);

		for (HashMap packageMap : packagedsArray) {
			String packagePath = packageMap.get("REQUEST_URL").toString();
			bw.write(packagePath);
			bw.newLine();
		}
		fw.flush();
//		FileTools.closeWriter(bw);
		bw.close();
		fw.close();
	}

	/**
	 * 遍历FTP中的文件,如果已经存在,表示该端机正在进行升级,则不再生成 mac.ini 文件.
	 * 
	 * <pre>
	 * Desc  
	 * @param ftpFileArray
	 * @param mac
	 * @return
	 * @author PSET
	 * @refactor PSET
	 * @date   2013年8月20日 下午2:55:19
	 * </pre>
	 */
	private boolean isUpdateFileExisted(FTPFile[] ftpFileArray, String mac) {

		String FUNCTION_NAME = "isUpdateFileExisted";

		for (FTPFile ftpFile : ftpFileArray) {
			// 如果是目录,不再进行匹配
			if (ftpFile.isDirectory())
				continue;
			// 如果当前FTP目录中, 文件名以mac地址起始.那么该端机正在进行升级
			if (ftpFile.getName().startsWith(mac)) {
				logger.info("当前选择的MAC地址已经存在,升级文件: " + ftpFile.getName());
				return true;
			}
		}
		return false;
	}
	
	public HashMap[] editAvailablesPage(String availableIds) throws Exception, SQLException {
		String[] idAndKeySet = availableIds.split(",");
		StringBuffer idSB = new StringBuffer();
		StringBuffer keySB = new StringBuffer();
		for (int i = 0; i < idAndKeySet.length; i++) {
			String[] idAndKey = idAndKeySet[i].split("@");
			if (i != idAndKeySet.length - 1) {
				idSB.append(idAndKey[0] + ",");
				keySB.append(idAndKey[1] + ",");
			} else {
				idSB.append(idAndKey[0]);
				keySB.append(idAndKey[1]);

			}
		}
		HashMap[] resultMap = new AvailablePageDAO().getChennalInfoByID(Long
				.parseLong(idSB.toString()));

		return resultMap;
	}

}
