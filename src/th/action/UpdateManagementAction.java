package th.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.net.ftp.FTPFile;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.dao.OrgDealDAO;
import th.dao.UpdateManagementDao;
import th.dao.UploadDao;
import th.util.FileTools;
import th.util.StringUtils;
import th.util.ftp.FtpUtils;

public class UpdateManagementAction extends BaseAction {

	protected UpdateManagementDao	dao	= new UpdateManagementDao();
	
	public String getIosOrgJsonById() throws SQLException {
		Map[] allOrgsMap = dao.getAllOrgByJsonFormat();
		Map[] allMacsMap = dao.getIosMachineInfoWithOrgByJsonFormat();
		
		Map[] newAllMapArrya = new Map[allOrgsMap.length + allMacsMap.length];
		for (int j = 0; j < allOrgsMap.length; ++j) {
			newAllMapArrya[j] = allOrgsMap[j];
		}
		
		for (int j = 0; j < allMacsMap.length; ++j) {
			newAllMapArrya[allOrgsMap.length + j] = allMacsMap[j];
		}
		
		JSONArray obj = JSONArray.fromObject(newAllMapArrya);
		// obj.add(allMacsMap);
		return obj.toString();
	}

	public String getSonOrgJsonById() throws SQLException {
		Map[] allOrgsMap = dao.getAllOrgByJsonFormat();
		Map[] allMacsMap = dao.getMachineInfoWithOrgByJsonFormat();

		Map[] newAllMapArrya = new Map[allOrgsMap.length + allMacsMap.length];
		for (int j = 0; j < allOrgsMap.length; ++j) {
			newAllMapArrya[j] = allOrgsMap[j];
		}

		for (int j = 0; j < allMacsMap.length; ++j) {
			newAllMapArrya[allOrgsMap.length + j] = allMacsMap[j];
		}

		JSONArray obj = JSONArray.fromObject(newAllMapArrya);
		// obj.add(allMacsMap);
		return obj.toString();
	}
		
	public String getSonOrgJsonByIdFen(String orgId) throws SQLException {
		List<HashMap> orgList = new OrgDealAction().getChildNodesByOrgId(Long.parseLong(orgId));
		String temp = "(";
		for(int i=0;i<orgList.size();i++){
			temp = temp + orgList.get( i ).get( "ORG_ID" ).toString()+",";
		}
		temp = temp.substring( 0, temp.length()-1 );
		temp = temp+")";
		Map[] allOrgsMap = dao.getAllOrgByJsonFormatFen(temp);
		Map[] allMacsMap = dao.getMachineInfoWithOrgByJsonFormatFen(temp);

		Map[] newAllMapArrya = new Map[allOrgsMap.length + allMacsMap.length];
		for (int j = 0; j < allOrgsMap.length; ++j) {
			newAllMapArrya[j] = allOrgsMap[j];
		}

		for (int j = 0; j < allMacsMap.length; ++j) {
			newAllMapArrya[allOrgsMap.length + j] = allMacsMap[j];
		}

		JSONArray obj = JSONArray.fromObject(newAllMapArrya);
		// obj.add(allMacsMap);
		return obj.toString();
	}
	public String getSysSonOrgJsonById() throws SQLException {
		Map[] allOrgsMap = dao.getAllOrgByJsonFormat();
		Map[] allMacsMap = dao.getSysMachineInfoWithOrgByJsonFormat();
		
		Map[] newAllMapArrya = new Map[allOrgsMap.length + allMacsMap.length];
		for (int j = 0; j < allOrgsMap.length; ++j) {
			newAllMapArrya[j] = allOrgsMap[j];
		}
		
		for (int j = 0; j < allMacsMap.length; ++j) {
			newAllMapArrya[allOrgsMap.length + j] = allMacsMap[j];
		}
		
		JSONArray obj = JSONArray.fromObject(newAllMapArrya);
		// obj.add(allMacsMap);
		return obj.toString();
	}
	public int getUpdateDataCount() throws SQLException {
		return dao.getUpdateDataCount();
	}
	public int getSysUpdateDataCount() throws SQLException {
		return dao.getSysUpdateDataCount();
	}
	
	public HashMap[] getAllOSTypes() throws SQLException {
		return dao.getAllOSTypes();
	}
	public HashMap[] getAllOSInfo() throws SQLException {
		return dao.getAllOSInfo();
	}

	public String getMachineMarkByMachineID(String machineID) throws SQLException {
		return dao.getMachineMarkByMachineID(machineID);
	}

