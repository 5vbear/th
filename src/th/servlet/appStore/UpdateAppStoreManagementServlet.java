package th.servlet.appStore;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.UpdateManagementAction;
import th.servlet.BaseServlet;
import th.util.StringUtils;

/**
 * Descriptions
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 */
public class UpdateAppStoreManagementServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long					serialVersionUID						= 1L;

	private static final String					REQ_PARAM_ACTION						= "action";
	private static final String					REQ_PARAM_VALUE_SENDUPDATEAPPORDER			= "sendUpdateAppOrder";
	private static final String					REQ_PARAM_VALUE_BACKTOSENDUPDATEORDER	= "backToSendUpdateAppOrder";
	private static final String					REQ_PARAM_VALUE_SENDPACKAGES			= "sendPackages";
	private static final String					REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES	= "chooseUpdatePackages";
	private static final String					REQ_PARAM_VALUE_CHECKEDIDS				= "checkedIds";
	private static final String					REQ_PARAM_VALUE_CHECKEDALL				= "checkedAll";
	private static final String					REQ_PARAM_VALUE_OS_TYPE					= "osType";

	private static final Map<String, String>	PAGE_FORWARD							= new HashMap<String, String>();
	static {
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SENDUPDATEAPPORDER, "/jsp/appStore/sendUpdateAppOrder.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_BACKTOSENDUPDATEORDER, "/jsp/appStore/sendUpdateAppOrder.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES, "/jsp/appStore/chooseUpdateAppStore.jsp");
	}

	private UpdateManagementAction				action									= new UpdateManagementAction();

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
		String FUNCTION_NAME = "doIt() ";
		String paramAction = req.getParameter(REQ_PARAM_ACTION);
		ServletContext sc = getServletContext();

		if (REQ_PARAM_VALUE_SENDUPDATEAPPORDER.equals(paramAction)) {
			req.setAttribute("zNodes", action.getSonOrgJsonById());
			req.setAttribute( "osTypes", action.getAllOSTypes());
			req.setAttribute("yyyyMMdd", StringUtils.getCurrentYyyyMMddString());
			req.setAttribute("hour", StringUtils.getCurrent24HourString());
			req.setAttribute("minute", StringUtils.getCurrentMinuteString());
			req.getSession().setAttribute("packagesArray", null);
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDIDS, null);
			req.getSession().setAttribute(REQ_PARAM_VALUE_OS_TYPE, null);
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDALL, null);
			sc.getRequestDispatcher(PAGE_FORWARD.get(paramAction)).forward(req, res);
			logger.info(FUNCTION_NAME + " page forward is: " + PAGE_FORWARD.get(action));
		}else if (REQ_PARAM_VALUE_BACKTOSENDUPDATEORDER.equals(paramAction)) {
			req.setAttribute("zNodes", action.getSonOrgJsonById());
			req.setAttribute( "osTypes", action.getAllOSTypes());
			req.setAttribute("yyyyMMdd", StringUtils.getCurrentYyyyMMddString());
			req.setAttribute("hour", StringUtils.getCurrent24HourString());
			req.setAttribute("minute", StringUtils.getCurrentMinuteString());
			sc.getRequestDispatcher(PAGE_FORWARD.get(paramAction)).forward(req, res);
			logger.info(FUNCTION_NAME + " page forward is: " + PAGE_FORWARD.get(action));
		} 
		else if (REQ_PARAM_VALUE_SENDPACKAGES.equals(paramAction)) {
			Boolean sendFlag = action.sendUpdatePackagesToMachine(req.getParameter("macIds"), 
					req.getParameter("packageIds"),
					req.getParameter("status"),
					StringUtils.getYyyyMMddHHmmssDate(req.getParameter("sendDate")),
					getCurrentUserId(req));
			this.writeMessage(res, sendFlag.toString());
		}

		logger.info(FUNCTION_NAME + " end ");
		return null;
	}
	
}
