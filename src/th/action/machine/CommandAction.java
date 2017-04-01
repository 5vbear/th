package th.action.machine;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.OrgDealAction;
import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define;
import th.com.util.Define4Machine;
import th.dao.OrgDealDAO;
import th.dao.machine.CommandDAO;
import th.util.StringUtils;


/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class CommandAction extends MachineAction {

	protected CommandDAO dao = new CommandDAO();

	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public CommandAction(HttpServletRequest req, HttpServletResponse res) {
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
		}else if("macOperView".equals(method)){
			result = macOperView();
		}else if("macOper".equals(method)){
			result = macOper();
		}else if("orgOperView".equals(method)){
			result = orgOperView();
		}else if("orgOper".equals(method)){
			result = orgOper();
		}else if("orgOperTypeView".equals(method)){
			result = orgOperTypeView();
		}
//		else if("oper".equals(method)){
//			result = oper();
//		}else if("dOper".equals(method)){
//			result = dOper();
//		}
		else{
			result = Define4Machine.JSP_MACHINE_INVALID;
		}
		return result;
		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String macOperView() {
		try{
			String macIDStd = req.getParameter("macIDStd");
			HashMap macMap = dao.getMacInfo(macIDStd);
			String orgIDStd = (String) macMap.get("ORG_ID");
			String orgNameStd = "";
			HashMap[] orgMaps = new OrgDealDAO().getCurOrgNodeByOrgId(Long.parseLong(orgIDStd));
			if(orgMaps != null && orgMaps.length > 0){
				orgNameStd = (String) orgMaps[0].get("ORG_NAME");
			}
			String operName = getOperName(macIDStd);
			String updateStatus = getUpdateStatus(macIDStd);
			req.setAttribute("macIDStd", macIDStd);
			req.setAttribute("macNameStd", macMap.get("MACHINE_MARK"));
			req.setAttribute("orgNameStd", orgNameStd);
			req.setAttribute("operName", operName);
			req.setAttribute("updateStatus", updateStatus);
			req.setAttribute("stopStatus", dao.getMacStatus(macIDStd, "2"));
			req.setAttribute("startStatus", dao.getMacStatus(macIDStd, "3"));
			req.setAttribute("srnStartStatus", dao.getMacStatus(macIDStd, "6"));
			req.setAttribute("srnEndStatus", dao.getMacStatus(macIDStd, "7"));
			req.setAttribute("startAdPlayStatus", dao.getMacStatus(macIDStd, "10"));
			req.setAttribute("endAdPlayStatus", dao.getMacStatus(macIDStd, "11"));
			req.setAttribute("startTemAdPlayStatus", dao.getMacStatus(macIDStd, "12"));
			req.setAttribute("endTemAdPlayStatus", dao.getMacStatus(macIDStd, "13"));
			req.setAttribute("lockStatus", dao.getMacStatus(macIDStd, "28"));
			req.setAttribute("unlockStatus", dao.getMacStatus(macIDStd, "29"));
			return Define4Machine.JSP_MACHINE_COMMAND_MACVIEW;
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
	private String orgOperView() {
		try{
			String orgIDStd = req.getParameter("orgIDStd");
			String orgNameStd = "";
			HashMap[] orgMaps = new OrgDealDAO().getCurOrgNodeByOrgId(Long.parseLong(orgIDStd));
			if(orgMaps != null && orgMaps.length > 0){
				orgNameStd = (String) orgMaps[0].get("ORG_NAME");
			}
			req.setAttribute("orgNameStd", orgNameStd);
			req.setAttribute("orgIDStd", orgIDStd);
			return Define4Machine.JSP_MACHINE_COMMAND_ORGVIEW;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}			
	}
	
	private String orgOperTypeView() {
		try{
			String orgIDStd = req.getParameter("orgTypeIDStd");
			String pName = req.getParameter("pName");
			String tName = req.getParameter("tName");
			/*String orgNameStd = "";
			HashMap[] orgMaps = new OrgDealDAO().getCurOrgNodeByOrgId(Long.parseLong(orgIDStd));
			if(orgMaps != null && orgMaps.length > 0){
				orgNameStd = (String) orgMaps[0].get("ORG_NAME");
			}*/
			req.setAttribute("orgNameStd", pName + "-" + tName);
			req.setAttribute("orgIDStd", orgIDStd);
			return Define4Machine.JSP_MACHINE_COMMAND_TYPEVIEW;
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
	private String macOper() {
		try {
			hasRight("24");
			String macIDStd = req.getParameter("macIDStd");
			String operType = req.getParameter("operType");
			String content = req.getParameter("content");
			if(StringUtils.isBlank(macIDStd)){
				throw new Exception("执行端机指令时未选中相应的端机!");
			}
			dao.doOper(macIDStd, operType, content);			
			if("2".equals(operType)){
				dao.insertMachinePauseHistory(macIDStd, user.getId());
				dao.undoOper(macIDStd, "3");
			}else if("3".equals(operType)){
				dao.updateMachinePauseHistory(macIDStd);
				dao.undoOper(macIDStd, "2");
			}else if("6".equals(operType)){
				dao.undoOper(macIDStd, "7");
			}else if("7".equals(operType)){
				dao.undoOper(macIDStd, "6");
			}else if("10".equals(operType)){
				dao.undoOper(macIDStd, "11");
			}else if("11".equals(operType)){
				dao.undoOper(macIDStd, "10");
			}else if("12".equals(operType)){
				dao.undoOper(macIDStd, "13");
			}else if("13".equals(operType)){
				dao.undoOper(macIDStd, "12");
			}else if("28".equals(operType)){
				dao.undoOper(macIDStd, "29");
			}else if("29".equals(operType)){
				dao.undoOper(macIDStd, "28");
			}else if("30".equals(operType)){
				dao.insertMessageHistory(macIDStd, content, user.getId());
			}else if("31".equals(operType)){
				// 报废
				String mac = "";
				HashMap[] eMap = dao.getMachineMac(macIDStd);
				if(null != eMap && eMap.length == 1){
					// mac地址取得
					mac =StringUtils.transStr((String)eMap[0].get("MAC")) ;
				}
				if(!"".equals(mac)){
					HashMap[] map = dao.getRetirementMachine(mac);
					if(map == null || map.length == 0){
						// 在报废表中插入机器mac地址
						dao.insertMachineRetirement(mac, macIDStd);
						// 更新机器状态
						dao.updateMachineStatus(macIDStd, Define.MACHINE_STATUS_RETIREMENT);
					}

				} 
			}else if("32".equals(operType)){
				// 升级
				String mac = "";
				HashMap[] eMap = dao.getMachineMac(macIDStd);
				if(null != eMap && eMap.length == 1){
					// mac地址取得
					mac =StringUtils.transStr((String)eMap[0].get("MAC")) ;
				}
				if(!"".equals(mac)){
					// 查询机器是否已在升级
					HashMap[] map = dao.getUpdatingMachine(mac);
					if(null == map || map.length == 0){
						// 在升级表中插入机器mac地址
						dao.insertMachineUpdating(mac, macIDStd);
					}

				} 
			}else if("33".equals(operType)){
				// 停止升级
				String mac = "";
				HashMap[] eMap = dao.getMachineMac(macIDStd);
				if(null != eMap && eMap.length == 1){
					// mac地址取得
					mac =StringUtils.transStr((String)eMap[0].get("MAC")) ;
				}
				if(!"".equals(mac)){
					// 在升级表中删除机器mac地址
					dao.deleteMachineUpdating(mac);
				} 
			}
			return Define4Machine.SERVLET_MACHINE_COMMAND_MAC + macIDStd;	
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
	private String orgOper() {
		try {
			hasRight("24");
			String orgIDStd = req.getParameter("orgIDStd");
			String operType = req.getParameter("operType");
			String content = req.getParameter("content");
			if(StringUtils.isBlank(orgIDStd)){
				throw new Exception("执行端机指令时未选中相应的分行!");
			}
			String suborgs = new OrgDealDAO().getSubOrg(orgIDStd);
			if(StringUtils.isNotBlank(suborgs)){
				suborgs = "," + suborgs;
			}
			HashMap[] macs = dao.getSubMacsByParentOrgid(orgIDStd + suborgs);
			for(int i = 0; i < macs.length; i++){
				dao.doOper(macs[i].get("MID").toString(), operType, content);			
				if("30".equals(operType)){
					dao.insertMessageHistory(macs[i].get("MID").toString(), content, user.getId());
				}
			}
			return Define4Machine.SERVLET_MACHINE_COMMAND_ORG + orgIDStd;	
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
	private String sendMsg() {
		try {
			hasRight("24");
			String macIDStd = req.getParameter("macIDStd");
			return Define4Machine.SERVLET_MACHINE_COMMAND_MAC + macIDStd;	
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
	private String oper() {
		try {
			hasRight("24");
			String macIDStd = req.getParameter("macIDStd");
			if(StringUtils.isNotBlank(macIDStd)){
				HashMap macMap = dao.getMacInfo(macIDStd);
				String orgIDStd = (String) macMap.get("ORG_ID");
				String orgNameStd = "";
				HashMap[] orgMaps = new OrgDealDAO().getCurOrgNodeByOrgId(Long.parseLong(orgIDStd));
				if(orgMaps != null && orgMaps.length > 0){
					orgNameStd = (String) orgMaps[0].get("ORG_NAME");
				}
				String operName = getOperName(macIDStd);
				String updateStatus = getUpdateStatus(macIDStd);
				req.setAttribute("macIDStd", macIDStd);
				req.setAttribute("macNameStd", macMap.get("MACHINE_MARK"));
				req.setAttribute("orgNameStd", orgNameStd);
				req.setAttribute("operName", operName);
				req.setAttribute("updateStatus", updateStatus);
			}
			return Define4Machine.JSP_MACHINE_COMMAND_OPER;	
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}			
	}
	
	private String getOperName(String macIDStd) throws Exception{
		HashMap[] map = dao.getMacStatus(macIDStd);
		String operName = "等待操作中...";
		if(map != null && map.length > 0){
			int cid = Integer.parseInt(map[0].get("CID").toString());
			String cname = map[0].get("CNAME").toString();
			operName = parseOper(macIDStd, cid, cname);
		}
		return operName;
		
	}
	
	private String getUpdateStatus(String macIDStd) throws Exception{
		String mac = "";
		HashMap[] map = dao.getMachineMac(macIDStd);
		String updateStatus = "停止升级中...";
		if(null != map && map.length == 1){
			// mac地址取得
			mac =StringUtils.transStr((String)map[0].get("MAC")) ;
		}
		if(!"".equals(mac)){
			HashMap[] eMap = dao.getUpdatingMachine(mac);
			if(null != eMap && eMap.length == 1){
				updateStatus = "正在升级中...";
			}
		}

		return updateStatus;
		
	}

	/**
	 * 
	 * 
	 * @param macIDStd 
	 * @param operType
	 * @param cname 
	 * @return
	 * @throws Exception 
	*/
	private String parseOper(String macIDStd, int operType, String cname) throws Exception {
		String resText;
		switch(operType){
			case 7:
				String ip = LocalProperties.getProperty("FTP_SERVER_HOST");
				String port = LocalProperties.getProperty("FTP_SERVER_PORT");
				String path = LocalProperties.getProperty("FTP_UPLOAD_FILE_PATH_SCREENSHOT");
				String mac = (String) dao.getMacInfo(macIDStd).get("MAC");
				String ftp = "ftp://" + ip + ":" + port + path +"/" + mac;
				resText = "正在停止截屏中...,稍后请到<a href='" + ftp + "' target='_blank' class='tableLink'>ftp服务器中</a>获取相应文件";
				break;
			default:
				resText = "正在" + cname + "中...";
		}		
		return resText;		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String dOper() {
		try {
			hasRight("24");
			String macIDStd = req.getParameter("macIDStd");
			String operType = req.getParameter("operType");
			String resText = "";
			if(StringUtils.isBlank(macIDStd)){
				throw new Exception("执行端机指令时未选中相应的端机!");
			}
			dao.doOper(macIDStd, operType, null);			
			
			if("6".equals(operType)){
				resText = "端机准备启动截屏!&1&正在启动截屏中...";
			}else if("7".equals(operType)){
				String ip = LocalProperties.getProperty("FTP_SERVER_HOST");
				String port = LocalProperties.getProperty("FTP_SERVER_PORT");
				String path = LocalProperties.getProperty("FTP_UPLOAD_FILE_PATH_SCREENSHOT");
				String mac = (String) dao.getMacInfo(macIDStd).get("MAC");
				String ftp = "ftp://" + ip + ":" + port + path +"/" + mac;
				resText = "端机准备停止截屏!&1&正在停止截屏中...,稍后请到<a href='" + ftp + "' target='_blank' class='tableLink'>ftp服务器中</a>获取相应文件";		
			}else if("4".equals(operType)){
				resText = "端机准备关机!&1&正在关机中...";			
			}else if("5".equals(operType)){
				resText = "端机准备重启!&1&正在重启中...";			
			}else if("10".equals(operType)){
				resText = "端机准备开始播放广告!&1&正在开始广告播放中...";	
			}else if("11".equals(operType)){
				resText = "端机准备停止播放广告!&1&正在停止广告播放中...";	
			}else if("12".equals(operType)){
				resText = "端机准备启动临时广告!&1&正在启动临时广告中...";	
			}else if("13".equals(operType)){
				resText = "端机准备停止临时广告!&1&正在停止临时广告中...";	
			}else if("8".equals(operType)){
				resText = "端机准备清除端机冗余数据!&1&正在清除端机冗余数据中...";	
			}else if("2".equals(operType)){
				resText = "端机准备报停!&1&正在报停中...";
				dao.insertMachinePauseHistory(macIDStd, user.getId());
			}else if("3".equals(operType)){
				resText = "端机准备启动!&1&正在启动中...";
				dao.updateMachinePauseHistory(macIDStd);
			}else if("27".equals(operType)){
				resText = "端机准备定位!&1&正在定位中...";	
			}else if("28".equals(operType)){
				resText = "端机准备锁屏!&1&正在锁屏中...";	
			}else if("29".equals(operType)){
				resText = "端机准备解锁!&1&正在解锁中...";	
			}else if("31".equalsIgnoreCase(operType)){
				resText = "端机准备报废!&1&正在报废中...";
			}else if("32".equalsIgnoreCase(operType)){
				resText = "端机准备升级!&1&正在升级中...";
			}else if("33".equalsIgnoreCase(operType)){
				resText = "端机准备停止升级!&1&正在停止升级中...";
			}else{
				resText = "无效操作!&0&";
			}
			res.setContentType("text/xml;charset=UTF-8");
			res.setHeader("Cache-Control", "no-cache");
			res.getWriter().write(resText);
			return null;	
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}			
	}
	
	/**
	 * 
	 * 
	 * @param macIDStd
	 * @return
	*/
	private String close(String macIDStd) throws SQLException {
		String result = "";
		String startStatus = dao.getMacStatus(macIDStd, "6");
		String endStatus = dao.getMacStatus(macIDStd, "7");
		if(!"0".equals(startStatus)&&!"1".equals(endStatus)){
			result = "端机正在启动截屏中,请先停止截屏,再进行此操作!&0&";
		}else{
			result = "端机准备关机!&1&关机中...";
			dao.doOper(macIDStd, "4", null);
		}
		return result;
	}
	
	/**
	 * 
	 * 
	 * @param macIDStd
	 * @return
	*/
	private String restart(String macIDStd) throws SQLException {
		String result = "";
		String startStatus = dao.getMacStatus(macIDStd, "6");
		String endStatus = dao.getMacStatus(macIDStd, "7");
		if(!"0".equals(startStatus)&&!"1".equals(endStatus)){
			result = "端机正在启动截屏中,请先停止截屏,再进行此操作!&0&";
		}else{
			result = "端机准备重启!&1&重启中...";
			dao.doOper(macIDStd, "5", null);
		}
		return result;
	}

	/**
	 * 进入指令首页
	 * 
	 * @return
	*/
	public String enter(){
		
		//根据当前用户取得所在组织结构
		try {
			hasRight("24");
			List<HashMap> orgList = new OrgDealAction().getChildNodesByOrgId(Long.parseLong(user.getOrg_id()));
			req.setAttribute("orgName", getOrgNameByUserID(user.getOrg_id()));
			req.setAttribute("orgid", user.getOrg_id());
			//根据当前用户取得所在组织结构
			req.setAttribute("orgMacsList", buildMachineInOrgCommnd(orgList));
			return Define4Machine.JSP_MACHINE_COMMAND;
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
	private String oper2() {
		try {
			hasRight("24");
			String macIDStd = req.getParameter("macIDStd");
			if(StringUtils.isNotBlank(macIDStd)){
				HashMap macMap = dao.getMacInfo(macIDStd);
				String orgIDStd = (String) macMap.get("ORG_ID");
				String orgNameStd = "";
				String operName = "等待操作中";
				HashMap[] orgMaps = new OrgDealDAO().getCurOrgNodeByOrgId(Long.parseLong(orgIDStd));
				if(orgMaps != null && orgMaps.length > 0){
					orgNameStd = (String) orgMaps[0].get("ORG_NAME");
				}
				if(dao.getMacStatus(macIDStd, 4)){
					operName = "正在关机中...";
					req.setAttribute("operStatus", "4");
				}else if(dao.getMacStatus(macIDStd, 5)){
					operName = "正在重启中...";
					req.setAttribute("operStatus", "5");
				}else if(dao.getMacStatus(macIDStd, 6) && !dao.getMacStatus(macIDStd, 7)){
					operName = "正在截屏中...";
					req.setAttribute("operStatus", "6");
				}else if(dao.getMacStatus(macIDStd, 7)){
					operName = "停止截屏中...";
					req.setAttribute("operStatus", "7");
				}else if(dao.getMacStatus(macIDStd, 8)){
					operName = "清除端机冗余数据中...";
					req.setAttribute("operStatus", "8");
				}else if(dao.getMacStatus(macIDStd, 10)){
					operName = "播放广告中...";
					req.setAttribute("operStatus", "10");
				}else if(dao.getMacStatus(macIDStd, 11)){
					operName = "停止播放广告中...";
					req.setAttribute("operStatus", "11");
				}
				req.setAttribute("macIDStd", macIDStd);
				req.setAttribute("macNameStd", macMap.get("MACHINE_MARK"));
				req.setAttribute("orgNameStd", orgNameStd);
				req.setAttribute("operName", operName);
			}
			return Define4Machine.JSP_MACHINE_COMMAND_OPER;	
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
	private String dOper2() {
		try {
			hasRight("24");
			String macIDStd = req.getParameter("macIDStd");
			String operType = req.getParameter("operType");
			if(StringUtils.isNotBlank(macIDStd)){
				dao.doOper(macIDStd, operType, null);
				req.setAttribute("macIDStd", macIDStd);
			}
			return Define4Machine.SERVLET_MACHINE_COMMAND_MAC + macIDStd;	
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}			
	}

	/**
	 * 
	 * 
	 * @param macIDStd
	 * @return
	 * @throws SQLException 
	*/
	private String startSrn(String macIDStd) throws SQLException {
		String result = "";
		String startStatus = dao.getMacStatus(macIDStd, "6");
		if(!"0".equals(startStatus)){
			result = "端机已启动截屏,请稍等!&0&";
		}else{
			result = "端机启动截屏!&1&正在启动截屏中...";
			dao.doOper(macIDStd, "6", null);
		}
		return result;		
	}
	
	/**
	 * 
	 * 
	 * @param macIDStd
	 * @return
	 * @throws SQLException 
	*/
	private String endSrn(String macIDStd) throws SQLException {
		String result = "";
		String startStatus = dao.getMacStatus(macIDStd, "6");
		String endStatus = dao.getMacStatus(macIDStd, "7");
		if("0".equals(startStatus)){
			result = "请先启动截屏,然后停止截屏!&0&";
		}else if(!"0".equals(endStatus)){
			result = "端机已停止截屏!&0&";
		}else{
			dao.doOper(macIDStd, "7", null);
			result = "端机停止截屏,稍后请到FTP服务器获取相应文件!&1&正在停止截屏中...";
		}
		return result;		
	}

}
