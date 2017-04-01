/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import th.com.property.LocalProperties;
import th.com.util.Define;
import th.dao.MobileDAO;
import th.entity.AppStoreBean;
import th.entity.ClientBean;
import th.entity.GuestBookBean;
import th.entity.MachineBean;
import th.entity.MessageBean;
import th.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class MobileAction extends BaseAction {
	
	 /**
	  * 根据 User Agent 获取操作系统名称 
	 * @param userAgent
	 * @return
	 */
	public String getOSNameByUserAgent(String userAgent) {
		String osVersion = "Unknown OS";
		if (userAgent.contains("NT 6.2")) {
			//osVersion = "Windows 8/Windows Server 2012";
			osVersion = "Windows 8";
		} else if (userAgent.contains("NT 6.1")) {
			//osVersion = "Windows 7/Windows Server 2008 R2";
			osVersion = "Windows 7";
		} else if (userAgent.contains("NT 6.0")) {
			//osVersion = "Windows Vista/Server 2008";
			osVersion = "Windows Vista";
		} else if (userAgent.contains("NT 5.2")) {
			osVersion = "Windows Server 2003";
		} else if (userAgent.contains("NT 5.1")) {
			osVersion = "Windows XP";
		} else if (userAgent.contains("NT 5")) {
			osVersion = "Windows 2000";
		} else if (userAgent.contains("NT 4")) {
			osVersion = "Windows NT4";
		} else if (userAgent.contains("Me")) {
			osVersion = "Windows Me";
		} else if (userAgent.contains("98")) {
			osVersion = "Windows 98";
		} else if (userAgent.contains("95")) {
			osVersion = "Windows 95";
		} else if (userAgent.contains("Mac")) {
			osVersion = "IOS";
		} else if (userAgent.toUpperCase().contains("ANDROID")) {
			osVersion = "Android";
		} else if (userAgent.contains("Unix")) {
			osVersion = "UNIX";
		} else if (userAgent.contains("Linux")) {
			osVersion = "Linux";
		} else if (userAgent.contains("SunOS")) {
			osVersion = "SunOS";
		}
		return osVersion;
	}
	
	/**  
     * 根据User-Agent，得到用户浏览器和操作系统信息  
     *   
     * @param userAgentInfo  
     * @return ClientInfo  
     */  
	/*public static ClientBean getClientInfo(String userAgentInfo) {
		String info = userAgentInfo.toUpperCase();
		ClientBean clientInfo = new ClientBean();
		String[] strInfo = info.substring(info.indexOf("(") + 1,
				info.indexOf(")")).split(";");
		if ((info.indexOf("MSIE")) > -1) {
			clientInfo.setBrowserInfo(strInfo[1].trim());
			clientInfo.setOsInfo(strInfo[2].trim());
		} else {
			String[] str = info.split(" ");
			if (info.indexOf("NAVIGATOR") < 0 && info.indexOf("FIREFOX") > -1) {
				clientInfo.setBrowserInfo(str[str.length - 1].trim());
				clientInfo.setOsInfo(strInfo[0].trim());
			} else if ((info.indexOf("OPERA")) > -1) {
				clientInfo.setBrowserInfo(str[0].trim());
				clientInfo.setOsInfo(strInfo[0].trim());
			} else if (info.indexOf("CHROME") < 0
					&& info.indexOf("SAFARI") > -1) {
				clientInfo.setBrowserInfo(str[str.length - 1].trim());
				clientInfo.setOsInfo(strInfo[1].trim());//sun android
			} else if (info.indexOf("CHROME") > -1) {
				clientInfo.setBrowserInfo(str[str.length - 2].trim());
				clientInfo.setOsInfo(strInfo[0].trim());
			} else if (info.indexOf("NAVIGATOR") > -1) {
				clientInfo.setBrowserInfo(str[str.length - 1].trim());
				clientInfo.setOsInfo(strInfo[2].trim());
			} else {
				clientInfo.setBrowserInfo("Unknown Browser");
				clientInfo.setOsInfo("Unknown OS");
			}
		}
		return clientInfo;
	}*/
	 
	/**
	 * 端机运行矩阵监控
	 * @param orgID
	 * @param orderType
	 * @return
	 * @throws Exception
	 */
	public String queryMachineByOrgType(HttpServletRequest req, String orgID, String orderType) throws Exception {

		MobileDAO mobileDAO = new MobileDAO();
		List<Integer> machineID = new ArrayList<Integer>();
		List<MachineBean> machineBean = new ArrayList<MachineBean>();
		List<MachineBean> breakBean = new ArrayList<MachineBean>();
		String htmlStr = "";
		String breakTime = LocalProperties.getProperty("MONITOR_BREAK_TIME");
		
		try {
			// 取得当前角色及以下银行资源树			
			HashMap[] resultMap = mobileDAO.getMachineInfoByOrgID(Integer.parseInt(orgID));
			if (null != resultMap && resultMap.length != 0){
				//取得所在组织的端机编号和端机名称
				for (int i = 0; i < resultMap.length; i++) {
					Iterator it = resultMap[i].entrySet().iterator();
					MachineBean bean = new MachineBean();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						if ("MACHINE_ID".equals((String) entry.getKey())) {
							machineID.add(Integer.valueOf((String)entry.getValue()));
							bean.setMachineId(Integer.valueOf((String)entry.getValue()));
						} else if("MACHINE_NAME".equals((String) entry.getKey())){
							bean.setMachineName((String) entry.getValue());
						} else if("STATUS".equals((String) entry.getKey())){
							bean.setStatus((String) entry.getValue());
						} else if("MACHINE_MARK".equals((String) entry.getKey())){
							bean.setMachineMark((String) entry.getValue());
						}
					}
					//只选取广告中和服务中端机，作为在线端机显示
					if("0".equals(bean.getStatus()) || "2".equals(bean.getStatus())){
						bean.setStatus("2");//设定状态, 在线=2;
						machineBean.add(bean);
					}
				}
				
				//取得线路中断端机信息
				HashMap[] breakMap = mobileDAO.getBreakMachineInfoByOrderType(machineID, breakTime);
				if (null != breakMap && breakMap.length != 0){
					for (int i = 0; i < breakMap.length; i++) {
						Iterator it = breakMap[i].entrySet().iterator();
						MachineBean bean = new MachineBean();
						while (it.hasNext()) {
							Map.Entry entry = (Map.Entry) it.next();
							if ("MACHINE_ID".equals((String) entry.getKey())) {
								machineID.add(Integer.valueOf((String)entry.getValue()));
								bean.setMachineId(Integer.valueOf((String)entry.getValue()));
							} else if("MACHINE_NAME".equals((String) entry.getKey())){
								bean.setMachineName((String) entry.getValue());
							} else if("MACHINE_MARK".equals((String) entry.getKey())){
								bean.setMachineMark((String) entry.getValue());
							}
							bean.setStatus("1");//离线
						}
						breakBean.add(bean);
					}
				}
				
				
				//端机显示排序
				//orderType :  1=离线 ; 2=在线
				htmlStr = sortListByType(req, orderType, machineBean, breakBean);
			} else {
				req.setAttribute("onlineNum", 0);
				req.setAttribute("offlineNum", 0);
				logger.debug("查询结果为空");
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return htmlStr;
	}
	
	private String sortListByType(HttpServletRequest req, String orderType, List<MachineBean> machineBean, List<MachineBean> breakBean){
		int onlineNum = 0;
		int offlineNum = 0;
		
		//更新machineBean端机状态，线路中断状态 替换正常状态
		for(int i = 0 ; i < breakBean.size(); i++){
			for(int j = 0 ; j < machineBean.size(); j++){
				if(breakBean.get(i).getMachineId()==machineBean.get(j).getMachineId()){
					machineBean.remove(j);
					machineBean.add(breakBean.get(i));
				}
			}
		}
		
		List<String> srtBean = new ArrayList<String>();
		//优先状态排序
		for (int k = 0; k < machineBean.size(); k++) {
			
			if(orderType.equals(machineBean.get(k).getStatus())){
				//1=离线 ; 2=在线
				if("2".equals(orderType)){
					srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" class=\"td_mobile_online\" >"+machineBean.get(k).getMachineMark()+"</td>");
					onlineNum++;
				} else if("1".equals(orderType)){
					srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" class=\"td_mobile_offline\" >"+machineBean.get(k).getMachineMark()+"</td>");
					offlineNum++;
				} else {
					logger.debug("端机状态无法获取");
				}
				machineBean.set(k, new MachineBean());
			}
		}
		
		//其他状态排序
		for (int m = 0; m < machineBean.size(); m++) {
			if("2".equals(machineBean.get(m).getStatus())){
				srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" class=\"td_mobile_online\" >"+machineBean.get(m).getMachineMark()+"</td>");
				onlineNum++;
			} else if("1".equals(machineBean.get(m).getStatus())){
				srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" class=\"td_mobile_offline\" >"+machineBean.get(m).getMachineMark()+"</td>");
				offlineNum++;
			} else {
				logger.debug("端机状态无法获取");
			}
		}
		
		req.setAttribute("onlineNum", onlineNum);
		req.setAttribute("offlineNum", offlineNum);
		
		//组织画面端机样式
		StringBuffer st = new StringBuffer();
		int n = 0;
		for (n = 0; n < srtBean.size(); n++) {
			if (n % 6 == 0) {
				st.append("<tr>");
			}
			st.append(srtBean.get(n));
			if (n % 6 == 5 && n != 0) {
				st.append("</tr>");
			}
		}
		if((n-1) % 6 < 5){
			//只有一行端机时,填补<TD>
			if (srtBean.size() < 6) {
				for (int b = 6 - srtBean.size(); b > 0; b--) {
					st.append("<td/>");
				}
			}
			st.append("</tr>");
		}
		//端机显示不足4行时，填补<TR>
		if (srtBean.size() < 19) {
			for (int b = 0; b < (24 - srtBean.size()) / 6; b++) {
				st.append("<tr/>");
			}
		}
		
		return st.toString();
	}
	
	public void searchAppStore(HttpServletRequest req, String osName) throws Exception {
		AppStoreBean bean = new AppStoreBean();

		// 检索结果当前位置
		if (null != req.getParameter("point_num")) {
			bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		} else {
			bean.setPoint_num(1);
		}

		List<AppStoreBean> resultBeans = new ArrayList<AppStoreBean>();

		HashMap[] map = new MobileDAO().getAppStoreList(osName);
		if (map != null && map.length != 0) {
			// 检索结果开始位置
			int start_p = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			// 检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;

			int loop = 0;
			for (int i = 0; i < map.length; i++) {
				loop++;
				if (loop <= start_p) {
					continue;
				}
				if (loop > start_p && loop <= end_p) {
					start_p++;
					AppStoreBean appBean = new AppStoreBean();
					Iterator it = map[i].entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						if ("APP_NAME".equals((String) entry.getKey())) {
							appBean.setAppName((String) entry.getValue());
						} else if ("DESCRIPTION".equals((String) entry.getKey())) {
							appBean.setDescription((String) entry.getValue());
						} else if ("DL_URL".equals((String) entry.getKey())) {
							appBean.setDownloadUrl((String) entry.getValue());
						} else if ("ICON_URL".equals((String) entry.getKey())) {
							appBean.setIconUrl((String) entry.getValue());
						} else if ("APP_ID".equals((String) entry.getKey())) {
							appBean.setAppId(Long.parseLong((String) entry.getValue()));
						} else if ("VERSION_TYPE".equals((String) entry.getKey())) {
							appBean.setVersionType((String) entry.getValue());
						} else if ("VERSION".equals((String) entry.getKey())) {
							appBean.setVersion(StringUtils.transStr((String) entry.getValue()));
						}
					}
					resultBeans.add(appBean);
				}
			}
			bean.setTotal_num(map.length);
		}
		// 检索条件保存
		req.setAttribute("select_object", bean);
		// 检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	public void searchGuestBook(HttpServletRequest req, String orgID) throws Exception {
		GuestBookBean bean = new GuestBookBean();

		// 检索结果当前位置
		if (null != req.getParameter("point_num")) {
			bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		} else {
			bean.setPoint_num(1);
		}

		//取得当前用户权限下所有意见簿信息
		List<GuestBookBean> resultBeans = new ArrayList<GuestBookBean>();
		
		HashMap[] map = new MobileDAO().getGuestBookInfoByOrgID(Integer.parseInt(orgID));
		
		if (map != null && map.length != 0) {
			// 检索结果开始位置
			int start_p = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			// 检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;

			int loop = 0;
			for (int i = 0; i < map.length; i++) {
				loop++;
				if (loop <= start_p) {
					continue;
				}
				if (loop > start_p && loop <= end_p) {
					start_p++;
					GuestBookBean gBean = new GuestBookBean();
					Iterator it = map[i].entrySet().iterator();
					
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						if ("ORG_NAME".equals((String) entry.getKey())) {
							gBean.setOrgName((String)entry.getValue());
						} else if("MACHINE_NAME".equals((String) entry.getKey())){
							gBean.setMachineName((String) entry.getValue());
						} else if("MACHINE_MARK".equals((String) entry.getKey())){
							gBean.setMachineMark((String) entry.getValue());
						} else if("IDEA_ID".equals((String) entry.getKey())){
							gBean.setIdeaId(Long.parseLong((String) entry.getValue()));
						} else if("IDEA_TYPE".equals((String) entry.getKey())){
							gBean.setIdeaType((String) entry.getValue());
						} else if("IDEA_CONTENT".equals((String) entry.getKey())){
							gBean.setIdeaContent((String) entry.getValue());
						} else if("OPERATETIME".equals((String) entry.getKey())){
							gBean.setOperateTime((String) entry.getValue());
						}
					}
					resultBeans.add(gBean);
				}
			}
			bean.setTotal_num(map.length);
		}
		// 检索条件保存
		req.setAttribute("select_object", bean);
		// 检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	public void searchMessage(HttpServletRequest req, String orgID) throws Exception {
		MessageBean bean = new MessageBean();

		// 检索结果当前位置
		if (null != req.getParameter("point_num")) {
			bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		} else {
			bean.setPoint_num(1);
		}

		//取得当前用户权限下所有意见簿信息
		List<MessageBean> resultBeans = new ArrayList<MessageBean>();
		
		HashMap[] map = new MobileDAO().getMessageInfoByOrgID(Integer.parseInt(orgID));
		
		if (map != null && map.length != 0) {
			// 检索结果开始位置
			int start_p = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			// 检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;

			int loop = 0;
			for (int i = 0; i < map.length; i++) {
				loop++;
				if (loop <= start_p) {
					continue;
				}
				if (loop > start_p && loop <= end_p) {
					start_p++;
					MessageBean mBean = new MessageBean();
					Iterator it = map[i].entrySet().iterator();
					
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						if ("ORG_NAME".equals((String) entry.getKey())) {
							mBean.setOrgName((String)entry.getValue());
						} else if("MACHINE_NAME".equals((String) entry.getKey())){
							mBean.setMachineName((String) entry.getValue());
						} else if("MACHINE_MARK".equals((String) entry.getKey())){
							mBean.setMachineMark((String) entry.getValue());
						} else if("MSG_CONTENT".equals((String) entry.getKey())){
							mBean.setMsgContent((String) entry.getValue());
						} else if("OPERATETIME".equals((String) entry.getKey())){
							mBean.setOperateTime((String) entry.getValue());
						} else if("NAME".equals((String) entry.getKey())){
							mBean.setOperator((String) entry.getValue());
						}
					}
					resultBeans.add(mBean);
				}
			}
			bean.setTotal_num(map.length);
		}
		// 检索条件保存
		req.setAttribute("select_object", bean);
		// 检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
}
