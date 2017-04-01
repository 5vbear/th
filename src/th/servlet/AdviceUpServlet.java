package th.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import th.action.adviceup.AdviceUpAction;
import th.entity.log.AdviceBean;
import th.util.StringUtils;

public class AdviceUpServlet extends HttpServlet {
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
			foward = "/error.jsp";
		} else {
			logger.info("当前访问中mac地址不为空，mac地址： " + mac);
			request.setAttribute("mac", mac);
			String content = request.getParameter("content");
			if (StringUtils.isBlank(content)) {
				logger.info("获得到意见内容为空。");
			} else {

				logger.info("获得到意见内容不为空，内容是： " + content);
				logger.info("获得到意见的种类是： " + request.getParameter("type"));
				logger.info("表情： " + request.getParameter("expression"));

				AdviceBean aBean = new AdviceBean();
				aBean.setContent(content);
				aBean.setType(request.getParameter("type"));
				aBean.setExpression(request.getParameter("expression"));
				aBean.setMac(mac);

				if (StringUtils.isNotBlank(request.getParameter("phone"))) {
					logger.info("获得到的电话号码是： " + request.getParameter("phone"));
					aBean.setTelephone(request.getParameter("phone"));
				}
				if (StringUtils.isNotBlank(request.getParameter("email"))) {
					logger.info("获得到的邮件地址是： " + request.getParameter("email"));
					aBean.setEmail(request.getParameter("email"));

				} else {
					aBean.setEmail("");
				}
				AdviceUpAction auAction = new AdviceUpAction();
				auAction.saveAdvice(aBean);

				logger.info("用户已经保存成功。");
			}
		}

		response.addHeader("Cache-Control", "private");
		sc.getRequestDispatcher(foward).forward(request, response);
	}

}
