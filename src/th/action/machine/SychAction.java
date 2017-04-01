package th.action.machine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.AdvertManagementAction;
import th.action.OrgDealAction;
import th.com.util.Define4Machine;
import th.dao.ChannelDAO;
import th.dao.machine.SychDAO;
import th.util.StringUtils;

/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-9-13
 * @author PSET
 * @since JDK1.6
 */
public class SychAction extends MachineAction {

	protected SychDAO dao = new SychDAO();

	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public SychAction(HttpServletRequest req, HttpServletResponse res) {
		super(req, res);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see th.action.machine.MachineAction#doIt()
	 */
	@Override
	public String doIt() {
		String result;
		if("enter".equals(method) || StringUtils.isBlank(method)){
			result = enter();
		}else if("view".equals(method)){
			result = view();
		}else if("macSync".equals(method)){
			result = macSync();
		}else{
			result = Define4Machine.JSP_MACHINE_INVALID;
		}
		return result;
	}

	/**
	 * 进入信息首页
	 * 
	 * @return
	*/
	public String enter(){
		
		try {
			hasRight("11");
			List<HashMap> orgList = new OrgDealAction().getChildNodesByOrgId(Long.parseLong(user.getOrg_id()));
			req.setAttribute("orgName", getOrgNameByUserID(user.getOrg_id()));
			req.setAttribute("orgid", user.getOrg_id());
			req.setAttribute("orgsList", buildMachineInOrg(orgList));
			return Define4Machine.JSP_MACHINE_SYCH_INFO;
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
			hasRight("12");
			// 用户ID
			String userId = user.getId();
			//组织ID
			String orgId = "";
			if (userId == null) {
				logger.debug("用户ID为空");
			}
			orgId = req.getParameter("orgID");
			if(StringUtils.isBlank(orgId)){
				orgId = user.getOrg_id();
			}
			if(StringUtils.isBlank(orgId)){
				logger.debug("组织ID为空");
			}
			String macIdStd = req.getParameter("macIdStd");
			String pageIdx = req.getParameter("pageIdx");
			if(StringUtils.isBlank(macIdStd)){
				return Define4Machine.JSP_MACHINE_SYCH_BLANK;
			}
			Map[] deployInfo = dao.getDeployInfo(macIdStd);
			HashMap macInfo = dao.getMacInfo(macIdStd);
			String orgid = (String) macInfo.get("ORG_ID");
			String cfgid = getCfgid(orgid);
			HashMap cfgInfo = dao.getCfgInfo(cfgid);
			Map macConfig = dao.getMacConfig(macIdStd);
			Map[] adList = dao.getMacAdList(macIdStd);
			Map[] channelList = dao.getMacChannel(macIdStd);
			Map[] quickEntryList = dao.getMacQuickEntry(macIdStd);
			Map[] whiteList = dao.getMacWhiteList(macIdStd);
			req.setAttribute("macIdStd", macIdStd);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("macIdStd", macIdStd);
			req.setAttribute("deployInfo", deployInfo[0]);
			req.setAttribute("macConfig", macConfig);
			req.setAttribute("adList", adList);
			req.setAttribute("channelList", channelList);
			req.setAttribute("quickEntryList", quickEntryList);
			req.setAttribute("whiteList", whiteList);
			return Define4Machine.JSP_MACHINE_SYCH_VIEW;
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
	private String macSync() {
		
		try {
			String macIdStd = req.getParameter("machineId");
			//节目单下发
			new AdvertManagementAction().programlistSynchronize(req);
			//配置下发
			new ConfigAction(req, res).generalCfgIniFile(macIdStd);
			//频道 快速入口
			ChannelDAO channelDAO = new ChannelDAO();
			channelDAO.updateCommand("0", Long.parseLong(macIdStd));
			channelDAO.updateCommand("1", Long.parseLong(macIdStd));
			//白名单处理
			th.action.availablePage.AuditAction availAuditAction = new th.action.availablePage.AuditAction();
			HashMap[] macMap = {dao.getMacInfo(macIdStd)};
			availAuditAction.sendUpdatePackagesToMachine(macMap, user.getOrg_id(), user.getId());
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
		
	}

	/**
	 * 
	 * 
	 * @return
	 * @throws SQLException 
	*/
	private String getCfgid(String orgid) throws Exception {
		//通过组织id(orgid)查看是否与模版表关联了
		Map cfgMap = dao.getCfgMng(orgid);
		String cfgid;
		if(cfgMap.get("MODULE_ID") == null){
			//未关联的情况
			//1.获取上级组织id
			Map orgMap = dao.getSupOrg(orgid);
			String olevel = (String) orgMap.get("OLEVEL");
			String poid = (String) orgMap.get("POID");
			if(orgMap.get("OLEVEL") != null && !"1".equals(orgMap.get("OLEVEL").toString())){
				cfgid = this.getCfgid(poid);
			}else{
				cfgid = "1";
			}
		}else{
			//关联的情况获取关联的模版id,并返回
			cfgid = cfgMap.get("MODULE_ID").toString();			
		}
		//2.通过上级组织id查看是否与模版表关联了
		return cfgid;		
	}

}
