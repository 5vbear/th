/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.sysMang.dptMang;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.action.OrgDealAction;
import th.dao.DptDealDAO;
import th.dao.OrgDealDAO;
import th.dao.RoleDealDAO;
import th.entity.DepartmentBean;
import th.entity.OrganizationBean;
import th.entity.RoleObjectManagementBean;
import th.entity.SearchConditionBean;
import th.servlet.BaseServlet;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2013-8-14
 * @author PSET
 * @since JDK1.6
 *
 */
public class DptSearchServlet extends BaseServlet {

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
	    	String jumpURL = "/jsp/sysMang/dptMang/dptSearch.jsp";

			try {

				OrgDealDAO odd = new OrgDealDAO();
				OrgDealAction oda = new OrgDealAction();
				DptDealDAO ddd = new DptDealDAO();

				RoleObjectManagementBean romb = new RoleObjectManagementBean();
				SearchConditionBean scb = new SearchConditionBean();
				DepartmentBean dpt = new DepartmentBean();

				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				// 部门管理权限的判断
				if(!user.hasRight( user.getOrg_id(), String.valueOf( 46 ))){
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
					// 查询条件取得的部门信息Map
					HashMap[] dptSerMap = null;


					// 已经进入过该页面，点选按钮后，跳回后台进行DB处理
					String dealFlg = req.getParameter( "dealFlg" );
					long conOrgId = -1;
					long conDptId = -1;
					String conManagerName = "";
					String searchOrgIdList = "";
					boolean orgActionFlg = false;
					int pageIndex = 1;

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
								// 当所属组织为全部时，允许在当前用户所在组织及其子组织中进行部门和负责人搜索
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
										conDptId = Long.parseLong( conds[1] );
										scb.setDptId( conDptId );
									}
									if(!"null".equals(conds[2])){
										conManagerName = conds[2];
										scb.setManagerName( conManagerName );
									}
									dptSerMap = ddd.getDepartmentBySearchCondition( scb );
								}
							}
							
						// 删除部门信息，以及与之关联的角色_组织映射表
						}else if("del".equals( dealFlg )){
							// 获取选中的部门Id
							String tmpDptIDStr = (String)req.getParameter( "sel_dept_id" );
							if(tmpDptIDStr!=null&&!"".equals( tmpDptIDStr )){						
								long selDptId = Long.parseLong( tmpDptIDStr );
								HashMap curDpt = (HashMap)ddd.getDepartmentByDptId( selDptId )[0];

								// 部门删除_49
								if(user.hasRight( (String)curDpt.get( "ORG_ID" ), String.valueOf( 49 ) )){
									// 在部门表中删除该dptID对应的部门记录
									ddd.deleteDptByDptID( selDptId );
									// 在组织_角色映射表中删除该dptID对应的映射记录
									odd.deleteRoleDptMappingByDptId( selDptId );
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}
			
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
					req.setAttribute( "ConDptId", conDptId+"" );
					req.setAttribute( "ConManagerName", conManagerName );
					// 检索取得数据的分页处理
					req.setAttribute( "CurPageIndex", pageIndex+"" );

					req.setAttribute( "searchResults", dptSerMap );


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
