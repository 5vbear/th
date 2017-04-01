package th.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define;
import th.com.util.Define4Machine;
import th.dao.AdvertDao;
import th.dao.MonitorDAO;
import th.dao.OrgDealDAO;
import th.entity.AdvertBean;
import th.entity.MachineBean;
import th.user.User;
import th.util.DateUtil;
import th.util.FileTools;
import th.util.MultipartRequest;
import th.util.StringUtils;
import th.util.TransferFileUtil;
import th.util.ftp.FtpUtils;

public class AdvertManagementAction extends BaseAction {

	// 媒体素材
	public static String MEDIA_MATERIAL = "1";
	
	// 字幕
	public static String SUBTITLES = "2";
	
	//服务器上传文件名
	String serviceFileName = "media";
	
	//文件上传服务器路径List
	List<String> fileListPath = null;
	
	//文件上传服务器List
	List<File> fileList = null;
	
	
	
	/**
	 * 素材检索
	 * @param req
	 * @throws Exception
	 */
	public void materialSearch(HttpServletRequest req) throws Exception {
		AdvertBean bean = new AdvertBean();
		//素材类型
		String adertType = req.getParameter("advert_type");
		bean.setMaterialType(adertType);
		//素材名称
		bean.setMaterialName(req.getParameter("material_name"));
		//审核状态
		bean.setAuditStatus(req.getParameter("audit_type"));
		//添加时间From
		bean.setMaterialAddDateFrom(req.getParameter("search_date_s"));
		//添加时间To
		bean.setMaterialAddDateTo(req.getParameter("search_date_e"));
		//文件格式
		bean.setMaterialFileType(req.getParameter("file_type"));
		//检索结果当前位置
		bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		//排序方式
		bean.setSortField(req.getParameter("sort_field"));
		//排序类型
		bean.setSortType(req.getParameter("sort_type"));
		
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		
		//媒体素材的场合
		List<AdvertBean> resultBeans = null;
//		if (MEDIA_MATERIAL.equals(adertType)) {
		resultBeans = new ArrayList<AdvertBean>();
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] materialBeans = dao.searchMaterial(bean);
		if (materialBeans != null) {
			//检索结果开始位置
			int start_p  = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			//检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;
			
			int loop = 0;
			for (HashMap<String, String> itemMap : materialBeans) {
				loop++;
				if( loop <= start_p ) {
                    continue;
                }
				if( loop > start_p && loop <= end_p ) {
					start_p++;
					AdvertBean advertbean = new AdvertBean();
					//素材ID
					advertbean.setMaterialId(itemMap.get("MEDIA_ID"));
					//素材类型
					advertbean.setAdvert_type(itemMap.get("ADVERT_TYPE"));
					//素材名称
					advertbean.setMaterialName(itemMap.get("MEDIA_NAME"));
					//素材分组
					advertbean.setMaterial_group(itemMap.get("GROUP_NAME"));
					//素材类型
					advertbean.setMaterialType(itemMap.get("MEDIA_TYPE"));
					//创建用户
					advertbean.setOperator(itemMap.get("NAME"));
					//过期时间
					advertbean.setExpireTime(itemMap.get("EXPIRETIME"));
					//创建时间
					advertbean.setCreateTime(itemMap.get("OPERATETIME"));
					//审核情况
					advertbean.setAuditStatus(itemMap.get("STATUS"));
					resultBeans.add(advertbean);
				}
			}
			//检索总行数
			bean.setTotal_num(materialBeans.length);
			//bean.setPoint_num( start_p );
		}
//		} else if (SUBTITLES.equals(adertType)) {
//			resultBeans = new ArrayList<AdvertBean>();
//			AdvertDao dao = new AdvertDao();
//			HashMap<String, String>[] subtitlesBeans = dao.searchSubtitles(bean);
//			for (HashMap<String, String> itemMap : subtitlesBeans) {
//				AdvertBean advertbean = new AdvertBean();
//				//素材ID
//				advertbean.setMaterialId(itemMap.get("SUBTITLES_ID"));
//				//素材名称
//				advertbean.setMaterialName(itemMap.get("SUBTITLES_NAME"));
//				//素材分组
//				advertbean.setMaterial_group(itemMap.get("GROUP_NAME"));
//				//素材类型
//				advertbean.setMaterialType("字幕");
//				//创建用户
//				advertbean.setOperator(itemMap.get("NAME"));
//				//过期时间
//				advertbean.setExpireTime(itemMap.get("EXPIRETIME"));
//				//创建时间
//				advertbean.setCreateTime(itemMap.get("OPERATETIME"));
//				//审核情况
//				advertbean.setAuditStatus(itemMap.get("STATUS"));
//				resultBeans.add(advertbean);
//			}
//		}
		//检索条件保存
		req.setAttribute("select_object", bean);
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	/**
	 * 取得素材组信息
	 * @param req
	 * @return 添加结果
	 */
	public List<AdvertBean> getGaterialGroupList(HttpServletRequest req) throws Exception {
		AdvertDao dao = new AdvertDao();
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		AdvertBean bean = new AdvertBean();
		bean.setOperator(user.getId());
		HashMap<String, String>[] groupBeans = dao.getGaterialGroupList(bean);
		List<AdvertBean> list = new ArrayList<AdvertBean>();
		for (HashMap<String, String> itemMap : groupBeans) {
			AdvertBean groupBean = new AdvertBean();
			//素材组ID
			groupBean.setMaterial_group(itemMap.get("GROUP_ID"));
			//素材组名称
			groupBean.setGroupName(itemMap.get("GROUP_NAME"));
			list.add(groupBean);
		}
		return list;
	}
	
