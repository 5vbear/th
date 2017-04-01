package th.action;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.net.ftp.FTPFile;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define;
import th.dao.AppManagementDao;
import th.dao.UploadDao;
import th.util.FileTools;
import th.util.StringUtils;
import th.util.ftp.FtpUtils;

public class AppManagementAction extends BaseAction {

	protected AppManagementDao	dao	= new AppManagementDao();
	private static final String					REQ_PARAM_IDS				= "updatePackageIds";

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
	public int getUpdateDataCount() throws SQLException {
		return dao.getUpdateDataCount();
	}
	public int getUpdateDataCount(	String appName,String versionType,String osType) throws SQLException {
		return dao.getUpdateDataCount( appName, versionType, osType);
	}

	public HashMap[] getUpdateData(final int firstResult, final int maxResults) throws SQLException {
		return dao.getUpdateData(firstResult, maxResults, null);
	}

	public HashMap[] getUpdateData( final int firstResult, final int maxResults, String appName, String versionType,
			String osType ) throws SQLException {
		return dao.getUpdateData( firstResult, maxResults, null, appName, versionType, osType );
	}
	public HashMap[] getAllUpdateData() throws SQLException {
		return dao.getUpdateData(0, -1, null);
	}

	public HashMap[] getUpdateDataByIds(String ids) throws SQLException {
		return dao.getUpdateData(0, -1, ids);
	}
	
