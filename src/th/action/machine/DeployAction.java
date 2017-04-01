package th.action.machine;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.com.util.Define4Machine;
import th.dao.ClientDAO;
import th.dao.OrgDealDAO;
import th.dao.machine.DeployDAO;
import th.user.User;
import th.util.StringUtils;


/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class DeployAction extends MachineAction {

	protected DeployDAO dao = new DeployDAO();

	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public DeployAction(HttpServletRequest req, HttpServletResponse res) {
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
			result = maclistQry();
		}else if("view".equalsIgnoreCase(method)){
			result = view();
		}else if("saveData".equalsIgnoreCase(method)){
			result = saveData();
		}else{
			result = Define4Machine.JSP_MACHINE_INVALID;
		}
		return result;
		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String saveData() {
		try {
			hasRight("29");
			String pageIdx = req.getParameter("pageIdx");
			String macIdStd = req.getParameter("macIdStd");
			String orgId = req.getParameter("orgId");
			String orgID = (String) dao.getMacInfo(macIdStd).get("ORG_ID");
			req.setAttribute("pageIdx", pageIdx);
			int result = dao.saveData(req);
			if(!org.apache.commons.lang.StringUtils.equals(orgId, orgID)){
				String orgName = (String) new OrgDealDAO().getCurOrgNodeByOrgId(Long.parseLong(orgId))[0].get("ORG_NAME");
				dao.saveTransfer(macIdStd, orgName, user.getId());
			}
			return Define4Machine.SERVLET_MACHINE_DEPLOY_LIST + pageIdx;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String view() {
		try {
			hasRight("29");
			String macIdStd = req.getParameter("macIdStd");
			String pageIdx = req.getParameter("pageIdx");
			Map[] deployInfo = dao.getDeployInfo(macIdStd);
			Map moreInfo = dao.getMoreInfo(macIdStd);
			moreInfo = this.replaceMacTypeToMap(moreInfo);
			Map[] alertInfo = dao.getAlertInfo(macIdStd);
			Map[] historyInfo = dao.getHistoryInfo(macIdStd);
			Map[] orgMaps = dao.getOrgMaps(getOrgIdsByParentId(user.getOrg_id()));
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("macIdStd", macIdStd);
			req.setAttribute("deployInfo", deployInfo[0]);
			req.setAttribute("moreInfo", moreInfo);
			req.setAttribute("alertInfo", alertInfo);
			req.setAttribute("historyInfo", historyInfo);
			req.setAttribute("orgMaps", orgMaps);
			return Define4Machine.JSP_MACHINE_DEPLOY_VIEW;
		}catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
		
	}

	/**
	 * 查询列表
	 * 
	 * @return
	*/
	public String maclistQry(){
		try {
			hasRight("29");
			String suborgs = new OrgDealDAO().getSubOrg(user.getOrg_id());
			if(StringUtils.isNotBlank(suborgs)){
				suborgs = "," + suborgs;
			}
			HashMap<String, String> typeMap = new ClientDAO().getMacType();
			String macTypeTemp = "";
			String osTemp = "";
			HashMap[] maplist = dao.getMacList(user.getOrg_id()+suborgs);
			for(int i=0;i<maplist.length;i++){
				osTemp = (String)maplist[i].get("OS");
			
				if(-1 != osTemp.indexOf("Win")){
					osTemp = "Win";
				}
				macTypeTemp = osTemp + "_" +(String)maplist[i].get("MACHINE_KIND");
				maplist[i].put("MACTYPE", typeMap.get(macTypeTemp).toString());
			}
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
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("pageNum", (maplist.length-1)/10+1);
			return Define4Machine.JSP_MACHINE_DEPLOY_LIST;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.SERVLET_MACHINE_DEPLOY_LIST;
		}
	}
	private Map replaceMacTypeToMap(Map moreInfo){
		HashMap<String, String> map = new ClientDAO().getMacType();
		String mType = (String)(moreInfo.get("MTYPE"));
		String os = (String)(moreInfo.get("OS"));
		String macKind = (String)(moreInfo.get("MACHINE_KIND"));
		if(-1 != os.indexOf("Win")){
			os = "Win";
		}
		String macTypeTemp = os + "_" + macKind;
		moreInfo.put("MTYPE", map.get(macTypeTemp).toString());
		
		return moreInfo;
	}
}
