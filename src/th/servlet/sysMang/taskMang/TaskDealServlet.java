/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.sysMang.taskMang;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.action.OrgDealAction;
import th.com.util.Define;
import th.dao.RoleDealDAO;
import th.dao.StrategyDealDAO;
import th.entity.RoleObjectManagementBean;
import th.entity.TaskBean;
import th.servlet.BaseServlet;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2014-1-6
 * @author PSET
 * @since JDK1.6
 *
 */
public class TaskDealServlet extends BaseServlet {

	private static final long serialVersionUID = 6896488381721520137L;

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
			String jumpURL = "/jsp/sysMang/taskMang/taskDeal.jsp";

			try {

				RoleDealDAO rdd = new RoleDealDAO();
				StrategyDealDAO sdd = new StrategyDealDAO();
				OrgDealAction oda = new OrgDealAction();

				TaskBean tb = new TaskBean();

				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				String dealFlg = req.getParameter( "dealFlg" );
				// 策略添加、修改权限的判断
				if(("add".equals( dealFlg )&&!user.hasRight( user.getOrg_id(), String.valueOf( 83 )))
						||("change".equals( dealFlg )&&!user.hasRight( user.getOrg_id(), String.valueOf( 84 )))){
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

					String pageTitle = "";
					String saveResult = "";
					String dealReportList = "";
					String dealRoleList = "";
					String enabled = "";
					long selStgId = -1; 

					String inRepNameList = (String)req.getParameter( "input_reportList" );

					// 由TaskDeal页面进入过该页面，点选按钮后，跳回后台进行DB处理
					if(inRepNameList!=null&&!"".equals(inRepNameList)){

						// 添加或修改策略信息，根据"hide_stg_id"进行具体判断
						if("add/change".equals( dealFlg )){
							
							String inStgName = (String)req.getParameter( "input_stg_name" );
							String inStgDesp = (String)req.getParameter( "input_stg_desp" );
							String inSendInternal = (String)req.getParameter( "input_send_internal" );
							String inSendTimeHour = (String)req.getParameter( "input_send_time_hour" );
							String inSendTimeMinute = (String)req.getParameter( "input_send_time_minute" );
							String inSendType = (String)req.getParameter( "input_send_type" );
							String inDeliverTerminal = (String)req.getParameter( "input_deliver_terminal" );
							String inDeliverRoleList = (String)req.getParameter( "input_roleList" );
							
							tb.setStgName( inStgName );
							tb.setStgDesp( inStgDesp );
							tb.setSendInternal( inSendInternal );
							tb.setSendTimeHour( Integer.parseInt( inSendTimeHour ) );
							tb.setSendTimeMinute( Integer.parseInt( inSendTimeMinute ) );
							tb.setSendType( inSendType );
							tb.setDeliverTerminal( inDeliverTerminal );
							tb.setReportNameList( inRepNameList );
							tb.setDeliverRoleList( inDeliverRoleList );
							
							
							long hideStgId = Long.parseLong( (String)req.getParameter( "hide_stg_id" ) );
							
							// add操作
							if(hideStgId==-1){

								// 策略添加_83
								if(user.hasRight( user.getOrg_id(), String.valueOf( 83 ) )){

									tb.setStatus( Define.CHAR_IDENTIFY_ZERO );
									tb.setOperator( userId );
									tb.setModifyuser( userId );								
									// 插入一条新的记录
									selStgId = sdd.insertTaskRecord( tb );
									saveResult = "策略添加成功!";
									pageTitle = "系统管理-策略信息添加";
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}	

							}else{

								// 策略修改_84
								if(user.hasRight( user.getOrg_id(), String.valueOf( 84 ) )){
									
									tb.setStgId( hideStgId );
									tb.setModifyuser( userId );	
									// 更新原有记录
									sdd.updateTaskRecord( tb );
									saveResult = "策略修改成功!";
									pageTitle = "系统管理-策略信息编辑";
									selStgId = hideStgId;
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}	

							}
							
							tb = sdd.getTaskBeanByStgId( selStgId );
							// 右侧报表列表取得
							dealReportList = oda.getReportListHtml( selStgId );
							enabled = "disabled='disabled'";
						}

					// taskList页面跳转进入本页面	
					}else{

						if("add".equals( dealFlg )){

							pageTitle = "系统管理-策略信息添加";
							enabled = "";

						}else if("change".equals( dealFlg )){

							// 获取选中的策略Id
							String tmpStgIDStr = (String)req.getParameter( "sel_stg_id" );
							if(tmpStgIDStr!=null&&!"".equals( tmpStgIDStr )){
								selStgId = Long.parseLong( tmpStgIDStr );
								tb = sdd.getTaskBeanByStgId( selStgId );
								pageTitle = "系统管理-策略信息编辑";
								enabled = "disabled='disabled'";
							}

						}
						// 右侧报表列表取得
						dealReportList = oda.getReportListHtml( selStgId );

					}
					
					HashMap[] rolesList = null;
					// 当前登陆用户类型取得
					String userType = user.getType();
					// 普通用户
					if(Define.USER_NORMAL.equals(userType)){
						// 获取当前用户对应的所有角色列表			
						rolesList = rdd.getRolesByUserID( userId );
					// 管理员用户	
					}else if(Define.USER_ADMIN.equals(userType)){
						// 获得当前用户对应的角色以及其所在组织及其子组织下的所有用户的角色列表
						rolesList = oda.getMasterRolesByUserId( userId );
						
					}
					
					// 当前登陆用户所具有的的策略列表取得
					dealRoleList = oda.getRoleListHtml( tb.getDeliverRoleList(), oda.mapToString( rolesList, "ROLE_ID" ) );

					req.setAttribute( "dealReportList", dealReportList );
					req.setAttribute( "dealRoleList", dealRoleList );
					// 选中策略原有信息显示
					req.setAttribute( "selTaskBean", tb );
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
