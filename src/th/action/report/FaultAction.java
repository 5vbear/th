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
import th.dao.FaultAO;
import th.dao.UKeyDAO;
import th.entity.FaultBean;

/**
 * UKey统计处理类
 * 
 * @version 2013-9-11
 * @author PSET
 * @since JDK1.6
 * 
 */
public class FaultAction extends BaseAction {

	public void readFile2DB() {

	}

	@SuppressWarnings("rawtypes")
	public ArrayList<FaultBean> getSummaryReport( String selectedOrgId, String startTime, String endTime, String macMark ) throws Exception {
		ArrayList<FaultBean> faultList = new ArrayList<FaultBean>();
		
		// 取得当前角色及以下银行资源树			
		HashMap[] resultMap = new FaultAO().getFaultInfoByOrgID(Integer.parseInt(selectedOrgId), startTime, endTime, macMark);
		if (null != resultMap && resultMap.length != 0){
			for (int i = 0; i < resultMap.length; i++) {
				Iterator it = resultMap[i].entrySet().iterator();
				FaultBean bean = new FaultBean();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					if("ORG_NAME".equals((String) entry.getKey())){
						bean.setOrgName((String) entry.getValue());
					} else if("MACHINE_MARK".equals((String) entry.getKey())){
						bean.setMachineMark((String) entry.getValue());
					} else if("FAULT_NUM".equals((String) entry.getKey())){
						bean.setFaultNum((String) entry.getValue());
					} else if("FAULT_TYPE".equals((String) entry.getKey())){
						bean.setFaultType((String) entry.getValue());
					}
				}
				faultList.add(bean);
			}
		}
		
		return faultList;

	}
	
}
