package th.servlet.updateManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.action.OrgDealAction;
import th.action.UpdateManagementAction;
import th.com.util.Define;
import th.servlet.BaseServlet;
import th.user.User;
import th.util.StringUtils;

/**
 * Descriptions
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 */
public class UpdateManagementServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long					serialVersionUID						= 1L;

	private static final String					REQ_PARAM_ACTION						= "action";
	private static final String					REQ_PARAM_VALUE_SENDUPDATEORDER			= "sendUpdateOrder";
	private static final String					REQ_PARAM_VALUE_SENDUPDATEORDER_NEW		= "sendUpdateOrder122";
	private static final String					REQ_PARAM_VALUE_BACKTOSENDUPDATEORDER	= "backToSendUpdateOrder";
	private static final String					REQ_PARAM_VALUE_BACKTOSENDUPDATEORDER_NEW	= "backToSendUpdateOrder_new";
	private static final String					REQ_PARAM_VALUE_SENDPACKAGES			= "sendPackages";
	private static final String					REQ_PARAM_VALUE_SENDPACKAGES_NEW		= "sendPackages_new";
	private static final String					REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES	= "chooseUpdatePackages";
	private static final String					REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES_NEW= "chooseUpdatePackages_new";
	private static final String					REQ_PARAM_VALUE_CHECKEDIDS				= "checkedIds";
	private static final String					REQ_PARAM_VALUE_CHECKEDALL				= "checkedAll";
	private static final String					REQ_PARAM_VALUE_OS_TYPE					= "osType";

	private static final Map<String, String>	PAGE_FORWARD							= new HashMap<String, String>();
	static {
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SENDUPDATEORDER, "/jsp/updateManagement/sendUpdateOrder.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SENDUPDATEORDER_NEW, "/jsp/updateManagement/updateSend.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_BACKTOSENDUPDATEORDER, "/jsp/updateManagement/sendUpdateOrder.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_BACKTOSENDUPDATEORDER_NEW, "/jsp/updateManagement/updateSend.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES, "/jsp/updateManagement/chooseUpdatePackages.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES_NEW, "/jsp/updateManagement/chooseUpdatePackages_new.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SENDPACKAGES_NEW, "/jsp/common/advertisementSending.jsp");
		
	}

	private UpdateManagementAction				action									= new UpdateManagementAction();

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
		String FUNCTION_NAME = "doIt() ";
		String paramAction = req.getParameter(REQ_PARAM_ACTION);
		ServletContext sc = getServletContext();
		// 获取用户信息
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");
		if (REQ_PARAM_VALUE_SENDUPDATEORDER.equals(paramAction)) {
//			String type = req.getParameter("type");
//			if("122".equals(type)){
				
				//根据当前用户取得所在组织结构
//				List<HashMap> orgList = new OrgDealAction().getChildNodesByOrgId(Long.parseLong(orgID));
//				req.setAttribute("MONITOR_ORG_MAC_LIST", monitorAction.buildMachineInOrg(orgList));
				
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
		else if(REQ_PARAM_VALUE_SENDUPDATEORDER_NEW.equals(paramAction)){
			String orgId = ((User)session.getAttribute("user_info")).getOrg_id();
			req.setAttribute("zNodes", action.getSonOrgJsonByIdFen(orgId ));
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
		}
		else if (REQ_PARAM_VALUE_BACKTOSENDUPDATEORDER_NEW.equals(paramAction)) {
			req.setAttribute("zNodes", action.getSonOrgJsonById());
			req.setAttribute( "osTypes", action.getAllOSTypes());
			req.setAttribute("yyyyMMdd", StringUtils.getCurrentYyyyMMddString());
			req.setAttribute("hour", StringUtils.getCurrent24HourString());
			req.setAttribute("minute", StringUtils.getCurrentMinuteString());
			sc.getRequestDispatcher(PAGE_FORWARD.get(paramAction)).forward(req, res);
			logger.info(FUNCTION_NAME + " page forward is: " + PAGE_FORWARD.get(action));
		} 
		else if (REQ_PARAM_VALUE_SENDPACKAGES_NEW.equals(paramAction)) {
			
			//Added by chengxq for send order 10 second later
			//修改为十秒钟之后，再发升级指令
			Calendar cal = Calendar.getInstance();//获取系统时间
//			cal.setTime( StringUtils.getYyyyMMddHHmmssDate(req.getParameter("sendDate")));
			cal.add( Calendar.SECOND, 10 );
			//Added by chengxq for send order 10 second later
			
			Boolean sendFlag = action.sendUpdatePackagesToMachine(req.getParameter("macIds"), 
					req.getParameter("packageIds"),
					req.getParameter("status"),
					cal.getTime(),
					getCurrentUserId(req));
			
			String machineID = req.getParameter("macIds");
			String machineMark = action.getMachineMarkByMachineID( machineID );
			req.setAttribute("macId", machineID);
			req.setAttribute("macName", machineMark);
			req.setAttribute("type", "updateSend");
			req.setAttribute("status", "1");
			sc.getRequestDispatcher(PAGE_FORWARD.get(paramAction)).forward(req, res);
			this.writeMessage(res, sendFlag.toString());
		}

		logger.info(FUNCTION_NAME + " end ");
		return null;
	}
	
}
