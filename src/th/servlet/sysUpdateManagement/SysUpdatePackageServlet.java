package th.servlet.sysUpdateManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.UpdateManagementAction;
import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define;
import th.servlet.BaseServlet;
import th.taglib.Pager;
import th.taglib.PagerHelper;
import th.util.FileTools;
import th.util.ftp.FtpUtils;

/**
 * Descriptions
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 */
public class SysUpdatePackageServlet extends BaseServlet {

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
		PAGE_FORWARD.put(REQ_PARAM_VALUE_LIST, "/jsp/sysUpdateManagement/sysUpdatePackageList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_DELETE, "/jsp/sysUpdateManagement/sysUpdatePackageList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_PAGEACTION, "/jsp/sysUpdateManagement/sysUpdatePackageList.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_GO_TO_ADD, "/jsp/sysUpdateManagement/sysUpdatePackageForm.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_ADD, "/jsp/sysUpdateManagement/sysUpdatePackageForm.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_GO_TO_EDIT, "/jsp/sysUpdateManagement/sysUpdatePackageForm.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_EDIT, "/jsp/sysUpdateManagement/sysUpdatePackageForm.jsp");
	}

	private UpdateManagementAction				action						= new UpdateManagementAction();

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
		String FUNCTION_NAME = "doIt() ";
		String paramAction = req.getParameter(REQ_PARAM_ACTION);
		ServletContext sc = getServletContext();

		if (REQ_PARAM_VALUE_LIST.equals(paramAction)) {
			pager = PagerHelper.getPager(action.getSysUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getSysUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);

		} else if (REQ_PARAM_VALUE_GO_TO_ADD.equals(paramAction)) {
			req.setAttribute( "osTypes", action.getAllOSInfo());
		} else if (REQ_PARAM_VALUE_GO_TO_EDIT.equals(paramAction)) {
			req.setAttribute( "osTypes", action.getAllOSInfo());
		} else if (REQ_PARAM_VALUE_DELETE.equals(paramAction)) {
			String message = getDeleteMessage(req);
			pager = PagerHelper.getPager(action.getSysUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getSysUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
			req.setAttribute("message", message);
			
		} else if (REQ_PARAM_VALUE_ADD.equals(paramAction)) {
			req.setAttribute("message", uploadFile(req));
			// this.writeMessage(res, uploadFile(req).toString());
			req.setAttribute( "osTypes", action.getAllOSInfo());
		} else if (REQ_PARAM_VALUE_EDIT.equals(paramAction)) {
			this.writeMessage(res, "");
		} else if (REQ_PARAM_VALUE_PAGEACTION.equals(paramAction)) {
			pager = PagerHelper.getPager(action.getSysUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getSysUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
		}
		sc.getRequestDispatcher(PAGE_FORWARD.get(paramAction)).forward(req, res);
		logger.info(FUNCTION_NAME + " page forward is: " + PAGE_FORWARD.get(action));
		logger.info(FUNCTION_NAME + " end ");
		return null;
	}

	private String getDeleteMessage(HttpServletRequest req) throws SQLException {
		String message = "";
		switch (action.deleteSysUpdateData(req.getParameter(REQ_PARAM_IDS))) {
			case 1:
				message="有正在升级未完成的升级包在删除数据中!";
				break;
			case 2:
				message="删除数据不存在!";
				break;
			case 3:
				message="取得FTP配置路径错误!";
				break;
			case 9:
				message="删除成功!";
				break;
			default:
				break;
		}
		return message;
	}

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
			String devId = request.getParameter("devId");
			boolean uploadFlag = false;
			if(devId==null||"".equalsIgnoreCase(devId))
				uploadFlag = action.saveUpdatePackageInfo(fileMap.get("upload_zip"), //jsp filename
						request.getParameter("file_desc"), getCurrentUserId(request),tmpeFilePath);
			else
				uploadFlag = action.saveUpdatePackageInfo(fileMap.get("upload_zip"), //jsp filename
						request.getParameter("file_desc"), getCurrentUserId(request),tmpeFilePath,Integer.parseInt(devId));
			//移除临时文件,如 C:/tmp/1377321668371下面所有文件
			FileTools.removeSubFiles(tmpeFilePath);
			//删除临时文件夹
			FileTools.removeDirByDirName(uploadTmp, tmpDir);
			return uploadFlag;

		} catch (Exception e) {
			logger.error("上传文件失败!" + e);
			return returnFlag;
		}
	}
}
