/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import th.action.BaseAction;
import th.dao.ClientDAO;
import th.dao.DeployDAO;
import th.dao.MonitorDAO;
import th.entity.MachineBean;
import th.util.StringUtils;

/**
 * 端机部署信息统计处理类
 * 
 * @version 2013-9-11
 * @author PSET
 * @since JDK1.6
 * 
 */
public class DeployAction extends BaseAction {

	public void readFile2DB() {

	}

	@SuppressWarnings("rawtypes")
	public ArrayList<MachineBean> getDeployInfo( String selectedOrgId, String startTime, String endTime, String macMark, String macType ) throws Exception {
		ArrayList<MachineBean> deployList = new ArrayList<MachineBean>();
		
		HashMap<String, String> typeMap = new ClientDAO().getMacType();
		String macTypeTemp = "";
		String osTemp = "";
		
		HashMap[] resultMap = new DeployDAO().getDeployInfoByInfo(Integer.parseInt(selectedOrgId), startTime, endTime, macMark, macType);
		if (null != resultMap && resultMap.length != 0){
			for (int i = 0; i < resultMap.length; i++) {
				Iterator it = resultMap[i].entrySet().iterator();
				MachineBean bean = new MachineBean();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					if("ORG_NAME".equals((String) entry.getKey())){
						bean.setOrgName((String) entry.getValue());
					} else if("MACHINE_MARK".equals((String) entry.getKey())){
						bean.setMachineMark((String) entry.getValue());
					} else if("MACHINE_ID".equals((String) entry.getKey())){
						bean.setMachineId(Long.parseLong((String) entry.getValue()));
					} else if("BRANCH_NAME".equals((String) entry.getKey())){
						bean.setBranchName(StringUtils.transStr((String) entry.getValue()));
					} else if("MANEGER_NAME".equals((String) entry.getKey())){
						bean.setManagerName(StringUtils.transStr((String) entry.getValue()));
					} else if("CONTACT_TELEPHONE".equals((String) entry.getKey())){
						bean.setContactTel(StringUtils.transStr((String) entry.getValue()));
					} else if("BRANCH_ADDRESS".equals((String) entry.getKey())){
						bean.setBranchAddress(StringUtils.transStr((String) entry.getValue()));
					} else if("BRANCH_NO".equals((String) entry.getKey())){
						bean.setBranchNumber(StringUtils.transStr((String) entry.getValue()));
					} else if("FREE_REPAIR_YEAR".equals((String) entry.getKey())){
						bean.setFreeRepairYear(StringUtils.transStr((String) entry.getValue()));
					} else if("CONTACT_NAME".equals((String) entry.getKey())){
						bean.setContactName(StringUtils.transStr((String) entry.getValue()));
					} else if("CONTACT_CELLPHONE".equals((String) entry.getKey())){
						bean.setContactMobile(StringUtils.transStr((String) entry.getValue()));
					} else if("WORK_TIME".equals((String) entry.getKey())){
						bean.setWorkTime(StringUtils.transStr((String) entry.getValue()));
					} else if("OPEN_DATE".equals((String) entry.getKey())){
						bean.setOpenDate(StringUtils.transStr((String) entry.getValue()));
					} else if("LON".equals((String) entry.getKey())){
						bean.setLongitude(StringUtils.transStr((String) entry.getValue()));
					} else if("LAT".equals((String) entry.getKey())){
						bean.setLatitude(StringUtils.transStr((String) entry.getValue()));
					} else if("REG_TIME".equals((String) entry.getKey())){
						bean.setRegTime(StringUtils.transStr((String) entry.getValue()));
					} else if("CONTACT_EMALL".equals((String) entry.getKey())){
						bean.setContactEmail(StringUtils.transStr((String) entry.getValue()));
					}
				}
				osTemp = (String)resultMap[i].get("OS");
				if(-1 != osTemp.indexOf("Win")){
					osTemp = "Win";
				}
				macTypeTemp = osTemp + "_" + (String)resultMap[i].get("MACHINE_KIND");
				bean.setMacType(typeMap.get(macTypeTemp));
				deployList.add(bean);
			}
		}
		
		return deployList;

	}
	
}
