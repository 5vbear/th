package th.action.machine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.MonitorAction;
import th.action.OrgDealAction;
import th.com.util.Define4Machine;
import th.dao.DptDealDAO;
import th.dao.machine.AlertDAO;
import th.dao.machine.FaqDAO;
import th.dao.machine.MachineDAO;
import th.dao.machine.RepairDAO;
import th.user.User;
import th.util.StringUtils;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-9-10
 * @author PSET
 * @since JDK1.6
 */
public class RepairAction extends MachineAction {
	
	private RepairDAO dao = new RepairDAO();
	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public RepairAction(HttpServletRequest req, HttpServletResponse res) {
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
			HashMap repairinfo = dao.getRepairInfoById(fid,view);
			req.setAttribute("repairinfo", repairinfo);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("SelectOrg", req.getParameter("SelectOrg"));
			req.setAttribute("machineName", req.getParameter("machineName"));
			req.setAttribute("operateTime_s", req.getParameter("operateTime_s"));
			req.setAttribute("operateTime_e", req.getParameter("operateTime_e"));
			return Define4Machine.JSP_MACHINE_REPAIR_VIEW;
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
			User user = (User) req.getSession().getAttribute( "user_info" );
			List<HashMap> oList = new OrgDealAction().getChildNodesByOrgId( Long.parseLong( user.getOrg_id() ) );
			req.setAttribute( "MONITOR_ORG_LIST", new MonitorAction().buildOrgOption( oList ) );
			HashMap repairinfo = dao.getRepairInfoById(fid,update);
			HashMap orgId =new HashMap();
			
			if(repairinfo.get("DEPARTMENT_ID")!=null){
				String department_id = (String)repairinfo.get("DEPARTMENT_ID");
				orgId = dao.getOrgIdByDepartmentId(department_id);
				DptDealDAO ddd = new DptDealDAO();
				long orgId1 = Long.parseLong((String)(orgId.get("ORG_ID")));
				HashMap[] dptsMap = ddd.getAllDptsByOrgId( orgId1 );
				HashMap curDpt = null;
				long dptId = -1;
				String dptName = "";
				StringBuilder sb = new StringBuilder();
				if(dptsMap!=null&&dptsMap.length>0){
					sb.append("<select id='SelectDpt' name='selectdpt'>");
					sb.append("<option value='-1'></option>");
					for(int i=0;i<dptsMap.length;i++){
						curDpt = (HashMap)dptsMap[i];
						dptId = Long.parseLong((String)curDpt.get( "DEPARTMENT_ID" ));
						dptName = (String)curDpt.get( "DEPARTMENT_NAME" );
						if(((String)curDpt.get( "DEPARTMENT_ID" )).equals(department_id)){
							sb.append("<option value='" + dptId + "' selected='selected'>" + dptName + "</option>");
						}else{
							sb.append("<option value='" + dptId + "'>" + dptName + "</option>");
						}
						
					}
					sb.append("</select>");
				}else{
					sb.append("<select id='SelectDpt' name='selectdpt'>");
					sb.append("<option value='-1'></option>");
					sb.append("</select>");
				}
				req.setAttribute("sb", sb.toString());
			}
			
			req.setAttribute("orgId", orgId);
			req.setAttribute("repairinfo", repairinfo);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("fid", req.getParameter("fid"));
			req.setAttribute("SelectOrg", req.getParameter("SelectOrg"));
			req.setAttribute("machineName", req.getParameter("machineName"));
			req.setAttribute("operateTime_s", req.getParameter("operateTime_s"));
			req.setAttribute("operateTime_e", req.getParameter("operateTime_e"));
			return Define4Machine.JSP_MACHINE_REPAIR_UPDATE;
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
			User user = (User) req.getSession().getAttribute( "user_info" );
			List<HashMap> oList = new OrgDealAction().getChildNodesByOrgId( Long.parseLong( user.getOrg_id() ) );
			req.setAttribute( "MONITOR_ORG_LIST", new MonitorAction().buildOrgOption( oList ) );
			String ret = dao.updateRepairInfoById(fid,req);
			if("3".equals(ret)){
				dao.insertFaqInfo(fid,req);
			}
			req.setAttribute("pageIdx", pageIdx);			
			HashMap[] maplist = dao.getMacAlertList(req);
			req.setAttribute("maplist", maplist);
			req.setAttribute("pageNum", (maplist.length-1)/10+1);
			req.setAttribute("SelectOrg", req.getParameter("SelectOrg"));
			req.setAttribute("machineName", req.getParameter("machineName"));
			req.setAttribute("operateTime_s", req.getParameter("operateTime_s"));
			req.setAttribute("operateTime_e", req.getParameter("operateTime_e"));
			return Define4Machine.JSP_MACHINE_REPAIR_LIST;
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
			User user = (User) req.getSession().getAttribute( "user_info" );
			List<HashMap> oList = new OrgDealAction().getChildNodesByOrgId( Long.parseLong( user.getOrg_id() ) );
			req.setAttribute( "MONITOR_ORG_LIST", new MonitorAction().buildOrgOption( oList ) );
			HashMap[] maplist = dao.getMacAlertList(req);
			int pageIdx = 1;
			if(StringUtils.isNotBlank(pageIdxs)){
				try {
					pageIdx = Integer.parseInt(pageIdxs);
				} catch (Exception e) {
					pageIdx = 1;
				}
			}
			req.setAttribute("maplist", maplist);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("pageNum", (maplist.length-1)/10+1);
			req.setAttribute("SelectOrg", req.getParameter("SelectOrg"));
			req.setAttribute("machineName", req.getParameter("machineName"));
			req.setAttribute("operateTime_s", req.getParameter("operateTime_s"));
			req.setAttribute("operateTime_e", req.getParameter("operateTime_e"));
			return Define4Machine.JSP_MACHINE_REPAIR_LIST;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
		
	}

}
