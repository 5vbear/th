/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.sysUpdateManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.dao.UpdateManagementDao;
import th.entity.UpdateHistorySearchBean;
import th.servlet.BaseServlet;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2013-8-27
 * @author PSET
 * @since JDK1.6
 *
 */
public class SysUpdateHistorySearchServlet extends BaseServlet {

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
			String jumpURL = "/jsp/sysUpdateManagement/sysUpdateHistorySearch.jsp";

			try {

				UpdateManagementDao umd = new UpdateManagementDao();
				
				UpdateHistorySearchBean uhsb = new UpdateHistorySearchBean();

				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				// 升级历史查询权限的判断
				if(!user.hasRight( user.getOrg_id(), String.valueOf( 101 ))){
					res.setContentType("text/html; charset=utf-8");
					res.sendRedirect("/th/jsp/common/noaction.jsp");
					return null;
				}else{
					
					// 已经进入过该页面，点选按钮后，跳回后台进行DB处理
					String dealFlg = req.getParameter( "dealFlg" );
					HashMap[] upHistoryMap = null;
					
					String conMacMark = "";
					String conFileName = "";
					String conTimeStart = "";
					String conTimeEnd = "";
					int pageIndex = 1;

					// 按钮触发，跳入该servlet
					if(dealFlg!=null&&dealFlg!=""){

						// 条件查询和数据分页显示
						if("search".equals( dealFlg )||"jump".equals( dealFlg )){
							String searchCond = (String)req.getParameter( "search_con_info" );
							if("jump".equals( dealFlg )){
								pageIndex = Integer.parseInt( (String)req.getParameter( "page_jump_index" ) );
							}
							if(searchCond!=null){
								String[] conds = searchCond.split( "," );
								if(conds.length!=4){
									logger.debug("页面跳转取得的查询条件解析不正确");
									return null;
								}

								if(!"null".equals(conds[0])){
									conMacMark = conds[0];
									uhsb.setMacMark( conMacMark );
								}
								if(!"null".equals(conds[1])){
									conFileName = conds[1];
									uhsb.setFileName( conFileName );
								}
								if(!"null".equals(conds[2])){
									conTimeStart = (String) conds[2];
								}else{
									conTimeStart = "";
								}
								uhsb.setStartTime( conTimeStart );
								
								if(!"null".equals(conds[2])){
									conTimeEnd = (String) conds[3];
								}else{
									conTimeEnd = "";
								}
								uhsb.setEndTime( conTimeEnd );

								upHistoryMap = umd.getSysUpHistorysBySearchCondition( uhsb );

							}
						}


					}

					
					// 检索条件显示设置
					req.setAttribute( "ConMacMark", conMacMark );
					req.setAttribute( "ConFileName", conFileName );
					req.setAttribute( "ConTimeStart", conTimeStart );
					req.setAttribute( "ConTimeEnd", conTimeEnd );
					// 检索取得数据的分页处理
					req.setAttribute( "CurPageIndex", pageIndex+"" );

					req.setAttribute( "searchResults", upHistoryMap );


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
