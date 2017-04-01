package th.action.machine;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.action.AdvertManagementAction;
import th.action.OrgDealAction;
import th.com.util.Define4Machine;
import th.dao.ChannelDAO;
import th.dao.InteractiveDao;
import th.dao.OrgDealDAO;
import th.dao.machine.AuditDAO;
import th.util.StringUtils;


/**
 * 功能说明:TODO
 * 
 * @see reference(可选,与当前类相关的包)
 * @version 13cyber, 2013-8-15
 * @author PSET
 * @since JDK1.6
 */
public class AuditAction extends MachineAction {

	protected AuditDAO dao = new AuditDAO();
	/**
	 * 
	 * 
	 * @param req
	 * @param res
	*/
	public AuditAction(HttpServletRequest req, HttpServletResponse res) {
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
		}else if("maclistQry".equals(method)){
			result = maclistQry();
		}else if("macsDelete".equals(method)){
			result = delete();
		}else if("macsAudit".equals(method)){
			result = audit();
		}else if("goBatch".equals(method)){
			result = goBatch();
		}else if("batchAudit".equals(method)){
			result = batchAudit();
		}else{
			result = Define4Machine.JSP_MACHINE_INVALID;;
		}
		return result;
		
	}

	/**
	 * 进入审核首页
	 * 
	 * @return
	*/
	public String enter(){		
		//根据当前用户取得所在组织结构
		try {
			hasRight("13");
			List<HashMap> orgList = new OrgDealAction().getChildNodesByOrgId(Long.parseLong(user.getOrg_id()));
			req.setAttribute("orgName", getOrgNameByUserID(user.getOrg_id()));
			req.setAttribute("orgid", user.getOrg_id());
			req.setAttribute("orgsList", buildMachineInOrg(orgList));
			return Define4Machine.JSP_MACHINE_AUDIT;
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
	public String maclistQry(){
		try {
			hasRight("13");
			HashMap[] maplist = dao.getMacAuditList();
			req.setAttribute("maclist", maplist);
			String pageIdxs = req.getParameter("pageIdx");
			req.setAttribute("orgid", req.getParameter("orgid"));
			req.setAttribute("isChildOrg", req.getParameter("isChildOrg"));
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
			return Define4Machine.JSP_MACHINE_AUDIT_LIST;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.SERVLET_MACHINE_AUDIT_QUERY;
		}
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String batchAudit() {

		try {
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
			req.setCharacterEncoding("utf-8");
			ServletInputStream uploadFiles = req.getInputStream(); 
			byte[] bt = new byte[4096];
			int t = uploadFiles.readLine (bt, 0, bt.length);
			int linenum = 1;
			int linecnt = 0;
			while(t != -1){
				String str = new String (bt, 0, t);
				if(str.startsWith("###")){
					str = str.replace("###", "");
					str = str.replace("\r\n", "");
					if(auditMac(str, linenum)){
						linecnt++;
					}
					System.out.println(str);
				}
				linenum++;
				t = uploadFiles.readLine (bt, 0, bt.length);
			}
			req.setAttribute("auditmsg", "共有"+linecnt+"条端机数据通过审核!");
			return Define4Machine.SERVLET_MACHINE_AUDIT_QUERY;
		} catch (IOException e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_INVALID;
		}
		
	}

	/**
	 * 
	 * 
	 * @param str
	*/
	private boolean auditMac(String str, int index) {
		String[] info = str.split(",");
		if(info.length < 2){
			logger.debug("第"+index+"行数据格式不正确,应该使用逗号','隔开!");
			System.out.println("第"+index+"行数据格式不正确,应该使用逗号','隔开!");
			return false;
		}
		String orgid = info[0];
		String mac = info[1];
		if(!isValidOrg(orgid)){
			logger.debug("第"+index+"行数据公司代码错误!");
			System.out.println("第"+index+"行数据公司代码错误!");
			return false;
		}
		if(!isValidMac(mac)){
			logger.debug("第"+index+"行数据mac地址错误!");
			System.out.println("第"+index+"行数据mac地址错误!");
			return false;
		}
		try {
			Map[] macinfo = dao.getMacInfoByMac(mac);
			auditMacs(macinfo[0].get("MID").toString(), orgid);
			return true;
		} catch (Exception e) {
			return false;			
		}
	}

	/**
	 * 
	 * 
	 * @param mac
	 * @return
	*/
	private boolean isValidMac(String mac) {
		try {
			Map[] macinfo = dao.getMacInfoByMac(mac);
			if(macinfo == null || macinfo.length<1){
				return false;
			}
			return true;
		} catch (SQLException e) {
			return false;
		}
		
	}

	/**
	 * 
	 * 
	 * @param orgid
	 * @return
	*/
	private boolean isValidOrg(String orgid) {
		try {
			long orgId = Long.parseLong(orgid);
			Map[] orginfo = new OrgDealDAO().getCurOrgNodeByOrgId(orgId);
			if(orginfo == null || orginfo.length<1){
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}

	/**
	 * 
	 * 
	 * @return
	*/
	private String goBatch() {
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
		return Define4Machine.JSP_MACHINE_BATCH_AUDIT;
		
	}
	
	/**
	 * 审核端机
	 * 
	 * @return
	*/
	public String audit(){
		String macIds = req.getParameter("macIds");
		String orgid = req.getParameter("orgid");
		try {
			hasRight("14");
			auditMacs(macIds, orgid);
			return Define4Machine.SERVLET_MACHINE_AUDIT;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.JSP_MACHINE_AUDIT;
		}
	}

	private void auditMacs(String macIds, String orgid) throws SQLException,
			IOException, Exception {
		String[] macId =macIds.split(",");
		String macTemp = "";
		String orgIDTemp = "";
		String macSplitStr = "";
		Map<String,String> map = new HashMap<String,String>();
		
		for(int j=0; j<macId.length; j++){
			if( -1 != macId[j].indexOf("@")){
				String[] macArray = macId[j].split("@");
				macTemp = macArray[0];
				orgIDTemp = macArray[1];
			}else{
				macTemp = macId[j];
				orgIDTemp = orgid;
			}
			
			if(map.containsKey(orgIDTemp)){
				macSplitStr = map.get(orgIDTemp) + "," + macTemp;
				map.remove(orgIDTemp);
				map.put(orgIDTemp, macSplitStr);
			} else {
				map.put(orgIDTemp, macTemp);
			}
		}
	    Iterator it = map.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry entry = (Map.Entry) it.next();
	        dao.auditMacs(entry.getValue().toString(), entry.getKey().toString());
	    }
		
		
		for(int i=0; i<macId.length; i++){
			if( -1 != macId[i].indexOf("@")){
				String[] macArray = macId[i].split("@");
				macTemp = macArray[0];
				orgIDTemp = macArray[1];
			}else{
				macTemp = macId[i];
				orgIDTemp = orgid;
			}
			
			String orgName = (String) new OrgDealDAO().getCurOrgNodeByOrgId(Long.parseLong(orgIDTemp))[0].get("ORG_NAME");
			dao.saveTransfer(macTemp, orgName, user.getId());
			//配置下发
			new ConfigAction(req, res).generalCfgIniFile(macTemp);
			//节目单下发
			new AdvertManagementAction().programlistSynchronize(macTemp, req);
			//频道 快速入口
			ChannelDAO channelDAO = new ChannelDAO();
			channelDAO.updateCommand("0", Long.parseLong(macTemp));
			channelDAO.updateCommand("1", Long.parseLong(macTemp));
			//黑白名单处理
			th.action.availablePage.AuditAction availAuditAction = new th.action.availablePage.AuditAction();
			HashMap[] macMap = {dao.getMacInfo(macTemp)};
			availAuditAction.sendUpdatePackagesToMachine(macMap, orgIDTemp, user.getId());
			
			InteractiveDao itDao = new InteractiveDao();
			//启动“调用端机状态信息上传API”
			itDao.setCommandStatus(Long.parseLong(macTemp),  "17", "1");
			//停止“调用端机状态信息上传API”
			itDao.setCommandStatus(Long.parseLong(macTemp),  "18", "1");
		}
		
	}
	
	
	
	/**
	 * 审核删除
	 * 
	 * @return
	*/
	public String delete(){
		String macIds = req.getParameter("macIds");
		try {
			hasRight("14");
			int result = dao.deleteMacs(macIds);
			System.out.println("成功删除"+result+"条数据!");
			return Define4Machine.SERVLET_MACHINE_AUDIT_QUERY;
		} catch (Exception e) {
			e.printStackTrace();
			return Define4Machine.SERVLET_MACHINE_AUDIT_QUERY;
		}
	}

}
