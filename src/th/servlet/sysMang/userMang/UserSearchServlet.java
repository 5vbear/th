/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.sysMang.userMang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.action.OrgDealAction;
import th.com.util.Define;
import th.dao.OrgDealDAO;
import th.dao.UserDao;
import th.entity.SearchConditionBean;
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
public class UserSearchServlet extends BaseServlet {

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
			String jumpURL = "/jsp/sysMang/userMang/userSearch.jsp";

			try {

				OrgDealDAO odd = new OrgDealDAO();
				OrgDealAction oda = new OrgDealAction();
				UserDao ud = new UserDao();

				SearchConditionBean scb = new SearchConditionBean();
				
				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				// 用户管理权限的判断
				if(!user.hasRight( user.getOrg_id(), String.valueOf( 51 ))){
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


					// 当前用户所在组织为根节点的组织树
					List userOrgs = null;
					// 查询条件取得的用户信息Map
					HashMap[] userSerMap = null;


					// 已经进入过该页面，点选按钮后，跳回后台进行DB处理
					String dealFlg = req.getParameter( "dealFlg" );
					long conOrgId = -1;
					String conDptName = "";
					String conUserName = "";
					String searchOrgIdList = "";
					boolean orgActionFlg = false;
					int pageIndex = 1;
					String saveResult = "";

					// 按钮触发，跳入该servlet
					if(dealFlg!=null&&dealFlg!=""){

						// 条件查询和数据分页显示
						if("search".equals( dealFlg )||"jump".equals( dealFlg )){
							userOrgs = oda.getChildNodesByUserID( userId );
							String searchCond = (String)req.getParameter( "search_con_info" );
							if("jump".equals( dealFlg )){
								pageIndex = Integer.parseInt( (String)req.getParameter( "page_jump_index" ) );
							}
							if(searchCond!=null){
								String[] conds = searchCond.split( "," );
								conOrgId = Long.parseLong( conds[0] );
								// 当所属组织为全部时，允许在当前用户所在组织及其子组织中进行部门和用户搜索
								if(conOrgId==-1){
									searchOrgIdList = oda.getIdStrByList( userOrgs );
									orgActionFlg = true;
								}else{
									searchOrgIdList = "(" + conOrgId + ")";
									orgActionFlg = oda.checkOrgIdExist( conOrgId, userOrgs );
								}
								// 页面点选的组织在该用户所在组织列表中，可以进行操作
								if(orgActionFlg){
									scb.setSearchOrgStr( searchOrgIdList );
									if(!"null".equals(conds[1])){
										conDptName = conds[1];
										scb.setDptName( conDptName );
									}
									if(!"null".equals(conds[2])){
										conUserName = conds[2];
										scb.setUserName( conUserName );
									}
									userSerMap = ud.getUsersBySearchCondition( scb );
								}
							}

						// 删除用户信息，以及与之关联的角色_组织映射表
						}else if("del".equals( dealFlg )){
							// 获取选中的用户Id
							String tmpUserIDStr = (String)req.getParameter( "sel_user_id" );
							if(tmpUserIDStr!=null&&!"".equals( tmpUserIDStr )){
								long selUserId = Long.parseLong( tmpUserIDStr );						
								UserBean curUser = ud.getUserBeanByUserId( selUserId );

								// 用户删除_54
								if(user.hasRight( String.valueOf(curUser.getOrgId()), String.valueOf( 54 ) )){
									// 在用户表中删除该UserID对应的用户记录
									ud.deleteUserByUserID( selUserId );
									// 在组织_角色映射表中删除该UserID对应的映射记录
									odd.deleteRoleUserMappingByUserId( selUserId );
									saveResult = "用户删除成功!";
									
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}

							}
							
						// 重置用户密码，设置为默认密码
						}else if("reset".equals( dealFlg )){
							// 获取选中的用户Id
							String tmpUserIDStr = (String)req.getParameter( "sel_user_id" );
							if(tmpUserIDStr!=null&&!"".equals( tmpUserIDStr )){
								long selUserId = Long.parseLong( tmpUserIDStr );						
								UserBean curUser = ud.getUserBeanByUserId( selUserId );

								// 用户修改_53
								if(user.hasRight( String.valueOf( curUser.getOrgId() ), String.valueOf( 53 ) )){
									// 在用户表中更新该UserID对应的密码
									curUser.setPassword( Define.DEFALULT_PASSWORD );
									curUser.setOperator( selUserId );
									ud.updateUserPassword(curUser);
									saveResult = "用户密码重置成功!";
									
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}

							}
						
						// 批量导入用户，需要具有添加用户及为用户授权的权限
						}else if("bathIn".equals( dealFlg )){
							
							// 用户添加_52/用户授权_55
							if(user.hasRight( user.getOrg_id(), String.valueOf( 52 ) )
									&&user.hasRight( user.getOrg_id(), String.valueOf( 55 ) )){
								
								req.setCharacterEncoding("utf-8");
								//指定以UTF-8编码读入
								InputStreamReader isr = new InputStreamReader(req.getInputStream(),"UTF-8"); 
								BufferedReader br = new BufferedReader(isr);
								
								int linenum = 1;
								int linecnt = 0;
								String line = "";
								while ((line = br.readLine()) != null) {
									logger.info( "InputFile's current Line  = " + line );
									if(line.startsWith("###")){
										line = line.replace("###", "");
										line = line.replace("\r\n", "");
										if(oda.checkUserRecord(line, linenum, user)){
											linecnt++;
										}
										System.out.println(line);
									}
									linenum++;

								}
								//关闭IO流
								br.close();	
								if(linecnt>0){
									logger.info( "共有"+linecnt+"条用户数据通过批量导入添加成功!" );
								}
								saveResult = "共有"+linecnt+"条用户数据通过批量导入添加成功!";
								
								
							}else{
								jumpURL = "/jsp/common/noaction.jsp";
							}
							
						}

					}


					// 由左侧列表进入，初始页面加载

					// 获取该用户所在组织树
					if(userId!=-1){
						userOrgs = oda.getChildNodesByUserID( userId );
					}

					// 组织列表树构成
					HashMap orgNode = null;
					long orgId = -1;
					long parentOrgId = -1;
					String orgName = "";
					int org_level = 0;
					StringBuffer sb = new StringBuffer();

					sb.append( "[ " );
					for (int i=0;i<userOrgs.size();i++){

						orgNode = (HashMap)userOrgs.get( i );
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


					req.setAttribute( "zNodes", sb.toString() );
					// 检索条件显示设置
					req.setAttribute( "ConOrgId", conOrgId+"" );
					req.setAttribute( "ConDptName", conDptName );
					req.setAttribute( "ConUserName", conUserName );
					// 检索取得数据的分页处理
					req.setAttribute( "CurPageIndex", pageIndex+"" );

					req.setAttribute( "searchResults", userSerMap );
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