	public HashMap[] getUpdateData(final int firstResult, final int maxResults) throws SQLException {
		return dao.getUpdateData(firstResult, maxResults, null);
	}
	public HashMap[] getSysUpdateData(final int firstResult, final int maxResults) throws SQLException {
		return dao.getSysUpdateData(firstResult, maxResults, null);
	}
	public HashMap[] getAllUpdateData() throws SQLException {
		return dao.getUpdateData(0, -1, null);
	}

	public HashMap[] getUpdateDataByIds(String ids) throws SQLException {
		return dao.getUpdateData(0, -1, ids);
	}
	
	public HashMap[] getSysUpdateDataByIds(String ids) throws SQLException {
		return dao.getSysUpdateData(0, -1, ids);
	}
	
	public int deleteUpdateData(String ids) throws SQLException {
		//是否有在升级数据,如果有正在升级的返回false
		if(dao.getSystemUpdateManagementCount(ids, "1")>1){
			logger.error("有正在升级未完成的升级包在删除数据中!");
			return 1;
		}
		//在DB删除之前先把要删除的数据取到
		HashMap[] updateMapArray = dao.getUpdateData(0, -1, ids);
		//删除DB中数据
		if(dao.deleteUpdateData(ids)<1){
			logger.error("删除数据不存在!");
			return 2;
		}
		//删除FTP中文件
		String fileFtpUrl;
		try {
			fileFtpUrl = LocalProperties.getProperty("FTP_FILE_PATH_UPGRADEPACKAGE");
		} catch (LocalPropertiesException e) {
			e.printStackTrace();
			logger.error("取得FTP配置路径错误!"+e);
			return 3;
		}
		for (HashMap hashMap : updateMapArray) {
			String fileName = (String)hashMap.get("FILE_NAME");
			FtpUtils.deleteFile(fileName, fileFtpUrl);
			logger.info("删除文件: "+fileName);
		}
		
		return 9;
	}
	
	public int deleteSysUpdateData(String ids) throws SQLException {
		//是否有在升级数据,如果有正在升级的返回false
		if(dao.getSystemUpdateManagementCount(ids, "1")>1){
			logger.error("有正在升级未完成的升级包在删除数据中!");
			return 1;
		}
		//在DB删除之前先把要删除的数据取到
		HashMap[] updateMapArray = dao.getSysUpdateData(0, -1, ids);
		//删除DB中数据
		if(dao.deleteUpdateData(ids)<1){
			logger.error("删除数据不存在!");
			return 2;
		}
		//删除FTP中文件
		String fileFtpUrl;
		try {
			fileFtpUrl = LocalProperties.getProperty("FTP_FILE_PATH_UPGRADEPACKAGE");
		} catch (LocalPropertiesException e) {
			e.printStackTrace();
			logger.error("取得FTP配置路径错误!"+e);
			return 3;
		}
		for (HashMap hashMap : updateMapArray) {
			String fileName = (String)hashMap.get("FILE_NAME");
			FtpUtils.deleteFile(fileName, fileFtpUrl);
			logger.info("删除文件: "+fileName);
		}
		
		return 9;
	}

