/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.servlet.sysMang.devMang;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.dao.EBankDeviceDAO;
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
public class EBankDeviceListServlet extends BaseServlet {

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
			String jumpURL = "/jsp/sysMang/devMang/ebDeviceList.jsp";

			try {

				EBankDeviceDAO edd = new EBankDeviceDAO();


				// 获取用户信息
				User user = (User) session.getAttribute("user_info");
				// 网银设备管理权限的判断
				if(!user.hasRight( user.getOrg_id(), String.valueOf( 114 ))){
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

					String dealFlg = req.getParameter( "dealFlg" );

					// 当前servlet是由roleList处理后转入的	

					// "网银设备定义_删除"		
					if("del".equals( dealFlg )){

						// 网银设备删除_117
						if(user.hasRight( user.getOrg_id(), String.valueOf( 117 ) )){
							long devId = Long.parseLong( req.getParameter( "sel_dev_id" ).toString() );
							// 网银设备删除处理
							edd.deleteEBankDeviceByDevID( devId );

						}else{
							jumpURL = "/jsp/common/noaction.jsp";
						}

					}

					HashMap[] ebDevList = edd.getAllEBankDevices();

					req.setAttribute( "ebDevList", ebDevList );
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
