package th.servlet.interactiveAPI;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.AdvertManagementAction;
import th.servlet.BaseServlet;

public class SubtitlesListGetServlet extends BaseServlet {
	
	/** */
	private static final long serialVersionUID = 3940024686514739248L;
	
	public String doIt(HttpServletRequest req, HttpServletResponse res)
	throws ServletException, IOException {

		// Log.debug( className + " MonitorServlet start..." );
		String forward = null;
		
		req.setCharacterEncoding("UTF-8");
		ServletContext sc = getServletContext();
		try {
			AdvertManagementAction action = new AdvertManagementAction();
			String result = action.getSubtitlesList(req);
			forward = "/jsp/advert/SubtitlesShow.jsp";
		} catch (Exception e) {
			logger.error(e.getMessage());
			req.setAttribute("exception", e);
		
		} finally {
			res.addHeader("Cache-Control", "private");
			if (forward != null) {
				sc.getRequestDispatcher(forward).forward(req, res);
			}
		}
		return forward;
	}
}
