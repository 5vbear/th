package th.servlet.iosManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.UpdateManagementAction;
import th.com.property.LocalProperties;
import th.com.util.Define;
import th.servlet.BaseServlet;
import th.util.HttpUtils;

/**
 * Descriptions
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 */
public class IosManagementServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long					serialVersionUID						= 1L;

	private static final String					REQ_PARAM_ACTION						= "action";
	private static final String					REQ_PARAM_VALUE_SENDUPDATEORDER			= "sendUpdateOrder";
	private static final String					REQ_PARAM_VALUE_INSTALL					= "install";
	private static final String					REQ_PARAM_VALUE_DELETE					= "delete";
	private static final String					REQ_PARAM_VALUE_CHECKEDIDS				= "checkedIds";
	private static final String					REQ_PARAM_VALUE_CHECKEDALL				= "checkedAll";
	private static final String					REQ_PARAM_VALUE_OS_TYPE					= "osType";

	private static final Map<String, String>	PAGE_FORWARD							= new HashMap<String, String>();
	static {
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SENDUPDATEORDER, "/jsp/iosSystemManagement/iosSysSend.jsp");
	}

	private UpdateManagementAction				action									= new UpdateManagementAction();

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
		String FUNCTION_NAME = "doIt() ";
		String paramAction = req.getParameter(REQ_PARAM_ACTION);
		ServletContext sc = getServletContext();
		//Define.HOST_SERVER+"/thccb/iosapp/"+
		String plistFileName = LocalProperties.getProperty("FTP_IOS_WEB_PARAM_LIST_NAME");
		String plistFileName_new = LocalProperties.getProperty("FTP_IOS_WEB_PARAM_LIST_NAME_TWO");
		
		if (REQ_PARAM_VALUE_SENDUPDATEORDER.equals(paramAction)) {
			req.setAttribute("zNodes", action.getIosOrgJsonById());
			Object [] object = new Object[2];
			object[0] = plistFileName;
			object[1] = plistFileName_new;
//			req.getSession().setAttribute("packagesArray", action.getIosFile());
			req.getSession().setAttribute("packagesArray", object);
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDIDS, null);
			req.getSession().setAttribute(REQ_PARAM_VALUE_OS_TYPE, null);
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDALL, null);
			sc.getRequestDispatcher(PAGE_FORWARD.get(paramAction)).forward(req, res);
			logger.info(FUNCTION_NAME + " page forward is: " + PAGE_FORWARD.get(action));
			
		}else if (REQ_PARAM_VALUE_INSTALL.equals(paramAction)) {
			//http://thccb.suntec.net/thmdm/mdmcontrol?command=InstallApplication&machineid=654321&app=tianheDemo.plist&action=a
			
			String [] macIds = ((String)req.getParameter("macIds")).split(",");
			String fileFtpUrl = LocalProperties.getProperty("FTP_IOS_WEB_INSTALL");

			for (int i = 0; i < macIds.length; i++) {
				String urlstring = fileFtpUrl+macIds[i]+"&app="+plistFileName+"&action=a";
				logger.info(FUNCTION_NAME + "安装路径为: "+urlstring);
				String returnMessage = HttpUtils.access(urlstring);
				logger.info(FUNCTION_NAME + "安装路径为: "+returnMessage);
				String urlstring_new = fileFtpUrl+macIds[i]+"&app="+plistFileName_new+"&action=a";
				logger.info(FUNCTION_NAME + "安装路径为: "+urlstring_new);
				String returnMessage_new = HttpUtils.access(urlstring);
				
				
	
				logger.info(FUNCTION_NAME + "安装路径为: "+returnMessage_new);

			}
			this.writeMessage(res, "true");

		}else if (REQ_PARAM_VALUE_DELETE.equals(paramAction)) {
			//http://thccb.suntec.net/thmdm/mdmcontrol?command=RemoveApplication&machineid=654321
			String [] macIds = ((String)req.getParameter("macIds")).split(",");
			String fileFtpUrl = LocalProperties.getProperty("FTP_IOS_WEB_REMOVE");
			for (int i = 0; i < macIds.length; i++) {
				String urlstring = fileFtpUrl+macIds[i];
				String returnMessage = HttpUtils.access(urlstring);
				logger.info(FUNCTION_NAME + "删除路径为: "+urlstring);
				logger.info(FUNCTION_NAME + "删除路径为: "+returnMessage);
			}
			this.writeMessage(res, "true");
		}
	

		logger.info(FUNCTION_NAME + " end ");
		return null;
	}
	
}
