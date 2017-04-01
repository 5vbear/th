package th.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import th.util.StringUtils;

public class AdviceSaveServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Logger logger = Logger.getLogger(this.getClass());
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ServletContext sc = getServletContext();
		String mac = request.getParameter("mac");
		String foward = "/au.jsp";
		if (StringUtils.isBlank(mac)) {
			logger.info("当前访问中mac地址为空，为不合法访问。");
			request.setAttribute("alertMessage", "没有输入mac地址，请通过网银体验设备访问本页面。");
		}else{
			logger.info("当前访问中mac地址不为空，mac地址： "+mac);
			request.setAttribute("mac", mac);
		}
		
		String content=request.getParameter("content");
		if(StringUtils.isBlank(content)){
			logger.info("获得到意见内容为： "+content);
			//开始保存意见内容。
		}
		
		response.addHeader("Cache-Control", "private");
		sc.getRequestDispatcher(foward).forward(request, response);
	}

}
