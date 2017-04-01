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

import th.com.util.Define;
import th.dao.StrategyDealDAO;
import th.entity.TaskBean;
import th.servlet.BaseServlet;
import th.user.User;

/**
 * Descriptions
 *
 * @version 2014-1-4
 * @author PSET
 * @since JDK1.6
 *
 */
public class TaskListServlet extends BaseServlet {

	/** */
	private static final long serialVersionUID = -8721927899984986645L;

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
			String jumpURL = "/jsp/sysMang/taskMang/taskList.jsp";

			try {

				StrategyDealDAO sdd = new StrategyDealDAO();

				TaskBean tb = new TaskBean();
				TaskBean condTaskBean = new TaskBean();

				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				// 策略定义权限的判断
				if(!user.hasRight( user.getOrg_id(), String.valueOf( 82 ))){
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

					int pageIndex = 1;
					String condStgName = "";
					String condSendInternal = "";
					String condSendType = "";
					String saveResult = "";
					// 策略列表
					HashMap[] tasksList = null;
					// 共通条件设置
					condTaskBean.setOperator( userId );
					condTaskBean.setModifyuser( userId );
					
					String dealFlg = req.getParameter( "dealFlg" );

					// 当前servlet是由roleList处理后转入的	

					// 条件查询和数据分页显示
					if("search".equals( dealFlg )||"jump".equals( dealFlg )){
						
						String searchCond = (String)req.getParameter( "search_con_info" );
						if("jump".equals( dealFlg )){
							pageIndex = Integer.parseInt( (String)req.getParameter( "page_jump_index" ) );
						}
						if(searchCond!=null){
							String[] conds = searchCond.split( "," );						
							
							if(!"null".equals(conds[0])){
								condStgName = conds[0];	;
								condTaskBean.setStgName( condStgName );	
							}
							if(!"null".equals(conds[1])){
								condSendInternal = conds[1];
								condTaskBean.setSendInternal( condSendInternal );
							}
							if(!"null".equals(conds[2])){
								condSendType = conds[2];
								condTaskBean.setSendType( condSendType );
							}
							
						}

					// "策略定义_删除"		
					}else if("del".equals( dealFlg )){

						// 策略删除_85
						if(user.hasRight( user.getOrg_id(), String.valueOf( 85 ) )){
							long stgId = Long.parseLong( req.getParameter( "sel_stg_id" ).toString() );
							// 策略删除处理
							sdd.deleteTaskRecordByStgID( stgId );
							saveResult = "策略删除成功!";

						}else{
							jumpURL = "/jsp/common/noaction.jsp";
						}
					
					// "策略定义_启用"
					}else if("enable".equals( dealFlg )){
						
						// 策略启用_86
						if(user.hasRight( user.getOrg_id(), String.valueOf( 86 ) )){
							long stgId = Long.parseLong( req.getParameter( "sel_stg_id" ).toString() );
							// 更改当前策略的状态
							tb.setStgId( stgId );
							tb.setStatus( Define.CHAR_IDENTIFY_ONE );
							tb.setModifyuser( userId );
							// 策略启用处理
							sdd.enableTaskStatus( tb );
							saveResult = "策略启用成功!";

						}else{
							jumpURL = "/jsp/common/noaction.jsp";
						}
						
					// "策略定义_停止"
					}else if("disable".equals( dealFlg )){
						
						// 策略启用_86
						if(user.hasRight( user.getOrg_id(), String.valueOf( 86 ) )){
							long stgId = Long.parseLong( req.getParameter( "sel_stg_id" ).toString() );
							// 更改当前策略的状态
							tb.setStgId( stgId );
							tb.setStatus( Define.CHAR_IDENTIFY_ZERO );
							tb.setModifyuser( userId );
							// 策略停止处理
							sdd.disableTaskStatus( tb );
							saveResult = "策略停止成功!";

						}else{
							jumpURL = "/jsp/common/noaction.jsp";
						}
						
					}

					// 当前登陆用户所创建/修改的策略记录取得
//					tasksList = sdd.getTasksByUserId(userId);
					tasksList = sdd.getTasksBySearchCondition( condTaskBean );
					
					// 检索条件显示设置
					req.setAttribute( "ConTask", condTaskBean );
					// 检索取得数据的分页处理
					req.setAttribute( "CurPageIndex", pageIndex+"" );
					
					req.setAttribute( "TasksList", tasksList );
					req.setAttribute( "saveResult", saveResult );
					// 页面跳转
					res.addHeader( "Cache-Control", "private" );
					req.setCharacterEncoding( "UTF-8" );
					sc.getRequestDispatcher( jumpURL).forward( req, res );
				}				

			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}

		}

		return null;
	}

}
