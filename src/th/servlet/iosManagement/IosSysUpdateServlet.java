package th.servlet.iosManagement;

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

import th.action.UpdateManagementAction;
import th.com.property.LocalProperties;
import th.com.util.Define;
import th.servlet.BaseServlet;
import th.taglib.PagerHelper;
import th.util.FileTools;
import th.util.HttpUtils;
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
public class IosSysUpdateServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long					serialVersionUID						= 1L;

	private static final String					PAGE_FORWARD					= "/jsp/iosSystemManagement/iosSysUpdate.jsp";


	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
		String FUNCTION_NAME = "doIt() ";
		ServletContext sc = getServletContext();
		String param = req.getParameter( "param" );
		if(!StringUtils.isBlank( param )){
			Boolean uploadResult = uploadFile(req);
			if(uploadResult){
				req.setAttribute( "result", "true" );
			}else{
				req.setAttribute( "result", "false" );
			}
		}

		
		sc.getRequestDispatcher(PAGE_FORWARD).forward(req, res);
		logger.info(FUNCTION_NAME + " page forward is: " + PAGE_FORWARD);

		logger.info(FUNCTION_NAME + " end ");
		return null;
	}
	
	//此处需要保留原有的文件名
	protected Map<String, String> uploadFile(String filePath, HttpServletRequest request) throws Exception {

		Map<String, String> pathMap = new HashMap<String, String>();
		Iterator<FileItem> i = getFileItems(request).iterator();
//		SimpleDateFormat dateformat=new SimpleDateFormat("yyyyMMddHHmmssS");
		while (i.hasNext()) {
			FileItem fi = (FileItem) i.next();
			if (!fi.isFormField()) {
				String fieldName = fi.getFieldName();
				String fileName = fi.getName();
				if(StringUtils.isBlank( fileName )){
					continue;
				}
//				String uploadFileName = dateformat.format(new Date()) + fileName.substring(fileName.lastIndexOf("."), fileName.length());
				String uploadFileName= LocalProperties.getProperty("FTP_IOS_WEB_PARAM_LIST_NAME");
				
				String fullFileName = FileTools.buildFullFilePath(filePath, uploadFileName);
				File file = new File(fullFileName);
				fi.write(file);
				pathMap.put(fieldName, uploadFileName);
			}
		}
		return pathMap;
	}
	protected Boolean uploadFile(HttpServletRequest request) {
		Boolean returnFlag = false;
		try {
			// C:/tmp/upgrade
			String uploadTmp = LocalProperties.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_UPGRADE");
			//当前用户的临时文件夹,执行完成后会自动删除 如  1377321668371
			String tmpDir = String.valueOf(System.currentTimeMillis());
			//临时目录 如 C:/tmp/upgrade/1377321668371
			String tmpeFilePath =FileTools.buildFullFilePath(uploadTmp, tmpDir);
			
			//创建文件夹
			FileTools.mkdirs(tmpeFilePath);
			Map<String, String> fileMap = uploadFile(tmpeFilePath, request);
			
			if (fileMap != null&&fileMap.size()>0) {
//			action.saveUpdatePackageInfo(fileMap.get("upload_zip"), 
//					request.getParameter("file_desc"), getCurrentUserId(request),tmpeFilePath);
			//移除临时文件,如 C:/tmp/1377321668371下面所有文件
			String fileFtpUrl = LocalProperties.getProperty("FTP_IOS_FILE_PATH");//FTP_FILE_PATH_UPGRADEPACKAGE
			//app_name,description,dl_url,icon_url,operatetime,operator
			
			//此处增加了path变量，除了用于上传之外，还要用于apk文件的解析
			String path=FileTools.buildFullFilePath(tmpeFilePath, fileMap.get("upload_zip"));
			
			logger.info("上传文件开始!"+path);
			
			
			
			
			
			}
			
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
