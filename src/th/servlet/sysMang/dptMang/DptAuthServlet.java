/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.sysMang.dptMang;

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
import th.dao.DptDealDAO;
import th.dao.OrgDealDAO;
import th.dao.RoleDealDAO;
import th.dao.StrategyDealDAO;
import th.entity.DepartmentBean;
import th.entity.RoleObjectManagementBean;
import th.servlet.BaseServlet;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2013-8-16
 * @author PSET
 * @since JDK1.6
 *
 */
public class DptAuthServlet extends BaseServlet {

	/* (non-Javadoc)
	 * @see th.servlet.BaseServlet#doIt(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public String doIt( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {


		// セッション取得
		ServletContext sc = getServletContext();
		HttpSession session = req.getSession(false);

		if (session == null || session.getAttribute("user_info") == null) {
			res.setContentType("text/html; charset=utf-8");
			res.sendRedirect("/th/index.jsp");
			return null;
		}else{
			String jumpURL = "/jsp/sysMang/dptMang/dptAuth.jsp";

			try {

				DptDealDAO ddd = new DptDealDAO();
				RoleDealDAO rdd = new RoleDealDAO();
				OrgDealAction oda = new OrgDealAction();
				OrgDealDAO odd = new OrgDealDAO();

				DepartmentBean dpt = new DepartmentBean();
				RoleObjectManagementBean romb = new RoleObjectManagementBean();

				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				// 部门授权权限的判断
				if(!user.hasRight( user.getOrg_id(), String.valueOf( 50 ))){
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
					String dealFlg = req.getParameter( "dealFlg" );
					String saveResult = "";
					// 当前用户所属组织List构成
					String ownedOrgList = "";
					String selRoleList = "";
					String dealRoleList = "";

					// 页面跳转标识
					String pageIndex = req.getParameter( "page_index" );

					// 当前servlet是有dptAuth.jsp页面跳转过来的
					if(pageIndex!=null&&pageIndex!=""){

						if("auth".equals( dealFlg )){
							String roleList = (String)req.getParameter( "roleList" );
							selRoleList = roleList;
							String[] roleIdArray = roleList.split( "," );
							long authDptId = Long.parseLong( (String)req.getParameter( "hide_dpt_id" ) );
							HashMap curDpt = (HashMap)ddd.getDepartmentByDptId( authDptId )[0];

							// 部门授权_50
							if(user.hasRight( (String)curDpt.get( "ORG_ID" ), String.valueOf( 50 ) )){
								long curRoleId = -1;
								// "角色_组织映射"中部门信息设置
								romb.setObjectType( "3" );
								romb.setObjectId( authDptId );
								romb.setOperator( userId );
								// 在组织_角色映射表中删除该dptID对应的映射记录
								odd.deleteRoleDptMappingByDptId( authDptId );

								// 角色列表循环
								for(int i=0;i<roleIdArray.length;i++){
									curRoleId = Long.parseLong( roleIdArray[i] );
									romb.setRoleId( curRoleId );
									// 角色_组织映射表中对应组织和角色记录插入
									odd.insertRoleObjectMap( romb );

								}
								saveResult = "部门角色授权成功!";
								dpt = ddd.getDptBeanByDptId( authDptId );

							}else{
								jumpURL = "/jsp/common/noaction.jsp";
							}

						}
						ownedOrgList = (String)req.getParameter( "hide_org_select" );

						// 当前servlet是由dptSearch.jsp页面跳转过来的
					}else{

						if("auth".equals( dealFlg )){
							// 获取选中的部门Id
							String tmpDptIDStr = (String)req.getParameter( "sel_dept_id" );
							if(tmpDptIDStr!=null&&!"".equals( tmpDptIDStr )){
								long selDptId = Long.parseLong( tmpDptIDStr );
								HashMap[] dptMap = ddd.getDepartmentByDptId( selDptId );
								if(dptMap!=null&&dptMap.length==1){
									dpt.setDptId( selDptId );
									dpt.setOrgId( Long.parseLong( (String) dptMap[0].get( "ORG_ID" ) ) );
									dpt.setDptName( (String) dptMap[0].get( "DEPARTMENT_NAME" ) ) ;
									dpt.setDptDescription( (String) dptMap[0].get( "DESCRIPTION" ) );
									dpt.setManagerName( (String) dptMap[0].get( "MANAGER_NAME" ) );
									dpt.setManagerMail( (String) dptMap[0].get( "MANAGER_EMAIL" ) );
									dpt.setManagerTel( (String) dptMap[0].get( "MANAGER_TELEPHONE" ) );
									dpt.setOtherContacts( (String) dptMap[0].get( "OTHER_CONTACTS" ) );

								}

								HashMap[] ownedRoleList = odd.getRolesByDptID( selDptId );
								selRoleList = oda.mapToString( ownedRoleList, "ROLE_ID" );

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


					// 用户角色取得
					HashMap[] rolesList = null;
					HashMap[] allRoles = null;
					// 当前登陆用户类型取得
					String userType = user.getType();
					// 普通用户
					if(Define.USER_NORMAL.equals(userType)){
						// 获取当前用户对应的所有角色列表			
						allRoles = rdd.getRolesByUserID( userId );
						// 管理员用户	
					}else if(Define.USER_ADMIN.equals(userType)){
						// 获得当前用户对应的角色以及其所在组织及其子组织下的所有用户的角色列表
						allRoles = oda.getMasterRolesByUserId( userId );

					}


					// 获取策略表中配置的层级和角色之间对应关系记录
					StrategyDealDAO sdd = new StrategyDealDAO();
					HashMap[] allStgs = sdd.getAllStrategies();
					String delRoleIdStr = "";
					String compStr = "ROLE_ID";
					if(allStgs!=null&&allStgs.length>0){
						HashMap tempStg = null;
						for(int i=0;i<allStgs.length;i++){
							tempStg = (HashMap) allStgs[i];
							delRoleIdStr += (String) tempStg.get( "OBJECT_END" );
							if(i<allStgs.length-1){
								delRoleIdStr += ",";
							}
						}
					}
					// 特定角色删除后剩余角色列表
					rolesList = oda.roleMapDeal( allRoles, delRoleIdStr, compStr );

					dealRoleList = oda.getRoleListHtml( selRoleList, oda.mapToString( rolesList, "ROLE_ID" ) );



					req.setAttribute( "orgSelect", ownedOrgList );
					req.setAttribute( "dealRoleList", dealRoleList );
					// 选中部门原有信息显示
					req.setAttribute( "selDptBean", dpt );
					req.setAttribute( "saveResult", saveResult );


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
