/**
 *  Copyright(C) 2012 Pioneer Electronics Co., Ltd.
 *  All Right Reserved.
 */
package th.action.report;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import th.action.BaseAction;
import th.com.util.Define4Report;
import th.dao.MonitorDAO;
import th.dao.ReportDAO;
import th.dao.UKeyDAO;
import th.entity.AdvertPlayBean;
import th.entity.AdvertPlayDetailBean;
import th.entity.MachineBean;
import th.entity.UKeyBean;

/**
 * UKey统计处理类
 * 
 * @version 2013-9-11
 * @author PSET
 * @since JDK1.6
 * 
 */
public class UkeyAction extends BaseAction {

	public void readFile2DB() {

	}

	@SuppressWarnings("rawtypes")
	public ArrayList<UKeyBean> getSummaryReport( String selectedOrgId, String startTime, String endTime, String macMark, String useNum ) throws Exception {
		ArrayList<UKeyBean> ukeyList = new ArrayList<UKeyBean>();
		
		// 取得当前角色及以下银行资源树			
		HashMap[] resultMap = new UKeyDAO().getUKeyInfoByOrgID(Integer.parseInt(selectedOrgId), startTime, endTime, macMark, useNum);
		if (null != resultMap && resultMap.length != 0){
			//取得所在组织的端机编号和端机名称
			for (int i = 0; i < resultMap.length; i++) {
				Iterator it = resultMap[i].entrySet().iterator();
				UKeyBean bean = new UKeyBean();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					if("ORG_NAME".equals((String) entry.getKey())){
						bean.setOrgName((String) entry.getValue());
					} else if("MACHINE_MARK".equals((String) entry.getKey())){
						bean.setMachineMark((String) entry.getValue());
					} else if("KEY_NUM".equals((String) entry.getKey())){
						bean.setUseNum((String) entry.getValue());
					} else if("USE_TIME".equals((String) entry.getKey())){
						bean.setLatestTime((String) entry.getValue());
					}
				}
				ukeyList.add(bean);
			}
		}
		
		return ukeyList;

	}
	
}