	public boolean sendUpdatePackagesToMachine(String macIds, String packageIds, String status, Date date, int currentUserId) throws SQLException {
		String FUNCTION_NAME = "sendUpdatePackagesToMachine";

		logger.info(FUNCTION_NAME + "start");
		if (StringUtils.isBlank(macIds)) {
			logger.error(FUNCTION_NAME + "选择的机器ID为空或者不存在!");
			return false;
		}

		if (StringUtils.isBlank(packageIds)) {
			logger.error(FUNCTION_NAME + "选择的升级包ID为空或者不存在!");
			return false;
		}
		HashMap[] packagedsArray = getUpdateDataByIds(packageIds);
		HashMap[] machinesArray = dao.getMachineInfoByMachineIds(macIds);

		try {
			String iniFilePath = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_PATH_UPGRADE");
			logger.info(FUNCTION_NAME + "FTP下载的升级文件目录: " + iniFilePath);

			if (StringUtils.isBlank(iniFilePath)) {
				logger.error(FUNCTION_NAME + "FTP的路径为空!");
				return false;
			}

			// STEP1: 取得FTP当前目录下所有的ini文件
			FTPFile[] ftpFileArray = FtpUtils.getFiles(iniFilePath);
			
			// STEP1_NEW: 先判断一下ZIP文件是否存在 
			String fileFtpUrl = LocalProperties.getProperty("FTP_FILE_PATH_UPGRADEPACKAGE");
			FTPFile[] zipFtpFileArray = FtpUtils.getFiles(fileFtpUrl);
			boolean isZipExisted = true;
			
			for (HashMap packageMap : packagedsArray) {
				String packageFileName = (String)packageMap.get("FILE_NAME");
				//3.判断文件是否存在
				if (!isUpdateFileExisted(zipFtpFileArray, packageFileName)) {
					isZipExisted = false;
				}
			}
			if(!isZipExisted){
				return false;
			}


			// STEP2: 根据选择的机器进行遍历,生成对应的 mac.ini文件
			for (HashMap machineMap : machinesArray) {

				// STEP2.1: 取得MAC地址
				String mac = (String) machineMap.get("MAC");
				logger.info(FUNCTION_NAME + "用户选择的MAC: " + mac);

				// STEP2.2: 如果该端机正在进行升级,不再升级
				if (isUpdateFileExisted(ftpFileArray, mac)) {
					continue;
				}

				// STEP2.3: 向FTP上传文件
				uploadIniFileToFTP(packagedsArray, iniFilePath, mac);
				
				// STEP2.4: 重新检查新的文件是否已经上传成功.如果已经上传成功.那么向DB中插入数据
				FTPFile[] newFtpFileArray = FtpUtils.getFiles(iniFilePath);
				if (isUpdateFileExisted(newFtpFileArray, mac)) {
					if (!insertSystemUpdateManagement33(mac, packagedsArray, status, date, currentUserId)) {
						return false;
					}
				}else{
					return false;
				}

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
	public boolean sendSysUpdatePackagesToMachine(String macIds, String packageIds, String status, Date date, int currentUserId) throws SQLException {
		String FUNCTION_NAME = "sendUpdatePackagesToMachine";
		
		logger.info(FUNCTION_NAME + "start");
		if (StringUtils.isBlank(macIds)) {
			logger.error(FUNCTION_NAME + "选择的机器ID为空或者不存在!");
			return false;
		}
		
		if (StringUtils.isBlank(packageIds)) {
			logger.error(FUNCTION_NAME + "选择的升级包ID为空或者不存在!");
			return false;
		}
		HashMap[] packagedsArray = getSysUpdateDataByIds(packageIds);
		HashMap[] machinesArray = dao.getMachineInfoByMachineIds(macIds);
		
		try {
			String iniFilePath = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_PATH_UPGRADE");
			logger.info(FUNCTION_NAME + "FTP下载的升级文件目录: " + iniFilePath);
			
			if (StringUtils.isBlank(iniFilePath)) {
				logger.error(FUNCTION_NAME + "FTP的路径为空!");
				return false;
			}
			
			// STEP1: 取得FTP当前目录下所有的ini文件
			FTPFile[] ftpFileArray = FtpUtils.getFiles(iniFilePath);
			
			// STEP2: 根据选择的机器进行遍历,生成对应的 mac.ini文件
			for (HashMap machineMap : machinesArray) {
				
				// STEP2.1: 取得MAC地址
				String mac = (String) machineMap.get("MAC");
				logger.info(FUNCTION_NAME + "用户选择的MAC: " + mac);
				
				// STEP2.2: 如果该端机正在进行升级,不再升级
				if (isUpdateFileExisted(ftpFileArray, mac)) {
					continue;
				}
				
				// STEP2.3: 向FTP上传文件
				uploadIniFileToFTP(packagedsArray, iniFilePath, mac);
				
				// STEP2.4: 重新检查新的文件是否已经上传成功.如果已经上传成功.那么向DB中插入数据
				FTPFile[] newFtpFileArray = FtpUtils.getFiles(iniFilePath);
				if (isUpdateFileExisted(newFtpFileArray, mac)) {
					if (!insertSystemUpdateManagement(mac, packagedsArray, status, date, currentUserId)) {
						return false;
					}
				}else{
					return false;
				}
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
	private void uploadIniFileToFTP(HashMap[] packagedsArray, String iniFilePath, String mac) throws IOException {

		String FUNCTION_NAME = "uploadIniFileToFTP";
		
		//系统配置的路径如   C:/tmp
		String macIniLocalTmpPath = LocalProperties.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_UPGRADE");
		String suffix = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");
		//当前用户的临时文件夹,执行完成后会自动删除 如  1377321668371
		String tmpDir = String.valueOf(System.currentTimeMillis());
		//临时目录 如 C:/tmp/1377321668371
		String tmpeFilePath =FileTools.buildFullFilePath(macIniLocalTmpPath, tmpDir);
		logger.info(FUNCTION_NAME + "升级文件本地临时目录: " + macIniLocalTmpPath);
		logger.info(FUNCTION_NAME + "升级文件后缀: " + suffix);
		
		// STEP1: 生成对应的临时目录,如 C:/tmp/1377321668371
		FileTools.mkdirs(tmpeFilePath);

		// STEP2: 向ini文件中写入数据,输出目录为: 如 C:/tmp/1377321668371
		writePackageInfoToIniFile(packagedsArray, FileTools.buildFullFilePath(tmpeFilePath, mac + suffix));

		// STEP3: 上传文件列表,读取路径如 C:/tmp/1377321668371
		FtpUtils.uploadFile(FileTools.getSubFiles(tmpeFilePath), iniFilePath);
		
		// STEP4: 移除临时文件,如 C:/tmp/1377321668371下面所有文件
		FileTools.removeSubFiles(tmpeFilePath);
		// STEP5: 删除临时文件夹
		FileTools.removeDirByDirName(macIniLocalTmpPath, tmpDir);
	}

	private boolean insertSystemUpdateManagement(String mac, HashMap[] packagedsArray, String status, Date date, int currentUserId) {
		try {
			String iniFilePath = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_PATH_UPGRADE");
			int machineId = new UploadDao().getMachinIdByMac(mac);
			for (HashMap packageMap : packagedsArray) {
				dao.insertSystemUpdateManagement(machineId, Integer.parseInt(packageMap.get("FILE_ID").toString()), status == null ? "1" : status,
						date, currentUserId);
				String packagePath = FileTools.buildFullFilePath(iniFilePath, mac+LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX"));
				dao.updateCommandManagement(machineId, packagePath, "1", 22, date);
				
				//Added by chengxq for update sys Version
				//将升级命令发出两小时后，执行一次环境信息上传接口
				Calendar cal = Calendar.getInstance();//获取系统时间
				cal.setTime( date );
				cal.add( Calendar.HOUR_OF_DAY, 2 );
				dao.updateCommandManagement(machineId, null, "1", 16, cal.getTime());
				//Added by chengxq for update sys Version
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("insertSystemUpdateManagement" + "数据插入失败!" + e);
			return false;
		}
		return true;
	}
	
	private boolean insertSystemUpdateManagement33(String mac, HashMap[] packagedsArray, String status, Date date, int currentUserId) {
		try {
			String iniFilePath = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_PATH_UPGRADE");
			int machineId = new UploadDao().getMachinIdByMac(mac);
			for (HashMap packageMap : packagedsArray) {
				dao.insertSystemUpdateManagement(machineId, Integer.parseInt(packageMap.get("FILE_ID").toString()), status == null ? "1" : status,
						date, currentUserId);
				String packagePath = FileTools.buildFullFilePath(iniFilePath, mac+LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX"));
				dao.updateCommandManagement(machineId, packagePath, "1", 33, date);//22 --> 33
				
				//Added by chengxq for update sys Version
				//将升级命令发出两小时后，执行一次环境信息上传接口
				Calendar cal = Calendar.getInstance();//获取系统时间
				cal.setTime( date );
				cal.add( Calendar.HOUR_OF_DAY, 2 );
				dao.updateCommandManagement(machineId, null, "1", 16, cal.getTime());
				//Added by chengxq for update sys Version
			}
		} catch (Exception e) {
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
	private void writePackageInfoToIniFile(HashMap[] packagedsArray, String path) throws IOException {
		String FUNCTION_NAME = "writePackageInfoToIniFile";
		logger.info(FUNCTION_NAME + " 临时文件目录: " + path);
		FileWriter fw = new FileWriter(new File(path));
		BufferedWriter bw = new BufferedWriter(fw);

		for (HashMap packageMap : packagedsArray) {
			String packagePath = FileTools.buildFullFilePath(packageMap.get("FILE_FTP_URL").toString(), packageMap.get("FILE_NAME").toString());
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
			if (ftpFile.isDirectory()) continue;
			// 如果当前FTP目录中, 文件名以mac地址起始.那么该端机正在进行升级
			if (ftpFile.getName().startsWith(mac)) {
				logger.info("当前选择的MAC地址已经存在,升级文件: " + ftpFile.getName());
				return true;
			}
		}
		return false;
	}

	public boolean saveUpdatePackageInfo(String fileName, String desc, int createUserId,String localTmpPath) {
		String FUNCTION_NAME = "saveUpdatePackageInfo";
		
		try {
			String fileFtpUrl = LocalProperties.getProperty("FTP_FILE_PATH_UPGRADEPACKAGE");
			
			//1.先做文件上传	
			FtpUtils.uploadFile(FileTools.buildFullFilePath(localTmpPath, fileName), fileFtpUrl);
			//2.取得文件列表
			FTPFile[] newFtpFileArray = FtpUtils.getFiles(fileFtpUrl);
			//3.判断文件是否存在
			if (isUpdateFileExisted(newFtpFileArray, fileName)) {
				//3.1如果存在才向DB中插入数据
				if (dao.insertSystemUpdateData(fileName, fileFtpUrl, "1", createUserId, desc)==0) {
					return false;
				}
			}else{
				return false;
			}
			
		} catch (LocalPropertiesException e) {
			logger.error("取得FTP配置路径错误!"+e);
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			logger.error("向DB中插入数据错误!"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean saveUpdatePackageInfo(String fileName, String desc, int createUserId,String localTmpPath,int dev_id) {
		String FUNCTION_NAME = "saveUpdatePackageInfo";
		
		try {
			String fileFtpUrl = LocalProperties.getProperty("FTP_FILE_PATH_UPGRADEPACKAGE");
//			this.dao.insertSystemUpdateData(fileName, fileFtpUrl, "1", createUserId, desc,dev_id);
//			FtpUtils.uploadFile(FileTools.buildFullFilePath(localTmpPath, fileName), fileFtpUrl);
			//1.先做文件上传	
			FtpUtils.uploadFile(FileTools.buildFullFilePath(localTmpPath, fileName), fileFtpUrl);
			//2.取得文件列表
			FTPFile[] newFtpFileArray = FtpUtils.getFiles(fileFtpUrl);
			//3.判断文件是否存在
			if (isUpdateFileExisted(newFtpFileArray, fileName)) {
				//3.1如果存在才向DB中插入数据
				if (dao.insertSystemUpdateData(fileName, fileFtpUrl, "1", createUserId, desc,dev_id)==0) {
					return false;
				}
			}else{
				return false;
			}
		} catch (LocalPropertiesException e) {
			logger.error("取得FTP配置路径错误!"+e);
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			logger.error("向DB中插入数据错误!"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean saveNewUpdatePackageInfo(String fileName, String desc, int createUserId,String localTmpPath,int new_dev_id) {
		String FUNCTION_NAME = "saveNewUpdatePackageInfo";
		
		try {
			String fileFtpUrl = LocalProperties.getProperty("FTP_FILE_PATH_UPGRADEPACKAGE");
//			this.dao.insertSystemUpdateData(fileName, fileFtpUrl, "1", createUserId, desc,dev_id);
//			FtpUtils.uploadFile(FileTools.buildFullFilePath(localTmpPath, fileName), fileFtpUrl);
			//1.先做文件上传	
			FtpUtils.uploadFile(FileTools.buildFullFilePath(localTmpPath, fileName), fileFtpUrl);
			//2.取得文件列表
			FTPFile[] newFtpFileArray = FtpUtils.getFiles(fileFtpUrl);
			//3.判断文件是否存在
			if (isUpdateFileExisted(newFtpFileArray, fileName)) {
				//3.1如果存在才向DB中插入数据
				if (dao.insertNewSystemUpdateData(fileName, fileFtpUrl, "1", createUserId, desc,new_dev_id)==0) {
					return false;
				}
			}else{
				return false;
			}
		} catch (LocalPropertiesException e) {
			logger.error("取得FTP配置路径错误!"+e);
			e.printStackTrace();
			return false;
		} catch (SQLException e) {
			logger.error("向DB中插入数据错误!"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	/**
	 * 
	 */
	public Object [] getIosFile() {
		String FUNCTION_NAME = "getIosFile";
		
		try {
			String fileFtpUrl = LocalProperties.getProperty("FTP_IOS_FILE_PATH");
			String fileSuffix = LocalProperties.getProperty("FTP_IOS_FILE_SUFFIX");
			
			//1.先做文件上传	
			FTPFile[] fileArray = FtpUtils.getFiles(fileFtpUrl);
			if (fileArray == null) {
				return null;
			}
			List<String> list = new ArrayList<String> ();
			for (FTPFile ftpFile : fileArray) {
				if (ftpFile.getName().endsWith(fileSuffix)) {
					list.add(ftpFile.getName());
				}
			}
			return list.toArray();
			
		} catch (LocalPropertiesException e) {
			logger.error("取得FTP配置路径错误!"+e);
			e.printStackTrace();
			return null;
		} 
	}	
}
