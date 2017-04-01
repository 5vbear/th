package th.servlet.appStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.AppManagementAction;
import th.com.property.LocalProperties;
import th.com.util.Define;
import th.dao.EBankDeviceDAO;
import th.servlet.BaseServlet;
import th.taglib.Pager;
import th.taglib.PagerHelper;
import th.util.FileTools;
import th.util.StringUtils;
import th.util.apk.ApkInfo;
import th.util.apk.ApkUtil;
import th.util.ftp.FtpUtils;

/**
 * Descriptions
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 */
public class AppManagementServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long					serialVersionUID			= 1L;

	private static final String					REQ_PARAM_ACTION			= "action";
	private static final String					REQ_PARAM_IDS				= "appManagementIds";
	private static final String					REQ_PARAM_VALUE_LIST		= "List";
	private static final String					REQ_PARAM_VALUE_ADD			= "Add";
	private static final String					REQ_PARAM_VALUE_GO_TO_ADD	= "goToAdd";
	private static final String					REQ_PARAM_VALUE_EDIT		= "Edit";
	private static final String					REQ_PARAM_VALUE_GO_TO_EDIT	= "goToEdit";
	private static final String					REQ_PARAM_VALUE_DELETE		= "Delete";
	private static final String					REQ_PARAM_VALUE_PAGEACTION	= "PageAction";
	private Pager								pager						= null;
	private static final Map<String, String>	PAGE_FORWARD				= new HashMap<String, String>();
	private static String className;
	static {
		PAGE_FORWARD.put(REQ_PARAM_VALUE_LIST, "/jsp/appStore/appManagementList.jsp");
//		PAGE_FORWARD.put(REQ_PARAM_VALUE_LIST, "/jsp/appStore/updatePackageList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_DELETE, "/jsp/appStore/appManagementList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_PAGEACTION, "/jsp/appStore/appManagementList.jsp");
		
		PAGE_FORWARD.put(REQ_PARAM_VALUE_ADD, "/jsp/appStore/appManagementList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_GO_TO_EDIT, "/jsp/appStore/appUpdate.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_EDIT, "/jsp/appStore/appManagementList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_GO_TO_ADD, "/jsp/appStore/appCreate.jsp");
	}

	private AppManagementAction				action						= new AppManagementAction();
	private EBankDeviceDAO				dao						= new EBankDeviceDAO();
	
	
	public void init( ServletConfig config ) throws ServletException {
		super.init( config );
		className = "AppManagementServlet";
	}

	public void destroy() {
		super.destroy();
	}

	public void doPost( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
		doIt( req, res );
	}

	public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
		doIt( req, res );
	}
	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) {
		String FUNCTION_NAME = "doIt() ";
		logger.info(FUNCTION_NAME + " begin time is "+new Date());
		String paramAction = req.getParameter(REQ_PARAM_ACTION);
		ServletContext sc = getServletContext();
		if (REQ_PARAM_VALUE_LIST.equals(paramAction)) {
			logger.info(FUNCTION_NAME + "in REQ_PARAM_VALUE_LIST");
			try{
				String versionType=req.getParameter("versionType");
				String osType=req.getParameter("osType");
				String appName=req.getParameter("appName");
				pager = PagerHelper.getPager(action.getUpdateDataCount(appName,versionType,osType), Define.VIEW_NUM, req);
				pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM,appName,versionType,osType));
			
			
			}catch(Exception e){
				//此处异常需要处理一下，通知页面。
				logger.info(FUNCTION_NAME + "REQ_PARAM_VALUE_LIST error",e);
			}
				req.setAttribute("pager", pager);
		} else if (REQ_PARAM_VALUE_GO_TO_ADD.equals(paramAction)) {

			try{
				pager = PagerHelper.getPager(10, Define.VIEW_NUM, req);
			pager.setResultData(dao.getAllEBankDevices());
			}catch(Exception e){
				//此处异常需要处理一下，通知页面。
				logger.info(FUNCTION_NAME + "REQ_PARAM_VALUE_LIST error",e);
			}
				req.setAttribute("pager", pager);
//=======
//			logger.info(FUNCTION_NAME + "in REQ_PARAM_VALUE_GO_TO_ADD");
//			try{
//			//	List devList = action.getUpdateDataCount();
//			}catch(Exception e){
//				//此处异常需要处理一下，通知页面。
//				logger.info(FUNCTION_NAME + "REQ_PARAM_VALUE_GO_TO_ADD error",e);
//			}
//				//req.setAttribute("devList", devList);
//		
//>>>>>>> .r1028
		} else if (REQ_PARAM_VALUE_GO_TO_EDIT.equals(paramAction)) {
			String message="";
			String updateId= req.getParameter(REQ_PARAM_IDS);
			logger.info("updateId: " + updateId);
			try {
				HashMap[] result = action.getUpdateDataByIds( updateId );
				if(result==null||result.length<=0){
					logger.info(FUNCTION_NAME + "更新应用信息前未查询到响应的应用信息，无法进行更新。");
					message="更新应用信息出错，请刷新页面后重新操作。";
				}else{
					
					//此时表示没有查询结果，这样是有问题的。有可能是由于多人同时操作引起
					pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
					pager.setResultData(result);
					req.setAttribute("allDevices",dao.getAllEBankDevices());
				}
			}
			catch ( SQLException e ) {
				//此处异常需要处理一下，通知页面。
				logger.info(FUNCTION_NAME + "REQ_PARAM_VALUE_GO_TO_EDIT error",e);
			}
			req.setAttribute("pager", pager);
//			req.setAttribute("message", message);
			
		} else if (REQ_PARAM_VALUE_DELETE.equals(paramAction)) {
			String message="";
			
				message = deleteApp(req);
			try {
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			}
			catch ( SQLException e ) {
				//此处异常需要处理一下，通知页面。
				logger.info(FUNCTION_NAME + "REQ_PARAM_VALUE_DELETE error",e);
			}
			req.setAttribute("pager", pager);
			req.setAttribute("message", message);
			
		} else if (REQ_PARAM_VALUE_ADD.equals(paramAction)) {
			int userId = getCurrentUserId(req);
			 uploadFile(req,paramAction);
//			boolean insertResult = action.insertApp(req,userId);
//			if(!insertResult){
//				req.setAttribute("message", "添加应用程序失败，请联系管理员。");
//			}
			try {
				
				pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
				pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
				
			}
			catch ( SQLException e ) {
				//此处异常需要处理一下，通知页面。
				logger.info(FUNCTION_NAME + "REQ_PARAM_VALUE_ADD error",e);
			}
			req.setAttribute("pager", pager);
			//req.setAttribute("message", uploadFile(req));
			// this.writeMessage(res, uploadFile(req).toString());
		}
		else if (REQ_PARAM_VALUE_EDIT.equals(paramAction)) {
			int userId = getCurrentUserId(req);
			uploadFile(req,paramAction);
			try {
				
				pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
				pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
				
			}
			catch ( SQLException e ) {
				//此处异常需要处理一下，通知页面。
				logger.info(FUNCTION_NAME + "REQ_PARAM_VALUE_EDIT error",e);
			}
			req.setAttribute("pager", pager);
		}
		else if (REQ_PARAM_VALUE_PAGEACTION.equals(paramAction)) {
			try {
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			}
			catch ( SQLException e ) {
				//此处异常需要处理一下，通知页面。
				logger.info(FUNCTION_NAME + "REQ_PARAM_VALUE_PAGEACTION error",e);
			}
			req.setAttribute("pager", pager);
		}
		try {
			sc.getRequestDispatcher(PAGE_FORWARD.get(paramAction)).forward(req, res);
		}
		catch ( ServletException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch ( IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info(FUNCTION_NAME + PAGE_FORWARD);
		logger.info(FUNCTION_NAME + action);
		
		logger.info(FUNCTION_NAME + " page forward is: " + PAGE_FORWARD.get(paramAction));
		logger.info(FUNCTION_NAME + " end ");
		logger.info(FUNCTION_NAME + " end time is "+new Date());
		return null;
	}

	private String deleteApp(HttpServletRequest req){
		String message = "";
		logger.info("ids: " + req.getParameter(REQ_PARAM_IDS));
		
		boolean result = action.deleteApp(req.getParameter(REQ_PARAM_IDS));
		if(!result){
			message="删除应用程序信息时发生异常，删除失败。";
		}
		return message;
	}

	protected Boolean uploadFile(HttpServletRequest request,String paramAction) {
		Boolean returnFlag = false;
		try {
			String uploadTmp = LocalProperties.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_UPGRADE");
			//当前用户的临时文件夹,执行完成后会自动删除 如  1377321668371
			String tmpDir = String.valueOf(System.currentTimeMillis());
			//临时目录 如 C:/tmp/1377321668371
			String tmpeFilePath =FileTools.buildFullFilePath(uploadTmp, tmpDir);
			FileTools.mkdirs(tmpeFilePath);
			Map<String, String> fileMap = uploadFile(tmpeFilePath, request);
			int userId = getCurrentUserId(request);
			if (fileMap != null&&fileMap.size()>0) {
//			action.saveUpdatePackageInfo(fileMap.get("upload_zip"), 
//					request.getParameter("file_desc"), getCurrentUserId(request),tmpeFilePath);
			//移除临时文件,如 C:/tmp/1377321668371下面所有文件
			String fileFtpUrl = LocalProperties.getProperty("FTP_FILE_PATH_UPGRADEPACKAGE");
			//app_name,description,dl_url,icon_url,operatetime,operator
			
			//此处增加了path变量，除了用于上传之外，还要用于apk文件的解析
			String path=FileTools.buildFullFilePath(tmpeFilePath, fileMap.get("upload_zip"));
			if(path!=null&&path.indexOf( ".apk" )==path.length()-4){ //此时是apk文件
				//
				ApkUtil apkUtil = new ApkUtil();
				ApkInfo apkInfo = apkUtil.getApkInfo(path);
				if(apkInfo!=null){
					String packageName=apkInfo.getPackageName();
					if(!StringUtils.isBlank( packageName )){
						request.setAttribute("packageName",packageName.replaceAll( "'", "" ));
					}
					String version = apkInfo.getVersion();
					if(!StringUtils.isBlank( version )){
						request.setAttribute("version",version.replaceAll( "'", "" ));
					}
					String versionCode = apkInfo.getVersionCode();
					if(!StringUtils.isBlank( versionCode )){
						request.setAttribute("versionCode",versionCode.replaceAll( "'", "" ));
					}
					
				}
			}
			
			logger.info("上传文件开始!"+path);
			FtpUtils.uploadFile(path, fileFtpUrl);
			FtpUtils.uploadFile(FileTools.buildFullFilePath(tmpeFilePath, fileMap.get("upload_zip2")), fileFtpUrl);
			//this.dao.insertApp("test", "test", "1",  desc,createUserId,0);
			logger.info("上传文件结束!");
			
//			String  dlUrl = req.getParameter("dlUrl");
//			String  iconUrl = req.getParameter("iconUrl");
			if(null!=fileMap.get("upload_zip")&&fileMap.get("upload_zip").length()>1){
			request.setAttribute("dlUrl",Define.HOST_SERVER+"/thccb/"+fileFtpUrl+"/"+fileMap.get("upload_zip"));}
			if(null!=fileMap.get("upload_zip2")&&fileMap.get("upload_zip2").length()>1){
			request.setAttribute("iconUrl", Define.HOST_SERVER+"/thccb/"+fileFtpUrl+"/"+fileMap.get("upload_zip2"));}
			}
			boolean dealResult ; 
			if(paramAction.equals(REQ_PARAM_VALUE_ADD)){
				dealResult=action.insertApp(request,userId);
				if(!dealResult){
					request.setAttribute("message", "添加应用程序失败，请联系管理员。");
				}
			}else if(paramAction.equals(REQ_PARAM_VALUE_EDIT)){
				dealResult=action.updateApp(request,userId);
				if(!dealResult){
					request.setAttribute("message", "更新应用程序失败，请联系管理员。");
				}
			}
//			boolean insertResult = action.insertApp(request,userId);
			
			FileTools.removeSubFiles(tmpeFilePath);
			//删除临时文件夹
			FileTools.removeDirByDirName(uploadTmp, tmpDir);
			return true;

		} catch (Exception e) {
			logger.error("上传文件失败!" + e);
			return returnFlag;
		}
	}
}
