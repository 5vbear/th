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

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import th.com.property.LocalProperties;
import th.com.property.LocalPropertiesException;
import th.com.util.Define;
import th.dao.MonitorDAO;
import th.dao.OrgDealDAO;
import th.dao.machine.MachineDAO;
import th.entity.AlarmBean;
import th.entity.MachineBean;
import th.entity.MachineProcessBean;
import th.entity.MachineServiceBean;
import th.util.StringUtils;
import th.util.ftp.FtpUtils;

import java.awt.Color; 
import java.awt.Font; 
import java.awt.RenderingHints;
import java.io.BufferedReader;
import java.io.IOException; 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import org.jfree.chart.ChartFactory; 
import org.jfree.chart.JFreeChart; 
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot; 
import org.jfree.chart.servlet.ServletUtilities; 
import org.jfree.chart.title.TextTitle; 

/**
 * Descriptions
 * 
 * @version 2013-8-12
 * @author PSET
 * @since JDK1.6
 * 
 */
@SuppressWarnings("rawtypes")
public class MonitorAction extends BaseAction {
	
	private static JFreeChart cpuChart;
	private static JFreeChart memoryChart;
	private static JFreeChart uploadChart;
	private static JFreeChart downloadChart;
	
	private static XYSeriesCollection cpuSeriesCollection;
	private static XYSeriesCollection memorySeriesCollection;
	private static XYSeriesCollection uploadSeriesCollection;
	private static XYSeriesCollection downloadSeriesCollection;

