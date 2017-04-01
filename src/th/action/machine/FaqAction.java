package th.action.machine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.com.util.Define4Machine;
import th.dao.ClientDAO;
import th.dao.MonitorDAO;
import th.dao.machine.FaqDAO;
import th.dao.machine.MachineDAO;
import th.util.StringUtils;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-9-10
 * @author PSET
 * @since JDK1.6
 */
public class FaqAction extends MachineAction {
	
	private FaqDAO dao = new FaqDAO();
	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public FaqAction(HttpServletRequest req, HttpServletResponse res) {
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
		}else if("view".equals(method)){
			result = view();
		}else if("update".equals(method)){
			result = update();
		}else if("doupdate".equals(method)){
			result = update2();
		}else{
			result = Define4Machine.JSP_MACHINE_INVALID;;
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @return
	*/
	private String view() {
		String pageIdx = req.getParameter("pageIdx");
		String fid = req.getParameter("fid");
		int view =1;
		try {
			HashMap faqinfo = dao.getFaqInfoById(fid,view);
			faqinfo = this.replaceMacTypeToMap(faqinfo);
			req.setAttribute("faqinfo", faqinfo);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("faqType", req.getParameter("faqType"));
			req.setAttribute("machineName", req.getParameter("machineName"));
			req.setAttribute("machineType", req.getParameter("machineType"));
			req.setAttribute("SelectOrg", req.getParameter("SelectOrg"));
			try {
				req.setAttribute("MTmap",new MonitorDAO().getMacType());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return Define4Machine.JSP_MACHINE_FAQ_VIEW;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}		
	}
	/**
	 * 
	 * 
	 * @return
	*/
	private String update() {
		String pageIdx = req.getParameter("pageIdx");
		String fid = req.getParameter("fid");
		int update =2;
		try {
			HashMap faqinfo = dao.getFaqInfoById(fid,update);
			faqinfo = this.replaceMacTypeToMap(faqinfo);
			req.setAttribute("faqinfo", faqinfo);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("fid", req.getParameter("fid"));
			req.setAttribute("faqType", (String)(faqinfo.get("HELP_ID")));
			req.setAttribute("machineName", req.getParameter("machineName"));
			req.setAttribute("machineType", req.getParameter("machineType"));
			req.setAttribute("SelectOrg", req.getParameter("SelectOrg"));
			return Define4Machine.JSP_MACHINE_FAQ_UPDATE;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}		
	}
	/**
	 * 
	 * 
	 * @return
	*/
	private String update2() {
		String pageIdx = req.getParameter("pageIdx");
		String fid = req.getParameter("fid");
		try {
			dao.updateFaqInfoById(fid,req);
			req.setAttribute("pageIdx", pageIdx);
			
			req.setAttribute("faqType", req.getParameter("faqType"));
			req.setAttribute("machineName", req.getParameter("machineName"));
			req.setAttribute("machineType", req.getParameter("machineType"));
			req.setAttribute("SelectOrg", req.getParameter("SelectOrg"));
			HashMap[] faqlist = dao.getFaqList(req);
			req.setAttribute("faqlist", faqlist);
			req.setAttribute("pageNum", (faqlist.length-1)/10+1);
			return Define4Machine.JSP_MACHINE_FAQ_LIST;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
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
			String pageIdxs = req.getParameter("pageIdx");
			HashMap[] faqlist = dao.getFaqList(req);
			int pageIdx = 1;
			if(StringUtils.isNotBlank(pageIdxs)){
				try {
					pageIdx = Integer.parseInt(pageIdxs);
				} catch (Exception e) {
					pageIdx = 1;
				}
			}
			faqlist = this.replaceMacType(faqlist);
			req.setAttribute("faqlist", faqlist);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("pageNum", (faqlist.length-1)/10+1);
			req.setAttribute("faqType", req.getParameter("faqType"));
			req.setAttribute("machineName", req.getParameter("machineName"));
			req.setAttribute("machineType", req.getParameter("machineType"));
			req.setAttribute("SelectOrg", req.getParameter("SelectOrg"));
			try {
				req.setAttribute("MTmap",new MonitorDAO().getMacType());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return Define4Machine.JSP_MACHINE_FAQ_LIST;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
		
	}

	private HashMap[] replaceMacType(HashMap[] faqlist){
		HashMap<String, String> map = new ClientDAO().getMacType();
		String mType = "";
		String os = "";
		String macKind = "";
		for(int i = 0; i < faqlist.length;i++ ){
			mType = (String)(faqlist[i].get("MACHINE_TYPE"));
			os = (String)(faqlist[i].get("OS"));
			macKind = (String)(faqlist[i].get("MACHINE_KIND"));
			if(-1 != os.indexOf("Win")){
				os = "Win";
			}
			String macTypeTemp = os + "_" + macKind;
			faqlist[i].put("MACHINE_TYPE", map.get(macTypeTemp).toString());
		}
		
		return faqlist;
	}
	
	private HashMap replaceMacTypeToMap(HashMap faqinfo){
		HashMap<String, String> map = new ClientDAO().getMacType();
		String mType = (String)(faqinfo.get("MACHINE_TYPE"));
		String os = (String)(faqinfo.get("OS"));
		String macKind = (String)(faqinfo.get("MACHINE_KIND"));
		if(-1 != os.indexOf("Win")){
			os = "Win";
		}
		String macTypeTemp = os + "_" + macKind;
		faqinfo.put("MACHINE_TYPE", map.get(macTypeTemp).toString());
		
		return faqinfo;
	}
}
