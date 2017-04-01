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

import th.dao.DptDealDAO;
import th.entity.DepartmentBean;
import th.servlet.BaseServlet;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2013-8-15
 * @author PSET
 * @since JDK1.6
 *
 */
public class DptDealServlet extends BaseServlet {

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
	    	String jumpURL = "/jsp/sysMang/dptMang/dptDeal.jsp";
	    	
	    	try {
				
				DptDealDAO ddd = new DptDealDAO();				
				DepartmentBean dpt = new DepartmentBean();
				
				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				String dealFlg = req.getParameter( "dealFlg" );
				// 部门添加、编辑权限的判断
				if(("add".equals( dealFlg )&&!user.hasRight( user.getOrg_id(), String.valueOf( 47 )))
						||("change".equals( dealFlg )&&!user.hasRight( user.getOrg_id(), String.valueOf( 48 )))){
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
					// 当前用户所属组织List构成
					String ownedOrgList = "";
					long topOrgId = -1;
					String enabled = "";
					
					// 页面跳转标识
					String pageIndex = req.getParameter( "page_index" );

					// 当前servlet是有dptDeal.jsp页面跳转过来的
					if(pageIndex!=null&&pageIndex!=""){

						// 添加或修改部门信息，根据"hide_dpt_id"进行具体判断
						if("add/change".equals( dealFlg )){
							long inputOrgId = Long.parseLong( (String)req.getParameter( "sel_org_id" ) );
							String inputDptName = (String)req.getParameter( "input_dpt_name" );
							String inputDptDescription = (String)req.getParameter( "input_dpt_desp" );
							String inputDptManager = (String)req.getParameter( "input_dpt_manager" );
							String inputManagerEmail = (String)req.getParameter( "input_manager_email" );
							String inputManagerTel = (String)req.getParameter( "input_manager_tel" );
							String inputManagerOthCont = (String)req.getParameter( "input_manager_otherContacts" );

							dpt.setOrgId( inputOrgId );
							dpt.setDptName( inputDptName );

							dpt.setDptDescription( inputDptDescription );
							dpt.setManagerName( inputDptManager );
							dpt.setManagerMail( inputManagerEmail );
							dpt.setManagerTel( inputManagerTel );
							dpt.setOtherContacts( inputManagerOthCont );
							dpt.setOperator( userId );

							long hideDptId = Long.parseLong( (String)req.getParameter( "hide_dpt_id" ) );
							// add操作
							if(hideDptId==-1){

								// 部门添加_47
								if(user.hasRight( String.valueOf( inputOrgId ), String.valueOf( 47 ) )){
									// 判断当前部门名称是否在对应inputOrgId中已经存在
									if(-1==ddd.getDptIdByDptNameAndOrgId( inputOrgId, inputDptName )){
										// 插入一条新的记录
										ddd.insertDepartmentRecord( dpt );
										saveResult = "部门添加成功!";
										pageTitle = "部门管理-部门信息添加";
									}	
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}	

							}else{

								// 部门修改_48
								if(user.hasRight( String.valueOf( inputOrgId ), String.valueOf( 48 ) )){
									// 更新原有记录
									dpt.setDptId( hideDptId );
									ddd.updateDepartmentRecord( dpt );
									saveResult = "部门修改成功!";
									pageTitle = "部门管理-部门信息编辑";
								}else{
									jumpURL = "/jsp/common/noaction.jsp";
								}	

							}

						}
						
						ownedOrgList = (String)req.getParameter( "hide_org_select" );
						enabled = "disabled='disabled'";
					
					// 当前servlet是由dptSearch.jsp页面跳转过来的
					}else{

						if("add".equals( dealFlg )){
							pageTitle = "部门管理-部门信息添加";
							enabled = "";
						}else if("change".equals( dealFlg )){
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

									pageTitle = "部门管理-部门信息编辑";
									enabled = "disabled='disabled'";

								}
							}

						}
						
						String ownedOrgAppender = (String)req.getParameter( "org_list_info" );
						if(ownedOrgAppender!=null&&!"".equals( ownedOrgAppender )){
							String[] ownedOrgArray = ownedOrgAppender.split( "," );
							topOrgId = Long.parseLong( (String)ownedOrgArray[0].split( "_" )[1] );
							for(int i=0;i<ownedOrgArray.length;i++){
								String[] selectOperation = ownedOrgArray[i].split( "_" );
								ownedOrgList += "<option value='" + selectOperation[1] + "'>" + selectOperation[0] + "</option>";
							}
						}
						
						

					}

					req.setAttribute( "orgSelect", ownedOrgList );
					req.setAttribute( "orgListTopId", topOrgId+"" );
					// 选中部门原有信息显示
					req.setAttribute( "selDptBean", dpt );
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
