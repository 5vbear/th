package th.servlet.interactiveAPI;

import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import th.dao.MobileDAO;

public class AppStoreUploadServlet extends HttpServlet {

	/**
	 * 应用商店信息下载API
	 */
	private static final long	serialVersionUID	= 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doIt(req, res);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doIt(req, res);
	}
	
	private void doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			String macType = req.getParameter("macType");
			JSONArray obj = JSONArray.fromObject(new MobileDAO().getAppStoreUploadList(macType));
			String jsonStr = obj.toString();
//			jsonStr = new String(jsonStr.getBytes(),"GB2312");
			
			res.setContentType("text/xml;charset=GB2312");
			res.setHeader("Cache-Control", "no-cache");
//			res.setCharacterEncoding("GB2312");
			res.getWriter().write(jsonStr);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}  
}
