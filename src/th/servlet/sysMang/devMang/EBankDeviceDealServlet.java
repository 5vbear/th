/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.sysMang.devMang;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.dao.EBankDeviceDAO;
import th.entity.EBankDeviceBean;
import th.servlet.BaseServlet;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2013-9-12
 * @author PSET
 * @since JDK1.6
 *
 */
public class EBankDeviceDealServlet extends BaseServlet {

	/* (non-Javadoc)
	 * @see th.servlet.BaseServlet#doIt(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public String doIt( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException,
	SQLException {
		// セッション取得
		ServletContext sc = getServletContext();
		HttpSession session = req.getSession(false);

		if (session == null || session.getAttribute("user_info") == null) {
			res.setContentType("text/html; charset=utf-8");
			res.sendRedirect("/th/index.jsp");
			return null;
		}else{
			String jumpURL = "/jsp/sysMang/devMang/ebDeviceDeal.jsp";

			try {

				EBankDeviceDAO edd = new EBankDeviceDAO();
				EBankDeviceBean ebb = new EBankDeviceBean();

				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				String dealFlg = req.getParameter( "dealFlg" );
				// 网银设备添加、修改权限的判断
				if(("add".equals( dealFlg )&&!user.hasRight( user.getOrg_id(), String.valueOf( 115 )))
						||("change".equals( dealFlg )&&!user.hasRight( user.getOrg_id(), String.valueOf( 116 )))){
					res.setContentType("text/html; charset=utf-8");
					res.sendRedirect("/th/jsp/common/noaction.jsp");
					return null;
				}else{
					// 用户ID
					String userID = user.getId();

					long userId = -1;

					if (userID==null||"".equals(userID)) {
						logger.debug("用户ID为空");
						return null;
					}else{
						userId = Long.parseLong( userID );
					}

					// 已经进入过该页面，点选按钮后，跳回后台进行DB处理
//					String dealFlg = req.getParameter( "dealFlg" );
					String saveResult = "";
					String pageTitle = "";
					String enabled = "";

					// 页面跳转标识
					String pageIndex = req.getParameter( "page_index" );

					// 当前servlet是有ebDeviceDeal.jsp页面跳转过来的
					if(pageIndex!=null&&pageIndex!=""){

						// 添加或修改网银设备信息，根据"hide_dev_id"进行具体判断
						if("add/change".equals( dealFlg )){
							String inputDevOs = (String)req.getParameter( "in_dev_os" );
							String inputDevDesp = (String)req.getParameter( "in_dev_description" );

							ebb.setDevOs( inputDevOs );
							ebb.setDevDesp( inputDevDesp );
							ebb.setOperator( userId );

							long hideDevId = Long.parseLong( (String)req.getParameter( "hide_dev_id" ) );
							// add操作
							if(hideDevId==-1){

								// 网银设备添加_115
								if(user.hasRight( user.getOrg_id(), String.valueOf( 115 ) )){
									// 判断当前输入的操作类型记录是否已经存在
									if(!edd.checkEBankDeviceExist( inputDevOs )){
										// 插入一条新的记录
										edd.insertEBankDeviceRecord( ebb );
										saveResult = "设备操作系统信息添加成功!";
									}else{
										saveResult = "当前输入操作类型的设备操作系统已经存在，设备信息添加失败!";
									}
									pageTitle = "系统管理-设备操作系统添加";
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}	

							}else{

								// 网银设备修改_116
								if(user.hasRight( user.getOrg_id(), String.valueOf( 116 ) )){
									// 更新原有记录
									ebb.setDevId( hideDevId );
									edd.updateEBankDeviceRecord( ebb );
									saveResult = "设备操作系统信息修改成功!";
									pageTitle = "系统管理-设备操作系统编辑";									
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}	

							}

							enabled = "disabled='disabled'";

						}

						// 当前servlet是由ebDeviceList.jsp页面跳转过来的
					}else{

						if("add".equals( dealFlg )){
							pageTitle = "系统管理-设备操作系统添加";
							enabled = "";
						}else if("change".equals( dealFlg )){
							// 获取选中的设备Id
							String tmpDevIDStr = (String)req.getParameter( "sel_dev_id" );
							if(tmpDevIDStr!=null&&!"".equals( tmpDevIDStr )){
								long selDevId = Long.parseLong( tmpDevIDStr );
								ebb = edd.getEBankDeviceBeanBydevID( selDevId );
								
								pageTitle = "系统管理-设备操作系统编辑";
								enabled = "disabled='disabled'";
							}
						}

					}

					// 选中网银设备原有信息显示
					req.setAttribute( "selDevBean", ebb );
					req.setAttribute( "pageTitle", pageTitle );
					req.setAttribute( "saveResult", saveResult );
					// 页面元素显示与否的控制变量
					req.setAttribute( "acTurn", enabled );

					// 页面跳转
					res.addHeader( "Cache-Control", "private" );
					req.setCharacterEncoding( "UTF-8" );
					sc.getRequestDispatcher( jumpURL ).forward( req, res );
				}

			}
			catch ( SQLException e ) {

				e.printStackTrace();
			}
		}

		return null;
	}

}