	/**
	 * 端机运行矩阵监控
	 * @param orgID
	 * @param orderType
	 * @return
	 * @throws Exception
	 */
	public String queryMachineByOrgType(HttpServletRequest req, String orgID, String orderType, String macType) throws Exception {

		MonitorDAO monitorDAO = new MonitorDAO();
		List<Integer> machineID = new ArrayList<Integer>();
		List<MachineBean> machineBean = new ArrayList<MachineBean>();
		List<MachineBean> breakBean = new ArrayList<MachineBean>();
		String htmlStr = "";
		String breakTime = LocalProperties.getProperty("MONITOR_BREAK_TIME");
		
		try {
			// 取得当前角色及以下银行资源树			
			HashMap[] resultMap = monitorDAO.getMachineInfoByOrgID(Integer.parseInt(orgID), macType);
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
						} else if("OS".equals((String) entry.getKey())){
							bean.setOs((String) entry.getValue());
						} else if("MACHINE_KIND".equals((String) entry.getKey())){
							bean.setMachine_kind((String) entry.getValue());
						}
					}
					machineBean.add(bean);
				}
				
				//取得线路中断端机信息
				HashMap[] breakMap = monitorDAO.getBreakMachineInfoByOrderType(machineID, breakTime, macType);
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
							} else if("OS".equals((String) entry.getKey())){
								bean.setOs((String) entry.getValue());
							} else if("MACHINE_KIND".equals((String) entry.getKey())){
								bean.setMachine_kind((String) entry.getValue());
							}
							bean.setStatus("4");//线路中断 
						}
						breakBean.add(bean);
					}
				}
				
				
				//端机显示排序
				//orderType : 0=服务中  ; 1=报停  ; 2=广告中  ; 4=线路中断 
				htmlStr = sortListByType(req, orderType, machineBean, breakBean);
			} else {
				req.setAttribute("ggNum", 0);
				req.setAttribute("fwzNum", 0);
				req.setAttribute("xlzdNum", 0);
				req.setAttribute("btNum", 0);
				logger.debug("查询结果为空");
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return htmlStr;
	}
	
	public void displayMachineListByType(HttpServletRequest req, String orgID, String orderType, String macType) throws Exception {
		MachineBean bean = new MachineBean();
		
		//检索结果当前位置
		if(null != req.getParameter("point_num")){
			bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		} else {
			bean.setPoint_num(1);
		}
		
		List<String> resultBeans = new ArrayList<String>();
		//端机html list
		List<String> macList = queryMachineListByOrgID(req, orgID, orderType, macType);
		if (macList != null) {
			//检索结果开始位置
			int start_p  = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			//检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;
			
			int loop = 0;
			for (String sBean : macList) {
				loop++;
				if( loop <= start_p ) {
                    continue;
                }
				if( loop > start_p && loop <= end_p ) {
					start_p++;
					resultBeans.add(sBean);
				}
			}
			//检索总行数
			bean.setTotal_num(macList.size());
		}
		//检索条件保存
		req.setAttribute("select_object", bean);
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
	/**
	 * 端机运行列表监控:按排序查询端机列表
	 * @param orgID
	 * @param orderType
	 * @param macType
	 * @return
	 */
	private List<String> queryMachineListByOrgID(HttpServletRequest req, String orgID, String orderType, String macType) throws Exception {
		MonitorDAO monitorDAO = new MonitorDAO();
		List<Integer> machineID = new ArrayList<Integer>();
		List<MachineBean> machineBean = new ArrayList<MachineBean>();
		List<MachineBean> breakBean = new ArrayList<MachineBean>();
		List<String> htmlList = new ArrayList<String>();
		String breakTime = LocalProperties.getProperty("MONITOR_BREAK_TIME");
		
		macType = th.util.StringUtils.isNotBlank(macType) ? macType : "0";
		try {
			// 取得当前角色及以下银行资源树			
			HashMap[] resultMap = monitorDAO.getMachineInfoByOrgID(Integer.parseInt(orgID), macType);
			if ( null != resultMap && resultMap.length != 0){
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
						} else if("OS".equals((String) entry.getKey())){
							bean.setOs((String) entry.getValue());
						} else if("MACHINE_KIND".equals((String) entry.getKey())){
							bean.setMachine_kind((String) entry.getValue());
						}
					}
					machineBean.add(bean);
				}
				
				//取得线路中断端机信息
				HashMap[] breakMap = monitorDAO.getBreakMachineInfoByOrderType(machineID, breakTime, macType);
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
							} else if("OS".equals((String) entry.getKey())){
								bean.setOs((String) entry.getValue());
							} else if("MACHINE_KIND".equals((String) entry.getKey())){
								bean.setMachine_kind((String) entry.getValue());
							}
							bean.setStatus("4");//线路中断 
						}
						breakBean.add(bean);
					}
				}
				
				
				//端机显示排序
				//orderType : 0=服务中  ; 1=报停  ; 2=广告中  ; 4=线路中断 
				htmlList = sortTableListByType(req, orderType, machineBean, breakBean);
			} else {
				req.setAttribute("ggNum", 0);
				req.setAttribute("fwzNum", 0);
				req.setAttribute("xlzdNum", 0);
				req.setAttribute("btNum", 0);
				logger.debug("查询结果为空");
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
		return htmlList;
	}
	
	private String sortListByType(HttpServletRequest req, String orderType, List<MachineBean> machineBean, List<MachineBean> breakBean){
		int ggNum = 0;
		int fwzNum = 0;
		int xlzdNum = 0;
		int btNum = 0;
		
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
				if(Define.MACHINE_STATUS_ADS.equals(orderType)){
					if(machineBean.get(k).getOs() == null || "".equals(machineBean.get(k).getOs()) || machineBean.get(k).getMachine_kind() == null || "".equals(machineBean.get(k).getMachine_kind())){
						srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_gg\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
					} else if(machineBean.get(k).getOs().indexOf("Windows") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pc")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_gg\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_gg_win_pad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
					} else if(machineBean.get(k).getOs().indexOf("IOS") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_gg_ipad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("phone")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_gg_iphone\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					} else if(machineBean.get(k).getOs().indexOf("Android") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_gg_andriod_pad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("phone")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_gg_andriod_phone\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					}
					ggNum++;
				} else if(Define.MACHINE_STATUS_SERVICE.equals(orderType)){
					if(machineBean.get(k).getOs() == null || "".equals(machineBean.get(k).getOs()) || machineBean.get(k).getMachine_kind() == null || "".equals(machineBean.get(k).getMachine_kind())){
						srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_fwz\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
					} else if(machineBean.get(k).getOs().indexOf("Windows") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pc")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_fwz\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_fwz_win_pad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					} else if(machineBean.get(k).getOs().indexOf("IOS") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_fwz_ipad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("phone")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_fwz_iphone\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					} else if(machineBean.get(k).getOs().indexOf("Android") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_fwz_andriod_pad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("phone")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_fwz_andriod_phone\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					}
					
					fwzNum++;
				} else if(Define.MACHINE_STATUS_BREAK.equals(orderType)){
					if(machineBean.get(k).getOs() == null || "".equals(machineBean.get(k).getOs()) || machineBean.get(k).getMachine_kind() == null || "".equals(machineBean.get(k).getMachine_kind())){
						srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_xlzd\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
					} else if(machineBean.get(k).getOs().indexOf("Windows") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pc")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_xlzd\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_xlzd_win_pad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					} else if(machineBean.get(k).getOs().indexOf("IOS") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_xlzd_ipad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("phone")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_xlzd_iphone\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					} else if(machineBean.get(k).getOs().indexOf("Android") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_xlzd_andriod_pad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("phone")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_xlzd_andriod_phone\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					}
					
					xlzdNum++;
				} else if(Define.MACHINE_STATUS_STOP.equals(orderType)){
					if(machineBean.get(k).getOs() == null || "".equals(machineBean.get(k).getOs()) || machineBean.get(k).getMachine_kind() == null || "".equals(machineBean.get(k).getMachine_kind())){
						srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_bt\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
					} else if(machineBean.get(k).getOs().indexOf("Windows") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pc")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_bt\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_bt_win_pad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					} else if(machineBean.get(k).getOs().indexOf("IOS") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_bt_ipad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("phone")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_bt_iphone\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					} else if(machineBean.get(k).getOs().indexOf("Android") != -1){
						if(machineBean.get(k).getMachine_kind().equals("pad")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_bt_andriod_pad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						} else if(machineBean.get(k).getMachine_kind().equals("phone")){
							srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" name=\""+machineBean.get(k).getOs()+"\" class=\"td_bt_andriod_phone\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(k).getMachineMark()+"</td>");
						}
						
					}
					
					btNum++;
				} else {
					logger.debug("端机状态无法获取");
				}
				machineBean.set(k, new MachineBean());
			}
		}
		
		//其他状态排序
		for (int m = 0; m < machineBean.size(); m++) {
			if(Define.MACHINE_STATUS_ADS.equals(machineBean.get(m).getStatus())){
				if(machineBean.get(m).getOs() == null || "".equals(machineBean.get(m).getOs()) || machineBean.get(m).getMachine_kind() == null || "".equals(machineBean.get(m).getMachine_kind())){
					srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_gg\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
				} else if(machineBean.get(m).getOs().indexOf("Windows") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pc")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_gg\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_gg_win_pad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
				} else if(machineBean.get(m).getOs().indexOf("IOS") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_gg_ipad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("phone")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_gg_iphone\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				} else if(machineBean.get(m).getOs().indexOf("Android") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_gg_andriod_pad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("phone")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_gg_andriod_phone\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				}
				ggNum++;
			} else if(Define.MACHINE_STATUS_SERVICE.equals(machineBean.get(m).getStatus())){
				if(machineBean.get(m).getOs() == null || "".equals(machineBean.get(m).getOs()) || machineBean.get(m).getMachine_kind() == null || "".equals(machineBean.get(m).getMachine_kind())){
					srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_fwz\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
				} else if(machineBean.get(m).getOs().indexOf("Windows") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pc")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_fwz\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_fwz_win_pad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				} else if(machineBean.get(m).getOs().indexOf("IOS") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_fwz_ipad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("phone")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_fwz_iphone\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				} else if(machineBean.get(m).getOs().indexOf("Android") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_fwz_andriod_pad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("phone")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_fwz_andriod_phone\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				}
				fwzNum++;
			} else if(Define.MACHINE_STATUS_BREAK.equals(machineBean.get(m).getStatus())){
				if(machineBean.get(m).getOs() == null || "".equals(machineBean.get(m).getOs()) || machineBean.get(m).getMachine_kind() == null || "".equals(machineBean.get(m).getMachine_kind())){
					srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_xlzd\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
				} else if(machineBean.get(m).getOs().indexOf("Windows") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pc")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_xlzd\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_xlzd_win_pad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				} else if(machineBean.get(m).getOs().indexOf("IOS") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_xlzd_ipad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("phone")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_xlzd_iphone\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				} else if(machineBean.get(m).getOs().indexOf("Android") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_xlzd_andriod_pad\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("phone")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_xlzd_andriod_phone\" oncontextmenu=\"closeRightMenu();\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				}
				
				xlzdNum++;
			} else if(Define.MACHINE_STATUS_STOP.equals(machineBean.get(m).getStatus())){
				if(machineBean.get(m).getOs() == null || "".equals(machineBean.get(m).getOs()) || machineBean.get(m).getMachine_kind() == null || "".equals(machineBean.get(m).getMachine_kind())){
					srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_bt\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
				} else if(machineBean.get(m).getOs().indexOf("Windows") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pc")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_bt\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_bt_win_pad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				} else if(machineBean.get(m).getOs().indexOf("IOS") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_bt_ipad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("phone")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_bt_iphone\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				} else if(machineBean.get(m).getOs().indexOf("Android") != -1){
					if(machineBean.get(m).getMachine_kind().equals("pad")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_bt_andriod_pad\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					} else if(machineBean.get(m).getMachine_kind().equals("phone")){
						srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" name=\""+machineBean.get(m).getOs()+"\" class=\"td_bt_andriod_phone\" oncontextmenu=\"openRightMenu(this.id, this.name);\">"+machineBean.get(m).getMachineMark()+"</td>");
					}
					
				}
				
				btNum++;
			} else {
				logger.debug("端机状态无法获取");
			}
		}
		
		req.setAttribute("ggNum", ggNum);
		req.setAttribute("fwzNum", fwzNum);
		req.setAttribute("xlzdNum", xlzdNum);
		req.setAttribute("btNum", btNum);
		
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
	
	
	/**
	 * @param orderType
	 * @param machineBean
	 * @param breakBean
	 * @return
	 * @throws SQLException 
	 */
	private List<String> sortTableListByType(HttpServletRequest req, String orderType, List<MachineBean> machineBean, List<MachineBean> breakBean) throws SQLException{
		String emptyTD = "<td/>";
		int ggNum = 0;
		int fwzNum = 0;
		int xlzdNum = 0;
		int btNum = 0;
		
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
				if(Define.MACHINE_STATUS_ADS.equals(orderType)){
					srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" class=\"td_tianhe_gg\" >"+machineBean.get(k).getMachineMark()+"</td>" + buildMacList(String.valueOf(machineBean.get(k).getMachineId()), String.valueOf(machineBean.get(k).getOs()), "td_tianhe_gg"));
					ggNum++;
				} else if(Define.MACHINE_STATUS_SERVICE.equals(orderType)){
					srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" class=\"td_tianhe_fwz\" >"+machineBean.get(k).getMachineMark()+"</td>" + emptyTD);
					fwzNum++;
				} else if(Define.MACHINE_STATUS_BREAK.equals(orderType)){
					srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" class=\"td_tianhe_xlzd\" >"+machineBean.get(k).getMachineMark()+"</td>" + emptyTD);
					xlzdNum++;
				} else if(Define.MACHINE_STATUS_STOP.equals(orderType)){
					srtBean.add("<td id=\""+machineBean.get(k).getMachineId()+"\" class=\"td_tianhe_bt\" >"+machineBean.get(k).getMachineMark()+"</td>" + buildMacList(String.valueOf(machineBean.get(k).getMachineId()), String.valueOf(machineBean.get(k).getOs()), "td_tianhe_bt"));
					btNum++;
				} else {
					logger.debug("端机状态无法获取");
				}
				machineBean.set(k, new MachineBean());
			}
		}
		
		//其他状态排序
		for (int m = 0; m < machineBean.size(); m++) {
			if(Define.MACHINE_STATUS_ADS.equals(machineBean.get(m).getStatus())){
				srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" class=\"td_tianhe_gg\" >"+machineBean.get(m).getMachineMark()+"</td>" + buildMacList(String.valueOf(machineBean.get(m).getMachineId()), String.valueOf(machineBean.get(m).getOs()), "td_tianhe_gg"));
				ggNum++;
			} else if(Define.MACHINE_STATUS_SERVICE.equals(machineBean.get(m).getStatus())){
				srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" class=\"td_tianhe_fwz\" >"+machineBean.get(m).getMachineMark()+"</td>" + emptyTD);
				fwzNum++;
			} else if(Define.MACHINE_STATUS_BREAK.equals(machineBean.get(m).getStatus())){
				srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" class=\"td_tianhe_xlzd\" >"+machineBean.get(m).getMachineMark()+"</td>" + emptyTD);
				xlzdNum++;
			} else if(Define.MACHINE_STATUS_STOP.equals(machineBean.get(m).getStatus())){
				srtBean.add("<td id=\""+machineBean.get(m).getMachineId()+"\" class=\"td_tianhe_bt\" >"+machineBean.get(m).getMachineMark()+"</td>" + buildMacList(String.valueOf(machineBean.get(m).getMachineId()), String.valueOf(machineBean.get(m).getOs()), "td_tianhe_bt"));
				btNum++;
			} else {
				logger.debug("端机状态无法获取");
			}
		}
		
		req.setAttribute("ggNum", ggNum);
		req.setAttribute("fwzNum", fwzNum);
		req.setAttribute("xlzdNum", xlzdNum);
		req.setAttribute("btNum", btNum);
		return srtBean;
	}
	
	private String buildMacList(String machineID,String os, String css) throws SQLException{
		StringBuffer sb = new StringBuffer();
		String[] array = showSingle(machineID).split(",");
		sb.append("<td class=\"" + css + "\">");
		sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"refreshButton\" value=\"刷新\" class=\"leftBtn\" onclick=\"rightOperation(this,1);\"/>");
		sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"macButton\" value=\"设备信息\" class=\"leftBtn\" onclick=\"rightOperation(this,2);\"/>");
		//sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"deployButton\" value=\"部署信息\" class=\"leftBtn\" onclick=\"rightOperation(this,3);\" />");
		if(os.indexOf("Android") != -1){
			if("1".equals(array[0])){
				sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"stopUseButton\" value=\"启用\" class=\"leftBtn\" onclick=\"rightOperation(this,4);\" />");
			} else if("0".equals(array[0])){
				sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"stopUseButton\" value=\"锁定\\报停\" class=\"leftBtn\" onclick=\"rightOperation(this,5);\" />");
			}
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"locateButton\" value=\"定位\" class=\"leftBtn\" onclick=\"rightOperation(this,12);\" />");
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"clearDataButton\" value=\"清除数据\" class=\"leftBtn\" onclick=\"rightOperation(this,8);\" />");
		}
		if(os.indexOf("Windows") != -1 || os.indexOf("Android") != -1){
			if("0".equals(array[1])){
				sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"startEndScreenShotButton\" value=\"启动截屏\" class=\"leftBtn\" onclick=\"rightOperation(this,10);\" />");
			} else if("1".equals(array[1])){
				sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"startEndScreenShotButton\" value=\"停止截屏\" class=\"leftBtn\" onclick=\"rightOperation(this,11);\" />");
			}
		}

		if("0".equals(array[2])){
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"startEndAdvButton\" value=\"开始广告播放\" class=\"leftBtn\" onclick=\"rightOperation(this,13);\" />");
		} else if("1".equals(array[2])){
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"startEndAdvButton\" value=\"停止广告播放\" class=\"leftBtn\" onclick=\"rightOperation(this,14);\" />");
		}
		if("0".equals(array[3])){
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"startEndTempAdvButton\" value=\"开始播放临时广告\" class=\"leftBtn\" onclick=\"rightOperation(this,15);\" />");
		} else if("1".equals(array[3])){
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"startEndTempAdvButton\" value=\"停止播放临时广告\" class=\"leftBtn\" onclick=\"rightOperation(this,16);\" />");
		}
		if(os.indexOf("Android") != -1 || os.indexOf("IOS") != -1){
			if("0".equals(array[4])){
				sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"lockUnlockButton\" value=\"锁屏\" class=\"leftBtn\" onclick=\"rightOperation(this,17);\" />");
			} else if("1".equals(array[4])){
				sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"lockUnlockButton\" value=\"解锁\" class=\"leftBtn\" onclick=\"rightOperation(this,18);\" />");
			}
		}
		if(os.indexOf("Windows") != -1){
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"clearDataButton\" value=\"清除数据\" class=\"leftBtn\" onclick=\"rightOperation(this,8);\" />");
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"restartButton\" value=\"重启\" class=\"leftBtn\" onclick=\"rightOperation(this,6);\" />");
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"shutDownButton\" value=\"关机\" class=\"leftBtn\" onclick=\"rightOperation(this,7);\" />");
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"remoteButton\" value=\"远程接管\" class=\"leftBtn\" onclick=\"remote(this.id);\" />");
		}


		if(os.indexOf("Android") != -1 || os.indexOf("IOS") != -1){
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"sendMessage\" value=\"发送消息\" class=\"leftBtn\" onclick=\"rightOperation(this,19);\" />");
			sb.append("<input type=\"button\" id=\"" + machineID + "\" name=\"retirement\" value=\"报废\" class=\"leftBtn\" onclick=\"rightOperation(this,20);\" />");
		}
		
		sb.append("</td>");
		
		return sb.toString();
	}
	
	public void queryMachineLocationListByOrgID(HttpServletRequest req, String orgID, String macType) throws Exception{
		MonitorDAO monitorDAO = new MonitorDAO();
		List<Integer> machineID = new ArrayList<Integer>();
		List<MachineBean> machineBean = new ArrayList<MachineBean>();
		List<MachineBean> breakBean = new ArrayList<MachineBean>();
		String breakTime = LocalProperties.getProperty("MONITOR_BREAK_TIME");
		String statusStr = "";
		macType = th.util.StringUtils.isNotBlank(macType) ? macType : "0";
		try {
			HashMap[] resultMap = monitorDAO.getMachineInfoByOrgID(Integer.parseInt(orgID), macType);
			if (null != resultMap && resultMap.length != 0){
				//取得所在组织的端机信息
				for (int i = 0; i < resultMap.length; i++) {
					Iterator it = resultMap[i].entrySet().iterator();
					MachineBean bean = new MachineBean();
					while (it.hasNext()) {
						Map.Entry entry = (Map.Entry) it.next();
						if ("MACHINE_ID".equals((String) entry.getKey())) {
							machineID.add(Integer.valueOf((String)entry.getValue()));
							bean.setMachineId(Integer.valueOf((String)entry.getValue()));
						} else if("STATUS".equals((String) entry.getKey())){
							statusStr = (String) entry.getValue();
							bean.setStatus(statusStr);
							if(Define.MACHINE_STATUS_ADS.equals(statusStr)){
								bean.setIconPath("./image/img/map_gg.png");
							} else if(Define.MACHINE_STATUS_SERVICE.equals(statusStr)){
								bean.setIconPath("./image/img/map_fwz.png");
							} else if(Define.MACHINE_STATUS_STOP.equals(statusStr)){
								bean.setIconPath("./image/img/map_bt.png");
							}
						} else if("MACHINE_MARK".equals((String) entry.getKey())){
							bean.setMachineMark((String) entry.getValue());
						} else if("LOCATION_LATITUDE".equals((String) entry.getKey())){
							bean.setLatitude((String) entry.getValue());
						} else if("LOCATION_LONGITUDE".equals((String) entry.getKey())){
							bean.setLongitude((String) entry.getValue());
						} else if("LOCATION_NAME".equals((String) entry.getKey())){
							bean.setLocationName((String) entry.getValue());
						} else if("OS".equals((String) entry.getKey())){
							bean.setOs((String) entry.getValue());
						} else if("MACHINE_KIND".equals((String) entry.getKey())){
							bean.setMachine_kind((String) entry.getValue());
						}
					}
					if(!"".equals(StringUtils.transStr(bean.getLongitude())) && !"".equals(StringUtils.transStr(bean.getLatitude()))){
						machineBean.add(bean);
					}
				}
				
				//取得线路中断端机信息
				HashMap[] breakMap = monitorDAO.getBreakMachineInfoByOrderType(machineID, breakTime, macType);
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
							} else if("OS".equals((String) entry.getKey())){
								bean.setOs((String) entry.getValue());
							} else if("MACHINE_KIND".equals((String) entry.getKey())){
								bean.setMachine_kind((String) entry.getValue());
							}
							bean.setIconPath("./image/img/map_fwzd.png");
							bean.setStatus("4");//线路中断 
						}
						breakBean.add(bean);
					}
				}
				
				//更新machineBean端机状态，线路中断状态 替换正常状态
				for(int i = 0 ; i < breakBean.size(); i++){
					for(int j = 0 ; j < machineBean.size(); j++){
						if(breakBean.get(i).getMachineId()==machineBean.get(j).getMachineId()){
							MachineBean breakTemp = breakBean.get(i);
							breakTemp.setLatitude(machineBean.get(j).getLatitude());
							breakTemp.setLongitude(machineBean.get(j).getLongitude());
							breakTemp.setLocationName(machineBean.get(j).getLocationName());
							
							machineBean.remove(j);
							machineBean.add(breakTemp);
						}
					}
				}
				
				HashMap[] map = new OrgDealDAO().getCurOrgNodeByOrgId(Long.parseLong(orgID));
				if(map!=null&&map.length>0){
					int zoomLevel = 5;
					if(machineBean != null && machineBean.size() > 0){
						String level = (String)map[0].get( "ORG_LEVEL" );
						if("2".equals(level)){
							zoomLevel = 7;
						} else if("3".equals(level)){
							zoomLevel = 11;
						}
					}
					req.setAttribute("zoomLevel", zoomLevel);
				}
				
				req.setAttribute("location", machineBean);
			} else {
				req.setAttribute("location", new ArrayList<MachineBean>());
				logger.debug("查询结果为空");
			}
		} catch (SQLException e) {
			logger.debug(e.getMessage());
		}
	} 
	/**
	 * @param userOrgs
	 * @param orgCurrentID
	 * @return 
	 */
	public String buildOrgOption(List<HashMap> userOrgs){
		/*HashMap curOrgNode = null;
		long curOrgId = -1;
		long curParentOrgId = -1;*/
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		if(userOrgs!=null&&userOrgs.size()>0){
			/*System.out.println("--------------------组织节点详情---------------------");
			for(int i=0;i<userOrgs.size();i++){
				curOrgNode = (HashMap)userOrgs.get( i );
				curOrgId = Long.parseLong(curOrgNode.get( "ORG_ID" ).toString());
				orgName = curOrgNode.get( "ORG_NAME" ).toString();
				curParentOrgId = Long.parseLong(curOrgNode.get( "PARENT_ORG_ID" ).toString());
				System.out.println("父节点ID : "+ curParentOrgId + " --- 当前节点ID ： "+curOrgId+" --- 节点名称 ： "+orgName);
			}
			System.out.println("--------------------生成组织树---------------------");*/
			sb.append( "[ " );
			for (int i=0;i<userOrgs.size();i++){//userOrgs替换

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );

				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=2 ){
					sb.append( ", open:true " );
				}
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}

			}
			sb.append( " ];" );
			
		}
		return sb.toString();
	}
	
	/**
	 * @param userOrgs
	 * @param orgCurrentID
	 * @return 
	 */
	public String buildSecondOrgOption(List<HashMap> userOrgs){
		/*HashMap curOrgNode = null;
		long curOrgId = -1;
		long curParentOrgId = -1;*/
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		if(userOrgs!=null&&userOrgs.size()>0){
			/*System.out.println("--------------------组织节点详情---------------------");
			for(int i=0;i<userOrgs.size();i++){
				curOrgNode = (HashMap)userOrgs.get( i );
				curOrgId = Long.parseLong(curOrgNode.get( "ORG_ID" ).toString());
				orgName = curOrgNode.get( "ORG_NAME" ).toString();
				curParentOrgId = Long.parseLong(curOrgNode.get( "PARENT_ORG_ID" ).toString());
				System.out.println("父节点ID : "+ curParentOrgId + " --- 当前节点ID ： "+curOrgId+" --- 节点名称 ： "+orgName);
			}
			System.out.println("--------------------生成组织树---------------------");*/
			sb.append( "[ " );
			sb.append( "{ \"id\": -100, \"pId\": -100, \"name\": \"------\", open:true }," );
			for (int i=0;i<userOrgs.size();i++){//userOrgs替换

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );
				
				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=2 ){
					sb.append( ", open:true " );
				}
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}

			}
			sb.append( " ];" );
			
		}
		return sb.toString();
	}
	
	public String buildOrderOrgOption(List<HashMap> userOrgs){
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		if(userOrgs!=null&&userOrgs.size()>0){
			sb.append( "[ " );
			for (int i=0;i<userOrgs.size();i++){//userOrgs替换

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );

				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=2 ){
					sb.append( ", open:true " );
				}
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}

			}
			sb.append( " ];" );
			
		} else {
			sb.append( "[ " );
			sb.append( "{ \"id\": -100, \"pId\": -100, \"name\": \"------\", open:true }," );
			sb.append( " ];" );
		}
		return sb.toString();
	}
	
	/**
	 * 通过用户ID取得组织名称
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public String getOrgNameByUserID(String orgID) throws Exception, SQLException{
		HashMap[] map = new OrgDealDAO().getCurOrgNodeByOrgId( Long.parseLong(orgID) );
		if(map != null && map.length > 0){
			return (String)map[0].get("ORG_NAME");
		}
		return "";
	}
	
	/**
	 * 端机右键设定处理
	 * @param machineID 机器ID
	 * @param doType 操作类型
	 * @param userID userID
	 * @param content 远程接管消息
	 * @return 处理结果(alert)
	 * @throws SQLException 
	 * @throws NumberFormatException 
	 */
	public String dealOfRightSetting(String machineID, String doType, String userID, String content) {
		StringBuffer strAlert = new StringBuffer();
		String successStr = "操作已成功";
		String failStr = "操作失败";
		String newLine = "\n";
		try {
		MonitorDAO monitorDAO =	new MonitorDAO();
		
		//if (!Define.MONITOR_START_SCREEN_SHOT.equals(doType) && !Define.MONITOR_STOP_SCREEN_SHOT.equals(doType)) {
		if(!Define.MONITOR_RUNNING_INUSE.equals(doType)){
			//设置心跳
			monitorDAO.openStepOverCommand(Long.parseLong(machineID), doType);
		}
		//}
		
		// 设备信息
		if (Define.MONITOR_RUNNING_MACHINEINFO.equals(doType)) {
			HashMap[] eMap = monitorDAO.getEquipmentInfoByMachineID(Long.parseLong(machineID));
			if(null != eMap && eMap.length == 1){
				strAlert.append("设备类型 : " + StringUtils.transStr(monitorDAO.getMacTypeByMacID().get(machineID)));
				strAlert.append(newLine);
				strAlert.append("出厂日期 : " + StringUtils.transStr((String)eMap[0].get("MANUFACTURE_DATE")));
				strAlert.append(newLine);
				strAlert.append("设备厂商 : " + StringUtils.transStr((String)eMap[0].get("MANUFACTURER")));
				strAlert.append(newLine);
				strAlert.append("设备编号 : " + StringUtils.transStr((String)eMap[0].get("DEVICE_NO")));
				strAlert.append(newLine);
				String years = StringUtils.transStr((String)eMap[0].get("FREE_REPAIR_YEAR"));
				strAlert.append("保修年限 : " + years + (!"".equals(years) ? "年" : ""));
			}
			if(strAlert.length() == 0){
				strAlert.append("未查询到相关信息");
			}
		}
		// 部署信息
		else if (Define.MONITOR_RUNNING_DEPLOYINFO.equals(doType)) {
			HashMap[] eMap = monitorDAO.getDeployInfoByMachineID(Long.parseLong(machineID));
			if(null != eMap && eMap.length == 1){
				strAlert.append("所属银行 : " + StringUtils.transStr((String)eMap[0].get("ORG_NAME")));
				strAlert.append(newLine);
				strAlert.append("端机名称 : " + StringUtils.transStr((String)eMap[0].get("MACHINE_NAME")));
				strAlert.append(newLine);
				strAlert.append("网点名称 : " + StringUtils.transStr((String)eMap[0].get("BRANCH_NAME")));
				strAlert.append(newLine);
				strAlert.append("网点地址 : " + StringUtils.transStr((String)eMap[0].get("BRANCH_ADDRESS")));
				strAlert.append(newLine);
				strAlert.append("网点编号 : " + StringUtils.transStr((String)eMap[0].get("BRANCH_NO")));
				strAlert.append(newLine);
				strAlert.append("负责人  : " + StringUtils.transStr((String)eMap[0].get("MANEGER_NAME")));
				strAlert.append(newLine);
				strAlert.append("联系人  : " + StringUtils.transStr((String)eMap[0].get("CONTACT_NAME")));
				strAlert.append(newLine);
				strAlert.append("联系电话 : " + StringUtils.transStr((String)eMap[0].get("CONTACT_TELEPHONE")));
				strAlert.append(newLine);
				strAlert.append("联系手机 : " + StringUtils.transStr((String)eMap[0].get("CONTACT_CELLPHONE")));
				strAlert.append(newLine);
				strAlert.append("营业时间 : " + transHours(StringUtils.transStr((String)eMap[0].get("OPEN_TIME")) + 
						"--" + StringUtils.transStr((String)eMap[0].get("CLOSE_TIME"))));
				strAlert.append(newLine);
				strAlert.append("营业周期 : " + StringUtils.transStr((String)eMap[0].get("OPEN_DATE")));
			}
			if(strAlert.length() == 0){
				strAlert.append("未查询到相关信息");
			}
		}
		// 启用
		else if (Define.MONITOR_RUNNING_INUSE.equals(doType)) {
			//更新报停履历表-恢复时间
			int result = monitorDAO.updateMachinePauseHistory(Long.parseLong(machineID));
			if(result == 0){
				strAlert.append("该端机未停用,请重试");
				logger.debug("查询端机 [ "+machineID+" ]报停履历记录为空");
			} else {
				monitorDAO.openStepOverCommand(Long.parseLong(machineID), doType);
				strAlert.append(successStr);
				logger.debug("更新报停履历表成功");
			}
		}
		// 报停
		else if (Define.MONITOR_RUNNING_STOP.equals(doType)) {
			//记录插入报停履历表
			monitorDAO.insertMachinePauseHistory(Long.parseLong(machineID), Long.parseLong(userID));
			strAlert.append(successStr);
		}
		// 启动截屏
		else if (Define.MONITOR_START_SCREEN_SHOT.equals(doType)){
			strAlert.append(successStr);
			/*CommandDAO dao = new CommandDAO();
			String startStatus = dao.getMacStatus(machineID, "6");
			if(!"0".equals(startStatus)){
				strAlert.append("端机已启动截屏,请稍等!");
			}else{
				strAlert.append("端机启动截屏! 正在启动截屏中...");
				dao.dOper(machineID, "6");
			}*/
		}
		// 停止截屏
		else if (Define.MONITOR_STOP_SCREEN_SHOT.equals(doType)){
			
			String ip = LocalProperties.getProperty("FTP_SERVER_HOST");
			String port = LocalProperties.getProperty("FTP_SERVER_PORT");
			String path = LocalProperties.getProperty("FTP_UPLOAD_FILE_PATH_SCREENSHOT");
			String mac = (String) new MachineDAO().getMacInfo(machineID).get("MAC");
			String ftp = "ftp://" + ip + ":" + port + path +"/" + mac;
			String resText = "\r\n正在停止截屏中...\r\n\r\n稍后请到以下目录获取相应文件\r\n\r\n" + ftp;
//			String resText = "<script>if(confrim(\"正在停止截屏中,是否查看文件\")){window.location.href=\""+ftp+"\"}</script>";
			strAlert.append(resText);
			/*CommandDAO dao = new CommandDAO();
			String startStatus = dao.getMacStatus(machineID, "6");
			String endStatus = dao.getMacStatus(machineID, "7");
			if("0".equals(startStatus)){
				strAlert.append("请先启动截屏,然后停止截屏!");
			} else if(!"0".equals(endStatus)){
				strAlert.append("端机已停止截屏!");
			} else {
				dao.dOper(machineID, "7");
				strAlert.append("端机停止截屏,稍后请到FTP服务器获取相应文件! 正在停止截屏中...");
			}*/
		}
		// 远程接管
		else if(Define.MONITOR_RUNNING_REMOUTE.equals(doType)){
			// 远程接管的id
			String operType = "9";
			int result = monitorDAO.updateRemoteMenuMessage(machineID, operType, content);
			if(result != 0){
				strAlert.append(successStr);
			}else{
				strAlert.append(failStr);
			}
		}
		// 删除应用程序
		else if(Define.MONITOR_RUNNING_DELETE_APP.equals(doType)){
			// 远程接管的id
			String operType = "32";
			int result = monitorDAO.updateRemoteMenuMessage(machineID, operType, content);
			if(result != 0){
				strAlert.append(successStr);
			}else{
				strAlert.append(failStr);
			}
		}
		// 发送消息
		else if(Define.MONITOR_SEND_MESSAGE.equals(doType)){
			//发送消息的指令id
			String operType = "30";
			monitorDAO.doOper(machineID, operType, content);
			monitorDAO.insertMessageHistory(machineID,  content, userID);
		}
		// 报废
		else if(Define.MONITOR_RETIREMENT.equals(doType)){
			String mac = "";
			HashMap[] eMap = monitorDAO.getMachineMac(machineID);
			if(null != eMap && eMap.length == 1){
				// mac地址取得
				mac =StringUtils.transStr((String)eMap[0].get("MAC")) ;
			}
			if(!"".equals(mac)){
				HashMap[] map = monitorDAO.getRetirementMachine(mac);
				if(map == null || map.length == 0){
					// 在报废表中插入机器mac地址
					int result = monitorDAO.insertMachineRetirement(mac, machineID);
					if(result != 0){
						// 更新机器状态
						int res = monitorDAO.updateMachineStatus(machineID, Define.MACHINE_STATUS_RETIREMENT);
						if(res == 0){
							strAlert.append(failStr);
						}
					} else{
						strAlert.append(failStr);
					}
				}
			}else{
				strAlert.append(failStr);
			}
			
		}
		if(strAlert.length() == 0){
			strAlert.append(successStr);
		}
		
		} catch (Exception e) {
//			strAlert = new StringBuffer();
			strAlert.append(failStr);
			logger.error(e.getMessage());
		}
		
		return strAlert.toString();
	}
	
	/**
	 * 取得心跳命令显示列：{  
	                                                                  报停=0/启用=1/全不显示=2,
	                                                                  启动截屏=0/停止截屏=1/全不显示=2,
	                                                                  开始播放广告=0/停止播放广告=1/全不显示=2,
	                                                                  启动临时广告=0/停止临时广告=1/全不显示=2,
	                                                                  锁屏=0/解锁=1/全不显示=2
	                      }
	 * @param machineID
	 * @return
	 * @throws SQLException 
	 */
	public String showSingle(String machineID) throws SQLException{
		Map<String,String> map = new HashMap<String,String>();
		MonitorDAO dao = new MonitorDAO();
		HashMap[] resultMap = dao.getMacStatusList(machineID);
		if (null != resultMap && resultMap.length != 0){
			for (int i = 0; i < resultMap.length; i++) {
				Iterator it = resultMap[i].entrySet().iterator();
				String[] array = new String[2];
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					if ("COMMAND_ID".equals((String) entry.getKey())) {
						array[0]=(String)entry.getValue();
					} else if("STATUS".equals((String) entry.getKey())){
						array[1]=(String)entry.getValue();
					} 
				}
				map.put(array[0], array[1]);
			}
		}
		
		StringBuffer sb = new StringBuffer();
		
		//报停、启用flag设定
		String stopStatus = map.get("2");
		String enableStatus = map.get("3");
		if("0".equals(enableStatus) && "0".equals(stopStatus)){
			sb.append("0");
		} else if("0".equals(enableStatus) && "1".equals(stopStatus)){
			sb.append("1");
		} else if("1".equals(enableStatus) && "0".equals(stopStatus)){
			sb.append("0");
		}  else if("0".equals(enableStatus) && "2".equals(stopStatus)){
			sb.append("1");
		} else {
			//sb.append("2");
			logger.debug("端机 [ " + machineID + " ]命令状态错误");
		}
		sb.append(",");
		
		//截屏flag设定
		String startScreenshotStatus = map.get("6");
		String endScreenshotStatus = map.get("7");
		if("0".equals(endScreenshotStatus) && "0".equals(startScreenshotStatus)){
			sb.append("0");
		} else if("0".equals(endScreenshotStatus) && "1".equals(startScreenshotStatus)){
			sb.append("1");
		} else if("1".equals(endScreenshotStatus) && "0".equals(startScreenshotStatus)){
			sb.append("0");
		}  else if("0".equals(endScreenshotStatus) && "2".equals(startScreenshotStatus)){
			sb.append("1");
		} else {
			//sb.append("2");
			logger.debug("端机 [ " + machineID + " ]命令状态错误");
		}
		sb.append(",");
		
		//播放flag设定
		String startPlayStatus = map.get("10");
		String endPlayStatus = map.get("11");
		if("0".equals(endPlayStatus) && "0".equals(startPlayStatus)){
			sb.append("0");
		} else if("0".equals(endPlayStatus) && "1".equals(startPlayStatus)){
			sb.append("1");
		} else if("1".equals(endPlayStatus) && "0".equals(startPlayStatus)){
			sb.append("0");
		}  else if("0".equals(endPlayStatus) && "2".equals(startPlayStatus)){
			sb.append("1");
		} else {
			//sb.append("2");
			logger.debug("端机 [ " + machineID + " ]命令状态错误");
		}
		sb.append(",");
		
		//临时播放flag设定
		String startPlayTempStatus = map.get("12");
		String endPlayTempStatus = map.get("13");
		if("0".equals(endPlayTempStatus) && "0".equals(startPlayTempStatus)){
			sb.append("0");
		} else if("0".equals(endPlayTempStatus) && "1".equals(startPlayTempStatus)){
			sb.append("1");
		} else if("1".equals(endPlayTempStatus) && "0".equals(startPlayTempStatus)){
			sb.append("0");
		}  else if("0".equals(endPlayTempStatus) && "2".equals(startPlayTempStatus)){
			sb.append("1");
		} else {
			//sb.append("2");
			logger.debug("端机 [ " + machineID + " ]命令状态错误");
		}
		sb.append(",");
		
		//锁屏、解锁flag设定
		String lockStatus = map.get("28");
		String unlockStatus = map.get("29");
		if("0".equals(unlockStatus) && "0".equals(lockStatus)){
			sb.append("0");
		} else if("0".equals(unlockStatus) && "1".equals(lockStatus)){
			sb.append("1");
		} else if("1".equals(unlockStatus) && "0".equals(lockStatus)){
			sb.append("0");
		}  else if("0".equals(unlockStatus) && "2".equals(lockStatus)){
			sb.append("1");
		} else {
			//sb.append("2");
			logger.debug("端机 [ " + machineID + " ]命令状态错误");
		}
		
		return sb.toString();
	}
	/**
	 * @param userOrgs
	 * @return
	 * @throws SQLException
	 */
	public String buildMachineInOrg(List<HashMap> userOrgs) throws SQLException{
		HashMap orgNode = null;
		long orgId = -1;
		long parentOrgId = -1;
		String orgName = "";
		String treeStr = "";
		int org_level = 0;
		StringBuffer sb = new StringBuffer();
		List<Long> orgIDList = new ArrayList<Long>();
		List<MachineBean> lBean = new ArrayList<MachineBean>();
		List<MachineBean> machineOrgList =  new MonitorDAO().getRelationlistBetweenMachineInfoWithOrg();
		if(userOrgs!=null&&userOrgs.size()>0){
			sb.append( "[ " );
			for (int i=0;i<userOrgs.size();i++){

				orgNode = userOrgs.get(i);
				orgId = Long.parseLong( orgNode.get( "ORG_ID" ).toString() );
				parentOrgId = Long.parseLong( orgNode.get( "PARENT_ORG_ID" ).toString() );
				orgName = orgNode.get( "ORG_NAME" ).toString();
				org_level = Integer.parseInt( orgNode.get( "ORG_LEVEL" ).toString() );

				orgIDList.add(orgId);
				
				sb.append( "{ \"id\": " );
				sb.append( orgId + "," );
				sb.append( " \"pId\": " );
				sb.append( parentOrgId + "," );
				sb.append( " \"name\": " );
				sb.append( "\"" + orgName + "\"" );
				if( org_level<=1 ){
					sb.append( ", open:true " );
				}
				
				sb.append( "}" );

				if(i<userOrgs.size()-1){
					sb.append( "," );
				}
			}
			
			sb.append( "," );
			//添加组织下端机ID NAME
			int n = 0;
			for(int k = 0; k < orgIDList.size(); k++){
				orgId = orgIDList.get(k);
				lBean = getMachineByOrgID(orgId, machineOrgList);
				for(n = 0; n < lBean.size(); n++){
					sb.append( "{ \"id\": \"" );
					//为避免与组织树中id重复，在本次端机循环中将   "999999"+端机ID 作为id
					//注：组织ID最大不能超过999999,否则资源数将显示错误
					sb.append( Define.TREE_ID_PREFIX+lBean.get(n).getMachineId() + "\"," );
					sb.append( " \"pId\": " );
					sb.append( orgId + "," );
					sb.append( " \"name\": " );
					sb.append( "\"" + lBean.get(n).getMachineMark() + "\"" );
					sb.append(", \"mactype\":\"mac\"");
					sb.append( "}" );

					if(n<lBean.size()-1){
						sb.append( "," );
					}
				}
				if(n != 0 && k<orgIDList.size()-1){
					sb.append( "," );
				}
			}
			treeStr = sb.toString();
			//当组织下没有任何端机时过滤结尾逗号
			if(treeStr.endsWith(",")){
				treeStr = treeStr.substring(0, treeStr.length()-1);
			}
			treeStr += " ];" ;
			
		}
		return treeStr;
	}
	
	/**
	 * 取得机器列表中与orgID相等的机器列表
	 * @param orgID
	 * @param list
	 * @return
	 */
	private List<MachineBean> getMachineByOrgID(long orgID, List<MachineBean> list){
		List<MachineBean> beanList = new ArrayList<MachineBean>();
		for(int m = 0; m<list.size(); m++){
			if(orgID == list.get(m).getOrgID()){
				beanList.add(list.get(m));
			}
		}
		return beanList;
	}
	
	/**
	 * 营业时间替换工具类
	 * @param str
	 * @return
	 */
	private static String transHours(String str){
		if("--".equals(str)){
			str = "";
		}
		return str;
	}
	
	public HashMap[] getMonitorCommonInfoByMachineID(String machineID, String pageTime) throws NumberFormatException, SQLException{
		//取得心跳数据
		return new MonitorDAO().getMonitorCommonInfoByMachineID(Long.parseLong(machineID), pageTime);
	}
	
	public String paint(HashMap[] map, HttpServletRequest req, StringBuffer s) throws IOException{
		if (null != map && map.length != 0){
			//设置硬盘饼状图数据   
	        DefaultPieDataset pieDataset = new DefaultPieDataset();
	        
			//最新端机信息
			pieDataset.setValue("已使用",Double.parseDouble((String) map[0].get("DISK_USED")));
			pieDataset.setValue("未使用",Double.parseDouble((String) map[0].get("DISK_UNUSED")));
			
			//通过工程创建3D饼图   图标题 \数据源\是否显示图例\是否显示tooltip\是否指定url
	        JFreeChart pieChart = ChartFactory.createPieChart(null, pieDataset, false, false, false);   

	        pieChart.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);   
	        //得到饼图的Plot对象   
	        PiePlot piePlot = (PiePlot) pieChart.getPlot();   
	        setSection(piePlot);   
	        setLabel(piePlot);   
	        setNoDataMessage(piePlot);   
	        setNullAndZeroValue(piePlot);
	        
	        String pieFilename = ServletUtilities.saveChartAsPNG(pieChart, 180, 180, req.getSession(false));
//	        ChartUtilities.saveChartAsJPEG(new File("d://bing.jpeg"), pieChart, 1000, 1000);
	        
			s.append("<img src=\""
							+ req.getContextPath()
							+ "/servlet/DisplayChart"
							+ "?filename="
							+ pieFilename
							+ "\" width=180 height=180 border=0 align=\"middle\" usemap=\"#"
							+ pieFilename + "\">");
			s.append("|");
			 
			
			//CPU、内存、上行速率、下行速率画线
			buildLineChart(map, req, s);
		}
		return s.toString();
	}   
  
    private static void setSection(PiePlot pieplot) {   
        //设置扇区颜色   
        pieplot.setSectionPaint("已使用", new Color(241, 58, 58));
        pieplot.setSectionPaint("未使用", new Color(119, 216, 119));   
        //设置扇区分离显示   
        pieplot.setExplodePercent("已使用", 0.2D);
        //设置扇区边框不可见   
        pieplot.setSectionOutlinesVisible(false);   
    }   
  
    private static void setLabel(PiePlot pieplot) {
        //设置扇区标签显示格式：关键字：值(百分比)   
        pieplot.setLabelGenerator(new StandardPieSectionLabelGenerator("{2}"));   
        pieplot.setBackgroundAlpha(0);
        //设置扇区标签颜色   
        pieplot.setLabelBackgroundPaint(new Color(255, 255, 255));   
        pieplot.setLabelFont((new Font("宋体", Font.PLAIN, 12)));   
    }
  
    private static void setNoDataMessage(PiePlot pieplot) {   
        //设置没有数据时显示的信息   
        pieplot.setNoDataMessage("无数据");   
        //设置没有数据时显示的信息的字体   
        pieplot.setNoDataMessageFont(new Font("宋体", Font.BOLD, 14));   
        //设置没有数据时显示的信息的颜色   
        pieplot.setNoDataMessagePaint(Color.red);   
    }
  
    private static void setNullAndZeroValue(PiePlot piePlot) {   
        //设置是否忽略0和null值   
         piePlot.setIgnoreNullValues(true);   
         piePlot.setIgnoreZeroValues(true);   
    }
    
    private static JFreeChart setLineChartStyle(JFreeChart chart){
    	chart.setTextAntiAlias(false); 
        // 设置背景色 
    	chart.setBackgroundPaint(Color.WHITE); 
    	chart.setBorderVisible(false);
    	chart.setTextAntiAlias(false);
        
        TextTitle title = new TextTitle();
        title.visible = false;
        chart.setTitle(title);
        
        // 获得plot 
        XYPlot categoryplot = chart.getXYPlot(); 
        // x轴 分类轴网格是否可见 
        categoryplot.setDomainGridlinesVisible(false); 
        // y轴 数据轴网格是否可见 
        categoryplot.setRangeGridlinesVisible(false); 
        // 设置背景色 
        categoryplot.setBackgroundPaint(Color.WHITE); 

        ValueAxis domainAxis = categoryplot.getDomainAxis();
        domainAxis.setVisible(false);

        ValueAxis rangeAxis = categoryplot.getRangeAxis();
        rangeAxis.setVisible(false);
        
        return chart;
    }
	
	private String buildLineChart(HashMap[] map, HttpServletRequest req, StringBuffer sr) throws IOException{
		//创建XYDataset对象        
        createDataset(map);   
        //根据Dataset生成JFreeChart对象
        createChart();         
        //Servlet输出
        return saveToServlet(req, sr); 
	}
	
	private static void createDataset(HashMap[] map) {   
		cpuSeriesCollection = new XYSeriesCollection();
		memorySeriesCollection = new XYSeriesCollection();
		uploadSeriesCollection = new XYSeriesCollection();
		downloadSeriesCollection = new XYSeriesCollection();
		
		XYSeries cpuSeries = new XYSeries("");
		XYSeries memorySeries = new XYSeries("");
		XYSeries uploadSeries = new XYSeries("");
		XYSeries downloadSeries = new XYSeries("");
		
		for (int i = 0; i < map.length; i++) {
			Iterator it = map[i].entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				if ("CUP_LOAD".equals((String) entry.getKey())) {
					cpuSeries.add(i, Double.parseDouble((String) map[i].get("CUP_LOAD")));
				} else if("MEMORY_LOAD".equals((String) entry.getKey())){
					memorySeries.add(i, Double.parseDouble((String) map[i].get("MEMORY_LOAD")));
				} else if("UPLOAD_RATE".equals((String) entry.getKey())){
					uploadSeries.add(i, Double.parseDouble((String) map[i].get("UPLOAD_RATE")));
				} else if("DOWNLOAD_RATE".equals((String) entry.getKey())){
					downloadSeries.add(i, Double.parseDouble((String) map[i].get("DOWNLOAD_RATE")));
				} 
			}
		}
		cpuSeriesCollection.addSeries(cpuSeries);
		memorySeriesCollection.addSeries(memorySeries);
		uploadSeriesCollection.addSeries(uploadSeries);
		downloadSeriesCollection.addSeries(downloadSeries);
    }
	
	private static void createChart() {
		//CPU
		//图表标题 \目录轴的显示标签\数值轴的显示标签  \数据集\图表方向：水平 垂直\是否显示图例\是否生成工具\是否生成URL链接
		cpuChart = ChartFactory.createXYLineChart(null, "", "",
				cpuSeriesCollection, PlotOrientation.VERTICAL, false, false, false);
		cpuChart = setLineChartStyle(cpuChart);
        
        //内存
		memoryChart = ChartFactory.createXYLineChart("", "", "",
				memorySeriesCollection, PlotOrientation.VERTICAL, false, false, false);
		memoryChart = setLineChartStyle(memoryChart);
        
        //上行速率
        uploadChart = ChartFactory.createXYLineChart("", "", "",
				uploadSeriesCollection, PlotOrientation.VERTICAL, false, false, false);
        uploadChart = setLineChartStyle(uploadChart);
        
        //下行速率
        downloadChart = ChartFactory.createXYLineChart("", "", "",
        		downloadSeriesCollection, PlotOrientation.VERTICAL, false, false, false);
        downloadChart = setLineChartStyle(downloadChart);
        
        
    }
	
    private static String saveToServlet(HttpServletRequest req, StringBuffer sr) throws IOException {
        //580是图片长度，73是图片高度 
    	
    	String cpuFileName = ServletUtilities.saveChartAsPNG(cpuChart, 580, 73, req.getSession(false));
    	//ChartUtilities.saveChartAsJPEG(new File("d://cpu.jpeg"), cpuChart, 1000, 1000);
        sr.append("<img src=\""
				+ req.getContextPath() +"/servlet/DisplayChart"
				+ "?filename="
						+ cpuFileName
						+ "\" width=580 height=73 border=0 align=\"middle\" usemap=\"#"+cpuFileName+"\">");
        sr.append("|");
        
        String memoryFileName = ServletUtilities.saveChartAsPNG(memoryChart, 580, 73, req.getSession(false));
        sr.append("<img src=\""
				+ req.getContextPath() +"/servlet/DisplayChart"
				+ "?filename="
						+ memoryFileName
						+ "\" width=580 height=73 border=0 align=\"middle\" usemap=\"#"+memoryFileName+"\">");
        sr.append("|");
        
        String uploadFileName = ServletUtilities.saveChartAsPNG(uploadChart, 380, 73, req.getSession(false));
        sr.append("<img src=\""
				+ req.getContextPath() +"/servlet/DisplayChart"
				+ "?filename="
						+ uploadFileName
						+ "\" width=380 height=73 border=0 align=\"middle\" usemap=\"#"+uploadFileName+"\">");
        sr.append("|");
        
        String downloadFileName = ServletUtilities.saveChartAsPNG(downloadChart, 380, 73, req.getSession(false));
        sr.append("<img src=\""
				+ req.getContextPath() +"/servlet/DisplayChart"
				+ "?filename="
						+ downloadFileName
						+ "\" width=380 height=73 border=0 align=\"middle\" usemap=\"#"+downloadFileName+"\">");
        
        return sr.toString();
    }
    
    /**
     * @param req
     * @param path
     * @param machineID
     * @throws Exception
     */
    public void searchProcess(HttpServletRequest req, String path, String machineID) throws Exception {
    	MachineProcessBean bean = new MachineProcessBean();
		
		//检索结果当前位置
		if(null != req.getParameter("point_num")){
			bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		} else {
			bean.setPoint_num(1);
		}
		
		List<MachineProcessBean> resultBeans = new ArrayList<MachineProcessBean>();
		//进程list
		List<MachineProcessBean> processList = getProcessFTPFile(path, machineID);
		if (processList != null) {
			MonitorDAO monitorDAO = new MonitorDAO();
			if(!monitorDAO.isExistLegalOperation(machineID, "1")){
				monitorDAO.insertLegalProcessList(processList, machineID);
			}
			
			//检索结果开始位置
			int start_p  = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			//检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;
			
			int loop = 0;
			for (MachineProcessBean processBean : processList) {
				loop++;
				if( loop <= start_p ) {
                    continue;
                }
				if( loop > start_p && loop <= end_p ) {
					start_p++;
					MachineProcessBean proBean = new MachineProcessBean();
					//进程名称
					proBean.setName(processBean.getName());
					//进程类型
					proBean.setType(processBean.getType());
					//进程ID
					proBean.setId(processBean.getId());
					
					resultBeans.add(proBean);
				}
			}
			//检索总行数
			bean.setTotal_num(processList.size());
		}
		//检索条件保存
		req.setAttribute("select_object", bean);
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
	
    
    /**
     * @param req
     * @param path
     * @param machineID
     * @throws Exception
     */
    public void searchService(HttpServletRequest req, String path, String machineID) throws Exception {
    	MachineServiceBean bean = new MachineServiceBean();
		
		//检索结果当前位置
		if(null != req.getParameter("point_num")){
			bean.setPoint_num(Integer.parseInt(req.getParameter("point_num")));
		} else {
			bean.setPoint_num(1);
		}
		
		List<MachineServiceBean> resultBeans = new ArrayList<MachineServiceBean>();
		//进程list
		List<MachineServiceBean> serviceList = getServiceFTPFile(path, machineID);
		if (serviceList != null) {
			MonitorDAO monitorDAO = new MonitorDAO();
			if(!monitorDAO.isExistLegalOperation(machineID, "2")){
				monitorDAO.insertLegalServiceList(serviceList, machineID);
			}
			//检索结果开始位置
			int start_p  = (bean.getPoint_num() - 1) * Define.VIEW_NUM;
			if (start_p < 0) {
				start_p = 0;
			}
			//检索结果结束位置
			int end_p = start_p + Define.VIEW_NUM;
			
			int loop = 0;
			for (MachineServiceBean sBean : serviceList) {
				loop++;
				if( loop <= start_p ) {
                    continue;
                }
				if( loop > start_p && loop <= end_p ) {
					start_p++;
					MachineServiceBean serviceBean = new MachineServiceBean();
					//服务名称
					serviceBean.setName(sBean.getName());
					//显示名称
					serviceBean.setDisplayName(sBean.getDisplayName());
					//可执行文件路径
					serviceBean.setFilePath(sBean.getFilePath());
					
					resultBeans.add(serviceBean);
				}
			}
			//检索总行数
			bean.setTotal_num(serviceList.size());
		}
		//检索条件保存
		req.setAttribute("select_object", bean);
		//检索结果保存
		req.setAttribute("resultList", resultBeans);
	}
    
    /**
     * @param path
     * @param machineID
     * @param req
     * @throws SQLException
     * @throws IOException 
     */
    private List<MachineProcessBean> getProcessFTPFile(String path, String machineID) throws SQLException, IOException{
    	MonitorDAO monitorDAO = new MonitorDAO();
    	List<String> legalProcessList = new ArrayList<String>();
    	//取得系统合法进程
    	HashMap[] maps = monitorDAO.queryLegalOperation(machineID, "1");
    	for (HashMap pMap : maps){
    		legalProcessList.add((String)pMap.get("OPERATION_NAME"));
    	}
    	
    	//由FTP读取端机进程列表
    	FTPFile[] files = FtpUtils.getFiles(path);
    	String mac = new MachineDAO().getMacInfo(machineID).get("MAC").toString();
    	String fileName = "";
    	
    	for (FTPFile ftpFile : files) {
			if(ftpFile.getName().startsWith(mac)){
	    		fileName = ftpFile.getName();
	    		break;
	    	}
		}
    	
    	if("".equals(fileName)){
    		logger.info("FTP服务器中未找到MAC地址为[" + mac + "]的文件");
    		return null;
    	}
    	
    	List<MachineProcessBean> listProcess = new ArrayList<MachineProcessBean>();
    	
        InputStream in = null;
    	FTPClient ftpClient = FtpUtils.getFTPClient();
    	ftpClient.changeWorkingDirectory(path);
    	in = ftpClient.retrieveFileStream(fileName);
    	
		if (in != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String data = null;
			String[] attrs;
			
			try {
				while ((data = br.readLine()) != null) {
					//build process
					MachineProcessBean processBean = new MachineProcessBean();
					attrs = data.split(",");
					if(attrs.length == 2){
						processBean.setId(attrs[0]);
						processBean.setName(attrs[1]);
						if(!legalProcessList.contains(attrs[1])){
							processBean.setType("异常进程");
						}
					} else {
						logger.info("端机 [" + mac + "] 进程信息异常 : "+ data);
					}
					listProcess.add(processBean);
				}
			} catch (IOException e) {
				logger.error("文件读取错误。");
				e.printStackTrace();
			} finally {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.error("InputStream为空，不能读取。");
		}
		return listProcess;
    }
    
    /**
     * @param path
     * @param machineID
     * @param req
     * @throws SQLException
     * @throws IOException 
     */
    private List<MachineServiceBean> getServiceFTPFile(String path, String machineID) throws SQLException, IOException{
    	FTPFile[] files = FtpUtils.getFiles(path);
    	String mac = new MachineDAO().getMacInfo(machineID).get("MAC").toString();
    	String fileName = "";
    	
    	for (FTPFile ftpFile : files) {
			if(ftpFile.getName().startsWith(mac)){
	    		fileName = ftpFile.getName();
	    		break;
	    	}
		}
    	
    	if("".equals(fileName)){
    		logger.error("FTP服务器中未找到MAC地址为[" + mac + "]的文件");
    		return null;
    	}
    	
		List<MachineServiceBean> listService = new ArrayList<MachineServiceBean>();
    	
        InputStream in = null;
    	FTPClient ftpClient = FtpUtils.getFTPClient();
    	ftpClient.changeWorkingDirectory(path);
    	in = ftpClient.retrieveFileStream(fileName);
    	
		if (in != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String data = null;
			String[] attrs;
			
			try {
				while ((data = br.readLine()) != null) {
					//build service
					MachineServiceBean serviceBean = new MachineServiceBean();
					attrs = data.split(",");
					if(attrs.length == 3){
						serviceBean.setName(attrs[0]);
						serviceBean.setDisplayName(attrs[1]);
						serviceBean.setFilePath(attrs[2]);
					} else {
						logger.info("端机 [" + mac + "] 服务信息异常 : "+ data);
					}
					listService.add(serviceBean);
				}
			} catch (IOException e) {
				logger.error("文件读取错误。");
				e.printStackTrace();
			} finally {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.error("InputStream为空，不能读取。");
		}
		return listService;
    }
    
    public int shutdownProcess(String machineID, String processName) throws SQLException, UnsupportedEncodingException{
    	MonitorDAO monitorDAO = new MonitorDAO();
    	HashMap[] map = monitorDAO.queryShutdownCommonContent(machineID);
    	String content = null;
    	if(null != map && map.length==1){
    		content = (String)map[0].get("COMMAND_CONTENT");
    		if(!"".equals(StringUtils.transStr(content))){
    			content += "," +  processName;
    		} else {
    			content = processName;
    		}
    	} else {
    		logger.info("端机[ID="+ machineID+"] 在心跳管理表中查询记录为空");
    	}
    	return monitorDAO.updateCommonMangement(machineID, 14, content, "1");
    }
	/**
	 * 获取特定终端的警报类型
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public List getAlertType(String orgID) throws Exception, SQLException{
		HashMap[] maps = new MonitorDAO().getAlertType( Long.parseLong(orgID) );
		
		List  list  = new ArrayList();
	    if(maps!=null&&maps.length>0){
	    	for(int i = 0; i< maps.length ; i++){
	    		HashMap map = maps[i];
	    		String alertId = (String)map.get("ALERT_ID");
	    		String alertType = (String)map.get("ALERT_TYPE");
	    		list.add(alertId+"@@"+alertType);
	    	}
	    }
	    if(list!=null&&list.size()>0){
	    	return list;
	    }
		return list;
	}
	
	/**
	 * 获取特定终端的警报类型
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public List getAlertManagement() throws Exception, SQLException{
		HashMap[] maps = new MonitorDAO().getAlertManagement();
		
		List  list  = new ArrayList();
	    if(maps!=null&&maps.length>0){
	    	for(int i = 0; i< maps.length ; i++){
	    		HashMap map = maps[i];
	    		String alertId = (String)map.get("ALERT_ID");
	    		String alertName = (String)map.get("ALERT_NAME");
	    		list.add(alertId+"@@"+alertName);
	    	}
	    }
	    if(list!=null&&list.size()>0){
	    	return list;
	    }
		return list;
	}
	
	
	/**
	 * 获取特定终端的警报类型
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public int[] insertAlertType(List list) throws Exception, SQLException{
		
		int[] values = new MonitorDAO().insertAlertTpye(list);
		
		return values;

	}
	
	/**
	 * 获取特定终端的警报类型
	 * @param orgID
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public void deleteAlertTpye(long orgId) throws Exception, SQLException{
		
		new MonitorDAO().deleteAlertTpye(orgId);
		

	}
	
	/** 取得所有已设定邮件报警的组织及上级组织下用户的邮箱、端机信息
	 * @return
	 * @throws SQLException
	 */
	public List<AlarmBean> getAlarmListInfo() throws SQLException{
		MonitorDAO monitorDAO = new MonitorDAO();
		HashMap[] resultMap = monitorDAO.getAlarmInfo();
		
		List<AlarmBean> alarmBean = new ArrayList<AlarmBean>();
		if (null != resultMap && resultMap.length != 0){
			for (int i = 0; i < resultMap.length; i++) {
				Iterator it = resultMap[i].entrySet().iterator();
				AlarmBean bean = new AlarmBean();
				
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					if ("ORG_ID".equals((String) entry.getKey())) {
						bean.setOrgID((String)entry.getValue());
					} else if("NAME".equals((String) entry.getKey())){
						bean.setUserName((String)entry.getValue());
					} else if ("EMAIL".equals((String) entry.getKey())) {
						bean.setEmail((String)entry.getValue());
					} else if("ALERT_ID".equals((String) entry.getKey())){
						bean.setAlertID((String)entry.getValue());
					} else if("ALERT_NAME".equals((String) entry.getKey())){
						bean.setAlertName((String)entry.getValue());
					} else if("MACHINE_ID".equals((String) entry.getKey())){
						bean.setMachineID((String)entry.getValue());
					} else if("MACHINE_MARK".equals((String) entry.getKey())){
						bean.setMachineMark((String)entry.getValue());
					}
				}
				
				alarmBean.add(bean);
			}
		}
		return alarmBean;
	}
	
    /**
     * 判断端机是否有非法进程
     * @param path
     * @param machineID
     * @return true-非法   false-合法
     * @throws SQLException
     * @throws IOException
     */
    public boolean isUnauthProcess(String path, String machineID) throws SQLException, IOException{
    	boolean result = false;
    	MonitorDAO monitorDAO = new MonitorDAO();
    	List<String> legalProcessList = new ArrayList<String>();
    	//取得系统合法进程
    	HashMap[] maps = monitorDAO.queryLegalOperation(machineID, "1");
    	for (HashMap pMap : maps){
    		legalProcessList.add((String)pMap.get("OPERATION_NAME"));
    	}
    	
    	if(legalProcessList.isEmpty()){
    		logger.info("合法进程表中未找到MAC ID为[" + machineID + "]的进程");
    		return result;
    	}
    	
    	//由FTP读取端机进程列表
    	FTPFile[] files = FtpUtils.getFiles(path);
    	String mac = new MachineDAO().getMacInfo(machineID).get("MAC").toString();
    	String fileName = "";
    	
    	for (FTPFile ftpFile : files) {
			if(ftpFile.getName().startsWith(mac)){
	    		fileName = ftpFile.getName();
	    		break;
	    	}
		}
    	
    	if("".equals(fileName)){
    		logger.info("FTP服务器中未找到MAC地址为[" + mac + "]的文件");
    		return result;
    	}
    	
    	
        InputStream in = null;
    	FTPClient ftpClient = FtpUtils.getFTPClient();
    	ftpClient.changeWorkingDirectory(path);
    	in = ftpClient.retrieveFileStream(fileName);
    	
		if (in != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String data = null;
			String[] attrs;
			
			try {
				while ((data = br.readLine()) != null) {
					//build process
					attrs = data.split(",");
					if(attrs.length == 2){
						if(!legalProcessList.contains(attrs[1])){
							result = true;
							break;
						}
					} else {
						logger.info("端机 [" + mac + "] 进程信息异常 : "+ data);
					}
				}
			} catch (IOException e) {
				logger.error("文件读取错误。");
				e.printStackTrace();
			} finally {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.error("InputStream为空，不能读取。");
		}
		return result;
    }
    
    public boolean isUnauthService(String path, String machineID) throws SQLException, IOException{
    	boolean result = false;
    	MonitorDAO monitorDAO = new MonitorDAO();
    	List<String> legalServiceList = new ArrayList<String>();
    	//取得系统合法服务
    	HashMap[] maps = monitorDAO.queryLegalOperation(machineID, "2");
    	for (HashMap pMap : maps){
    		legalServiceList.add((String)pMap.get("OPERATION_NAME"));
    	}
    	
    	if(legalServiceList.isEmpty()){
    		logger.info("合法服务表中未找到MAC ID为[" + machineID + "]的服务");
    		return result;
    	}
    	
    	FTPFile[] files = FtpUtils.getFiles(path);
    	String mac = new MachineDAO().getMacInfo(machineID).get("MAC").toString();
    	String fileName = "";
    	
    	for (FTPFile ftpFile : files) {
			if(ftpFile.getName().startsWith(mac)){
	    		fileName = ftpFile.getName();
	    		break;
	    	}
		}
    	
    	if("".equals(fileName)){
    		logger.error("FTP服务器中未找到MAC地址为[" + mac + "]的文件");
    		return result;
    	}
    	
    	
        InputStream in = null;
    	FTPClient ftpClient = FtpUtils.getFTPClient();
    	ftpClient.changeWorkingDirectory(path);
    	in = ftpClient.retrieveFileStream(fileName);
    	
		if (in != null) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String data = null;
			String[] attrs;
			
			try {
				while ((data = br.readLine()) != null) {
					attrs = data.split(",");
					if(attrs.length == 3){
						if(!legalServiceList.contains(attrs[0])){
							result = true;
							break;
						}
					} else {
						logger.info("端机 [" + mac + "] 服务信息异常 : "+ data);
					}
				}
			} catch (IOException e) {
				logger.error("文件读取错误。");
				e.printStackTrace();
			} finally {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			logger.error("InputStream为空，不能读取。");
		}
		return result;
    }
    
    public boolean isGreaterCommonVal(String fieldName, String machineID) throws Exception, SQLException{
		boolean result = false;
    	HashMap[] maps = new MonitorDAO().getMaxValue(fieldName, machineID);
		
	    if(null != maps && maps.length>0){
	    	String val = (String)maps[0].get("MAXVAL");
	    	if( null != val && !"".equals(val)){
	    		double valDouble = Double.parseDouble(val);
	    		//cpu负荷过高
	    		if(fieldName.equals(Define.ALERT_CPU_FIELD_NAME)){
	    			if(valDouble > Double.parseDouble(LocalProperties.getProperty("ALERT_FHGG_CPU_NUM"))){
	    				result = true;
	    			}
	    		} else if(fieldName.equals(Define.ALERT_MEMORY_FIELD_NAME)){
	    			//内存负荷过高
	    			if(valDouble > Double.parseDouble(LocalProperties.getProperty("ALERT_FHGG_MEMORY_NUM"))){
	    				result = true;
	    			}
	    		} else if(fieldName.equals(Define.ALERT_DISK_UNUSED_FIELD_NAME)){
	    			//硬盘容量不足
	    			if(valDouble < Double.parseDouble(LocalProperties.getProperty("ALERT_RLBZ_HARD_NUM"))){
	    				result = true;
	    			}
	    		} else if(fieldName.equals(Define.ALERT_UPLOAD_RATE_FIELD_NAME)){
	    			//上行速率过慢
	    			if(valDouble < Double.parseDouble(LocalProperties.getProperty("ALERT_SDGM_UP_NUM"))){
	    				result = true;
	    			}
	    		} else if(fieldName.equals(Define.ALERT_DOWNLOAD_RATE_FIELD_NAME)){
	    			//下行速率过慢
	    			if(valDouble < Double.parseDouble(LocalProperties.getProperty("ALERT_SDGM_DOWN_NUM"))){
	    				result = true;
	    			}
	    		}
	    	}
	    }
	    return result;
	}
    
    public boolean isMachineBreak(String machineID) throws LocalPropertiesException, SQLException{
    	List<Integer> machineIDAray = new ArrayList<Integer>();
    	machineIDAray.add(Integer.parseInt(machineID));
    	
    	HashMap[] breakMap = new MonitorDAO().getBreakMachineInfoByOrderType(machineIDAray, LocalProperties.getProperty("MONITOR_BREAK_TIME"), null);
		if (null != breakMap && breakMap.length > 0){
			return true;
		} else {
			return false;
		}
		
    }
}