	public boolean deleteApp(String ids)  {//TODO::该方法还没有实现。
		String FUNCTION_NAME = "deleteApp ";
		try {
			dao.deleteApp( ids );
		}
		catch ( SQLException e ) {
			logger.info(FUNCTION_NAME + "error",e);
			return false;
		}
		logger.info(FUNCTION_NAME + "end");
		return true;
	}
	public boolean editApp(String ids,int status)  {//TODO::该方法还没有实现。
		String FUNCTION_NAME = "deleteApp ";
		try {
			dao.updateApp( ids ,status);
		}
		catch ( SQLException e ) {
			logger.info(FUNCTION_NAME + "error",e);
			return false;
		}
		logger.info(FUNCTION_NAME + "end");
		return true;
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

			// STEP2: 根据选择的机器进行遍历,生成对应的 mac.ini文件
			for (HashMap machineMap : machinesArray) {

				// STEP2.1: 取得MAC地址
				String mac = (String) machineMap.get("MAC");
				logger.info(FUNCTION_NAME + "用户选择的MAC: " + mac);

				// STEP2.2: 如果该端机正在进行升级,不再升级
				if (isUpdateFileExisted(ftpFileArray, mac)) {
					continue;
				}

				// STEP2.3: 向DB中插入数据
				if (!insertSystemUpdateManagement(mac, packagedsArray, status, date, currentUserId)) {
					return false;
				}

				// STEP2.4: 向FTP上传文件
				uploadIniFileToFTP(packagedsArray, iniFilePath, mac);
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
			//app_name,description,dl_url,icon_url,operatetime,operator
			FtpUtils.uploadFile(FileTools.buildFullFilePath(localTmpPath, fileName), fileFtpUrl);
			this.dao.insertApp("test", "test","1", "1",  desc,createUserId,0,"alpha","","");
			
			
		} catch (LocalPropertiesException e) {
			logger.error("取得FTP配置路径错误!"+e);
			e.printStackTrace();
			return false;
		}
		catch (SQLException e) {
			logger.error("向DB中插入数据错误!"+e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param req
	 */
	public boolean insertApp( HttpServletRequest req ,int userId) {
		String FUNCTION_NAME = "insertApp";
		// TODO Auto-generated method stub
		String  appName = req.getParameter("appName");
		String  description = req.getParameter("description");
		
		
		String  version = req.getParameter("version");
		
		String  dlUrl = req.getParameter("dlUrl");
		String  iconUrl = req.getParameter("iconUrl");
		String  dev_ids = req.getParameter(REQ_PARAM_IDS);
		String  versionType = req.getParameter("versionType");
		//
		String packageName=(String )req.getAttribute("packageName");
		String versionCode=(String )req.getAttribute("versionCode");
		String versionTemp = (String )req.getAttribute("version");
		
		if (packageName == null) {
			packageName="";
		}
		if (versionCode == null) {
			versionCode="";
		}
		if (versionTemp == null) {
			versionTemp="";
		}
		
		
		
	    if(null!=versionTemp&&(!"".equals(versionTemp))){
	    	version=versionTemp;
		}
		
		Object dlurltemp=req.getAttribute("dlUrl");
		
		if(null!=dlurltemp&&(!"".equals(dlurltemp))){
			dlUrl=(String )req.getAttribute("dlUrl");
		}
		
		Object iconUrltemp=req.getAttribute("iconUrl");
		
		if(null!=iconUrltemp&&(!"".equals(iconUrltemp))){
			iconUrl=(String )req.getAttribute("iconUrl");
		}
	
		
		
		
		
		try {
			//List list=getIDList(dev_ids);
			String[] list=dev_ids.split(",");
			for(int i=0;i<list.length;i++){
				dao.insertApp(appName,description,version,dlUrl,iconUrl,userId,Integer.parseInt(list[i]),versionType,packageName,versionCode);
			}
		}
		catch ( SQLException e ) {
			logger.info(FUNCTION_NAME + "error",e);
			return false;
		}
		return true;
	}
	/**
	 * @param req
	 */
	public boolean updateApp( HttpServletRequest req ,int userId) {
		String FUNCTION_NAME = "updateApp";
		// TODO Auto-generated method stub
		String  appID = req.getParameter("appID");
		String  appName = req.getParameter("appName");
		String  description = req.getParameter("description");
		String  version = req.getParameter("version");
		
		String  dlUrl = req.getParameter("dlUrl");
		String  iconUrl = req.getParameter("iconUrl");
		String  dev_ids = req.getParameter(REQ_PARAM_IDS);
		String  versionType = req.getParameter("versionType");
		
		String packageName=(String )req.getAttribute("packageName");
		String versionCode=(String )req.getAttribute("versionCode");
		String versionTemp = (String )req.getAttribute("version");
		
		if (packageName == null) {
			packageName="";
		}
		if (versionCode == null) {
			versionCode="";
		}
		if (versionTemp == null) {
			versionTemp="";
		}
		
		
	    if(null!=versionTemp&&(!"".equals(versionTemp))){
	    	version=versionTemp;
		}
		
		Object dlurltemp=req.getAttribute("dlUrl");
		if(null!=dlurltemp&&(!"".equals(dlurltemp))){
			dlUrl=(String )req.getAttribute("dlUrl");
			
		}
		Object iconUrltemp=req.getAttribute("iconUrl");
		if(null!=iconUrltemp&&(!"".equals(iconUrltemp))){
			iconUrl=(String )req.getAttribute("iconUrl");
			
		}
//		request.setAttribute("dlUrl",Define.HOST_SERVER+fileFtpUrl+"/"+fileMap.get("upload_zip"));
//		request.setAttribute("iconUrl", Define.HOST_SERVER+fileFtpUrl+"/"+fileMap.get("upload_zip2"));
		try {
			//List list=getIDList(dev_ids);
			String[] list=dev_ids.split(",");
			for(int i=0;i<list.length;i++){
			dao.updateApp(appID,appName,description,version,dlUrl,iconUrl,userId,Integer.parseInt(list[i]),versionType,packageName,versionCode);
			}
		}
		catch ( SQLException e ) {
			logger.info(FUNCTION_NAME + "error",e);
			return false;
		}
		return true;
	}
	public HashMap[] getAllOSTypes() throws SQLException {
		return dao.getAllOSTypes();
	}
	
	public List getIDList(String ids){
		List list=new ArrayList();
		int index=ids.indexOf(',');
		while(index>0){
			list.add(Integer.parseInt(ids.substring(0,index)));
		}
		return list;
	}
}
