package th.servlet.sysUpdateManagement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.UpdateManagementAction;
import th.com.util.Define;
import th.servlet.BaseServlet;
import th.taglib.Pager;
import th.taglib.PagerHelper;
import th.util.StringUtils;

/**
 * Descriptions
 * @version 2013-8-13
 * @author PSET
 * @since JDK1.6
 */
public class SysChoosePackagesServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long					serialVersionUID						= 1L;

	private static final String					REQ_PARAM_ACTION						= "action";
	private static final String					REQ_PARAM_IDS							= "updatePackageIds";
	private static final String					REQ_PARAM_VALUE_SENDUPDATEORDER			= "sendUpdateOrder";
	private static final String					REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES	= "chooseUpdatePackages";
	private static final String					REQ_PARAM_VALUE_SUBMITCHOOSE			= "submitChoose";
	private static final String					REQ_PARAM_VALUE_CHECKEDIDS				= "checkedIds";
	private static final String					REQ_PARAM_VALUE_CHECKEDALL				= "checkedAll";
	private static final String					REQ_PARAM_VALUE_OS_TYPE					= "osType";
	private static final String					REQ_PARAM_VALUE_PAGEACTION				= "PageAction";
	private Pager								pager									= null;

	private static final Map<String, String>	PAGE_FORWARD							= new HashMap<String, String>();
	static {
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SENDUPDATEORDER, "/jsp/sysUpdateManagement/sysSendUpdateOrder.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES, "/jsp/sysUpdateManagement/sysChooseUpdatePackages.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_PAGEACTION, "/jsp/sysUpdateManagement/sysChooseUpdatePackages.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SUBMITCHOOSE, "/jsp/sysUpdateManagement/sysSendUpdateOrder.jsp");
	}

	private UpdateManagementAction				action									= new UpdateManagementAction();

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
		String FUNCTION_NAME = "doIt() ";
		String paramAction = req.getParameter(REQ_PARAM_ACTION);
		ServletContext sc = getServletContext();

		if (REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES.equals(paramAction)) {
			pager = PagerHelper.getPager(action.getSysUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getSysUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDIDS, req.getParameter(REQ_PARAM_VALUE_CHECKEDIDS));
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDALL, req.getParameter(REQ_PARAM_VALUE_CHECKEDALL));
			req.getSession().setAttribute(REQ_PARAM_VALUE_OS_TYPE, req.getParameter(REQ_PARAM_VALUE_OS_TYPE));
		} else if (REQ_PARAM_VALUE_SUBMITCHOOSE.equals(paramAction)) {
			req.setAttribute("zNodes", action.getSysSonOrgJsonById());
			req.setAttribute("osTypes", action.getAllOSTypes());
			req.setAttribute("yyyyMMdd", StringUtils.getCurrentYyyyMMddString());
			req.setAttribute("hour", StringUtils.getCurrent24HourString());
			req.setAttribute("minute", StringUtils.getCurrentMinuteString());
			req.getSession().setAttribute("packagesArray", action.getSysUpdateDataByIds(req.getParameter(REQ_PARAM_IDS)));
		} else if (REQ_PARAM_VALUE_PAGEACTION.equals(paramAction)) {
			pager = PagerHelper.getPager(action.getSysUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getSysUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
		}

		sc.getRequestDispatcher(PAGE_FORWARD.get(paramAction)).forward(req, res);
		logger.info(FUNCTION_NAME + " page forward is: " + PAGE_FORWARD.get(paramAction));

		logger.info(FUNCTION_NAME + " end ");
		return null;
	}
	
	public HashMap[] getPackagesArray(HttpServletRequest req) throws SQLException{
		HashMap[] newPackagesArray = action.getUpdateDataByIds(req.getParameter(REQ_PARAM_IDS));
		HashMap[] oldPackagesArray = (HashMap[])req.getSession().getAttribute("packagesArray");
		if(oldPackagesArray==null) return newPackagesArray;
		if(newPackagesArray==null) return newPackagesArray;
		Map<String,HashMap> allPackagesMap = new HashMap<String,HashMap>();
		for (HashMap hashMap : newPackagesArray) {
			allPackagesMap.put((String)hashMap.get("FILE_ID"), hashMap);
		}
		for (HashMap hashMap : oldPackagesArray) {
			allPackagesMap.put((String)hashMap.get("FILE_ID"), hashMap);
		}
		int i=0;
		HashMap[] allPackagesArray = new HashMap[allPackagesMap.size()];
        Set<Map.Entry<String, HashMap>> set = allPackagesMap.entrySet();
        for (Iterator<Map.Entry<String, HashMap>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, HashMap> entry = (Map.Entry<String, HashMap>) it.next();
            allPackagesArray[i++] = (HashMap) it.next();
        }
		return allPackagesArray;
	}

}
