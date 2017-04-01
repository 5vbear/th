package th.servlet.appStore;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import th.action.AppManagementAction;
import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define;
import th.servlet.BaseServlet;
import th.taglib.Pager;
import th.taglib.PagerHelper;
import th.util.FileTools;

/**
 * Descriptions
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 */
public class UpdateAppStoreServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long					serialVersionUID			= 1L;

	private static final String					REQ_PARAM_ACTION			= "action";
	private static final String					REQ_PARAM_IDS				= "updatePackageIds";
	private static final String					REQ_PARAM_VALUE_LIST		= "List";
	private static final String					REQ_PARAM_VALUE_ADD			= "Add";
	private static final String					REQ_PARAM_VALUE_GO_TO_ADD	= "goToAdd";
	private static final String					REQ_PARAM_VALUE_EDIT		= "Edit";
	private static final String					REQ_PARAM_VALUE_GO_TO_EDIT	= "goToEdit";
	private static final String					REQ_PARAM_VALUE_DELETE		= "Delete";
	private static final String					REQ_PARAM_VALUE_PAGEACTION	= "PageAction";
	private Pager								pager						= null;
	private static final Map<String, String>	PAGE_FORWARD				= new HashMap<String, String>();
	static {
		PAGE_FORWARD.put(REQ_PARAM_VALUE_LIST, "/jsp/appStore/updateAppList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_DELETE, "/jsp/appStore/updateAppList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_PAGEACTION, "/jsp/appStore/updateAppList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_GO_TO_ADD, "/jsp/appStore/updateAppList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_ADD, "/jsp/appStore/updateAppList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_GO_TO_EDIT, "/jsp/appStore/updateAppList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_EDIT, "/jsp/appStore/updateAppList.jsp");
	}

	private AppManagementAction				action						= new AppManagementAction();

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
		String FUNCTION_NAME = "doIt() ";
		String paramAction = req.getParameter(REQ_PARAM_ACTION);
		ServletContext sc = getServletContext();

		if (REQ_PARAM_VALUE_LIST.equals(paramAction)) {
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);

		} else if (REQ_PARAM_VALUE_GO_TO_ADD.equals(paramAction)) {
			String message = getEditMessage(req);
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
			req.setAttribute("message", message);
			
			
		} else if (REQ_PARAM_VALUE_GO_TO_EDIT.equals(paramAction)) {
			String message = getEditMessage(req);
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
			req.setAttribute("message", message);
		} else if (REQ_PARAM_VALUE_DELETE.equals(paramAction)) {
			String message = getDeleteMessage(req);
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
			req.setAttribute("message", message);
			
		} else if (REQ_PARAM_VALUE_ADD.equals(paramAction)) {
			req.setAttribute("message", uploadFile(req));
			// this.writeMessage(res, uploadFile(req).toString());
		} else if (REQ_PARAM_VALUE_EDIT.equals(paramAction)) {
			this.writeMessage(res, "");
		} else if (REQ_PARAM_VALUE_PAGEACTION.equals(paramAction)) {
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
		}
		sc.getRequestDispatcher(PAGE_FORWARD.get(paramAction)).forward(req, res);
		logger.info(FUNCTION_NAME + " page forward is: " + PAGE_FORWARD.get(action));
		logger.info(FUNCTION_NAME + " end ");
		return null;
	}

	private String getDeleteMessage(HttpServletRequest req) throws SQLException {
		String message = "";
		
		if(action.deleteApp(req.getParameter(REQ_PARAM_IDS))) {
			
				message="删除成功!";}
		else{
			message="删除数据不存在!";
		}
		
		return message;
	}
	private String getEditMessage(HttpServletRequest req) throws SQLException {
		String message = "";
		
		if(action.editApp(req.getParameter(REQ_PARAM_IDS),1)) {
			
				message="上架成功!";}
		else{
			message="上架应用不存在!";
		}
		
		return message;
	}

//	protected Boolean uploadFile(HttpServletRequest request) {
//		Boolean returnFlag = false;
//		try {
//			String uploadTmp =Define.FTP_SERVER_HOST+"/jboss-4.0.5.GA/server/default/deploy/jbossweb-tomcat55.sar/ROOT.war/";
//			//LocalProperties.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_UPGRADE");
//			//当前用户的临时文件夹,执行完成后会自动删除 如  1377321668371
//			String tmpDir = String.valueOf(System.currentTimeMillis());
//			//临时目录 如 C:/tmp/1377321668371
//			//String tmpeFilePath =FileTools.buildFullFilePath(uploadTmp, tmpDir);
//			//FileTools.mkdirs(tmpeFilePath);
//			//Map<String, String> fileMap = uploadFile(tmpeFilePath, request);
//			Map<String, String> fileMap = uploadFile(uploadTmp, request);
//			if (fileMap == null) return returnFlag;
//			action.saveUpdatePackageInfo(fileMap.get("upload_zip"), 
//					request.getParameter("file_desc"), getCurrentUserId(request),"");
//			//移除临时文件,如 C:/tmp/1377321668371下面所有文件
//			//FileTools.removeSubFiles(tmpeFilePath);
//			//删除临时文件夹
//			//FileTools.removeDirByDirName(uploadTmp, tmpDir);
//			return true;
//
//		} catch (Exception e) {
//			logger.error("上传文件失败!" + e);
//			return returnFlag;
//		}
//	}
//	
	protected Boolean uploadFile(HttpServletRequest request) {
		Boolean returnFlag = false;
		try {
			String uploadTmp = LocalProperties.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_UPGRADE");
			//当前用户的临时文件夹,执行完成后会自动删除 如  1377321668371
			String tmpDir = String.valueOf(System.currentTimeMillis());
			//临时目录 如 C:/tmp/1377321668371
			String tmpeFilePath =FileTools.buildFullFilePath(uploadTmp, tmpDir);
			FileTools.mkdirs(tmpeFilePath);
			Map<String, String> fileMap = uploadFile(tmpeFilePath, request);
			if (fileMap == null) return returnFlag;
			action.saveUpdatePackageInfo(fileMap.get("upload_zip"), 
					request.getParameter("file_desc"), getCurrentUserId(request),tmpeFilePath);
			//移除临时文件,如 C:/tmp/1377321668371下面所有文件
			FileTools.removeSubFiles(tmpeFilePath);
			//删除临时文件夹
			FileTools.removeDirByDirName(uploadTmp, tmpDir);
			return true;

		} catch (Exception e) {
			logger.error("上传文件失败!" + e);
			return returnFlag;
		}
	}
	protected Map<String, String> uploadFile(String filePath, HttpServletRequest request) throws Exception {

		Map<String, String> pathMap = new HashMap<String, String>();
		Iterator<FileItem> i = getFileItems(request).iterator();
		SimpleDateFormat dateformat=new SimpleDateFormat("yyyyMMddHHmmssS");
		while (i.hasNext()) {
			FileItem fi = (FileItem) i.next();
			if (!fi.isFormField()) {
				String fieldName = fi.getFieldName();
				String fileName = fi.getName();
				String uploadFileName = dateformat.format(new Date()) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
				String fullFileName = FileTools.buildFullFilePath(filePath, uploadFileName);
				File file = new File(fullFileName);
				fi.write(file);
				pathMap.put(fieldName, uploadFileName);
			}
		}
		return pathMap;
	}
}
