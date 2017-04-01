package th.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

import org.apache.commons.net.ftp.FTPClient;

import th.action.AdvertManagementAction;
import th.action.OrgDealAction;
import th.com.util.Define;
import th.dao.AdvertDao;
import th.user.User;
import th.util.ftp.FtpUtils;

public class AdvertServlet extends BaseServlet{

	public String doIt(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		
		// Log.debug( className + " MonitorServlet start..." );
		String forward = null;
		
		req.setCharacterEncoding("UTF-8");
		ServletContext sc = getServletContext();
		
		HttpSession session = req.getSession(true);
		
		try {
			//页面ID
			String pageId = req.getParameter("pageId");
			//模块ID
			String funcId = req.getParameter("funcId");
			String a = sc.getRealPath("/image/media/");
			String b = System.getProperty("user.dir");
			logger.info("页面ID： "+pageId);
			logger.info("模块ID： "+funcId);
			
			// 素材检索画面
			if (Define.JSP_MATERIAL_SEARCH_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					forward = "/jsp/advert/materialSearch.jsp";
				}
				//素材检索
				if (Define.FUNC_MATERIAL_SEARCH_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.materialSearch(req);
					forward = "/jsp/advert/materialSearch.jsp";
				}
				//素材审核
				if (Define.FUNC_MATERIAL_AUDIT_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.materialAudit(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "审核成功");
					} else {
						req.setAttribute("result", "审核失败");
					}
					//再检索
					action.materialSearch(req);
					forward = "/jsp/advert/materialSearch.jsp";
				}
				//素材删除
				if (Define.FUNC_MATERIAL_DELETE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.materialDelete(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "删除成功");
					} else {
						req.setAttribute("result", "删除失败");
					}
					//再检索
					action.materialSearch(req);
					forward = "/jsp/advert/materialSearch.jsp";
				}
				//素材批量审核
				if (Define.FUNC_MATERIAL_ALLAUDIT_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.materialAllAudit(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "审核成功");
					} else {
						req.setAttribute("result", "审核失败");
					}
					//再检索
					action.materialSearch(req);
					forward = "/jsp/advert/materialSearch.jsp";
				}
				//素材批量删除
				if (Define.FUNC_MATERIAL_ALLDELETE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.materialAllDelete(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "删除成功");
					} else {
						req.setAttribute("result", "删除失败");
					}
					//再检索
					action.materialSearch(req);
					forward = "/jsp/advert/materialSearch.jsp";
				}
			} 
			// 素材添加画面
			else if (Define.JSP_MATERIAL_ADD_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					req.setAttribute("GROUP_LIST", new AdvertManagementAction().getGaterialGroupList(req));
					forward = "/jsp/advert/materialAdd.jsp";
				}
				//素材添加
				if (Define.FUNC_MATERIAL_ADD_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.materialAdd(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "添加成功");
					} else {
						req.setAttribute("result", "添加失败");
					}
					req.setAttribute("GROUP_LIST", new AdvertManagementAction().getGaterialGroupList(req));
					forward = "/jsp/advert/materialAdd.jsp";
				}
				//字幕预览
				if (Define.FUNC_SUBTITLES_PREVIEW_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.subtitlesPreview(req);
					req.setAttribute("prePage", Define.JSP_MATERIAL_ADD_ID);
					forward = "/jsp/advert/SubtitlesShow.jsp";
				}
			}
			// 素材编辑画面
			else if (Define.JSP_MATERIAL_EDIT_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.materialEdit(req);
					req.setAttribute("GROUP_LIST", action.getGaterialGroupList(req));
					forward = "/jsp/advert/materialEdit.jsp";
				}
				//素材更新
				if (Define.FUNC_MATERIAL_UPDATE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.materialUpdate(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "更新成功");
					} else {
						req.setAttribute("result", "更新失败");
					}
					//再初始化
					action.materialEdit(req);
					req.setAttribute("GROUP_LIST", action.getGaterialGroupList(req));
					forward = "/jsp/advert/materialEdit.jsp";
				}
			}
			// 布局检索画面
			else if (Define.JSP_LAYOUT_SEARCH_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					forward = "/jsp/advert/layoutSearch.jsp";
				}
				//布局检索
				if (Define.FUNC_LAYOUT_SEARCH_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.layoutSearch(req);
					forward = "/jsp/advert/layoutSearch.jsp";
				}
				//布局审核
				if (Define.FUNC_LAYOUT_AUDIT_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.layoutAudit(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "审核成功");
					} else {
						req.setAttribute("result", "审核失败");
					}
					//再检索
					action.layoutSearch(req);
					forward = "/jsp/advert/layoutSearch.jsp";
				}
				//布局删除
				if (Define.FUNC_LAYOUT_DELETE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.layoutDelete(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "删除成功");
					} else {
						req.setAttribute("result", "删除失败");
					}
					//再检索
					action.layoutSearch(req);
					forward = "/jsp/advert/layoutSearch.jsp";
				}
				//布局批量审核
				if (Define.FUNC_LAYOUT_ALLAUDIT_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.layoutAllAudit(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "审核成功");
					} else {
						req.setAttribute("result", "审核失败");
					}
					//再检索
					action.layoutSearch(req);
					forward = "/jsp/advert/layoutSearch.jsp";
				}
				//布局批量删除
				if (Define.FUNC_LAYOUT_ALLDELETE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.layoutAllDelete(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "删除成功");
					} else {
						req.setAttribute("result", "删除失败");
					}
					//再检索
					action.layoutSearch(req);
					forward = "/jsp/advert/layoutSearch.jsp";
				}
			} 
			// 布局添加画面
			else if (Define.JSP_LAYOUT_ADD_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					forward = "/jsp/advert/layoutAdd.jsp";
				}
				//布局添加
				if (Define.FUNC_LAYOUT_ADD_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					//添加成功返回布局ID
					String result = action.layoutAdd(req);
//					if ("success".equals(result)) {
//						req.setAttribute("result", "添加成功");
//					} else {
//						req.setAttribute("result", "添加失败");
//					}
					if (result == null || "".equals(result)) {
						req.setAttribute("result", "添加失败");
					} else {
						req.setAttribute("result", "添加成功");
					}
					forward = "/jsp/advert/layoutAdd.jsp";
				}
			}
			// 布局编辑画面
			else if (Define.JSP_LAYOUT_EDIT_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.layoutEdit(req);
					forward = "/jsp/advert/layoutEdit.jsp";
				}
				//布局更新
				if (Define.FUNC_LAYOUT_UPDATE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.layoutUpdate(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "更新成功");
					} else {
						req.setAttribute("result", "更新失败");
					}
					//再初始化
					action.layoutEdit(req);
					forward = "/jsp/advert/layoutEdit.jsp";
				}
			}
			// 节目单检索画面
			else if (Define.JSP_PROGRAMLIST_SEARCH_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					forward = "/jsp/advert/programlistSearch.jsp";
				}
				//节目单检索
				if (Define.FUNC_PROGRAMLIST_SEARCH_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.searchProgramlist(req);
					forward = "/jsp/advert/programlistSearch.jsp";
				}
				//节目单审核
				if (Define.FUNC_PROGRAMLIST_AUDIT_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistAudit(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "审核成功");
					} else {
						req.setAttribute("result", "审核失败");
					}
					//再检索
					action.searchProgramlist(req);
					forward = "/jsp/advert/programlistSearch.jsp";
				}
				//节目单删除
				if (Define.FUNC_PROGRAMLIST_DELETE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistDelete(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "删除成功");
					} else {
						req.setAttribute("result", "删除失败");
					}
					//再检索
					action.searchProgramlist(req);
					forward = "/jsp/advert/programlistSearch.jsp";
				}
				//节目单批量审核
				if (Define.FUNC_PROGRAMLIST_ALLAUDIT_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistAllAudit(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "审核成功");
					} else {
						req.setAttribute("result", "审核失败");
					}
					//再检索
					action.searchProgramlist(req);
					forward = "/jsp/advert/programlistSearch.jsp";
				}
				//节目单批量删除
				if (Define.FUNC_PROGRAMLIST_ALLDELETE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistAllDelete(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "删除成功");
					} else {
						req.setAttribute("result", "删除失败");
					}
					//再检索
					action.searchProgramlist(req);
					forward = "/jsp/advert/programlistSearch.jsp";
				}
			} 
			// 节目单添加画面
			else if (Define.JSP_PROGRAMLIST_ADD_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					forward = "/jsp/advert/programlistAdd.jsp";
				}
				//节目单添加
				if (Define.FUNC_PROGRAMLIST_ADD_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistAdd(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "添加成功");
					} else {
						req.setAttribute("result", "添加失败");
					}
					forward = "/jsp/advert/programlistAdd.jsp";
				}
			}
			// 节目单编辑画面
			else if (Define.JSP_PROGRAMLIST_EDIT_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.programlistEdit(req);
					forward = "/jsp/advert/programlistEdit.jsp";
				}
				//节目单更新
				if (Define.FUNC_PROGRAMLIST_UPDATE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistUpdate(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "更新成功");
					} else {
						req.setAttribute("result", "更新失败");
					}
					//再初始化
					action.programlistEdit(req);
					forward = "/jsp/advert/programlistEdit.jsp";
				}
			}
			// 节目单组检索画面
			else if (Define.JSP_PROGRAMLISTGROUP_SEARCH_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					forward = "/jsp/advert/programlistGroupSearch.jsp";
				}
				//节目单组检索
				if (Define.FUNC_PROGRAMLISTGROUP_SEARCH_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.searchProgramlistGroup(req);
					forward = "/jsp/advert/programlistGroupSearch.jsp";
				}
				//节目单组审核
				if (Define.FUNC_PROGRAMLISTGROUP_AUDIT_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistGroupAudit(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "审核成功");
					} else {
						req.setAttribute("result", "审核失败");
					}
					//再检索
					action.searchProgramlistGroup(req);
					forward = "/jsp/advert/programlistGroupSearch.jsp";
				}
				//节目单组删除
				if (Define.FUNC_PROGRAMLISTGROUP_DELETE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistGroupDelete(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "删除成功");
					} else {
						req.setAttribute("result", "删除失败");
					}
					//再检索
					action.searchProgramlistGroup(req);
					forward = "/jsp/advert/programlistGroupSearch.jsp";
				}
				//节目单组批量审核
				if (Define.FUNC_PROGRAMLISTGROUP_ALLAUDIT_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistGroupAllAudit(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "审核成功");
					} else {
						req.setAttribute("result", "审核失败");
					}
					//再检索
					action.searchProgramlistGroup(req);
					forward = "/jsp/advert/programlistGroupSearch.jsp";
				}
				//节目单组批量删除
				if (Define.FUNC_PROGRAMLISTGROUP_ALLDELETE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistGroupAllDelete(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "删除成功");
					} else {
						req.setAttribute("result", "删除失败");
					}
					//再检索
					action.searchProgramlistGroup(req);
					forward = "/jsp/advert/programlistGroupSearch.jsp";
				}
			} 
			// 节目单组添加画面
			else if (Define.JSP_PROGRAMLISTGROUP_ADD_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					forward = "/jsp/advert/programlistGroupAdd.jsp";
				}
				//节目单组添加
				if (Define.FUNC_PROGRAMLISTGROUP_ADD_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistGroupAdd(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "添加成功");
					} else {
						req.setAttribute("result", "添加失败");
					}
					forward = "/jsp/advert/programlistGroupAdd.jsp";
				}
			}
			// 节目单组编辑画面
			else if (Define.JSP_PROGRAMLISTGROUP_EDIT_ID.equals(pageId)) {
				//初始化 
				if (funcId == null || "".equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.programlistGroupEdit(req);
					forward = "/jsp/advert/programlistGroupEdit.jsp";
				}
				//节目单组更新
				if (Define.FUNC_PROGRAMLISTGROUP_UPDATE_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					String result = action.programlistGroupUpdate(req);
					if ("success".equals(result)) {
						req.setAttribute("result", "更新成功");
					} else {
						req.setAttribute("result", "更新失败");
					}
					//再初始化
					action.programlistGroupEdit(req);
					forward = "/jsp/advert/programlistGroupEdit.jsp";
				}
			}
			// 模式窗口画面
			else if (Define.JSP_SUB_WINDOW_ID.equals(pageId)) {
				//选取布局模式窗口处理
				if (Define.FUNC_LAYOUT_SUBWINDOW_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.layoutSubWindow(req);
					forward = "/jsp/advert/layoutSubWindow.jsp";
				}
				//选取素材模式窗口处理
				if (Define.FUNC_MATERIAL_SUBWINDOW_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.materialSubWindow(req);
					forward = "/jsp/advert/materialSubWindow.jsp";
				}
				//选取字幕模式窗口处理
				if (Define.FUNC_SUBTITLES_SUBWINDOW_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.subtitlesSubWindow(req);
					forward = "/jsp/advert/subtitlesSubWindow.jsp";
				}
				//选取节目单模式窗口处理
				if (Define.FUNC_PROGRAMLIST_SUBWINDOW_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.programlistSubWindow(req);
					forward = "/jsp/advert/programlistSubWindow.jsp";
				}
				//选取节目单组模式窗口处理
				if (Define.FUNC_PROGRAMLISTGROUP_SUBWINDOW_ID.equals(funcId)) {
					AdvertManagementAction action = new AdvertManagementAction();
					action.programlistGroupSubWindow(req);
					forward = "/jsp/advert/programlistGroupSubWindow.jsp";
				}
			}
			//区域授权
			else if (Define.JSP_REGIONAL_AUTH_ID.equals(pageId)){
				//初始化 
				if(Define.FUNC_PROGRAMLIST_AUTH_ID.equals(funcId)){
					AdvertManagementAction action = new AdvertManagementAction();
					
					forward = action.regionalAuth(req);					
				}
				//区域授权
				if(Define.FUNC_REGIONAL_AUTH_ID.equals(funcId)){
					
					AdvertManagementAction action = new AdvertManagementAction();
										
					String result= action.regionalAuthinsert(req);
					
					if ("success".equals(result)) {
						req.setAttribute("result", "授权成功");
					} else {
						req.setAttribute("result", "授权失败");
					}
					
					forward = "/AdvertServlet?pageId=jsp_regional_auth_id&funcId=func_programlist_auth_id";
				}
				if(Define.FUNC_GET_LAYOUT_INFO.equals(funcId)){
					
					AdvertManagementAction action = new AdvertManagementAction();
										
					String result= action.getLayoutInfo(req);
					
					if ("success".equals(result)) {
						req.setAttribute("result", "授权成功");
					} else {
						req.setAttribute("result", "授权失败");
					}
					res.setContentType("text/xml;charset=UTF-8");
					res.setHeader("Cache-Control", "no-cache");
					res.getWriter().write(result);
					forward = null;
				}
			}
			//节目单发布
			else if (Define.JSP_PROGRAMLIST_SEND_ID.equals(pageId)){
				String type = req.getParameter("type");
				//端机指令-广告下发
				if("123".equals(type)){
					//初始化 
					if (funcId == null || "".equals(funcId)) {
						AdvertManagementAction action = new AdvertManagementAction();
						action.getMachinTree(req, true);
						
						forward="/jsp/advert/advertisementSend.jsp";
					}
					//节目单发布
					if (Define.FUNC_PROGRAM_SEND_ID.equals(funcId)) {
						AdvertManagementAction action = new AdvertManagementAction();
						String result = action.programSendByMachine(req);
						if ("success".equals(result)) {
							req.setAttribute("result", "发布成功");
							//跳转到广告下发控制画面
							req.setAttribute("macId", req.getParameter("sel_nodes_info"));
							req.setAttribute("macName", req.getParameter("sel_nodes_name"));
							req.setAttribute("type", "advertisementSend");
							forward = "/jsp/common/advertisementSending.jsp";
						} else {
							req.setAttribute("result", "发布失败");
							//重新取得组织树
							action.getMachinTree(req, true);
							forward = "/jsp/advert/advertisementSend.jsp";
						}
					}
					//清除端机节目单列表
					if (Define.FUNC_PROGRAMLIST_CLEAN_ID.equals(funcId)) {
						AdvertManagementAction action = new AdvertManagementAction();
						String result= action.updateProgramSendByMachine(req);
						String ret = "";
						if ("success".equals(result)) {
							ret = "该端机历史记录清除成功";
						} else {
							ret = "该端机历史记录清除失败";
						}
						res.setContentType("text/xml;charset=UTF-8");
						res.setHeader("Cache-Control", "no-cache");
						res.getWriter().write(ret);
						forward = null;
					}
					
				}else{
					
					//初始化 
					if (funcId == null || "".equals(funcId)) {
						AdvertManagementAction action = new AdvertManagementAction();
						action.getMachinTree(req, true);
						
						forward="/jsp/advert/programSend.jsp";
					}
					//节目单发布
					if (Define.FUNC_PROGRAM_SEND_ID.equals(funcId)) {
						AdvertManagementAction action = new AdvertManagementAction();
						String result = action.programSend(req);
						if ("success".equals(result)) {
							req.setAttribute("result", "发布成功");
						} else {
							req.setAttribute("result", "发布失败");
						}
						//重新取得组织树
						action.getMachinTree(req, true);
						forward = "/jsp/advert/programSend.jsp";
					}
					//清除端机节目单列表
					if (Define.FUNC_PROGRAMLIST_CLEAN_ID.equals(funcId)) {
						AdvertManagementAction action = new AdvertManagementAction();
						String result= action.updateProgramSend(req);
						String ret = "";
						if ("success".equals(result)) {
							ret = "该端机历史记录清除成功";
						} else {
							ret = "该端机历史记录清除失败";
						}
						res.setContentType("text/xml;charset=UTF-8");
						res.setHeader("Cache-Control", "no-cache");
						res.getWriter().write(ret);
						forward = null;
					}
				}
			
			}
			//FTP取得素材缩略图
			else if (Define.FUNC_FTP_FILE_GET_ID.equals(pageId)) {
				String dir = req.getParameter("dir");
				String path = dir.substring(0, dir.lastIndexOf("/")+1);
				String fileName = dir.substring(dir.lastIndexOf("/")+1);
				FTPClient ftpClient = FtpUtils.getFTPClient();
				ftpClient.changeWorkingDirectory(path);
				InputStream in = ftpClient.retrieveFileStream(fileName);
				if (fileName.endsWith(".jpg") || fileName.endsWith(".png")
						|| fileName.endsWith(".bmp") || fileName.endsWith(".gif")) {
					res.setContentType("image/*"); 
				} else if (fileName.endsWith(".mpg") || fileName.endsWith(".mp4")
						|| fileName.endsWith(".swf") || fileName.endsWith(".wmv")
						|| fileName.endsWith(".avi") || fileName.endsWith(".mpeg")) {
					res.setContentType("audio/*"); 
				}
				OutputStream toClient=res.getOutputStream(); 
				byte buffer[]=new byte[4*1024];
				int len = 0;
				while((len = in.read(buffer)) != -1) {
					toClient.write(buffer,0,len);
				}
				toClient.flush();  
				toClient.close();   
				in.close();   
				return null;  
			}
			else if ("123".equals(pageId)) {
				FTPClient ftpClient = FtpUtils.getFTPClient();
				ftpClient.changeWorkingDirectory("/th_SystemConfig/media");
				InputStream in = ftpClient.retrieveFileStream("media_0e7de700-a050-4d2b-b977-ecff28d3f5b3.flv");
				res.setContentType("audio/*"); 
				//res.setContentType("image/*"); 
				OutputStream toClient=res.getOutputStream(); 
				byte buffer[]=new byte[4*1024];
				int len = 0;
				while((len = in.read(buffer)) != -1) {
					toClient.write(buffer,0,len);
				}
				toClient.flush();  
				toClient.close();   
				in.close();   
				return null;  
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			req.setAttribute("exception", e);
		
		} finally {
			res.addHeader("Cache-Control", "private");
			if (forward != null) {
				sc.getRequestDispatcher(forward).forward(req, res);
			}
		}
		return forward;
	}
}
