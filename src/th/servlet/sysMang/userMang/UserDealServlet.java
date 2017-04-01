/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.sysMang.userMang;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.action.OrgDealAction;
import th.dao.OrgDealDAO;
import th.dao.UserDao;
import th.entity.RoleObjectManagementBean;
import th.entity.UserBean;
import th.servlet.BaseServlet;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2013-8-17
 * @author PSET
 * @since JDK1.6
 *
 */
public class UserDealServlet extends BaseServlet {

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
	    	String jumpURL = "/jsp/sysMang/userMang/userDeal.jsp";
	    	
	    	try {

	    		OrgDealDAO odd = new OrgDealDAO();
				UserDao ud = new UserDao();
				OrgDealAction oda = new OrgDealAction();

				UserBean ub = new UserBean();
				RoleObjectManagementBean romb = new RoleObjectManagementBean();

				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				String dealFlg = req.getParameter( "dealFlg" );
				// 用户添加、编辑、授权权限的判断
				if(("add".equals( dealFlg )
						&&!(user.hasRight( user.getOrg_id(), String.valueOf( 52 ))&&user.hasRight( user.getOrg_id(), String.valueOf( 55 ))))
						||("change".equals( dealFlg )
						&&!(user.hasRight( user.getOrg_id(), String.valueOf( 53 ))&&user.hasRight( user.getOrg_id(), String.valueOf( 55 ))))){
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

//					String dealFlg = req.getParameter( "dealFlg" );
					String userConfirmFlg = "0";
					String pageTitle = "";
					String saveResult = "";
					String dealRoleList = "";
					// 当前用户所属组织List构成
					String ownedOrgList = "";
					String enabled = "";
					
					String roleList = (String)req.getParameter( "roleList" );
					String curAllRoles = (String)req.getParameter( "curAllRoles" );
					
					// 由UserDeal页面进入过该页面，点选按钮后，跳回后台进行DB处理
					if(roleList!=null&&!"".equals(roleList)){
						
						String[] roleIdArray = roleList.split( "," );
						
						long inputOrgId = Long.parseLong( (String)req.getParameter( "sel_org_id" ) );
						long inDptId = Long.parseLong( (String)req.getParameter( "input_dpt_id" ) );
						String inUserPassword = (String)req.getParameter( "input_user_password" );
						String inUserName = (String)req.getParameter( "input_user_name" );
						String inUserEmail = (String)req.getParameter( "input_user_email" );
						String inUserFixTel = (String)req.getParameter( "input_user_fixedTel" );
						String inUserOthCont = (String)req.getParameter( "input_user_othCont" );
						String inUserDesp = (String)req.getParameter( "input_user_desp" );
						String inUserType = (String)req.getParameter( "input_user_type" );

						if(inDptId!=-1){

							ub.setOrgId( inputOrgId );
							ub.setDptId( inDptId );
							ub.setPassword( inUserPassword );
							ub.setUserName( inUserName );
							ub.setEmail( inUserEmail );
							ub.setFixedTel( inUserFixTel );
							ub.setMobilePhone( inUserOthCont );
							ub.setUserDesp( inUserDesp );
							ub.setUserType( inUserType );
							ub.setOperator( userId );

							// add操作
							if("add".equals( dealFlg )){

								// 用户添加_52/用户授权_55
								if(user.hasRight( String.valueOf( inputOrgId ), String.valueOf( 52 ) )
										&&user.hasRight( String.valueOf( inputOrgId ), String.valueOf( 55 ) )){
									
									String inUserNickname = (String)req.getParameter( "input_user_nickname" );
									ub.setNickName( inUserNickname );									
									// 插入一条新的记录
									long newUserId = ud.insertUserRecord( ub );
									// 授权操作
									long curRoleId = -1;
									// "角色_组织映射"中用户信息设置
									romb.setObjectType( "1" );
									romb.setObjectId( newUserId );
									romb.setOperator( userId );
									// 角色列表循环
									for(int i=0;i<roleIdArray.length;i++){
										curRoleId = Long.parseLong( roleIdArray[i] );
										romb.setRoleId( curRoleId );
										// 角色_组织映射表中对应组织和角色记录插入
										odd.insertRoleObjectMap( romb );

									}
									saveResult = "用户添加成功!";
									userConfirmFlg = "1";
									pageTitle = "用户管理-用户信息添加";
									ub = ud.getUserBeanByUserId( newUserId );

								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}

							// change操作
							}else if("change".equals( dealFlg )){

								// 用户修改_53/用户授权_55
								if(user.hasRight( String.valueOf( inputOrgId ), String.valueOf( 53 ) )
										&&user.hasRight( String.valueOf( inputOrgId ), String.valueOf( 55 ) )){
									
									long hideUserId = Long.parseLong( (String)req.getParameter( "hide_user_id" ) );
									ub.setUserId( hideUserId );
									// 更新原有记录
									ud.updateUserRecord( ub );
									// 授权操作
									long curRoleId = -1;
									// "角色_组织映射"中用户信息设置
									romb.setObjectType( "1" );
									romb.setObjectId( hideUserId );
									romb.setOperator( userId );
									// 在组织_角色映射表中删除该UserID对应的映射记录
									odd.deleteRoleUserMappingByUserId( hideUserId );
									
									// 角色列表循环
									for(int i=0;i<roleIdArray.length;i++){
										curRoleId = Long.parseLong( roleIdArray[i] );
										romb.setRoleId( curRoleId );
										// 角色_组织映射表中对应组织和角色记录插入
										odd.insertRoleObjectMap( romb );

									}
									saveResult = "用户修改成功!";
									pageTitle = "用户管理-用户信息修改";
									ub = ud.getUserBeanByUserId( hideUserId );
									
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}

							}
							
							dealRoleList = oda.getRoleListHtml( roleList, curAllRoles );
							ownedOrgList = (String)req.getParameter( "hide_org_select" ); 
							enabled = "disabled='disabled'";

						}
						
					// userSearch页面跳转进入本页面	
					}else{
						
						if("add".equals( dealFlg )){

							userConfirmFlg = "1";
							pageTitle = "用户管理-用户信息添加";
							enabled = "";
							
						}else if("change".equals( dealFlg )){

							// 获取选中的用户Id
							String tmpUserIDStr = (String)req.getParameter( "sel_user_id" );
							if(tmpUserIDStr!=null&&!"".equals( tmpUserIDStr )){
								long selUserId = Long.parseLong( tmpUserIDStr );
								ub = ud.getUserBeanByUserId( selUserId );
								pageTitle = "用户管理-用户信息编辑";
								enabled = "disabled='disabled'";
							}
							
						}
						
			
						String ownedOrgAppender = (String)req.getParameter( "org_list_info" );
						if(ownedOrgAppender!=null&&!"".equals( ownedOrgAppender )){
							String[] ownedOrgArray = ownedOrgAppender.split( "," );
							for(int i=0;i<ownedOrgArray.length;i++){
								String[] selectOperation = ownedOrgArray[i].split( "_" );
								ownedOrgList += "<option value='" + selectOperation[1] + "'>" + selectOperation[0] + "</option>";
							}
						}
						
					}					

	
					req.setAttribute( "orgSelect", ownedOrgList );
					req.setAttribute( "dealRoleList", dealRoleList );
					// 选中用户原有信息显示
					req.setAttribute( "selUserBean", ub );
					// 用户是否需要确认口令标识
					req.setAttribute( "userConfirmFlg", userConfirmFlg );
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
