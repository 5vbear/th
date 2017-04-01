package th.action.machine;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import th.action.OrgDealAction;
import th.com.property.LocalProperties;
import th.com.util.Define;
import th.com.util.Define4Machine;
import th.dao.AdvertDao;
import th.dao.OrgDealDAO;
import th.dao.machine.ConfigDAO;
import th.entity.AdvertBean;
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
public class ConfigAction extends MachineAction {

	protected ConfigDAO dao = new ConfigDAO();

	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public ConfigAction(HttpServletRequest req, HttpServletResponse res) {
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
		}else if("distribute".equalsIgnoreCase(method)){
			result = distribute();
		}
//		else if("cfgListQry".equalsIgnoreCase(method)){
//			result = cfgListQry();
//		}else if("deleteData".equalsIgnoreCase(method)){
//			result = deleteData();
//		}else if("goAdd".equalsIgnoreCase(method)){
//			result = goAdd();
//		}else if("addData".equalsIgnoreCase(method)){
//			result = addData();
//		}
		else if("goEdit".equalsIgnoreCase(method)){
			result = goEdit();
		}else if("editData".equalsIgnoreCase(method)){
			result = editData();
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
	public String addData() {
		try {	
			int result = dao.addData(req);
			return "success";
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
	private String goAdd() {
		try {	
			hasRight("19");
			req.setAttribute("pageIdx", req.getParameter("pageIdx"));
			req.setAttribute("orgid", req.getParameter("orgid"));
			return Define4Machine.JSP_MACHINE_CONFIG_ADD;
		} catch (Exception e) {
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
	private String goEdit() {
		try {
			hasRight("20");
			String orgid = req.getParameter("orgid");
			if(StringUtils.isBlank(orgid)){
				return Define4Machine.JSP_MACHINE_CONFIG_BLANK;
			}
			String cfgid = getCfgid(orgid);
			HashMap cfgInfo = dao.getCfgInfo(cfgid);
			req.setAttribute("orgid", orgid);
			req.setAttribute("cfgid", cfgid);
			req.setAttribute("cfgInfo", cfgInfo);
//			HashMap[] orgList = dao.getOrgList(cfgid);
//			req.setAttribute("pageIdx", req.getParameter("pageIdx"));
//			req.setAttribute("orgList", orgList);
			return Define4Machine.JSP_MACHINE_CONFIG_EDIT;	
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
	private String editData() {
		try {
			hasRight("20");
			String orgid = req.getParameter("orgid");
			Map cfgMap = dao.getCfgMng(orgid);
			if(cfgMap.get("MODULE_ID") == null){
				String cfgid = dao.generalCfgid();
				dao.addData(req, cfgid);
				dao.insertCfg(cfgid, orgid, user.getId());
			}else{
				String cfgid = cfgMap.get("MODULE_ID").toString();
				dao.editData(req, cfgid);		
			}
			String suborgs = new OrgDealDAO().getSubOrg(orgid);
			if(StringUtils.isNotBlank(suborgs)){
				suborgs = "," + suborgs;
			}
			HashMap[] macs = dao.getAllMacsInfoByOrgid(orgid+suborgs);
			for(int i = 0; i < macs.length; i++){			
				generalCfgIniFile(macs[i].get("MID").toString());
			}
			HashMap[] macs1 = dao.getMacsInfoByOrgid(orgid);
			for(int i = 0; i < macs1.length; i++){			
				generalCfgIniFile(macs1[i].get("MID").toString());
			}
			return Define4Machine.SERVLET_MACHINE_CONFIG_EDIT + orgid;
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
	private String deleteData() {
		String cfgIds = req.getParameter("cfgIds");
		String pageIdx = req.getParameter("pageIdx");
		String orgid = req.getParameter("orgid");
		try {	
			hasRight("21");
			dao.deleteCfgMG(cfgIds);
			int result = dao.deleteCfgs(cfgIds);
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("orgid", orgid);
			System.out.println("成功删除"+result+"条数据!");
			return Define4Machine.SERVLET_MACHINE_CONFIG_LIST + pageIdx + "&orgid="+orgid;
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
	private String enter() {
		try {
			hasRight("18");
			List<HashMap> orgList = new OrgDealAction().getChildNodesByOrgId(Long.parseLong(user.getOrg_id()));
			req.setAttribute("orgName", getOrgNameByUserID(user.getOrg_id()));
			req.setAttribute("orgid", user.getOrg_id());
			req.setAttribute("orgsList", buildOrgOption(orgList));
			return Define4Machine.JSP_MACHINE_CONFIG;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
	}

	/**
	 * 查询列表
	 * 
	 * @return
	*/
	public String cfgListQry(){
		try {
			hasRight("23");	
			HashMap[] cfgList = dao.getCfgList();
			req.setAttribute("cfgList", cfgList);
			String pageIdxs = req.getParameter("pageIdx");
			int pageIdx = 1;
			if(StringUtils.isNotBlank(pageIdxs)){
				try {
					pageIdx = Integer.parseInt(pageIdxs);
				} catch (Exception e) {
					pageIdx = 1;
				}
			}
			req.setAttribute("pageIdx", pageIdx);
			req.setAttribute("pageNum", (cfgList.length-1)/10+1);
			req.setAttribute("orgid", req.getParameter("orgid"));
			return Define4Machine.JSP_MACHINE_CONFIG_LIST;
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
	private String distribute() {
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");

		// 用户ID
		String userId = user.getId();
		String pageIdx = req.getParameter("pageIdx");
		String orgid = req.getParameter("orgid");
		String cfgIds = req.getParameter("cfgIds");
		req.setAttribute("pageIdx", pageIdx);
		req.setAttribute("orgid", orgid);
		try {
			hasRight("23");	
			int cnt = dao.isDistributed(orgid);
			if(cnt > 0){
				int result = dao.updateCfg(cfgIds, orgid);
			}else{
				int result = dao.insertCfg(cfgIds, orgid, userId);
			}
			HashMap[] macs = dao.getMacsInfoByOrgid(orgid);
			for(int i = 0; i < macs.length; i++){			
				generalCfgIniFile(macs[i].get("MID").toString());
			}			
			return Define4Machine.SERVLET_MACHINE_CONFIG_LIST+pageIdx + "&orgid="+orgid;
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
	public String distribute1() {
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");

		// 用户ID

		String cfgIds = req.getParameter("checkedConfigs");
		String macs = req.getParameter("checkedIds");
		
		String note = req.getParameter("note");
		try {
			
			String[] macId =macs.split(",");
			for(int i =0 ;i<macId.length;i++){
				if(!macId[i].split("_")[0].equals("mac"))continue;
				System.out.print(macId[i].split("_")[1]);
				generalCfgIniFile(macId[i].split("_")[1],cfgIds);
			}
			
				
			return "OK";
		} catch (Exception e) { 
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
	}
	
	public void insertModuleManagement() {
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user_info");
		String orgId = "";
		String cfgId = "";
		// 用户ID
		String cfgIds = req.getParameter("checkedConfigs");
		String macs = req.getParameter("checkedIds");
		try {
			String[] macId =macs.split(",");
			for(int i =0 ;i<macId.length;i++){
				if(macId[i].split("_")[0].equals("org")){
					orgId = macId[i].split("_")[1];
					String[] mouId =cfgIds.split(",");
					for(int j =0 ;j<mouId.length;j++){
						cfgId = mouId[j].split("@@")[0];
						int cnt = dao.isDistributed(orgId);
						if(cnt > 0){
							dao.updateCfg(cfgId, orgId);
						}else{
							dao.insertCfg(cfgId, orgId, user.getId());
						}
					}
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public void generalCfgIniFile(String macid) throws Exception{
		String updateZipFilePath = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_PATH_CONFIGURATION");
		HashMap macInfo = dao.getMacInfo(macid);
		String orgid = (String) macInfo.get("ORG_ID");
		String cfgid = getCfgid(orgid);
		HashMap cfgInfo = dao.getCfgInfo(cfgid);
		uploadIniFileToFTP(updateZipFilePath, cfgInfo, macInfo.get("MAC").toString());
		dao.updateCommand(macid, updateZipFilePath + "/" + macInfo.get("MAC").toString()+".ini");
	}


	public void generalCfgIniFile(String macid,String cfgIds) throws Exception{
		String updateZipFilePath = LocalProperties.getProperty("FTP_DOWNLOAD_FILE_PATH_CONFIGURATION");
		HashMap macInfo = dao.getMacInfo(macid);
		HashMap cfgInfo= new HashMap();
		ArrayList list =new ArrayList();
		ArrayList itemList =new ArrayList();
		itemList.add("0");
		itemList.add("1");
		itemList.add("2");
		itemList.add("3");
		itemList.add("4");
		itemList.add("5");
		itemList.add("6");
		itemList.add("7");
		itemList.add("8");
		for(int i =0 ;i<cfgIds.split(",").length;i++){
			String value =cfgIds.split(",")[i];
			if(value.split("@@").length>0){
				String configId =value.split("@@")[0];
				HashMap map = dao.getCfgInfo(configId);
				list.add(map.get("ITEMNAME"));
				if("0".equals(map.get("ITEMNAME"))){
					cfgInfo.put("STIME", map.get("STIME")==null?"":map.get("STIME"));
				}else if("1".equals(map.get("ITEMNAME"))){
					cfgInfo.put("CTIME", map.get("CTIME")==null?"":map.get("CTIME"));
				}else if("2".equals(map.get("ITEMNAME"))){
					cfgInfo.put("SPTIME", map.get("SPTIME")==null?"":map.get("SPTIME"));
				}else if("3".equals(map.get("ITEMNAME"))){
					cfgInfo.put("SCRTIME", map.get("SCRTIME")==null?"":map.get("SCRTIME"));
				}else if("4".equals(map.get("ITEMNAME"))){
					cfgInfo.put("IVLTIME", map.get("IVLTIME")==null?"":map.get("IVLTIME"));
				}else if("5".equals(map.get("ITEMNAME"))){
					cfgInfo.put("PROPATH", map.get("PROPATH")==null?"":map.get("PROPATH"));
				}else if("6".equals(map.get("ITEMNAME"))){
					cfgInfo.put("SURL", map.get("SURL")==null?"":map.get("SURL"));
				}else if("7".equals(map.get("ITEMNAME"))){
					cfgInfo.put("FTPIP", map.get("FTPIP")==null?"":map.get("FTPIP"));
				}else if("8".equals(map.get("ITEMNAME"))){
					cfgInfo.put("COMMAND_TIME", map.get("COMMANDTIME")==null?"":map.get("COMMANDTIME"));
				}
			}
			
		}
		
		if(list!=null){
			for(int i =0 ;i<list.size();i++){
				if(itemList.contains(list.get(i))){
					itemList.remove(list.get(i));
				}
			}
			
			
				if(itemList.contains("0")){
					cfgInfo.put("STIME", LocalProperties.getProperty("MACHINE_CONFIG_HOTIME")+":"+LocalProperties.getProperty("MACHINE_CONFIG_MOTIME")+":00");
				}
				if(itemList.contains("1")){
					cfgInfo.put("CTIME",LocalProperties.getProperty("MACHINE_CONFIG_HCTIME")+":"+LocalProperties.getProperty("MACHINE_CONFIG_MCTIME")+":00");			
				}
				if(itemList.contains("2")){
					cfgInfo.put("SPTIME", LocalProperties.getProperty("MACHINE_CONFIG_PROTIME"));
				}
				if(itemList.contains("3")){
					cfgInfo.put("SCRTIME",  LocalProperties.getProperty("MACHINE_CONFIG_SCREENCOPYDURATION"));
				}
				if(itemList.contains("4")){
					cfgInfo.put("IVLTIME", LocalProperties.getProperty("MACHINE_CONFIG_SCREENCOPYINTERVAL"));
				}
				if(itemList.contains("5")){
					cfgInfo.put("PROPATH", "");
				}
				if(itemList.contains("6")){
					cfgInfo.put("SURL", LocalProperties.getProperty("HOST_SERVER"));
				}
				if(itemList.contains("7")){
					cfgInfo.put("FTPIP",LocalProperties.getProperty("FTP_SERVER_HOST"));
				}
				if(itemList.contains("8")){
					cfgInfo.put("COMMAND_TIME",LocalProperties.getProperty("MACHINE_CONFIG_COMMAND_TIME"));
				}
			
			
		}
//		String orgid = (String) macInfo.get("ORG_ID");
//		String cfgid = getCfgid(orgid);
		
		
		uploadIniFileToFTP(updateZipFilePath, cfgInfo, macInfo.get("MAC").toString());
		dao.updateCommand(macid, updateZipFilePath + "/" + macInfo.get("MAC").toString()+".ini");
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

	/**
	 * 素材检索
	 * @param req
	 * @throws Exception
	 */
	public HashMap[] configSearch(HttpServletRequest req) throws Exception {
		
		String machineConfigName = req.getParameter("machineConfigName");
		String userName = req.getParameter("userName");
		String search_date_s = req.getParameter("search_date_s");
		String search_date_e = req.getParameter("search_date_e");
		if(machineConfigName!=null){
			
			machineConfigName = machineConfigName.trim();
		}
		if(userName!=null){
			
			userName = userName.trim();
		}
		if(search_date_s!=null){
			
			search_date_s = search_date_s.trim();
		}
		if(search_date_e!=null){
			
			search_date_e = search_date_e.trim();
		}
		
		
		ConfigDAO dao = new ConfigDAO();
		HashMap[] machineConfigBeans = dao.searchmachineConfig(machineConfigName,userName,search_date_s,search_date_e);
		req.setAttribute("machineConfigName", machineConfigName);
		req.setAttribute("userName", userName);
		req.setAttribute("search_date_s", search_date_s);
		req.setAttribute("search_date_e", search_date_e);
		return machineConfigBeans;
	}
	
	
	
	/**
	 * 
	 * 
	 * @return
	*/
	public void deleteConfig() {
		String cfgIds = req.getParameter("checkValue");
		try {	
			int result = dao.deleteCfgs(cfgIds);
		
			System.out.println("成功删除"+result+"条数据!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
