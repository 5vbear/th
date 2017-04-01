/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.sysMang.com;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.action.OrgDealAction;
import th.com.util.Define;
import th.dao.OrgDealDAO;
import th.dao.RoleDealDAO;
import th.dao.StrategyDealDAO;
import th.dao.UserDao;
import th.entity.UserBean;
import th.servlet.BaseServlet;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2013-9-3
 * @author PSET
 * @since JDK1.6
 *
 */
public class AjaxPageCheckServlet extends BaseServlet {

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
			String forward = null;

			try {

				String type = req.getParameter( "type" );	
				
				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				// 用户ID
				String userID = user.getId();

				long userId = -1;
				if (userID==null||"".equals(userID)) {
					logger.debug("用户ID为空");
					return null;
				}else{
					userId = Long.parseLong( userID );
				}

				// 角色管理-角色添加
				// 检查页面输入角色名称是否已经存在
				if ( "roleNameCheck".equals( type ) ) {
//					forward = "/jsp/sysMang/roleMang/roleDeal.jsp";
					String roleName = (String) req.getParameter( "roleName" );
					roleName = URLDecoder.decode( roleName, "UTF-8" );
					RoleDealDAO rdd = new RoleDealDAO();
					int result = rdd.checkRoleName( roleName );
					res.setContentType( "text/xml;charset=UTF-8" );
					res.setHeader( "Cache-Control", "no-cache" );
					String writeData = URLDecoder.decode( String.valueOf( result ), "UTF-8" );
					res.getWriter().write( writeData );
					res.addHeader( "Cache-Control", "private" );
										
				// 用户管理-用户添加
				// 检查页面输入用户账号是否已经存在	
				}else if( "nickNameCheck".equals( type ) ){
//					forward = "/jsp/sysMang/roleMang/roleDeal.jsp";
					String nickName = (String) req.getParameter( "nickName" );
					nickName = URLDecoder.decode( nickName, "UTF-8" );
					UserDao ud = new UserDao();
					long result = ud.getUserIdByNickName( nickName );
					res.setContentType( "text/xml;charset=UTF-8" );
					res.setHeader( "Cache-Control", "no-cache" );
					String writeData = URLDecoder.decode( String.valueOf( result ), "UTF-8" );
					res.getWriter().write( writeData );
					res.addHeader( "Cache-Control", "private" );

					
				// 用户管理-用户处理
				// 根据当前选中的用户类型和组织ID，判断当前可用的角色列表
				}else if( "userTypeCheck".equals( type ) ){

					long selOrgId = Long.parseLong( (String) req.getParameter( "orgId" ) );
					String selUserType = (String) req.getParameter( "userType" );
					
					OrgDealDAO odd = new OrgDealDAO();
					RoleDealDAO rdd = new RoleDealDAO();
					OrgDealAction oda = new OrgDealAction();
					UserDao ud = new UserDao();
					
					// 当前登陆用户所在组织层级取得
					HashMap[] curNodeMap = odd.getCurOrgNodeByUserID( userId );
					long curUserOrgLevel = Long.parseLong( (String) curNodeMap[0].get( "ORG_LEVEL" ) );
					// 选中orgId所在层级取得
					HashMap[] selNodeMap = odd.getCurOrgNodeByOrgId( selOrgId );
					long selOrgLevel = Long.parseLong( (String) selNodeMap[0].get( "ORG_LEVEL" ) );
					
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
					
					String ownedRoleList = "";
					// 策略中定义角色Id取得
					String stgRoleId = "";
					if((selOrgLevel>curUserOrgLevel)&&Define.USER_ADMIN.equals(selUserType)){
						stgRoleId = sdd.getObjEndByObjBegin( String.valueOf( selOrgLevel ) );
						if(stgRoleId!=null&&!"".equals( stgRoleId )){
							ownedRoleList += "<div><input type='checkbox' name='role' value='" + stgRoleId + "' checked='checked' disabled='disabled' /> "
									+ rdd.getRoleNameByRoleID( Long.parseLong( stgRoleId ) ) + "</div> ";
						}
						
					}
					
					
					long dealUserId = Long.parseLong( (String) req.getParameter( "dealUserId" ) );
					String dealFlg = (String) req.getParameter( "dealFlg" );
					// 修改用户角色取得
					HashMap[] dealUserRolesList = null;
					HashMap[] dealUserAllRoles = null;
					
					if("add".equals(dealFlg)){
						for ( int i = 0; i < rolesList.length; i++ ) {
							HashMap tmpMap = rolesList[i];
							ownedRoleList += "<div><input type='checkbox' name='role' value='" + (String) tmpMap.get( "ROLE_ID" ) + "' /> "
									+ (String) tmpMap.get( "ROLE_NAME" ) + "</div> ";
						}
					}else if("change".equals(dealFlg)){
						UserBean ub = ud.getUserBeanByUserId( dealUserId );
						if(Define.USER_NORMAL.equals(ub.getUserType())){
							// 获取当前用户对应的所有角色列表			
							dealUserAllRoles = rdd.getRolesByUserID( dealUserId );
						// 管理员用户	
						}else if(Define.USER_ADMIN.equals(ub.getUserType())){
							// 获得当前用户对应的角色以及其所在组织及其子组织下的所有用户的角色列表
							dealUserAllRoles = oda.getMasterRolesByUserId( dealUserId );							
						}
						// 特定角色删除后剩余角色列表
						dealUserRolesList = oda.roleMapDeal( dealUserAllRoles, delRoleIdStr, compStr );
						HashMap curRole = null;
						boolean containFlg = false;
						int count = 0;
						for ( int i = 0; i < rolesList.length; i++ ) {
							HashMap tmpMap = rolesList[i];
							if(dealUserRolesList==null||dealUserRolesList.length==0){
								containFlg = false;
							}else{
								count = 0;
								for(int j=0;j<dealUserRolesList.length;j++){
									curRole = dealUserRolesList[j];
									if(((String)curRole.get( "ROLE_ID" )).equals( (String)tmpMap.get( "ROLE_ID" ) )){
										break;
									}else{
										count++;
									}
								}
								if(count<dealUserRolesList.length){
									containFlg = true;
								}else{
									containFlg = false;
								}
							}
							ownedRoleList += "<div><input type='checkbox' name='role' value='" + (String) tmpMap.get( "ROLE_ID" ) + "' ";
							if(containFlg){
								ownedRoleList += "checked='checked'";
							}
							ownedRoleList += " /> " + (String) tmpMap.get( "ROLE_NAME" ) + "</div> ";
	
						}
					}
					
					
//					String oneRN = Define.ROLE_PROVINCE_ADMIN;
//					String twoRN = Define.ROLE_CITY_ADMIN;
//					
//					
//					// 特定角色删除后剩余角色列表
//					rolesList = oda.roleMapDeal( allRoles, oneRN + "," + twoRN, "ROLE_NAME" );
//					
//					String ownedRoleList = "";
//					if((selOrgLevel>curUserOrgLevel)&&"1".equals(selUserType)){
//						HashMap[] temp = null;
//						// "省级管理员角色"
//						if(selOrgLevel==2){
//							temp = rdd.getRoleByRoleName( oneRN );			
//						// "市级管理员角色"
//						}else if(selOrgLevel==3){
//							temp = rdd.getRoleByRoleName( twoRN );			
//						}
//						ownedRoleList += "<div><input type='checkbox' name='role' value='" + (String) temp[0].get( "ROLE_ID" ) + "' checked='checked' /> "
//								+ (String) temp[0].get( "ROLE_NAME" ) + "</div> ";
//					}
//					
//					long dealUserId = Long.parseLong( (String) req.getParameter( "dealUserId" ) );
//					String dealFlg = (String) req.getParameter( "dealFlg" );
//					// 修改用户角色取得
//					HashMap[] dealUserRolesList = null;
//					HashMap[] dealUserAllRoles = null;
//					
//					if("add".equals(dealFlg)){
//						for ( int i = 0; i < rolesList.length; i++ ) {
//							HashMap tmpMap = rolesList[i];
//							ownedRoleList += "<div><input type='checkbox' name='role' value='" + (String) tmpMap.get( "ROLE_ID" ) + "' /> "
//									+ (String) tmpMap.get( "ROLE_NAME" ) + "</div> ";
//						}
//					}else if("change".equals(dealFlg)){
//						UserBean ub = ud.getUserBeanByUserId( dealUserId );
//						if("0".equals(ub.getUserType())){
//							// 获取当前用户对应的所有角色列表			
//							dealUserAllRoles = rdd.getRolesByUserID( dealUserId );
//						// 管理员用户	
//						}else if("1".equals(ub.getUserType())){
//							// 获得当前用户对应的角色以及其所在组织及其子组织下的所有用户的角色列表
//							dealUserAllRoles = oda.getMasterRolesByUserId( dealUserId );							
//						}
//						// 特定角色删除后剩余角色列表
//						dealUserRolesList = oda.roleMapDeal( dealUserAllRoles, oneRN + "," + twoRN, "ROLE_NAME" );
//						HashMap curRole = null;
//						boolean containFlg = false;
//						int count = 0;
//						for ( int i = 0; i < rolesList.length; i++ ) {
//							HashMap tmpMap = rolesList[i];
//							if(dealUserRolesList==null||dealUserRolesList.length==0){
//								containFlg = false;
//							}else{
//								count = 0;
//								for(int j=0;j<dealUserRolesList.length;j++){
//									curRole = dealUserRolesList[j];
//									if(((String)curRole.get( "ROLE_ID" )).equals( (String)tmpMap.get( "ROLE_ID" ) )){
//										break;
//									}else{
//										count++;
//									}
//								}
//								if(count<dealUserRolesList.length){
//									containFlg = true;
//								}else{
//									containFlg = false;
//								}
//							}
//							ownedRoleList += "<div><input type='checkbox' name='role' value='" + (String) tmpMap.get( "ROLE_ID" ) + "' ";
//							if(containFlg){
//								ownedRoleList += "checked='checked'";
//							}
//							ownedRoleList += " /> " + (String) tmpMap.get( "ROLE_NAME" ) + "</div> ";
//	
//						}
//					}

					res.setContentType( "text/xml;charset=UTF-8" );
					res.setHeader( "Cache-Control", "no-cache" );
					String writeData = URLDecoder.decode( ownedRoleList, "UTF-8" );
					res.getWriter().write( writeData );
					res.addHeader( "Cache-Control", "private" );

				// 部门管理-部门授权
				// 根据当前选中的组织ID，判断当前可用的角色列表
				}else if("orgRoleList".equals( type )){
					
					long selOrgId = Long.parseLong( (String) req.getParameter( "orgId" ) );
					
					RoleDealDAO rdd = new RoleDealDAO();
					OrgDealDAO odd = new OrgDealDAO();
					OrgDealAction oda = new OrgDealAction();
					
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
							
					HashMap[] dealOrgRolesList = odd.getRolesByOrgID(selOrgId);
					
					String ownedRoleList = "";
					ownedRoleList += "<div style='width: 100%; height: 50%'><div style='float: left; border-style: solid; border-width: 1px; border-color: #000000; overflow: scroll; width: 260px; height: 100%'>";
					HashMap curRole = null;
					boolean containFlg = false;
					int count = 0;
					for ( int i = 0; i < rolesList.length; i++ ) {
						HashMap tmpMap = rolesList[i];
						if(dealOrgRolesList==null||dealOrgRolesList.length==0){
							containFlg = false;
						}else{
							count = 0;
							for(int j=0;j<dealOrgRolesList.length;j++){
								curRole = dealOrgRolesList[j];
								if(((String)curRole.get( "ROLE_ID" )).equals( (String)tmpMap.get( "ROLE_ID" ) )){
									break;
								}else{
									count++;
								}
							}
							if(count<dealOrgRolesList.length){
								containFlg = true;
							}else{
								containFlg = false;
							}
						}
						ownedRoleList += "<div><input type='checkbox' name='role' value='" + (String) tmpMap.get( "ROLE_ID" ) + "' ";
						if(containFlg){
							ownedRoleList += "checked='checked'";
						}
						ownedRoleList += " /> " + (String) tmpMap.get( "ROLE_NAME" ) + "</div> ";

					}
					ownedRoleList += "</div></div><table><tr style ='heigt:30px'></tr></table>";
					ownedRoleList += "<div style='float: left; width:100%' class='x-client-form'><input class='tableBtn' type='button' name='button_OK' id='btnOK' value='确定' onclick='btnOper.authRole()'/></div>";
					
					res.setContentType( "text/xml;charset=UTF-8" );
					res.setHeader( "Cache-Control", "no-cache" );
//					String writeData = URLDecoder.decode( ownedRoleList, "UTF-8" );
					res.getWriter().write( ownedRoleList );
					res.addHeader( "Cache-Control", "private" );
					
					
				}

				/*sc.getRequestDispatcher( forward ).forward( req, res );*/
				return forward;


			}
			catch ( SQLException e ) {

				e.printStackTrace();
			}

		}

		return null;



	}

}
