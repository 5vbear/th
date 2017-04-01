package th.action.machine;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.report.ReportCommonAction;
import th.com.util.Define4Machine;
import th.com.util.Define4Report;
import th.dao.OrgDealDAO;
import th.dao.machine.AlertDAO;
import th.user.User;
import th.util.DateUtil;
import th.util.StringUtils;


/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class AlertAction extends MachineAction {

	protected AlertDAO dao = new AlertDAO();
	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public AlertAction(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see th.action.machine.MachineAction#doIt()
	 */
	@Override
	public String doIt() {
		String result;
		if("query".equals(method) || StringUtils.isBlank(method)){
			result = query();
		}else if("repair".equals(method)){
			result = repair();
		}else if("audit".equals(method)){
			result = audit();
		}else if("deal".equals(method)){
			result = deal();
		}else if("alert_detailinfo_subwindow".equals(method)){
			result = detailinfo();
		}
		else{
			result = Define4Machine.JSP_MACHINE_INVALID;
		}
		return result;
		
	}
	
	/**
	 * 派修告警记录
	 * 
	 * @return
	*/
	public String repair(){
		String alertIds = req.getParameter("alertIds");
		User user = (User)req.getSession().getAttribute("user_info");
		String realname = user.getReal_name();
		try {
			hasRight("30");
			int result = dao.repair(alertIds,realname);
			System.out.println("成功派修"+result+"条数据!");
			return Define4Machine.SERVLET_MACHINE_ALERT_LIST+1;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
	}
	
	/**
	 * 处理告警记录
	 * 
	 * @return
	*/
	public String deal(){
		String alertIds = req.getParameter("alertIds");
		try {
			hasRight("30");
			int result = dao.deal(alertIds);
			System.out.println("成功处理"+result+"条数据!");
			return Define4Machine.SERVLET_MACHINE_ALERT_LIST+2;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
	}
	
	/**
	 * 审核告警记录
	 * 
	 * @return
	*/
	public String audit(){
		String alertIds = req.getParameter("alertIds");
		try {
			hasRight("30");
			int result = dao.audit(alertIds);
			System.out.println("成功审核"+result+"条数据!");
			return Define4Machine.SERVLET_MACHINE_ALERT_LIST+3;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
	}
	
	/**
	 * 警告信息详情
	 * 
	 * @return
	*/
	
	public String detailinfo(){
		String alertIds = req.getParameter("alertIds");
		try{
			hasRight("30");
			String orgSelect = req.getParameter("orgSelect");			
			HashMap[] maplist = dao.getMacAlertDetail(alertIds, orgSelect);
			req.setAttribute("detaillist", maplist);			
			return Define4Machine.JSP_MACHINE_ALERT_DETAIL_INFO;
		}catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String query() {
		try {
			hasRight("30");
			String orgSelect = req.getParameter("orgSelect");
			if(StringUtils.isBlank(orgSelect)){
				orgSelect = user.getOrg_id();
			}
			String suborgs = new OrgDealDAO().getSubOrg(orgSelect);
			if(StringUtils.isNotBlank(suborgs)){
				suborgs = "," + suborgs;
			}
			HashMap[] maplist = dao.getMacAlertList(req, orgSelect+suborgs);
			req.setAttribute("maclist", maplist);
			String pageIdxs = req.getParameter("pageIdx");
			req.setAttribute("orgid", req.getParameter("orgid"));
			int pageIdx = 1;
			if(StringUtils.isNotBlank(pageIdxs)){
				try {
					pageIdx = Integer.parseInt(pageIdxs);
				} catch (Exception e) {
					pageIdx = 1;
				}
			}
			String orgs = ReportCommonAction.getOrgNodes( req );
			req.setAttribute("orgSelect", orgSelect );
			req.setAttribute("orgs", orgs );
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("pageNum", (maplist.length-1)/10+1);
			return Define4Machine.JSP_MACHINE_ALERT_LIST;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}		
	}

}
