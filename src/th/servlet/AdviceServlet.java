package th.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.action.LogListAction;
import th.action.report.ReportCommonAction;
import th.com.util.Define4Report;
import th.user.User;
import th.util.StringUtils;

public class AdviceServlet extends BaseServlet {

	/** 变量说明，值为{@value} */
	private static final long serialVersionUID = 1L;
	private static final String RIGHT_CODE = "111";

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException, SQLException {
		// TODO Auto-generated method stub

		if (req == null) {
			return null;
		}
		req.setCharacterEncoding("UTF-8");
		// session取得
		ServletContext sc = getServletContext();

		String forward = null;

		try {
			// TypeID取得
			String searchInfoId = req.getParameter("type");
			String dataType = req.getParameter( Define4Report.REQ_PARAM_DATA_TYPE );

			logger.info("searchInfoId： " + searchInfoId);

			
			if (hasRight(req, RIGHT_CODE)) {
				String orgs = ReportCommonAction.getOrgNodes(req);
				req.setAttribute("orgs", orgs);
				req.setAttribute( Define4Report.REQ_PARAM_DATA_TYPE, dataType );
				if ("doSearch".equals(searchInfoId)) {
					String selectedOrgId = (String) req.getParameter("orgSelect");
					selectedOrgId = StringUtils.isBlank(selectedOrgId) ? "1" : selectedOrgId;
					req.setAttribute("selectedOrgId", selectedOrgId);
					LogListAction action = new LogListAction();
					action.adviceSearch(req);
				}
				forward = "/jsp/log/adviceList.jsp";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			req.setAttribute("exception", e);

		} finally {
			res.addHeader("Cache-Control", "private");
			sc.getRequestDispatcher(forward).forward(req, res);
		}
		return forward;

	}

	protected boolean hasRight(HttpServletRequest req, String actionId) {

		HttpSession session = req.getSession(false);
		// 获取用户信息
		User user = (User) session.getAttribute("user_info");
		// 用户ID
		String userId = user.getId();
		// 组织ID
		String orgId = user.getOrg_id();
		boolean result = false;
		if (StringUtils.isBlank(userId)) {
			logger.debug("用户ID为空");
		}
		if (StringUtils.isBlank(orgId)) {
			logger.debug("组织ID为空");
		}
		if (user.hasRight(orgId, actionId)) {
			result = true;
		}
		return result;
	}

}