	/**
	 * 素材添加
	 * @param req
	 * @return 添加结果
	 */
	public String materialAdd(HttpServletRequest req) throws Exception {
		MultipartRequest multipartReq = null;
		String result = "";
		try {
			if ( ServletFileUpload.isMultipartContent( req ) == true ) {
				try {
					multipartReq = new MultipartRequest( req );
				}
				catch ( Exception e ) {
					logger.error(e.getMessage());
					e.printStackTrace();
				}
				
				//uploadFiles = ( (MultipartRequest) req ).getUploadFileItem();
				AdvertBean bean = getAdvertBean(multipartReq);
				//操作者
				User user = (User) req.getSession().getAttribute("user_info");
				bean.setOperator(user.getId());
				AdvertDao dao = new AdvertDao();
				//素材组添加
				if (bean.getCustom_group() != null && !"".equals(bean.getCustom_group())) {
					String groupId = dao.insertMaterialGroup(bean);
					bean.setMaterial_group(groupId);
				}
				//媒体素材的场合
				if (MEDIA_MATERIAL.equals(bean.getAdvert_type())) {
					// 素材文件上传
					uploadMaterialFile(multipartReq, MEDIA_MATERIAL);
					//视频文件生成缩略图
					transVideoToIamge();
					//上传到FTP服务器
					uploadMaterialFileToFtp();
					//媒体文件上传数量
					bean.setUploadFileCount(multipartReq.getUploadFileCount());
					//媒体文件服务器路径
					if (fileListPath != null) {
						bean.setServiceFilePath(fileListPath.toArray(new String[fileListPath.size()]));
					}
					// 媒体素材添加DB插入
					int[] cnt = dao.insertMaterial(bean);
					if (cnt == null) {
						result = "failed";
					}else{
						result = "success";
					}
				} 
				//字幕的场合
				else if (SUBTITLES.equals(bean.getAdvert_type())){
					// 字幕背景图片上传
					uploadMaterialFile(multipartReq, SUBTITLES);
					//上传到FTP服务器
					uploadMaterialFileToFtp();
					//媒体文件服务器路径
					if (fileListPath != null) {
						bean.setServiceFilePath(fileListPath.toArray(new String[fileListPath.size()]));
					}
					int cnt = dao.insertSubtitles(bean);
					if(cnt == 0){
						result = "failed";
					}else{
						result = "success";
					}
				} else {
					result = "failed";
					throw new Exception("素材类型不正确!");
				}
				req.setAttribute("advert_type", bean.getAdvert_type());
			}
		} catch (Exception e ) {
			result = "failed";
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 素材添加Bean取得
	 * @param req
	 * @return
	 */
	private AdvertBean getAdvertBean(MultipartRequest req) throws Exception {
		AdvertBean bean = new AdvertBean();
		//素材分组
		bean.setMaterial_group(req.getParameter("material_group"));
		//自定义组
		bean.setCustom_group(req.getParameter("custom_group"));
		//素材类型
		bean.setAdvert_type(req.getParameter("advert_type"));
		//素材文件路径
		//bean.setMaterial_filePath(new String(req.getParameter("material_filePath").getBytes(fileEncode), encode));
		bean.setMaterial_filePath(req.getParameters("material_filePath"));
		//文件类型
		//bean.setMaterial_type(new String(req.getParameter("material_type").getBytes(fileEncode), encode));
		bean.setMaterial_type(req.getParameters("material_type"));
		//文件名称
		//bean.setMaterial_name(new String(req.getParameter("material_name").getBytes(fileEncode), encode));
		bean.setMaterial_name(req.getParameters("material_name"));
		//素材说明
		//bean.setMaterial_describe(new String(req.getParameter("material_describe").getBytes(fileEncode), encode));
		bean.setMaterial_describe(req.getParameters("material_describe"));
		//素材联接
		//bean.setMaterial_describe(new String(req.getParameter("material_describe").getBytes(fileEncode), encode));
		bean.setMaterial_link(req.getParameters("material_link"));
		//字幕名称
		bean.setSubtitles_name(req.getParameter("subtitles_name"));
		//字幕说明
		bean.setSubtitles_describe(req.getParameter("subtitles_describe"));
		//滚动文字
		bean.setRoll_word(req.getParameter("roll_word"));
		//背景图片路径
		bean.setBackground_filePath(req.getParameter("background_filePath"));
		//背景颜色
		bean.setBackground_color(req.getParameter("background_color"));
		//字体颜色
		bean.setWord_color(req.getParameter("word_color"));
		//字体属性
		bean.setWord_attribute(req.getParameter("word_tilt") + ";" + req.getParameter("word_bold") + 
				";" + req.getParameter("word_size") + ";" + req.getParameter("word_type"));
		//字体高
		bean.setWord_height(req.getParameter("word_height"));
		//字体宽
		bean.setWord_width(req.getParameter("word_width"));
		//字体水平对齐(0:居左 1:居中 2:居右)
		bean.setText_align(req.getParameter("text_align"));
		//字体垂直对齐(0:居左 1:居中 2:居右)
		bean.setVertical_align(req.getParameter("vertical_align"));
		//滚动延迟(微妙)
		bean.setRoll_delay(req.getParameter("roll_delay"));
		
		return bean;
	}
	
	/**
	 * 素材文件上传
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public String uploadMaterialFile( MultipartRequest req, String formName) throws Exception {
		String dir = LocalProperties.getProperty("TH_UPLOAD_FILE_PATH_MEDIA");
//		String realDir = req.getRealPath(dir);
//		dir = realDir + "\\";
		//fileListPath = new ArrayList<String>();
		fileList = new ArrayList<File>();
		String result = null;
		ArrayList uploadFiles = req.getUploadFileItem();
		Iterator items = uploadFiles.iterator();
		while ( items.hasNext() ) {
			FileItem item = (FileItem) items.next();
			if ( !item.isFormField() ) {
				String tmp = item.getName().toLowerCase();
				//String uploadfileName = tmp.substring( tmp.lastIndexOf( '\\' ) + 1, tmp.length() );
				if ( item.getSize() > 0 ) {
					//创建文件唯一名称  
					String uuid = UUID.randomUUID().toString();  
					String uploadfileName = serviceFileName + "_" + uuid +
					tmp.substring( tmp.lastIndexOf( "." ), tmp.length() );;
					File uploadDir = new File( dir );
					if ( !uploadDir.exists() ) {
						boolean mkUploadDirResult = uploadDir.mkdirs();
						if ( mkUploadDirResult == false ) {
							throw new Exception( "uploadDir:'" + uploadDir + "' can not be created!" );
						}
					}
					String path = dir + uploadfileName;
					//fileListPath.add(path);
					File oldFile = new File( path );
					if ( oldFile.exists() ) {
						File backupDir = new File( dir + "backup" );
						if ( !backupDir.exists() ) {
							boolean mkBackupDirResult = backupDir.mkdirs();

							if ( mkBackupDirResult == false ) {
								throw new Exception( "backupDir:'" + backupDir + "' can not be created!" );
							}
						}
						String backupPath = dir + "backup" + "\\"
							+ uploadfileName.substring( 0, uploadfileName.lastIndexOf( "." ) ) + "_"
							+ new SimpleDateFormat( "yyyyMMddHHmmss" ).format( new Date() ) 
							+ uploadfileName.substring( uploadfileName.lastIndexOf( "." ), uploadfileName.length() );
						
						TransferFileUtil.transferFile( path, backupPath );
						TransferFileUtil.delFile( oldFile );
					}
					if ( oldFile.createNewFile() || oldFile.canWrite() ) {
						item.write( oldFile );
						fileList.add(oldFile);
						result = "success";
					}
					else {
						result = "fail";
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 素材文件上传到FTP
	 * @param req
	 * @return
	 * @throws Exception
	 */
	public void uploadMaterialFileToFtp() throws Exception {
		String FUNCTION_NAME = "uploadMaterialFileToFtp() ";
		logger.info( FUNCTION_NAME + "start" );
		if (fileList != null && fileList.size() > 0) {
			String uploadFTPFilePath = LocalProperties.getProperty("FTP_FILE_PATH_MEDIA");
			//上传文件列表
			FtpUtils.uploadFile(fileList, uploadFTPFilePath);
			fileListPath = new ArrayList<String>();
			for (File fileItem : fileList) {
				logger.info( FUNCTION_NAME + "FTP上传文件列表:" + fileItem.getName());
				fileListPath.add(FileTools.buildFullFilePath(uploadFTPFilePath, fileItem.getName()));
			}
		}
		logger.info( FUNCTION_NAME + "end" );
	}
	
	/**
	 * 素材审核
	 * @param bean 素材Bean
	 * @return
	 */
	public String materialAudit( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//素材ID
			bean.setMaterialId(req.getParameter("materialId"));
			//素材类型
			bean.setAdvert_type(req.getParameter("adertType"));
			//媒体素材的场合
			if (MEDIA_MATERIAL.equals(bean.getAdvert_type())) {
				AdvertDao dao = new AdvertDao();
				// 媒体素材审核
				int updateCnt = dao.materialAudit(bean);
				if (updateCnt == 0) {
					result = "failed";
				} else {
					result = "success";
				}
			} 
			//字幕的场合
			else if (SUBTITLES.equals(bean.getAdvert_type())){
				AdvertDao dao = new AdvertDao();
				//字幕审核
				int updateCnt = dao.subtitlesAudit(bean);
				if(updateCnt == 0){
					result = "failed";
				}else{
					result = "success";
				}
			} else {
				result = "failed!";
				throw new Exception("素材类型不正确!");
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 素材编辑初始化
	 * @param bean 素材Bean
	 * @return
	 */
	public void materialEdit( HttpServletRequest req ) throws Exception{
		AdvertBean bean = new AdvertBean();
		//素材类型
		bean.setAdvert_type(req.getParameter("adertType"));
		//素材ID
		bean.setMaterialId(req.getParameter("materialId"));
		
		AdvertDao dao = new AdvertDao();
		//媒体素材的场合
		if (MEDIA_MATERIAL.equals(bean.getAdvert_type())) {
			HashMap<String, String>[] materialBeans = dao.searchMaterialById(bean.getMaterialId());
			//素材ID
			bean.setMaterialId(materialBeans[0].get("MEDIA_ID"));
			//素材名称
			bean.setMaterialName(materialBeans[0].get("MEDIA_NAME"));
			//素材组别ID	
			bean.setMaterial_group(materialBeans[0].get("GROUP_ID"));
			//素材类型
			bean.setMaterialType(materialBeans[0].get("MEDIA_TYPE"));
			//素材文件名称
			String[] filePathArr = new String[1];
			filePathArr[0] = materialBeans[0].get("MEDIA_FILE_NAME");
			bean.setMaterial_filePath(filePathArr);
			//文件服务器路径
			String[] serviceFilePathArr = new String[1];
			serviceFilePathArr[0] = materialBeans[0].get("MEDIA_URL");
			bean.setServiceFilePath(serviceFilePathArr);
			//素材描述
			String[] desctibeArr = new String[1];
			desctibeArr[0] = materialBeans[0].get("DESCRIPTION");
			bean.setMaterial_describe(desctibeArr);
			//素材联接
			String[] linkArr = new String[1];
			linkArr[0] = materialBeans[0].get("LINK_URL");
			bean.setMaterial_link(linkArr);
		}
		//字幕的场合
		else if (SUBTITLES.equals(bean.getAdvert_type())){
			HashMap<String, String>[] materialBeans = dao.searchSubtitlesById(bean.getMaterialId());
			//字幕ID
			bean.setMaterialId(materialBeans[0].get("SUBTITLES_ID"));
			//字幕名称
			bean.setSubtitles_name(materialBeans[0].get("SUBTITLES_NAME"));
			//素材组别ID
			bean.setMaterial_group(materialBeans[0].get("GROUP_ID"));
			//字幕描述
			bean.setSubtitles_describe(materialBeans[0].get("DESCRIPTION"));
			//字幕内容
			bean.setRoll_word(materialBeans[0].get("SUBTITLES_CONTENT"));
			//背景图片
			bean.setBackground_filePath(materialBeans[0].get("BACKGROUND_PICTURE"));
			//背景颜色
			bean.setBackground_color(materialBeans[0].get("BACKGROUND_COLOR"));
			//文字颜色
			bean.setWord_color(materialBeans[0].get("FONT_COLOR"));
			//文字属性
			bean.setWord_attribute(materialBeans[0].get("FONT_PROPERTIES"));
			//高度
			bean.setWord_height(materialBeans[0].get("HEIGHT"));
			//宽度
			bean.setWord_width(materialBeans[0].get("WIDTH"));
			//垂直对齐方式
			bean.setVertical_align(materialBeans[0].get("VERTICAL_ALIGN"));
			//水平对齐方式
			bean.setText_align(materialBeans[0].get("HORIZONTAL_ALIGN"));
			//延迟播放时间　
			bean.setRoll_delay(materialBeans[0].get("DELAY_TIME"));
		} else {
			throw new Exception("素材类型不正确!");
		}
		req.setAttribute("resultBean", bean);
	}
	
	/**
	 * 素材编辑保存
	 * @param req 
	 * @return
	 */
	public String materialUpdate( HttpServletRequest req ) {
		MultipartRequest multipartReq = null;
		if ( ServletFileUpload.isMultipartContent( req ) == true ) {
			try {
				req = new MultipartRequest( req );
				uploadMaterialFile(multipartReq, "");
			}
			catch ( Exception e ) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//素材ID
			bean.setMaterialId(req.getParameter("materialId"));
			//素材组别
			bean.setMaterial_group(req.getParameter("material_group"));
			//素材类型
			bean.setAdvert_type(req.getParameter("adertType"));
			
			//媒体素材的场合
			if (MEDIA_MATERIAL.equals(bean.getAdvert_type())) {
				//素材文件路径
				String[] filePathArr = new String[1];
				filePathArr[0] = req.getParameter("material_filePath");
				bean.setMaterial_filePath(filePathArr);
				//文件服务器路径
				if (fileListPath != null) {
					bean.setServiceFilePath(fileListPath.toArray(new String[fileListPath.size()]));
				} else {
					String[] serviceFilePathArr = new String[1];
					serviceFilePathArr[0] = req.getParameter("service_filePath");
					bean.setServiceFilePath(serviceFilePathArr);
				}
				//素材名称
				String[] materialName = new String[1];
				materialName[0] = req.getParameter("material_name");
				bean.setMaterial_name(materialName);
				//素材文件类型
				String[] materialType = new String[1];
				materialType[0] = req.getParameter("material_type");
				bean.setMaterial_type(materialType);
				//素材描述
				String[] materialDescribe = new String[1];
				materialDescribe[0] = req.getParameter("material_describe");
				bean.setMaterial_describe(materialDescribe);
				//素材联接
				String[] materialLink = new String[1];
				materialLink[0] = req.getParameter("material_link");
				bean.setMaterial_link(materialLink);
				AdvertDao dao = new AdvertDao();
				// 媒体素材审核
				int updateCnt = dao.materialUpdate(bean);
				if (updateCnt == 0) {
					result = "failed";
				} else {
					result = "success";
				}
			} 
			//字幕的场合
			else if (SUBTITLES.equals(bean.getAdvert_type())){
				//字幕名称
				bean.setSubtitles_name(req.getParameter("subtitles_name"));
				//字幕说明
				bean.setSubtitles_describe(req.getParameter("subtitles_describe"));
				//滚动文字
				bean.setRoll_word(req.getParameter("roll_word"));
				//背景图片路径
				bean.setBackground_filePath(req.getParameter("background_filePath"));
				//背景颜色
				bean.setBackground_color(req.getParameter("background_color"));
				//字体颜色
				bean.setWord_color(req.getParameter("word_color"));
				//字体属性
				bean.setWord_attribute(req.getParameter("word_tilt") + ";" + req.getParameter("word_bold") + 
						";" + req.getParameter("word_size") + ";" + req.getParameter("word_type"));
				//字体高
				bean.setWord_height(req.getParameter("word_height"));
				//字体宽
				bean.setWord_width(req.getParameter("word_width"));
				//字体水平对齐(1:居左 2:居中 3:居右)
				bean.setText_align(req.getParameter("text_align"));
				//字体垂直对齐(1:居左 2:居中 3:居右)
				bean.setVertical_align(req.getParameter("vertical_align"));
				//滚动延迟(微妙)
				bean.setRoll_delay(req.getParameter("roll_delay"));
				
				AdvertDao dao = new AdvertDao();
				//字幕更新
				int updateCnt = dao.subtitlesUpdate(bean);
				if(updateCnt == 0){
					result = "failed";
				}else{
					result = "success";
				}
			} else {
				result = "failed!";
				throw new Exception("素材类型不正确!");
			}
			AdvertBean newBean = new AdvertBean();
			newBean.setAdvert_type(bean.getAdvert_type());
			req.setAttribute("resultBean", newBean);
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 素材删除
	 * @param req 
	 * @return
	 */
	public String materialDelete( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			bean.setMaterialId(req.getParameter("materialId"));
			//素材类型
			bean.setAdvert_type(req.getParameter("adertType"));
			
			//媒体素材的场合
			if (MEDIA_MATERIAL.equals(bean.getAdvert_type())) {
				AdvertDao dao = new AdvertDao();
				// 媒体素材审核
				int delCnt = dao.materialDelete(bean);
				if (delCnt == 0) {
					result = "failed";
				} else {
					result = "success";
				}
			} 
			//字幕的场合
			else if (SUBTITLES.equals(bean.getAdvert_type())){
				AdvertDao dao = new AdvertDao();
				//字幕审核
				int delCnt = dao.subtitlesDelete(bean);
				if(delCnt == 0){
					result = "failed";
				}else{
					result = "success";
				}
			} else {
				result = "failed!";
				throw new Exception("素材类型不正确!");
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 素材批量删除
	 * @param req 
	 * @return
	 */
	public String materialAllDelete( HttpServletRequest req ) {
		String result = "";
		try {
			
			//素材ID
			String materialId = req.getParameter("materialId");
			//素材类型
			String advertType = req.getParameter("adertType");
			if (materialId.contains(",")) {
				String[] materialIdArr = materialId.split(",");
				String[] advertTypeArr = advertType.split(",");
				for (int i = 0; i < materialIdArr.length; i++) {
					AdvertBean bean = new AdvertBean();
					//素材ID
					bean.setMaterialId(materialIdArr[i]);
					//素材类型
					bean.setAdvert_type(advertTypeArr[i]);
					
					//媒体素材的场合
					if (MEDIA_MATERIAL.equals(bean.getAdvert_type())) {
						AdvertDao dao = new AdvertDao();
						// 媒体素材审核
						int delCnt = dao.materialDelete(bean);
						if (delCnt == 0) {
							result = "failed";
						} else {
							result = "success";
						}
					} 
					//字幕的场合
					else if (SUBTITLES.equals(bean.getAdvert_type())){
						AdvertDao dao = new AdvertDao();
						//字幕审核
						int delCnt = dao.subtitlesDelete(bean);
						if(delCnt == 0){
							result = "failed";
						}else{
							result = "success";
						}
					} else {
						result = "failed!";
						throw new Exception("素材类型不正确!");
					}
				}
			} else {
				result = materialDelete(req);
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 素材批量审核
	 * @param req 
	 * @return
	 */
	public String materialAllAudit( HttpServletRequest req ) {
		String result = "";
		try {
			//素材ID
			String materialId = req.getParameter("materialId");
			//素材类型
			String advertType = req.getParameter("adertType");
			if (materialId.contains(",")) {
				String[] materialIdArr = materialId.split(",");
				String[] advertTypeArr = advertType.split(",");
				for (int i = 0; i < materialIdArr.length; i++) {
					AdvertBean bean = new AdvertBean();
					//素材ID
					bean.setMaterialId(materialIdArr[i]);
					//素材类型
					bean.setAdvert_type(advertTypeArr[i]);
					
					//媒体素材的场合
					if (MEDIA_MATERIAL.equals(bean.getAdvert_type())) {
						AdvertDao dao = new AdvertDao();
						// 媒体素材审核
						int delCnt = dao.materialAudit(bean);
						if (delCnt == 0) {
							result = "failed";
						} else {
							result = "success";
						}
					} 
					//字幕的场合
					else if (SUBTITLES.equals(bean.getAdvert_type())){
						AdvertDao dao = new AdvertDao();
						//字幕审核
						int delCnt = dao.subtitlesAudit(bean);
						if(delCnt == 0){
							result = "failed";
						}else{
							result = "success";
						}
					} else {
						result = "failed!";
						throw new Exception("素材类型不正确!");
					}
				}
			} else {
				result = materialAudit(req);
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 布局检索
	 * @param req
	 * @throws Exception
	 */
	public void layoutSearch(HttpServletRequest req) throws Exception {
		AdvertBean bean = new AdvertBean();
		//布局名称
		bean.setLayoutName(req.getParameter("layout_name"));
		//审核状态
		bean.setAuditStatus(req.getParameter("audit_type"));
		//创建用户
		bean.setOperator(req.getParameter("operator_name"));
		//添加时间From
		bean.setLayoutAddDateFrom(req.getParameter("search_date_s"));
		//添加时间To
		bean.setLayoutAddDateTo(req.getParameter("search_date_e"));
		//检索结果当前位置
		bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		//排序方式
		bean.setSortField(req.getParameter("sort_field"));
		//排序类型
		bean.setSortType(req.getParameter("sort_type"));
		
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		
		List<AdvertBean> resultBeans = new ArrayList<AdvertBean>();
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] layoutBeans = dao.searchLayout(bean);
		if (layoutBeans != null) {
			//检索结果开始位置
			int start_p  = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			//检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;
			
			int loop = 0;
			for (HashMap<String, String> itemMap : layoutBeans) {
				loop++;
				if( loop <= start_p ) {
                    continue;
                }
				if( loop > start_p && loop <= end_p ) {
					start_p++;
					AdvertBean advertbean = new AdvertBean();
					//布局ID
					advertbean.setLayoutId(itemMap.get("LAYOUT_ID"));
					//布局名称
					advertbean.setLayoutName(itemMap.get("LAYOUT_NAME"));
					//创建用户
					advertbean.setOperator(itemMap.get("NAME"));
					//布局分辨率宽
					if (itemMap.get("LAYOUT_WIDTH") != null) {
						advertbean.setLayoutwidth(Integer.parseInt(itemMap.get("LAYOUT_WIDTH")));
					}
					//布局分辨率高
					if (itemMap.get("LAYOUT_HEIGHT") != null) {
						advertbean.setLayoutHeight(Integer.parseInt(itemMap.get("LAYOUT_HEIGHT")));
					}
					//创建时间
					advertbean.setCreateTime(itemMap.get("OPERATETIME"));
					//审核情况
					advertbean.setAuditStatus(itemMap.get("STATUS"));
					resultBeans.add(advertbean);
				}
			}
			//检索总行数
			bean.setTotal_num(layoutBeans.length);
			//bean.setPoint_num( start_p );
		}
		//检索条件保存
		req.setAttribute("select_object", bean);
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	/**
	 * 布局添加
	 * @param req
	 * @return 添加结果
	 */
	public String layoutAdd(HttpServletRequest req) throws Exception {
		String result = "";
		String layoutId = "";
		try {
			AdvertBean bean = new AdvertBean();
			//布局名称
			bean.setLayoutName(req.getParameter("layout_name"));
		    //布局描述
			bean.setLayoutDescribe(req.getParameter("layout_describe"));
			//布局类型
			bean.setLayoutScreen(req.getParameter("layout_screen"));
		    //布局分辨率宽
			if (req.getParameter("layout_width") != null) {
				bean.setLayoutwidth(Integer.parseInt(req.getParameter("layout_width")));
			}
			//布局分辨率高
			if (req.getParameter("layout_height") != null) {
				bean.setLayoutHeight(Integer.parseInt(req.getParameter("layout_height")));
			}
			//分屏数
			bean.setScreenNum(Integer.parseInt(req.getParameter("layout_screen")));
			//布局坐标信息
			String coordinate = req.getParameter("coordinate");
			String[] screenCnt = coordinate.split(";");
			if (screenCnt == null || screenCnt.length == 0) {
				throw new Exception("布局信息不正确。");
			}
			int[] screenStartX = new int[screenCnt.length];
			int[] screenStartY = new int[screenCnt.length];
			int[] screenEndX = new int[screenCnt.length];
			int[] screenEndY = new int[screenCnt.length];
			String[] screenName = new String[screenCnt.length];
			int index = 0;
			for (String item : screenCnt) {
				String[] coordinateItem = item.split(",");
				if (coordinateItem == null || coordinateItem.length != 4) {
					throw new Exception("布局信息不正确。");
				}
				screenStartX[index] = Integer.parseInt(coordinateItem[0]);
				screenStartY[index] = Integer.parseInt(coordinateItem[1]);
				screenEndX[index] = Integer.parseInt(coordinateItem[2]);
				screenEndY[index] = Integer.parseInt(coordinateItem[3]);
				screenName[index] = "win" + (++index);
			}
//			if (bean.getScreenNum() == 1) {
//				screenStartX = new int[1];
//				screenStartY = new int[1];
//				screenEndX = new int[1];
//				screenEndY = new int[1];
//				screenName = new String[1];
//				//布局1坐标
//				screenStartX[0] = 0;
//				screenStartY[0] = 0;
//				screenEndX[0] = 1024;
//				screenEndY[0] = 768;
//				screenName[0] = "win1";
//			} else if (bean.getScreenNum() == 2) {
//				screenStartX = new int[2];
//				screenStartY = new int[2];
//				screenEndX = new int[2];
//				screenEndY = new int[2];
//				screenName = new String[2];
//				//布局1坐标
//				screenStartX[0] = 0;
//				screenStartY[0] = 0;
//				screenEndX[0] = 512;
//				screenEndY[0] = 768;
//				screenName[0] = "win1";
//				//布局2坐标
//				screenStartX[1] = 512;
//				screenStartY[1] = 0;
//				screenEndX[1] = 1024;
//				screenEndY[1] = 768;
//				screenName[1] = "win2";
//			} else if (bean.getScreenNum() == 4) {
//				screenStartX = new int[4];
//				screenStartY = new int[4];
//				screenEndX = new int[4];
//				screenEndY = new int[4];
//				screenName = new String[4];
//				//布局1坐标
//				screenStartX[0] = 0;
//				screenStartY[0] = 0;
//				screenEndX[0] = 512;
//				screenEndY[0] = 384;
//				screenName[0] = "win1";
//				//布局2坐标
//				screenStartX[1] = 512;
//				screenStartY[1] = 0;
//				screenEndX[1] = 1024;
//				screenEndY[1] = 384;
//				screenName[1] = "win2";
//				//布局3坐标
//				screenStartX[2] = 0;
//				screenStartY[2] = 384;
//				screenEndX[2] = 512;
//				screenEndY[2] = 768;
//				screenName[2] = "win3";
//				//布局4坐标
//				screenStartX[3] = 512;
//				screenStartY[3] = 384;
//				screenEndX[3] = 1024;
//				screenEndY[3] = 768;
//				screenName[3] = "win4";
//			}
			///分屏开始横坐标
			bean.setScreenStartX(screenStartX);
			//分屏开始纵坐标
			bean.setScreenStartY(screenStartY);
			//分屏结束横坐标
			bean.setScreenEndX(screenEndX);
			//分屏结束纵坐标
			bean.setScreenEndY(screenEndY);
			//分屏名称
			bean.setScreenName(screenName);
			//操作者
			User user = (User) req.getSession().getAttribute("user_info");
			bean.setOperator(user.getId());
			AdvertDao dao = new AdvertDao();
			layoutId = dao.insertLayout(bean);
			if(layoutId == null){
				result = "failed";
			}else{
				result = "success";
			}
		} catch (Exception e ) {
			result = "failed";
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return layoutId;
	}
	
	/**
	 * 布局审核
	 * @param bean Bean
	 * @return
	 */
	public String layoutAudit( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//布局ID
			bean.setLayoutId(req.getParameter("layoutId"));
			AdvertDao dao = new AdvertDao();
			// 布局审核
			int updateCnt = dao.layoutAudit(bean);
			if (updateCnt == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 布局编辑初始化
	 * @param bean 素材Bean
	 * @return
	 */
	public void layoutEdit( HttpServletRequest req ) {
		AdvertBean bean = new AdvertBean();
		//布局ID
		bean.setLayoutId(req.getParameter("layoutId"));
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] materialBeans = dao.searchLayout(bean);
		AdvertBean beanItem = new AdvertBean();
		if (materialBeans != null && materialBeans.length > 0) {
			//布局ID
			beanItem.setLayoutId(materialBeans[0].get("LAYOUT_ID"));
			//布局名称
			beanItem.setLayoutName(materialBeans[0].get("LAYOUT_NAME"));
			//布局描述
			beanItem.setLayoutDescribe(materialBeans[0].get("DESCRIPTION"));
			//创建用户
			beanItem.setOperator(materialBeans[0].get("NAME"));
			//布局分辨率宽
			if (materialBeans[0].get("LAYOUT_WIDTH") != null) {
				beanItem.setLayoutwidth(Integer.parseInt(materialBeans[0].get("LAYOUT_WIDTH")));
			}
			//布局分辨率高
			if (materialBeans[0].get("LAYOUT_HEIGHT") != null) {
				beanItem.setLayoutHeight(Integer.parseInt(materialBeans[0].get("LAYOUT_HEIGHT")));
			}
			//分屏类型
			beanItem.setLayoutScreen(materialBeans[0].get("SCREEN_NUM"));
			//分屏数
			beanItem.setScreenNum(Integer.parseInt(materialBeans[0].get("SCREEN_NUM")));
			//创建时间
			beanItem.setCreateTime(materialBeans[0].get("OPERATETIME"));
			//审核情况
			beanItem.setAuditStatus(materialBeans[0].get("STATUS"));
		}
		req.setAttribute("resultBean", beanItem);
	}
	
	/**
	 * 布局编辑保存
	 * @param req 
	 * @return
	 */
	public String layoutUpdate( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//布局ID
			bean.setLayoutId(req.getParameter("layoutId"));
			//布局名称
			bean.setLayoutName(req.getParameter("layout_name"));
		    //布局描述
			bean.setLayoutDescribe(req.getParameter("layout_describe"));
			//布局类型
			bean.setLayoutScreen(req.getParameter("layout_screen"));
		    //布局分辨率宽
			if (req.getParameter("layout_width") != null) {
				bean.setLayoutwidth(Integer.parseInt(req.getParameter("layout_width")));
			}
			//布局分辨率高
			if (req.getParameter("layout_height") != null) {
				bean.setLayoutHeight(Integer.parseInt(req.getParameter("layout_height")));
			}
			//分屏数
			bean.setScreenNum(Integer.parseInt(req.getParameter("layout_screen")));
			//布局坐标信息
			String coordinate = req.getParameter("coordinate");
			String[] screenCnt = coordinate.split(";");
			if (screenCnt == null || screenCnt.length == 0) {
				throw new Exception("布局信息不正确。");
			}
			int[] screenStartX = new int[screenCnt.length];
			int[] screenStartY = new int[screenCnt.length];
			int[] screenEndX = new int[screenCnt.length];
			int[] screenEndY = new int[screenCnt.length];
			String[] screenName = new String[screenCnt.length];
			int index = 0;
			for (String item : screenCnt) {
				String[] coordinateItem = item.split(",");
				if (coordinateItem == null || coordinateItem.length != 4) {
					throw new Exception("布局信息不正确。");
				}
				screenStartX[index] = Integer.parseInt(coordinateItem[0]);
				screenStartY[index] = Integer.parseInt(coordinateItem[1]);
				screenEndX[index] = Integer.parseInt(coordinateItem[2]);
				screenEndY[index] = Integer.parseInt(coordinateItem[3]);
				screenName[index] = "win" + (++index);
			}
//			if (bean.getScreenNum() == 1) {
//				screenStartX = new int[1];
//				screenStartY = new int[1];
//				screenEndX = new int[1];
//				screenEndY = new int[1];
//				screenName = new String[1];
//				//布局1坐标
//				screenStartX[0] = 0;
//				screenStartY[0] = 0;
//				screenEndX[0] = 1024;
//				screenEndY[0] = 768;
//				screenName[0] = "win1";
//			} else if (bean.getScreenNum() == 2) {
//				screenStartX = new int[2];
//				screenStartY = new int[2];
//				screenEndX = new int[2];
//				screenEndY = new int[2];
//				screenName = new String[2];
//				//布局1坐标
//				screenStartX[0] = 0;
//				screenStartY[0] = 0;
//				screenEndX[0] = 512;
//				screenEndY[0] = 768;
//				screenName[0] = "win1";
//				//布局2坐标
//				screenStartX[1] = 512;
//				screenStartY[1] = 0;
//				screenEndX[1] = 1024;
//				screenEndY[1] = 768;
//				screenName[1] = "win2";
//			} else if (bean.getScreenNum() == 4) {
//				screenStartX = new int[4];
//				screenStartY = new int[4];
//				screenEndX = new int[4];
//				screenEndY = new int[4];
//				screenName = new String[4];
//				//布局1坐标
//				screenStartX[0] = 0;
//				screenStartY[0] = 0;
//				screenEndX[0] = 512;
//				screenEndY[0] = 384;
//				screenName[0] = "win1";
//				//布局2坐标
//				screenStartX[1] = 512;
//				screenStartY[1] = 0;
//				screenEndX[1] = 1024;
//				screenEndY[1] = 384;
//				screenName[1] = "win2";
//				//布局3坐标
//				screenStartX[2] = 0;
//				screenStartY[2] = 384;
//				screenEndX[2] = 512;
//				screenEndY[2] = 768;
//				screenName[2] = "win3";
//				//布局4坐标
//				screenStartX[3] = 512;
//				screenStartY[3] = 384;
//				screenEndX[3] = 1024;
//				screenEndY[3] = 768;
//				screenName[3] = "win4";
//			}
			//分屏开始横坐标
			bean.setScreenStartX(screenStartX);
			//分屏开始纵坐标
			bean.setScreenStartY(screenStartY);
			//分屏结束横坐标
			bean.setScreenEndX(screenEndX);
			//分屏结束纵坐标
			bean.setScreenEndY(screenEndY);
			//分屏名称
			bean.setScreenName(screenName);
			
			AdvertDao dao = new AdvertDao();
			// 布局编辑保存
			int updateCnt = dao.layoutUpdate(bean);
			if (updateCnt == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 布局删除
	 * @param req 
	 * @return
	 */
	public String layoutDelete( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//布局ID
			bean.setLayoutId(req.getParameter("layoutId"));
			
			AdvertDao dao = new AdvertDao();
			// 布局删除
			int delCnt = dao.layoutDelete(bean);
			if (delCnt == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 布局批量删除
	 * @param req 
	 * @return
	 */
	public String layoutAllDelete( HttpServletRequest req ) {
		String result = "";
		try {
			//布局ID
			String layoutId = req.getParameter("layoutId");
			if (layoutId.contains(",")) {
				String[] layoutIdArr = layoutId.split(",");
				for (int i = 0; i < layoutIdArr.length; i++) {
					AdvertBean bean = new AdvertBean();
					//布局ID
					bean.setLayoutId(layoutIdArr[i]);
					
					AdvertDao dao = new AdvertDao();
					// 媒体素材审核
					int delCnt = dao.layoutDelete(bean);
					if (delCnt == 0) {
						result = "failed";
					} else {
						result = "success";
					}
				}
			} else {
				result = layoutDelete(req);
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 布局批量审核
	 * @param req 
	 * @return
	 */
	public String layoutAllAudit( HttpServletRequest req ) {
		String result = "";
		try {
			//布局ID
			String layoutId = req.getParameter("layoutId");
			if (layoutId.contains(",")) {
				String[] layoutIdArr = layoutId.split(",");
				for (int i = 0; i < layoutIdArr.length; i++) {
					AdvertBean bean = new AdvertBean();
					//布局ID
					bean.setLayoutId(layoutIdArr[i]);
					
					AdvertDao dao = new AdvertDao();
					// 布局审核
					int delCnt = dao.layoutAudit(bean);
					if (delCnt == 0) {
						result = "failed";
					} else {
						result = "success";
					}
				}
			} else {
				result = layoutAudit(req);
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单检索
	 * @param req
	 * @throws Exception
	 */
	public void searchProgramlist(HttpServletRequest req) throws Exception {
		AdvertBean bean = new AdvertBean();
		//节目单名称
		bean.setProgramlistName(req.getParameter("programlist_name"));
		//审核状态
		bean.setAuditStatus(req.getParameter("audit_type"));
		//创建用户
		bean.setOperator(req.getParameter("operator_name"));
		//添加时间From
		bean.setProgramlistAddDateFrom(req.getParameter("search_date_s"));
		//添加时间To
		bean.setProgramlistAddDateTo(req.getParameter("search_date_e"));
		//检索结果当前位置
		bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		//排序方式
		bean.setSortField(req.getParameter("sort_field"));
		//排序类型
		bean.setSortType(req.getParameter("sort_type"));
		
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		
		List<AdvertBean> resultBeans = new ArrayList<AdvertBean>();
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] plBeans = dao.searchProgramlist(bean);
		if (plBeans != null) {
			//检索结果开始位置
			int start_p  = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			//检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;
			
			int loop = 0;
			for (HashMap<String, String> itemMap : plBeans) {
				loop++;
				if( loop <= start_p ) {
                    continue;
                }
				if( loop > start_p && loop <= end_p ) {
					start_p++;
					AdvertBean advertbean = new AdvertBean();
					//节目单ID
					advertbean.setProgramlistId(itemMap.get("BILL_ID"));
					//节目单名称
					advertbean.setProgramlistName(itemMap.get("BILL_NAME"));
					//创建用户
					advertbean.setOperator(itemMap.get("NAME"));
					//创建时间
					advertbean.setCreateTime(itemMap.get("OPERATETIME"));
					//审核情况
					advertbean.setAuditStatus(itemMap.get("STATUS"));
					resultBeans.add(advertbean);
				}
			}
			//检索总行数
			bean.setTotal_num(plBeans.length);
		}
		//检索条件保存
		req.setAttribute("select_object", bean);
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	/**
	 * 节目单添加
	 * @param req
	 * @return 添加结果
	 */
	public String programlistAdd(HttpServletRequest req) throws Exception {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//节目单名称
			bean.setProgramlistName(req.getParameter("programlist_name"));
		    //节目单描述
			bean.setProgramlistDescribe(req.getParameter("programlist_describe"));
			//节目单时长
			if (req.getParameter("programlist_time") != null && !"".equals(req.getParameter("programlist_time"))) {
				bean.setProgramlistPlayTime(Integer.parseInt(req.getParameter("programlist_time")));
			}
			//布局ID
			String layoutId = req.getParameter("layoutId");
			if (layoutId == null || "".equals(layoutId)){
				//布局ID为空,节目单新创建的布局,先插入布局表
				layoutId = layoutAdd(req);
			}
			bean.setLayoutId(layoutId);
			//画面添加的素材ID
			String[] materialIdArr = req.getParameterValues("materialId");
			//该素材所在布局No
			String[] windowsNoArr = req.getParameterValues("layout_index");
			//素材播放时长
			String[] materialPlayTime = req.getParameterValues("play_time");
			//素材显示级别
			String[] materialLevel = req.getParameterValues("show_level");
			
			AdvertDao dao = new AdvertDao();
			//取得布局分屏详细信息
			HashMap<String, String>[] layoutDetail = dao.searchLayoutDetail(bean);
			//素材List
			List<AdvertBean> programlistMaterials = new ArrayList<AdvertBean>();
			//设置素材,素材所在布局,素材播放时长,素材显示级别
			if (materialIdArr != null && windowsNoArr != null) {
				for (int i = 0; i < materialIdArr.length; i++) {
					if (windowsNoArr[i] == null || "".equals(windowsNoArr[i])) {
						continue;
					}
					AdvertBean materialListBean = new AdvertBean();
					materialListBean.setMaterialId(materialIdArr[i]);
					if (materialPlayTime[i] == null || "".equals(materialPlayTime[i])) {
						materialListBean.setMaterialPlayTime(0);
					} else {
						materialListBean.setMaterialPlayTime(Integer.parseInt(materialPlayTime[i]));
					}
					if (materialLevel[i] == null || "".equals(materialLevel[i])) {
						materialListBean.setMaterialLevel(0);
					} else {
						materialListBean.setMaterialLevel(Integer.parseInt(materialLevel[i]));
					}
					String windowId = "";
					for (HashMap<String, String> itemMap : layoutDetail) {
						String windowNo = (String) itemMap.get("WINDOW_NO");
						if (windowNo.equals(windowsNoArr[i])) {
							windowId = (String) itemMap.get("WINDOW_ID");
							break;
						}
					}
					materialListBean.setWindowId(windowId);
					programlistMaterials.add(materialListBean);
				}
			}
			bean.setProgramlistMaterials(programlistMaterials);
			//操作者
			User user = (User) req.getSession().getAttribute("user_info");
			bean.setOperator(user.getId());
			bean.setOrgId(user.getOrg_id());
			String programlistId = dao.insertProgramlist(bean);
			if(programlistId == null){
				result = "failed";
			}else{
				result = "success";
			}
			//节目单ID
			bean.setProgramlistId(programlistId);
			//向FTP上传节目单文件
			initBillFileByBillID(bean);
		} catch (Exception e ) {
			result = "failed";
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单审核
	 * @param bean Bean
	 * @return
	 */
	public String programlistAudit( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//节目单ID
			bean.setProgramlistId(req.getParameter("programlistId"));
			AdvertDao dao = new AdvertDao();
			// 节目单审核
			int updateCnt = dao.programlistAudit(bean);
			if (updateCnt == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单编辑初始化
	 * @param bean Bean
	 * @return
	 */
	public void programlistEdit( HttpServletRequest req ) {
		AdvertBean bean = new AdvertBean();
		//节目单ID
		bean.setProgramlistId(req.getParameter("programlistId"));
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] plBeans = dao.searchProgramlist(bean);
		AdvertBean resultBean = new AdvertBean();
		if (plBeans != null && plBeans.length > 0) {
			//节目单ID
			resultBean.setProgramlistId(plBeans[0].get("BILL_ID"));
			//节目单名称
			resultBean.setProgramlistName(plBeans[0].get("BILL_NAME"));
			//节目单描述
			resultBean.setProgramlistDescribe(plBeans[0].get("DESCRIPTION"));
			//节目单时长
			if (plBeans[0].get("PLAYTIME") != null) {
				resultBean.setProgramlistPlayTime(Integer.parseInt(plBeans[0].get("PLAYTIME")));
			}
			//布局ID
			resultBean.setLayoutId(plBeans[0].get("LAYOUT_ID"));
			//布局名称
			resultBean.setLayoutName(plBeans[0].get("LAYOUT_NAME"));
			//布局描述
			resultBean.setLayoutDescribe(plBeans[0].get("LAYOUT_DESCRIPTION"));
			//布局宽
			resultBean.setLayoutwidth(Integer.parseInt(plBeans[0].get("LAYOUT_WIDTH")));
			//布局高
			resultBean.setLayoutHeight(Integer.parseInt(plBeans[0].get("LAYOUT_HEIGHT")));
			//分屏类型
			resultBean.setLayoutScreen(plBeans[0].get("SCREEN_NUM"));
			//分屏数
			resultBean.setScreenNum(Integer.parseInt(plBeans[0].get("SCREEN_NUM")));
			//创建用户
			resultBean.setOperator(plBeans[0].get("NAME"));
			//创建时间
			resultBean.setCreateTime(plBeans[0].get("OPERATETIME"));
			//审核情况
			resultBean.setAuditStatus(plBeans[0].get("STATUS"));
			
			//节目单素材信息检索
			HashMap<String, String>[] materialBeans = dao.searchProgramlistDetail(resultBean);
			if (materialBeans != null && materialBeans.length != 0) {
				List<AdvertBean> programlistMaterials = new ArrayList<AdvertBean>();
				for (HashMap<String, String> itemMap : materialBeans) {
					AdvertBean itemBean = new AdvertBean();
					//素材ID
					itemBean.setMaterialId(itemMap.get("MEDIA_ID"));
					//素材名
					itemBean.setMaterialName(itemMap.get("MEDIA_NAME"));
					//素材文件
					itemBean.setMaterialFilelName(itemMap.get("MEDIA_URL"));
					//素材所在布局
					if (itemMap.get("WINDOW_NO") != null) {
						itemBean.setWindowNo(Integer.parseInt(itemMap.get("WINDOW_NO")));
					}
					//素材显示级别
					if (itemMap.get("MEDIA_LEVEL") != null) {
						itemBean.setMaterialLevel(Integer.parseInt(itemMap.get("MEDIA_LEVEL")));
					}
					//素材播放时长
					if (itemMap.get("PLAYTIME") != null) {
						itemBean.setMaterialPlayTime(Integer.parseInt(itemMap.get("PLAYTIME")));
					}
					//素材链接
					itemBean.setMaterialLink(itemMap.get("LINK_URL"));
					programlistMaterials.add(itemBean);
				}
				//节目单素材信息
				resultBean.setProgramlistMaterials(programlistMaterials);
			}
		}
		req.setAttribute("resultBean", resultBean);
	}
	
	/**
	 * 节目单编辑保存
	 * @param req 
	 * @return
	 */
	public String programlistUpdate( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//节目单ID
			bean.setProgramlistId(req.getParameter("programlistId"));
			//节目单名称
			bean.setProgramlistName(req.getParameter("programlist_name"));
		    //节目单描述
			bean.setProgramlistDescribe(req.getParameter("programlist_describe"));
			//节目单时长
			if (req.getParameter("programlist_time") != null && !"".equals(req.getParameter("programlist_time"))) {
				bean.setProgramlistPlayTime(Integer.parseInt(req.getParameter("programlist_time")));
			}
			//布局ID
			String layoutId = req.getParameter("layoutId");
			if (layoutId == null || "".equals(layoutId)){
				//布局ID为空,节目单新创建的布局,先插入布局表
				layoutId = layoutAdd(req);
			}
			bean.setLayoutId(layoutId);
			//画面添加的素材ID
			String[] materialIdArr = req.getParameterValues("materialId");
			//该素材所在布局No
			String[] windowsNoArr = req.getParameterValues("layout_index");
			//素材播放时长
			String[] materialPlayTime = req.getParameterValues("play_time");
			//素材显示级别
			String[] materialLevel = req.getParameterValues("show_level");
			
			AdvertDao dao = new AdvertDao();
			//取得布局分屏详细信息
			HashMap<String, String>[] layoutDetail = dao.searchLayoutDetail(bean);
			//素材List
			List<AdvertBean> programlistMaterials = new ArrayList<AdvertBean>();
			//设置素材,素材所在布局,素材播放时长,素材显示级别
			if (materialIdArr != null && windowsNoArr != null) {
				for (int i = 0; i < materialIdArr.length; i++) {
					if (windowsNoArr[i] == null || "".equals(windowsNoArr[i])) {
						continue;
					}
					AdvertBean materialListBean = new AdvertBean();
					materialListBean.setMaterialId(materialIdArr[i]);
					if (materialPlayTime[i] == null || "".equals(materialPlayTime[i])) {
						materialListBean.setMaterialPlayTime(0);
					} else {
						materialListBean.setMaterialPlayTime(Integer.parseInt(materialPlayTime[i]));
					}
					if (materialPlayTime[i] == null || "".equals(materialPlayTime[i])) {
						materialListBean.setMaterialLevel(0);
					} else {
						materialListBean.setMaterialLevel(Integer.parseInt(materialLevel[i]));
					}
					String windowId = "";
					for (HashMap<String, String> itemMap : layoutDetail) {
						String windowNo = (String) itemMap.get("WINDOW_NO");
						if (windowNo.equals(windowsNoArr[i])) {
							windowId = (String) itemMap.get("WINDOW_ID");
							break;
						}
					}
					materialListBean.setWindowId(windowId);
					programlistMaterials.add(materialListBean);
				}
			}
			bean.setProgramlistMaterials(programlistMaterials);
			//操作者
			User user = (User) req.getSession().getAttribute("user_info");
			bean.setOperator(user.getId());
			bean.setOrgId(user.getOrg_id());
			// 节目单编辑保存
			int updateCnt = dao.programlistUpdate(bean);
			if (updateCnt == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单删除
	 * @param req 
	 * @return
	 */
	public String programlistDelete( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//节目单ID
			bean.setProgramlistId(req.getParameter("programlistId"));
			
			AdvertDao dao = new AdvertDao();
			// 节目单删除
			int delCnt = dao.programlistDelete(bean);
			if (delCnt == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单批量删除
	 * @param req 
	 * @return
	 */
	public String programlistAllDelete( HttpServletRequest req ) {
		String result = "";
		try {
			//节目单ID
			String programlistId = req.getParameter("programlistId");
			if (programlistId.contains(",")) {
				String[] programlistIdArr = programlistId.split(",");
				for (int i = 0; i < programlistIdArr.length; i++) {
					AdvertBean bean = new AdvertBean();
					//节目单ID
					bean.setProgramlistId(programlistIdArr[i]);
					
					AdvertDao dao = new AdvertDao();
					// 节目单删除
					int delCnt = dao.programlistDelete(bean);
					if (delCnt == 0) {
						result = "failed";
					} else {
						result = "success";
					}
				}
			} else {
				result = programlistDelete(req);
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单批量审核
	 * @param req 
	 * @return
	 */
	public String programlistAllAudit( HttpServletRequest req ) {
		String result = "";
		try {
			//节目单ID
			String programlistId = req.getParameter("programlistId");
			if (programlistId.contains(",")) {
				String[] programlistIdArr = programlistId.split(",");
				for (int i = 0; i < programlistIdArr.length; i++) {
					AdvertBean bean = new AdvertBean();
					//节目单ID
					bean.setProgramlistId(programlistIdArr[i]);
					
					AdvertDao dao = new AdvertDao();
					// 节目单审核
					int delCnt = dao.programlistAudit(bean);
					if (delCnt == 0) {
						result = "failed";
					} else {
						result = "success";
					}
				}
			} else {
				result = programlistAudit(req);
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 打开模式窗口画面-选取布局
	 * @param req 
	 * @return
	 */
	public void layoutSubWindow( HttpServletRequest req ) {
		List<AdvertBean> resultBeans = new ArrayList<AdvertBean>();
		AdvertDao dao = new AdvertDao();
		AdvertBean bean = new AdvertBean();
		bean.setAuditStatus("2");
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		HashMap<String, String>[] layoutBeans = dao.searchLayout(bean);
		if (layoutBeans != null) {
			for (HashMap<String, String> itemMap : layoutBeans) {
				AdvertBean advertbean = new AdvertBean();
				//布局ID
				advertbean.setLayoutId(itemMap.get("LAYOUT_ID"));
				//布局名称
				advertbean.setLayoutName(itemMap.get("LAYOUT_NAME"));
				//布局描述
				advertbean.setLayoutDescribe(itemMap.get("DESCRIPTION"));
				//布局类型
				advertbean.setLayoutScreen(itemMap.get("SCREEN_NUM"));
				//创建用户
				advertbean.setOperator(itemMap.get("NAME"));
				//布局分辨率宽
				if (itemMap.get("LAYOUT_WIDTH") != null) {
					advertbean.setLayoutwidth(Integer.parseInt(itemMap.get("LAYOUT_WIDTH")));
				}
				//布局分辨率高
				if (itemMap.get("LAYOUT_HEIGHT") != null) {
					advertbean.setLayoutHeight(Integer.parseInt(itemMap.get("LAYOUT_HEIGHT")));
				}
				//创建时间
				advertbean.setCreateTime(itemMap.get("OPERATETIME"));
				//审核情况
				advertbean.setAuditStatus(itemMap.get("STATUS"));
				//取得布局分屏详细信息
				HashMap<String, String>[] layoutDetail = dao.searchLayoutDetail(advertbean);
				String windows = "";
				for (HashMap<String, String> detail : layoutDetail) {
					if ("".equals(windows)) {
						windows = detail.get("WINDOW_ID");
					} else {
						windows += "," + detail.get("WINDOW_ID");
					}
				}
				//分屏ID
				advertbean.setWindowId(windows);
				resultBeans.add(advertbean);
			}
		}
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	/**
	 * 打开模式窗口画面-选取素材
	 * @param req 
	 * @return
	 */
	public void materialSubWindow( HttpServletRequest req ) {
		List<AdvertBean> resultBeans = new ArrayList<AdvertBean>();
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		AdvertBean bean = new AdvertBean();
		bean.setSubOrgIdArr(orgIdStr);
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] materialBeans = dao.selectMaterial(bean);
		if (materialBeans != null) {
			for (HashMap<String, String> itemMap : materialBeans) {
				AdvertBean advertbean = new AdvertBean();
				//素材ID
				advertbean.setMaterialId(itemMap.get("MEDIA_ID"));
				//素材名称
				advertbean.setMaterialName(itemMap.get("MEDIA_NAME"));
				//素材文件名
				if (itemMap.get("MEDIA_URL") != null && !"".equals(itemMap.get("MEDIA_URL"))) {
					//String fileName = itemMap.get("MEDIA_URL").substring(itemMap.get("MEDIA_URL").lastIndexOf('/') + 1);
					advertbean.setMaterialFilelName(itemMap.get("MEDIA_URL"));
				}
				//素材链接
				advertbean.setMaterialLink(itemMap.get("LINK_URL"));
				//创建用户
				advertbean.setOperator(itemMap.get("NAME"));
				//创建时间
				advertbean.setCreateTime(itemMap.get("OPERATETIME"));
				//审核情况
				advertbean.setAuditStatus(itemMap.get("STATUS"));
				resultBeans.add(advertbean);
			}
		}
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	/**
	 * 打开模式窗口画面-选取字幕
	 * @param req 
	 * @return
	 */
	public void subtitlesSubWindow( HttpServletRequest req ) {
		List<AdvertBean> resultBeans = new ArrayList<AdvertBean>();
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		AdvertBean bean = new AdvertBean();
		bean.setSubOrgIdArr(orgIdStr);
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] materialBeans = dao.selectSubtitles(bean);
		if (materialBeans != null) {
			for (HashMap<String, String> itemMap : materialBeans) {
				AdvertBean advertbean = new AdvertBean();
				//字幕ID
				advertbean.setSubtitlesId(itemMap.get("SUBTITLES_ID"));
				//字幕名称
				advertbean.setSubtitles_name(itemMap.get("SUBTITLES_NAME"));
				//创建用户
				advertbean.setOperator(itemMap.get("NAME"));
				//创建时间
				advertbean.setCreateTime(itemMap.get("OPERATETIME"));
				//审核情况
				advertbean.setAuditStatus(itemMap.get("STATUS"));
				resultBeans.add(advertbean);
			}
		}
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	/**
	 * 打开模式窗口画面-选取节目单
	 * @param req 
	 * @return
	 */
	public void programlistSubWindow( HttpServletRequest req ) {
		List<AdvertBean> resultBeans = new ArrayList<AdvertBean>();
		AdvertDao dao = new AdvertDao();
		AdvertBean bean = new AdvertBean();
		bean.setAuditStatus("2");
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		HashMap<String, String>[] materialBeans = dao.searchProgramlist(bean);
		if (materialBeans != null) {
			for (HashMap<String, String> itemMap : materialBeans) {
				AdvertBean advertbean = new AdvertBean();
				//节目单ID
				advertbean.setProgramlistId(itemMap.get("BILL_ID"));
				//节目单名称
				advertbean.setProgramlistName(itemMap.get("BILL_NAME"));
				//节目单描述
				advertbean.setProgramlistDescribe(itemMap.get("DESCRIPTION"));
				//节目单时长
				if (itemMap.get("PLAYTIME") != null && !"".equals(itemMap.get("PLAYTIME"))) {
					advertbean.setProgramlistPlayTime(Integer.parseInt(itemMap.get("PLAYTIME")));
				}
				//布局类型
				advertbean.setLayoutScreen(itemMap.get("SCREEN_NUM"));
				//创建用户
				advertbean.setOperator(itemMap.get("NAME"));
				//创建时间
				advertbean.setCreateTime(itemMap.get("OPERATETIME"));
				//审核情况
				advertbean.setAuditStatus(itemMap.get("STATUS"));
				resultBeans.add(advertbean);
			}
		}
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	/**
	 * 打开模式窗口画面-选取节目单组
	 * @param req 
	 * @return
	 */
	public void programlistGroupSubWindow( HttpServletRequest req ) {
		List<AdvertBean> resultGroupBeans = new ArrayList<AdvertBean>();
		List<AdvertBean> resultBeans = new ArrayList<AdvertBean>();
		AdvertDao dao = new AdvertDao();
		AdvertBean bean = new AdvertBean();
		bean.setAuditStatus("2");
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		//节目单组检索
		HashMap<String, String>[] groupBeans = dao.searchProgramlistGroup(bean);
		if (groupBeans != null) {
			for (HashMap<String, String> itemMap : groupBeans) {
				AdvertBean advertbean = new AdvertBean();
				//节目单组ID
				advertbean.setProgramlistGroupId(itemMap.get("GROUP_ID"));
				//节目单组名称
				advertbean.setProgramlistGroupName(itemMap.get("GROUP_NAME"));
				resultGroupBeans.add(advertbean);
			}
		}
		//节目单检索
		HashMap<String, String>[] materialBeans = dao.searchProgramlist(bean);
		if (materialBeans != null) {
			for (HashMap<String, String> itemMap : materialBeans) {
				AdvertBean advertbean = new AdvertBean();
				//节目单ID
				advertbean.setProgramlistId(itemMap.get("BILL_ID"));
				//节目单名称
				advertbean.setProgramlistName(itemMap.get("BILL_NAME"));
				//节目单描述
				advertbean.setProgramlistDescribe(itemMap.get("DESCRIPTION"));
				//节目单时长
				if (itemMap.get("PLAYTIME") != null && !"".equals(itemMap.get("PLAYTIME"))) {
					advertbean.setProgramlistPlayTime(Integer.parseInt(itemMap.get("PLAYTIME")));
				}
				//布局类型
				advertbean.setLayoutScreen(itemMap.get("SCREEN_NUM"));
				//创建用户
				advertbean.setOperator(itemMap.get("NAME"));
				//创建时间
				advertbean.setCreateTime(itemMap.get("OPERATETIME"));
				//审核情况
				advertbean.setAuditStatus(itemMap.get("STATUS"));
				resultBeans.add(advertbean);
			}
		}
		//检索结果保存
		req.setAttribute("resultGroupList", resultGroupBeans);
		req.setAttribute("resultProgramList", resultBeans);
	}
	
	/**
	 * 区域授权
	 * @param req 
	 * @return
	 */
	public String regionalAuth( HttpServletRequest req ){
		
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");
		
		// 用户ID
		String userId = user.getId();
		//组织ID
		String orgID = "";
		if (userId == null) {
			logger.debug("用户ID为空");
		}
		orgID = req.getParameter("orgID");
		if(StringUtils.isBlank(orgID)){
			orgID = user.getOrg_id();
		}
		if(StringUtils.isBlank(orgID)){
			logger.debug("组织ID为空");
		}
		try{
			
			List<HashMap> orgList = new OrgDealAction().getChildNodesByOrgId(Long.parseLong(orgID));
			req.setAttribute("orgName", getOrgNameByUserID(orgID));
			req.setAttribute("orgid", orgID);
			req.setAttribute("orgsList", buildOrgOption(orgList));
			HashMap<String, String>[] programList=programlistSearch(req);
			session.setAttribute("programList", programList);
			return "/jsp/advert/empower.jsp";
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		
	}
	
	/**
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public String getOrgNameByUserID(String orgID) throws Exception, SQLException{
		HashMap[] map = new OrgDealDAO().getCurOrgNodeByOrgId( Long.parseLong(orgID) );
		if(map != null && map.length > 0){
			return (String)map[0].get("ORG_NAME");
		}
		return "";
	}
	
	/**
	 * @param userOrgs
	 * @param orgCurrentID
	 * @return 
	 */
	public String buildOrgOption(List<HashMap> userOrgs){
		/*HashMap curOrgNode = null;
		long curOrgId = -1;
		long curParentOrgId = -1;*/
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		if(userOrgs!=null&&userOrgs.size()>0){
			/*System.out.println("--------------------组织节点详情---------------------");
			for(int i=0;i<userOrgs.size();i++){
				curOrgNode = (HashMap)userOrgs.get( i );
				curOrgId = Long.parseLong(curOrgNode.get( "ORG_ID" ).toString());
				orgName = curOrgNode.get( "ORG_NAME" ).toString();
				curParentOrgId = Long.parseLong(curOrgNode.get( "PARENT_ORG_ID" ).toString());
				System.out.println("父节点ID : "+ curParentOrgId + " --- 当前节点ID ： "+curOrgId+" --- 节点名称 ： "+orgName);
			}
			System.out.println("--------------------生成组织树---------------------");*/
			sb.append( "[ " );
			for (int i=0;i<userOrgs.size();i++){//userOrgs替换

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );

				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=2 ){
					sb.append( ", open:true " );
				}
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}

			}
			sb.append( " ];" );
			
		}
		return sb.toString();
	}
	/**
	 * 节目单检索
	 * @param req
	 * @return list
	 */
	public HashMap<String, String>[] programlistSearch(HttpServletRequest req )throws Exception{
		String FUNCTION_NAME = "programlistSearch() ";
		logger.info( FUNCTION_NAME + "start" );
		
		HttpSession session = req.getSession(false);
		
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		AdvertBean bean = new AdvertBean();
		bean.setSubOrgIdArr(orgIdStr);
		AdvertDao dao = new AdvertDao();
		
		HashMap<String, String>[] programList = dao.programlistSearchDAO(bean);
		
		return programList;
		
		
	}
	
	/**
	 * 通过节目单ID检索出布局信息
	 * @param req
	 * @return String
	 */
	public String getLayoutInfo(HttpServletRequest req )throws Exception{
		String FUNCTION_NAME = "getLayoutName() ";
		logger.info( FUNCTION_NAME + "start" );
		
		//取得布局ID
		int billId = Integer.valueOf(req.getParameter("billId"));
		
		AdvertDao dao = new AdvertDao();
		
		String layoutInfo = "";
		
		HashMap<String, String>[] layoutBeans = dao.getLayoutInfoDAO(billId);
		
		if( layoutBeans !=null && layoutBeans.length >0){
				layoutInfo = layoutBeans[0].get("LAYOUT_ID") + "," + 
						layoutBeans[0].get("LAYOUT_NAME") + "," + layoutBeans[0].get("SCREEN_NUM");
		}
		
		return layoutInfo;
		
		
	}
	
	/**
	 * 区域授权
	 * @param req
	 * @return list
	 */
	public String regionalAuthinsert(HttpServletRequest req )throws Exception{
		String FUNCTION_NAME = "regionalAuthinsert() ";
		logger.info( FUNCTION_NAME + "start" );
		
		AdvertDao dao = new AdvertDao();
		AdvertBean bean = new AdvertBean();
		//节目单ID
		bean.setProgramlistId(req.getParameter("billId"));
		//组织ID
		bean.setOrgId(req.getParameter("orgId"));
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		//检索节目单表信息
		HashMap<String, String>[] plBeans = dao.searchProgramlist(bean);
		//布局ID
		bean.setLayoutId(plBeans[0].get("LAYOUT_ID"));
		//检索布局表信息
		HashMap<String, String>[] layoutBeans = dao.searchLayout(bean);
		
		/**复制布局表,布局元素表 */
		//布局名称
		bean.setLayoutName(layoutBeans[0].get("LAYOUT_NAME"));
	    //布局描述
		bean.setLayoutDescribe(layoutBeans[0].get("DESCRIPTION"));
		//布局类型
		bean.setLayoutScreen(layoutBeans[0].get("SCREEN_NUM"));
	    //布局分辨率宽
		if (layoutBeans[0].get("LAYOUT_WIDTH") != null) {
			bean.setLayoutwidth(Integer.parseInt(layoutBeans[0].get("LAYOUT_WIDTH")));
		}
		//布局分辨率高
		if (layoutBeans[0].get("LAYOUT_HEIGHT") != null) {
			bean.setLayoutHeight(Integer.parseInt(layoutBeans[0].get("LAYOUT_HEIGHT")));
		}
		//分屏数
		bean.setScreenNum(Integer.parseInt(layoutBeans[0].get("SCREEN_NUM")));
		HashMap<String, String>[] layoutDetail = dao.searchLayoutDetail(bean);
		int[] screenStartX = new int[layoutDetail.length];
		int[] screenStartY = new int[layoutDetail.length];
		int[] screenEndX = new int[layoutDetail.length];
		int[] screenEndY = new int[layoutDetail.length];
		String[] screenName = new String[layoutDetail.length];
		for (int i = 0; i < layoutDetail.length; i++) {
			//分屏开始横坐标
			if (layoutDetail[i].get("START_X") != null) {
				screenStartX[i]= Integer.parseInt(layoutDetail[i].get("START_X"));
			}
			//分屏开始纵坐标
			if (layoutDetail[i].get("START_Y") != null) {
				screenStartY[i]= Integer.parseInt(layoutDetail[i].get("START_Y"));
			}
			//分屏结束横坐标
			if (layoutDetail[i].get("END_X") != null) {
				screenEndX[i]= Integer.parseInt(layoutDetail[i].get("END_X"));
			}
			//分屏结束纵坐标
			if (layoutDetail[i].get("END_Y") != null) {
				screenEndY[i]= Integer.parseInt(layoutDetail[i].get("END_Y"));
			}
			//分屏名称
			if (layoutDetail[i].get("WINDOW_NAME") != null) {
				screenName[i]= layoutDetail[i].get("WINDOW_NAME");
			}
		}
		//分屏开始横坐标
		bean.setScreenStartX(screenStartX);
		//分屏开始纵坐标
		bean.setScreenStartY(screenStartY);
		//分屏结束横坐标
		bean.setScreenEndX(screenEndX);
		//分屏结束纵坐标
		bean.setScreenEndY(screenEndY);
		//分屏名称
		bean.setScreenName(screenName);
		
		bean.setOperator(user.getId());
		String layoutId = dao.insertLayout(bean);
		
		/**复制节目单表,节目单详细表 */
		//节目单名称
		bean.setProgramlistName(plBeans[0].get("BILL_NAME"));
	    //节目单描述
		bean.setProgramlistDescribe(plBeans[0].get("DESCRIPTION"));
		//节目单时长
		if (plBeans[0].get("PLAYTIME") != null && !"".equals(plBeans[0].get("PLAYTIME"))) {
			bean.setProgramlistPlayTime(Integer.parseInt(plBeans[0].get("PLAYTIME")));
		}
		//布局ID
		bean.setLayoutId(layoutId);
		//检索复制后新的布局详细信息
		HashMap<String, String>[] newLayoutDetail = dao.searchLayoutDetail(bean);
		//检索节目单素材信息
		HashMap<String, String>[] plDetail = dao.searchProgramlistDetail(bean);
		//设置节目单素材信息
		if (plDetail != null && plDetail.length > 0) {
			//素材List
			List<AdvertBean> programlistMaterials = new ArrayList<AdvertBean>();
			//设置素材,素材所在布局,素材播放时长,素材显示级别
			for (int i = 0; i < plDetail.length; i++) {
				AdvertBean materialListBean = new AdvertBean();
				materialListBean.setMaterialId(plDetail[i].get("MEDIA_ID"));
				if (plDetail[i].get("PLAYTIME") == null || "".equals(plDetail[i].get("PLAYTIME"))) {
					materialListBean.setMaterialPlayTime(0);
				} else {
					materialListBean.setMaterialPlayTime(Integer.parseInt(plDetail[i].get("PLAYTIME")));
				}
				if (plDetail[i].get("MEDIA_LEVEL") == null || "".equals(plDetail[i].get("MEDIA_LEVEL"))) {
					materialListBean.setMaterialLevel(0);
				} else {
					materialListBean.setMaterialLevel(Integer.parseInt(plDetail[i].get("MEDIA_LEVEL")));
				}
				String windowId = "";
				for (HashMap<String, String> itemMap : newLayoutDetail) {
					String windowNo = (String) itemMap.get("WINDOW_NO");
					if (windowNo.equals(plDetail[i].get("WINDOW_NO"))) {
						windowId = (String) itemMap.get("WINDOW_ID");
						break;
					}
				}
				materialListBean.setWindowId(windowId);
				programlistMaterials.add(materialListBean);
			}
			bean.setProgramlistMaterials(programlistMaterials);
		}
		//操作者
		bean.setOperator(user.getId());
		String programlistId = dao.insertProgramlist(bean);
		
		
		String result = "";
		if (programlistId == null) {
			result = "failed";
		} else {
			result = "success";
		}
		//节目单ID
		bean.setProgramlistId(programlistId);
		//向FTP上传节目单文件
		initBillFileByBillID(bean);
		return result;
	}
	
	/**
	 * 节目单发布-按组织发布
	 * @param BILL_ID
	 * @return String
	 */
	public String programSend(HttpServletRequest req )throws Exception{
		
		String FUNCTION_NAME = "programSend() ";
		logger.info( FUNCTION_NAME + "start" );
		AdvertBean bean = new AdvertBean();
		//节目单,字幕(1:节目单 2:字幕)
		String advertType = req.getParameter("advert_type");
		String result = "";
		String sel_nodes_info = req.getParameter("sel_nodes_info");
		String sel_nodes_type = req.getParameter("sel_nodes_type");
		
		//端机类型
		String machingType = req.getParameter("machingType");
		String[] machingTypes = null;
		if (machingType.contains(",")) {
			machingTypes = machingType.split(",");
		} else {
			machingTypes = new String[1];
			machingTypes[0] = machingType;
		}
		bean.setMachingType(machingTypes);
		
		String[] machineId = null;
		AdvertDao dao = new AdvertDao();
		if("org".equals(sel_nodes_type)){
			//选中组织
			bean.setOrgId(sel_nodes_info);
			
			//根据组织ID和端机类型取得端机ID
			HashMap<String, String>[] beanMaps = dao.getMachineIdByOrgIdAndType(bean);
			if (beanMaps != null) {
				machineId = new String[beanMaps.length];
				for (int i = 0; i < beanMaps.length; i++) {
					machineId[i] = beanMaps[i].get("MACHINE_ID");
				}
			}
		} else {
			//sel_nodes_type = mac
			//选中端机
			if (!"".equals(sel_nodes_info)) {
				if (sel_nodes_info.contains(",")) {
					machineId = sel_nodes_info.split(",");
				} else {
					machineId = new String[1];
					machineId[0] = sel_nodes_info;
				}
			}
		}
		
		bean.setmachineId(machineId);
		//播放屏幕
		bean.setScreenType(req.getParameter("screenType"));
		//临时广告
		bean.setTempAds(req.getParameter("tempAds"));
		//立刻下发
		bean.setSendType(req.getParameter("sendType"));
		//下发时间(节目单)
		if (req.getParameter("sendTime") != null && !"".equals(req.getParameter("sendTime").trim())) {
			bean.setSendTime(req.getParameter("sendTime") + " " + req.getParameter("sendTime_hh") + req.getParameter("sendTime_mi"));
		}
		//结束播放时间(节目单)
		if (req.getParameter("valueTime") != null && !"".equals(req.getParameter("valueTime").trim())) {
			bean.setValueTime(req.getParameter("valueTime") + " " + req.getParameter("valueTime_hh") + req.getParameter("valueTime_mi"));
		}
		//下发时间(字幕)
		if (req.getParameter("sendTime2") != null && !"".equals(req.getParameter("sendTime2").trim())) {
			bean.setSendTime2(req.getParameter("sendTime2") + " " + req.getParameter("sendTime2_hh") + req.getParameter("sendTime2_mi"));
		}
		//结束播放时间(字幕)
		if (req.getParameter("valueTime2") != null && !"".equals(req.getParameter("valueTime2").trim())) {
			bean.setValueTime2(req.getParameter("valueTime2") + " " + req.getParameter("valueTime2_hh") + req.getParameter("valueTime2_mi"));
		}
		//下载控制
		bean.setControlType(req.getParameter("controlType"));
		//节目单组ID
		bean.setProgramlistGroupId(req.getParameter("programlistGroupId"));
		//节目单ID
		bean.setProgramlistId(req.getParameter("billId"));
		//字幕ID
		bean.setSubtitlesId(req.getParameter("subtitlesId"));
		
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		String operator = user.getId();
		bean.setOperator(operator);
		if ("1".equals(advertType)) {
			//节目单组详细信息检索
			if (bean.getProgramlistGroupId() != null && !"".equals(bean.getProgramlistGroupId())) {
				//根据节目单组下发
				HashMap<String, String>[] programlistBeans = dao.searchProgramlistByGroupId(bean);
				for (HashMap<String, String> itemMap : programlistBeans) {
					bean.setProgramlistId(itemMap.get("BILL_ID"));
					int[] re = dao.programSendDAO(bean);
					if(re == null) {
						result = "failed";
					} else {
						result = "success";
					}
				}
			} else {
				//根据节目单下发
				bean.setProgramlistId(req.getParameter("billId"));
				int[] re = dao.programSendDAO(bean);
				if(re == null) {
					result = "failed";
				} else {
					result = "success";
				}
			}
			
			//向FTP上传节目单发布文件
			String sendTime = "";
			if ("1".equals(bean.getSendType())) {
				sendTime = DateUtil.getCurrentDate("yyyy-MM-dd hhmm");
			} else {
				sendTime = bean.getSendTime();
			}
			for (String item : machineId) {
				uploadIssueBillListFile(Long.parseLong(item), sendTime, bean.getControlType());
			}
		}else {
			int[] re = dao.subtitlesSendDAO(bean);
			if (re != null) {
				result = "success";
			} else {
				result = "failed";
			}
		}
		req.setAttribute("advert_type", advertType);
		
		logger.info( FUNCTION_NAME + "end" );
		
		return result;
		
	}
	
	/**
	 * 节目单发布-按端机发布
	 * @param BILL_ID
	 * @return String
	 */
	public String programSendByMachine(HttpServletRequest req )throws Exception{
		
		String FUNCTION_NAME = "programSend() ";
		logger.info( FUNCTION_NAME + "start" );
		AdvertBean bean = new AdvertBean();
		//节目单,字幕(1:节目单 2:字幕)
		String advertType = req.getParameter("advert_type");
		String result = "";
		String sel_nodes_info = req.getParameter("sel_nodes_info");
//		//组织ID
//		bean.setOrgId(sel_nodes_info);
//		//端机类型
//		String machingType = req.getParameter("machingType");
//		String[] machingTypes = null;
//		if (machingType.contains(",")) {
//			machingTypes = machingType.split(",");
//		} else {
//			machingTypes = new String[1];
//			machingTypes[0] = machingType;
//		}
//		bean.setMachingType(machingTypes);
		//端机ID
		String[] machineId = null;
		if (!"".equals(sel_nodes_info)) {
			if (sel_nodes_info.contains(",")) {
				machineId = sel_nodes_info.split(",");
			} else {
				machineId = new String[1];
				machineId[0] = sel_nodes_info;
			}
		}
		bean.setmachineId(machineId);
		//播放屏幕
		bean.setScreenType(req.getParameter("screenType"));
		//临时广告
		bean.setTempAds(req.getParameter("tempAds"));
		//立刻下发
		bean.setSendType(req.getParameter("sendType"));
		//下发时间(节目单)
		if (req.getParameter("sendTime") != null && !"".equals(req.getParameter("sendTime").trim())) {
			bean.setSendTime(req.getParameter("sendTime") + " " + req.getParameter("sendTime_hh") + req.getParameter("sendTime_mi"));
		}
		//结束播放时间(节目单)
		if (req.getParameter("valueTime") != null && !"".equals(req.getParameter("valueTime").trim())) {
			bean.setValueTime(req.getParameter("valueTime") + " " + req.getParameter("valueTime_hh") + req.getParameter("valueTime_mi"));
		}
		//下发时间(字幕)
		if (req.getParameter("sendTime2") != null && !"".equals(req.getParameter("sendTime2").trim())) {
			bean.setSendTime2(req.getParameter("sendTime2") + " " + req.getParameter("sendTime2_hh") + req.getParameter("sendTime2_mi"));
		}
		//结束播放时间(字幕)
		if (req.getParameter("valueTime2") != null && !"".equals(req.getParameter("valueTime2").trim())) {
			bean.setValueTime2(req.getParameter("valueTime2") + " " + req.getParameter("valueTime2_hh") + req.getParameter("valueTime2_mi"));
		}
		//下载控制
		bean.setControlType(req.getParameter("controlType"));
		//节目单组ID
		bean.setProgramlistGroupId(req.getParameter("programlistGroupId"));
		//节目单ID
		bean.setProgramlistId(req.getParameter("billId"));
		//字幕ID
		bean.setSubtitlesId(req.getParameter("subtitlesId"));
		
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		String operator = user.getId();
		bean.setOperator(operator);
		AdvertDao dao = new AdvertDao();
		if ("1".equals(advertType)) {
			//节目单组详细信息检索
			if (bean.getProgramlistGroupId() != null && !"".equals(bean.getProgramlistGroupId())) {
				//根据节目单组下发
				HashMap<String, String>[] programlistBeans = dao.searchProgramlistByGroupId(bean);
				for (HashMap<String, String> itemMap : programlistBeans) {
					bean.setProgramlistId(itemMap.get("BILL_ID"));
					int[] re = dao.programSendDAO(bean);
					if(re == null) {
						result = "failed";
					} else {
						result = "success";
					}
				}
			} else {
				//根据节目单下发
				bean.setProgramlistId(req.getParameter("billId"));
				int[] re = dao.programSendDAO(bean);
				if(re == null) {
					result = "failed";
				} else {
					result = "success";
				}
			}
			
			//向FTP上传节目单发布文件
			String sendTime = "";
			if ("1".equals(bean.getSendType())) {
				sendTime = DateUtil.getCurrentDate("yyyy-MM-dd hhmm");
			} else {
				sendTime = bean.getSendTime();
			}
			for (String item : machineId) {
				uploadIssueBillListFile(Long.parseLong(item), sendTime, bean.getControlType());
			}
		}else {
			int[] re = dao.subtitlesSendDAO(bean);
			if (re != null) {
				result = "success";
			} else {
				result = "failed";
			}
		}
		req.setAttribute("advert_type", advertType);
		
		logger.info( FUNCTION_NAME + "end" );
		
		return result;
		
	}
	
	/**
	 * 清除端机节目单列表-按组织/端机
	 * @param BILL_ID
	 * @return String
	 */
	public String updateProgramSend(HttpServletRequest req )throws Exception{
		String FUNCTION_NAME = "updateProgramSend() ";
		logger.info( FUNCTION_NAME + "start" );
		AdvertBean bean = new AdvertBean();
		//节目单,字幕(1:节目单 2:字幕)
		String advertType = req.getParameter("advert_type");
		String result = "";
		String sel_nodes_info = req.getParameter("sel_nodes_info");
		String sel_nodes_type = req.getParameter("sel_nodes_type");
		AdvertDao dao = new AdvertDao();
		String[] machineId = null;
		if("org".equals(sel_nodes_type)){
			//按组织
			bean.setOrgId(sel_nodes_info);
			//根据组织ID和端机类型取得端机ID
			HashMap<String, String>[] beanMaps = dao.getMachineIdByOrgIdAndType(bean);
			if (beanMaps != null) {
				machineId = new String[beanMaps.length];
				for (int i = 0; i < beanMaps.length; i++) {
					machineId[i] = beanMaps[i].get("MACHINE_ID");
				}
			}
		} else {
			//按端机
			if (!"".equals(sel_nodes_info)) {
				if (sel_nodes_info.contains(",")) {
					machineId = sel_nodes_info.split(",");
				} else {
					machineId = new String[1];
					machineId[0] = sel_nodes_info;
				}
			}
		}
		bean.setmachineId(machineId);
		if(machineId.length > 0){
			if ("1".equals(advertType)) {
				if(dao.isProgramSend(bean)){
					int re = dao.updateProgramSend(bean);
					if(re == 0) {
						result = "failed";
					} else {
						result = "success";
					}
				} else {
					result = "success";
				}
			}else {
				if(dao.isSubtitlesSend(bean)){
					int re = dao.updateSubtitlesSend(bean);
					if(re == 0) {
						result = "failed";
					} else {
						result = "success";
					}
				} else {
					result = "success";
				}
			}
		} else {
			result = "success";
		}
		
		req.setAttribute("advert_type", advertType);
		
		logger.info( FUNCTION_NAME + "end" );
		
		return result;
	}
	
	/**
	 * 清除端机节目单列表-按端机
	 * @param BILL_ID
	 * @return String
	 */
	public String updateProgramSendByMachine(HttpServletRequest req )throws Exception{
		String FUNCTION_NAME = "updateProgramSend() ";
		logger.info( FUNCTION_NAME + "start" );
		AdvertBean bean = new AdvertBean();
		//节目单,字幕(1:节目单 2:字幕)
		String advertType = req.getParameter("advert_type");
		String result = "";
		String sel_nodes_info = req.getParameter("sel_nodes_info");
		//端机ID
		String[] machineId = null;
		if (!"".equals(sel_nodes_info)) {
			if (sel_nodes_info.contains(",")) {
				machineId = sel_nodes_info.split(",");
			} else {
				machineId = new String[1];
				machineId[0] = sel_nodes_info;
			}
		}
		bean.setmachineId(machineId);
		
		AdvertDao dao = new AdvertDao();
//		//组织ID
//		bean.setOrgId(sel_nodes_info);
//		//根据组织ID和端机类型取得端机ID
//		HashMap<String, String>[] beanMaps = dao.getMachineIdByOrgIdAndType(bean);
//		if (beanMaps != null) {
//			machineId = new String[beanMaps.length];
//			for (int i = 0; i < beanMaps.length; i++) {
//				machineId[i] = beanMaps[i].get("MACHINE_ID");
//			}
//		}
//		bean.setmachineId(machineId);
		if ("1".equals(advertType)) {
			int re = dao.updateProgramSend(bean);
			if(re == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		}else {
			int re = dao.updateSubtitlesSend(bean);
			if(re == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		}
		req.setAttribute("advert_type", advertType);
		
		logger.info( FUNCTION_NAME + "end" );
		
		return result;
	}
	
	/**
	 * 创建节目单时向FTP上传节目单文件
	 * @param billID 节目单ID
	 */
	public void initBillFileByBillID(AdvertBean bean){

		String FUNCTION_NAME = "initBillFileByBillID";
		AdvertDao advertDao = new AdvertDao();
		try{
			String uploadBillListFilePath = LocalProperties.getProperty("FTP_UPLOAD_FILE_PATH_PLAYBILL");	
			HashMap[] billList = advertDao.getProgramlistInfo(bean);
			//向节目单.init文件中写入每个节目单信息
			uploadBillFileToFTP(bean, uploadBillListFilePath, billList);
			//更新节目单表BILL_FILE_LOCATION字段
			advertDao.updateBillFileLocation(bean);
		} catch (LocalPropertiesException e) {
			e.printStackTrace();
			logger.error(FUNCTION_NAME+"FTP的路径为空!"+e);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(FUNCTION_NAME+"IO异常!"+e);
		}finally{
			logger.info(FUNCTION_NAME+"end");
		}
	}
	
	/**
	 * 节目单下发列表文件生成及下发命令通知
	 */
	public boolean uploadIssueBillListFile(long machineID, String sendTime, String control){
		String FUNCTION_NAME = "uploadIssueBillListFile";
		
		boolean ret = false;
		
		//根据machineID获取机器节目单下发列表
		AdvertDao advertDao = new AdvertDao();
		HashMap[] billIssuedList = advertDao.getBillIssuedList(machineID);
			try {	
				String updateBillListFilePath = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_PATH_PLAYBILL");	
				logger.info(FUNCTION_NAME+"FTP节目单下发目录: "+updateBillListFilePath);
				
				String suffix = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");
				logger.info(FUNCTION_NAME+"升级文件后缀: "+suffix);
				
				if (StringUtils.isBlank(updateBillListFilePath)) {
					logger.error(FUNCTION_NAME+"FTP的路径为空!");
					return false;
				}
				String mac = advertDao.getMacByMachineID( machineID );
				logger.info(FUNCTION_NAME+"用户选择的MAC: "+mac);

				//STEP2.2: 向FTP上传文件
				uploadIniBillListFileToFTP( mac, updateBillListFilePath, billIssuedList);
				
				//STEP2.1: 进行指令更新
				int updateResult = advertDao.UpdateCommandManagement( machineID, "1", 
						FileTools.buildFullFilePath(updateBillListFilePath, mac + suffix), 21 ,sendTime);
				if ("2".equals(control)) {
					String newSendTime = DateUtil.setNewDay(sendTime, "yyyy-MM-dd hhmm", Calendar.MINUTE, 1);
					//下载完成重启播放
					advertDao.UpdateCommandManagement( machineID, "1", 
							FileTools.buildFullFilePath(updateBillListFilePath, mac + suffix), 5 , newSendTime);
				} else if ("3".equals(control)) {
					String newSendTime = DateUtil.setNewDay(sendTime, "yyyy-MM-dd hhmm", Calendar.MINUTE, 1);
					//下载完成关闭端机
					advertDao.UpdateCommandManagement( machineID, "1", 
							FileTools.buildFullFilePath(updateBillListFilePath, mac + suffix), 4 , newSendTime);
				}
			} catch (LocalPropertiesException e) {
				e.printStackTrace();
				logger.error(FUNCTION_NAME+"FTP的路径为空!"+e);
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				logger.error(FUNCTION_NAME+"IO异常!"+e);
				return false;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(FUNCTION_NAME+"IO异常!"+e);
				return false;
			} finally{
				logger.info(FUNCTION_NAME+"end");
			}


		return ret;
	}	
	/**
	 * 生成节目单下发列表文件mac.ini,并上传至FTP指定目录中
	 * <pre>
	 * Desc  
	 * @param packagedsArray
	 * @param updateZipFilePath
	 * @param mac
	 * @throws IOException
	 * </pre>
	 */
	private void uploadIniBillListFileToFTP( String mac, String updateBillListFilePath, HashMap[] billIssuedList)
			throws IOException {
		
		String FUNCTION_NAME = "uploadIniBillListFileToFTP";
		String macIniLocalTmpPath = LocalProperties.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_PLAYBILL");
		String suffix = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");
		logger.info(FUNCTION_NAME+"升级文件本地临时目录: "+macIniLocalTmpPath);
		logger.info(FUNCTION_NAME+"升级文件后缀: "+suffix);
		
		//STEP1: 生成对应的临时目录
		FileTools.mkdirs(macIniLocalTmpPath);
		
		//STEP2: 
		//向ini文件中写入数据 节目单列表
		writePackageInfoToIniFile(billIssuedList, FileTools.buildFullFilePath(macIniLocalTmpPath, mac + suffix));
	
		//STEP3: 上传文件列表
		FtpUtils.uploadFile(FileTools.getSubFiles(macIniLocalTmpPath), updateBillListFilePath);
		
		//STEP4: 移除临时文件
		FileTools.removeSubFiles(macIniLocalTmpPath);
	}
	
	/**
	 * 向Mac.ini文件中写入信息.每行用回车转行.新生成的文件保存至path目录中.
	 * <pre>
	 * Desc  
	 * @param packagedsArray
	 * @param path
	 * @return
	 * @throws IOException
	 * @author PSET
	 * @refactor PSET
	 * </pre>
	 */
	private void writePackageInfoToIniFile(HashMap[] billList, String path) throws IOException {
		String FUNCTION_NAME = "writePackageInfoToIniFile";
		logger.info(FUNCTION_NAME+" 临时文件目录: "+path);
		FileWriter fw = new FileWriter(new File(path));
		BufferedWriter bw = new BufferedWriter(fw);
//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		
		for (HashMap packageMap : billList) {
			String billFileLocation = (String)packageMap.get( "BILL_FILE_LOACTION" ) ;
			if( null != billFileLocation ){

				int playTime = Integer.parseInt( (String)packageMap.get( "PLAYTIME" ) );
				int playBillType = Integer.parseInt( (String)packageMap.get( "BILL_TYPE" ) );
				Date validTime = StringUtils.getYyyyMMddHHmmss( (String)packageMap.get( "VALID_TIME" ) );
				String strValidTime = StringUtils.getYyyyMMddHHmmss( validTime );
				String playbill_action=(String)packageMap.get("PLAYBILL_ACTION");
				if(playbill_action==null){
					playbill_action="0";
				}
				bw.write(billFileLocation +","+ playTime +","+ strValidTime +","+ playBillType+","+ playbill_action);
				bw.newLine();
			}
		}
		fw.flush();
//		FileTools.closeWriter(bw);
		bw.close();
		fw.close();
	}
	
	/**
	 * 生成节目单生成时创建.ini文件,并上传至FTP指定目录中
	 * <pre>
	 * Desc  
	 * @param packagedsArray
	 * @param updateZipFilePath
	 * @param mac
	 * @throws IOException
	 * </pre>
	 */
	private void uploadBillFileToFTP( AdvertBean bean, String updateBillListFilePath, HashMap[] billList)
			throws IOException {
		
		String FUNCTION_NAME = "uploadBillFileToFTP";
		String billTmpPath = LocalProperties.getProperty("FTP_DOWNLOAD_TMP_FILE_PATH_PLAYBILL");
		String suffix = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_SUFFIX");
		logger.info(FUNCTION_NAME+"节目单文件本地临时目录: "+billTmpPath);
		logger.info(FUNCTION_NAME+"节目单文件后缀: "+suffix);
		
		//STEP1: 生成对应的临时目录
		FileTools.mkdirs(billTmpPath);
		
		//STEP2: 
		//写入布局信息
		writeLayoutFile(bean, FileTools.buildFullFilePath(billTmpPath, "layout_" + bean.getLayoutId() + suffix));
		//写入节目单信息
		writeBillFile(billList, FileTools.buildFullFilePath(billTmpPath, "bill_" + bean.getProgramlistId() + suffix),
				FileTools.buildFullFilePath(updateBillListFilePath, "layout_" + bean.getLayoutId() + suffix));
		List<File> fileList = new ArrayList<File>();
		File file1 = new File(FileTools.buildFullFilePath(billTmpPath, "layout_" + bean.getLayoutId() + suffix));
		File file2 = new File(FileTools.buildFullFilePath(billTmpPath, "bill_" + bean.getProgramlistId() + suffix));
		//设置节目单文件路径,
		bean.setBill_file_location(FileTools.buildFullFilePath(updateBillListFilePath, "bill_" + bean.getProgramlistId() + suffix));
		
		fileList.add(file1);
		fileList.add(file2);
		//STEP3: 上传文件列表
		FtpUtils.uploadFile(fileList, updateBillListFilePath);
		
		//STEP4: 移除临时文件
		FileTools.removeSubFiles(billTmpPath);
	}
	
	/**
	 * 向节目单.ini文件中写入信息.每行用回车转行.新生成的文件保存至path目录中.
	 * <pre>
	 * Desc  
	 * @param packagedsArray
	 * @param path
	 * @return
	 * @throws IOException
	 * @author PSET
	 * @refactor PSET
	 * </pre>
	 */
	private void writeLayoutFile(AdvertBean bean, String path) throws IOException {
		String FUNCTION_NAME = "writeLayoutFile";
		logger.info(FUNCTION_NAME+" 临时文件目录: "+path);
		FileWriter fw = new FileWriter(new File(path));
		BufferedWriter bw = new BufferedWriter(fw);
//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		
		AdvertDao advertDao = new AdvertDao();
		HashMap<String, String>[] billList = null;
		//取得布局信息
		billList = advertDao.getLayoutDetail(bean);
		for (HashMap<String, String> itemMap : billList) {
				String startX = itemMap.get("START_X");
				String startY = itemMap.get("START_Y");
				String endX = itemMap.get("END_X");
				String endY = itemMap.get("END_Y");
				bw.write(startX +","+ startY);
				bw.newLine();
				bw.write(endX +","+ endY);
				bw.newLine();
		}
		fw.flush();
//		FileTools.closeWriter(bw);
		bw.close();
		fw.close();
	}
	
	/**
	 * 向节目单.ini文件中写入信息.每行用回车转行.新生成的文件保存至path目录中.
	 * <pre>
	 * Desc  
	 * @param packagedsArray
	 * @param path
	 * @return
	 * @throws IOException
	 * @author PSET
	 * @refactor PSET
	 * </pre>
	 */
	private void writeBillFile(HashMap[] billList, String path, String layoutPath) throws IOException {
		String FUNCTION_NAME = "writeBillFile";
		logger.info(FUNCTION_NAME+" 临时文件目录: "+path);
		FileWriter fw = new FileWriter(new File(path));
		BufferedWriter bw = new BufferedWriter(fw);
//		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path)));
		bw.write(layoutPath);
		bw.newLine();
		for (HashMap<String, String> itemMap : billList) {
			String windowNo = itemMap.get("WINDOW_NO");
			String materialPath = itemMap.get("MEDIA_URL");
			String linkUrl = itemMap.get("LINK_URL");

			bw.write(windowNo +","+ materialPath +","+ linkUrl);
			bw.newLine();
		}
		fw.flush();
//		FileTools.closeWriter(bw);
		bw.close();
		fw.close();
	}
	
	/**
	 * 
	 * @param userOrgs
	 * @param showMachineFlg 是否显示组织下的端机
	 * @return
	 * @throws SQLException
	 */
	protected String buildMachineInOrg(List<HashMap> userOrgs, boolean showMachineFlg) throws SQLException{
		
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		String treeStr = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		List<Long> orgIDList = new ArrayList<Long>();
		List<MachineBean> lBean = new ArrayList<MachineBean>();
		List<MachineBean> machineOrgList =  new MonitorDAO().getRelationlistBetweenMachineInfoWithOrg();
		if(userOrgs!=null&&userOrgs.size()>0){
			sb.append( "[ " );
			for (int i=0;i<userOrgs.size();i++){

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );

				orgIDList.add(orgId);
				
				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=1 ){
					sb.append( ", open:true " );
				}
				sb.append(", \"mactype\":\"org\"");
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}
			}
			if (showMachineFlg) {
				sb.append( "," );
				//添加组织下端机ID NAME
				int n = 0;
				for(int k = 0; k < orgIDList.size(); k++){
					orgId = orgIDList.get(k);
					lBean = getMachineByOrgID(orgId, machineOrgList);
					for(n = 0; n < lBean.size(); n++){
						sb.append( "{ \"id\": " );
						//为避免与组织树中id重复，在本次端机循环中将   "999999"+端机ID 作为id
						//注：组织ID最大不能超过999999,否则资源数将显示错误
						sb.append( "\"" + Define4Machine.TREE_MACID_PREFIX+lBean.get(n).getMachineId() + "\"," );
						sb.append( " \"pId\": " );
						sb.append( orgId + "," );
						sb.append( " \"name\": " );
						sb.append( "\"" + lBean.get(n).getMachineMark() + "\"" );
						sb.append(", \"mactype\":\"mac\"");
						sb.append( "}" );
	
						if(n<lBean.size()-1){
							sb.append( "," );
						}
					}
					if(n != 0 && k<orgIDList.size()-1){
						sb.append( "," );
					}
				}
			}
			treeStr = sb.toString();
			//当组织下没有任何端机时过滤结尾逗号
			if(treeStr.charAt(treeStr.length()-1) == ','){
				treeStr = treeStr.substring(0, treeStr.length()-1);
			}
			treeStr += " ];" ;
			
		}
		return treeStr;
	}
	/**
	 * 取得机器列表中与orgID相等的机器列表
	 * @param orgID
	 * @param list
	 * @return
	 */
	private List<MachineBean> getMachineByOrgID(long orgID, List<MachineBean> list){
		List<MachineBean> beanList = new ArrayList<MachineBean>();
		for(int m = 0; m<list.size(); m++){
			if(orgID == list.get(m).getOrgID()){
				beanList.add(list.get(m));
			}
		}
		return beanList;
	}
	
	/**
	 * 
	 * @param req
	 * @param showMachineFlg 是否显示组织下的端机
	 * @return
	 */
	public String getMachinTree(HttpServletRequest req, boolean showMachineFlg){
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");
		
		// 用户ID
		String userId = user.getId();
		//组织ID
		String orgID = "";
		if (userId == null) {
			logger.debug("用户ID为空");
		}
		orgID = req.getParameter("orgID");
		if(StringUtils.isBlank(orgID)){
			orgID = user.getOrg_id();
		}
		if(StringUtils.isBlank(orgID)){
			logger.debug("组织ID为空");
		}
		try{
			
			List<HashMap> orgList = new OrgDealAction().getChildNodesByOrgId(Long.parseLong(orgID));
			req.setAttribute("orgName", getOrgNameByUserID(orgID));
			req.setAttribute("orgid", orgID);
			req.setAttribute("machinList", buildMachineInOrg(orgList, showMachineFlg));
			req.setAttribute("osTypes", getAllOSTypes());
			return "/jsp/advert/programSend.jsp";
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * 端机获取字幕列表
	 * @param BILL_ID
	 * @return String
	 */
	public String getSubtitlesList(HttpServletRequest req )throws Exception{
		String FUNCTION_NAME = "getSubtitlesList() ";
		logger.info( FUNCTION_NAME + "start" );
		AdvertBean bean = new AdvertBean();
		//节目单,字幕(1:节目单 2:字幕)
		String advertType = req.getParameter("advert_type");
		bean.setAdvert_type(advertType);
		String result = "";
		//端机ID
		String mac = req.getParameter("mac");
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] subtitlesList = dao.getSubtitlesList(mac);
		if (subtitlesList != null && subtitlesList.length > 0) {
			StringBuffer rollWord = new StringBuffer();
			StringBuffer wordStyle = new StringBuffer();
			for (HashMap<String, String> itemMap : subtitlesList) {
				//滚动文字
				rollWord.append(itemMap.get("SUBTITLES_CONTENT"));
			}
			//背景颜色
			if (subtitlesList[0].get("BACKGROUND_COLOR") != null && !"".equals(subtitlesList[0].get("BACKGROUND_COLOR"))) {
				wordStyle.append("background-color: ");
				wordStyle.append(subtitlesList[0].get("BACKGROUND_COLOR"));
				wordStyle.append(";");
			}
			//字体颜色
			if (subtitlesList[0].get("FONT_COLOR") != null && !"".equals(subtitlesList[0].get("FONT_COLOR"))) {
				wordStyle.append("color: ");
				wordStyle.append(subtitlesList[0].get("FONT_COLOR"));
				wordStyle.append(";");
			}
			//字体属性
			String fontStyle = subtitlesList[0].get("FONT_PROPERTIES");
			if (fontStyle != null && fontStyle.contains(";")) {
				fontStyle = fontStyle.replace(";", " ");
			}
			wordStyle.append("font: ");
			wordStyle.append(fontStyle);
			wordStyle.append(";");
			//高
			if (subtitlesList[0].get("HEIGHT") != null && !"".equals(subtitlesList[0].get("HEIGHT"))) {
				wordStyle.append("height: ");
				wordStyle.append(subtitlesList[0].get("HEIGHT"));
				wordStyle.append(";");
			}
			//宽
			if (subtitlesList[0].get("WIDTH") != null && !"".equals(subtitlesList[0].get("WIDTH"))) {
				wordStyle.append("width: ");
				wordStyle.append(subtitlesList[0].get("WIDTH"));
				wordStyle.append(";");
			}
			//水平对齐
			if (subtitlesList[0].get("HORIZONTAL_ALIGN") != null && !"".equals(subtitlesList[0].get("HORIZONTAL_ALIGN"))) {
				wordStyle.append("text-align: ");
				if ("1".equals(subtitlesList[0].get("HORIZONTAL_ALIGN"))) {
					wordStyle.append("left");
				} else if ("2".equals(subtitlesList[0].get("HORIZONTAL_ALIGN"))) {
					wordStyle.append("center");
				} else if ("3".equals(subtitlesList[0].get("HORIZONTAL_ALIGN"))) {
					wordStyle.append("right");
				} else {
					wordStyle.append(subtitlesList[0].get("HORIZONTAL_ALIGN"));
				}
				wordStyle.append(";");
			}
			//垂直对齐
			if (subtitlesList[0].get("VERTICAL_ALIGN") != null && !"".equals(subtitlesList[0].get("VERTICAL_ALIGN"))) {
				wordStyle.append("vertical-align: ");
				if ("1".equals(subtitlesList[0].get("VERTICAL_ALIGN"))) {
					wordStyle.append("top");
				} else if ("2".equals(subtitlesList[0].get("VERTICAL_ALIGN"))) {
					wordStyle.append("middle");
				} else if ("3".equals(subtitlesList[0].get("VERTICAL_ALIGN"))) {
					wordStyle.append("bottom");
				} else {
					wordStyle.append(subtitlesList[0].get("VERTICAL_ALIGN"));
				}
				wordStyle.append(";");
			}
			//滚动延迟设定
			bean.setRoll_delay(subtitlesList[0].get("DELAY_TIME"));
			//滚动文字设定
			bean.setRoll_word(rollWord.toString());
			//文字样式设定
			bean.setWord_style(wordStyle.toString());
			//检索结果保存
			req.setAttribute("resultBean", bean);
		}
		
		logger.info( FUNCTION_NAME + "end" );
		
		return result;
	}
	
	/**
	 * 素材上传的视频文件生成缩略图
	 * @param BILL_ID
	 * @return String
	 */
	public void transVideoToIamge() throws Exception{
		String FUNCTION_NAME = "transVideoToIamge() ";
		logger.info( FUNCTION_NAME + " start" );
		if (fileList == null || fileList.size() == 0) {
			return;
		}
		String batFilePath = LocalProperties.getProperty("TH_FFMPEG_BAT_PATH");
		logger.info( FUNCTION_NAME + " batFilePath:" + batFilePath);
		List<File> list = new ArrayList<File>();
		for (File fileItem : fileList) {
			String fileName = fileItem.getName();
			//视频文件
			if (fileName.toLowerCase().endsWith(".mpg") 
//					|| fileName.toLowerCase().endsWith(".swf")
					|| fileName.toLowerCase().endsWith(".wmv") 
					|| fileName.toLowerCase().endsWith(".avi")
					|| fileName.toLowerCase().endsWith(".mpeg")
					|| fileName.toLowerCase().endsWith(".mp4")
					|| fileName.toLowerCase().endsWith(".flv")) {
				//视频文件路径
				String videoRealPath = fileItem.getPath();
				logger.info( FUNCTION_NAME + " videoRealPath:" + videoRealPath);
				//截图的路径（输出路径）    
				String imageRealPath = videoRealPath.substring(0, videoRealPath.lastIndexOf('.') + 1) + "jpg"; 
				logger.info( FUNCTION_NAME + " imageRealPath:" + imageRealPath);
				String os = System.getProperty("os.name").toLowerCase(); 
				Process process = null;
				if (os.contains("linux")) {
					process = Runtime.getRuntime().exec(batFilePath + " " + videoRealPath + " " + imageRealPath);  
				} else {
					process = Runtime.getRuntime().exec("cmd.exe /c " + batFilePath + " " + videoRealPath + " " + imageRealPath);  
				}
				InputStream stderr = process.getErrorStream();
				InputStreamReader isr = new InputStreamReader(stderr);
				BufferedReader br = new BufferedReader(isr);
				String line = "";
				while ((line = br.readLine()) != null) {
					logger.info(line); 
				}
				process.waitFor();

				logger.info( FUNCTION_NAME + " ffmpeg succsse!");
				File imageFile = new File(imageRealPath);
				list.add(imageFile);
			}
		}
		//将视频截图文件加入FTP上传文件List
		for (File fileItem : list) {
			fileList.add(fileItem);
			//截图文件LOG
			logger.info( FUNCTION_NAME + " imgFile:" + fileItem.getName());
		}
		logger.info( FUNCTION_NAME + " end" );
	}
	
	/**
	 * 字幕预览
	 * @param BILL_ID
	 * @return String
	 */
	public void subtitlesPreview(HttpServletRequest req )throws Exception{
		AdvertBean bean = new AdvertBean();
		StringBuffer wordStyle = new StringBuffer();
		//背景颜色
		if (req.getParameter("background_color") != null && !"".equals(req.getParameter("background_color"))) {
			wordStyle.append("background-color: ");
			wordStyle.append(req.getParameter("background_color"));
			wordStyle.append(";");
		}
		//字体颜色
		if (req.getParameter("word_color") != null && !"".equals(req.getParameter("word_color"))) {
			wordStyle.append("color: ");
			wordStyle.append(req.getParameter("word_color"));
			wordStyle.append(";");
		}
		//字体属性
		wordStyle.append("font: ");
		wordStyle.append(req.getParameter("word_tilt"));
		wordStyle.append(" ");
		wordStyle.append(req.getParameter("word_bold"));
		wordStyle.append(" ");
		wordStyle.append(req.getParameter("word_size"));
		wordStyle.append(" ");
		wordStyle.append(req.getParameter("word_type"));
		wordStyle.append(";");
		//高
		if (req.getParameter("word_height") != null && !"".equals(req.getParameter("word_height"))) {
			wordStyle.append("height: ");
			wordStyle.append(req.getParameter("word_height"));
			wordStyle.append("px;");
		}
		//宽
		if (req.getParameter("word_width") != null && !"".equals(req.getParameter("word_width"))) {
			wordStyle.append("width: ");
			wordStyle.append(req.getParameter("word_width"));
			wordStyle.append("px;");
		}
		//水平对齐
		if (req.getParameter("text_align") != null && !"".equals(req.getParameter("text_align"))) {
			wordStyle.append("text-align: ");
			if ("1".equals(req.getParameter("text_align"))) {
				wordStyle.append("left");
			} else if ("2".equals(req.getParameter("text_align"))) {
				wordStyle.append("center");
			} else if ("3".equals(req.getParameter("text_align"))) {
				wordStyle.append("right");
			} else {
				wordStyle.append(req.getParameter("text_align"));
			}
			wordStyle.append(";");
		}
		//垂直对齐
		if (req.getParameter("vertical_align") != null && !"".equals(req.getParameter("vertical_align"))) {
			wordStyle.append("vertical-align: ");
			if ("1".equals(req.getParameter("vertical_align"))) {
				wordStyle.append("top");
			} else if ("2".equals(req.getParameter("vertical_align"))) {
				wordStyle.append("middle");
			} else if ("3".equals(req.getParameter("vertical_align"))) {
				wordStyle.append("bottom");
			} else {
				wordStyle.append(req.getParameter("vertical_align"));
			}
			wordStyle.append(";");
		}
		//滚动延迟设定
		bean.setRoll_delay(req.getParameter("roll_delay"));
		//滚动文字设定
		bean.setRoll_word(req.getParameter("roll_word"));
		//文字样式设定
		bean.setWord_style(wordStyle.toString());
		//检索结果保存
		req.setAttribute("resultBean", bean);
	}
	
	/**
	 * 节目单组检索
	 * @param req
	 * @throws Exception
	 */
	public void searchProgramlistGroup(HttpServletRequest req) throws Exception {
		AdvertBean bean = new AdvertBean();
		//节目单组名称
		bean.setProgramlistGroupName(req.getParameter("programlistGroup_name"));
		//审核状态
		bean.setAuditStatus(req.getParameter("audit_type"));
		//创建用户
		bean.setOperator(req.getParameter("operator_name"));
		//添加时间From
		bean.setProgramlistAddDateFrom(req.getParameter("search_date_s"));
		//添加时间To
		bean.setProgramlistAddDateTo(req.getParameter("search_date_e"));
		//检索结果当前位置
		bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		//排序方式
		bean.setSortField(req.getParameter("sort_field"));
		//排序类型
		bean.setSortType(req.getParameter("sort_type"));
		
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		
		List<AdvertBean> resultBeans = new ArrayList<AdvertBean>();
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] plBeans = dao.searchProgramlistGroup(bean);
		if (plBeans != null) {
			//检索结果开始位置
			int start_p  = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			//检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;
			
			int loop = 0;
			for (HashMap<String, String> itemMap : plBeans) {
				loop++;
				if( loop <= start_p ) {
                    continue;
                }
				if( loop > start_p && loop <= end_p ) {
					start_p++;
					AdvertBean advertbean = new AdvertBean();
					//节目单组ID
					advertbean.setProgramlistGroupId(itemMap.get("GROUP_ID"));
					//节目单组名称
					advertbean.setProgramlistGroupName(itemMap.get("GROUP_NAME"));
					//节目列表说明
					advertbean.setProgramlistGroup_desc(itemMap.get("DESCRIPTION"));
					//循环周期
					if (itemMap.get("LOOP_TIME") != null) {
						advertbean.setProgramlistGroup_loop(Integer.parseInt(itemMap.get("LOOP_TIME")));
					}
					//创建用户
					advertbean.setOperator(itemMap.get("NAME"));
					//创建时间
					advertbean.setCreateTime(itemMap.get("OPERATETIME"));
					//审核情况
					advertbean.setAuditStatus(itemMap.get("STATUS"));
					resultBeans.add(advertbean);
				}
			}
			//检索总行数
			bean.setTotal_num(plBeans.length);
		}
		//检索条件保存
		req.setAttribute("select_object", bean);
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	/**
	 * 节目单组添加
	 * @param req
	 * @return 添加结果
	 */
	public String programlistGroupAdd(HttpServletRequest req) throws Exception {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//节目单组名称
			bean.setProgramlistGroupName(req.getParameter("programlistGroup_name"));
			//添加的节目单
			String[] programlists = req.getParameterValues("programlistId");
			bean.setProgramlistGroup(programlists);
			//节目列表说明
			bean.setProgramlistGroup_desc(req.getParameter("programlistGroup_desc"));
			//循环周期
			if (req.getParameter("programlistGroup_loop") != null 
					&& !"".equals(req.getParameter("programlistGroup_loop"))) {
				bean.setProgramlistPlayTime(Integer.parseInt(req.getParameter("programlistGroup_loop")));
			}
			//节目单动作说明
			bean.setProgramlistActionList(req.getParameterValues("programlist_action"));
			
			AdvertDao dao = new AdvertDao();
			//操作者
			User user = (User) req.getSession().getAttribute("user_info");
			bean.setOperator(user.getId());
			//DB插入
			String programlistGroupId = dao.insertProgramlistGroup(bean);
			if(programlistGroupId == null){
				result = "failed";
			}else{
				result = "success";
			}
		} catch (Exception e ) {
			result = "failed";
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单组审核
	 * @param bean Bean
	 * @return
	 */
	public String programlistGroupAudit( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//节目单ID
			bean.setProgramlistGroupId(req.getParameter("programlistGroupId"));
			AdvertDao dao = new AdvertDao();
			// 节目单审核
			int updateCnt = dao.programlistGroupAudit(bean);
			if (updateCnt == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单组编辑初始化
	 * @param bean Bean
	 * @return
	 */
	public void programlistGroupEdit( HttpServletRequest req ) {
		AdvertBean bean = new AdvertBean();
		//节目单组ID
		bean.setProgramlistGroupId(req.getParameter("programlistGroupId"));
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		// 当前用户所在组织及其所有子组织信息取得
		OrgDealAction oda = new OrgDealAction();
		List orgList = oda.getChildNodesByUserID( Long.parseLong(user.getId()) );
		String orgIdStr = oda.getIdStrByList( orgList );
		bean.setSubOrgIdArr(orgIdStr);
		AdvertDao dao = new AdvertDao();
		HashMap<String, String>[] plBeans = dao.searchProgramlistGroup(bean);
		AdvertBean resultBean = new AdvertBean();
		List<AdvertBean> list = new ArrayList<AdvertBean>();
		if (plBeans != null && plBeans.length > 0) {
			//节目单组ID
			resultBean.setProgramlistGroupId(plBeans[0].get("GROUP_ID"));
			//节目单组名称
			resultBean.setProgramlistGroupName(plBeans[0].get("GROUP_NAME"));
			//节目列表说明
			resultBean.setProgramlistGroup_desc(plBeans[0].get("DESCRIPTION"));
			//循环周期
			if (plBeans[0].get("LOOP_TIME") != null) {
				resultBean.setProgramlistGroup_loop(Integer.parseInt(plBeans[0].get("LOOP_TIME")));
			}
			//创建用户
			resultBean.setOperator(plBeans[0].get("NAME"));
			//创建时间
			resultBean.setCreateTime(plBeans[0].get("OPERATETIME"));
			//审核情况
			resultBean.setAuditStatus(plBeans[0].get("STATUS"));
			
			//节目单组信息检索
			HashMap<String, String>[] programlistBeans = dao.searchProgramlistByGroupId(resultBean);
			if (programlistBeans != null && programlistBeans.length != 0) {
				for (HashMap<String, String> itemMap : programlistBeans) {
					AdvertBean itemBean = new AdvertBean();
					//节目单ID
					itemBean.setProgramlistId(itemMap.get("BILL_ID"));
					//节目单名称
					itemBean.setProgramlistName(itemMap.get("BILL_NAME"));
					//节目单描述
					itemBean.setProgramlistDescribe(itemMap.get("DESCRIPTION"));
					//节目单时长
					if (itemMap.get("PLAYTIME") != null && !"".equals(itemMap.get("PLAYTIME"))) {
						itemBean.setProgramlistPlayTime(Integer.parseInt(itemMap.get("PLAYTIME")));
					}
					//节目单动作说明
					itemBean.setProgramlistAction(itemMap.get("PLAYBILL_ACTION"));
					//布局类型
					itemBean.setLayoutScreen(itemMap.get("SCREEN_NUM"));
					//创建用户
					itemBean.setOperator(itemMap.get("NAME"));
					//创建时间
					itemBean.setCreateTime(itemMap.get("OPERATETIME"));
					//审核情况
					itemBean.setAuditStatus(itemMap.get("STATUS"));
					
					list.add(itemBean);
				}
			}
		}
		req.setAttribute("resultBean", resultBean);
		req.setAttribute("groupList", list);
	}
	
	/**
	 * 节目单组编辑保存
	 * @param req 
	 * @return
	 */
	public String programlistGroupUpdate( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//节目单组ID
			bean.setProgramlistGroupId(req.getParameter("programlistGroupId"));
			//节目单组名称
			bean.setProgramlistGroupName(req.getParameter("programlistGroup_name"));
			//添加的节目单
			String[] programlists = req.getParameterValues("programlistId");
			bean.setProgramlistGroup(programlists);
			//节目列表说明
			bean.setProgramlistGroup_desc(req.getParameter("programlistGroup_desc"));
			//循环周期
			if (req.getParameter("programlistGroup_loop") != null 
					&& !"".equals(req.getParameter("programlistGroup_loop"))) {
				bean.setProgramlistGroup_loop(Integer.parseInt(req.getParameter("programlistGroup_loop")));
			}
			//节目单动作说明
			bean.setProgramlistActionList(req.getParameterValues("programlist_action"));
			
			AdvertDao dao = new AdvertDao();
			//操作者
			User user = (User) req.getSession().getAttribute("user_info");
			bean.setOperator(user.getId());
			// 节目单编辑保存
			int updateCnt = dao.programlistGroupUpdate(bean);
			if (updateCnt == 0) {
				result = "failed";
			} else {
				result = "success";
			}
			//节目单组信息检索
			List<AdvertBean> list = new ArrayList<AdvertBean>();
			HashMap<String, String>[] programlistBeans = dao.searchProgramlistByGroupId(bean);
			if (programlistBeans != null && programlistBeans.length != 0) {
				for (HashMap<String, String> itemMap : programlistBeans) {
					AdvertBean itemBean = new AdvertBean();
					//节目单ID
					itemBean.setProgramlistId(itemMap.get("BILL_ID"));
					//节目单名称
					itemBean.setProgramlistName(itemMap.get("BILL_NAME"));
					//节目单描述
					itemBean.setProgramlistDescribe(itemMap.get("DESCRIPTION"));
					//节目单时长
					if (itemMap.get("PLAYTIME") != null && !"".equals(itemMap.get("PLAYTIME"))) {
						itemBean.setProgramlistPlayTime(Integer.parseInt(itemMap.get("PLAYTIME")));
					}
					//布局类型
					itemBean.setLayoutScreen(itemMap.get("SCREEN_NUM"));
					//创建用户
					itemBean.setOperator(itemMap.get("NAME"));
					//创建时间
					itemBean.setCreateTime(itemMap.get("OPERATETIME"));
					//审核情况
					itemBean.setAuditStatus(itemMap.get("STATUS"));
					
					list.add(itemBean);
				}
			}
			req.setAttribute("resultBean", bean);
			req.setAttribute("groupList", list);
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单组删除
	 * @param req 
	 * @return
	 */
	public String programlistGroupDelete( HttpServletRequest req ) {
		String result = "";
		try {
			AdvertBean bean = new AdvertBean();
			//节目单组ID
			bean.setProgramlistGroupId(req.getParameter("programlistGroupId"));
			
			AdvertDao dao = new AdvertDao();
			// 节目单组删除
			int delCnt = dao.programlistGroupDelete(bean);
			if (delCnt == 0) {
				result = "failed";
			} else {
				result = "success";
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单组批量删除
	 * @param req 
	 * @return
	 */
	public String programlistGroupAllDelete( HttpServletRequest req ) {
		String result = "";
		try {
			//节目单组ID
			String programlistGroupId = req.getParameter("programlistGroupId");
			if (programlistGroupId.contains(",")) {
				String[] programlistGroupIdArr = programlistGroupId.split(",");
				for (int i = 0; i < programlistGroupIdArr.length; i++) {
					AdvertBean bean = new AdvertBean();
					//节目单组ID
					bean.setProgramlistGroupId(programlistGroupIdArr[i]);
					
					AdvertDao dao = new AdvertDao();
					// 节目单组删除
					int delCnt = dao.programlistGroupDelete(bean);
					if (delCnt == 0) {
						result = "failed";
					} else {
						result = "success";
					}
				}
			} else {
				result = programlistGroupDelete(req);
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 节目单组批量审核
	 * @param req 
	 * @return
	 */
	public String programlistGroupAllAudit( HttpServletRequest req ) {
		String result = "";
		try {
			//节目单组ID
			String programlistGroupId = req.getParameter("programlistGroupId");
			if (programlistGroupId.contains(",")) {
				String[] programlistGroupIdArr = programlistGroupId.split(",");
				for (int i = 0; i < programlistGroupIdArr.length; i++) {
					AdvertBean bean = new AdvertBean();
					//节目单组ID
					bean.setProgramlistGroupId(programlistGroupIdArr[i]);
					
					AdvertDao dao = new AdvertDao();
					// 节目单审核
					int delCnt = dao.programlistGroupAudit(bean);
					if (delCnt == 0) {
						result = "failed";
					} else {
						result = "success";
					}
				}
			} else {
				result = programlistGroupAudit(req);
			}
		} catch (Exception e ) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 端机同步-节目单发布
	 * @param BILL_ID
	 * @return String
	 */
	public String programlistSynchronize(HttpServletRequest req )throws Exception{
		String FUNCTION_NAME = "programlistSynchronize() ";
		logger.info( FUNCTION_NAME + "start" );
		AdvertBean bean = new AdvertBean();
		String result = "";
		//根据machineID获取机器节目单下发列表
		AdvertDao advertDao = new AdvertDao();
		
		//端机ID
		String[] machineId = new String[1];
		machineId[0] = req.getParameter("machineId");
		bean.setmachineId(machineId);
		//根据端机ID获取机器节目单信息
		HashMap<String, String>[] billIssuedList = advertDao.getBillIssuedList(Long.parseLong(machineId[0]));
		if (billIssuedList == null || billIssuedList.length == 0) {
			//节目单ID为空,不执行下发操作
			return "success";
		}
		
		//播放屏幕
		bean.setScreenType("1");
		//临时广告
		bean.setTempAds("0");
		//立刻下发
		bean.setSendType("1");
		//下发时间(节目单)
		if (req.getParameter("sendTime") != null && !"".equals(req.getParameter("sendTime").trim())) {
			bean.setSendTime(req.getParameter("sendTime") + " " + req.getParameter("sendTime_hh") + req.getParameter("sendTime_mi"));
		}
		//结束播放时间(节目单)
		if (req.getParameter("valueTime") != null && !"".equals(req.getParameter("valueTime").trim())) {
			bean.setValueTime(req.getParameter("valueTime") + " " + req.getParameter("valueTime_hh") + req.getParameter("valueTime_mi"));
		}
		//下载控制
		bean.setControlType("1");
		
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		String operator = user.getId();
		bean.setOperator(operator);
		
		//根据节目单下发
		for (HashMap<String, String> item : billIssuedList) {
			//节目单ID
			bean.setProgramlistId(item.get("BILL_ID"));
			int[] re = advertDao.programSendDAO(bean);
			if(re == null) {
				result = "failed";
			} else {
				result = "success";
			}
		}
		
		//向FTP上传节目单发布文件
		String sendTime = "";
		if ("1".equals(bean.getSendType())) {
			sendTime = DateUtil.getCurrentDate("yyyy-MM-dd hhmm");
		} else {
			sendTime = bean.getSendTime();
		}
		for (String item : machineId) {
			uploadIssueBillListFile(Long.parseLong(item), sendTime, "1");
		}
		
		logger.info( FUNCTION_NAME + "end" );
		
		return result;
		
	}
	
	/**
	 * 端机同步-节目单发布
	 * @param BILL_ID
	 * @return String
	 */
	public String programlistSynchronize(String machineID, HttpServletRequest req )throws Exception{
		String FUNCTION_NAME = "programlistSynchronize() ";
		logger.info( FUNCTION_NAME + "start" );
		AdvertBean bean = new AdvertBean();
		String result = "";
		//根据machineID获取机器节目单下发列表
		AdvertDao advertDao = new AdvertDao();
		
		//端机ID
		String[] machineId = new String[1];
		machineId[0] = machineID;
		bean.setmachineId(machineId);
		//根据端机ID获取机器节目单信息
		HashMap<String, String>[] billIssuedList = advertDao.getBillIssuedList(Long.parseLong(machineId[0]));
		if (billIssuedList == null || billIssuedList.length == 0) {
			//节目单ID为空,不执行下发操作
			return "success";
		}
		
		//播放屏幕
		bean.setScreenType("1");
		//临时广告
		bean.setTempAds("0");
		//立刻下发
		bean.setSendType("1");
		//下发时间(节目单)
		if (req.getParameter("sendTime") != null && !"".equals(req.getParameter("sendTime").trim())) {
			bean.setSendTime(req.getParameter("sendTime") + " " + req.getParameter("sendTime_hh") + req.getParameter("sendTime_mi"));
		}
		//结束播放时间(节目单)
		if (req.getParameter("valueTime") != null && !"".equals(req.getParameter("valueTime").trim())) {
			bean.setValueTime(req.getParameter("valueTime") + " " + req.getParameter("valueTime_hh") + req.getParameter("valueTime_mi"));
		}
		//下载控制
		bean.setControlType("1");
		
		//操作者
		User user = (User) req.getSession().getAttribute("user_info");
		String operator = user.getId();
		bean.setOperator(operator);
		
		//根据节目单下发
		for (HashMap<String, String> item : billIssuedList) {
			//节目单ID
			bean.setProgramlistId(item.get("BILL_ID"));
			int[] re = advertDao.programSendDAO(bean);
			if(re == null) {
				result = "failed";
			} else {
				result = "success";
			}
		}
		
		//向FTP上传节目单发布文件
		String sendTime = "";
		if ("1".equals(bean.getSendType())) {
			sendTime = DateUtil.getCurrentDate("yyyy-MM-dd hhmm");
		} else {
			sendTime = bean.getSendTime();
		}
		for (String item : machineId) {
			uploadIssueBillListFile(Long.parseLong(item), sendTime, "1");
		}
		
		logger.info( FUNCTION_NAME + "end" );
		
		return result;
		
	}
	
	/**
	 * 取得端机类型
	 * @return
	 * @throws SQLException
	 */
	public HashMap[] getAllOSTypes() throws SQLException {
		AdvertDao dao = new AdvertDao();
		return dao.getAllOSTypes();
	}
}
