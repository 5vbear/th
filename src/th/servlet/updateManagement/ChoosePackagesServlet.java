package th.servlet.updateManagement;

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
public class ChoosePackagesServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long					serialVersionUID						= 1L;

	private static final String					REQ_PARAM_ACTION						= "action";
	private static final String					REQ_PARAM_IDS							= "updatePackageIds";
	private static final String					REQ_PARAM_VALUE_SENDUPDATEORDER			= "sendUpdateOrder";
	private static final String					REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES	= "chooseUpdatePackages";
	private static final String					REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES_NEW	= "chooseUpdatePackages_new";
	private static final String					REQ_PARAM_VALUE_SUBMITCHOOSE			= "submitChoose";
	private static final String					REQ_PARAM_VALUE_SUBMITCHOOSE_NEW     	= "submitChoose_new";
	private static final String					REQ_PARAM_VALUE_CHECKEDIDS				= "checkedIds";
	private static final String					REQ_PARAM_VALUE_CHECKEDALL				= "checkedAll";
	private static final String					REQ_PARAM_VALUE_OS_TYPE					= "osType";
	private static final String					REQ_PARAM_VALUE_PAGEACTION				= "PageAction";
	private Pager								pager									= null;

	private static final Map<String, String>	PAGE_FORWARD							= new HashMap<String, String>();
	static {
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SENDUPDATEORDER, "/jsp/updateManagement/sendUpdateOrder.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES, "/jsp/updateManagement/chooseUpdatePackages.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES_NEW, "/jsp/updateManagement/chooseUpdatePackages_new.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_PAGEACTION, "/jsp/updateManagement/chooseUpdatePackages.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SUBMITCHOOSE, "/jsp/updateManagement/sendUpdateOrder.jsp");
		PAGE_FORWARD.put(REQ_PARAM_VALUE_SUBMITCHOOSE_NEW, "/jsp/updateManagement/updateSend.jsp");
	}

	private UpdateManagementAction				action									= new UpdateManagementAction();

	@Override
	public String doIt(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException, SQLException {
		String FUNCTION_NAME = "doIt() ";
		String paramAction = req.getParameter(REQ_PARAM_ACTION);
		ServletContext sc = getServletContext();

		if (REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES.equals(paramAction)) {
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDIDS, req.getParameter(REQ_PARAM_VALUE_CHECKEDIDS));
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDALL, req.getParameter(REQ_PARAM_VALUE_CHECKEDALL));
			req.getSession().setAttribute(REQ_PARAM_VALUE_OS_TYPE, req.getParameter(REQ_PARAM_VALUE_OS_TYPE));
		} else if (REQ_PARAM_VALUE_SUBMITCHOOSE.equals(paramAction)) {
			req.setAttribute("zNodes", action.getSonOrgJsonById());
			req.setAttribute("osTypes", action.getAllOSTypes());
			req.setAttribute("yyyyMMdd", StringUtils.getCurrentYyyyMMddString());
			req.setAttribute("hour", StringUtils.getCurrent24HourString());
			req.setAttribute("minute", StringUtils.getCurrentMinuteString());
			req.getSession().setAttribute("packagesArray", action.getUpdateDataByIds(req.getParameter(REQ_PARAM_IDS)));
		}else if (REQ_PARAM_VALUE_PAGEACTION.equals(paramAction)) {
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
		}
	
		else if (REQ_PARAM_VALUE_CHOOSEUPDATEPACKAGES_NEW.equals(paramAction)) {
			pager = PagerHelper.getPager(action.getUpdateDataCount(), Define.VIEW_NUM, req);
			pager.setResultData(action.getUpdateData(pager.getStartRow(), Define.VIEW_NUM));
			req.setAttribute("pager", pager);
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDIDS, req.getParameter(REQ_PARAM_VALUE_CHECKEDIDS));
			req.getSession().setAttribute(REQ_PARAM_VALUE_CHECKEDALL, req.getParameter(REQ_PARAM_VALUE_CHECKEDALL));
			req.getSession().setAttribute(REQ_PARAM_VALUE_OS_TYPE, req.getParameter(REQ_PARAM_VALUE_OS_TYPE));
		}
		else if (REQ_PARAM_VALUE_SUBMITCHOOSE_NEW.equals(paramAction)) {

			req.setAttribute("zNodes", action.getSonOrgJsonById());
			req.setAttribute("osTypes", action.getAllOSTypes());
			req.setAttribute("yyyyMMdd", StringUtils.getCurrentYyyyMMddString());
			req.setAttribute("hour", StringUtils.getCurrent24HourString());
			req.setAttribute("minute", StringUtils.getCurrentMinuteString());
			req.setAttribute("type", "122");
			req.getSession().setAttribute("packagesArray", action.getUpdateDataByIds(req.getParameter(REQ_PARAM_IDS)));
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
